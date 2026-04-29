package com.halfacode.japserreport.dto;

import lombok.Data;

import java.util.List;

@Data
public class InspectionResponsibilityDTO {

    // --- القسم الأول: إقرار المصمم المشرف ---
    private String supervisorName;          // الاسم بالكامل (المصمم المشرف)
    private String supervisorIdOrLicense;   // رقم الهوية / الشهادة
    private String supervisorSignaturePath; // مسار صورة التوقيع (إن وجد)
    private String officeStampPath;        // مسار صورة ختم المكتب

    // --- القسم الثاني: ملاحظات جهة التفتيش ---
    // قائمة الملاحظات (لتمثيل الصفوف 1، 2، 3)
    private List<String> inspectionNotes;

    // --- القسم الثالث: نتيجة التفتيش ---
    // يمكن استخدام Enum (ACCEPTED, REJECTED, PENDING)
    private String inspectionStatus;

    // --- القسم الرابع: اعتماد جهة التفتيش (الخاص بالمفتش) ---
    private String inspectorName;           // الاسم بالكامل (المفتش)
    private String inspectorIdOrLicense;    // رقم الهوية / الشهادة
    private String inspectorSignaturePath;  // توقيع المفتش
    private String inspectionBodyStampPath; // ختم جهة التفتيش
}