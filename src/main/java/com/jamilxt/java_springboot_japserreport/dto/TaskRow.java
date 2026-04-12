package com.jamilxt.java_springboot_japserreport.dto;

import lombok.Data;

@Data
public class TaskRow {

    private int index;         // رقم المهمة (م)
    private String taskName;   // اسم المهمة
    private String compliant;  // مطابق / غير مطابق / لا ينطبق
    private String notes;      // ملاحظات

    // Getters & Setters
}