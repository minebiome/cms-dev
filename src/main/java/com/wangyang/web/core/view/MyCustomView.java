package com.wangyang.web.core.view;

import com.wangyang.common.CmsConst;
import com.wangyang.common.utils.CMSUtils;
import com.wangyang.common.utils.ServiceUtil;
import com.wangyang.common.utils.TemplateUtil;
import com.wangyang.config.CmsConfig;
import com.wangyang.pojo.authorize.Role;
import com.wangyang.pojo.authorize.User;
import com.wangyang.pojo.authorize.UserDetailDTO;
import com.wangyang.util.AuthorizationUtil;
import org.apache.el.lang.EvaluationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.View;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.expression.ThymeleafEvaluationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.Beans;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class MyCustomView implements View {

    private ApplicationContext applicationContext;

    private ConversionService mvcConversionService;

    private String viewName;
    public  MyCustomView(String viewName,ApplicationContext applicationContext,ConversionService mvcConversionService){
        this.viewName = viewName;
        this.applicationContext =applicationContext;
        this.mvcConversionService =mvcConversionService;
    }
    @Override
    public void render(Map<String, ?> mapInput, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String,Object> map=(Map<String, Object>) mapInput;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        if(viewName.startsWith("redirect:")){
            String redirectPath = viewName.substring("redirect:".length());
            response.sendRedirect(redirectPath);
            return;
        }else if(!viewName.startsWith("html")){
            viewName = CMSUtils.getTemplates()+viewName;
        }

        String viewNamePath = viewName.replace("_", File.separator);
        if(viewName.equals("error")){
            viewNamePath =CMSUtils.getTemplates()+"error";
        }
        String path = CmsConst.WORK_DIR+ File.separator+viewNamePath+".html";
        UserDetailDTO user = AuthorizationUtil.getUser(request);
        if(user!=null){
            map.put("username",user.getUsername());
            map.put("userId",user.getId());
            if(user.getRoles()!=null && user.getRoles().size()>0){
                Set<String> strings = ServiceUtil.fetchProperty(user.getRoles(), Role::getEnName);
                map.put("roles",strings);
            }else {
                map.put("roles",new HashSet<>());
            }
        }
//        https://stackoverflow.com/questions/38518377/thymeleaf-email-template-and-conversionservice
        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(),map);
        final ThymeleafEvaluationContext evaluationContext = new ThymeleafEvaluationContext(applicationContext, mvcConversionService);
        ctx.setVariable(ThymeleafEvaluationContext.THYMELEAF_EVALUATION_CONTEXT_CONTEXT_VARIABLE_NAME, evaluationContext);
        String requestURI = request.getRequestURI();
        ctx.setVariable("URL",requestURI);
//        ctx.setVariable(ThymeleafEvaluationContext.THYMELEAF_EVALUATION_CONTEXT_CONTEXT_VARIABLE_NAME,
//                applicationContext);

        ITemplateEngine templateEngine = TemplateUtil.getWebEngine();
        String[] pathArgs = viewName.split("_");
        if(!Paths.get(path).toFile().exists()&&!invokeGenerateHtml(pathArgs)){
            viewNamePath = CMSUtils.getTemplates()+"error";
            if(!Paths.get(path).toFile().exists()){
                ctx.setVariable("message","模板不存在："+path);
            }
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }
        templateEngine.process(viewNamePath,ctx,response.getWriter());
    }

    /**
     * 文件不存在，查看GenerateHtml存是否存在生成html的方法，生成之后再用视图名称渲染
     * @see GenerateHtml
     * @param pathArgs
     */
    public boolean invokeGenerateHtml(String[] pathArgs) {
        if(pathArgs.length<2){
            return false;
        }
        pathArgs = pathArgs[1].split("-");

        try {
            GenerateHtml generateHtml = CmsConfig.getBean(GenerateHtml.class);
            Method[] methods = generateHtml.getClass().getDeclaredMethods();
            for (Method method: methods){
                if(method.getName().equals(pathArgs[pathArgs.length-1])){
                    method.invoke(generateHtml,new Object[]{pathArgs});
                    return true;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public String getContentType() {
        //相当于response.setContextType()
        return "text/html;charset=utf-8";
    }
}
