package com.halfacode.japserreport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConditionDto {
    private Integer rowNumber;
    private String conditionText;
    private Boolean compliant;
    private Boolean nonCompliant;
    private String classification;
    private Integer occurrenceCount;
    private String side;
    private List<ConditionImageDto> images;
}

