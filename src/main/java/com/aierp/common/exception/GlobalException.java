package com.aierp.common.exception;

import com.aierp.common.result.Result;
import com.aierp.common.result.ResultCode;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.BindException;
import java.util.Locale;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalException  {

    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e){
        return Result.fail(e.getCode(), e.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(";"));
        return Result.fail(ResultCode.PARAM_ERROR,message);
    }
    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException e){
        String message = e.getMessage();
        return Result.fail(ResultCode.PARAM_ERROR,message);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Void> handleConstraintViolationException(
            ConstraintViolationException e) {

        return Result.fail(
                ResultCode.PARAM_ERROR,
                e.getMessage()
        );
    }
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        return Result.fail(ResultCode.SYSTEM_ERROR);
    }
}
