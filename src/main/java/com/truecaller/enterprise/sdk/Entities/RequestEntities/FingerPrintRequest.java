package com.truecaller.enterprise.sdk.Entities.RequestEntities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FingerPrintRequest {
    private String fingerPrint;
    private String label;
}