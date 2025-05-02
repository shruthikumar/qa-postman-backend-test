package sdk.enterprise.Entities.ResponseEntities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpVerificationStatusResponse {
    private String clientId;
    private String status;
}

