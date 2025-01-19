package com.test.cas.config;

import com.test.cas.handler.MyAuthenticationHandler;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlan;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlanConfigurer;
import org.apereo.cas.authentication.AuthenticationHandler;
import org.apereo.cas.authentication.principal.DefaultPrincipalFactory;
import org.apereo.cas.services.ServicesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: wangsaichao
 * @date: 2018/7/21
 * @description: 注册验证器
 */
@Configuration("myAuthenticationConfiguration")
public class MyAuthenticationConfiguration implements AuthenticationEventExecutionPlanConfigurer {

    @Autowired
    @Qualifier("servicesManager")
    private ServicesManager servicesManager;

    @Bean
    public AuthenticationHandler adminAuthenticationHandler() {
        return new MyAuthenticationHandler(
                MyAuthenticationHandler.class.getSimpleName(), // 处理器名称
                servicesManager, // ServicesManager
                new DefaultPrincipalFactory(), // PrincipalFactory
                1 // 处理器顺序
        );
    }

    @Override
    public void configureAuthenticationExecutionPlan(AuthenticationEventExecutionPlan plan) {
        // 注册自定义的认证处理器
        plan.registerAuthenticationHandler(adminAuthenticationHandler());
    }
}