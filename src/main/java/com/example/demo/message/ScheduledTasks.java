package com.example.demo.message;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author: 杨春生
 * @Date: 2020/7/20 15:28
 * @Description:
 */
@Component
public class ScheduledTasks {

    @Scheduled(cron="* * * 1 * ?")
    public void testScheduled(){
        System.out.println("****************哈哈哈哈哈******");
    }
}
