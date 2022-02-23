package cn.darkjrong.watermark.factory;

import cn.darkjrong.watermark.domain.WatermarkParam;
import cn.darkjrong.watermark.exceptions.WatermarkException;

import java.io.File;

/**
 * 水印处理器
 *
 * @author Rong.Jia
 * @date 2021/08/12 14:59:25
 */
public interface WatermarkProcessor {

    /**
     * 添加水印
     *
     * @param watermarkParam 水印参数
     * @param target         目标文件
     * @throws WatermarkException 水印异常
     */
    void addWatermark(WatermarkParam watermarkParam, File target) throws WatermarkException;

    /**
     * 添加水印
     *
     * @param watermarkParam 水印参数
     * @return {@link byte[]}
     * @throws WatermarkException 水印异常
     */
    byte[] addWatermark(WatermarkParam watermarkParam) throws WatermarkException;

    /**
     * 支持类型
     *
     * @param file 文件
     * @return {@link Boolean}
     */
    default Boolean supportType(File file) {
        return Boolean.FALSE;
    }









}
