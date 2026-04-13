package com.jamilxt.java_springboot_japserreport.dto;

import lombok.Data;
@Data
public class BuildingInfo {

    // --- الصف الأول: نوع المبنى وحالة البناء ---
    private String buildingType;        // نوع المبنى (سكني، سكني-تجاري، تجاري، فيلا، أخرى)
    private String otherBuildingType;   // في حال اختيار "أخرى يحدد..."

    // حالة البناء على الأرض (Checkboxes في الصورة)
    private boolean isFullyBuilt;       // مبنية بالكامل
    private boolean isPartiallyBuilt;   // جزء من المباني في الرخصة

    // --- الصف الثاني: وصف البناء ---
    private String buildingDescription; // وصف البناء

    // --- الصف الثالث: رقم رخصة البناء وتفاصيلها ---
    private String licenseNumber;       // رقم رخصة البناء
    private String licenseDate;         // تاريخها (يفضل String للتمثيل الهجري/الميلادي)
    private String planNumber;          // رقم المخطط
    private String pieceNumber;         // رقم القطعة (مكتوب في الصورة "رقم القطعة")
    private String floorsCount;         // عدد الطوابق
    private String landNumber;         // رقم الأرض
    private String blockNumber;        // رقم البلك
    // --- الصف الرابع: مساحة الأرض (3 أنواع حسب الصورة) ---
    private Double areaByDeed;          // حسب الصك
    private Double areaByLicense;       // حسب الرخصة
    private Double areaByNature;        // حسب الطبيعة

    // --- الصف الخامس: الموقع ---
    private String sector;              // القطاع
    private String district;            // الحي
    private String street;              // الشارع
}