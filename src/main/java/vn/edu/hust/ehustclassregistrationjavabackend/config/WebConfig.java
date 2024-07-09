package vn.edu.hust.ehustclassregistrationjavabackend.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariConfigMXBean;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import com.zaxxer.hikari.pool.HikariPool;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.GsonUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Configuration
@Component
@RequiredArgsConstructor
@EnableMethodSecurity

public class WebConfig implements WebMvcConfigurer {
    private static final Logger log = LoggerFactory.getLogger(WebConfig.class);
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

//    @Override
//    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
////        System.out.println("ex handler: "+resolvers.size());
//        resolvers.clear();
//        resolvers.add(customExceptionResolver);
//    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add(0, customExceptionResolver);
        for (var x : resolvers) {
            log.info("Exception handler found: {}", x.getClass());
        }
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitInterceptor);
        registry.addInterceptor(restApiInterceptor);
    }

//    @Bean
//    public AuditorAware<String> auditorProvider() {
//    }


//    @Bean
//    HikariDataSource getDataSource(HikariConfig config){
//        return new MyHikariDataSource(config);
//    }
//    static class MyHikariDataSource extends HikariDataSource {
//        @Override
//        public Connection getConnection() throws SQLException {
//            System.out.println("get connection");
//            return super.getConnection();
//        }
//        MyHikariDataSource(HikariConfig config) {
//            super(config);
//        }
//    }
}
