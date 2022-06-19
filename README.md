# Java_IS216.M21_21
## Đồ án xây dựng hệ thống quản lý phòng khám chữa bệnh
### 1. Mục tiêu đồ án
#### Đây là project của môn học Xây dựng hệ thống quản lý phòng khám chữa bệnh - UIT. Nội dung là tạo một trang web quản lý phòng khám chữa bệnh
#### Trang web phải bảo đảm các mục tiêu
##### - Quản lý hồ sơ bệnh nhân đến khám
##### - Quản lý thông tin nhân viên
##### - Quản lý thông tin thuốc
##### - Quản lý thông tin hóa đơn
##### - Quản lý thông tin phòng
##### - Thông báo thuốc gần hết, đưa ra thông báo nhập thêm hàng
##### - Sao lưu, phục hồi dữ liệu khi gặp sự cố
##### - Thống kê doanh số đạt được theo từng loại thuốc, doanh thu theo từng tháng, năm
### 2. Các thành viên tham gia project
[Thành viên tham gia project](https://drive.google.com/file/d/1A4VofpuuExIDg0OhLMJLMxuaMTaPZdqz/view?usp=sharing)
### 3. Công cụ sử dụng
#### Trong quá trình thực hiện và xây dựng phần mềm cho phần mềm nhóm đã sử dụng các phần mềm sau:
##### - Phần mềm sql developer (Oracle)
##### - Phần mềm NetBaen: để thực hiện phần mềm Java
##### - Driver: ojdbc7.
##### - Phần mềm StarUML.
##### - Công cụ iReport.
##### Tất cả phần mềm được nhóm cài đặt trên hệ điều hành Windows 10.
### 4. Phân tích yêu cầu
#### 4.1 Yêu cầu chức năng
##### Chức năng lưu trữ dữ liệu
###### - Thống kê số lượng bệnh nhân theo tuần, theo tháng.
###### - Quản lý việc thu/chi của bệnh nhân từ lúc vào phòng khám đến khi ra khỏi phòng khám gồm các khâu như: thu phí khám, phí dịch vụ, phí tạm ứng nhập viện, các chi phí khác đến các khoản chi như chi tiền thừa trả lại, chi trả lại do hủy dịch vụ,... Chức năng kết nối xuyên suốt với tất cả các chức năng khác trong hệ thống giúp mọi khoản thu chi minh bạch, dữ liệu đồng nhất.
###### - Quản lý khám bệnh: Bác sĩ có thể xem tiền sử bệnh, chẩn đoán bệnh, ghi sinh hiệu, chỉ định cận lâm sàng, xem kết quả cận lâm sàng trên phần mềm, kê đơn thuốc điện tử, làm hồ sơ điều trị ngoại trú...
###### - Quản lý nhân viên: quản lý từ Hồ sơ nhân viên, tuyển dụng, chấm công, tính lương, truyền thông nội bộ, đánh giá nhân sự, quản lý thôi việc...
###### - Quản lý thuốc: quản lý theo hạn dùng, quản lý theo đơn và quản lý xuất – nhập các đơn thuốc, số lượng thuốc ...
##### Chức năng tìm kiếm và tra cứu
###### - Nhân viên quản lý có thể thực hiên các chức năng tìm kiếm: đơn thuốc, lịch khám, kết quả khám bệnh....
###### - Chức năng tra cứu thông tin bệnh nhân, bác sĩ.
##### Chức năng thêm, xóa, sửa
###### - Nhân viên quản lý có thể thêm, xóa, sửa các thông tin bệnh nhân, bác sĩ, đơn thuốc, lịch khám....
##### Chức năng xử lý đồng thời:
###### - Một bệnh nhân đã đặt lịch khám theo giờ theo ngày thì bệnh nhân khác không được đặt đồng thời vào.
###### - Xử lý các vấn đề gây mất nhất quán dữ liệu lost update, uncommited, deadlock, dirty read.
#### 4.2 Yêu cầu phi chức năng
##### Yêu cầu về giao diện : Giao diện ứng dễ dàng thao tác, dễ sử dụng, thân thiện với người dùng cũng như người quản lý.
##### Yêu cầu về chất lượng: Đảm bảo hoạt động tốt trong quá trình sử dụng.
##### Phân quyền chặt chẽ: Mỗi người dùng được cung cấp một số chức năng nhất định.
##### Yêu cầu thuận tiện: Dễ sử dụng, thân thiện trực quan.









