package com.truecaller.enterprise.sdk.Entities.RequestEntities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestPhoneNumberRequest {
    private long phoneNumber;
}

