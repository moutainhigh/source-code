package com.yd.api.pay.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.thoughtworks.xstream.XStream;
import com.yd.core.utils.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.Security;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;

/**
 * 微信H5支付工具类
 *
 * @author Xulg
 * Created in 2019-07-29 19:13
 */
@SuppressWarnings("SpellCheckingInspection")
public class WechatH5PayUtils {
    private static final Logger logger = LoggerFactory.getLogger(WechatH5PayUtils.class);

    private static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * 下单接口地址
     */
    private static final String PAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    /**
     * 退款接口地址
     */
    private static final String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";

    /**
     * 公众账号ID
     */
    private static final String APP_ID = "wxe95ee20aa1a7d92c";

    /**
     * 商户号
     */
    private static final String MCH_ID = "1557295581";

    /**
     * 交易类型(H5支付的交易类型为MWEB)
     */
    private static final String TRADE_TYPE = "MWEB";

    /**
     * 签名key
     */
    @SuppressWarnings("FieldCanBeLocal")
    private static String signKey = "192006250b4c09247ec02edce69f6a21";

    private static final String SUCCESS = "SUCCESS";

    /**
     * 发起H5微信支付
     *
     * @param outOrderId       外部订单号(商户订单号)
     * @param totalFee         支付金额(分)
     * @param goodsDescription 商品描述(腾讯充值中心-QQ会员充值)
     * @param clientIp         客户端ip地址
     * @param notifyUrl        异步通知地址
     * @param sceneWapUrl      WAP网站URL地址
     * @param sceneWapName     WAP网站名
     */
    public static WechatSubmitH5PayResponse submitH5WechatPay(String outOrderId, int totalFee, String goodsDescription, String clientIp,
                                                              String notifyUrl, String sceneWapUrl, String sceneWapName, String macId,
                                                              String appId, String signKey) {
        if (StringUtils.isBlank(outOrderId)) {
            throw new BusinessException("err_no_out_order_id",
                    "本系统外部订单号不能为空");
        }

        if (totalFee <= 0) {
            throw new BusinessException("err_no_order_price",
                    "支付金额不能为负");
        }
        if (StringUtils.isBlank(goodsDescription)) {
            throw new BusinessException("err_no_body",
                    "商品描述不能为空");
        }
        if (StringUtils.isNotBlank(goodsDescription)
                && goodsDescription.length() > 128) {
            throw new BusinessException("err_no_body_length",
                    "商品描述不能超过128个字符");
        }
        if (StringUtils.isBlank(clientIp)) {
            throw new BusinessException("err_no_client_ip",
                    "商客户端ip地址不能为空");
        }
        if (StringUtils.isBlank(notifyUrl)) {
            throw new BusinessException("err_no_notify_url",
                    "异步通知回调地址不能为空");
        }
        if (StringUtils.isBlank(sceneWapUrl)) {
            throw new BusinessException("err_no_wap_url",
                    "场景WAP网站URL不能为空");
        }
        if (StringUtils.isBlank(sceneWapName)) {
            throw new BusinessException("err_no_wap_name",
                    "场景WAP网站名不能为空");
        }
        Map<String, String> requestParameters = new TreeMap<>();
        // 公众账号ID
        requestParameters.put("appid", appId);
        // 商户号
        requestParameters.put("mch_id", macId);
        // 随机字符串
        requestParameters.put("nonce_str", randomString());
        // 签名类型
        requestParameters.put("sign_type", "MD5");
        // 商品描述
        requestParameters.put("body", goodsDescription);
        // 商户订单号
        requestParameters.put("out_trade_no", outOrderId);
        // 总金额
        requestParameters.put("total_fee", String.valueOf(totalFee));
        // 终端IP
        requestParameters.put("spbill_create_ip", clientIp);
        // 通知地址
        requestParameters.put("notify_url", notifyUrl);
        // 交易类型
        requestParameters.put("trade_type", TRADE_TYPE);
        // 场景信息
        requestParameters.put("scene_info", createSceneInfo(sceneWapUrl, sceneWapName));
        // 签名
        requestParameters.put("sign", createSign(requestParameters, signKey));

        // 发起请求
        String requestParameterXmlString = XmlUtils.map2XmlString(requestParameters);
        logger.info("微信H5支付请求参数xml字符串====>>>" + requestParameterXmlString);
        String resultXmlString = sendHttpsPostRequest(PAY_URL, requestParameterXmlString);
        logger.info("微信H5支付返回数据xml字符串====>>>" + resultXmlString);

        // 解析返回结果
        JSONObject result = new JSONObject(XmlUtils.xmlString2Map(resultXmlString));
        logger.info("微信H5支付返回数据json字符串====>>>" + JSON.toJSONString(result, true));
        String returnCode = result.getString("return_code");
        String resultCode = result.getString("result_code");

        // 返回结果
        WechatSubmitH5PayResponse response = new WechatSubmitH5PayResponse();
        response.setReturnCode(returnCode);
        response.setResultCode(resultCode);
        response.setReturnMsg(result.getString("return_msg"));
        if (SUCCESS.equalsIgnoreCase(returnCode) && SUCCESS.equalsIgnoreCase(resultCode)) {
            // 交易类型
            String tradeType = result.getString("trade_type");
            // 预支付交易会话标识(微信生成的预支付回话标识，用于后续接口调用中使用，
            // 该值有效期为2小时,针对H5支付此参数无特殊用途)
            String prepayId = result.getString("prepay_id");
            // 支付跳转链接(mweb_url为拉起微信支付收银台的中间页面，
            // 可通过访问该url来拉起微信客户端，完成支付,mweb_url的有效期为5分钟)
            String mwebUrl = result.getString("mweb_url");
            response.setSuccess(true);
            response.setAppId(result.getString("appid"));
            response.setMchId(result.getString("mch_id"));
            response.setDeviceInfo(result.getString("device_info"));
            response.setNonceStr(result.getString("nonce_str"));
            response.setSign(result.getString("sign"));

            response.setErrCode(result.getString("err_code"));
            response.setErrCodeDes(result.getString("err_code_des"));
            response.setTradeType(tradeType);
            response.setPrepayId(prepayId);
            response.setMwebUrl(mwebUrl);
        } else if (SUCCESS.equalsIgnoreCase(returnCode)) {
            // 发起支付失败
            response.setSuccess(false);
            response.setAppId(result.getString("appid"));
            response.setMchId(result.getString("mch_id"));
            response.setDeviceInfo(result.getString("device_info"));
            response.setNonceStr(result.getString("nonce_str"));
            response.setSign(result.getString("sign"));
            response.setErrCode(result.getString("err_code"));
            response.setErrCodeDes(result.getString("err_code_des"));
        } else {
            // 请求失败
            response.setSuccess(false);
        }
        return response;
    }

    /**
     * 处理H5微信支付的回调信息
     *
     * @param request          the http servlet request
     * @param response         the http servlet response
     * @param outOrderPrefix   外部订单的前缀
     * @param businessFunction 回调成功后的业务逻辑
     */
    public static <R> void processH5WechatPayCallback(HttpServletRequest request, HttpServletResponse response, String outOrderPrefix,
                                                      Function<WechatH5CallbackParameterHolder, R> businessFunction) {
        response.setContentType("application/xml");
        JSONObject responseData = new JSONObject(parseDataFromStream(request));
        logger.info("微信H5支付回调接收到的参数====>>>" + JSON.toJSONString(responseData, true));

        // 返回状态码
        String returnCode = responseData.getString("return_code");
        // 返回信息
        String returnMsg = responseData.getString("return_msg");
        // 业务结果
        String resultCode = responseData.getString("result_code");

        // 支付失败
        if (!SUCCESS.equalsIgnoreCase(returnCode) || !SUCCESS.equalsIgnoreCase(resultCode)) {
            logger.info("微信H5支付回调, 支付失败returnCode:{}, returnMsg:{}, resultCode:{}=====>>>",
                    returnCode, returnMsg, resultCode);
            writeResponse(response, "FAIL", "支付失败");
            return;
        }

        // 签名验证
        if (!checkSign(responseData)) {
            String sign = responseData.getString("sign");
            Map<String, String> signMap = new TreeMap<>();
            for (Map.Entry<String, Object> entry : responseData.entrySet()) {
                signMap.put(entry.getKey(), entry.getValue().toString());
            }
            String mySign = createSign(signMap, signKey);
            logger.info("微信H5支付回调验证签名失败, sign:{}, mySign:{}******signMap:{}=====>>>",
                    sign, mySign, JSON.toJSONString(signMap));
            writeResponse(response, "FAIL", "验签失败");
            return;
        }

        // 公众账号ID
        String appId = responseData.getString("appid");
        // 商户号
        String mchId = responseData.getString("mch_id");
        // 设备号
        String deviceInfo = responseData.getString("device_info");
        // 随机字符串
        String nonceStr = responseData.getString("nonce_str");
        // 签名
        String sign = responseData.getString("sign");
        // 签名类型
        String signType = responseData.getString("sign_type");
        // 错误代码
        String errCode = responseData.getString("err_code");
        // 错误代码描述
        String errCodeDes = responseData.getString("err_code_des");
        // 用户在商户appid下的唯一标识
        String openId = responseData.getString("openid");
        // 用户是否关注公众账号，Y-关注，N-未关注
        String isSubscribe = responseData.getString("is_subscribe");
        // 交易类型
        String tradeType = responseData.getString("trade_type");
        // 付款银行
        String bankType = responseData.getString("bank_type");
        // 订单金额(分)
        Integer totalFee = responseData.getInteger("total_fee");
        // 应结订单金额=订单金额-非充值代金券金额，应结订单金额<=订单金额
        Integer settlementTotalFee = responseData.getInteger("settlement_total_fee");
        // 货币种类
        String feeType = responseData.getString("fee_type");
        // 现金支付金额
        Integer cashFee = responseData.getInteger("cash_fee");
        // 现金支付货币类型
        String cashFeeType = responseData.getString("cash_fee_type");
        // 总代金券金额
        Integer couponFee = responseData.getInteger("coupon_fee");
        // 代金券使用数量
        Integer couponCount = responseData.getInteger("coupon_count");
        /*
            代金券类型(CASH--充值代金券, NO_CASH---非充值代金券)
            并且订单使用了免充值券后有返回（取值：CASH、NO_CASH）
            $n为下标,从0开始编号，举例：coupon_type_0)
            responseData.getString("coupon_type_$n");
            代金券ID(代金券ID,$n为下标，从0开始编号)
            responseData.getString("coupon_id_$n");
        */
        // 微信支付订单号
        String transactionId = responseData.getString("transaction_id");
        // 商户订单号
        String outTradeNo = responseData.getString("out_trade_no");
        // 商家数据包
        String attach = responseData.getString("attach");
        // 支付完成时间(支付完成时间，格式为yyyyMMddHHmmss)
        Date timeEnd = parseDate(responseData.getString("time_end"));

        try {
            // 执行回调逻辑
            WechatH5PayUtils.doProcessNotify(outOrderPrefix, outTradeNo, transactionId, resultCode,
                    totalFee, openId, timeEnd, businessFunction);
            // 操作成功
            WechatH5PayUtils.writeResponse(response, SUCCESS, "OK");
        } catch (BusinessException e) {
            e.printStackTrace();
            logger.error("wechatH5NotifyUrl业务操作执行失败", e);
            writeResponse(response, "FAIL", "业务执行失败");
        }
    }

    /**
     * 提交H5支付退款申请
     *
     * @param outOrderId       外部订单号(商户订单号)
     * @param orderPrice       订单金额(分)
     * @param refundMoney      退款金额(分)
     * @param outRefundOrderId 商户的退款订单号
     * @param refundReason     退款原因(可为空)
     * @param notifyUrl        通知地址(可为空)
     * @return void
     */
    public static Void submitH5WechatRefundApply(String outOrderId, int orderPrice, int refundMoney,
                                                 String outRefundOrderId, String refundReason, String notifyUrl) {
        if (StringUtils.isBlank(outOrderId)) {
            throw new BusinessException("err_no_out_order_id",
                    "本系统外部订单号不能为空");
        }
        if (orderPrice <= 0) {
            throw new BusinessException("err_no_order_price",
                    "订单金额不能为负");
        }
        if (refundMoney <= 0) {
            throw new BusinessException("err_no_refund_money",
                    "退款金额不能为负");
        }
        if (StringUtils.isBlank(outRefundOrderId)) {
            throw new BusinessException("err_no_refund_order_id",
                    "本系统退款订单号不能为空");
        }

        Map<String, String> requestParameters = new TreeMap<>();
        // 公众账号ID
        requestParameters.put("appid", APP_ID);
        // 商户号
        requestParameters.put("mch_id", MCH_ID);
        // 随机字符串
        requestParameters.put("nonce_str", randomString());
        // 签名类型
        requestParameters.put("sign_type", "MD5");
        // 商户订单号
        requestParameters.put("out_trade_no", outOrderId);
        // 商户退款订单id
        requestParameters.put("out_refund_no", outRefundOrderId);
        // 订单金额
        requestParameters.put("total_fee", String.valueOf(orderPrice));
        // 退款金额
        requestParameters.put("refund_fee", String.valueOf(refundMoney));
        // 退款原因
        requestParameters.put("refund_desc", refundReason);
        // 通知地址
        requestParameters.put("notify_url", notifyUrl);
        // 签名
        requestParameters.put("sign", createSign(requestParameters, signKey));

        // 读取微信的证书
        BufferedInputStream in = null;
        try {
            in = FileUtil.getInputStream("/home/www/web-deploy/conf/cert/apiclient_jdd_cert.p12");
        } catch (RuntimeException e) {
            logger.error("加载微信证书异常", e);
            throw new BusinessException("err_no_wechat_cert", "加载微信证书失败");
        } finally {
            IoUtil.close(in);
        }

        CloseableHttpClient httpClient = null;
        String resultXmlString;
        try {
            // 加载证书
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(in, MCH_ID.toCharArray());
            SSLContext sslContext = SSLContexts.custom().loadKeyMaterial(keyStore, MCH_ID.toCharArray()).build();
            SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslContext, new String[]{"TLSv1"},
                    null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            httpClient = HttpClients.custom().setSSLSocketFactory(factory).build();
            // 发起请求
            String requestParameterXmlString = XmlUtils.map2XmlString(requestParameters);
            logger.info("微信H5退款请求参数xml字符串====>>>" + requestParameterXmlString);
            HttpPost httpPost = new HttpPost(REFUND_URL);
            httpPost.setEntity(new StringEntity(requestParameterXmlString, Charset
                    .forName(DEFAULT_CHARSET)));
            resultXmlString = new String(IoUtil.readBytes(httpClient.execute(httpPost)
                    .getEntity().getContent()), DEFAULT_CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("微信退款请求失败", e);
            throw new BusinessException("err_wechat_refund_req", "微信退款请求失败");
        } finally {
            IoUtil.close(httpClient);
        }
        logger.info("微信H5退款返回数据xml字符串====>>>" + resultXmlString);

        // 解析返回结果
        JSONObject result = new JSONObject(XmlUtils.xmlString2Map(resultXmlString));
        logger.info("微信H5支付返回数据json字符串====>>>" + JSON.toJSONString(result, true));
        String returnCode = result.getString("return_code");
        String resultCode = result.getString("result_code");

        // 返回结果
        result.getString("return_msg");
        if (SUCCESS.equalsIgnoreCase(returnCode) && SUCCESS.equalsIgnoreCase(resultCode)) {
            // 错误代码
            result.getString("err_code");
            // 错误代码描述
            result.getString("err_code_des");
            // 公众账号ID
            result.getString("appid");
            // 商户号
            result.getString("mch_id");
            // 随机字符串
            result.getString("nonce_str");
            // 签名
            result.getString("sign");
            // 微信订单号
            result.getString("transaction_id");
            // 商户订单号
            result.getString("out_trade_no");
            // 商户退款单号
            result.getString("out_refund_no");
            // 微信退款单号
            result.getString("refund_id");
            // 退款金额(分)
            result.getIntValue("refund_fee");
            // 应结退款金额(分)
            result.getIntValue("settlement_refund_fee");
            // 标价金额(分)
            result.getIntValue("total_fee");
            // 应结订单金额(分)
            result.getIntValue("settlement_total_fee");
            // 标价币种
            result.getString("fee_type");
            // 现金支付金额
            result.getIntValue("cash_fee");
            // 现金支付币种
            result.getString("cash_fee_type");
            // 现金退款金额
            result.getIntValue("cash_refund_fee");
            /*
             * 代金券类型
             * CASH--充值代金券
             * NO_CASH---非充值代金券
             * 订单使用代金券时有返回（取值：CASH、NO_CASH）。$n为下标,从0开始编号，举例：coupon_type_0
             */
            result.getString("coupon_type_$n");
            /*
             * 代金券退款总金额
             * 代金券退款金额<=退款金额，退款金额-代金券或立减优惠退款金额为现金，说明详见代金券或立减优惠
             */
            result.getIntValue("coupon_refund_fee");
            /*
             * 单个代金券退款金额
             * 代金券退款金额<=退款金额，退款金额-代金券或立减优惠退款金额为现金，说明详见代金券或立减优惠
             */
            result.getIntValue("coupon_refund_fee_$n");
            // 退款代金券使用数量
            result.getIntValue("coupon_refund_count");
            /*
             * 退款代金券ID
             * 退款代金券ID, $n为下标，从0开始编号
             */
            result.getString("coupon_refund_id_$n");
        } else if (SUCCESS.equalsIgnoreCase(returnCode)) {
            // 发起退款失败
        } else {
            // 请求失败
        }
        return null;
    }

    /**
     * 处理H5微信退款的回调信息
     *
     * @param request          the http servlet request
     * @param response         the http servlet response
     * @param businessFunction 回调成功后的业务逻辑
     * @return void
     */
    public static <R> Void processH5WechatRefundCallback(HttpServletRequest request, HttpServletResponse response,
                                                         Function<Object, R> businessFunction) {
        /*
         * 返回状态码
         * SUCCESS/FAIL
         * 此字段是通信标识，非交易标识，交易是否成功需要查看trade_state来判断
         */
        request.getParameter("return_code");
        /*
         * 返回信息
         * 当return_code为FAIL时返回信息为错误原因，例如签名失败
         */
        request.getParameter("return_msg");
        /*
         * 公众账号ID
         * 微信分配的公众账号ID（企业号corpid即为此appId）
         */
        request.getParameter("appid");
        /*
         * 退款的商户号
         * 微信支付分配的商户号
         */
        request.getParameter("mch_id");
        /*
         * 随机字符串
         */
        request.getParameter("nonce_str");
        /*
         * 加密信息
         * 加密信息请用商户秘钥进行解密
         */
        String encrypteReqInfo = request.getParameter("req_info");
        // 解密信息
        String decryptReqInfo = decryptData(encrypteReqInfo);

        // 返回的数据
        JSONObject reqInfo = new JSONObject(XmlUtils.xmlString2Map(decryptReqInfo));
        /*
         * 微信订单号
         */
        reqInfo.getString("transaction_id");
        /*
         * 商户订单号
         * 商户系统内部的订单号
         */
        reqInfo.getString("out_trade_no");
        /*
         * 微信退款单号
         */
        reqInfo.getString("refund_id");
        /*
         * 商户退款单号
         */
        reqInfo.getString("out_refund_no");
        /*
         * 订单金额
         * 订单总金额，单位为分，只能为整数
         */
        reqInfo.getIntValue("total_fee");
        /*
         * 应结订单金额(分)
         */
        reqInfo.getIntValue("settlement_total_fee");
        /*
         * 申请退款金额(分)
         * 退款总金额,单位为分
         */
        reqInfo.getIntValue("refund_fee");
        /*
         * 退款金额
         * 退款金额=申请退款金额-非充值代金券退款金额，退款金额<=申请退款金额
         */
        reqInfo.getIntValue("settlement_refund_fee");
        /*
         * 退款状态
         * SUCCESS-退款成功
         * CHANGE-退款异常
         * REFUNDCLOSE—退款关闭
         */
        reqInfo.getString("refund_status");
        /*
         * 退款成功时间
         * 资金退款至用户帐号的时间，格式2017-12-15 09:46:01
         */
        reqInfo.getDate("success_time");
        /*
         * 退款入账账户
         * 取当前退款单的退款入账方
         * 1）退回银行卡：
         * {银行名称}{卡类型}{卡尾号}
         * 2）退回支付用户零钱:
         * 支付用户零钱
         * 3）退还商户:
         * 商户基本账户
         * 商户结算银行账户
         * 4）退回支付用户零钱通:
         * 支付用户零钱通
         */
        reqInfo.getString("refund_recv_accout");
        /*
         * 退款资金来源
         * REFUND_SOURCE_RECHARGE_FUNDS 可用余额退款/基本账户
         * REFUND_SOURCE_UNSETTLED_FUNDS 未结算资金退款
         */
        reqInfo.getString("refund_account");
        /*
         * 退款发起来源
         * API接口
         * VENDOR_PLATFORM商户平台
         */
        reqInfo.getString("refund_request_source");
        return null;
    }

    private static String decryptData(String base64Data) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
            SecretKeySpec key = new SecretKeySpec(MD5Util.MD5Encode(signKey,
                    DEFAULT_CHARSET).toLowerCase().getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64Util.decode(base64Data)));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /* ********************************private method******************************* */

    private static <R> void doProcessNotify(String outOrderPrefix, String outTradeNo,
                                            String transactionId, String resultCode,
                                            Integer totalFee, String openId, Date timeEnd,
                                            Function<WechatH5CallbackParameterHolder, R> businessFunction) {
        if (businessFunction == null) {
            return;
        }

        // 订单号
        String orderId;
        if (outOrderPrefix != null) {
            // 非指定订单前缀则不执行业务逻辑
            if (!outTradeNo.startsWith(outOrderPrefix)) {
                return;
            }
            orderId = outTradeNo.split(outOrderPrefix)[1];
        } else {
            orderId = outTradeNo;
        }

        // 封装回调中的参数
        WechatH5CallbackParameterHolder callbackParameterHolder = new WechatH5CallbackParameterHolder();
        callbackParameterHolder.setOutTradeNo(outTradeNo);
        callbackParameterHolder.setOrderId(orderId);
        callbackParameterHolder.setTransactionId(transactionId);
        callbackParameterHolder.setTotalFee(totalFee);
        callbackParameterHolder.setOpenId(openId);
        callbackParameterHolder.setPayTime(timeEnd);
        callbackParameterHolder.setTradeStatus(resultCode);
        logger.info("微信H5支付回调返回的参数" + JSON.toJSONString(callbackParameterHolder));
        // 执行业务逻辑
        R result = businessFunction.apply(callbackParameterHolder);
        logger.info("微信H5支付回调成功后的业务操作结果--->" + JSON.toJSONString(result, true));
    }

    private static Date parseDate(String dateString) {
        if (dateString == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static boolean checkSign(JSONObject responseData) {
        String sign = responseData.getString("sign");
        Map<String, String> signMap = new TreeMap<>();
        for (Map.Entry<String, Object> entry : responseData.entrySet()) {
            signMap.put(entry.getKey(), entry.getValue().toString());
        }
        String mySign = createSign(signMap, signKey);
        return mySign.equals(sign);
    }

    private static void writeResponse(HttpServletResponse response, String returnCode, String returnMsg) {
        try {
            PrintWriter writer = response.getWriter();
            writer.write(payStatusToXml(returnCode, returnMsg));
            writer.flush();
        } catch (IOException e1) {
            e1.printStackTrace();
            throw new RuntimeException(e1);
        }
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    private static String payStatusToXml(String returnCode, String returnMsg) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        sb.append("<return_code><![CDATA[").append(returnCode).append("]]></return_code>");
        sb.append("<return_msg><![CDATA[").append(returnMsg).append("]]></return_msg>");
        sb.append("</xml>");
        return sb.toString();
    }

    private static Map<String, Object> parseDataFromStream(HttpServletRequest request) {
        // 将解析结果存储在HashMap中
        Map<String, Object> map = new HashMap<>(20);
        BufferedReader bufferReader = null;
        try {
            bufferReader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            StringBuilder dataString = new StringBuilder();
            for (String line; (line = bufferReader.readLine()) != null; ) {
                dataString.append(line).append("\n");
            }
            // 读取输入流
            SAXReader reader = new SAXReader();
            String feature = "http://apache.org/xml/features/disallow-doctype-decl";
            reader.setFeature(feature, true);
            Document document = DocumentHelper.parseText(dataString.toString()
                    .replaceAll("[^\\x20-\\x7e]", "")
                    .replaceAll("&gt;", ">")
                    .replaceAll("&lt;", "<"));
            // 得到xml根元素
            Element root = document.getRootElement();
            // 得到根元素的所有子节点
            @SuppressWarnings("unchecked")
            List<Element> elementList = root.elements();
            // 遍历所有子节点
            for (Element e : elementList) {
                map.put(e.getName(), e.getText());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (bufferReader != null) {
                try {
                    bufferReader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return map;
    }

    /**
     * post请求并得到返回结果
     *
     * @param requestUrl the request url
     * @param output     请求数据
     * @return 响应数据
     */
    @SuppressWarnings("SameParameterValue")
    private static String sendHttpsPostRequest(String requestUrl, String output) {
        HttpsURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(requestUrl);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            if (null != output) {
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(output.getBytes(DEFAULT_CHARSET));
                outputStream.close();
            }
            // 从输入流读取返回内容
            reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), DEFAULT_CHARSET));
            StringBuilder buffer = new StringBuilder();
            for (String str; (str = reader.readLine()) != null; ) {
                buffer.append(str);
            }
            return buffer.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * 随机字符串
     *
     * @return the random string
     */
    private static String randomString() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 创建场景信息
     * {
     * "h5_info":{
     * "type": "Wap",
     * "wap_url": "https://pay.qq.com",
     * "wap_name": "腾讯充值"
     * }
     * }
     *
     * @param wapUrl  WAP网站URL地址
     * @param wapName WAP网站名
     * @return 场景信息
     */
    private static String createSceneInfo(String wapUrl, String wapName) {
        Map<String, String> info = Maps.newHashMapWithExpectedSize(3);
        info.put("type", "Wap");
        info.put("wap_url", wapUrl);
        info.put("wap_name", wapName);
        Map<String, Map<String, String>> sceneInfo = Maps.newHashMapWithExpectedSize(1);
        sceneInfo.put("h5_info", info);
        return JSON.toJSONString(sceneInfo);
    }

    /**
     * 创建签名串
     *
     * @param parameters 待签名数据
     * @param signKey    the sign key
     * @return the sign string
     */
    private static String createSign(Map<String, String> parameters, String signKey) {
        if (!(parameters instanceof TreeMap)) {
            parameters = new TreeMap<>(parameters);
        }
        StringBuilder sb = new StringBuilder();
        Set<Map.Entry<String, String>> es = parameters.entrySet();
        for (Map.Entry<String, String> entry : es) {
            String k = entry.getKey();
            String v = entry.getValue();
            if (StringUtils.isNotEmpty(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k).append("=").append(v).append("&");
            }
        }
        sb.append("key=").append(signKey);
        return MD5Util.MD5Encode(sb.toString(), DEFAULT_CHARSET).toUpperCase();
    }

    /**
     * xml解析工具啊
     *
     * @author Xulg
     * Created in 2019-04-30 17:36
     */
    private static class XmlUtils {
        private final static String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";

        private XmlUtils() {
        }

        /**
         * 序列化XML
         *
         * @param obj the obj
         * @return the xml string
         */
        public static String toXmlString(Object obj) {
            XStream stream = getXStream();
            stream.processAnnotations(obj.getClass());
            return XML_DECLARATION + stream.toXML(obj);
        }

        /**
         * 反序列化XML
         *
         * @param xmlString th xml string
         * @param clazz     th class type
         * @return the object
         */
        public static <T> T fromXml(String xmlString, Class<T> clazz) {
            XStream stream = getXStream();
            stream.processAnnotations(clazz);
            Object obj = stream.fromXML(xmlString);
            try {
                return clazz.cast(obj);
            } catch (ClassCastException e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * 将Map转换为XML格式的字符串
         *
         * @param data Map类型数据
         * @return XML格式的字符串
         */
        @SuppressWarnings("WeakerAccess")
        public static String map2XmlString(Map<String, String> data) {
            if (data == null) {
                return null;
            }
            try {
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                org.w3c.dom.Document document = documentBuilder.newDocument();
                org.w3c.dom.Element root = document.createElement("xml");
                document.appendChild(root);
                for (Map.Entry<String, String> entry : data.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (value == null) {
                        continue;
                    }
                    org.w3c.dom.Element filed = document.createElement(key);
                    filed.appendChild(document.createTextNode(value));
                    root.appendChild(filed);
                }
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer transformer = tf.newTransformer();
                DOMSource source = new DOMSource(document);
                transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                StringWriter writer = new StringWriter();
                StreamResult result = new StreamResult(writer);
                transformer.transform(source, result);
                String output = writer.getBuffer().toString();
                close(writer);
                return output;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        /**
         * XML格式字符串转换为Map
         *
         * @param strXML XML字符串
         * @return XML数据转换后的Map
         */
        @SuppressWarnings("WeakerAccess")
        public static Map<String, Object> xmlString2Map(String strXML) {
            if (strXML == null) {
                return null;
            }
            try {
                Map<String, Object> data = new HashMap<>(16);
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
                org.w3c.dom.Document doc = documentBuilder.parse(stream);
                doc.getDocumentElement().normalize();
                NodeList nodeList = doc.getDocumentElement().getChildNodes();
                for (int index = 0; index < nodeList.getLength(); ++index) {
                    Node node = nodeList.item(index);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                        data.put(element.getNodeName(), element.getTextContent());
                    }
                }
                close(stream);
                return data;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        private static void close(Closeable closeable) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @SuppressWarnings("all")
        private static XStream getXStream() {
            return new XStream();
        }
    }

    private static class Base64Util {
        private static final char S_BASE64CHAR[] = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
                'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
                'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
        private static final byte S_DECODETABLE[];

        static {
            S_DECODETABLE = new byte[128];
            for (int i = 0; i < S_DECODETABLE.length; i++)
                S_DECODETABLE[i] = 127;

            for (int i = 0; i < S_BASE64CHAR.length; i++)
                S_DECODETABLE[S_BASE64CHAR[i]] = (byte) i;

        }

        /**
         * @param ibuf
         * @param obuf
         * @param wp
         * @return
         */
        private static int decode0(char ibuf[], byte obuf[], int wp) {
            int outlen = 3;
            if (ibuf[3] == '=')
                outlen = 2;
            if (ibuf[2] == '=')
                outlen = 1;
            int b0 = S_DECODETABLE[ibuf[0]];
            int b1 = S_DECODETABLE[ibuf[1]];
            int b2 = S_DECODETABLE[ibuf[2]];
            int b3 = S_DECODETABLE[ibuf[3]];
            switch (outlen) {
                case 1: // '\001'
                    obuf[wp] = (byte) (b0 << 2 & 252 | b1 >> 4 & 3);
                    return 1;

                case 2: // '\002'
                    obuf[wp++] = (byte) (b0 << 2 & 252 | b1 >> 4 & 3);
                    obuf[wp] = (byte) (b1 << 4 & 240 | b2 >> 2 & 15);
                    return 2;

                case 3: // '\003'
                    obuf[wp++] = (byte) (b0 << 2 & 252 | b1 >> 4 & 3);
                    obuf[wp++] = (byte) (b1 << 4 & 240 | b2 >> 2 & 15);
                    obuf[wp] = (byte) (b2 << 6 & 192 | b3 & 63);
                    return 3;
            }
            throw new RuntimeException("Internal error");
        }

        /**
         * @param data
         * @param off
         * @param len
         * @return
         */
        public static byte[] decode(char data[], int off, int len) {
            char ibuf[] = new char[4];
            int ibufcount = 0;
            byte obuf[] = new byte[(len / 4) * 3 + 3];
            int obufcount = 0;
            for (int i = off; i < off + len; i++) {
                char ch = data[i];
                if (ch != '=' && (ch >= S_DECODETABLE.length || S_DECODETABLE[ch] == 127))
                    continue;
                ibuf[ibufcount++] = ch;
                if (ibufcount == ibuf.length) {
                    ibufcount = 0;
                    obufcount += decode0(ibuf, obuf, obufcount);
                }
            }

            if (obufcount == obuf.length) {
                return obuf;
            } else {
                byte ret[] = new byte[obufcount];
                System.arraycopy(obuf, 0, ret, 0, obufcount);
                return ret;
            }
        }

        /**
         * @param data
         * @return
         */
        public static byte[] decode(String data) {
            char ibuf[] = new char[4];
            int ibufcount = 0;
            byte obuf[] = new byte[(data.length() / 4) * 3 + 3];
            int obufcount = 0;
            for (int i = 0; i < data.length(); i++) {
                char ch = data.charAt(i);
                if (ch != '=' && (ch >= S_DECODETABLE.length || S_DECODETABLE[ch] == 127))
                    continue;
                ibuf[ibufcount++] = ch;
                if (ibufcount == ibuf.length) {
                    ibufcount = 0;
                    obufcount += decode0(ibuf, obuf, obufcount);
                }
            }

            if (obufcount == obuf.length) {
                return obuf;
            } else {
                byte ret[] = new byte[obufcount];
                System.arraycopy(obuf, 0, ret, 0, obufcount);
                return ret;
            }
        }

        /**
         * @param data
         * @param off
         * @param len
         * @param ostream
         * @throws IOException
         */
        public static void decode(char data[], int off, int len, OutputStream ostream) throws IOException {
            char ibuf[] = new char[4];
            int ibufcount = 0;
            byte obuf[] = new byte[3];
            for (int i = off; i < off + len; i++) {
                char ch = data[i];
                if (ch != '=' && (ch >= S_DECODETABLE.length || S_DECODETABLE[ch] == 127))
                    continue;
                ibuf[ibufcount++] = ch;
                if (ibufcount == ibuf.length) {
                    ibufcount = 0;
                    int obufcount = decode0(ibuf, obuf, 0);
                    ostream.write(obuf, 0, obufcount);
                }
            }

        }

        /**
         * @param data
         * @param ostream
         * @throws IOException
         */
        public static void decode(String data, OutputStream ostream) throws IOException {
            char ibuf[] = new char[4];
            int ibufcount = 0;
            byte obuf[] = new byte[3];
            for (int i = 0; i < data.length(); i++) {
                char ch = data.charAt(i);
                if (ch != '=' && (ch >= S_DECODETABLE.length || S_DECODETABLE[ch] == 127))
                    continue;
                ibuf[ibufcount++] = ch;
                if (ibufcount == ibuf.length) {
                    ibufcount = 0;
                    int obufcount = decode0(ibuf, obuf, 0);
                    ostream.write(obuf, 0, obufcount);
                }
            }

        }

        /**
         * @param data
         * @return
         */
        public static String encode(byte data[]) {
            return encode(data, 0, data.length);
        }

        /**
         * @param data
         * @param off
         * @param len
         * @return
         */
        public static String encode(byte data[], int off, int len) {
            if (len <= 0)
                return "";
            char out[] = new char[(len / 3) * 4 + 4];
            int rindex = off;
            int windex = 0;
            int rest;
            for (rest = len - off; rest >= 3; rest -= 3) {
                int i = ((data[rindex] & 255) << 16) + ((data[rindex + 1] & 255) << 8) + (data[rindex + 2] & 255);
                out[windex++] = S_BASE64CHAR[i >> 18];
                out[windex++] = S_BASE64CHAR[i >> 12 & 63];
                out[windex++] = S_BASE64CHAR[i >> 6 & 63];
                out[windex++] = S_BASE64CHAR[i & 63];
                rindex += 3;
            }

            if (rest == 1) {
                int i = data[rindex] & 255;
                out[windex++] = S_BASE64CHAR[i >> 2];
                out[windex++] = S_BASE64CHAR[i << 4 & 63];
                out[windex++] = '=';
                out[windex++] = '=';
            } else if (rest == 2) {
                int i = ((data[rindex] & 255) << 8) + (data[rindex + 1] & 255);
                out[windex++] = S_BASE64CHAR[i >> 10];
                out[windex++] = S_BASE64CHAR[i >> 4 & 63];
                out[windex++] = S_BASE64CHAR[i << 2 & 63];
                out[windex++] = '=';
            }
            return new String(out, 0, windex);
        }

        /**
         * @param data
         * @param off
         * @param len
         * @param ostream
         * @throws IOException
         */
        public static void encode(byte data[], int off, int len, OutputStream ostream) throws IOException {
            if (len <= 0)
                return;
            byte out[] = new byte[4];
            int rindex = off;
            int rest;
            for (rest = len - off; rest >= 3; rest -= 3) {
                int i = ((data[rindex] & 255) << 16) + ((data[rindex + 1] & 255) << 8) + (data[rindex + 2] & 255);
                out[0] = (byte) S_BASE64CHAR[i >> 18];
                out[1] = (byte) S_BASE64CHAR[i >> 12 & 63];
                out[2] = (byte) S_BASE64CHAR[i >> 6 & 63];
                out[3] = (byte) S_BASE64CHAR[i & 63];
                ostream.write(out, 0, 4);
                rindex += 3;
            }

            if (rest == 1) {
                int i = data[rindex] & 255;
                out[0] = (byte) S_BASE64CHAR[i >> 2];
                out[1] = (byte) S_BASE64CHAR[i << 4 & 63];
                out[2] = 61;
                out[3] = 61;
                ostream.write(out, 0, 4);
            } else if (rest == 2) {
                int i = ((data[rindex] & 255) << 8) + (data[rindex + 1] & 255);
                out[0] = (byte) S_BASE64CHAR[i >> 10];
                out[1] = (byte) S_BASE64CHAR[i >> 4 & 63];
                out[2] = (byte) S_BASE64CHAR[i << 2 & 63];
                out[3] = 61;
                ostream.write(out, 0, 4);
            }
        }

        /**
         * @param data
         * @param off
         * @param len
         * @param writer
         * @throws IOException
         */
        public static void encode(byte data[], int off, int len, Writer writer) throws IOException {
            if (len <= 0)
                return;
            char out[] = new char[4];
            int rindex = off;
            int rest = len - off;
            int output = 0;
            do {
                if (rest < 3)
                    break;
                int i = ((data[rindex] & 255) << 16) + ((data[rindex + 1] & 255) << 8) + (data[rindex + 2] & 255);
                out[0] = S_BASE64CHAR[i >> 18];
                out[1] = S_BASE64CHAR[i >> 12 & 63];
                out[2] = S_BASE64CHAR[i >> 6 & 63];
                out[3] = S_BASE64CHAR[i & 63];
                writer.write(out, 0, 4);
                rindex += 3;
                rest -= 3;
                if ((output += 4) % 76 == 0)
                    writer.write("\n");
            }
            while (true);
            if (rest == 1) {
                int i = data[rindex] & 255;
                out[0] = S_BASE64CHAR[i >> 2];
                out[1] = S_BASE64CHAR[i << 4 & 63];
                out[2] = '=';
                out[3] = '=';
                writer.write(out, 0, 4);
            } else if (rest == 2) {
                int i = ((data[rindex] & 255) << 8) + (data[rindex + 1] & 255);
                out[0] = S_BASE64CHAR[i >> 10];
                out[1] = S_BASE64CHAR[i >> 4 & 63];
                out[2] = S_BASE64CHAR[i << 2 & 63];
                out[3] = '=';
                writer.write(out, 0, 4);
            }
        }

    }

    /**
     * 发起微信H5支付响应结果封装
     */
    @SuppressWarnings({"SpellCheckingInspection", "WeakerAccess"})
    public static class WechatSubmitH5PayResponse implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 发起支付是否成功
         */
        private Boolean success;

        /**
         * 返回状态码(SUCCESS|FAIL)
         * 此字段是通信标识，非交易标识，交易是否成功需要查看trade_state来判断
         */
        private String returnCode;

        /**
         * 返回信息
         * 当return_code为FAIL时返回信息为错误原因
         * 例如: 签名失败,参数格式校验错误
         */
        private String returnMsg;

        /*以下字段在return_code为SUCCESS的时候有返回*/

        /**
         * 公众账号ID
         */
        private String appId;

        /**
         * 商户号
         */
        private String mchId;

        /**
         * 设备号
         */
        private String deviceInfo;

        /**
         * 随机字符串
         */
        private String nonceStr;

        /**
         * 签名
         */
        private String sign;

        /**
         * 业务结果(SUCCESS|FAIL)
         */
        private String resultCode;

        /**
         * 错误代码
         */
        private String errCode;

        /**
         * 错误代码描述
         */
        private String errCodeDes;

        /*以下字段在return_code 和result_code都为SUCCESS的时候有返回*/

        /**
         * 交易类型
         * 调用接口提交的交易类型，取值如下：JSAPI，NATIVE，APP，,H5支付固定传MWEB
         */
        private String tradeType;

        /**
         * 预支付交易会话标识
         * 微信生成的预支付回话标识，用于后续接口调用中使用，该值有效期为2小时,针对H5支付此参数无特殊用途
         */
        private String prepayId;

        /**
         * 支付跳转链接
         * mweb_url为拉起微信支付收银台的中间页面，可通过访问该url来拉起微信客户端，
         * 完成支付,mweb_url的有效期为5分钟
         */
        private String mwebUrl;

        private WechatSubmitH5PayResponse() {
        }

        public Boolean getSuccess() {
            return success;
        }

        public void setSuccess(Boolean success) {
            this.success = success;
        }

        public String getReturnCode() {
            return returnCode;
        }

        public void setReturnCode(String returnCode) {
            this.returnCode = returnCode;
        }

        public String getReturnMsg() {
            return returnMsg;
        }

        public void setReturnMsg(String returnMsg) {
            this.returnMsg = returnMsg;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getMchId() {
            return mchId;
        }

        public void setMchId(String mchId) {
            this.mchId = mchId;
        }

        public String getDeviceInfo() {
            return deviceInfo;
        }

        public void setDeviceInfo(String deviceInfo) {
            this.deviceInfo = deviceInfo;
        }

        public String getNonceStr() {
            return nonceStr;
        }

        public void setNonceStr(String nonceStr) {
            this.nonceStr = nonceStr;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getResultCode() {
            return resultCode;
        }

        public void setResultCode(String resultCode) {
            this.resultCode = resultCode;
        }

        public String getErrCode() {
            return errCode;
        }

        public void setErrCode(String errCode) {
            this.errCode = errCode;
        }

        public String getErrCodeDes() {
            return errCodeDes;
        }

        public void setErrCodeDes(String errCodeDes) {
            this.errCodeDes = errCodeDes;
        }

        public String getTradeType() {
            return tradeType;
        }

        public void setTradeType(String tradeType) {
            this.tradeType = tradeType;
        }

        public String getPrepayId() {
            return prepayId;
        }

        public void setPrepayId(String prepayId) {
            this.prepayId = prepayId;
        }

        public String getMwebUrl() {
            return mwebUrl;
        }

        public void setMwebUrl(String mwebUrl) {
            this.mwebUrl = mwebUrl;
        }
    }
}
