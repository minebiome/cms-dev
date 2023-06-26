package com.wangyang.config;


import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpConfig {

    @Value("${http.port}")
    private Integer httpPort;

    @Value("${server.port}")
    private Integer httpsPort;
    @Bean
    public ServletWebServerFactory servletContainer(){
        final Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setPort(httpPort);
        final TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();

        tomcat.addAdditionalTomcatConnectors(connector);
        return tomcat;
    }

    /**
     * 路径特殊字符允许
     * @return
     */
//    @Bean
//    public ConfigurableServletWebServerFactory webServerFactory() {
//        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory()
//        {
//            @Override
//            protected void postProcessContext(Context context) {
//                SecurityConstraint constraint = new SecurityConstraint();
//                constraint.setUserConstraint("CONFIDENTIAL");
//                SecurityCollection collection = new SecurityCollection();
//                collection.addPattern("/*");
//                constraint.addCollection(collection);
//                context.addConstraint(constraint);
//            }
//        };
//        tomcat.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> connector.setProperty("relaxedQueryChars", "^+|{}[]\\"));
//        tomcat.addAdditionalTomcatConnectors(createStandardConnector());
//
//        return tomcat;
//    }
//
//
//    private Connector createStandardConnector() {
//        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//        connector.setScheme("http");
//        connector.setPort(httpPort); // 80
//        connector.setSecure(false);
//        connector.setRedirectPort(httpsPort); //443
//        return connector;
//    }

}
