package com.yd.service.job.config;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.Service;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.DateBuilder;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.yd.api.service.SchedulerService;


@Service(dynamic = true)
public class SchedulerServiceImpl implements SchedulerService {
	private final static Logger LOG = LoggerFactory.getLogger(SchedulerServiceImpl.class);
	
	@Resource
	private Scheduler scheduler;

	@Override
	public void pause(String jobName, String jobGroupName) {
		try {
			scheduler.pauseJob(JobKey.jobKey(jobName, jobGroupName));
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void resumeJob(String jobName, String jobGroupName) {
		try {
			JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
			scheduler.resumeJob(jobKey);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void runAJobNow(String jobName, String jobGroupName) {
		try {
			JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
			scheduler.triggerJob(jobKey);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addJob(String jobClassName, String jobName, String jobGroupName, int jobTime,
			int jobTimes) {
		try {
			Class jobClass=Class.forName(jobClassName);
			if(QuartzJobBean.class.isAssignableFrom(jobClass)) {
				JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName)// 任务名称和组构成任务key
						.build();
				// 使用simpleTrigger规则
				Trigger trigger = null;
				if (jobTimes < 0) {
					trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroupName)
							.withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(1).withIntervalInSeconds(jobTime))
							.startNow().build();
				} else {
					trigger = TriggerBuilder
							.newTrigger().withIdentity(jobName, jobGroupName).withSchedule(SimpleScheduleBuilder
									.repeatSecondlyForever(1).withIntervalInSeconds(jobTime).withRepeatCount(jobTimes))
							.startNow().build();
				}
				scheduler.scheduleJob(jobDetail, trigger);
				scheduler.start();
			}else {
				LOG.error("====jobClassName 错误");
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addJob(String jobClassName, String jobName, String jobGroupName, String jobTime) {
		try {
			Class jobClass=Class.forName(jobClassName);
			if(QuartzJobBean.class.isAssignableFrom(jobClass)) {
				// 创建jobDetail实例，绑定Job实现类
				// 指明job的名称，所在组的名称，以及绑定job类
				JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName)// 任务名称和组构成任务key
						.build();
				// 定义调度触发规则
				// 使用cornTrigger规则
				Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroupName)// 触发器key
						.startAt(DateBuilder.futureDate(1, IntervalUnit.SECOND))
						.withSchedule(CronScheduleBuilder.cronSchedule(jobTime)).startNow().build();
				// 把作业和触发器注册到任务调度中
				scheduler.scheduleJob(jobDetail, trigger);
			}else {
				LOG.error("====jobClassName 错误");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateJob(String jobName, String jobGroupName, String jobTime) {
		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
					.withSchedule(CronScheduleBuilder.cronSchedule(jobTime)).build();
			// 重启触发器
			scheduler.rescheduleJob(triggerKey, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteJob(String jobName, String jobGroupName) {
		try {
			scheduler.deleteJob(new JobKey(jobName, jobGroupName));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<Map<String, Object>> queryAllJob() {
		List<Map<String, Object>> jobList = null;
		try {
			GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
			Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
			jobList = new ArrayList<Map<String, Object>>();
			for (JobKey jobKey : jobKeys) {
				List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
				for (Trigger trigger : triggers) {
					Map<String, Object> map = new HashMap<>();
					map.put("jobName", jobKey.getName());
					map.put("jobGroupName", jobKey.getGroup());
					map.put("description", "触发器:" + trigger.getKey());
					Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
					map.put("jobStatus", triggerState.name());
					if (trigger instanceof CronTrigger) {
						CronTrigger cronTrigger = (CronTrigger) trigger;
						String cronExpression = cronTrigger.getCronExpression();
						map.put("jobTime", cronExpression);
					}
					jobList.add(map);
				}
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return jobList;
	}

	@Override
	public List<Map<String, Object>> queryRunJob() {
		List<Map<String, Object>> jobList = null;
		try {
			List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
			jobList = new ArrayList<Map<String, Object>>(executingJobs.size());
			for (JobExecutionContext executingJob : executingJobs) {
				Map<String, Object> map = new HashMap<String, Object>();
				JobDetail jobDetail = executingJob.getJobDetail();
				JobKey jobKey = jobDetail.getKey();
				Trigger trigger = executingJob.getTrigger();
				map.put("jobName", jobKey.getName());
				map.put("jobGroupName", jobKey.getGroup());
				map.put("description", "触发器:" + trigger.getKey());
				Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
				map.put("jobStatus", triggerState.name());
				if (trigger instanceof CronTrigger) {
					CronTrigger cronTrigger = (CronTrigger) trigger;
					String cronExpression = cronTrigger.getCronExpression();
					map.put("jobTime", cronExpression);
				}
				jobList.add(map);
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return jobList;
	}

}
