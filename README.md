# book-social

## 🧪 Run Test Coverage

Để chạy toàn bộ unit test và tạo báo cáo độ phủ (code coverage):

```bash
  ./mvnw test jacoco:report
```

## 📊 View Coverage Report

Sau khi chạy xong, báo cáo coverage được sinh ra tại:

```bash
  target/site
```

Mở file index.html trong trình duyệt để xem chi tiết:

```bash
  open target/site/index.html
```

(hoặc mở thủ công trong thư mục target/site → chọn file index.html)
