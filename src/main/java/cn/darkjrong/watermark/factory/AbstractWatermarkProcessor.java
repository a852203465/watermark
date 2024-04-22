package cn.darkjrong.watermark.factory;

import cn.darkjrong.watermark.Converter;
import cn.darkjrong.watermark.FileTypeUtils;
import cn.darkjrong.watermark.LicenseUtils;
import cn.darkjrong.watermark.domain.WatermarkParam;
import cn.darkjrong.watermark.exceptions.WatermarkException;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

/**
 * 水印抽象处理器
 *
 * @author Rong.Jia
 * @date 2024/04/22
 */
@Slf4j
public abstract class AbstractWatermarkProcessor implements WatermarkProcessor {

    @Override
    public void addWatermark(WatermarkParam watermarkParam, File target) throws WatermarkException {
        FileUtil.writeBytes(this.addWatermark(watermarkParam), target);
    }

    @Override
    public byte[] addWatermark(WatermarkParam watermarkParam) throws WatermarkException {
        LicenseUtils.verificationLicense();
        return watermark(watermarkParam);
    }

    /**
     * 水印
     *
     * @param watermarkParam 水印参数
     * @return {@link byte[]}
     * @throws WatermarkException 水印异常
     */
    protected abstract byte[] watermark(WatermarkParam watermarkParam) throws WatermarkException;

    /**
     * 获取输入流
     *
     * @param file 文件
     * @return {@link InputStream}
     * @throws WatermarkException 水印异常
     */
    protected InputStream getInputStream(File file) throws WatermarkException {
        if (FileTypeUtils.isXls(file)) {
            return IoUtil.toStream(Converter.xls2Xlsx(file));
        }else if (FileTypeUtils.isDoc(file)) {
            return IoUtil.toStream(Converter.doc2Docx(file));
        }else if (FileTypeUtils.isPpt(file)) {
            return IoUtil.toStream(Converter.ppt2Pptx(file));
        }else if (FileTypeUtils.isHtml(file)) {
            return IoUtil.toStream(Converter.html2Pdf(file));
        }else {
            return FileUtil.getInputStream(file);
        }
    }

    /**
     * 获取输入流
     *s
     * @param file 文件
     * @return {@link InputStream}
     * @throws WatermarkException 水印异常
     */
    protected InputStream getInputStream(byte[] file) throws WatermarkException {
        if (FileTypeUtils.isXls(file)) {
            return IoUtil.toStream(Converter.xls2Xlsx(file));
        }else if (FileTypeUtils.isDoc(file)) {
            return IoUtil.toStream(Converter.doc2Docx(file));
        }else if (FileTypeUtils.isPpt(file)) {
            return IoUtil.toStream(Converter.ppt2Pptx(file));
        }else if (FileTypeUtils.isHtml(file)) {
            return IoUtil.toStream(Converter.html2Pdf(file));
        }else {
            return new ByteArrayInputStream(file);
        }
    }




}
