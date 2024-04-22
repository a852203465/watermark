package cn.darkjrong.watermark;

import cn.darkjrong.watermark.enums.ExceptionEnum;
import cn.darkjrong.watermark.enums.FileType;
import cn.darkjrong.watermark.exceptions.WatermarkException;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import com.aspose.cells.Workbook;
import com.aspose.pdf.HtmlLoadOptions;
import com.aspose.slides.Presentation;
import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * 转换器
 *
 * @author Rong.Jia
 * @date 2021/08/29
 */
@Slf4j
public class Converter {

    /**
     * 文件类型转换
     *
     * @param srcFile    src文件
     * @param saveFormat {@link SaveFormat} 保存格式
     * @return {@link byte[]} 字节数组
     * @throws WatermarkException 水印异常
     */
    private static byte[] convertWord(File srcFile, int saveFormat) throws WatermarkException {
        try {
            return convertWord(FileUtil.readBytes(srcFile), saveFormat);
        } catch (IORuntimeException e) {
            log.error(String.format("****************,convertWord(),文件【%s】转换WORD异常,Exception【%s】", srcFile.getName(), e.getMessage()), e);
            throw new WatermarkException(e.getMessage());
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
    private static byte[] convertWord(byte[] srcFile, int saveFormat) throws WatermarkException {
        LicenseUtils.verificationLicense();
        ByteArrayOutputStream os = null;
        ByteArrayInputStream in = null;
        try {
            os = new ByteArrayOutputStream(1024);
            in = new ByteArrayInputStream(srcFile);
            Document doc = null;
            if (FileTypeUtils.isHtml(srcFile)) {
                com.aspose.words.HtmlLoadOptions htmlOptions = new com.aspose.words.HtmlLoadOptions();
                doc = new Document(in, htmlOptions);
            } else {
                doc = new Document(in);
            }
            doc.save(os, saveFormat);
            return os.toByteArray();
        } catch (Exception e) {
            log.error(String.format("****************,convertWord(),文件转换WORD异常,Exception【%s】", e.getMessage()), e);
            throw new WatermarkException(e.getMessage());
        } finally {
            IoUtil.close(os);
            IoUtil.close(in);
        }
    }

    /**
     * 文件类型转换
     *
     * @param srcFile    src文件
     * @param saveFormat {@link com.aspose.pdf.SaveFormat} 保存格式
     * @return {@link byte[]} 字节数组
     * @throws WatermarkException 水印异常
     */
    private static byte[] convertPdf(File srcFile, com.aspose.pdf.SaveFormat saveFormat) throws WatermarkException {
        try {
            return convertPdf(FileUtil.readBytes(srcFile), saveFormat);
        } catch (IORuntimeException e) {
            log.error(String.format("****************,convertPdf(),文件【%s】转换PDF异常,Exception【%s】", srcFile.getName(), e.getMessage()), e);
            throw new WatermarkException(e.getMessage());
        }
    }

    /**
     * 文件类型转换
     *
     * @param srcFile    src文件
     * @param saveFormat {@link com.aspose.pdf.SaveFormat} 保存格式
     * @return {@link byte[]} 字节数组
     * @throws WatermarkException 水印异常
     */
    private static byte[] convertPdf(byte[] srcFile, com.aspose.pdf.SaveFormat saveFormat) throws WatermarkException {
        LicenseUtils.verificationLicense();
        ByteArrayOutputStream os = null;
        ByteArrayInputStream in = null;
        try {
            os = new ByteArrayOutputStream(1024);
            in = new ByteArrayInputStream(srcFile);
            com.aspose.pdf.Document doc = null;
            if (FileTypeUtils.isHtml(srcFile)) {
                HtmlLoadOptions htmlOptions = new HtmlLoadOptions();
                doc = new com.aspose.pdf.Document(in, htmlOptions);
            } else {
                doc = new com.aspose.pdf.Document(in);
            }
            doc.save(os, saveFormat);
            return os.toByteArray();
        } catch (Exception e) {
            log.error(String.format("****************,convertWord(),文件转换PDF异常,Exception【%s】", e.getMessage()), e);
            throw new WatermarkException(e.getMessage());
        } finally {
            IoUtil.close(os);
            IoUtil.close(in);
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
        try {
            return convertExcel(FileUtil.readBytes(srcFile), saveFormat);
        } catch (IORuntimeException e) {
            log.error(String.format("****************,convertExcel(),文件【%s】转换Excel异常,Exception【%s】", srcFile.getName(), e.getMessage()), e);
            throw new WatermarkException(e.getMessage());
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
    private static byte[] convertExcel(byte[] srcFile, int saveFormat) throws WatermarkException {
        LicenseUtils.verificationLicense();
        ByteArrayOutputStream os = null;
        ByteArrayInputStream in = null;
        try {
            os = new ByteArrayOutputStream(1024);
            in = new ByteArrayInputStream(srcFile);
            Workbook doc = null;
            if (FileTypeUtils.isHtml(srcFile)) {
                com.aspose.cells.HtmlLoadOptions htmlOptions = new com.aspose.cells.HtmlLoadOptions();
                doc = new Workbook(in, htmlOptions);
            } else {
                doc = new Workbook(in);
            }
            doc.save(os, saveFormat);
            return os.toByteArray();
        } catch (Exception e) {
            log.error(String.format("****************,convertExcel(),文件转换Excel异常,Exception【%s】", e.getMessage()), e);
            throw new WatermarkException(e.getMessage());
        } finally {
            IoUtil.close(in);
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
        try {
            return convertPpt(FileUtil.readBytes(srcFile), saveFormat);
        } catch (IORuntimeException e) {
            log.error(String.format("****************,convertPpt(),文件【%s】转换PPT异常,Exception【%s】", srcFile.getName(), e.getMessage()), e);
            throw new WatermarkException(e.getMessage());
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
    private static byte[] convertPpt(byte[] srcFile, int saveFormat) throws WatermarkException {
        LicenseUtils.verificationLicense();
        ByteArrayOutputStream os = null;
        ByteArrayInputStream in = null;
        try {
            in = new ByteArrayInputStream(srcFile);
            os = new ByteArrayOutputStream(1024);
            Presentation pres = new Presentation(in);
            pres.save(os, saveFormat);
            return os.toByteArray();
        } catch (Exception e) {
            log.error(String.format("****************,convertPpt(),文件转换PPT异常,Exception【%s】", e.getMessage()), e);
            throw new WatermarkException(e.getMessage());
        } finally {
            IoUtil.close(in);
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
     * word 转 pdf
     *
     * @param wordFile word文件
     * @return {@link byte[]}
     * @throws WatermarkException 水印异常
     */
    public static byte[] word2Pdf(byte[] wordFile) throws WatermarkException {
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
     * html转pdf
     *
     * @param htmlFile html文件
     * @return {@link byte[]}
     * @throws WatermarkException 水印异常
     */
    public static byte[] html2Pdf(byte[] htmlFile) throws WatermarkException {
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
     * pdf转Word
     *
     * @param pdfFile pdf文件
     * @return {@link byte[]}
     * @throws WatermarkException 水印异常
     */
    public static byte[] pdf2Word(byte[] pdfFile) throws WatermarkException {
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
    public static byte[] doc2Docx(File docFile) throws WatermarkException {
        Assert.isTrue(FileTypeUtils.isDoc(docFile), getErrorMsg(FileType.PDF));
        return convertWord(docFile, SaveFormat.DOCX);
    }

    /**
     * doc 转docx
     *
     * @param docFile 字文件
     * @return {@link byte[]} 字节数组
     * @throws WatermarkException 水印异常
     */
    public static byte[] doc2Docx(byte[] docFile) throws WatermarkException {
        Assert.isTrue(FileTypeUtils.isDoc(docFile), getErrorMsg(FileType.PDF));
        return convertWord(docFile, SaveFormat.DOCX);
    }

    /**
     * docx转doc
     *
     * @param docFile 字文件
     * @return {@link byte[]} 字节数组
     * @throws WatermarkException 水印异常
     */
    public static byte[] docx2Doc(File docFile) throws WatermarkException {
        Assert.isTrue(FileTypeUtils.isDocx(docFile), getErrorMsg(FileType.DOCX));
        return convertWord(docFile, SaveFormat.DOC);
    }

    /**
     * docx转doc
     *
     * @param docFile 字文件
     * @return {@link byte[]} 字节数组
     * @throws WatermarkException 水印异常
     */
    public static byte[] docx2Doc(byte[] docFile) throws WatermarkException {
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
    public static byte[] xls2Xlsx(File docFile) throws WatermarkException {
        Assert.isTrue(FileTypeUtils.isXls(docFile), getErrorMsg(FileType.XLS));
        return convertExcel(docFile, com.aspose.cells.SaveFormat.XLSX);
    }

    /**
     * xls 转 xlsx
     *
     * @param docFile 字文件
     * @return {@link byte[]} 字节数组
     * @throws WatermarkException 水印异常
     */
    public static byte[] xls2Xlsx(byte[] docFile) throws WatermarkException {
        Assert.isTrue(FileTypeUtils.isXls(docFile), getErrorMsg(FileType.XLS));
        return convertExcel(docFile, com.aspose.cells.SaveFormat.XLSX);
    }

    /**
     * xlsx 转 xls
     *
     * @param docFile 字文件
     * @return {@link byte[]} 字节数组
     * @throws WatermarkException 水印异常
     */
    public static byte[] xlsx2Xls(File docFile) throws WatermarkException {
        Assert.isTrue(FileTypeUtils.isXlsx(docFile), getErrorMsg(FileType.XLSX));
        return convertExcel(docFile, com.aspose.cells.SaveFormat.AUTO);
    }

    /**
     * xlsx 转 xls
     *
     * @param docFile 字文件
     * @return {@link byte[]} 字节数组
     * @throws WatermarkException 水印异常
     */
    public static byte[] xlsx2Xls(byte[] docFile) throws WatermarkException {
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
    public static byte[] ppt2Pptx(File docFile) throws WatermarkException {
        Assert.isTrue(FileTypeUtils.isPpt(docFile), getErrorMsg(FileType.PPT));
        return convertPpt(docFile, com.aspose.slides.SaveFormat.Pptx);
    }

    /**
     * ppt 转 pptx
     *
     * @param docFile 字文件
     * @return {@link byte[]} 字节数组
     * @throws WatermarkException 水印异常
     */
    public static byte[] ppt2Pptx(byte[] docFile) throws WatermarkException {
        Assert.isTrue(FileTypeUtils.isPpt(docFile), getErrorMsg(FileType.PPT));
        return convertPpt(docFile, com.aspose.slides.SaveFormat.Pptx);
    }

    /**
     * pptx 转 ppt
     *
     * @param docFile 字文件
     * @return {@link byte[]} 字节数组
     * @throws WatermarkException 水印异常
     */
    public static byte[] pptx2Ppt(File docFile) throws WatermarkException {
        Assert.isTrue(FileTypeUtils.isPptx(docFile), getErrorMsg(FileType.PPTX));
        return convertExcel(docFile, com.aspose.slides.SaveFormat.Ppt);
    }

    /**
     * pptx 转 ppt
     *
     * @param docFile 字文件
     * @return {@link byte[]} 字节数组
     * @throws WatermarkException 水印异常
     */
    public static byte[] pptx2Ppt(byte[] docFile) throws WatermarkException {
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

    /**
     * rtf2pdf
     *
     * @param file 文件
     * @return {@link byte[]}
     * @throws WatermarkException 水印异常
     */
    public static byte[] rtf2Pdf(byte[] file) throws WatermarkException {
        Assert.isTrue(FileTypeUtils.isRtf(file), getErrorMsg(FileType.RTF));
        return convertWord(file, SaveFormat.PDF);
    }


}
