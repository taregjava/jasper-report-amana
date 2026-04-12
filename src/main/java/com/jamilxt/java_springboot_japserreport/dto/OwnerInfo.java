package com.jamilxt.java_springboot_japserreport.dto;

import lombok.Data;

@Data
public class OwnerInfo {

    private String ownerName;        // اسم المالك
    private String idNumber;         // رقم الهوية
    private String mobileNumber;     // رقم الجوال
    private String engineeringOffice; // المكتب الهندسي المشرف
    private String designerOffice;    // المكتب الهندسي المصمم
    private String contractorName;    // اسم المقاول
    private String contractorLicense; // سجل المقاول
    private String projectName;       // اسم المشروع وعنوانه
    private String reportDate;        // تاريخ التقرير

    // Getters & Setters
}
