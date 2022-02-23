package cn.darkjrong.watermark;

import cn.darkjrong.watermark.exceptions.WatermarkException;
import org.junit.jupiter.api.Test;

import java.io.File;

public class ImageUtilsTest {
    
    @Test
    public void createImage() throws WatermarkException {

        ImageUtils.createImage(new File("F:\\4 - 副本.jpeg"), 10f, 5f);
        
    }


}
