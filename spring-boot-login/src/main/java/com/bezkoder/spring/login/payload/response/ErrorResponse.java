package com.bezkoder.spring.login.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
public class ErrorResponse {
    private Integer statusCode;
    private HttpStatus status;
    private String reason;
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy hh:mm:ss", timezone = "Asia/Dhaka")
    private Date timestamp;

//    private Map<String, List<ValidationError>> validationErrors = new HashMap<>();
}
