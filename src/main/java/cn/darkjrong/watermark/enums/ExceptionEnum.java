package cn.darkjrong.watermark.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 异常枚举
 *
 * @author Rong.Jia
 * @date 2021/08/29
 */
@Getter
@AllArgsConstructor
public enum ExceptionEnum {

    // 必须是xxx类型文件
    THE_FILE_MUST_BE_OF_TYPE_XXX("The file must be of type %s"),










    ;




    private String value;


}
