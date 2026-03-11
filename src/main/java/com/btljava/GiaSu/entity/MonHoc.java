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

    @Column(name = "ten_mon", length = 100)
    private String tenMon;

    @Column(name = "mo_ta", length = 255)
    private String moTa;
}
