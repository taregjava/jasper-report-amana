package com.halfacode.japserreport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConditionImageDto {
    private Integer rowNumber;
    private String conditionText;
    private byte[] imagePath;
    private String noteText;
}

