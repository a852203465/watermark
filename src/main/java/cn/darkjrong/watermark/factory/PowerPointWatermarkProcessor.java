package cn.darkjrong.watermark.factory;

import cn.darkjrong.watermark.FileTypeUtils;
import cn.darkjrong.watermark.domain.WatermarkParam;
import cn.darkjrong.watermark.exceptions.WatermarkException;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import org.apache.poi.sl.usermodel.PictureData.PictureType;
import org.apache.poi.xslf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.geom.Rectangle2D;
import java.io.*;

/**
 * ppt处理器
 *
 * @author Rong.Jia
 * @date 2021/08/13 17:45:49
 */
public class PowerPointWatermarkProcessor extends AbstractWatermarkProcessor {

	private static final Logger logger = LoggerFactory.getLogger(PowerPointWatermarkProcessor.class);

	@Override
	public Boolean supportType(File file) {
		return FileTypeUtils.isPpts(file);
	}

	@Override
	public void addWatermark(WatermarkParam watermarkParam, File target) throws WatermarkException {
		FileUtil.writeBytes(this.addWatermark(watermarkParam), target);
	}

	@Override
	public byte[] addWatermark(WatermarkParam watermarkParam) throws WatermarkException {
		XMLSlideShow pptx = null;
		ByteArrayOutputStream output = null;
		InputStream inputStream = null;
		try {
			inputStream = getInputStream(watermarkParam.getFile());
			pptx = new XMLSlideShow(inputStream);
			XSLFPictureData pictureData = pptx.addPicture(watermarkParam.getImageFile(), PictureType.PNG);
			for (int i=0;i<pptx.getSlideMasters().size();i++) {
				XSLFSlideMaster slideMaster = pptx.getSlideMasters().get(i);
				XSLFSlideLayout[] slideLayouts = slideMaster.getSlideLayouts();
				for (XSLFSlideLayout slidelayout : slideLayouts) {
					XSLFPictureShape pictureShape = slidelayout.createPicture(pictureData);
					pictureShape.setAnchor(new Rectangle2D.Double(20, 20, 640, 400));
				}
			}
			output = new ByteArrayOutputStream();
			pptx.write(output);
			return output.toByteArray();
		} catch (FileNotFoundException e) {
			logger.error("No file found {}", e.getMessage());
			throw new WatermarkException(e.getMessage());
		} catch (IOException e) {
			logger.error("A watermark is incorrectly added to the PPT {}", e.getMessage());
			throw new WatermarkException(e.getMessage());
		} finally {
			IoUtil.close(output);
			IoUtil.close(pptx);
			IoUtil.close(inputStream);
			delete(watermarkParam.getImageFile());
		}
	}
}
