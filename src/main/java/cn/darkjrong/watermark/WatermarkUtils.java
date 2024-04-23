package cn.darkjrong.watermark;

import cn.darkjrong.watermark.domain.ImageFile;
import cn.darkjrong.watermark.domain.SrcFile;
import cn.darkjrong.watermark.domain.WatermarkParam;
import cn.darkjrong.watermark.exceptions.WatermarkException;
import cn.darkjrong.watermark.factory.*;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 水印工具类
 *
 * @author Rong.Jia
 * @date 2021/08/13 13:50:07
 */
@Slf4j
public class WatermarkUtils {

    private static final List<WatermarkProcessor> processors = new ArrayList<>();

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
        SrcFile file = getFile(watermarkParam.getFile());
        WatermarkProcessor processor = processors.stream()
                .filter(a -> a.supportType(file.getBytes()))
                .findAny()
                .orElse(null);
        if (ObjectUtil.isNull(processor)) {
            String fileType = FileTypeUtils.getFileType(file.getBytes());
            log.error("*************,The watermark does not support the file format is: {}", fileType);
            throw new WatermarkException("不支持文件格式为 " + fileType + " 的水印处理");
        }
        ImageFile imageFile = handlerWatermarkFile(watermarkParam);
        WatermarkParam param = WatermarkParam.builder()
                .alpha(watermarkParam.getAlpha())
                .fontSize(watermarkParam.getFontSize())
                .bespread(watermarkParam.getBespread())
                .color(watermarkParam.getColor())
                .xMove(watermarkParam.getXMove())
                .yMove(watermarkParam.getYMove())
                .degree(watermarkParam.getDegree())
                .imageFile(imageFile)
                .file(file)
                .build();
        return processor.addWatermark(param);
    }

    /**
     * 处理程序水印文件
     *
     * @param watermarkParam 水印参数
     * @return {@link ImageFile}
     * @throws WatermarkException 水印异常
     */
    private static ImageFile handlerWatermarkFile(WatermarkParam watermarkParam) throws WatermarkException {
        byte[] imageFile = getImageFile(watermarkParam.getImageFile(), watermarkParam);
        return ImageFile.builder().bytes(imageFile).build();
    }

    private static SrcFile getFile(SrcFile srcFile) {
        byte[] byteFile = null;
        File file = srcFile.getFile();
        byte[] bytes = srcFile.getBytes();
        if (FileUtil.exist(file)) {
            try {
                byteFile = FileUtil.readBytes(file);
            } catch (Exception e) {
                log.error("****************,getFile(),文件【{}】读取异常 {}", file.getName(), e.getMessage());
            }
        }
        if (ArrayUtil.isNotEmpty(bytes)) {
            byteFile = bytes;
        }

        if (ArrayUtil.isEmpty(byteFile)) {
            log.error("************,getFile(),待加水印的文件不存在或者文件内容为空,请检查");
            throw new WatermarkException("待加水印的文件不存在或者文件内容为空,请检查");
        }
        return SrcFile.builder().bytes(byteFile).build();
    }

    private static byte[] getImageFile(ImageFile imageFile, WatermarkParam watermarkParam) {
        byte[] byteFile = null;
        File file = imageFile.getFile();
        byte[] bytes = imageFile.getBytes();
        String text = imageFile.getText();

        if (FileUtil.exist(file)) {
            try {
                byteFile = ImageUtils.createImage(FileUtil.readBytes(file), watermarkParam.getDegree(), watermarkParam.getAlpha());
            } catch (Exception e) {
                log.error("****************,getImageFile(),文件【{}】读取异常 {}", file.getName(), e.getMessage());
            }
        }
        if (ArrayUtil.isNotEmpty(bytes)) {
            byteFile = bytes;
        }
        if (StrUtil.isNotBlank(text)) {
            byteFile = ImageUtils.createImageByte(text, watermarkParam.getColor(),
                    watermarkParam.getFontSize(), watermarkParam.getDegree(), watermarkParam.getAlpha());
        }

        if (ArrayUtil.isEmpty(byteFile)) {
            log.error("************,getImageFile(),水印文件的文件为空,请检查");
            throw new WatermarkException("水印文件的文件为空,请检查");
        }
        return byteFile;
    }


}
