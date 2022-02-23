package cn.darkjrong.watermark;

import org.junit.jupiter.api.Test;

import java.io.File;

public class FileTypeUtilsTest {

    @Test
    public void isXlsx() {

        System.out.println(FileTypeUtils.isXlsx(new File("F:\\xls.xlsx")));
        System.out.println(FileTypeUtils.isXls(new File("F:\\xls.xlsx")));

    }

    @Test
    public void getMimeType() {
        String fileType = FileTypeUtils.getFileType(new File("F:\\test\\水印.jpeg"));
        System.out.println(fileType);






    }




}
