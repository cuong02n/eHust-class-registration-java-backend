package vn.edu.hust.ehustclassregistrationjavabackend.config;

import com.google.gson.*;
import io.swagger.v3.core.util.Json;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.GsonUtil;

import java.lang.reflect.Type;
import java.util.List;

@Configuration
@Component
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    final RateLimitInterceptor rateLimitInterceptor;

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        converters.clear();
        GsonHttpMessageConverter gsonConverter = new GsonHttpMessageConverter();
        gsonConverter.setGson(GsonUtil.gsonExpose);

//        GsonHttpMessageConverter gsonSwaggerConverter = new GsonHttpMessageConverter();
//        gsonConverter.setGson(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().registerTypeAdapter(Json.class,new SpringfoxJsonToGsonAdapter()).create());
//        converters.add(gsonSwaggerConverter);


        converters.add(gsonConverter);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitInterceptor);
    }

//    static class SpringfoxJsonToGsonAdapter implements JsonSerializer<Json> {
//        @Override
//        public JsonElement serialize(Json json, Type type, JsonSerializationContext jsonSerializationContext) {
//           return GsonUtil.gsonExpose.toJsonTree(json);
//        }
//    }
}
