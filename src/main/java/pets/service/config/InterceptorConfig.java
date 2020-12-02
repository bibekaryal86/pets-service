package pets.service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pets.service.utils.ControllerLoggingInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    private final ControllerLoggingInterceptor controllerLoggingInterceptor = new ControllerLoggingInterceptor();

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.controllerLoggingInterceptor);
    }
}
