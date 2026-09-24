package com.aierp.common.result;

import lombok.Data;

@Data
public class Result<T> {
    private Integer code;
    private String message;
    private T data;
    private static <T> Result<T> build(ResultCode code,T data){
        Result<T> result = new Result<>();
        result.setCode(code.getCode());
        result.setData(data);
        result.setMessage(code.getMessage());
        return result;
    }
    public static <T> Result<T> success(){
        return build(ResultCode.SUCCESS,null);
    }
    public static <T> Result<T> success(T data){
        return build(ResultCode.SUCCESS,data);
    }
    public static <T> Result<T> fail(ResultCode resultCode){
        return build(resultCode,null);
    }
    public static <T> Result<T> fail(ResultCode resultCode,String message){
        Result<T> result = new Result<>();
        result.setCode(resultCode.getCode());
        result.setMessage(message);
        return result;
    }
    public static <T> Result<T> fail(Integer code,String message){
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
