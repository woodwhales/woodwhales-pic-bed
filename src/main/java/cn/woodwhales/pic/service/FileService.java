package cn.woodwhales.pic.service;

import cn.woodwhales.common.model.result.OpResult;
import cn.woodwhales.pic.model.param.UploadFileParam;
import cn.woodwhales.pic.model.resp.UploadFileVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author woodwhales on 2022-09-21 18:08
 */
public interface FileService {
    OpResult<UploadFileVo> upload(UploadFileParam param, MultipartFile[] files) throws IOException;
}
