package cn.darkjrong.watermark.factory;

import cn.darkjrong.watermark.Converter;
import cn.darkjrong.watermark.FileTypeUtils;
import cn.darkjrong.watermark.LicenseUtils;
import cn.darkjrong.watermark.domain.WatermarkParam;
import cn.darkjrong.watermark.exceptions.WatermarkException;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;

import java.io.File;
import java.io.InputStream;

/**
 * 水印抽象处理器
 *
 * @author Rong.Jia
 * @date 2021/08/12 14:31:18
 */
public abstract class AbstractWatermarkProcessor implements WatermarkProcessor {

    @Override
    public void addWatermark(WatermarkParam watermarkParam, File target) throws WatermarkException {
        FileUtil.writeBytes(this.addWatermark(watermarkParam), target);
    }

    @Override
    public byte[] addWatermark(WatermarkParam watermarkParam) throws WatermarkException {
        try {
            LicenseUtils.verificationLicense();
            return watermark(watermarkParam);
        }finally {
            delete(watermarkParam.getImageFile());
        }
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
    InputStream getInputStream(File file) throws WatermarkException {
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
     * 删除文件
     *
     * @param file 文件
     */
    void delete(File file) {
        try {
            FileUtil.del(file);
        }catch (Exception ignored) {}
    }





}
