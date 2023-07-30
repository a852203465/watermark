package cn.darkjrong.watermark.factory;

import cn.darkjrong.watermark.FileTypeUtils;
import cn.darkjrong.watermark.domain.WatermarkParam;
import cn.darkjrong.watermark.exceptions.WatermarkException;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * 图像处理器
 *
 * @author Rong.Jia
 * @date 2018年9月17日 下午5:22:56
 */
public class ImageWatermarkProcessor extends AbstractWatermarkProcessor {

	private static final Logger logger = LoggerFactory.getLogger(ImageWatermarkProcessor.class);

	@Override
	public Boolean supportType(File file) {
		return FileTypeUtils.isImage(file);
	}

	@Override
	public byte[] watermark(WatermarkParam watermarkParam) throws WatermarkException {
		ByteArrayOutputStream outputStream = null;
		try {
			Image srcImage = ImageIO.read(watermarkParam.getFile());
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
			ImageIcon imgIcon = new ImageIcon(FileUtil.readBytes(watermarkParam.getImageFile()));
			Image img = imgIcon.getImage();

			// 水印图片的位置
			if (!watermarkParam.getBespread()) {
				g.drawImage(img,
						srcImage.getWidth(null) / 2 - watermarkParam.getXMove(),
						srcImage.getHeight(null) / 2 - watermarkParam.getYMove(), null);
			}else {
				for (int height = watermarkParam.getYMove() + imgIcon.getIconHeight();
					 height < bufferImg.getHeight();
					 height = height + watermarkParam.getYMove()+ imgIcon.getIconHeight()) {
					for (int weight = watermarkParam.getXMove() + imgIcon.getIconWidth();
						 weight < bufferImg.getWidth();
						 weight = weight + watermarkParam.getXMove()+ imgIcon.getIconWidth()) {
						g.drawImage(img, weight - imgIcon.getIconWidth(), height
								- imgIcon.getIconHeight(), null);
					}
				}
			}
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
			g.dispose();
			outputStream = new ByteArrayOutputStream();
			ImageIO.write(bufferImg, FileUtil.extName(watermarkParam.getFile()), outputStream);
			return outputStream.toByteArray();
		}catch (Exception e) {
			logger.error("Failed to add watermark to the image {}", e.getMessage());
			throw new WatermarkException(e.getMessage());
		}finally {
			IoUtil.close(outputStream);
		}
	}
}
