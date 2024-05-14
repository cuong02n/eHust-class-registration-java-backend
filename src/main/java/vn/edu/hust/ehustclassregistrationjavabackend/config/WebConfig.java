package vn.edu.hust.ehustclassregistrationjavabackend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.GsonUtil;

import java.util.List;

@Configuration
@Component
@RequiredArgsConstructor
@EnableMethodSecurity
public class WebConfig implements WebMvcConfigurer {
    final RateLimitInterceptor rateLimitInterceptor;
    final CustomExceptionResolver customExceptionResolver;
    final RestApiInterceptor restApiInterceptor;
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        converters.clear();
        GsonHttpMessageConverter gsonConverter = new GsonHttpMessageConverter();
        gsonConverter.setGson(GsonUtil.gsonExpose);

        converters.add(gsonConverter);
    }

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
//        resolvers.clear();
        resolvers.add(customExceptionResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitInterceptor);
        registry.addInterceptor(restApiInterceptor);
    }

}
