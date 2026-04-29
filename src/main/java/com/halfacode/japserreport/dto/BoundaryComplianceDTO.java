package com.halfacode.japserreport.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Boundary Compliance (Setbacks & Protrusions)
 * Contains verification data for North, South, East, West boundaries
 */
@Data
@NoArgsConstructor
public class BoundaryComplianceDTO {

    // Setbacks (Boundary distances)
    private SetbackDirection northSetback;
    private SetbackDirection southSetback;
    private SetbackDirection eastSetback;
    private SetbackDirection westSetback;

    // Protrusions (Overhang distances)
    private ProtrustionDirection northProtrusion;
    private ProtrustionDirection southProtrusion;
    private ProtrustionDirection eastProtrusion;
    private ProtrustionDirection westProtrusion;

    @Data
    @NoArgsConstructor
    public static class SetbackDirection {
        private String direction;           // شمال, جنوب, شرق, غرب
        private String boundaryDescription; // وصف الحد (e.g., شارع, جار)
        private Double licenseMeasure;      // القياس حسب الرخصة
        private Double planMeasure;         // القياس حسب المخطط
        private Double executedMeasure;     // القياس المنفذ الفعلي
        private Double increase;             // الزيادة
        private Boolean isCompliant;        // مطابق
        private Boolean isNonCompliant;     // غير مطابق
        private Boolean isNotApplicable;    // لا ينطبق
        private String notes;                // ملاحظات

        public SetbackDirection(String direction, String boundary, Double license, Double plan, 
                                Double executed, Double increase, Boolean compliant, String notes) {
            this.direction = direction;
            this.boundaryDescription = boundary;
            this.licenseMeasure = license;
            this.planMeasure = plan;
            this.executedMeasure = executed;
            this.increase = increase;
            this.isCompliant = compliant;
            this.isNonCompliant = !compliant;
            this.notes = notes;
        }
    }

    @Data
    @NoArgsConstructor
    public static class ProtrustionDirection {
        private String direction;           // شمال, جنوب, شرق, غرب
        private String boundaryDescription; // وصف الحد
        private Double licenseMeasure;      // القياس حسب الرخصة
        private Double planMeasure;         // القياس حسب المخطط
        private Double executedMeasure;     // القياس المنفذ الفعلي
        private Double increase;             // الزيادة
        private Boolean isCompliant;        // مطابق
        private Boolean isNonCompliant;     // غير مطابق
        private Boolean isNotApplicable;    // لا ينطبق
        private String notes;                // ملاحظات

        public ProtrustionDirection(String direction, String boundary, Double license, Double plan,
                                    Double executed, Double increase, Boolean compliant, String notes) {
            this.direction = direction;
            this.boundaryDescription = boundary;
            this.licenseMeasure = license;
            this.planMeasure = plan;
            this.executedMeasure = executed;
            this.increase = increase;
            this.isCompliant = compliant;
            this.isNonCompliant = !compliant;
            this.notes = notes;
        }
    }
}

