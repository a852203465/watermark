package cn.darkjrong.watermark;

import cn.darkjrong.watermark.domain.WatermarkParam;
import cn.darkjrong.watermark.exceptions.WatermarkException;
import cn.darkjrong.watermark.factory.*;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 水印工具类
 *
 * @author Rong.Jia
 * @date 2021/08/13 13:50:07
 */
public class WatermarkUtils {

    private static final List<WatermarkProcessor> processors = new ArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(WatermarkUtils.class);

    static {
        processors.add(new ExcelWatermarkProcessor());
        processors.add(new ImageWatermarkProcessor());
        processors.add(new PdfWatermarkProcessor());
        processors.add(new PowerPointWatermarkProcessor());
        processors.add(new WordWatermarkProcessor());
    }

    /**
     * 添加处理器
     *
     * @param processor 处理器
     */
    public static void addProcessor(WatermarkProcessor processor) {
            if (ObjectUtil.isNotNull(processor)) {
                processors.add(processor);
            }
    }

    /**
     * 添加水印
     *
     * @param watermarkParam 水印参数
     * @param outputFile     输出文件
     * @throws WatermarkException 水印异常
     */
    public static void addWatermark(WatermarkParam watermarkParam, File outputFile) throws WatermarkException {
        LicenseUtils.verificationLicense();
        FileUtil.writeBytes(addWatermark(watermarkParam), outputFile);
    }

    /**
     * 添加水印
     *
     * @param watermarkParam 水印参数
     * @throws WatermarkException 水印异常
     */
    public static byte[] addWatermark(WatermarkParam watermarkParam) throws WatermarkException {
        LicenseUtils.verificationLicense();
        File file = watermarkParam.getFile();
        WatermarkProcessor processor = processors.stream().filter(a -> a.supportType(file)).findAny().orElse(null);
        if (ObjectUtil.isNull(processor)) {
            logger.error("The watermark does not support the file format is: {}", FileTypeUtils.getFileType(file));
            throw new WatermarkException("不支持文件格式为 " + FileTypeUtils.getFileType(file) + " 的水印处理");
        }
        handlerWatermarkFile(watermarkParam);
        return processor.addWatermark(watermarkParam);
    }

    /**
     * 处理程序水印文件
     *
     * @param watermarkParam 水印参数
     * @throws WatermarkException 水印的例外
     */
    private static void handlerWatermarkFile(WatermarkParam watermarkParam) throws WatermarkException {

        File imageFile = watermarkParam.getImageFile();
        if (FileUtil.exist(imageFile)) {
            ImageUtils.createImage(imageFile, watermarkParam.getDegree(), watermarkParam.getAlpha());
        }else if (StrUtil.isNotBlank(watermarkParam.getText())) {
            watermarkParam.imageFile(ImageUtils.createImage(watermarkParam.getText(),
                    watermarkParam.getColor(), watermarkParam.getFontSize(),
                    watermarkParam.getDegree(), watermarkParam.getAlpha()));
        }
    }




































}
