package cn.woodwhales.pic.aop;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author woodwhales on 2022-09-17 22:48
 */
@Getter
@Component
@Configuration
public class AppConfig {

    @Value("${system.filePath:/data/woodwhales-pic-bed}")
    private String filePath;

    public String getFilePath() {
        return this.filePath + File.separator;
    }

}
