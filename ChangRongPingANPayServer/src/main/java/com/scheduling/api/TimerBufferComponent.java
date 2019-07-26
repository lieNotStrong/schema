package com.scheduling.api;


import com.scheduling.service.ifac.ExtendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TimerBufferComponent {

    private int i = 0;

    @Autowired
    private ExtendsService extendsService;

    //每1秒执行 爆仓 保证金率小于等于杠杆强制平仓
    @Scheduled(fixedDelay=1000)
    public void findddthree() {

    }
    //每60分钟同步一次3天内的订单状
    @Scheduled(cron = "0 0 */1 * * ?")
    public void findddone() {

    }
//周五的下午16:00:00触发 周五下午4点进行交割
@Scheduled(cron = "0 00 16 ? * FRI")
public void findddoneday() {

}
    //每月25号执行
    @Scheduled(cron = "0 00 1 25 * ? ")
    public void HelpRecom() {

    }



    //@Scheduled(cron = "0/5 * * * * ? ")
    //每天00点执行
    @Scheduled(cron = "0 0 0 * * ? ")
    public void delRelationInfo() {

    }


    //@Scheduled(cron = "0/5 * * * * ? ")
    //每天8点开始
    @Scheduled(cron = "0 0 8 * * ? ")
    public void relationInfo() {

    }

    //@Scheduled(cron = "0/5 * * * * ? ")
    @Scheduled(cron = "0 0 7 * * ? ")
    public void relationstart() {

    }


    //@Scheduled(cron = "0/5 * * * * ? ")
    //每天00点执行
    @Scheduled(cron = "0 58 7,11,19,23 * * ? ")
    public void remindSecKill() {

    }

    //每天00点执行
    //@Scheduled(cron = "0/5 * * * * ? ")
    @Scheduled(cron = "0 0 0 * * ? ")
    public void needCard() {

    }

    //@Scheduled(cron = "0/5 * * * * ? ")
    @Scheduled(cron = "0 0 1 * * ? ")
    public void retreat() {
        try {
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
