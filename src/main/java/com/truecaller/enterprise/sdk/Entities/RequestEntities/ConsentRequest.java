package com.truecaller.enterprise.sdk.Entities.RequestEntities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsentRequest {
    private String appName;
    private List<String> scopes;
    private String userSupportEmail;
    private String developerEmail;
    private String developerName;
    private String privacyPolicyUrl;
    private String tosUrl;
    private String homePageUrl;
    private List<String> mandatoryScopes;
}
