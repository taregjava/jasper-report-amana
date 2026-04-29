package com.halfacode.japserreport.dto;

import lombok.Data;

@Data
public class TaskRow {

    private int index;         // رقم المهمة (م)
    private String taskName;   // اسم المهمة
    private String compliant;  // مطابق / غير مطابق / لا ينطبق
    private String notes;      // ملاحظات
    private String groupTitle; // عنوان المجموعة مثل: متطلبات سور الحماية
    private String sideNumber; // الرقم الجانبي المدمج مثل: 2 / 3

    // Alias for payloads that send "status" instead of "compliant".
    public String getStatus() {
        return compliant;
    }

    public void setStatus(String status) {
        this.compliant = status;
    }


}