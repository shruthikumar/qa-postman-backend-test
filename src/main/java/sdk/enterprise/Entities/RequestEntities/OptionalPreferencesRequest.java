package sdk.enterprise.Entities.RequestEntities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OptionalPreferencesRequest {
    private String partnerComments;
}

