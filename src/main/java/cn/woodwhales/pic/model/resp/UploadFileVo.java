package cn.woodwhales.pic.model.resp;

import lombok.Data;

import java.util.List;

/**
 * @author woodwhales on 2022-09-21 18:10
 */
@Data
public class UploadFileVo {

    /**
     * 文件对外链接
     */
    private List<String> fileShareUrl;

}
