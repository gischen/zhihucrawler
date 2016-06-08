package com.zhihu.quartz;

/**
 * Created by Administrator on 2015/7/20.
 */


import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;

import java.text.ParseException;

public class QuartzManager {

    private static SchedulerFactory sf = new StdSchedulerFactory();
    private static String JOB_GROUP_NAME = "group1";
    private static String TRIGGER_GROUP_NAME = "trigger1";


    /**
     *  ??????????????????????????????????????????????????
     * @param jobName ??????
     * @param job     ????
     * @param time    ?????????¦Ï?quartz??????
     * @throws SchedulerException
     * @throws ParseException
     */
    public static void addJob(String jobName,Job job,String time)
            throws SchedulerException, ParseException{
        Scheduler sched = sf.getScheduler();
        JobDetailImpl jobDetail = new JobDetailImpl();//?????????????ï…?????????
        jobDetail.setName(jobName);
        jobDetail.setGroup(JOB_GROUP_NAME);
        jobDetail.setJobClass(job.getClass());
        //??????
        CronTriggerImpl  trigger =
                new CronTriggerImpl();//????????,????????
        trigger.setName(jobName);
        trigger.setGroup(TRIGGER_GROUP_NAME);

        trigger.setCronExpression(time);//??????????Ú…
        sched.scheduleJob(jobDetail,trigger);
        //????
        if(!sched.isShutdown())
            sched.start();
    }

    /**
     * ?????????????
     * @param jobName ??????
     * @param jobGroupName ????????
     * @param triggerName  ????????
     * @param triggerGroupName ??????????
     * @param job     ????
     * @param time    ?????????¦Ï?quartz??????
     * @throws SchedulerException
     * @throws ParseException
     */
    public static void addJob(String jobName,String jobGroupName,
                              String triggerName,String triggerGroupName,
                              Job job,String time)
            throws SchedulerException, ParseException{
        Scheduler sched = sf.getScheduler();
        JobDetailImpl jobDetail = new JobDetailImpl();
        jobDetail.setName(jobName);
        jobDetail.setGroup(jobGroupName);
        jobDetail.setJobClass(job.getClass());
        //??????
        CronTriggerImpl  trigger =
                new CronTriggerImpl();
        trigger.setName(triggerName);
        trigger.setGroup(triggerGroupName);
        trigger.setCronExpression(time);//??????????Ú…
        sched.scheduleJob(jobDetail,trigger);
        if(!sched.isShutdown())
            sched.start();
    }

    /**
     * ?????????????????(?????????????????????????????????????)
     * @param jobName
     * @param time
     * @throws SchedulerException
     * @throws ParseException
     */
    public static void modifyJobTime(String jobName,String time)
            throws SchedulerException, ParseException{
        Scheduler sched = sf.getScheduler();
        Trigger trigger =  sched.getTrigger(new TriggerKey(jobName,TRIGGER_GROUP_NAME));
        if(trigger != null){
            CronTriggerImpl  ct = (CronTriggerImpl)trigger;
            ct.setCronExpression(time);
            sched.resumeTrigger(new TriggerKey(jobName,TRIGGER_GROUP_NAME));
        }
    }

    /**
     * ?????????????????
     * @param triggerName
     * @param triggerGroupName
     * @param time
     * @throws SchedulerException
     * @throws ParseException
     */
    public static void modifyJobTime(String triggerName,String triggerGroupName,
                                     String time)
            throws SchedulerException, ParseException{
        Scheduler sched = sf.getScheduler();
        Trigger trigger =  sched.getTrigger(new TriggerKey(triggerName,triggerGroupName));
        if(trigger != null){
            CronTriggerImpl  ct = (CronTriggerImpl)trigger;
            //??????
            ct.setCronExpression(time);
            //??????????
            sched.resumeTrigger(new TriggerKey(triggerName,triggerGroupName));
        }
    }

    /**
     * ??????????(?????????????????????????????????????)
     * @param jobName
     * @throws SchedulerException
     */
    public static void removeJob(String jobName)
            throws SchedulerException{
        Scheduler sched = sf.getScheduler();
        sched.pauseTrigger(new TriggerKey(jobName,TRIGGER_GROUP_NAME));//????????
        sched.unscheduleJob(new TriggerKey(jobName,TRIGGER_GROUP_NAME));//?????????
        sched.deleteJob(new JobKey(jobName,JOB_GROUP_NAME));//???????
    }

    /**
     * ??????????
     * @param jobName
     * @param jobGroupName
     * @param triggerName
     * @param triggerGroupName
     * @throws SchedulerException
     */
    public static void removeJob(String jobName,String jobGroupName,
                                 String triggerName,String triggerGroupName)
            throws SchedulerException{
        Scheduler sched = sf.getScheduler();
        sched.pauseTrigger(new TriggerKey(triggerName,triggerGroupName));//????????
        sched.unscheduleJob(new TriggerKey(triggerName,triggerGroupName));//?????????
        sched.deleteJob(new JobKey(jobName,jobGroupName));//???????
    }
}

