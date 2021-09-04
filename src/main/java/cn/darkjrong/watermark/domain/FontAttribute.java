package cn.darkjrong.watermark.domain;

import lombok.Builder;
import lombok.Data;

/**
 * 字体属性
 *
 * @author Rong.Jia
 * @date 2021/08/12 15:05:27
 */
@Data
@Builder
public class FontAttribute {

    private int width;
    private int height;

}
