package cn.darkjrong.watermark.factory;

import cn.darkjrong.watermark.FileTypeUtils;
import cn.darkjrong.watermark.domain.ImageFile;
import cn.darkjrong.watermark.domain.WatermarkParam;
import cn.darkjrong.watermark.exceptions.WatermarkException;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import com.aspose.slides.*;
import lombok.extern.slf4j.Slf4j;

import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Power Point 水印处理器
 *
 * @author Rong.Jia
 * @date 2024/04/22
 */
@Slf4j
public class PowerPointWatermarkProcessor extends AbstractWatermarkProcessor {

    @Override
    public Boolean supportType(File file) {
        return FileTypeUtils.isPpts(file);
    }

    @Override
    public Boolean supportType(byte[] file) {
        return FileTypeUtils.isPpts(file);
    }

    @Override
    protected byte[] watermark(WatermarkParam watermarkParam) throws WatermarkException {

        ByteArrayOutputStream out = null;
        ByteArrayInputStream in = new ByteArrayInputStream(watermarkParam.getFile().getBytes());

        Presentation pres = new Presentation(in);
        IMasterSlideCollection presMasters = pres.getMasters();
        ImageFile imageFile = watermarkParam.getImageFile();

        try {
            BufferedImage bufferedImage = ImgUtil.toImage(imageFile.getBytes());
            IPPImage image = pres.getImages().addImage(imageFile.getBytes());
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
            out = new ByteArrayOutputStream();
            pres.save(out, SaveFormat.Pptx);
            return out.toByteArray();
        } catch (Exception e) {
            log.error(String.format("A watermark is incorrectly added to the PPT 【%s】", e.getMessage()), e);
            throw new WatermarkException(e.getMessage());
        } finally {
            IoUtil.close(in);
            IoUtil.close(out);
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
