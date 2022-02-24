package cn.darkjrong.watermark;

import cn.darkjrong.watermark.exceptions.WatermarkException;
import cn.hutool.core.io.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.File;

public class ConverterTest {

    @Test
    public void xls2Xlsx() throws WatermarkException {

        byte[] xls2Xlsx = Converter.xls2Xlsx(new File("F:\\xls.xls"));
        FileUtil.writeBytes(xls2Xlsx, new File("F:\\xls2xlsx.xlsx"));

    }

    @Test
    public void doc2Pdf() {
        FileUtil.writeBytes(Converter.word2Pdf(new File("F:\\test\\demo.docx")), new File("F:\\test\\demo.pdf"));
    }


    @Test
    public void html2Pdf() {
        FileUtil.writeBytes(Converter.html2Pdf(new File("F:\\test\\pdfhtml.html")), new File("F:\\test\\pdfhtml.pdf"));

    }

    @Test
    public void pdf2Word() {
        FileUtil.writeBytes(Converter.pdf2Word(new File("F:\\test\\pdfhtml.pdf")), new File("F:\\test\\pdfhtml.docx"));
    }

    @Test
    public void rtf2Pdf() {
        FileUtil.writeBytes(Converter.rtf2Pdf(new File("F:\\test\\1.rtf")), new File("F:\\test\\rtf.pdf"));
    }







}
