package cn.woodwhales.pic.util;

import cn.woodwhales.common.model.result.OpResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.tika.Tika;

import java.io.File;
import java.io.InputStream;
import java.util.Date;

/**
 * @author woodwhales on 2022-09-18 0:11
 */
@Slf4j
public class FileTool {

    /**
     * 获取年月日路径
     */
    public static String getBaseFilePath() {
        return "/" + DateFormatUtils.format(new Date(), "yyyy/MM/dd") + "/";
    }

    /**
     * 是否为图片文件
     *
     * @param file
     * @return
     */
    public static OpResult<String> belongImageMime(File file) {
        OpResult<String> opResult = fileMime(file);
        if (opResult.isFailure()) {
            return opResult;
        }
        if (StringUtils.contains(opResult.getData(), "image/")) {
            return opResult;
        } else {
            return OpResult.failure();
        }
    }

    /**
     * 获取文件的MIME
     *
     * @param file
     * @return
     */
    public static OpResult<String> fileMime(File file) {
        Tika tika = new Tika();
        try {
            String fileType = tika.detect(file);
            return OpResult.success(fileType);
        } catch (Exception e) {
            log.warn("文件失败MIME失败");
            return OpResult.failure();
        }
    }

    /**
     * 获取文件的MIME
     *
     * @param inputStream
     * @return
     */
    public static OpResult<String> fileMime(InputStream inputStream) {
        Tika tika = new Tika();
        try {
            String fileType = tika.detect(inputStream);
            return OpResult.success(fileType);
        } catch (Exception e) {
            log.warn("文件失败MIME失败");
            return OpResult.failure();
        }

    }

}
