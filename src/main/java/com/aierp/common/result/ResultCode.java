package com.aierp.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {
    SUCCESS(200,"成功"),
    PARAM_ERROR(400,"参数错误"),
    UNAUTHORIZED(401,"未登录或登录已失效"),
    FORBIDDEN(403,"无权限访问"),
    NOT_FOUND(404,"资源不存在"),
    BUSINESS_ERROR(1000,"业务异常"),
    SYSTEM_ERROR(500,"系统异常");
    private final Integer code;
    private final String message;
}
