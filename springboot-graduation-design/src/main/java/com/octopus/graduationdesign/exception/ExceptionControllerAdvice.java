package com.octopus.graduationdesign.exception;

import com.octopus.graduationdesign.properties.OProperties;
import com.octopus.graduationdesign.utils.R;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = {"pers.djz.gulimall.product.controller"})
public class ExceptionControllerAdvice {
    @ExceptionHandler(Throwable.class)
    public R handleException(Throwable throwable) {
        throwable.printStackTrace();
        return R.error(OProperties.HTTP_INTERNAL_SERVER_ERROR).put("message", throwable.getMessage()).put("exception_class", throwable.getClass());
    }
}
