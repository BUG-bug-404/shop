package com.keith.config;

import com.keith.modules.service.user.UserMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

@Component
public class Task {
    @Autowired
    private UserMemberService userMemberService;
   // @Scheduled(fixedDelay = 5000)
   @Scheduled(cron="0 15 10 28-31 * ?")
    public void task01() throws Exception{
        System.out.println(Thread.currentThread().getName()+"----------task");
       final Calendar c = Calendar.getInstance();
       if (c.get(Calendar.DATE) == c.getActualMaximum(Calendar.DATE)) {
            //是最后一天
           System.out.println("执行任务");
           userMemberService.forNoP();
       }

//        TimeUnit.SECONDS.sleep(60*60*24*1000);
        TimeUnit.SECONDS.sleep(1000);

    }
}
