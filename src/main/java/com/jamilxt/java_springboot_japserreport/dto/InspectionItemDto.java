package com.jamilxt.java_springboot_japserreport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InspectionItemDto {

    private Integer itemNumber;
    private String description;
    private String status;
    private String classification;
    private Integer repeat;

    public String getMatch() {
        return "MATCHED".equals(status) ? "⭕" : "";
    }

    public String getNotMatch() {
        return "NOT_MATCHED".equals(status) ? "⭕" : "";
    }

    public String getRepeat() {
        return repeat == null ? "" : repeat.toString();
    }
}