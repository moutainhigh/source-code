package com.yd.service.impl;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.Service;

import com.yd.api.result.TestResult;
import com.yd.api.service.TestService;
import com.yd.core.utils.BeanUtilExt;
import com.yd.service.bean.Test;
import com.yd.service.dao.TestDao;

@Service(dynamic = true)
public class TestServiceImpl implements TestService {
	@Resource
	private TestDao		testDao;

	@Override
	public TestResult getTest(int id) {
		Test t=testDao.findTestById(id);
		if(t==null) {
			return null;
		}
		TestResult result=new TestResult();
		
		BeanUtilExt.copyProperties(result, t);
		
		return result;
	}
}
