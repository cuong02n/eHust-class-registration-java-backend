package vn.edu.hust.ehustclassregistrationjavabackend.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        converters.clear();
//        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
//        GsonHttpMessageConverter gsonConverter = new GsonHttpMessageConverter();
//        gsonConverter.setGson(gson);
//        converters.add(gsonConverter);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            private final Map<String, Long> requestCounts = new ConcurrentHashMap<>();
            @Value("${max_request_per_min}")
            int MAX_REQUEST_PER_MIN = 5;

            private String getClientIpAddress(HttpServletRequest request) {
                String xForwardedForHeader = request.getHeader("X-Forwarded-For");
                if (xForwardedForHeader != null) {
                    return xForwardedForHeader.split(",")[0].trim();
                }
                return request.getRemoteAddr();
            }

            @Override
            public boolean preHandle(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull Object handler) throws Exception {
                System.out.println(MAX_REQUEST_PER_MIN);
                String clientIpAddress = getClientIpAddress(request);
                long currentTimeMillis = System.currentTimeMillis();

                synchronized (requestCounts) {
                    // Remove expired entries
                    requestCounts.entrySet().removeIf(entry -> currentTimeMillis - entry.getValue() > 60000);

                    // Check request count
//                    long requestCount = requestCounts.
                    long requestCount = requestCounts.getOrDefault(clientIpAddress, 0L);
                    if (requestCount >= MAX_REQUEST_PER_MIN) {
                        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                        return false; // Reject the request
                    }

                    // Increment request count
                    requestCounts.put(clientIpAddress, currentTimeMillis);
                }

                return true;
            }

            @Override
            public void postHandle(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull Object handler, ModelAndView modelAndView) throws Exception {
                HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
            }

            @Override
            public void afterCompletion(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull Object handler, Exception ex) throws Exception {
                HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
            }
        });
    }
}
