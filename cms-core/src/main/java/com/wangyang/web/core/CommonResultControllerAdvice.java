package com.wangyang.web.core;

import com.wangyang.common.BaseResponse;
import com.wangyang.common.exception.ObjectException;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.HashMap;
import java.util.Map;
//@ControllerAdvice({"com.wangyang.web.controller","com.wangyang.authorize.controller","com.wangyang.schedule.controller"})
@ControllerAdvice
@Slf4j
public class CommonResultControllerAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return AbstractJackson2HttpMessageConverter.class.isAssignableFrom(converterType);
    }

    @Override
    @NonNull
    public final Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType,
                                        MediaType contentType, Class<? extends HttpMessageConverter<?>> converterType,
                                        ServerHttpRequest request, ServerHttpResponse response) {
        MappingJacksonValue container = getOrCreateContainer(body);
        // The contain body will never be null
        beforeBodyWriteInternal(container, contentType, returnType, request, response);
        return container;
    }

    /**
     * Wrap the body in a {@link MappingJacksonValue} value container (for providing
     * additional serialization instructions) or simply cast it if already wrapped.
     */
    private MappingJacksonValue getOrCreateContainer(Object body) {
        return (body instanceof MappingJacksonValue ? (MappingJacksonValue) body : new MappingJacksonValue(body));
    }

    private void beforeBodyWriteInternal(MappingJacksonValue bodyContainer,
                                         MediaType contentType,
                                         MethodParameter returnType,
                                         ServerHttpRequest request,
                                         ServerHttpResponse response) {
        // Get return body
        Object returnBody = bodyContainer.getValue();

        if (returnBody instanceof BaseResponse) {
            // If the return body is instance of BaseResponse
            BaseResponse<?> baseResponse = (BaseResponse) returnBody;
            response.setStatusCode(HttpStatus.resolve(baseResponse.getStatus()));
            return;
        }
        if(returnBody instanceof  Map){
            Map map = (Map) returnBody;
            if(map.get("status")!=null && map.get("path")!=null){
                if( map.get("status").equals(404)){
                    if(map.get("path").toString().endsWith("css")
                            || map.get("path").toString().endsWith("js")
                            || map.get("path").toString().endsWith(".map")
                            || map.get("path").toString().endsWith(".ico")){
                        log.info(map.get("path").toString()+"不存在！");
                    }else {
                        throw new ObjectException( map.get("path").toString()+"不存在！");
                    }

                }
            }

        }

        // Wrap the return body
        BaseResponse<?> baseResponse = BaseResponse.ok(returnBody);
        bodyContainer.setValue(baseResponse);
        response.setStatusCode(HttpStatus.valueOf(baseResponse.getStatus()));
    }
}
