package com.wangyang.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wangyang.interceptor.BioInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Configuration
//@EnableWebMvc
//https://blog.csdn.net/Yinbin_/article/details/102647745
//https://blog.csdn.net/hou_ge/article/details/119931067
public class WebMvcAutoConfiguration extends WebMvcConfigurationSupport  {
    @Value("${cms.workDir}")
    private String workDir;
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").
                allowedOrigins("*"). //允许跨域的域名，可以用*表示允许任何域名使用
                allowedMethods("*"). //允许任何方法（post、get等）
                allowedHeaders("*"). //允许任何请求头
                allowCredentials(true); //带上cookie信息
//                exposedHeaders(HttpHeaders.SET_COOKIE).maxAge(3600L); //maxAge(3600)表明在3600秒内，不需要再发送预检验请求，可以缓存该结果
    }


    /**
     * 解决拦截器在spring context创建之前完成加载
     * @return
     */
    @Bean
    public BioInterceptor bioInterceptor(){
        return new BioInterceptor();
    }
    /**
     * 配置拦截器
     * @param registry
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(bioInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/templates/**","/admin/**","/favicon.ico","/api/user/login", "/logout/**", "/loginPage/**", "/error/**",
                        "/doc.html","/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**");
    }
//    #    static-locations: file:${cms.workDir}/html/, file:${cms.workDir}/, classpath:/static/
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("file:"+workDir+"/")//file:/home/wy/.bioinfo/
                .addResourceLocations("file:"+workDir+"/html/")
                .addResourceLocations("file:"+workDir+"/templates/")
                .addResourceLocations("classpath:/static/");


        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");;
        super.addResourceHandlers(registry);
    }



    /**
     * 解决Pageable pageable不能作为参数的问题
     * @param argumentResolvers
     */
    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add( new PageableHandlerMethodArgumentResolver());
    }

}
