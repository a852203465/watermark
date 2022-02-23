package cn.darkjrong.watermark;

import org.junit.jupiter.api.Test;

import java.io.File;

public class FileTypeUtilsTest {

    @Test
    public void isXlsx() {

        System.out.println(FileTypeUtils.isXlsx(new File("F:\\xls.xlsx")));
        System.out.println(FileTypeUtils.isXls(new File("F:\\xls.xlsx")));

    }

}
