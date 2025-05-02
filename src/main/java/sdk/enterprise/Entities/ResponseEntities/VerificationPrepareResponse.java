package sdk.enterprise.Entities.ResponseEntities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerificationPrepareResponse {
    private String id;
    private String appName;
    private List<String> scopes;
    private String userSupportEmail;
    private String developerEmail;
    private String developerName;
    private String privacyPolicyUrl;
    private String tosUrl;
    private String homePageUrl;
    private String appLogoUrl;
    private String createdAt;
    private List<String> mandatoryScopes;
}
