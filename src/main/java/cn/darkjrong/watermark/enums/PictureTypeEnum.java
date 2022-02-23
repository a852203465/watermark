package cn.darkjrong.watermark.enums;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 图片类型
 *
 * @author Rong.Jia
 * @date 2022/02/23
 */
@Getter
@AllArgsConstructor
public enum PictureTypeEnum {

    /** Extended windows meta file */
    EMF(2,2,"image/x-emf",".emf"),
    /** Windows Meta File */
    WMF(3,3,"image/x-wmf",".wmf"),
    /** Mac PICT format */
    PICT(4,4,"image/pict",".pict"),
    /** JPEG format */
    JPEG(5,5,"image/jpeg",".jpg"),
    /** PNG format */
    PNG(6,6,"image/png",".png"),
    /** Device independent bitmap */
    DIB(7,7,"image/dib",".dib"),
    /** GIF image format */
    GIF(-1,8,"image/gif",".gif"),
    /** Tag Image File (.tiff) */
    TIFF(17,9,"image/tiff",".tif"),
    /** Encapsulated Postscript (.eps) */
    EPS(-1,10,"image/x-eps",".eps"),
    /** Windows Bitmap (.bmp) */
    BMP(-1,11,"image/x-ms-bmp",".bmp"),
    /** WordPerfect graphics (.wpg) */
    WPG(-1,12,"image/x-wpg",".wpg"),
    /** Microsoft Windows Media Photo image (.wdp) */
    WDP(-1,13,"image/vnd.ms-photo",".wdp"),
    /** Scalable vector graphics (.svg) - supported by Office 2016 and higher */
    SVG(-1, -1, "image/svg+xml", ".svg"),
    /** Unknown picture type - specific to escher bse record */
    UNKNOWN(1, -1, "", ".dat"),
    /** Picture type error - specific to escher bse record */
    ERROR(0, -1, "", ".dat"),
    /** JPEG in the YCCK or CMYK color space. */
    CMYKJPEG( 18, -1, "image/jpeg", ".jpg"),
    /** client defined blip type - native-id 32 to 255 */
    CLIENT( 32, -1, "", ".dat")
    ;

    public final int nativeId, ooxmlId;
    public final String contentType,extension;

    /**
     * 对内容类型
     *
     * @param contentType 内容类型
     * @return {@link PictureTypeEnum}
     */
    public static PictureTypeEnum forContentType(String contentType) {
        for (PictureTypeEnum ans : values()) {
            if (StrUtil.equals(ans.contentType, contentType)) return ans;
        }

        return PictureTypeEnum.JPEG;
    }


}
