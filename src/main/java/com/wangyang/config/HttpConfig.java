package com.wangyang.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpConfig {
    /**
     * 路径特殊字符允许
     * @return
     */
//    @Bean
//    public ConfigurableServletWebServerFactory webServerFactory() {
//        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
////        {
////            @Override
////            protected void postProcessContext(Context context) {
////                SecurityConstraint constraint = new SecurityConstraint();
////                constraint.setUserConstraint("CONFIDENTIAL");
////                SecurityCollection collection = new SecurityCollection();
////                collection.addPattern("/*");
////                constraint.addCollection(collection);
////                context.addConstraint(constraint);
////            }
////        };
//        tomcat.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> connector.setProperty("relaxedQueryChars", "^+|{}[]\\"));
//        tomcat.addAdditionalTomcatConnectors(createStandardConnector());
//
//        return tomcat;
//    }
//
////    @Bean
////    public ServletWebServerFactory servletContainer() {
////        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
////        tomcat.addAdditionalTomcatConnectors(createStandardConnector());
////        return tomcat;
////    }
//
//    private Connector createStandardConnector() {
//        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//        connector.setScheme("http");
//        connector.setPort(8080);
////        connector.setSecure(false);
////        connector.setRedirectPort(8989);
//        return connector;
//    }

}
