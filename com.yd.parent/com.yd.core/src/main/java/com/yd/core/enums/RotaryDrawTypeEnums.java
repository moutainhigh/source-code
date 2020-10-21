package com.yd.core.enums;

public enum RotaryDrawTypeEnums {

    YD_DAZHUANPAN_DRAW("YD_DAZHUANPAN_DRAW", "优度大转盘抽奖"),
    YD_HONGBAO_DRAW("YD_HONGBAO_DRAW", "优度大转盘抽奖"),
    YD_JIUGONGGE_DRAW("YD_JIUGONGGE_DRAW", "优度九宫格抽奖");

    /**
     * 枚举编码
     */
    private String code;

    /**
     * 枚举描述
     */
    private String description;

    RotaryDrawTypeEnums(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static RotaryDrawTypeEnums getByCode(String code) {
        if (code == null) {
            return null;
        }
        for (RotaryDrawTypeEnums value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public String getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }
}
