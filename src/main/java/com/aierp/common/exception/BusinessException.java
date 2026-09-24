package com.aierp.common.exception;

import com.aierp.common.result.ResultCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final Integer code;

    public BusinessException(Integer code,String message){
        super(message);
        this.code = code;
    }
    public BusinessException(ResultCode resultCode){
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }
    public BusinessException(ResultCode resultCode,String message){
        super(message);
        this.code = resultCode.getCode();
    }
}
