package com.jamilxt.java_springboot_japserreport.dto;

import lombok.Data;

@Data
public class ProjectPhotosDTO {
    // مسارات الصور (أو يمكن استخدام InputStream / RenderedImage)
    private String spatialPortalPhoto;  // صورة الموقع طبقاً للبوابة المكانية
    private String implementationPhoto;  // صور أعمال التنفيذ
    private String aerialPhoto;          // صورة جوية لموقع المبنى

    private String officeName;      //   "اسم المكتب"
    private String digitalStampPath; // مسار صورة الختم (إذا كان رقمياً)
}