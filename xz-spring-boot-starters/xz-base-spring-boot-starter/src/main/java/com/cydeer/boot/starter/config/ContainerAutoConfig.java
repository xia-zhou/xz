package com.cydeer.boot.starter.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author song.z
 */
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Configuration
@ComponentScan({"com.cydeer.boot.starter.web.support", "com.cydeer.boot.starter.aop"})
public class ContainerAutoConfig {


}
