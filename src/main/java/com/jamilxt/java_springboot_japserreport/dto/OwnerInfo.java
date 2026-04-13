package com.jamilxt.java_springboot_japserreport.dto;

import lombok.Data;

@Data
public class OwnerInfo {

    private String ownerName;                 // اسم المالك
    private String idNumber;                  // رقم الهوية (legacy)
    private String ownerIdNumber;             // رقم الهوية (preferred param name)
    private String mobileNumber;              // رقم الجوال (legacy)
    private String ownerMobile;               // رقم الجوال (preferred param name)
    private String deedNumber;                // رقم الصك
    private String engineeringOffice;         // المكتب الهندسي المشرف (legacy)
    private String supervisingEngineeringOffice; // المكتب الهندسي المشرف (preferred param name)
    private String designerOffice;            // المكتب الهندسي المصمم (legacy)
    private String designingEngineeringOffice;// المكتب الهندسي المصمم (preferred param name)
    private String contractorName;            // اسم المقاول
    private String contractorLicense;         // سجل المقاول (legacy)
    private String contractorRecord;          // سجل المقاول (preferred param name)
    private String contractorMobile;          // جوال المقاول (preferred param name)
    private String projectName;               // اسم المشروع وعنوانه (legacy)
    private String projectNameAndAddress;     // اسم المشروع وعنوانه (preferred param name)
    private String supervisingEngineerName;   // اسم المهندس المشرف
    private String reportDate;                // تاريخ التقرير
    private String stageInspectionResult;     // نتيجة فحص المرحلة
    private String stageInspectionNotes;      // ملاحظات الفحص

    // Getters & Setters
}
