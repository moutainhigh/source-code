package com.yd.core.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.security.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

/**
 * 微信实时转账工具
 *
 * @author Xulg
 * Created in 2018-11-07 16:12
 */
public class WeiXinTransferUtils {
    private static final Logger logger = LoggerFactory.getLogger(WeiXinTransferUtils.class);

    /**
     * 微信转账接口
     */
    private static final String TRANSFER_INTERFACE_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";

    /**
     * 商户账号appid
     */
    private static final String APP_ID = "wxa86f8b4f13451660";

    /**
     * 微信支付分配的商户号
     */
    private static final String MCH_ID = "1502794471";

    /**
     * 微信支付分配的终端设备号
     */
    private static final String DEVICE_INFO = "";

    /**
     * 商户平台设置的密钥key
     */
    private static final String KEY = "BsQqimhyZW7rAllvv6kK30GpSMExTXG6";

    /**
     * 微信转账
     *
     * @param outOrderId openid
     * @param openid     微信账号openid
     * @param realName   收款方姓名
     * @param money      转账金额(单位:元)
     * @param remark     备注
     * @param ip         ip地址
     * @return the result
     * @throws BusinessException e
     */
    public static WeiXinTransferResponse weiXinTransfer(String outOrderId, String openid, String realName,
                                                        Double money, String remark, String ip) throws BusinessException {
        if (StringUtils.isBlank(outOrderId)) {
            throw new BusinessException("err_out_order_id", "外部订单号不能为空");
        }
        if (StringUtils.isBlank(openid)) {
            throw new BusinessException("err_open_id", "微信openid不能为空");
        }
        if (StringUtils.isBlank(realName)) {
            throw new BusinessException("err_real_name", "姓名不能为空");
        }
        if (money == null || money <= 0) {
            throw new BusinessException("err_money", "金额不能为空");
        }
        if (StringUtils.isBlank(remark)) {
            throw new BusinessException("err_remark", "备注信息不能为空");
        }
        Map<String, String> requestParam = buildRequestParam(outOrderId, openid, realName, money, remark, ip);
        try {
            String xmlString = mapToXml(requestParam);
            //String transferResult = HttPClientUtil.doXmlPost(TRANSFER_INTERFACE_URL, xmlString);
            String transferResult = postRequest(xmlString, "youdu");
            Map<String, String> result = xmlToMap(transferResult);
            WeiXinTransferResponse response = new WeiXinTransferResponse();
            response.setReturn_code(result.get("return_code"));
            response.setReturn_msg(result.get("return_msg"));
            response.setMch_appid(result.get("mch_appid"));
            response.setMchid(result.get("mchid"));
            response.setDevice_info(result.get("device_info"));
            response.setNonce_str(result.get("nonce_str"));
            response.setResult_code(result.get("result_code"));
            response.setErr_code(result.get("err_code"));
            response.setErr_code_des(result.get("err_code_des"));
            response.setPartner_trade_no(result.get("partner_trade_no"));
            response.setPayment_no(result.get("payment_no"));
            response.setPayment_time(result.get("payment_time"));
            response.setResult(transferResult);
            if (!response.isSuccess()) {
                String returnMsg = response.getReturn_msg();
                String errCode = response.getErr_code();
                String errCodeDes = response.getErr_code_des();
                logger.error("=====>>>>>微信转账失败了，请求参数如下：{}=====返回信息：{}",
                        JSON.toJSONString(requestParam), JSON.toJSONString(result));
                throw new BusinessException("err_weixin_transfer", "微信转账失败，错误信息: " + errCodeDes);
            }
            return response;
        } catch (Exception e) {
            System.out.println("=====>>>>>微信转账发生错误，请求参数如下：{}======异常信息如下：{}"
                     + JSON.toJSONString(requestParam) + JSON.toJSONString(e));
            logger.error("=====>>>>>微信转账发生错误，请求参数如下：{}======异常信息如下：{}",
                    JSON.toJSONString(requestParam), JSON.toJSONString(e));
            if (e instanceof BusinessException) {
                throw new BusinessException(((BusinessException) e).getCode(), e.getMessage());
            } else {
                throw new BusinessException("err_weixin_transfer", "微信转账失败");
            }
        }
    }

    /**
     * @param outOrderId openid
     * @param openid     微信账号openid
     * @param realName   收款方姓名
     * @param money      转账金额(单位:元)
     * @param remark     备注
     * @return the request param
     */
    @SuppressWarnings("all")
    private static Map<String, String> buildRequestParam(String outOrderId, String openid,
                                                         String realName, Double money, String remark, String ip) {
        Map<String, String> param = new TreeMap<>();
        param.put("mch_appid", APP_ID);
        param.put("mchid", MCH_ID);
        //param.put("device_info", DEVICE_INFO);
        String randomString = randomString();
        System.out.println("========" + randomString);
        param.put("nonce_str", randomString);
        param.put("openid", openid);
        param.put("partner_trade_no", outOrderId);
        param.put("desc", remark);
        param.put("re_user_name", realName);
        // 设置强制校验姓名
        param.put("check_name", "FORCE_CHECK");
        param.put("spbill_create_ip", ip);
        param.put("amount", String.valueOf(Double.valueOf(MathUtils.multiply(money, 100, 0)).intValue()));
        // 设置签名(最后设置)
        param.put("sign", sign(param));
        return param;
    }

    /**
     * 参数MD5加密
     *
     * @param data 待处理数据
     * @return MD5结果
     */
    @SuppressWarnings("all")
    public static String MD5(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(data.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString().toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 参数HMACSHA256加密
     *
     * @param data 待处理数据
     * @param key  密钥
     * @return 加密结果
     * @throws Exception e
     */
    @SuppressWarnings("all")
    public static String HMACSHA256(String data, String key) throws Exception {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString().toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * XML格式字符串转换为Map
     *
     * @param strXML XML字符串
     * @return XML数据转换后的Map
     * @throws Exception e
     */
    @SuppressWarnings("all")
    public static Map<String, String> xmlToMap(String strXML) throws Exception {
        try {
            Map<String, String> data = new HashMap<>();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
            org.w3c.dom.Document doc = documentBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int idx = 0; idx < nodeList.getLength(); ++idx) {
                Node node = nodeList.item(idx);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                    data.put(element.getNodeName(), element.getTextContent());
                }
            }
            close(stream);
            return data;
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * 将Map转换为XML格式的字符串
     *
     * @param data Map类型数据
     * @return XML格式的字符串
     * @throws Exception e
     */
    @SuppressWarnings("all")
    public static String mapToXml(Map<String, String> data) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        org.w3c.dom.Document document = documentBuilder.newDocument();
        org.w3c.dom.Element root = document.createElement("xml");
        document.appendChild(root);
        for (String key : data.keySet()) {
            String value = data.get(key);
            if (value == null) {
                value = "";
            }
            value = value.trim();
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
        //.replaceAll("\n|\r", "");
        close(writer);
        return output;
    }

    /**
     * 对请求参数进行MD5签名
     *
     * @param request the request
     * @return the sign string
     * @throws Exception e
     */
    @SuppressWarnings("all")
    private static String sign(WeiXinTransferRequest request) throws Exception {
        Map<String, Object> param = new TreeMap<>();
        BeanInfo beanInfo = Introspector.getBeanInfo(request.getClass(), Object.class);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            String name = propertyDescriptor.getName();
            Object value = propertyDescriptor.getReadMethod().invoke(request);
            if (value != null && StringUtils.isNotBlank(value.toString())) {
                param.put(name, value.toString().trim());
            }
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if ("sign".equals(key)) {
                continue;
            }
            sb.append(key).append("=")
                    .append(value).append("&");
        }
        // 拼接秘钥key
        sb.append("key").append("=").append(KEY);
        return MD5(sb.toString()).toUpperCase();
    }

    /**
     * 对请求参数进行MD5签名
     *
     * @param param the param
     * @return the sign string
     */
    private static String sign(Map<String, String> param) {
        if (!(param instanceof TreeMap)) {
            param = (param == null) ? new TreeMap<String, String>() : new TreeMap<>(param);
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : param.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if ("sign".equals(key)) {
                continue;
            }
            if (value != null) {
                sb.append(key).append("=")
                        .append(value.toString().trim()).append("&");
            }
        }
        // 拼接秘钥key
        sb.append("key").append("=").append(KEY);
        return MD5(sb.toString()).toUpperCase();
    }

    private static String randomString() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 32);
    }

    private static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * 发送请求
     *
     * @param requestXML      请求参数xml字符串
     * @param accessTokenType 商户证书名称
     * @return the result string
     */
    @SuppressWarnings("all")
    public static String postRequest(String requestXML, String accessTokenType) throws NoSuchAlgorithmException, IOException,
            KeyManagementException, UnrecoverableKeyException, KeyStoreException {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        String mchId = MCH_ID;
        FileInputStream inputStream = null;
        synchronized (WeiXinTransferUtils.class) {
            try {
                // 微信证书所在目录
                inputStream = new FileInputStream(new File("/home/www/web-deploy/conf/cert/apiclient_" + accessTokenType + "_cert.p12"));
                keyStore.load(inputStream, mchId.toCharArray());
            } catch (Exception e) {
                logger.error("加载证书异常：" + JSON.toJSONString(e));
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        }
        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, mchId.toCharArray()).build();
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"},
                null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();
        String result = "";
        try {
            HttpPost httpPost = new HttpPost(TRANSFER_INTERFACE_URL);
            StringEntity reqEntity = new StringEntity(requestXML, "utf-8"); //如果此处编码不对，可能导致客户端签名跟微信的签名不一致
            reqEntity.setContentType("application/x-www-form-urlencoded");
            httpPost.setEntity(reqEntity);
            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
                    String text;
                    while ((text = bufferedReader.readLine()) != null) {
                        result += text;
                    }
                }
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
        return result;
    }

    /**
     * 微信转账请求参数封装类
     */
    @SuppressWarnings("all")
    private static class WeiXinTransferRequest implements Serializable {
        private static final long serialVersionUID = -1L;

        /**
         * 申请商户号的appid或商户号绑定的appid
         */
        private String mch_appid;

        /**
         * 微信支付分配的商户号
         */
        private String mchid;

        /**
         * 微信支付分配的终端设备号
         */
        private String device_info;

        /**
         * 随机字符串，不长于32位
         */
        private String nonce_str;

        /**
         * 签名
         */
        private String sign;

        /**
         * 商户订单号，需保持唯一性
         * (只能是字母或者数字，不能包含有其他字符)
         */
        private String partner_trade_no;

        /**
         * 商户appid下，某用户的openid
         */
        private String openid;

        /**
         * NO_CHECK：不校验真实姓名
         * FORCE_CHECK：强校验真实姓名
         */
        private String check_name;

        /**
         * 收款用户真实姓名。
         * 如果check_name设置为FORCE_CHECK，则必填用户真实姓名
         */
        private String re_user_name;

        /**
         * 企业付款金额，单位为分
         */
        private String amount;

        /**
         * 企业付款备注，必填
         */
        private String desc;

        /**
         * 该IP同在商户平台设置的IP白名单中的IP没有关联，该IP可传用户端或者服务端的IP
         */
        private String spbill_create_ip;

        public String getMch_appid() {
            return mch_appid;
        }

        public void setMch_appid(String mch_appid) {
            this.mch_appid = mch_appid;
        }

        public String getMchid() {
            return mchid;
        }

        public void setMchid(String mchid) {
            this.mchid = mchid;
        }

        public String getDevice_info() {
            return device_info;
        }

        public void setDevice_info(String device_info) {
            this.device_info = device_info;
        }

        public String getNonce_str() {
            return nonce_str;
        }

        public void setNonce_str(String nonce_str) {
            this.nonce_str = nonce_str;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getPartner_trade_no() {
            return partner_trade_no;
        }

        public void setPartner_trade_no(String partner_trade_no) {
            this.partner_trade_no = partner_trade_no;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getCheck_name() {
            return check_name;
        }

        public void setCheck_name(String check_name) {
            this.check_name = check_name;
        }

        public String getRe_user_name() {
            return re_user_name;
        }

        public void setRe_user_name(String re_user_name) {
            this.re_user_name = re_user_name;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getSpbill_create_ip() {
            return spbill_create_ip;
        }

        public void setSpbill_create_ip(String spbill_create_ip) {
            this.spbill_create_ip = spbill_create_ip;
        }
    }

    /**
     * 微信转账结果封装类
     */
    @SuppressWarnings("all")
    public static class WeiXinTransferResponse implements Serializable {
        private static final long serialVersionUID = -1L;

        private WeiXinTransferResponse() {
        }

        /**
         * SUCCESS/FAIL
         * 此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
         */
        private String return_code;

        /**
         * 返回信息，如非空，为错误原因
         * 签名失败
         * 参数格式校验错误
         */
        private String return_msg;

        /*以下字段在return_code=SUCCESS的时候有返回*/

        /**
         * 申请商户号的appid或商户号绑定的appid（企业号corpid即为此appId）
         */
        private String mch_appid;

        /**
         * 微信支付分配的商户号
         */
        private String mchid;

        /**
         * 微信支付分配的终端设备号
         */
        private String device_info;

        /**
         * 随机字符串，不长于32位
         */
        private String nonce_str;

        /**
         * 业务结果: SUCCESS/FAIL
         * 注意：当状态为FAIL时，存在业务结果未明确的情况，
         * 所以如果状态FAIL，请务必再请求一次查询接口[请务
         * 必关注错误代码（err_code字段），通过查询查询接
         * 口确认此次付款的结果。]，以确认此次付款的结果。
         */
        private String result_code;

        /**
         * 错误码信息，注意：出现未明确的错误码时（SYSTEMERROR等）
         * [出现系统错误的错误码时（SYSTEMERROR），请务必用原商户
         * 订单号重试，或通过查询接口确认此次付款的结果。]，请请
         * 务必再请求一次查询接口，以确认此次付款的结果。
         */
        private String err_code;

        /**
         * 错误代码描述
         * 结果信息描述
         */
        private String err_code_des;

        /*以下字段在return_code 和result_code都为SUCCESS的时候有返回*/

        /**
         * 商户订单号，需保持历史全局唯一性(只能是字母或者数字，不能包含有其他字符)
         */
        private String partner_trade_no;

        /**
         * 企业付款成功，返回的微信付款单号
         */
        private String payment_no;

        /**
         * 企业付款成功时间
         */
        private String payment_time;

        /**
         * 微信转账返回结果字符串
         */
        private String result;

        public String getReturn_code() {
            return return_code;
        }

        public void setReturn_code(String return_code) {
            this.return_code = return_code;
        }

        public String getReturn_msg() {
            return return_msg;
        }

        public void setReturn_msg(String return_msg) {
            this.return_msg = return_msg;
        }

        public String getMch_appid() {
            return mch_appid;
        }

        public void setMch_appid(String mch_appid) {
            this.mch_appid = mch_appid;
        }

        public String getMchid() {
            return mchid;
        }

        public void setMchid(String mchid) {
            this.mchid = mchid;
        }

        public String getDevice_info() {
            return device_info;
        }

        public void setDevice_info(String device_info) {
            this.device_info = device_info;
        }

        public String getNonce_str() {
            return nonce_str;
        }

        public void setNonce_str(String nonce_str) {
            this.nonce_str = nonce_str;
        }

        public String getResult_code() {
            return result_code;
        }

        public void setResult_code(String result_code) {
            this.result_code = result_code;
        }

        public String getErr_code() {
            return err_code;
        }

        public void setErr_code(String err_code) {
            this.err_code = err_code;
        }

        public String getErr_code_des() {
            return err_code_des;
        }

        public void setErr_code_des(String err_code_des) {
            this.err_code_des = err_code_des;
        }

        public String getPartner_trade_no() {
            return partner_trade_no;
        }

        public void setPartner_trade_no(String partner_trade_no) {
            this.partner_trade_no = partner_trade_no;
        }

        public String getPayment_no() {
            return payment_no;
        }

        public void setPayment_no(String payment_no) {
            this.payment_no = payment_no;
        }

        public String getPayment_time() {
            return payment_time;
        }

        public void setPayment_time(String payment_time) {
            this.payment_time = payment_time;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        /**
         * 操作是否成功
         */
        public boolean isSuccess() {
            return "SUCCESS".equals(result_code) && "SUCCESS".equals(result_code);
        }
    }

    /**
     * 加密方式
     */
    @SuppressWarnings("all")
    public enum SignType {
        MD5, HMACSHA256
    }

    public static void main(String[] args) throws BusinessException {
        WeiXinTransferResponse response = weiXinTransfer("11111111111", "ovmKSs1a9Zsr3vBtgCgzPa0h524M", "牛帆", 0.01, "测试", "39.108.99.224");
        System.out.println("=====" + JSON.toJSONString(response, true));
    }

}
