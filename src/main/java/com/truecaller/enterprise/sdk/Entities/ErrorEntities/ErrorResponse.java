package com.truecaller.enterprise.sdk.Entities.ErrorEntities;

import lombok.Data;

@Data
public class ErrorResponse {
    private Integer status;
    private String message;
}
