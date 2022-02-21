package cn.darkjrong.watermark.domain;

import lombok.Getter;

import java.awt.*;
import java.io.File;

/**
 * 水印参数
 *
 * @author Rong.Jia
 * @date 2021/08/12 17:50:36
 */
@Getter
public class WatermarkParam {

    /**
     * 图片
     */
    private File file;

    /**
     * 水印图片, 与文本二选一
     */
    private File imageFile;

    /**
     * 水印文本, 与图片二选一
     */
    private String text;

    /**
     * 水印透明度
     */
    private Float alpha = 0.5f;

    /**
     * 水印文字大小
     */
    public Integer fontSize = 60;

    /**
     * 水印文字颜色
     */
    private Color color = Color.LIGHT_GRAY;

    /**
     * 水印旋转角度
     */
    private Float degree = 0.0F;

    /**
     * 水印之间的间隔
     */
    private Integer xMove = 80;

    /**
     * 水印之间的间隔
     */
    private Integer yMove = 80;

    /**
     *  是否铺满
     */
    private Boolean bespread = Boolean.FALSE;

    /**
     * 构建器
     * @return {@link WatermarkParam}  构建器对象
     */
    public static WatermarkParam builder() {
        return new WatermarkParam();
    }

    public WatermarkParam file(File file) {
        this.file = file;
        return this;
    }

    public WatermarkParam imageFile(File imageFile) {
        this.imageFile = imageFile;
        return this;
    }

    public WatermarkParam text(String text) {
        this.text = text;
        return this;
    }

    public WatermarkParam alpha(Float alpha) {
        this.alpha = alpha;
        return this;
    }

    public WatermarkParam fontSize(Integer fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public WatermarkParam color(Color color) {
        this.color = color;
        return this;
    }

    public WatermarkParam degree(Float degree) {
        this.degree = degree;
        return this;
    }

    public WatermarkParam yMove(Integer yMove) {
        this.yMove = yMove;
        return this;
    }

    public WatermarkParam xMove(Integer xMove) {
        this.xMove = xMove;
        return this;
    }

    public WatermarkParam bespread(Boolean bespread) {
        this.bespread = bespread;
        return this;
    }

    /**
     * 返回对象
     * @return WatermarkParam
     */
    public WatermarkParam build() {
        return this;
    }


}
