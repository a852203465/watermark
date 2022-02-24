package cn.darkjrong.watermark;

import cn.darkjrong.watermark.enums.ExceptionEnum;
import cn.darkjrong.watermark.enums.FileType;
import cn.darkjrong.watermark.exceptions.WatermarkException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import com.aspose.cells.Workbook;
import com.aspose.pdf.HtmlLoadOptions;
import com.aspose.slides.Presentation;
import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * 转换器
 *
 * @author Rong.Jia
 * @date 2021/08/29
 */
public class Converter {

    private static final Logger logger = LoggerFactory.getLogger(Converter.class);

    /**
     * 文件类型转换
     *
     * @param srcFile    src文件
     * @param saveFormat {@link SaveFormat} 保存格式
     * @return {@link byte[]} 字节数组
     * @throws WatermarkException 水印异常
     */
    private static byte[] convertWord(File srcFile, int saveFormat) throws WatermarkException {
        LicenseUtils.verificationLicense();
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream(1024);
            Document doc = new Document(srcFile.getPath());
            doc.save(os, saveFormat);
            return os.toByteArray();
        } catch (Exception e) {
            logger.error("convertWord {}", e.getMessage());
            throw new WatermarkException(e.getMessage());
        }finally {
            IoUtil.close(os);
        }
    }

    /**
     * 文件类型转换
     *
     * @param srcFile    src文件
     * @param saveFormat {@link SaveFormat} 保存格式
     * @return {@link byte[]} 字节数组
     * @throws WatermarkException 水印异常
     */
    private static byte[] convertPdf(File srcFile, int saveFormat) throws WatermarkException {
        LicenseUtils.verificationLicense();
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream(1024);
            com.aspose.pdf.Document doc = null;
            if (FileTypeUtils.isHtml(srcFile)) {
                HtmlLoadOptions htmlOptions = new HtmlLoadOptions();
                doc = new com.aspose.pdf.Document(srcFile.getPath(), htmlOptions);
            }else {
                doc = new com.aspose.pdf.Document(srcFile.getPath());
            }
             doc.save(os, saveFormat);
            return os.toByteArray();
        } catch (Exception e) {
            logger.error("convertPdf {}", e.getMessage());
            throw new WatermarkException(e.getMessage());
        }finally {
            IoUtil.close(os);
        }
    }

    /**
     * 文件类型转换
     *
     * @param srcFile    src文件
     * @param saveFormat {@link SaveFormat} 保存格式
     * @return {@link byte[]} 字节数组
     * @throws WatermarkException 水印异常
     */
    private static byte[] convertExcel(File srcFile, int saveFormat) throws WatermarkException {
        LicenseUtils.verificationLicense();
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream(1024);
            Workbook doc = new Workbook(srcFile.getPath());
            doc.save(os, saveFormat);
            return os.toByteArray();
        } catch (Exception e) {
            logger.error("convertExcel {}", e.getMessage());
            throw new WatermarkException(e.getMessage());
        }finally {
            IoUtil.close(os);
        }
    }

    /**
     * 文件类型转换
     *
     * @param srcFile    src文件
     * @param saveFormat {@link SaveFormat} 保存格式
     * @return {@link byte[]} 字节数组
     * @throws WatermarkException 水印异常
     */
    private static byte[] convertPpt(File srcFile, int saveFormat) throws WatermarkException {
        LicenseUtils.verificationLicense();
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream(1024);
            Presentation pres = new Presentation(srcFile.getPath());
            pres.save(os, saveFormat);
            return os.toByteArray();
        } catch (Exception e) {
            logger.error("convertPpt {}", e.getMessage());
            throw new WatermarkException(e.getMessage());
        }finally {
            IoUtil.close(os);
        }
    }

    /**
     * 获取错误消息
     *
     * @param fileType 文件类型
     * @return {@link String}
     */
    private static String getErrorMsg(FileType fileType) {
        return String.format(ExceptionEnum.THE_FILE_MUST_BE_OF_TYPE_XXX.getValue(), fileType.name());
    }

    /**
     * word 转 pdf
     *
     * @param wordFile word文件
     * @return {@link byte[]}
     * @throws WatermarkException 水印异常
     */
    public static byte[] word2Pdf(File wordFile) throws WatermarkException {
        Assert.isTrue(FileTypeUtils.isWord(wordFile), getErrorMsg(FileType.DOC));
        return convertWord(wordFile, SaveFormat.PDF);
    }

    /**
     * html转pdf
     *
     * @param htmlFile html文件
     * @return {@link byte[]}
     * @throws WatermarkException 水印异常
     */
    public static byte[] html2Pdf(File htmlFile) throws WatermarkException {
        Assert.isTrue(FileTypeUtils.isHtml(htmlFile), getErrorMsg(FileType.HTML));
        return convertPdf(htmlFile, com.aspose.pdf.SaveFormat.Pdf);
    }

    /**
     * pdf转Word
     *
     * @param pdfFile pdf文件
     * @return {@link byte[]}
     * @throws WatermarkException 水印异常
     */
    public static byte[] pdf2Word(File pdfFile) throws WatermarkException {
        Assert.isTrue(FileTypeUtils.isPdf(pdfFile), getErrorMsg(FileType.PDF));
        return convertPdf(pdfFile, com.aspose.pdf.SaveFormat.DocX);
    }

    /**
     * doc 转docx
     *
     * @param docFile 字文件
     * @return {@link byte[]} 字节数组
     * @throws WatermarkException 水印异常
     */
    public static byte[] doc2Docx(File docFile)  throws WatermarkException {
        Assert.isTrue(FileTypeUtils.isDoc(docFile), getErrorMsg(FileType.PDF));
        return convertWord(docFile, SaveFormat.DOCX);
    }

    /**
     *  docx转doc
     *
     * @param docFile 字文件
     * @return {@link byte[]} 字节数组
     * @throws WatermarkException 水印异常
     */
    public static byte[] docx2Doc(File docFile)  throws WatermarkException {
        Assert.isTrue(FileTypeUtils.isDocx(docFile), getErrorMsg(FileType.DOCX));
        return convertWord(docFile, SaveFormat.DOC);
    }

    /**
     * xls 转 xlsx
     *
     * @param docFile 字文件
     * @return {@link byte[]} 字节数组
     * @throws WatermarkException 水印异常
     */
    public static byte[] xls2Xlsx(File docFile)  throws WatermarkException {
        Assert.isTrue(FileTypeUtils.isXls(docFile), getErrorMsg(FileType.XLS));
        return convertExcel(docFile, com.aspose.cells.SaveFormat.XLSX);
    }

    /**
     *  xlsx 转 xls
     *
     * @param docFile 字文件
     * @return {@link byte[]} 字节数组
     * @throws WatermarkException 水印异常
     */
    public static byte[] xlsx2Xls(File docFile)  throws WatermarkException {
        Assert.isTrue(FileTypeUtils.isXlsx(docFile), getErrorMsg(FileType.XLSX));
        return convertExcel(docFile, com.aspose.cells.SaveFormat.AUTO);
    }

    /**
     * ppt 转 pptx
     *
     * @param docFile 字文件
     * @return {@link byte[]} 字节数组
     * @throws WatermarkException 水印异常
     */
    public static byte[] ppt2Pptx(File docFile)  throws WatermarkException {
        Assert.isTrue(FileTypeUtils.isPpt(docFile), getErrorMsg(FileType.PPT));
        return convertPpt(docFile, com.aspose.slides.SaveFormat.Pptx);
    }

    /**
     *  pptx 转 ppt
     *
     * @param docFile 字文件
     * @return {@link byte[]} 字节数组
     * @throws WatermarkException 水印异常
     */
    public static byte[] pptx2Ppt(File docFile)  throws WatermarkException {
        Assert.isTrue(FileTypeUtils.isPptx(docFile), getErrorMsg(FileType.PPTX));
        return convertExcel(docFile, com.aspose.slides.SaveFormat.Ppt);
    }

    /**
     * rtf2pdf
     *
     * @param file 文件
     * @return {@link byte[]}
     * @throws WatermarkException 水印异常
     */
    public static byte[] rtf2Pdf(File file) throws WatermarkException {
        Assert.isTrue(FileTypeUtils.isRtf(file), getErrorMsg(FileType.RTF));
        return convertWord(file, SaveFormat.PDF);
    }









}
