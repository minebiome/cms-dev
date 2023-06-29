package com.wangyang.web.core;

import com.wangyang.common.exception.CmsException;
import com.wangyang.common.utils.ExceptionUtils;
import com.wangyang.common.utils.ValidationUtils;
import com.wangyang.common.BaseResponse;
import com.wangyang.util.AuthorizationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.Assert;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
//@ControllerAdvice
//@EnableWebMvc
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BaseResponse<Map<String, String>> baseResponse = handleBaseException(e);
        baseResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        baseResponse.setMessage("字段验证错误，请完善后重试！");
        Map<String, String> errMap = ValidationUtils.mapWithFieldError(e.getBindingResult().getFieldErrors());
        baseResponse.setData(errMap);
        return baseResponse;
    }
    @ExceptionHandler(CmsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse cmsException(CmsException e) {
        BaseResponse<Map<String, String>> baseResponse = handleBaseException(e);
        baseResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        return baseResponse;
    }

//    @ExceptionHandler(InternalAuthenticationServiceException.class)
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    public BaseResponse userException(InternalAuthenticationServiceException e) {
//        BaseResponse<Map<String, String>> baseResponse = handleBaseException(e);
//        baseResponse.setStatus(HttpStatus.BAD_REQUEST.value());
//        return baseResponse;
//    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse httpMessageNotReadableException(HttpMessageNotReadableException e) {
        BaseResponse<Map<String, String>> baseResponse = handleBaseException(e);
        baseResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        return baseResponse;
    }
//    // TODO 针对动态页面错误的处理；
//    @ExceptionHandler(AuthorizationException.class)
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    public BaseResponse authorizationException(Exception e) {
//        BaseResponse baseResponse = handleBaseException(e);
//        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
//        baseResponse.setStatus(status.value());
//        baseResponse.setMessage(e.getMessage());
//        return baseResponse;
//    }

    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ModelAndView authorizationException(HttpServletRequest request, HttpServletResponse response, AuthorizationException e) throws IOException {
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("Authorization")){
                    cookie.setValue(null);
                    cookie.setMaxAge(0);// 立即销毁cookie
                    cookie.setPath("/");
//                    System.out.println("被删除的cookie名字为:"+cookie.getName());
                    response.addCookie(cookie);
                    break;
                }
            }
        }
//        response.sendRedirect("/");
        return mvException(e,request,HttpStatus.UNAUTHORIZED.value());
    }
    // TODO 针对动态页面错误的处理；
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse handleGlobalException(Exception e) {
        BaseResponse baseResponse = handleBaseException(e);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        baseResponse.setStatus(status.value());
        baseResponse.setMessage(e.getMessage());
        return baseResponse;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public BaseResponse noHandleFoundException(NoHandlerFoundException e) {
        BaseResponse baseResponse = handleBaseException(e);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        baseResponse.setStatus(status.value());
        baseResponse.setMessage(e.getMessage());
        return baseResponse;
    }


    private <T> BaseResponse<T> handleBaseException(Throwable t) {
        Assert.notNull(t, "Throwable must not be null");

        log.error("Captured an exception", t);

        BaseResponse<T> baseResponse = new BaseResponse<>();
        baseResponse.setMessage(t.getMessage());

        if (log.isDebugEnabled()) {
            baseResponse.setDevMessage(ExceptionUtils.getStackTrace(t));
        }
        return baseResponse;
    }

    private  ModelAndView mvException(Throwable t,HttpServletRequest request,int status) {
        Assert.notNull(t, "Throwable must not be null");

        log.error("Captured an exception", t);
        ModelAndView modelAndView;
        if(!isAjaxRequest(request)){
            modelAndView= new ModelAndView("error");
        }else {
            modelAndView = new ModelAndView(new MappingJackson2JsonView());
        }
        modelAndView.addObject("message",t.getMessage());
        modelAndView.addObject("status",status);

        return modelAndView;
    }
    private boolean isAjaxRequest(HttpServletRequest request) {
        String accept = request.getHeader("accept");
        if (accept != null && accept.indexOf("application/json") != -1) {
            return true;
        }


        return false;
    }

}
