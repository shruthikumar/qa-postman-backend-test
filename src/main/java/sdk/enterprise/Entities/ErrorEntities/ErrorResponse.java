package sdk.enterprise.Entities.ErrorEntities;

import lombok.Data;

@Data
public class ErrorResponse {
    private Integer status;
    private String message;
}
