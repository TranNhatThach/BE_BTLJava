package com.btljava.GiaSu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Mon_Hoc")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonHoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_mon")
    private Integer maMon;

    @Column(name = "ten_mon", columnDefinition = "nvarchar(100)")
    private String tenMon;

    @Column(name = "mo_ta", columnDefinition = "nvarchar(255)")
    private String moTa;
}
