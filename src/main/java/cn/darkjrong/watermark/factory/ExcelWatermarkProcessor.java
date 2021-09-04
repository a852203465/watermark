package cn.darkjrong.watermark.factory;

import cn.darkjrong.watermark.Converter;
import cn.darkjrong.watermark.FileTypeUtils;
import cn.darkjrong.watermark.domain.WatermarkParam;
import cn.darkjrong.watermark.exceptions.WatermarkException;
import cn.hutool.core.io.FileUtil;
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage;
import org.docx4j.openpackaging.parts.SpreadsheetML.WorksheetPart;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.relationships.Relationship;
import org.xlsx4j.jaxb.Context;
import org.xlsx4j.sml.CTSheetBackgroundPicture;

import java.io.File;

/**
 * excel处理器
 *
 * @author Rong.Jia
 * @date 2021/08/13 14:01:45
 */
public class ExcelWatermarkProcessor extends AbstractWatermarkProcessor {

    @Override
    public Boolean supportType(File file) {
        return FileTypeUtils.isExcel(file);
    }

        @Override
    public void process(WatermarkParam watermarkParam) throws WatermarkException {

        File file = null;
        try {
             file = watermarkParam.getFile();
            if (FileTypeUtils.isXls(file)) {
                file = FileUtil.writeBytes(Converter.xls2Xlsx(file), file.getPath() + "x");
            }

            SpreadsheetMLPackage excelMLPackage = SpreadsheetMLPackage.load(file);
            int size = excelMLPackage.getWorkbookPart().getContents().getSheets().getSheet().size();
            for (int i=0;i<size;i++) {
                WorksheetPart worksheet = excelMLPackage.getWorkbookPart().getWorksheet(i);
                createBgPic(worksheet, excelMLPackage, watermarkParam.getImageFile());
            }
            excelMLPackage.save(file);
            if (FileTypeUtils.isXls(file)) {
                FileUtil.writeBytes(Converter.xlsx2Xls(file), watermarkParam.getFile());
            }
        } catch (Exception e) {
            throw new WatermarkException("Docx4JException", e);
        }finally {
            if (FileTypeUtils.isXls(file)) {
                try {
                    FileUtil.del(file);
                }catch (Exception ignored){}
            }
        }

    }

    /**
     * 使用水印图片作为excel背景，达到水印效果<但打印时不会生效>
     *
     * @param worksheet      工作表
     * @param excelMLPackage excelmlpackage
     * @param imageFile      图像文件
     * @throws WatermarkException 水印异常
     */
    private void createBgPic(WorksheetPart worksheet, SpreadsheetMLPackage excelMLPackage, File imageFile) throws Exception {

        CTSheetBackgroundPicture ctSheetBackgroundPicture = Context.getsmlObjectFactory().createCTSheetBackgroundPicture();
        worksheet.getContents().setPicture(ctSheetBackgroundPicture);
        BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(excelMLPackage, worksheet, FileUtil.readBytes(imageFile));
        Relationship sourceRelationship = imagePart.getSourceRelationships().get(0);
        String imageRelId = sourceRelationship.getId();
        ctSheetBackgroundPicture.setId(imageRelId);
    }



}
