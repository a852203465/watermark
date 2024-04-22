package cn.darkjrong.watermark.factory;

import cn.darkjrong.watermark.FileTypeUtils;
import cn.darkjrong.watermark.domain.WatermarkParam;
import cn.darkjrong.watermark.exceptions.WatermarkException;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage;
import org.docx4j.openpackaging.parts.SpreadsheetML.WorksheetPart;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.relationships.Relationship;
import org.xlsx4j.jaxb.Context;
import org.xlsx4j.sml.CTSheetBackgroundPicture;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Excel水印处理器
 *
 * @author Rong.Jia
 * @date 2024/04/22
 */
@Slf4j
public class ExcelWatermarkProcessor extends AbstractWatermarkProcessor {

    @Override
    public Boolean supportType(File file) {
        return FileTypeUtils.isExcel(file);
    }

    @Override
    public Boolean supportType(byte[] file) {
        return FileTypeUtils.isExcel(file);
    }

    @Override
    public byte[] watermark(WatermarkParam watermarkParam) throws WatermarkException {
        ByteArrayOutputStream outputStream = null;
        try {
            SpreadsheetMLPackage excelPackage = SpreadsheetMLPackage.load(getInputStream(watermarkParam.getFile().getBytes()));
            int size = excelPackage.getWorkbookPart().getContents().getSheets().getSheet().size();
            for (int i=0;i<size;i++) {
                WorksheetPart worksheet = excelPackage.getWorkbookPart().getWorksheet(i);
                createBgPic(worksheet, excelPackage, watermarkParam.getImageFile().getBytes());
            }
            outputStream = new ByteArrayOutputStream();
            excelPackage.save(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            log.error(String.format("Excel added watermark exception 【%s】", e.getMessage()), e);
            throw new WatermarkException(e.getMessage());
        }finally {
            IoUtil.close(outputStream);
        }
    }

    /**
     * 使用水印图片作为excel背景，达到水印效果<但打印时不会生效>
     *
     * @param worksheet      工作表
     * @param excelPackage excel package
     * @param imageFile      图像文件
     * @throws WatermarkException 水印异常
     */
    private void createBgPic(WorksheetPart worksheet, SpreadsheetMLPackage excelPackage, File imageFile) throws Exception {
        createBgPic(worksheet, excelPackage, FileUtil.readBytes(imageFile));
    }

    /**
     * 使用水印图片作为excel背景，达到水印效果<但打印时不会生效>
     *
     * @param worksheet      工作表
     * @param excelPackage excel package
     * @param imageFile      图像文件
     * @throws WatermarkException 水印异常
     */
    private void createBgPic(WorksheetPart worksheet, SpreadsheetMLPackage excelPackage, byte[] imageFile) throws Exception {
        CTSheetBackgroundPicture ctSheetBackgroundPicture = Context.getsmlObjectFactory().createCTSheetBackgroundPicture();
        worksheet.getContents().setPicture(ctSheetBackgroundPicture);
        BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(excelPackage, worksheet, imageFile);
        Relationship sourceRelationship = imagePart.getSourceRelationships().get(0);
        String imageRelId = sourceRelationship.getId();
        ctSheetBackgroundPicture.setId(imageRelId);
    }


}
