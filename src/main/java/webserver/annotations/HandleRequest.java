package webserver.annotations;

import webserver.http.message.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME) //컴파일 시에만 필요한 것 맞는지?
public @interface HandleRequest {
    String path();
    HttpMethod httpMethod();
}
