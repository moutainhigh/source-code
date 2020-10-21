package com.yd.api.pay.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.thoughtworks.xstream.XStream;
import com.yd.core.utils.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 微信付款码支付工具类
 */
public class WechatPaymentCodePayUtils {
    private static final Logger logger = LoggerFactory.getLogger(WechatPaymentCodePayUtils.class);

    private static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * 下单接口地址
     */
    private static final String PAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    /**
     * 交易类型(付款码支付为 NATIVE )
     */
    private static final String TRADE_TYPE_NATIVE = "NATIVE";

    /**
     * 交易类型(H5打开页面支付为 MWEB )
     */
    private static final String TRADE_TYPE_WEB = "MWEB";

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
    public static WechatSubmitH5PayResponse submitPaymentCodeToPay(String outOrderId, int totalFee, String goodsDescription, String clientIp,
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
        requestParameters.put("trade_type", TRADE_TYPE_NATIVE);
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
        logger.info("微信H5支付返回数据json字符串====>>>" + JSON.toJSONString(result));
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

            // trade_type=NATIVE时有返回，此url用于生成支付二维码，然后提供给用户进行扫码支付。
            String codeUrl = result.getString("code_url");
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
            if (tradeType.equalsIgnoreCase("NATIVE")) {
                response.setCodeUrl(codeUrl);
            }
            System.out.println(JSON.toJSONString("====微信H5支付response=") + JSON.toJSONString(response));
        } else if (SUCCESS.equalsIgnoreCase(returnCode)) {
            // 发起支付失败
            response.setSuccess(false);
            response.setAppId(result.getString("appid"));
            response.setMchId(result.getString("mch_id"));
            response.setDeviceInfo(result.getString("device_info"));
            response.setNonceStr(result.getString("nonce_str"));
            response.setErrCode(result.getString("err_code"));
            response.setSign(result.getString("sign"));
            response.setErrCodeDes(result.getString("err_code_des"));
        } else {
            // 请求失败
            response.setSuccess(false);
        }
        return response;
    }


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
    public static Map<String, String> wechatH5ToPay(String outOrderId, int totalFee, String goodsDescription, String clientIp,
                                                    String notifyUrl, String sceneWapUrl, String sceneWapName, String macId,
                                                    String appId, String signKey) throws BusinessException{
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
        requestParameters.put("trade_type", TRADE_TYPE_WEB);
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
        logger.info("微信H5支付返回数据json字符串====>>>" + JSON.toJSONString(result));
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
            String prepayId = result.getString("prepay_id");
            // trade_type=NATIVE时有返回，此url用于生成支付二维码，然后提供给用户进行扫码支付。
            String codeUrl = result.getString("code_url");

            // 支付跳转链接(mweb_url为拉起微信支付收银台的中间页面，
            String mwebUrl = result.getString("mweb_url");

            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("appId", appId);
            resultMap.put("timeStamp", new Date().getTime() / 1000 + "");
            resultMap.put("nonceStr", RandomHelper.getNonceStr());
            resultMap.put("package", "prepay_id=" + prepayId);
            resultMap.put("signType", "MD5");
            resultMap.put("mwebUrl", mwebUrl);
            String paySign = createSign(requestParameters, signKey);
            resultMap.put("paySign", paySign);
            if (tradeType.equalsIgnoreCase("NATIVE")) {
                resultMap.put("codeUrl", codeUrl);
            }
            resultMap.put("returnUrl", notifyUrl);
            System.out.println(JSON.toJSONString("====微信H5支付resultMap=") + JSON.toJSONString(response));
            return resultMap;
        } else {
            logger.error("====H5统一下单失败outOrderId=" + outOrderId + ", 返回值为=" + JSONObject.toJSONString(response));
            throw new BusinessException("err_order_fail", "统一下单失败");
        }
    }


    /* ********************************private method******************************* */

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
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setDoInput(true);
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
        info.put("wap_name", wapName);
        info.put("wap_url", wapUrl);
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
            String v = entry.getValue();
            String k = entry.getKey();
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
                    String value = entry.getValue();
                    String key = entry.getKey();
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
            int b3 = S_DECODETABLE[ibuf[3]];
            int b2 = S_DECODETABLE[ibuf[2]];
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
         * trade_type=NATIVE时有返回，此url用于生成支付二维码，然后提供给用户进行扫码支付。
         */
        private String codeUrl;

        /** 打开微信支付的url */
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

        public String getCodeUrl() {
            return codeUrl;
        }

        public void setCodeUrl(String codeUrl) {
            this.codeUrl = codeUrl;
        }
    }
}
