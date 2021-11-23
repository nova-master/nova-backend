package net.getnova.framework.core;

import java.util.stream.Collectors;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = Type.REACTIVE)
@ConditionalOnClass(WebFluxConfigurer.class)
@AutoConfigureBefore(WebFluxAutoConfiguration.class)
public class NovaWebExceptionHandlerConfiguration {

  @Bean
  @Order(-2)
  public ErrorWebExceptionHandler errorWebExceptionHandler(
    final ErrorAttributes errorAttributes,
    final WebProperties webProperties,
    final ApplicationContext applicationContext,
    final ServerProperties serverProperties,
    final ObjectProvider<ViewResolver> viewResolvers,
    final ServerCodecConfigurer serverCodecConfigurer
  ) {
    final AbstractErrorWebExceptionHandler exceptionHandler = new GlobalErrorWebExceptionHandler(
      errorAttributes,
      webProperties.getResources(),
      applicationContext,
      serverProperties.getError()
    );
    exceptionHandler.setViewResolvers(viewResolvers.orderedStream().collect(Collectors.toList()));
    exceptionHandler.setMessageWriters(serverCodecConfigurer.getWriters());
    exceptionHandler.setMessageReaders(serverCodecConfigurer.getReaders());
    return exceptionHandler;
  }
}
