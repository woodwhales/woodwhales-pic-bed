package cn.woodwhales.pic.controller;

import cn.woodwhales.common.model.vo.RespVO;
import cn.woodwhales.pic.aop.AppConfig;
import cn.woodwhales.pic.model.param.UploadFileParam;
import cn.woodwhales.pic.model.resp.UploadFileVo;
import cn.woodwhales.pic.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public RespVO<UploadFileVo> upload(@RequestPart(value = "param") UploadFileParam param, @RequestPart("files") MultipartFile[] files) throws IOException {
        return RespVO.resp(fileService.upload(param, files));
    }

}
