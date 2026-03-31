USE [master]
GO

-- 1. LÀM SẠCH: Xóa database cũ nếu đã tồn tại để tránh lỗi trùng lặp
IF EXISTS (SELECT name FROM sys.databases WHERE name = N'HeThongGiaSu')
BEGIN
    ALTER DATABASE [HeThongGiaSu] SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE [HeThongGiaSu];
END
GO

-- 2. TẠO DATABASE (Tự động chọn đường dẫn mặc định của máy bạn)
CREATE DATABASE [HeThongGiaSu]
GO

USE [HeThongGiaSu]
GO

---------------------------------------------------------
-- 3. TẠO TOÀN BỘ CẤU TRÚC BẢNG (14 BẢNG THEO FILE CỦA BẠN)
---------------------------------------------------------

CREATE TABLE [dbo].[tai_khoan](
    [ma_tai_khoan] [int] IDENTITY(1,1) NOT NULL PRIMARY KEY,
    [ho_ten] [nvarchar](100) NULL,
    [email] [varchar](100) NOT NULL UNIQUE,
    [mat_khau_ma_hoa] [varchar](255) NOT NULL,
    [so_dien_thoai] [varchar](20) NULL,
    [vai_tro] [varchar](20) NULL,
    [trang_thai] [varchar](20) NULL,
    [vi_tri] [nvarchar](225) NULL,
    [ngay_tao] [datetime2](6) DEFAULT GETDATE()
    );

CREATE TABLE [dbo].[mon_hoc](
    [ma_mon] [int] IDENTITY(1,1) NOT NULL PRIMARY KEY,
    [ten_mon] [nvarchar](100) NULL,
    [mo_ta] [nvarchar](255) NULL
    );

CREATE TABLE [dbo].[gia_su](
    [ma_gia_su] [int] IDENTITY(1,1) NOT NULL PRIMARY KEY,
    [ma_tai_khoan] [int] FOREIGN KEY REFERENCES [tai_khoan]([ma_tai_khoan]),
    [nam_sinh] [int] NULL,
    [gioi_tinh] [varchar](10) NULL,
    [chuyen_nganh] [nvarchar](100) NULL,
    [truong_dai_hoc] [nvarchar](100) NULL,
    [so_nam_kinh_nghiem] [int] NULL,
    [mo_ta] [nvarchar](max) NULL
    );

CREATE TABLE [dbo].[hoc_vien](
    [ma_hoc_vien] [int] IDENTITY(1,1) NOT NULL PRIMARY KEY,
    [ma_tai_khoan] [int] FOREIGN KEY REFERENCES [tai_khoan]([ma_tai_khoan]),
    [truong_hoc] [nvarchar](100) NULL,
    [dia_chi] [nvarchar](255) NULL,
    [lop_hoc] [varchar](50) NULL,
    [hinh_thuc_hoc_uu_tien] [varchar](50) NULL,
    [ghi_chu] [nvarchar](255) NULL,
    [vi_do] [float] NULL,
    [kinh_do] [float] NULL
    );

CREATE TABLE [dbo].[yeu_cau_tim_gia_su](
    [ma_yeu_cau] [int] IDENTITY(1,1) NOT NULL PRIMARY KEY,
    [ma_hoc_vien] [int] FOREIGN KEY REFERENCES [hoc_vien]([ma_hoc_vien]),
    [ma_mon] [int] FOREIGN KEY REFERENCES [mon_hoc]([ma_mon]),
    [ngan_sach_min] [int] NULL,
    [ngan_sach_max] [int] NULL,
    [hinh_thuc] [nvarchar](50) NULL,
    [trinh_do] [nvarchar](50) NULL,
    [dia_diem] [nvarchar](255) NULL,
    [lich_hoc_du_kien] [nvarchar](255) NULL,
    [mo_ta] [nvarchar](300) NULL,
    [trang_thai] [nvarchar](50) NULL,
    [ngay_tao] [datetime2](6) DEFAULT GETDATE()
    );

CREATE TABLE [dbo].[lop_hoc](
    [ma_lop] [int] IDENTITY(1,1) NOT NULL PRIMARY KEY,
    [ma_gia_su] [int] FOREIGN KEY REFERENCES [gia_su]([ma_gia_su]),
    [ma_hoc_vien] [int] FOREIGN KEY REFERENCES [hoc_vien]([ma_hoc_vien]),
    [ma_mon] [int] FOREIGN KEY REFERENCES [mon_hoc]([ma_mon]),
    [ma_yeu_cau] [int] FOREIGN KEY REFERENCES [yeu_cau_tim_gia_su]([ma_yeu_cau]),
    [hoc_phi_thoa_thuan] [int] NULL,
    [ngay_bat_dau] [date] NULL,
    [ngay_ket_thuc] [date] NULL,
    [trang_thai] [varchar](50) NULL,
    [lich_hoc] [nvarchar](255) NULL,
    [tong_so_buoi] [int] NULL,
    [so_buoi_con_lai] [int] NULL,
    [ghi_chu] [nvarchar](500) NULL
    );

CREATE TABLE [dbo].[buoi_hoc](
    [ma_buoi] [int] IDENTITY(1,1) NOT NULL PRIMARY KEY,
    [ma_lop] [int] FOREIGN KEY REFERENCES [lop_hoc]([ma_lop]),
    [so_gio_hoc] [int] NULL,
    [thoi_gian_bat_dau] [datetime2](6) NULL,
    [thoi_gian_ket_thuc] [datetime2](6) NULL,
    [trang_thai] [varchar](50) NULL
    );

CREATE TABLE [dbo].[gia_su_mon_hoc](
    [ma_gia_su] [int] NOT NULL,
    [ma_mon] [int] NOT NULL,
    [hoc_phi_moi_gio] [int] NULL,
    [trinh_do] [varchar](50) NULL,
    PRIMARY KEY ([ma_gia_su], [ma_mon]),
    FOREIGN KEY ([ma_gia_su]) REFERENCES [gia_su]([ma_gia_su]),
    FOREIGN KEY ([ma_mon]) REFERENCES [mon_hoc]([ma_mon])
    );

CREATE TABLE [dbo].[thanh_toan](
    [ma_thanh_toan] [int] IDENTITY(1,1) NOT NULL PRIMARY KEY,
    [ma_lop] [int] FOREIGN KEY REFERENCES [lop_hoc]([ma_lop]),
    [so_tien] [int] NULL,
    [phuong_thuc] [nvarchar](50) NULL,
    [trang_thai] [nvarchar](50) NULL,
    [ngay_tao] [datetime2](6) DEFAULT GETDATE()
    );

CREATE TABLE [dbo].[lich_su_giao_dich](
    [ma_giao_dich] [int] IDENTITY(1,1) NOT NULL PRIMARY KEY,
    [ma_thanh_toan] [int] FOREIGN KEY REFERENCES [thanh_toan]([ma_thanh_toan]),
    [so_tien] [int] NULL,
    [ngay_giao_dich] [datetime2](6) NULL,
    [loai] [varchar](50) NULL
    );

CREATE TABLE [dbo].[danh_gia](
    [ma_danh_gia] [int] IDENTITY(1,1) NOT NULL PRIMARY KEY,
    [ma_lop] [int] FOREIGN KEY REFERENCES [lop_hoc]([ma_lop]),
    [diem] [int] NULL,
    [nhan_xet] [nvarchar](255) NULL,
    [thoi_gian_danh_gia] [datetime2](6) NULL
    );

CREATE TABLE [dbo].[thong_bao](
    [ma_thong_bao] [int] IDENTITY(1,1) NOT NULL PRIMARY KEY,
    [ma_tai_khoan] [int] NOT NULL FOREIGN KEY REFERENCES [tai_khoan]([ma_tai_khoan]),
    [noi_dung] [nvarchar](500) NULL,
    [loai] [nvarchar](50) NULL,
    [da_doc] [bit] DEFAULT 0,
    [ngay_tao] [datetime2](6) DEFAULT GETDATE()
    );

CREATE TABLE [dbo].[ung_tuyen](
    [ma_gia_su] [int] NOT NULL,
    [ma_yeu_cau] [int] NOT NULL,
    [muc_hoc_phi_de_xuat] [int] NULL,
    [loi_nhan] [nvarchar](300) NULL,
    [trang_thai] [nvarchar](50) NULL,
    [ngay_ung_tuyen] [datetime2](6) DEFAULT GETDATE(),
    PRIMARY KEY ([ma_gia_su], [ma_yeu_cau]),
    FOREIGN KEY ([ma_gia_su]) REFERENCES [gia_su]([ma_gia_su]),
    FOREIGN KEY ([ma_yeu_cau]) REFERENCES [yeu_cau_tim_gia_su]([ma_yeu_cau])
    );

GO

---------------------------------------------------------
-- 4. CHÈN DỮ LIỆU MẪU (FULL DATA)
---------------------------------------------------------

-- 1. tai_khoan
INSERT INTO [tai_khoan] (ho_ten, email, mat_khau_ma_hoa, vai_tro, trang_thai) VALUES
(N'Lê Gia Sư 1', 'gs1@gmail.com', 'pwd1', 'GiaSu', 'Active'),
(N'Nguyễn Học Viên 1', 'hv1@gmail.com', 'pwd2', 'HocVien', 'Active'),
(N'Admin Hệ Thống', 'admin@gmail.com', 'pwd3', 'Admin', 'Active');

-- 2. mon_hoc
INSERT INTO [mon_hoc] (ten_mon, mo_ta) VALUES
    (N'Toán học', N'Cấp 1, 2, 3'), (N'Tiếng Anh', N'Giao tiếp & Ielts'), (N'Lập trình', N'Java, Python');

-- 3. gia_su
INSERT INTO [gia_su] (ma_tai_khoan, nam_sinh, gioi_tinh, chuyen_nganh, truong_dai_hoc, so_nam_kinh_nghiem)
VALUES (1, 1995, 'Nam', N'Sư phạm Toán', N'ĐH Sư Phạm', 5);

-- 4. hoc_vien
INSERT INTO [hoc_vien] (ma_tai_khoan, truong_hoc, dia_chi)
VALUES (2, N'THPT Việt Đức', N'Hà Nội');

-- 5. gia_su_mon_hoc
INSERT INTO [gia_su_mon_hoc] (ma_gia_su, ma_mon, hoc_phi_moi_gio, trinh_do)
VALUES (1, 1, 200000, N'Thạc sĩ');

-- 6. yeu_cau_tim_gia_su
INSERT INTO [yeu_cau_tim_gia_su] (ma_hoc_vien, ma_mon, ngan_sach_max, hinh_thuc, trinh_do, trang_thai)
VALUES (1, 1, 250000, N'Trực tiếp', N'Sinh viên năm 4', N'Đang tìm');

-- 7. ung_tuyen
INSERT INTO [ung_tuyen] (ma_gia_su, ma_yeu_cau, muc_hoc_phi_de_xuat, loi_nhan, trang_thai)
VALUES (1, 1, 220000, N'Tôi có kinh nghiệm dạy lớp 12', N'Chờ duyệt');

-- 8. lop_hoc
INSERT INTO [lop_hoc] (ma_gia_su, ma_hoc_vien, ma_mon, ma_yeu_cau, hoc_phi_thoa_thuan, tong_so_buoi, so_buoi_con_lai, trang_thai)
VALUES (1, 1, 1, 1, 220000, 10, 10, 'Active');

-- 9. buoi_hoc
INSERT INTO [buoi_hoc] (ma_lop, so_gio_hoc, thoi_gian_bat_dau, trang_thai)
VALUES (1, 2, '2026-04-01 18:00:00', 'Scheduled');

-- 10. thanh_toan
INSERT INTO [thanh_toan] (ma_lop, so_tien, phuong_thuc, trang_thai)
VALUES (1, 2200000, N'Chuyển khoản', N'Đã thanh toán');

-- 11. lich_su_giao_dich
INSERT INTO [lich_su_giao_dich] (ma_thanh_toan, so_tien, loai)
VALUES (1, 2200000, 'Income');

-- 12. danh_gia
INSERT INTO [danh_gia] (ma_lop, diem, nhan_xet)
VALUES (1, 5, N'Gia sư dạy rất nhiệt tình');

-- 13. thong_bao
INSERT INTO [thong_bao] (ma_tai_khoan, noi_dung, loai)
VALUES (1, N'Bạn có một yêu cầu dạy mới', 'System');

GO

-- Kiểm tra
SELECT 'Thanh Cong!' as Status;