package cn.darkjrong.watermark.domain;

import lombok.Builder;
import lombok.Getter;

/**
 * 字体属性
 *
 * @author Rong.Jia
 * @date 2024/04/22
 */
@Getter
@Builder
public class FontAttribute {

    private int width;
    private int height;

}
