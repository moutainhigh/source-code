package com.yd.web.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yd.api.service.SchedulerService;
import com.yd.core.res.BaseResponse;
import com.yd.web.util.WebUtil;

@RestController
public class JobController extends BaseController{
	@Reference
	private SchedulerService	schedulerService;

	@RequestMapping(value="/job/getAll")
	public BaseResponse<List<Map<String, Object>>> getAll(HttpServletRequest request, HttpServletResponse response) {
		List<Map<String, Object>> list=schedulerService.queryAllJob();
		return  BaseResponse.success(list);
	}
	
	
	@RequestMapping(value="/job/getRunList")
	public BaseResponse<List<Map<String, Object>>> getRunList(HttpServletRequest request, HttpServletResponse response) {
		List<Map<String, Object>> list=schedulerService.queryRunJob();
		return  BaseResponse.success(list);
	}
	
	
	@RequestMapping(value="/job/add")
	public BaseResponse<String> add(HttpServletRequest request, HttpServletResponse response) {
		BaseResponse<String> result=new BaseResponse<String>();
		String jobClass=WebUtil.getParameter(request, "jobClass");
		String jobName=WebUtil.getParameter(request, "jobName");
		String jobGroupName=WebUtil.getParameter(request, "jobGroupName");
		Integer jobTime=getIntAttr(request, "jobTime");
		Integer jobTimes=getIntAttr(request, "jobTimes", -1);
		if(StringUtils.isEmpty(jobClass)) {
			result.setValue("err_jobClass", "jobClass不能为空");
			return result;
		}
		if(StringUtils.isEmpty(jobName)) {
			result.setValue("err_jobName", "jobName不能为空");
			return result;
		}
		if(StringUtils.isEmpty(jobGroupName)) {
			result.setValue("err_jobGroupName", "jobGroupName不能为空");
			return result;
		}
		if(jobTime==null) {
			result.setValue("err_jobTime", "jobTime不能为空");
			return result;
		}
		schedulerService.addJob(jobClass, jobName, jobGroupName, jobTime, jobTimes);
		return  BaseResponse.success(null);
	}

	@RequestMapping(value="/job/resumeJob")
	public BaseResponse<String> resumeJob(HttpServletRequest request, HttpServletResponse response) {
		BaseResponse<String> result=new BaseResponse<String>();
		
		String jobName=WebUtil.getParameter(request, "jobName");
		String jobGroupName=WebUtil.getParameter(request, "jobGroupName");
		if(StringUtils.isEmpty(jobName)) {
			result.setValue("err_jobName", "jobName不能为空");
			return result;
		}
		if(StringUtils.isEmpty(jobGroupName)) {
			result.setValue("err_jobGroupName", "jobGroupName不能为空");
			return result;
		}
		schedulerService.resumeJob(jobName, jobGroupName);
		return  BaseResponse.success(null);
	}
	
	
	
	@RequestMapping(value="/job/deleteJob")
	public BaseResponse<String> deleteJob(HttpServletRequest request, HttpServletResponse response) {
		BaseResponse<String> result=new BaseResponse<String>();
		
		String jobName=WebUtil.getParameter(request, "jobName");
		String jobGroupName=WebUtil.getParameter(request, "jobGroupName");
		if(StringUtils.isEmpty(jobName)) {
			result.setValue("err_jobName", "jobName不能为空");
			return result;
		}
		if(StringUtils.isEmpty(jobGroupName)) {
			result.setValue("err_jobGroupName", "jobGroupName不能为空");
			return result;
		}
		schedulerService.deleteJob(jobName, jobGroupName);
		return  BaseResponse.success(null);
	}
}
