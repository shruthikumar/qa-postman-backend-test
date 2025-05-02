package sdk.enterprise.Entities.ResponseEntities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailsOfAllProjectsResponse {
    private String id;
    private String name;
    private String mode;
    private String status;
    private BusinessCategory businessCategory;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BusinessCategory {
        private String id;
        private String label;
    }
}

