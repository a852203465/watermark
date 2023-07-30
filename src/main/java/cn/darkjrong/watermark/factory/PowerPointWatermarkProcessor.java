package cn.darkjrong.watermark.factory;

import cn.darkjrong.watermark.FileTypeUtils;
import cn.darkjrong.watermark.domain.WatermarkParam;
import cn.darkjrong.watermark.enums.PictureTypeEnum;
import cn.darkjrong.watermark.exceptions.WatermarkException;
import cn.hutool.core.io.IoUtil;
import org.apache.poi.sl.usermodel.PictureData.PictureType;
import org.apache.poi.xslf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
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

//	@Override
//	protected byte[] watermark(WatermarkParam watermarkParam) throws WatermarkException {
//
//		Presentation pres = new Presentation(watermarkParam.getFile().getPath());
//		ByteArrayOutputStream outputStream = null;
//
//		try {
//			IMasterSlide master = pres.getMasters().get_Item(0);
//
//			Point2D.Float center = new Point2D.Float((float) pres.getSlideSize().getSize().getWidth() / 2,
//					(float) pres.getSlideSize().getSize().getHeight() / 2);
//
//			float width = 300;
//			float height = 300;
//			float x = (float) center.getX() - width / 2;
//			float y = (float) center.getY() - height / 2;
//
//			// 添加形狀
//			IAutoShape watermarkShape = master.getShapes().addAutoShape(ShapeType.Rectangle, x, y, width, height);
//
//			IPPImage image = pres.getImages().addImage(FileUtil.readBytes(watermarkParam.getImageFile()));
//
//			// 設置填充類型
//			watermarkShape.getFillFormat().setFillType(FillType.Picture);
//			watermarkShape.getFillFormat().getPictureFillFormat().getPicture().setImage(image);
//			watermarkShape.getFillFormat().getPictureFillFormat().setPictureFillMode(PictureFillMode.Stretch);
//			watermarkShape.getLineFormat().getFillFormat().setFillType(FillType.NoFill);
//
//			// 鎖定形狀以防止修改
//			watermarkShape.getAutoShapeLock().setSelectLocked(true);
//			watermarkShape.getAutoShapeLock().setSizeLocked(true);
//			watermarkShape.getAutoShapeLock().setTextLocked(true);
//			watermarkShape.getAutoShapeLock().setPositionLocked(true);
//			watermarkShape.getAutoShapeLock().setGroupingLocked(true);
//
//			outputStream = new ByteArrayOutputStream();
//			pres.save(outputStream, SaveFormat.Pptx);
//			return outputStream.toByteArray();
//		} finally {
//			IoUtil.close(outputStream);
//			if (ObjectUtil.isNotNull(pres)) pres.dispose();
//		}
//	}

    @Override
    public byte[] watermark(WatermarkParam watermarkParam) throws WatermarkException {
        XMLSlideShow pptx = null;
        ByteArrayOutputStream output = null;
        InputStream inputStream = null;
        try {
            inputStream = getInputStream(watermarkParam.getFile());
            pptx = new XMLSlideShow(inputStream);
            XSLFPictureData pictureData = pptx.addPicture(watermarkParam.getImageFile(), PictureType.forNativeID(PictureTypeEnum.forContentType(FileTypeUtils.getFileType(watermarkParam.getImageFile())).nativeId));
            BufferedImage image = ImageIO.read(watermarkParam.getImageFile());
            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();
            for (int i = 0; i < pptx.getSlideMasters().size(); i++) {
                XSLFSlideMaster slideMaster = pptx.getSlideMasters().get(i);
                Dimension pageSize = pptx.getPageSize();
                int srcWidth = pageSize.width;
                int srcHeight = pageSize.height;
                if (!watermarkParam.getBespread()) {
                    setAnchor(slideMaster, pictureData, srcWidth / 2.0 - watermarkParam.getXMove(),
                            srcHeight / 2.0 - watermarkParam.getYMove(), imageWidth, imageHeight);
                } else {
                    for (double y = 0; y < srcHeight; y = y + imageHeight + watermarkParam.getYMove()) {
                        for (double x = 0; x < srcWidth; x = x + imageWidth + watermarkParam.getXMove()) {
                            setAnchor(slideMaster, pictureData, x, y, imageWidth, imageHeight);
                        }
                    }
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

    /**
     * 设置锚
     *
     * @param slideMaster ppt
     * @param pictureData 图片数据
     * @param x           x
     * @param y           y
     * @param w           w
     * @param h           h
     */
    private void setAnchor(XSLFSlideMaster slideMaster, XSLFPictureData pictureData, double x, double y, double w, double h) {
        XSLFSlideLayout[] slideLayouts = slideMaster.getSlideLayouts();
        for (XSLFSlideLayout slidelayout : slideLayouts) {
            XSLFPictureShape pictureShape = slidelayout.createPicture(pictureData);
            pictureShape.setAnchor(new Rectangle2D.Double(x, y, w, h));
        }
    }


}
