package com.yd.api.pay;

public enum EnumPayMethod {
    ALIPAY_WAP("alipayWap", "支付宝"),
    WECHAT_MP("wechatMp", "微信"),
    FREE("free", "免费");;
    private String code;
    private String name;

    private EnumPayMethod(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static EnumPayMethod getByCode(String code) {
        for (EnumPayMethod item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

}
