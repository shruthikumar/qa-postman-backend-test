package com.truecaller.enterprise.sdk.Entities.RequestEntities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CredentialsRequest {
    private String platform;
    private Metadata metadata;
    private String clientId;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Metadata {
        private String packageName;
        private List<Fingerprint> fingerPrints;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Fingerprint {
        private String fingerPrint;
        private String label;
    }
}


