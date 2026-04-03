package ru.job4j.social.service.scheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        System.out.println("Текущее время: " + new java.util.Date());
    }

    @Scheduled(cron = "0 0 10 * * ?")
    public void performTaskUsingCron() {
        System.out.println("Задача выполняется каждый день в 10 утра");
    }
}
