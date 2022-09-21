package cn.woodwhales.pic.config;

import cn.woodwhales.pic.aop.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

/**
 * @author woodwhales on 2022-09-21 18:02
 */
@Slf4j
@Component
public class InConfig {

    @Autowired
    private AppConfig appConfig;

    @PostConstruct
    public void init() throws IOException {
        String filePath = appConfig.getFilePath();
        File file = new File(filePath);
        if(!file.exists()) {
            FileUtils.forceMkdir(file);
            log.info("生成文件目录：{}", filePath);
        }
    }

}
