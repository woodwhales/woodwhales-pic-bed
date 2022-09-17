package cn.woodwhales.pic.config;

import cn.woodwhales.pic.aop.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author woodwhales on 2022-09-17 22:49
 */
@Configuration
public class WebFileConfigurer implements WebMvcConfigurer {

    @Autowired
    private AppConfig appConfig;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/ota/**")
                .addResourceLocations("file:" + appConfig.getFilePath());
    }
}
