package com.yg.web.controller;

import com.yg.core.QiniuHelper;
import com.yg.core.res.BaseResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class UploadController {
	@RequestMapping({"/api/uploadImg"})
	public BaseResponse<String> editorUpload(HttpServletRequest request, HttpServletResponse response) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		System.out.println("==============upload");
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		String returnPath = "";
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			// 上传文件名
			MultipartFile mf = entity.getValue();
			try {
				returnPath = QiniuHelper.upload(mf.getBytes());
				
				return BaseResponse.success(returnPath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return BaseResponse.fail("err_upload", "图片上传失败");
	}

	@RequestMapping({"/api/uploadImgMore"})
	public BaseResponse<List<String>> uploadImgMore(HttpServletRequest request, HttpServletResponse response) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		
		List<String> list=new ArrayList<String>();
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			// 上传文件名
			MultipartFile mf = entity.getValue();
			try {
				String returnPath = QiniuHelper.upload(mf.getBytes());
				list.add(returnPath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return BaseResponse.success(list);
	}
}
