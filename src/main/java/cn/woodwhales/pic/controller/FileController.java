package cn.woodwhales.pic.controller;

import cn.hutool.core.lang.UUID;
import cn.woodwhales.common.business.DataTool;
import cn.woodwhales.common.model.vo.RespVO;
import cn.woodwhales.pic.aop.AppConfig;
import cn.woodwhales.pic.model.UploadParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * @author woodwhales on 2022-09-17 23:21
 */
@Slf4j
@Validated
@RequestMapping("/file")
@RestController
public class FileController {

    @Autowired
    private AppConfig appConfig;

    @PostMapping("/upload")
    public RespVO<List<String>> upload(@RequestPart(value = "param") UploadParam param, @RequestPart("files") MultipartFile[] files) throws IOException {
        List<String> newFileNameList = new ArrayList<>();
        for (MultipartFile file : files) {
            String originalFilename = file.getOriginalFilename();
            log.info("originalFilename={}", originalFilename);
            String extension = FilenameUtils.getExtension(originalFilename);
            String newFileName = UUID.randomUUID().toString(true) + FilenameUtils.EXTENSION_SEPARATOR + extension;
            String filePath = appConfig.getFilePath() + newFileName;
            File dest = new File(filePath);
            Files.copy(file.getInputStream(), dest.toPath());
            newFileNameList.add(newFileName);
        }
        return RespVO.success(DataTool.toList(newFileNameList, fileName -> "http://192.168.0.108:10010/ota/" + fileName));
    }

}
