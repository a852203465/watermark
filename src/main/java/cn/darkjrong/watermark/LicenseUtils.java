package cn.darkjrong.watermark;

import cn.hutool.core.lang.Assert;
import com.aspose.words.License;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 许可证工具类
 *
 * @author Rong.Jia
 * @date 2021/08/29
 */
public class LicenseUtils {

    private static final Logger logger = LoggerFactory.getLogger(LicenseUtils.class);
    private static final String LICENSE_XML = "license.xml";
    private static AtomicBoolean license = new AtomicBoolean(Boolean.FALSE);

    static {
        try {
            License wordLicense = new License();
            com.aspose.cells.License cellsLicense = new com.aspose.cells.License();
            com.aspose.slides.License slideLicense = new com.aspose.slides.License();
            com.aspose.pdf.License pdLicense = new com.aspose.pdf.License();
            cellsLicense.setLicense(LicenseUtils.class.getClassLoader().getResourceAsStream(LICENSE_XML));
            pdLicense.setLicense(LicenseUtils.class.getClassLoader().getResourceAsStream(LICENSE_XML));
            slideLicense.setLicense(LicenseUtils.class.getClassLoader().getResourceAsStream(LICENSE_XML));
            wordLicense.setLicense(LicenseUtils.class.getClassLoader().getResourceAsStream(LICENSE_XML));
            license.compareAndSet(Boolean.FALSE, Boolean.TRUE);
        } catch (Exception e) {
            license.compareAndSet(Boolean.FALSE, Boolean.FALSE);
            logger.error("License验证失败...  {}", e);
        }
    }

    /**
     * 验证许可证
     */
    public static void verificationLicense() {
        Assert.isTrue(license.get(), "License验证不通过...");
    }


}
