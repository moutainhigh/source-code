package com.yd.api.result.order;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Title:商户订单
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 19:35:32
 * @Version:1.1.0
 */
public class YdUserOrderResult extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Integer id;

    private String orderNo;

    private Integer merchantId;
    
    private Integer userId;

    private Integer userCouponId;
    
    private String remark;
    
    private Integer provinceId;
    
    private String province;
    
    private Integer cityId;
    
    private String city;
    
    private Integer districtId;
    
    private String district;

    private String address;
    
    /**  收货人手机号码
     */
    private String mobile;
    
    /**  收货人姓名
     */
    private String realname;

    /**  自提，配送 ZT | PS */
    private String receiveWay;

    /** 是否选中积分抵扣 */
    private String isCheckIntegralReduce;

    /** 是否选中旧机回收 */
    private String isCheckOldMachineReduce;

    /** 商品数量 */
    private Integer itemCount;
    
    /**  实际需要支付的金额
     */
    private Double payPrice;
    
    /**  订单总价格
     */
    private Double orderPrice;
    
    /**  优惠券面额
     */
    private Double couponPrice;
    
    /**  积分抵扣金额
     */
    private Double integralReducePrice;
    
    /**  旧机抵扣金额
     */
    private Double oldMachineReducePrice;

    /**  手动优惠金额
     */
    private Double manualReducePrice;

    /** 旧机抵扣手机名称(型号) */
    private String oldMobileName;

    /**  订单状态  WAIT_PAY(待付款), CANCEL(订单取消), WAIT_DELIVER(待发货), WAIT_HANDLE(待处理), WAIT_GOODS(待收货), SUCCESS(已完成)
     */
    private String orderStatus;
    
    /**  订单支付状态
     */
    private String payStatus;
    
    /** 支付时间 */
    private Date payTime;
    
    /**  支付流水编号
     */
    private String billNo;
    
    /** 发货状态 */
    private String shipStatus;
    
    /** 发货时间 */
    private Date shipTime;
    
    /** 确认收货状态 */
    private String confirmGoodsStatus;
    
    /** 确认收货时间 */
    private Date confirmGoodsTime;

    /** 支付重试次数, 默认为0 */
    private Integer payEntryCount;
    
    /** 快递公司 */
    private String expressCompany;
    
    /** 快递公司编号  */
    private String expressCompanyNumber;
    
    /** 快递单号 */
    private String expressOrderId;

    // ---------------------附加

    private List<YdUserOrderDetailResult> orderDetails;

    private YdGiftOrderResult giftOrderResult;

    // 自提订单二维码url
    private String qrCodeUrl;

    private Date expireTime;

    /** 订单商品数量 */
    private Integer orderItemCount;

    private String merchantName;

    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public Integer getProvinceId() {
        return provinceId;
    }
    
    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }
    public String getProvince() {
        return province;
    }
    
    public void setProvince(String province) {
        this.province = province;
    }
    public Integer getCityId() {
        return cityId;
    }
    
    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    public Integer getDistrictId() {
        return districtId;
    }
    
    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }
    public String getDistrict() {
        return district;
    }
    
    public void setDistrict(String district) {
        this.district = district;
    }
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    public String getMobile() {
        return mobile;
    }
    
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getRealname() {
        return realname;
    }
    
    public void setRealname(String realname) {
        this.realname = realname;
    }
    public Double getPayPrice() {
        return payPrice;
    }
    
    public void setPayPrice(Double payPrice) {
        this.payPrice = payPrice;
    }
    public Double getOrderPrice() {
        return orderPrice;
    }
    
    public void setOrderPrice(Double orderPrice) {
        this.orderPrice = orderPrice;
    }
    public Double getCouponPrice() {
        return couponPrice;
    }
    
    public void setCouponPrice(Double couponPrice) {
        this.couponPrice = couponPrice;
    }
    public Double getIntegralReducePrice() {
        return integralReducePrice;
    }
    
    public void setIntegralReducePrice(Double integralReducePrice) {
        this.integralReducePrice = integralReducePrice;
    }
    public Double getOldMachineReducePrice() {
        return oldMachineReducePrice;
    }
    
    public void setOldMachineReducePrice(Double oldMachineReducePrice) {
        this.oldMachineReducePrice = oldMachineReducePrice;
    }
    public String getPayStatus() {
        return payStatus;
    }
    
    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }
    public Date getPayTime() {
        return payTime;
    }
    
    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }
    public String getBillNo() {
        return billNo;
    }
    
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    public String getShipStatus() {
        return shipStatus;
    }
    
    public void setShipStatus(String shipStatus) {
        this.shipStatus = shipStatus;
    }
    public Date getShipTime() {
        return shipTime;
    }
    
    public void setShipTime(Date shipTime) {
        this.shipTime = shipTime;
    }
    public String getConfirmGoodsStatus() {
        return confirmGoodsStatus;
    }
    
    public void setConfirmGoodsStatus(String confirmGoodsStatus) {
        this.confirmGoodsStatus = confirmGoodsStatus;
    }
    public Date getConfirmGoodsTime() {
        return confirmGoodsTime;
    }
    
    public void setConfirmGoodsTime(Date confirmGoodsTime) {
        this.confirmGoodsTime = confirmGoodsTime;
    }
    public String getExpressCompany() {
        return expressCompany;
    }
    
    public void setExpressCompany(String expressCompany) {
        this.expressCompany = expressCompany;
    }
    public String getExpressCompanyNumber() {
        return expressCompanyNumber;
    }
    
    public void setExpressCompanyNumber(String expressCompanyNumber) {
        this.expressCompanyNumber = expressCompanyNumber;
    }
    public String getExpressOrderId() {
        return expressOrderId;
    }
    
    public void setExpressOrderId(String expressOrderId) {
        this.expressOrderId = expressOrderId;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<YdUserOrderDetailResult> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<YdUserOrderDetailResult> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public Double getManualReducePrice() {
        return manualReducePrice;
    }

    public void setManualReducePrice(Double manualReducePrice) {
        this.manualReducePrice = manualReducePrice;
    }

    public String getIsCheckIntegralReduce() {
        return isCheckIntegralReduce;
    }

    public void setIsCheckIntegralReduce(String isCheckIntegralReduce) {
        this.isCheckIntegralReduce = isCheckIntegralReduce;
    }

    public String getIsCheckOldMachineReduce() {
        return isCheckOldMachineReduce;
    }

    public void setIsCheckOldMachineReduce(String isCheckOldMachineReduce) {
        this.isCheckOldMachineReduce = isCheckOldMachineReduce;
    }

    public String getReceiveWay() {
        return receiveWay;
    }

    public void setReceiveWay(String receiveWay) {
        this.receiveWay = receiveWay;
    }

    public YdGiftOrderResult getGiftOrderResult() {
        return giftOrderResult;
    }

    public void setGiftOrderResult(YdGiftOrderResult giftOrderResult) {
        this.giftOrderResult = giftOrderResult;
    }

    public Integer getUserCouponId() {
        return userCouponId;
    }

    public void setUserCouponId(Integer userCouponId) {
        this.userCouponId = userCouponId;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getOrderItemCount() {
        return orderItemCount;
    }

    public void setOrderItemCount(Integer orderItemCount) {
        this.orderItemCount = orderItemCount;
    }

    public String getOldMobileName() {
        return oldMobileName;
    }

    public void setOldMobileName(String oldMobileName) {
        this.oldMobileName = oldMobileName;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }


    public Integer getPayEntryCount() {
        return payEntryCount;
    }

    public void setPayEntryCount(Integer payEntryCount) {
        this.payEntryCount = payEntryCount;
    }
}
