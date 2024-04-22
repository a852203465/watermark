package cn.darkjrong.watermark.factory;

import cn.darkjrong.watermark.FileTypeUtils;
import cn.darkjrong.watermark.domain.WatermarkParam;
import cn.darkjrong.watermark.exceptions.WatermarkException;
import cn.hutool.core.img.ImgUtil;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * 图片水印处理器
 *
 * @author Rong.Jia
 * @date 2024/04/22
 */
@Slf4j
public class ImageWatermarkProcessor extends AbstractWatermarkProcessor {

    @Override
    public Boolean supportType(File file) {
        return FileTypeUtils.isImage(file);
    }

    @Override
    public Boolean supportType(byte[] file) {
        return FileTypeUtils.isImage(file);
    }

    @Override
    public byte[] watermark(WatermarkParam watermarkParam) throws WatermarkException {
        try {
            Image srcImage = ImgUtil.toImage(watermarkParam.getFile().getBytes());
            BufferedImage bufferImg = new BufferedImage(srcImage.getWidth(null), srcImage.getHeight(null), BufferedImage.TYPE_INT_RGB);

            // 1、得到画笔对象
            Graphics2D g = bufferImg.createGraphics();

            // 2、设置对线段的锯齿状边缘处理
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(srcImage.getScaledInstance(srcImage.getWidth(null),
                    srcImage.getHeight(null),
                    BufferedImage.SCALE_SMOOTH), 0, 0, null);

            // 水印图片的路径 水印图片一般为gif或者png的，这样可设置透明度
            ImageIcon imgIcon = new ImageIcon(watermarkParam.getImageFile().getBytes());
            Image img = imgIcon.getImage();

            // 水印图片的位置
            if (!watermarkParam.getBespread()) {
                g.drawImage(img,
                        srcImage.getWidth(null) / 2 - watermarkParam.getXMove(),
                        srcImage.getHeight(null) / 2 - watermarkParam.getYMove(), null);
            } else {
                for (int height = watermarkParam.getYMove() + imgIcon.getIconHeight();
                     height < bufferImg.getHeight();
                     height = height + watermarkParam.getYMove() + imgIcon.getIconHeight()) {
                    for (int weight = watermarkParam.getXMove() + imgIcon.getIconWidth();
                         weight < bufferImg.getWidth();
                         weight = weight + watermarkParam.getXMove() + imgIcon.getIconWidth()) {
                        g.drawImage(img, weight - imgIcon.getIconWidth(), height
                                - imgIcon.getIconHeight(), null);
                    }
                }
            }
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            g.dispose();
            return ImgUtil.toBytes(bufferImg, ImgUtil.IMAGE_TYPE_PNG);
        } catch (Exception e) {
            log.error(String.format("Failed to add watermark to the image 【%s】", e.getMessage()), e);
            throw new WatermarkException(e.getMessage());
        }
    }
}
