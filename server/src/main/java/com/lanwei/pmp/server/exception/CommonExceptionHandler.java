package com.lanwei.pmp.server.exception;

import com.lanwei.pmp.common.response.BaseResponse;
import com.lanwei.pmp.common.response.StatusCode;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 通用异常捕获处理器
 * @author lanwei
 * @email 335747949@qq.com
 */
 @RestControllerAdvice
public class CommonExceptionHandler {

    private static final Logger log= LoggerFactory.getLogger(CommonExceptionHandler.class);

    //处理 访问 没有经过授权的 异常
    @ExceptionHandler(AuthorizationException.class)
    public BaseResponse handleAuthorizationException(AuthorizationException e){
        log.info("访问了没有经过授权的操作或者资源：",e.fillInStackTrace());

        return new BaseResponse(StatusCode.CurrUserHasNotPermission);
    }

}
