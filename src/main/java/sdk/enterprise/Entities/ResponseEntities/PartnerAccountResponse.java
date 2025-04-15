package sdk.enterprise.Entities.ResponseEntities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartnerAccountResponse {
    private String partnerId;
    private String email;
    private String country;
    private String contactFirstName;
    private String contactLastName;
    private String legalBusinessName;
    private long mobileNumber;
    private String  createdAt;
}
