package com.jamilxt.java_springboot_japserreport.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Building Components
 * Contains verification data for floors, rooms, parking, and utilities
 */
@Data
@NoArgsConstructor
public class BuildingComponentsDTO {

    // Building floors & components
    private ComponentVerification basement;        // قبو
    private ComponentVerification groundFloor;     // الدور الأرضي
    private ComponentVerification mezzanine;       // طابق الميزانين
    private ComponentVerification firstFloor;      // الدور الأول
    private ComponentVerification secondFloor;     // الدور الثاني
    private ComponentVerification thirdFloor;      // الدور الثالث
    private ComponentVerification fourthFloor;     // الدور الرابع
    private ComponentVerification roofAnnex;       // الملحق العلوي
    private ComponentVerification stairs;          // درج
    private ComponentVerification fences;          // أسوار
    private ComponentVerification electricityRoom; // غرفة كهرباء

    // Parking section
    private ParkingVerification parking;            // المواقف

    // Utilities section
    private UtilityRoomVerification electricityBox; // غرفة التوزيع الكهربائية

    @Data
    @NoArgsConstructor
    public static class ComponentVerification {
        private String componentName;            // مكونات المبنى (e.g., قبو)
        private String usage;                    // الاستخدام (سكني, تجاري, صناعي)
        private Integer numberOfUnits;           // عدد الوحدات
        private Double componentHeight;          // ارتفاع المكون (متر)
        private Double planArea;                 // المساحة حسب المخطط
        private Double executedArea;             // المساحة المنفذة الفعلية
        private Boolean isCompliant;             // مطابق
        private Boolean isNonCompliant;          // غير مطابق
        private Boolean isNotApplicable;         // لا ينطبق
        private String notes;                    // ملاحظات

        public ComponentVerification(String name, String usage, Integer units, Double height,
                                     Double planArea, Double execArea, Boolean compliant, String notes) {
            this(name, usage, units, height, planArea, execArea, compliant, false, notes);
        }

        public ComponentVerification(String name, String usage, Integer units, Double height,
                                     Double planArea, Double execArea,
                                     Boolean compliant, Boolean notApplicable, String notes) {
            this.componentName = name;
            this.usage = usage;
            this.numberOfUnits = units;
            this.componentHeight = height;
            this.planArea = planArea;
            this.executedArea = execArea;
            boolean applicable = !Boolean.TRUE.equals(notApplicable);
            this.isNotApplicable = !applicable;
            this.isCompliant = applicable && Boolean.TRUE.equals(compliant);
            this.isNonCompliant = applicable && !Boolean.TRUE.equals(compliant);
            this.notes = notes;
        }
    }

    @Data
    @NoArgsConstructor
    public static class ParkingVerification {
        private String description;              // وصف (مواقف)
        private Integer requiredNumber;          // العدد المطلوب حسب الرخصة
        private Integer executedNumber;          // العدد المنفذ الفعلي
        private Boolean isCompliant;             // مطابق
        private Boolean isNonCompliant;          // غير مطابق
        private String notes;                    // ملاحظات

        public ParkingVerification(Integer required, Integer executed, Boolean compliant, String notes) {
            this.description = "مواقف";
            this.requiredNumber = required;
            this.executedNumber = executed;
            this.isCompliant = compliant;
            this.isNonCompliant = !compliant;
            this.notes = notes;
        }
    }

    @Data
    @NoArgsConstructor
    public static class UtilityRoomVerification {
        private String description;              // وصف (غرفة توزيع كهرباء)
        private Double planLength;               // الطول حسب المخطط
        private Double planWidth;                // العرض حسب المخطط
        private Double executedLength;           // الطول حسب الطبيعة
        private Double executedWidth;            // العرض حسب الطبيعة
        private Boolean isCompliant;             // مطابق
        private Boolean isNonCompliant;          // غير مطابق
        private String notes;                    // ملاحظات

        public UtilityRoomVerification(Double length, Double width, Boolean compliant, String notes) {
            this(length, width, null, null, compliant, notes);
        }

        public UtilityRoomVerification(Double planLength, Double planWidth,
                                       Double executedLength, Double executedWidth,
                                       Boolean compliant, String notes) {
            this.description = "غرفة التوزيع الكهربائية";
            this.planLength = planLength;
            this.planWidth = planWidth;
            this.executedLength = executedLength;
            this.executedWidth = executedWidth;
            this.isCompliant = compliant;
            this.isNonCompliant = !compliant;
            this.notes = notes;
        }
    }
}

