package cn.darkjrong.watermark.domain;

import cn.darkjrong.watermark.exceptions.WatermarkException;
import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * 原文件
 *
 * @author Rong.Jia
 * @date 2024/04/21
 */
@Slf4j
@Getter
@SuppressWarnings("ALL")
public class SrcFile {

    /**
     * 文件
     */
    private File file;

    /**
     * 文件字节数组
     */
    private byte[] bytes;

    public static SrcFile.Builder builder() {
        return new SrcFile.Builder();
    }

    public static class Builder {

        /**
         * 文件
         */
        private File file;

        /**
         * 文件字节数组
         */
        private byte[] bytes;

        public Builder file(File file) {
            this.file = file;
            return this;
        }

        public Builder bytes(byte[] bytes) {
            this.bytes = bytes;
            return this;
        }

        public SrcFile build() {
            if (ObjectUtil.isNull(this.file) && ObjectUtil.isNull(this.bytes)) {
                log.error("The file and byte array cannot be empty at the same time");
                throw new WatermarkException("'file'和'bytes'不能同时为空");
            }
            return new SrcFile(this);
        }
    }

    private SrcFile(Builder builder) {
        this.file = builder.file;
        this.bytes = builder.bytes;
    }






}
