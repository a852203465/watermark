package cn.darkjrong.watermark.domain;

import cn.darkjrong.watermark.exceptions.WatermarkException;
import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * 图像文件
 *
 * @author Rong.Jia
 * @date 2024/04/21
 */
@Slf4j
@Getter
@SuppressWarnings("ALL")
public class ImageFile {

    /**
     * 水印图片
     */
    private File file;

    /**
     * 水印图片字节数组
     */
    private byte[] bytes;

    /**
     * 水印文本
     */
    private String text;

    public static ImageFile.Builder builder() {
        return new ImageFile.Builder();
    }

    public static class Builder {

        /**
         * 水印图片
         */
        private File file;

        /**
         * 水印图片字节数组
         */
        private byte[] bytes;

        /**
         * 水印文本
         */
        private String text;

        public Builder file(File file) {
            this.file = file;
            return this;
        }

        public Builder bytes(byte[] bytes) {
            this.bytes = bytes;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public ImageFile build() {
            if (ObjectUtil.isAllEmpty(this.file, this.bytes, this.text)) {
                log.error("Watermark file, watermark byte array, text cannot be empty at the same time");
                throw new WatermarkException("'imageFile','bytes','text'不能同时为空");
            }
            return new ImageFile(this);
        }
    }

    private ImageFile(Builder builder) {
        this.text = builder.text;
        this.file = builder.file;
        this.bytes = builder.bytes;
    }


}
