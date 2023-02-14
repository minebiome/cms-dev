package com.wangyang.common.utils;

import com.wangyang.common.thymeleaf.CmsFileDialect;
import com.wangyang.common.thymeleaf.CmsWebDialect;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.io.File;

/**
 * @author wangyang
 * @date 2020/12/14
 */
public class HtmlTemplateEngine {

    private static TemplateEngine templateWebEngine ;
    private static TemplateEngine templateEngineFile ;
    /**
     * https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#chaining-template-resolvers
     * @param prefix
     * @param suffix
     * @return templateEngine
     */
    public static TemplateEngine getFileInstance(String prefix, String suffix){
        if(templateEngineFile==null){
            templateEngineFile = new SpringTemplateEngine();
            templateEngineFile.addDialect(new CmsFileDialect());
            FileTemplateResolver fileTemplateResolver = new FileTemplateResolver();
//            templateEngineFile.addDialect(new CmsDialect(null));
            fileTemplateResolver.setOrder(Integer.valueOf(1));
            fileTemplateResolver.setCacheable(false);
            fileTemplateResolver.setPrefix(prefix+ File.separator);
            fileTemplateResolver.setSuffix(suffix);
//            fileTemplateResolver.getResolvablePatternSpec().addPattern("templates/*");
//            fileTemplateResolver.getResolvablePatternSpec().addPattern("html/*");
//            StringTemplateResolver stringTemplateResolver = new StringTemplateResolver();

//            stringTemplateResolver.setOrder(Integer.valueOf(2));
//            stringTemplateResolver.getResolvablePatternSpec().addPattern("str:");
            // 添加字符串模板
//            templateEngineFile.addTemplateResolver(stringTemplateResolver);
            templateEngineFile.addTemplateResolver(fileTemplateResolver);
        }
        return templateEngineFile;
    }

    public static TemplateEngine getWebInstance(String prefix, String suffix){
        if(templateWebEngine==null){
            templateWebEngine = new SpringTemplateEngine();
//            templateWebEngine.addDialect(new CmsDialect(prefix));
//            templateWebEngine.setDialect(new CmsWebDialect());
            FileTemplateResolver fileTemplateResolver = new FileTemplateResolver();
            templateWebEngine.addDialect(new CmsWebDialect());
            fileTemplateResolver.setOrder(Integer.valueOf(1));
            fileTemplateResolver.setCacheable(false);
            fileTemplateResolver.setPrefix(prefix+ File.separator);
            fileTemplateResolver.setSuffix(suffix);
//            fileTemplateResolver.getResolvablePatternSpec().addPattern("templates/*");
//            fileTemplateResolver.getResolvablePatternSpec().addPattern("html/*");
//            StringTemplateResolver stringTemplateResolver = new StringTemplateResolver();
//
//            stringTemplateResolver.setOrder(Integer.valueOf(2));
//            stringTemplateResolver.getResolvablePatternSpec().addPattern("str:");
            // 添加字符串模板
//            templateWebEngine.addTemplateResolver(stringTemplateResolver);
            templateWebEngine.addTemplateResolver(fileTemplateResolver);
        }
        return templateWebEngine;
    }
}
