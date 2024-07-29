package com.meet.api.enums;

/**
 * @Description 是否启用
 * @Author WangFang
 * @Date 2020/2/5
 */
public enum EnabledEnum {
    /** 是 */
    Y(1),

    /** 否 */
    N(0);

    /** 是否启用 */
    private Integer value;

    EnabledEnum(Integer value) {
        this.value = value;
    }
    public Integer getValue() {
        return this.value;
    }
}
