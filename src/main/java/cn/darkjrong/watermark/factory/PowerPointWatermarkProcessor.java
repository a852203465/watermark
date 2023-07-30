package cn.darkjrong.watermark.factory;

import cn.darkjrong.watermark.FileTypeUtils;
import cn.darkjrong.watermark.domain.WatermarkParam;
import cn.darkjrong.watermark.exceptions.WatermarkException;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import com.aspose.slides.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

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
    protected byte[] watermark(WatermarkParam watermarkParam) throws WatermarkException {

        Presentation pres = new Presentation(watermarkParam.getFile().getPath());
        ByteArrayOutputStream outputStream = null;
        IMasterSlideCollection presMasters = pres.getMasters();

        try {
            BufferedImage bufferedImage = ImageIO.read(watermarkParam.getImageFile());
            IPPImage image = pres.getImages().addImage(FileUtil.readBytes(watermarkParam.getImageFile()));
            int imageWidth = bufferedImage.getWidth();
            int imageHeight = bufferedImage.getHeight();

            for (IMasterSlide master : presMasters) {
                Dimension2D dimension2D = pres.getSlideSize().getSize();
                float srcWidth = Convert.toFloat(dimension2D.getWidth());
                float srcHeight = Convert.toFloat(dimension2D.getHeight());
                if (!watermarkParam.getBespread()) {
                    IAutoShape watermarkShape = master.getShapes().addAutoShape(ShapeType.Rectangle, srcWidth / 2.0F - watermarkParam.getXMove(),
                            srcHeight / 2.0F - watermarkParam.getYMove(), imageWidth, imageHeight);
                    setShape(watermarkShape, image);
                } else {
                    for (float y = 0; y < srcHeight; y = y + imageHeight + watermarkParam.getYMove()) {
                        for (float x = 0; x < srcWidth; x = x + imageWidth + watermarkParam.getXMove()) {
                            IAutoShape watermarkShape = master.getShapes().addAutoShape(ShapeType.Rectangle, x, y, imageWidth, imageHeight);
                            setShape(watermarkShape, image);
                        }
                    }
                }
            }
            outputStream = new ByteArrayOutputStream();
            pres.save(outputStream, SaveFormat.Pptx);
            return outputStream.toByteArray();
        } catch (Exception e) {
            logger.error("A watermark is incorrectly added to the PPT {}", e.getMessage());
            throw new WatermarkException(e.getMessage());
        } finally {
            IoUtil.close(outputStream);
            if (ObjectUtil.isNotNull(pres)) {
                pres.dispose();
            }
        }
    }

    private void setShape(IAutoShape shape, IPPImage image) {

        // 設置填充類型
        shape.getFillFormat().setFillType(FillType.Picture);
        shape.getFillFormat().getPictureFillFormat().getPicture().setImage(image);
        shape.getFillFormat().getPictureFillFormat().setPictureFillMode(PictureFillMode.Stretch);
        shape.getLineFormat().getFillFormat().setFillType(FillType.NoFill);

        // 鎖定形狀以防止修改
        shape.getAutoShapeLock().setSelectLocked(true);
        shape.getAutoShapeLock().setSizeLocked(true);
        shape.getAutoShapeLock().setTextLocked(true);
        shape.getAutoShapeLock().setPositionLocked(true);
        shape.getAutoShapeLock().setGroupingLocked(true);
    }





}
