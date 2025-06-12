package com.truecaller.enterprise.sdk.Entities.ResponseEntities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsentResponse {
    private String summary;
    private List<Scope> scopes;
    private String appLogoUrl;
    private String appName;
    private String userSupportEmail;
    private String developerEmail;
    private String developerName;
    private String privacyPolicyUrl;
    private String tosUrl;
    private String homePageUrl;
    private List<String> mandatoryScopes;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Scope {
        private String name;
        private List<String> children;
        private String description;
        private String title;
    }
}

