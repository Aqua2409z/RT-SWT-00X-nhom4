# RT-SWT-00X-nhom4
> **SWT301 – Software Testing (Research-Based Learning Project)**  
> **Topic:** RT-SWT-000

---

## 📌 Thông Tin Chung
* **Môn học:** SWT301 – Software Testing
* **Hình thức:** Research-Based Learning (RBL)
* **Học kỳ:** SUMMER 2026
* **Giảng viên hướng dẫn:** Cô Lê Thị Quỳnh Chi

---

## 👥 Danh Sách Thành Viên (Nhóm 4)

| STT | Họ và Tên | Ký hiệu viết tắt | Vai trò | Thư mục cá nhân |
| :---: | :--- | :---: | :--- | :--- |
| 1 | **Trần Bích Trâm** | **PL** | Project Leader (Trưởng nhóm) | `/TranBichTram/SLR` |
| 2 | **Trần Minh Quý** | **DG** | Developer / Researcher | `/TranMinhQuy/SLR` |
| 3 | **Lê Trần Anh Khoa** | **LM** | Developer / Researcher | `/LeTranAnhKhoa/SLR` |
| 4 | **Trương Đan Huy** | **MS** | Developer / Researcher | `/TruongDanHuy/SLR` |
| 5 | **Vương Quốc Khánh** | **RW** | Developer / Researcher | `/VuongQuocKhanh/SLR` |

---

## 📂 Cấu Trúc Thư Mục Dự Án (Folder Tree)

Dự án tuân thủ nghiêm ngặt cấu trúc thư mục chuẩn hóa cho quá trình Nghiên cứu dựa trên Thực nghiệm (Research-Based Learning):

```text
📂 RT-SWT-00X-nhom4
 ┣ 📂 [member-folders]/SLR/     # Thư mục cá nhân của từng thành viên (Trơn không dấu)
 ┃ ┣ 📂 papers/                 # [RBL-1A] PDF các paper đã included
 ┃ ┃ ┗ 📄 Author_Year_Keyword.pdf
 ┃ ┣ 📄 search-log.md           # [RBL-1A] Query strings + ngày + số kết quả
 ┃ ┣ 📄 01_all_records.csv      # [RBL-1A] Tất cả paper sau khi loại trùng (dedup)
 ┃ ┣ 📄 02_after_screening_v1.csv # [RBL-1A] Lọc v1 (+ cột v1_decision, v1_reason)
 ┃ ┣ 📄 03_final_included.csv   # [RBL-1A] Lọc v2 (+ cột v2_decision, v2_reason)
 ┃ ┣ 📄 ie_criteria.md          # [RBL-1A] Tiêu chí lựa chọn/loại trừ (inclusion/exclusion)
 ┃ ┣ 📄 prisma-flow.md          # [RBL-1A] Sơ đồ PRISMA khớp số lượng CSV
 ┃ ┣ 📄 evidence-table.md       # [RBL-1A] Bảng trích xuất dữ liệu của cá nhân
 ┃ ┗ 📄 gap-analysis.md         # [RBL-2] Phân tích sâu khoảng trống nghiên cứu (GAP) tự chọn
 ┃
 ┣ 📂 team-synthesis/           # [RBL-1B & RBL-3] Thư mục tổng hợp của nhóm
 ┃ ┣ 📄 evidence-table-merged.md # Gộp paper của toàn nhóm, lọc trùng
 ┃ ┣ 📄 gap-list.md             # Danh sách GAP toàn nhóm + phân công người chọn
 ┃ ┣ 📄 gap-final.md            # GAP chính và GAP phụ được nhóm chốt lại
 ┃ ┗ 📄 proposal.md             # Đề cương nghiên cứu đã được GV phê duyệt
 ┃
 ┣ 📂 data/                     # [RBL-4] Dữ liệu thực nghiệm
 ┃ ┣ 📂 raw/                    # Dataset gốc chưa qua chỉnh sửa
 ┃ ┃ ┗ 📄 README.md             # Nguồn, license, cấu trúc cột, ngày tải...
 ┃ ┣ 📄 pilot_sample.csv        # Mẫu thử nghiệm (10-20% N, random seed cố định)
 ┃ ┣ 📄 pilot_ground_truth.csv  # Nhãn gán thủ công (annotate) cho tập pilot
 ┃ ┗ 📄 full_ground_truth.csv   # Nhãn gán thủ công cho toàn bộ dataset
 ┃
 ┣ 📂 scripts/                  # [RBL-4] Mã nguồn bộ công cụ thực nghiệm
 ┃ ┣ 📄 test_api.py             # Script kiểm tra gọi API (Gate E3)
 ┃ ┣ 📄 run_experiment.py       # Chạy LLM hàng loạt (batch-run) theo cấu hình proposal
 ┃ ┗ 📄 compute_metric.py       # Tính toán các chỉ số (metric) & kiểm định thống kê
 ┃
 ┣ 📂 results/                  # [RBL-4] Kết quả đầu ra từ thực nghiệm
 ┃ ┣ 📄 pilot_llm_output.csv    # Kết quả LLM trả về trên tập pilot
 ┃ ┣ 📄 pilot_api_log.txt       # Nhật ký gọi API (mẫu thử): model version, cost/call
 ┃ ┣ 📄 pilot_analysis.ipynb    # Biểu đồ phân bố (histogram) & thống kê mô tả tập pilot
 ┃ ┣ 📄 full_llm_output.csv     # Kết quả LLM trên toàn bộ dataset
 ┃ ┣ 📄 full_api_log.txt        # Nhật ký gọi API (toàn bộ): timestamp, model, cost, errors
 ┃ ┣ 📄 full_analysis.ipynb     # Phân tích thống kê sâu (* p-value, effect size, kết luận)
 ┃ ┗ 📄 summary.csv             # Bảng tóm tắt kết quả (1 dòng/RQ: metric, p, effect size, N)
 ┃
 ┣ 📂 figures/                  # [RBL-4] Biểu đồ trực quan hóa
 ┃ ┣ 📄 fig1_distribution.png   # Boxplot/violin metric chính (>= 300 DPI)
 ┃ ┗ 📄 fig2_comparison.png     # Biểu đồ so sánh (nếu có RQ2)
 ┃
 ┣ 📂 paper/                    # [RBL-5] Viết trên Overleaf, mirror về local
 ┃ ┣ 📄 main.tex                # File mã nguồn LaTeX chính
 ┃ ┣ 📄 references.bib          # Tài liệu tham khảo BibTeX (mỗi entry có DOI)
 ┃ ┣ 📂 sections/               # Chi tiết các phần nội dung bài báo
 ┃ ┃ ┣ 📄 00_abstract.tex       # PL viết (sau cùng)
 ┃ ┃ ┣ 📄 01_intro.tex          # RW viết
 ┃ ┃ ┣ 📄 02_related.tex        # DG viết
 ┃ ┃ ┣ 📄 03_method.tex         # LM + MS viết
 ┃ ┃ ┣ 📄 04_results.tex        # MS viết
 ┃ ┃ ┣ 📄 05_discussion.tex     # PL + MS viết
 ┃ ┃ ┣ 📄 06_threats.tex        # RW viết
 ┃ ┃ ┗ 📄 07_conclusion.tex     # RW viết
 ┃ ┣ 📂 figures/                # Sao chép từ thư mục `/figures/` ngoài (để LaTeX build)
 ┃ ┣ 📂 output/                 
 ┃ ┃ ┗ 📄 paper_final.pdf       # ★ PDF biên dịch cuối cùng từ main.tex
 ┃ ┗ 📂 quality/                
 ┃   ┗ 📄 ai_check_log.md       # Kết quả kiểm tra AI detector + ghi chú
 ┃
 ┣ 📂 presentation/             # [RBL-3 & RBL-5] Slide thuyết trình dự án
 ┃ ┣ 📄 slides_proposal.pptx    # Slide bảo vệ đề cương
 ┃ ┣ 📄 slides_proposal.pdf     # Bản PDF slide đề cương nộp GV
 ┃ ┣ 📄 slides_final.pptx       # Slide trình bày báo cáo cuối kỳ
 ┃ ┗ 📄 slides_final.pdf        # Bản PDF slide báo cáo cuối kỳ nộp GV
 ┃
 ┣ 📄 .gitignore                # Quản lý loại trừ các file rác không cần đẩy lên git
 ┗ 📄 notes.md                  # [RBL-4] Ghi lại mọi quyết định kỹ thuật + nhật ký lỗi (error log)
