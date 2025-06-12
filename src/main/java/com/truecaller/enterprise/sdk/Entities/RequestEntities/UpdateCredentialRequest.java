package com.truecaller.enterprise.sdk.Entities.RequestEntities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCredentialRequest {
    private String fingerPrint;
    private String newFingerPrint;
    private String newLabel;
}