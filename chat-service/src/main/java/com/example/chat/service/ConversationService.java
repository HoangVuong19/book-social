package com.example.chat.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import org.springframework.stereotype.Service;

import com.example.chat.dto.request.ConversationRequest;
import com.example.chat.dto.response.ConversationResponse;
import com.example.chat.entity.Conversation;
import com.example.chat.entity.ParticipantInfo;
import com.example.chat.exception.UncategorizedException;
import com.example.chat.mapper.ConversationMapper;
import com.example.chat.repository.ConversationRepository;
import com.example.chat.repository.httpclient.ProfileClient;
import com.example.chat.utils.UserContext;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConversationService {
    ConversationRepository conversationRepository;
    ProfileClient profileClient;

    ConversationMapper conversationMapper;
    UserContext userContext;

    public List<ConversationResponse> myConversations() {
        String userId = userContext.getCurrentUsername();
        List<Conversation> conversations = conversationRepository.findAllByParticipantIdsContains(userId);

        return conversations.stream().map(this::toConversationResponse).toList();
    }

    public ConversationResponse create(ConversationRequest request) {
        // Fetch user infos
        String userId = userContext.getCurrentUsername();
        var userInfoResponse = profileClient.getProfile(userId);
        var participantInfoResponse =
                profileClient.getProfile(request.participantIds().get(0));

        if (Objects.isNull(userInfoResponse) || Objects.isNull(participantInfoResponse)) {
            throw new UncategorizedException("Uncategorized error");
        }

        var userInfo = userInfoResponse.getData();
        var participantInfo = participantInfoResponse.getData();

        List<String> userIds = new ArrayList<>();
        userIds.add(userId);
        userIds.add(participantInfo.userId());

        var sortedIds = userIds.stream().sorted().toList();
        String userIdHash = generateParticipantHash(sortedIds);

        var conversation = conversationRepository
                .findByParticipantsHash(userIdHash)
                .orElseGet(() -> {
                    List<ParticipantInfo> participantInfos = List.of(
                            ParticipantInfo.builder()
                                    .userId(userInfo.userId())
                                    .username(userInfo.username())
                                    .firstName(userInfo.firstName())
                                    .lastName(userInfo.lastName())
                                    .avatar(userInfo.avatar())
                                    .build(),
                            ParticipantInfo.builder()
                                    .userId(participantInfo.userId())
                                    .username(participantInfo.username())
                                    .firstName(participantInfo.firstName())
                                    .lastName(participantInfo.lastName())
                                    .avatar(participantInfo.avatar())
                                    .build());

                    // Build conversation info
                    Conversation newConversation = Conversation.builder()
                            .type(request.type())
                            .participantsHash(userIdHash)
                            .createdDate(Instant.now())
                            .modifiedDate(Instant.now())
                            .participants(participantInfos)
                            .build();

                    return conversationRepository.save(newConversation);
                });

        return toConversationResponse(conversation);
    }

    private String generateParticipantHash(List<String> ids) {
        StringJoiner stringJoiner = new StringJoiner("_");
        ids.forEach(stringJoiner::add);
        return stringJoiner.toString();
    }

    private ConversationResponse toConversationResponse(Conversation conversation) {
        String currentUserId = userContext.getCurrentUsername();

        ConversationResponse base = conversationMapper.toConversationResponse(conversation);

        return conversation.getParticipants().stream()
                .filter(participant -> !participant.getUserId().equals(currentUserId))
                .findFirst()
                .map(participant -> new ConversationResponse(
                        base.id(),
                        base.type(),
                        base.participantsHash(),
                        participant.getAvatar(),
                        participant.getUsername(),
                        base.participants(),
                        base.createdDate(),
                        base.modifiedDate()))
                .orElse(base);
    }
}
