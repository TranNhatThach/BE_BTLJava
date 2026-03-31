package com.btljava.GiaSu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Tin_Nhan")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TinNhan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_tin_nhan")
    private Long maTinNhan;

    @Column(name = "room_id", nullable = false, length = 50)
    private String roomId;

    @Column(name = "sender_id")
    private Integer senderId;

    @Column(name = "sender_role", length = 20)
    private String senderRole;

    @Column(name = "sender_name", length = 100, columnDefinition = "nvarchar(100)")
    private String senderName;

    @Column(name = "noi_dung", nullable = false, columnDefinition = "nvarchar(max)")
    private String noiDung;

    @CreationTimestamp
    @Column(name = "ngay_gui", updatable = false)
    private LocalDateTime ngayGui;
}
