package cn.darkjrong.watermark.factory;

import cn.darkjrong.watermark.FileTypeUtils;
import cn.darkjrong.watermark.domain.WatermarkParam;
import cn.darkjrong.watermark.exceptions.WatermarkException;
import cn.hutool.core.io.IoUtil;
import com.aspose.words.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

/**
 * word处理器
 *
 * @author Rong.Jia
 * @date 2021/08/13 16:32:33
 */
public class WordWatermarkProcessor extends AbstractWatermarkProcessor {

    private static final Logger logger = LoggerFactory.getLogger(WordWatermarkProcessor.class);

    @Override
    public Boolean supportType(File file) {
        return FileTypeUtils.isWord(file);
    }

    @Override
    public byte[] watermark(WatermarkParam watermarkParam) throws WatermarkException {
        ByteArrayOutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            inputStream = getInputStream(watermarkParam.getFile());
            Document doc = new Document(inputStream);
            Paragraph watermarkPara = new Paragraph(doc);
            if (!watermarkParam.getBespread()) {
                Shape shape = buildShape(doc, watermarkParam.getImageFile(),
                        500 / 2.0 - watermarkParam.getYMove(),
                        700 / 2.0 - watermarkParam.getXMove());
                watermarkPara.appendChild(shape);
                insertWatermark(doc, watermarkPara);
            }else {
                for (int j = 0; j < 500; j = j + watermarkParam.getYMove()) {
                    for (int i = 0; i < 700; i = i + watermarkParam.getXMove()) {
                        Shape waterShape = buildShape(doc, watermarkParam.getImageFile(), j, i);
                        watermarkPara.appendChild(waterShape);
                    }
                }
                insertWatermark(doc, watermarkPara);
            }
            outputStream = new ByteArrayOutputStream();
            doc.save(outputStream, SaveFormat.DOCX);
            return outputStream.toByteArray();
        }catch (Exception e) {
            logger.error("WordWatermarkProcessor {}", e.getMessage());
            throw new WatermarkException(e.getMessage());
        }finally {
            IoUtil.close(outputStream);
            IoUtil.close(inputStream);
        }
    }

    /**
     * 插入水印
     *
     * @param watermarkPara 水印段落
     * @param doc 对象
     * @throws Exception 异常
     */
    private void insertWatermark(Document doc, Paragraph watermarkPara) throws Exception {
        for (Section sect : doc.getSections()) {
            insertWatermark(watermarkPara, sect, HeaderFooterType.HEADER_PRIMARY);
            insertWatermark(watermarkPara, sect, HeaderFooterType.HEADER_FIRST);
            insertWatermark(watermarkPara, sect, HeaderFooterType.HEADER_EVEN);
        }
    }

    /**
     * 插入水印
     *
     * @param watermarkPara 水印段落
     * @param sect          部件
     * @param headerType  {@link HeaderFooterType}   头标类型字段
     * @throws Exception 异常
     */
    private void insertWatermark(Paragraph watermarkPara, Section sect, int headerType) throws Exception {
        HeaderFooter header = sect.getHeadersFooters().getByHeaderFooterType(headerType);
        if (header == null) {
            header = new HeaderFooter(sect.getDocument(), headerType);
            sect.getHeadersFooters().add(header);
        }
        header.appendChild(watermarkPara.deepClone(true));
    }

    /**
     * 构建shape类
     *
     * @param doc            文档对象
     * @param watermarkParam 水印参数
     * @return {@link Shape} shape类
     * @throws Exception 异常
     */
    private Shape buildShape(Document doc, WatermarkParam watermarkParam) throws Exception {
        return buildShape(doc, watermarkParam.getImageFile(), watermarkParam.getYMove(), watermarkParam.getXMove());
    }

    /**
     * 构建shape类
     *
     * @param doc            文档对象
     * @param imgFile 水印图片
     * @param left 左
     * @param top  下
     * @return {@link Shape} shape类
     * @throws Exception 异常
     */
    private Shape buildShape(Document doc, File imgFile, double left, double top) throws Exception {

        Shape shape = new Shape(doc, ShapeType.IMAGE);
        BufferedImage sourceImg = ImageIO.read(imgFile);
        shape.setWidth(sourceImg.getWidth());
        shape.setHeight(sourceImg.getHeight());
        shape.getImageData().setImage(sourceImg);
        shape.setLeft(left);
        shape.setTop(top);
        shape.setWrapType(WrapType.NONE);

        return shape;
    }







}
