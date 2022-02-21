package cn.darkjrong.watermark.factory;

import cn.darkjrong.watermark.Converter;
import cn.darkjrong.watermark.FileTypeUtils;
import cn.darkjrong.watermark.exceptions.WatermarkException;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;

import java.io.File;
import java.io.InputStream;

/**
 * 水印抽象处理器
 *
 * @author Rong.Jia
 * @date 2021/08/12 14:31:18
 */
public abstract class AbstractWatermarkProcessor implements WatermarkProcessor {

    /**
     * 获取输入流
     *
     * @param file 文件
     * @return {@link InputStream}
     * @throws WatermarkException 水印异常
     */
    InputStream getInputStream(File file) throws WatermarkException {
        if (FileTypeUtils.isXls(file)) {
            return IoUtil.toStream(Converter.xls2Xlsx(file));
        }else if (FileTypeUtils.isDoc(file)) {
            return IoUtil.toStream(Converter.doc2Docx(file));
        }else if (FileTypeUtils.isPpt(file)) {
            return IoUtil.toStream(Converter.ppt2Pptx(file));
        }else {
            return FileUtil.getInputStream(file);
        }
    }


}
