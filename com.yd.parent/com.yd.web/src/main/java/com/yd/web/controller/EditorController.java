package com.yd.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.yd.core.QiniuHelper;
import com.yd.core.utils.PropertiesHelp;
import com.yd.web.util.WebUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Map;

@Controller
public class EditorController {

    @ResponseBody
    @RequestMapping("/editor/image/upload")
    public JSONObject uploadImg(HttpServletRequest request) throws IOException {
        String action = WebUtil.getParameter(request, "action");
        if ("config".equals(action)) {
            String json = FileUtils.readFileToString(new File(PropertiesHelp.getProperty("ueditorJsonPath", "/home/www/web-deploy/deploy/vip.guijitech.com/properties/ueditorConfig.json")));
            return JSONObject.parseObject(json);
        } else if ("uploadimage".equals(action)) {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
            String returnPath = "";
            for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
                // 上传文件名
                MultipartFile mf = entity.getValue();
                try {
                    returnPath = QiniuHelper.upload(mf.getBytes());
                    JSONObject json = new JSONObject();
                    json.put("state", "SUCCESS");
                    json.put("url", returnPath);
                    return json;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

//    @RequestMapping(value = "/picture/generateQrCode", method = {RequestMethod.GET, RequestMethod.POST})
//    @ResponseBody
//    public void generateQrCode(@NotEmpty(message = "二维码内容不能为空") String content,
//                               @RequestParam(name = "width", defaultValue = "300") Integer width,
//                               @RequestParam(name = "height", defaultValue = "200") Integer height,
//                               HttpServletResponse response) throws IOException, BusinessException {
//        ValidateBusinessExceptions.assertTrue(width > 0 && height > 0,
//                "err_qr_code", "转化二维码参数异常");
//        // png格式的二维码图片
//        response.getOutputStream().write(QrCodeUtil.generatePng(content, width, height));
//    }
}