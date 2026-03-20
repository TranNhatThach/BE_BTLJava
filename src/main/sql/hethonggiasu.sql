CREATE DATABASE HeThongGiaSu;
GO
USE [HeThongGiaSu];
GO

CREATE TABLE Tai_Khoan (
    ma_tai_khoan INT IDENTITY(1,1) PRIMARY KEY,
    email NVARCHAR(100) UNIQUE NOT NULL,
    mat_khau_ma_hoa NVARCHAR(255) NOT NULL,
    so_dien_thoai NVARCHAR(20),
    ho_ten NVARCHAR(100),
    vai_tro NVARCHAR(20),
    trang_thai NVARCHAR(20),
    ngay_tao DATETIME DEFAULT GETDATE(),
    vi_tri NVARCHAR(255)
);
GO
CREATE TABLE Gia_Su (
    ma_gia_su INT IDENTITY(1,1) PRIMARY KEY,
    ma_tai_khoan INT UNIQUE,
    gioi_tinh NVARCHAR(10),
    truong_dai_hoc NVARCHAR(100),
    chuyen_nganh NVARCHAR(100),
    nam_sinh INT,
    so_nam_kinh_nghiem INT,

    FOREIGN KEY (ma_tai_khoan) REFERENCES Tai_Khoan(ma_tai_khoan)
);
GO
CREATE TABLE Hoc_Vien (
    ma_hoc_vien INT IDENTITY(1,1) PRIMARY KEY,
    ma_tai_khoan INT UNIQUE,
    lop_hoc NVARCHAR(50),
    truong_hoc NVARCHAR(100),
    vi_do FLOAT,
    kinh_do FLOAT,
    hinh_thuc_hoc_uu_tien NVARCHAR(50),
    ghi_chu NVARCHAR(255),

    FOREIGN KEY (ma_tai_khoan) REFERENCES Tai_Khoan(ma_tai_khoan)
);
GO
CREATE TABLE Mon_Hoc (
    ma_mon INT IDENTITY(1,1) PRIMARY KEY,
    ten_mon NVARCHAR(100),
    mo_ta NVARCHAR(255)
);
GO
CREATE TABLE Gia_Su_Mon_Hoc (
    ma_gia_su INT,
    ma_mon INT,
    trinh_do NVARCHAR(50),
    hoc_phi_moi_gio int,

    PRIMARY KEY (ma_gia_su, ma_mon),

    FOREIGN KEY (ma_gia_su) REFERENCES Gia_Su(ma_gia_su),
    FOREIGN KEY (ma_mon) REFERENCES Mon_Hoc(ma_mon)
);
GO
CREATE TABLE Yeu_Cau_Tim_Gia_Su (
    ma_yeu_cau INT IDENTITY(1,1) PRIMARY KEY,
    ma_mon INT,
    ma_hoc_vien INT,
    trinh_do NVARCHAR(50),
    lich_hoc_du_kien NVARCHAR(255),
    hinh_thuc NVARCHAR(50),
    dia_diem NVARCHAR(255),
    ngan_sach_min int,
    ngan_sach_max int,
    trang_thai NVARCHAR(50),
    ngay_tao DATETIME DEFAULT GETDATE(),
    mo_ta NVARCHAR(MAX),

    FOREIGN KEY (ma_mon) REFERENCES Mon_Hoc(ma_mon),
    FOREIGN KEY (ma_hoc_vien) REFERENCES Hoc_Vien(ma_hoc_vien)
);
GO
CREATE TABLE Ung_Tuyen (
    ma_gia_su INT,
    ma_yeu_cau INT,
    loi_nhan NVARCHAR(255),
    muc_hoc_phi_de_xuat int,
    trang_thai NVARCHAR(50),
    ngay_ung_tuyen DATETIME DEFAULT GETDATE(),

    PRIMARY KEY (ma_gia_su, ma_yeu_cau),

    FOREIGN KEY (ma_gia_su) REFERENCES Gia_Su(ma_gia_su),
    FOREIGN KEY (ma_yeu_cau) REFERENCES Yeu_Cau_Tim_Gia_Su(ma_yeu_cau)
);
GO
CREATE TABLE Lop_Hoc (
    ma_lop INT IDENTITY(1,1) PRIMARY KEY,
    ma_hoc_vien INT,
    ma_gia_su INT,
    ma_mon INT,
    hoc_phi_thoa_thuan int,
    ngay_bat_dau DATE,
    ngay_ket_thuc DATE,
    trang_thai NVARCHAR(50),
    ma_yeu_cau INT,

    FOREIGN KEY (ma_hoc_vien) REFERENCES Hoc_Vien(ma_hoc_vien),
    FOREIGN KEY (ma_gia_su) REFERENCES Gia_Su(ma_gia_su),
    FOREIGN KEY (ma_mon) REFERENCES Mon_Hoc(ma_mon),
    FOREIGN KEY (ma_yeu_cau) REFERENCES Yeu_Cau_Tim_Gia_Su(ma_yeu_cau)
);
GO
CREATE TABLE Buoi_Hoc (
    ma_buoi INT IDENTITY(1,1) PRIMARY KEY,
    ma_lop INT,
    thoi_gian_bat_dau DATETIME,
    thoi_gian_ket_thuc DATETIME,
    so_gio_hoc int,
    trang_thai NVARCHAR(50),

    FOREIGN KEY (ma_lop) REFERENCES Lop_Hoc(ma_lop)
);
GO
CREATE TABLE Thanh_Toan (
    ma_thanh_toan INT IDENTITY(1,1) PRIMARY KEY,
    ma_lop INT,
    so_tien int,
    phuong_thuc NVARCHAR(50),
    trang_thai NVARCHAR(50),
    ngay_tao DATETIME DEFAULT GETDATE(),

    FOREIGN KEY (ma_lop) REFERENCES Lop_Hoc(ma_lop)
);
GO
CREATE TABLE Lich_Su_Giao_Dich (
    ma_giao_dich INT IDENTITY(1,1) PRIMARY KEY,
    ma_thanh_toan INT,
    so_tien int,
    loai NVARCHAR(50),
    ngay_giao_dich DATETIME DEFAULT GETDATE(),

    FOREIGN KEY (ma_thanh_toan) REFERENCES Thanh_Toan(ma_thanh_toan)
);
GO
CREATE TABLE Danh_Gia (
    ma_danh_gia INT IDENTITY(1,1) PRIMARY KEY,
    ma_lop INT,
    diem INT,
    nhan_xet NVARCHAR(255),
    thoi_gian_danh_gia DATETIME DEFAULT GETDATE(),

    FOREIGN KEY (ma_lop) REFERENCES Lop_Hoc(ma_lop)
);
GO


INSERT INTO Tai_Khoan (email, mat_khau_ma_hoa, so_dien_thoai, ho_ten, vai_tro, trang_thai)
VALUES
('hocsinh1@gmail.com','123456','0901111111','Nguyen Van A','HOC_VIEN','HOAT_DONG'),
('hocsinh2@gmail.com','123456','0902222222','Tran Thi B','HOC_VIEN','HOAT_DONG'),
('giasu1@gmail.com','123456','0903333333','Le Van C','GIA_SU','HOAT_DONG'),
('giasu2@gmail.com','123456','0904444444','Pham Thi D','GIA_SU','HOAT_DONG'),
('admin@gmail.com','123456','0905555555','Admin','QUAN_TRI','HOAT_DONG');
GO

INSERT INTO Gia_Su (ma_tai_khoan, gioi_tinh, truong_dai_hoc, chuyen_nganh, nam_sinh, so_nam_kinh_nghiem)
VALUES
(3,'Nam','DH Bach Khoa','Cong nghe thong tin',1999,3),
(4,'Nu','DH Su Pham','Su Pham Toan',1998,4);
GO

INSERT INTO Hoc_Vien (ma_tai_khoan, lop_hoc, truong_hoc, vi_do, kinh_do, hinh_thuc_hoc_uu_tien)
VALUES
(1,'10','THPT Chu Van An',21.0285,105.8542,'Hoc tai nha'),
(2,'11','THPT Kim Lien',21.0300,105.8500,'Online');
GO

INSERT INTO Mon_Hoc (ten_mon, mo_ta)
VALUES
('Toan','Mon toan pho thong'),
('Ly','Vat ly pho thong'),
('Hoa','Hoa hoc pho thong'),
('Tieng Anh','Tieng anh giao tiep');
GO

INSERT INTO Gia_Su_Mon_Hoc (ma_gia_su, ma_mon, trinh_do, hoc_phi_moi_gio)
VALUES
(1,1,'Dai hoc',200000),
(1,4,'Dai hoc',180000),
(2,1,'Thac si',250000),
(2,2,'Thac si',230000);
GO

INSERT INTO Yeu_Cau_Tim_Gia_Su 
(ma_mon, ma_hoc_vien, trinh_do, lich_hoc_du_kien, hinh_thuc, dia_diem, ngan_sach_min, ngan_sach_max, trang_thai)
VALUES
(1,1,'Gia su sinh vien','T2 T4 T6','Hoc tai nha','Ha Noi',150000,250000,'Dang tim'),
(4,2,'Gia su sinh vien','T3 T5','Online','Ha Noi',150000,200000,'Dang tim');
GO

INSERT INTO Ung_Tuyen 
(ma_gia_su, ma_yeu_cau, loi_nhan, muc_hoc_phi_de_xuat, trang_thai)
VALUES
(1,1,'Em co kinh nghiem day lop 10',200000,'Cho duyet'),
(2,1,'Da day 4 nam mon Toan',220000,'Cho duyet'),
(1,2,'Day tieng anh giao tiep',180000,'Cho duyet');
GO

INSERT INTO Lop_Hoc
(ma_hoc_vien, ma_gia_su, ma_mon, hoc_phi_thoa_thuan, ngay_bat_dau, trang_thai, ma_yeu_cau)
VALUES
(1,2,1,220000,'2026-03-01','Dang hoc',1),
(2,1,4,180000,'2026-03-05','Dang hoc',2);
GO

INSERT INTO Buoi_Hoc
(ma_lop, thoi_gian_bat_dau, thoi_gian_ket_thuc, so_gio_hoc, trang_thai)
VALUES
(1,'2026-03-02 18:00','2026-03-02 20:00',2,'Hoan thanh'),
(1,'2026-03-04 18:00','2026-03-04 20:00',2,'Hoan thanh'),
(2,'2026-03-06 19:00','2026-03-06 21:00',2,'Hoan thanh');
GO

INSERT INTO Thanh_Toan
(ma_lop, so_tien, phuong_thuc, trang_thai)
VALUES
(1,440000,'Chuyen khoan','Da thanh toan'),
(2,360000,'Tien mat','Da thanh toan');
GO

INSERT INTO Lich_Su_Giao_Dich
(ma_thanh_toan, so_tien, loai)
VALUES
(1,440000,'Thanh toan hoc phi'),
(2,360000,'Thanh toan hoc phi');
GO

INSERT INTO Danh_Gia
(ma_lop, diem, nhan_xet)
VALUES
(1,5,'Gia su day rat de hieu'),
(2,4,'Gia su nhiet tinh');
GO
