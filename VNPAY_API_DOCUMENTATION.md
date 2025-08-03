# VNPay API Documentation

## Tổng quan

API tích hợp thanh toán VNPay sandbox cho ứng dụng Spring Boot.

## Cấu hình

- **Terminal ID**: U9EDAY3T
- **Secret Key**: 7CCIFQY9GTNNZR72HCS5MAZXWWI5L5H8
- **URL Sandbox**: https://sandbox.vnpayment.vn/paymentv2/vpcpay.html

## API Endpoints

### 1. Tạo URL thanh toán

**POST** `/api/payment/create-payment`

**Request Body:**

```json
{
  "amount": 100000,
  "orderInfo": "Thanh toan don hang",
  "orderId": "ORDER_123456",
  "bankCode": "NCB"
}
```

**Response:**

```json
{
  "paymentUrl": "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?...",
  "orderId": "ORDER_123456",
  "amount": 100000,
  "message": "Tạo URL thanh toán thành công",
  "success": true
}
```

### 2. Xử lý kết quả thanh toán (complete payment)

**POST** `/api/payment/complete`

**Request Body:**

```json
{
  "amount": 100000
}
```

**Response:**

```json
{
  "message": "Complete payment successful"
}
```

## Mã ngân hàng hỗ trợ (bankCode)

- `NCB` - Ngân hàng NCB

## 🧪 Thông tin thẻ test (Sandbox)

| Trường             | Giá trị               |
|--------------------|-----------------------|
| **Ngân hàng**      | NCB                   |
| **Số thẻ**         | `9704198526191432198` |
| **Tên chủ thẻ**    | `NGUYEN VAN A`        |
| **Ngày phát hành** | `07/15`               |
| **Mật khẩu OTP**   | `123456`              |

## Mã lỗi (Response Code)

- `00` - Giao dịch thành công
- `07` - Trừ tiền thành công. Giao dịch bị nghi ngờ (liên quan tới lừa đảo, giao dịch bất thường)
- `09` - Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng chưa đăng ký dịch vụ InternetBanking (Ngân hàng
  chưa hỗ trợ giao dịch trực tuyến)
- `13` - Giao dịch không thành công do Quý khách nhập sai mật khẩu xác thực giao dịch (OTP). Xin quý khách vui lòng thực
  hiện lại giao dịch
- `24` - Giao dịch không thành công do Quý khách hủy giao dịch
- `51` - Giao dịch không thành công do: Tài khoản của quý khách không đủ số dư để thực hiện giao dịch
- `65` - Giao dịch không thành công do: Tài khoản của Quý khách đã vượt quá hạn mức cho phép
- `75` - Ngân hàng thanh toán đang bảo trì
- `79` - Giao dịch không thành công do: Quý khách nhập sai mật khẩu thanh toán quốc tế
- `99` - Các lỗi khác (lỗi còn lại, không có trong danh sách mã lỗi đã liệt kê)

## Lưu ý

1. Số tiền trong request phải nhân với 100 (VNPay yêu cầu)