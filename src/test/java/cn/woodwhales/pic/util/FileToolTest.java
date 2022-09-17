package cn.woodwhales.pic.util;

import cn.woodwhales.common.model.result.OpResult;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileToolTest {

    @Test
    public void test1() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:hello.exe");
        String fileMime = FileTool.fileMime(file).getData();
        System.out.println(fileMime);
        assertEquals("application/x-msdownload; format=pe64", fileMime);

        OpResult<String> opResult = FileTool.belongImageMime(file);
        assertEquals(false, opResult.isSuccessful());

    }

    @Test
    public void test2() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:img.jpg");
        String fileMime = FileTool.fileMime(file).getData();
        System.out.println(fileMime);
        assertEquals("image/jpeg", fileMime);

        OpResult<String> opResult = FileTool.belongImageMime(file);
        assertEquals(true, opResult.isSuccessful());
    }

    @Test
    public void test3() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:Hello.java");
        String fileMime = FileTool.fileMime(file).getData();
        System.out.println(fileMime);
        assertEquals("text/x-java-source", fileMime);
    }

    @Test
    public void test4() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:Hello.class");
        String fileMime = FileTool.fileMime(file).getData();
        System.out.println(fileMime);
        assertEquals("application/java-vm", fileMime);
    }

}