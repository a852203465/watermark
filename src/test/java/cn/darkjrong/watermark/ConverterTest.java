package cn.darkjrong.watermark;

import cn.darkjrong.watermark.exceptions.WatermarkException;
import cn.hutool.core.io.FileUtil;
import org.junit.Test;

import java.io.File;

public class ConverterTest {

    @Test
    public void xls2Xlsx() throws WatermarkException {

        byte[] xls2Xlsx = Converter.xls2Xlsx(new File("F:\\xls.xls"));
        FileUtil.writeBytes(xls2Xlsx, new File("F:\\xls2xlsx.xlsx"));


    }

}
