package com.xp.store.utils.response;

import com.xp.store.utils.response.dto.ResponseErrorMessageDto;
import com.xp.store.utils.response.dto.ResponseMessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtils {
    public ResponseEntity<Object> successResponse(HttpStatus httpStatus,int status,String message, Object data){
        return ResponseEntity.status(httpStatus)
                .body(new ResponseMessageDto(
                        message, status, data));
    };

    public ResponseEntity<Object> errorResponse(HttpStatus httpStatus,int status,String message, Object data){
        return ResponseEntity.status(httpStatus)
                .body(new ResponseErrorMessageDto(
                        message, status, data));
    };
}
