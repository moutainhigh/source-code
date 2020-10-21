package com.yd.core.utils;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信api工具类
 * @author wuyc
 * created 2019/12/11 9:45
 **/
public class WechatUtil {

    private static final Logger logger = LoggerFactory.getLogger(WechatUtil.class);

    /** 用户默认头像url */
    private static final String DEFAULT_USER_IMAGE_URL = "https://c9.51jujibao.com/upload/2019/12/10/201912101025469298";

    /** 微信获取用户信息url */
    private static final String GET_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";

    /** 微信获取accessToken url */
    private static final String GET_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    /** 微信授权认证url */
    private static final String GET_AUTHORIZATION_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";

    /** 获取微信公众号token,生成临时带参二维码使用 */
    private static final String GET_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?appid=%s&secret=%s&grant_type=client_credential";

    /** 获取微信绑定二维码ticket */
    private static final String WX_QRCODE_GET_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=%s";

    public static String getWeixinAuthorizationUrlForPlatform(String autoLoginUrl, String sendURL, String appId, String isBind){
        String redirectUrl = autoLoginUrl + "?isBind=" + isBind + "&sendURL=" + URLEncoder.encode(sendURL);
        String formatUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
        String url = String.format(formatUrl,appId, URLEncoder.encode(redirectUrl));
        logger.info("##redirectURL:" + url);
        return url;
    }

    public static String getWeixinOpenIdUrl(String autoLoginUrl, String sendURL, String appId) {
        String redirectUrl = autoLoginUrl + "?sendURL=" + URLEncoder.encode(sendURL);
        String formatUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
        String url = String.format(formatUrl, appId, URLEncoder.encode(redirectUrl));
        logger.info("======getWeixinOpenIdUrl结果为url=" + url);
        return url;
    }

    /**
     * 获取accessToken授权凭证
     * @param appId     公众账号唯一标识
     * @param appSecret 公众账号密钥
     * @param code      微信返回的code
     */
    public static Map<String,String> getAccessToken(String appId, String appSecret, String code){
        String url = String.format(GET_ACCESS_TOKEN_URL, appId, appSecret, code);
        logger.info("获取token请求url===" +  url);
        String result = HttpRequest.get(url).timeout(20000).execute().body();
        JSONObject jsonObject = JSONObject.parseObject(result);
        logger.info("获取token返回值===" +  JSON.toJSONString(jsonObject));

        String accessToken = jsonObject.getString("access_token");
        String openId = jsonObject.getString("openid");

        Map<String, String> resultMap = new HashMap<>(4);
        resultMap.put("openId", openId);
        resultMap.put("accessToken", accessToken);
        return resultMap;
    }


    /**
     * 拉取用户信息
     * @param accessToken   授权凭证
     * @param openId        用户标识
     */
    public static Map<String, String> getUserInfo(String accessToken, String openId){
        try {
            String url = String.format(GET_USER_INFO_URL, accessToken, openId);
            logger.info("获取用户信息请求url===" +  JSON.toJSONString(url));
            String result = HttpRequest.get(url).timeout(20000).execute().body();
            JSONObject jsonObject = JSONObject.parseObject(result);
            logger.info("获取用户信息返回值===" +  JSON.toJSONString(jsonObject));

            String unionId = jsonObject.getString("unionid");
            String nickname = jsonObject.getString("nickname");
            String imageUrl = jsonObject.getString("headimgurl");
            imageUrl = StringUtil.isEmpty(imageUrl) ? DEFAULT_USER_IMAGE_URL : imageUrl;
            nickname = StringUtils.isEmpty(nickname) ? StringUtil.getRandomString(8, true) : nickname;
            Map<String,String> resultMap = new HashMap<>(6);
            resultMap.put("unionId", unionId);
            resultMap.put("nickname", nickname);
            resultMap.put("headImage", imageUrl);
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getUserOpenId() {
        // 1. 获取code

        return null;
    }


    /**
     * 获取微信公众号token,生成临时带参二维码使用
     * @param appId
     * @param appSecret
     * @return
     */
    public static String getAccessToken(String appId, String appSecret) {
        try {
            String url = String.format(GET_TOKEN_URL, appId, appSecret);
            logger.info("获取token请求url===" +  url);
            String result = HttpRequest.get(url).timeout(20000).execute().body();
            JSONObject jsonObject = JSONObject.parseObject(result);
            logger.info("获取token返回值===" +  JSON.toJSONString(jsonObject));
            System.out.println("获取token返回值===" +  JSON.toJSONString(jsonObject));

            String accessToken = jsonObject.getString("access_token");
            return accessToken;
        } catch (Exception e) {
            e.getStackTrace();
            return null;
        }
    }

    public static String getWxQrcodeTicket(String accessToken, String sceneStr) {
        try {
            String url = String.format(WX_QRCODE_GET_TICKET_URL, accessToken);
            logger.info("获取ticket请求url===" +  url);

            Map<String, Object> sceneInfo = new HashMap<>();
            sceneInfo.put("scene_str", sceneStr);

            Map<String, Object> actionInfo = new HashMap<>();
            actionInfo.put("scene", sceneInfo);

            Map<String, Object> params = new HashMap<>();
            params.put("action_name", "QR_LIMIT_STR_SCENE");
            params.put("action_info", actionInfo);

            System.out.println("获取微信ticket的json入参===" + JSON.toJSONString(params));
            String result = HttpRequest.post(url).body(JSON.toJSONString(params)).timeout(20000).execute().body();;
            JSONObject jsonObject = JSONObject.parseObject(result);
            logger.info("获取ticket返回值===" +  JSON.toJSONString(jsonObject));
            System.out.println("获取ticket返回值===" +  JSON.toJSONString(jsonObject));
            return jsonObject.getString("ticket");
        } catch (Exception e) {
            e.getStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        // getAccessToken("wxa86f8b4f13451660", "QBWvs8XfG145fly9kAqL", "071jRQ2I1dl1R30bck1I1ccU2I1jRQ2s");
        // getAccessToken("wxa86f8b4f13451660", "f0914d8a5df6a0b43786383895e0086e");
        getWxQrcodeTicket("12356","store-10000");
    }

}
