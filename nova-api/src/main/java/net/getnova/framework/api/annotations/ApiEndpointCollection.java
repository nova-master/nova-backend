package net.getnova.framework.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.getnova.framework.api.ApiAuthenticator;
import net.getnova.framework.api.DefaultApiAuthenticator;
import net.getnova.framework.api.data.ApiType;
import org.springframework.stereotype.Component;

@Component
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiEndpointCollection {

  String id();

  String[] description();

  ApiType type();

  boolean disabled() default false;

  Class<? extends ApiAuthenticator> authenticator() default DefaultApiAuthenticator.class;
}
