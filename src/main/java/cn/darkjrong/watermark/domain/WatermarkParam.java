package cn.darkjrong.watermark.domain;

import cn.darkjrong.watermark.exceptions.WatermarkException;
import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

/**
 * 水印参数
 *
 * @author Rong.Jia
 * @date 2024/04/21
 */
@Slf4j
@Getter
@SuppressWarnings("ALL")
public class WatermarkParam {

    /**
     * 文件
     */
    private SrcFile file;

    /**
     * 水印文件
     */
    private ImageFile imageFile;

    /**
     * 水印透明度
     */
    private Float alpha = 0.5f;

    /**
     * 水印文字大小
     */
    private Integer fontSize = 60;

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

    public static WatermarkParam.Builder builder() {
        return new WatermarkParam.Builder();
    }

    public static class Builder {

        /**
         * 文件
         */
        private SrcFile file;

        /**
         * 水印文件
         */
        private ImageFile imageFile;

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

        public Builder file(SrcFile file) {
            this.file = file;
            return this;
        }

        public Builder imageFile(ImageFile imageFile) {
            this.imageFile = imageFile;
            return this;
        }

        public Builder alpha(Float alpha) {
            this.alpha = alpha;
            return this;
        }

        public Builder fontSize(Integer fontSize) {
            this.fontSize = fontSize;
            return this;
        }

        public Builder color(Color color) {
            this.color = color;
            return this;
        }

        public Builder degree(Float degree) {
            this.degree = degree;
            return this;
        }

        public Builder yMove(Integer yMove) {
            this.yMove = yMove;
            return this;
        }

        public Builder xMove(Integer xMove) {
            this.xMove = xMove;
            return this;
        }

        public Builder bespread(Boolean bespread) {
            this.bespread = bespread;
            return this;
        }

        public WatermarkParam build() {
            if (ObjectUtil.isNull(this.file) || ObjectUtil.isNull(this.imageFile)) {
                log.error("file and image cannot be empty");
                throw new WatermarkException("'file'和'imageFile'不能为空");
            }
            return new WatermarkParam(this);
        }
    }

    private WatermarkParam(Builder builder) {
        this.file = builder.file;
        this.imageFile = builder.imageFile;
        this.fontSize = builder.fontSize;
        this.alpha = builder.alpha;
        this.color = builder.color;
        this.bespread = builder.bespread;
        this.degree = builder.degree;
        this.xMove = builder.xMove;
        this.yMove = builder.yMove;
    }


}
