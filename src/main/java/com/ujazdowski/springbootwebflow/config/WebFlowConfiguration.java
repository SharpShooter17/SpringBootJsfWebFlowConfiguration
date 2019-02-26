package com.ujazdowski.springbootwebflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.faces.config.ResourcesBeanDefinitionParser;
import org.springframework.faces.webflow.FlowFacesContextLifecycleListener;
import org.springframework.faces.webflow.JsfResourceRequestHandler;
import org.springframework.util.ClassUtils;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.webflow.config.AbstractFlowConfiguration;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.builder.support.FlowBuilderServices;
import org.springframework.webflow.executor.FlowExecutor;
import org.springframework.webflow.security.SecurityFlowExecutionListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Spring WebFlow configuration.
 */
@Configuration
public class WebFlowConfiguration extends AbstractFlowConfiguration {

    private static final boolean isRichFacesPresent =
            ClassUtils.isPresent("org.richfaces.application.CoreConfiguration",
                    ResourcesBeanDefinitionParser.class.getClassLoader());

    @Bean
    public SimpleUrlHandlerMapping jsrResourceHandlerMapping() {

        Map<String, Object> urlMap = new HashMap<>();
        urlMap.put("/javax.faces.resource/**", jsfResourceRequestHandler());
        if (isRichFacesPresent) {
            urlMap.put("/rfRes/**", jsfResourceRequestHandler());
        }

        SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
        handlerMapping.setUrlMap(urlMap);
        handlerMapping.setOrder(0);
        return handlerMapping;
    }

    @Bean
    public JsfResourceRequestHandler jsfResourceRequestHandler() {
        return new JsfResourceRequestHandler();
    }

    @Bean
    public FlowDefinitionRegistry flowRegistry() {
        return getFlowDefinitionRegistryBuilder()
                .setBasePath("/")
                .addFlowLocationPattern("/**/*-flow.xml")
                .setFlowBuilderServices(this.flowBuilderServices())
                .addFlowLocation("parent-flow.xml")
                .build();
    }

    @Bean
    public FlowExecutor flowExecutor() {
        return getFlowExecutorBuilder(flowRegistry())
                .addFlowExecutionListener(new FlowFacesContextLifecycleListener())
                .addFlowExecutionListener(new SecurityFlowExecutionListener())
                .build();
    }

    @Bean
    public FlowBuilderServices flowBuilderServices() {
        return getFlowBuilderServicesBuilder()
                .setDevelopmentMode(true)
                .build();
    }

}
