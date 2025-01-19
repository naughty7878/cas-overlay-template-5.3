package com.test.cas.handler;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apereo.cas.authentication.AuthenticationHandlerExecutionResult;
import org.apereo.cas.authentication.Credential;
import org.apereo.cas.authentication.PreventedException;
import org.apereo.cas.authentication.UsernamePasswordCredential;
import org.apereo.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.services.ServicesManager;
import org.springframework.beans.factory.annotation.Autowired;

import javax.security.auth.login.FailedLoginException;
import java.security.GeneralSecurityException;
import java.util.Collections;

/**
 * 自定义验证器
 */
@Slf4j
public class MyAuthenticationHandler extends AbstractUsernamePasswordAuthenticationHandler {

    @Autowired
    private ObjectMapper objectMapper;

    public MyAuthenticationHandler(String name, ServicesManager servicesManager, PrincipalFactory principalFactory, Integer order) {
        super(name, servicesManager, principalFactory, order);
    }

    @Override
    protected AuthenticationHandlerExecutionResult authenticateUsernamePasswordInternal(UsernamePasswordCredential credential, String originalPassword)
            throws GeneralSecurityException, PreventedException {

        try {
            // 打印凭证信息
            log.info("credential: {}", objectMapper.writeValueAsString(credential));
        } catch (JsonProcessingException e) {

        }
        // 获取用户名
        String username = credential.getUsername();

        // 检查用户名是否为 "admin"
        if ("admin".equals(username)) {
            // 认证成功，返回凭证
            return createHandlerResult(credential, this.principalFactory.createPrincipal(username, Collections.emptyMap()));
        } else {
            // 认证失败，抛出异常
            throw new FailedLoginException("Invalid username: " + username);
        }
    }

    @Override
    public boolean supports(Credential credential) {
        // 仅支持 UsernamePasswordCredential 类型的凭证
        return credential instanceof UsernamePasswordCredential;
    }
}