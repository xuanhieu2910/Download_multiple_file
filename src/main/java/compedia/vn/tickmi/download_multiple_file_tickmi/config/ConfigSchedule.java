package compedia.vn.tickmi.download_multiple_file_tickmi.config;

import compedia.vn.tickmi.download_multiple_file_tickmi.utils.DbConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableScheduling
public class ConfigSchedule {



    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(DbConstant.SIZE_POOL_THREAD);
        threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
        return threadPoolTaskScheduler;
    }
//
//    @Bean
//    public void updateWholeTable() {
//        updateWholeEventRequest();
//        updateWholeEventRequestDetail();
//        DbConstant.IS_FLAT_RUN_JOB = true;
//    }



}
