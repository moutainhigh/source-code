package com.yd.core.enums;

/**
 * 商户交易流水来源枚举
 * @author wuyc
 * created 2019/11/7 11:44
 **/
public enum YdMerchantTransSourceEnum {


    YD_TO_PAY("yd_to_pay", "优度打款", "2", "https://c9.51jujibao.com/upload/2019/12/10/201912101025019840"),

    WITHDRAWAL("withdrawal", "提现", "3", "https://c9.51jujibao.com/upload/2019/12/10/201912101025019840"),

    TRANS_GIFT_ACCOUNT("trans_gift_account", "转入礼品账户", "4", "https://c9.51jujibao.com/upload/2019/12/10/201912101025469298"),

    ACCOUNT_TRANS("account_trans", "余额账户转入", "4", "https://c9.51jujibao.com/upload/2019/12/10/201912101025019840"),

    TOP_UP("top_up", "充值", "6", "https://c9.51jujibao.com/upload/2019/12/10/201912101025469298"),

    TRANS_ACCOUNT("trans_account", "转入余额账户", "7", "https://c9.51jujibao.com/upload/2019/12/10/201912101025469298"),

    GIFT_ACCOUNT_TRANS("gift_account_trans", "礼品账户转入", "7", "https://c9.51jujibao.com/upload/2019/12/10/201912101025019840"),

    YD_QR_CODE("qr_code", "APP二维码收钱", "8", "https://c9.51jujibao.com/upload/2019/12/10/201912101025019840"),

    PURCHASE_GIFT("purchase_gift", "购买礼品", "8", "https://c9.51jujibao.com/upload/2019/12/10/201912101025469298");

    /** 枚举编码 */
    private String code;

    /** 枚举描述 */
    private String description;

    /** 流水编码规则 */
    private String billCode;

    /** 图标 */
    private String icon;

    YdMerchantTransSourceEnum(String code, String description, String billCode, String icon) {
        this.code = code;
        this.icon = icon;
        this.billCode = billCode;
        this.description = description;
    }

    public static YdMerchantTransSourceEnum getByCode(String code) {
        if (code == null) {
            return null;
        }
        for (YdMerchantTransSourceEnum value : values()) {
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

    public String getIcon() {
        return icon;
    }

    public String getBillCode() {
        return billCode;
    }
}
