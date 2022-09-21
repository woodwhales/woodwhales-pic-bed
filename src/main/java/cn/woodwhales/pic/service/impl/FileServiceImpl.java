package cn.woodwhales.pic.service.impl;

import cn.hutool.core.lang.UUID;
import cn.woodwhales.common.model.result.OpResult;
import cn.woodwhales.pic.aop.AppConfig;
import cn.woodwhales.pic.model.param.UploadFileParam;
import cn.woodwhales.pic.model.resp.UploadFileVo;
import cn.woodwhales.pic.service.FileService;
import cn.woodwhales.pic.util.FileTool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * @author woodwhales on 2022-09-21 18:09
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private AppConfig appConfig;

    @Override
    public OpResult<UploadFileVo> upload(UploadFileParam param, MultipartFile[] files) throws IOException {
        List<String> fileShareUrlList = new ArrayList<>();
        for (MultipartFile file : files) {
            fileShareUrlList.add(this.copyToDestFile(file));
        }
        UploadFileVo uploadFileVo = new UploadFileVo();
        uploadFileVo.setFileShareUrl(fileShareUrlList);
        return OpResult.success(uploadFileVo);
    }

    /**
     * 文件规则：yyyy/MM/dd/uuid.文件后缀名
     * @param file
     * @return
     */
    private String copyToDestFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        try {
            log.info("originalFilename={}", originalFilename);
            String extension = FilenameUtils.getExtension(originalFilename);
            String baseFilePath = FileTool.getBaseFilePath();
            File parentFile = new File(appConfig.getFilePath() + baseFilePath);
            if(!parentFile.exists()) {
                FileUtils.forceMkdir(parentFile);
            }
            String newFileName = UUID.randomUUID().toString(true) + FilenameUtils.EXTENSION_SEPARATOR + extension;
            File destFile = new File(parentFile.getPath() + "/" + newFileName);
            Files.copy(file.getInputStream(), destFile.toPath());
            return appConfig.getSite() + "/ota" + baseFilePath + newFileName;
        } catch (Exception exception) {
            log.error("保存文件失败, causeBy={}, originalFilename={}", originalFilename, exception.getMessage(), exception);
        }
        return null;
    }

}
