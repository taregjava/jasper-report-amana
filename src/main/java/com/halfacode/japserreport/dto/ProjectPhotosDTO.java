package com.halfacode.japserreport.dto;

import lombok.Data;

@Data
public class ProjectPhotosDTO {
    // مسارات الصور (أو يمكن استخدام InputStream / RenderedImage)
    private String spatialPortalPhoto;  // صورة الموقع طبقاً للبوابة المكانية
    private String implementationPhoto;  // صور أعمال التنفيذ
    private String aerialPhoto;          // صورة جوية لموقع المبنى

    // Top grid (2x2): صور أعمال التنفيذ 1..4
    private String implementationPhoto1;
    private String implementationPhoto2;
    private String implementationPhoto3;
    private String implementationPhoto4;

    // Bottom grid (3x2): صور تفصيلية 1..6
    private String detailPhoto1;
    private String detailPhoto2;
    private String detailPhoto3;
    private String detailPhoto4;
    private String detailPhoto5;
    private String detailPhoto6;

    // وصف الصورة لكل عنصر في الشبكة السفلية
    private String detailDescription1;
    private String detailDescription2;
    private String detailDescription3;
    private String detailDescription4;
    private String detailDescription5;
    private String detailDescription6;

    private String officeName;      //   "اسم المكتب"
    private String digitalStampPath; // مسار صورة الختم (إذا كان رقمياً)
}