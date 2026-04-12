package com.jamilxt.java_springboot_japserreport.dto;

import lombok.Data;

@Data
public class BuildingInfo {

    private String buildingType;       // نوع المبنى (سكني – تجاري – صناعي – …)
    private String buildingDescription;// وصف البناء
    private boolean isFullyFinished;   // مبنية بالكامل
    private boolean isPartiallyBuilt;  // جزء من المباني في الرخصة
    private String landNumber;         // رقم الأرض
    private String blockNumber;        // رقم البلك
    private String planNumber;         // رقم المخطط
    private String licenseNumber;      // رقم الرخصة
    private String licenseDate;        // تاريخ الرخصة
    private String district;           // الحي
    private String street;             // الشارع

    // Getters & Setters
}