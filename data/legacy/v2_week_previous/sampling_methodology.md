# Tài liệu Phương pháp luận Lấy mẫu (Class Sampling Methodology)

Tài liệu này giải thích chi tiết về thuật toán bốc mẫu được sử dụng để xây dựng tập dữ liệu gồm **300 class chính** (main classes) và **58 class dự phòng** (backup classes) từ **33 repositories** thành công của dự án baseline.

---

## 1. Bài toán và Thách thức Thực tế
Trong nghiên cứu thực nghiệm Kỹ nghệ Phần mềm (Empirical Software Engineering), việc lấy mẫu mã nguồn gặp phải hai thách thức lớn mâu thuẫn nhau:
1.  **Sự mất cân bằng độ phức tạp tự nhiên (Complexity Imbalance)**: Lập trình viên viết rất nhiều class đơn giản (ví dụ class cấu hình, class chứa dữ liệu getter/setter có CC 2–3) và viết rất ít class phức tạp (CC từ 10 trở lên). Nếu sử dụng phương pháp bốc mẫu ngẫu nhiên truyền thống hoặc phân tầng theo tỷ lệ, tập dữ liệu thu được sẽ bị thống trị bởi các class đơn giản. Điều này làm cho việc đánh giá các công cụ viết test (EvoSuite, Randoop, LLM) không còn giá trị vì các class quá dễ viết test.
2.  **Sự khác biệt về quy mô dự án (Project Size Variance)**: Một số repository cứu hộ hoặc nhỏ chỉ có các class đơn giản (CC 2–3) mà không hề chứa bất kỳ class phức tạp nào. Nếu chúng ta loại bỏ hoàn toàn các class đơn giản, các repository này sẽ không đóng góp được class nào và bị loại bỏ khỏi nghiên cứu, dẫn đến không đạt độ bao phủ toàn bộ 33 repository.

---

## 2. Giải pháp thuật toán: Lấy mẫu phân tầng phi tỷ lệ dồn toa (Disproportional Stratified Random Sampling with Cascading Backfill)
Để giải quyết triệt để hai thách thức trên một cách khoa học và liêm chính học thuật, thuật toán **Lấy mẫu phân tầng phi tỷ lệ với cơ chế dồn toa** đã được thiết kế và triển khai trong file `04_sample_classes_balanced.py`.

### Chi tiết các bước thực hiện của thuật toán:

#### Bước 1: Phân tầng độ phức tạp (Complexity Stratification)
*   Toàn bộ quần thể class hợp lệ (thỏa mãn NLOC 5–500, CC 2–14 và có phương thức public) được chia vào **13 tầng** tương ứng với các mức Cyclomatic Complexity từ **CC = 2** đến **CC = 14**.
*   Đặt chỉ tiêu phân bổ đều (Uniform Target): Mỗi tầng CC bốc mẫu **23 class** ($300 \text{ class} / 13 \text{ tầng} \approx 23$).

#### Bước 2: Ràng buộc bao phủ Repository (Repository Coverage Constraint)
*   Để đảm bảo toàn bộ **33 repository** đều đóng góp dữ liệu, ở lượt bốc đầu tiên, thuật toán duyệt qua từng repo và lấy ra đúng **1 class đại diện** đầu tiên của repo đó (dựa trên thứ tự ngẫu nhiên được xáo trộn bằng mã băm).
*   Class đại diện này thuộc CC nào thì chỉ tiêu của tầng CC tương ứng đó sẽ được trừ đi 1. Điều này đảm bảo 100% repo có đại diện, giải quyết triệt để bài toán bao phủ repo.

#### Bước 3: Bốc mẫu phân tầng dồn toa (Cascading Stratified Selection)
Duyệt qua các tầng CC theo thứ tự giảm dần từ **CC = 14** xuống **CC = 2**:
*   Tại mỗi tầng CC, chúng ta bốc số lượng class còn thiếu để đạt chỉ tiêu của tầng đó.
*   **Cơ chế dồn toa (Cascading Backfill)**: Nếu một tầng CC ở mức cao bị thiếu mẫu (do mã nguồn thực tế không chứa đủ số class CC cao như vậy, ví dụ CC 14 chỉ có 3 class), thuật toán sẽ nhận toàn bộ số class đó, đồng thời chuyển (dồn) lượng chỉ tiêu còn thiếu sang tầng CC liền kề phía dưới (CC 13, rồi xuống CC 12).
*   Cơ chế này giúp tối đa hóa độ phức tạp của tập mẫu thực nghiệm, hạn chế tối đa việc dồn chỉ tiêu về các tầng CC 2 và CC 3 dễ dãi.

#### Bước 4: Đảm bảo tính Liêm chính học thuật (Academic Honesty & Reproducibility)
*   Để việc chọn lựa hoàn toàn khách quan và không thiên vị, mỗi class được tính một mã băm SHA-256 duy nhất:
    `SHA-256(Hạt giống 42 + ID Repo + Tên Class + Đường dẫn file)`
*   Các class được sắp xếp theo mã băm này và bốc từ trên xuống dưới.
*   Vì mã băm SHA-256 là ngẫu nhiên nhưng xác định (deterministic), nên thuật toán hoàn toàn tự động, khách quan 100%, không bị ảnh hưởng bởi thiên kiến của người nghiên cứu, và có khả năng tái hiện kết quả y hệt trên bất kỳ máy tính nào.

---

## 3. Kết quả phân bổ CC thực tế
Sau khi chạy thuật toán, chúng ta đạt được bảng phân bổ độ phức tạp CC rất lý tưởng để làm thực nghiệm:

| Độ phức tạp (CC) | Số lượng mẫu (Class) | Tỷ lệ (%) | Trạng thái phân bổ |
| :---: | :---: | :---: | :--- |
| **CC = 2** | 23 | 7.7% | Đạt chỉ tiêu đều (Target = 23) |
| **CC = 3** | 23 | 7.7% | Đạt chỉ tiêu đều (Target = 23) |
| **CC = 4** | 23 | 7.7% | Đạt chỉ tiêu đều (Target = 23) |
| **CC = 5** | 23 | 7.7% | Đạt chỉ tiêu đều (Target = 23) |
| **CC = 6** | 23 | 7.7% | Đạt chỉ tiêu đều (Target = 23) |
| **CC = 7** | 23 | 7.7% | Đạt chỉ tiêu đều (Target = 23) |
| **CC = 8** | 23 | 7.7% | Đạt chỉ tiêu đều (Target = 23) |
| **CC = 9** | 23 | 7.7% | Đạt chỉ tiêu đều (Target = 23) |
| **CC = 10** | 23 | 7.7% | Đạt chỉ tiêu đều (Target = 23) |
| **CC = 11** | 23 | 7.7% | Đạt chỉ tiêu đều (Target = 23) |
| **CC = 12** | 46 | 15.3% | Đạt chỉ tiêu đều + Bù dồn toa của CC 13 & 14 |
| **CC = 13** | 21 | 7.0% | Lấy tối đa class thực tế hiện có |
| **CC = 14** | 3 | 1.0% | Lấy tối đa class thực tế hiện có |
| **Tổng cộng** | **300** | **100%** | |
