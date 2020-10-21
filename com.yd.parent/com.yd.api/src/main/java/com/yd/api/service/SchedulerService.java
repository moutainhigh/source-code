package com.yd.api.service;


import java.util.List;
import java.util.Map;

public interface SchedulerService {
	/**
	 * 暂停一个job
	 * 
	 * @param jobClassName
	 * @param jobGroupName
	 */
	public void pause(String jobClassName, String jobGroupName);

	/**
	 * 恢复一个job
	 * 
	 * @param jobName
	 * @param jobGroupName
	 */
	public void resumeJob(String jobName, String jobGroupName);

	/**
	 * 立即执行一个job
	 * 
	 * @param jobName
	 * @param jobGroupName
	 */
	public void runAJobNow(String jobName, String jobGroupName);

	/**
	 * 增加一个job
	 * 
	 * @param jobClass     任务实现类
	 * @param jobName      任务名称
	 * @param jobGroupName 任务组名
	 * @param jobTime      时间表达式 (这是每隔多少秒为一次任务)
	 * @param jobTimes     运行的次数 （<0:表示不限次数）
	 */
	public void addJob(String jobClassName, String jobName, String jobGroupName, int jobTime,
			int jobTimes);

	/**
	 * 增加一个job
	 * 
	 * @param jobClass     任务实现类
	 * @param jobName      任务名称
	 * @param jobGroupName 任务组名
	 * @param jobTime      时间表达式 （如：0/5 * * * * ? ）
	 */
	public void addJob(String jobClassName, String jobName, String jobGroupName, String jobTime);

	/**
	 * 修改 一个job的 时间表达式
	 * 
	 * @param jobName
	 * @param jobGroupName
	 * @param jobTime
	 */
	public void updateJob(String jobName, String jobGroupName, String jobTime);

	/**
	 * 删除任务一个job
	 * 
	 * @param jobName      任务名称
	 * @param jobGroupName 任务组名
	 */
	public void deleteJob(String jobName, String jobGroupName);

	/**
	 * 获取所有计划中的任务列表
	 * @return
	 */
	public List<Map<String, Object>> queryAllJob();
	
	
	/**
	 * 获取所有正在运行的job
	 * @return
	 */
	public List<Map<String, Object>> queryRunJob();
}
