package sdk.enterprise.Entities.ResponseEntities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CredentialsResponse {
    private String clientId;
    private String platform;
    private String created;
    private Metadata metadata;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Metadata {
        private String packageName;
        private List<Fingerprint> fingerPrints;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Fingerprint {
        private String fingerPrint;
        private String label;
    }
}
