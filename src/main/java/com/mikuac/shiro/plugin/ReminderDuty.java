package com.mikuac.shiro.plugin;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotContainer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Component
@EnableScheduling
public class ReminderDuty {
    @Resource
    private BotContainer botContainer;

    //qq机器人提醒值日生，6人排6天，周四全体值日
    //@Scheduled(cron = "0/10 * * * * ?")
    //定时任务，每天早上8点提醒值日生，提醒日期为4.1-9.30
    public void sendMsg() {
        // 机器人账号
        long botId = 2904918755L;
        // 通过机器人账号取出 Bot 对象
        Bot bot = botContainer.robots.get(botId);

        List<Long> qqNumberList = new ArrayList<>();
        qqNumberList.add(2726568046L);
        qqNumberList.add(1185361379L);
        qqNumberList.add(192670480L);
        qqNumberList.add(1953408667L);
        qqNumberList.add(2028350271L);
        qqNumberList.add(2682736440L);
        qqNumberList.add(2028350271L);

        List<String> nameList = new ArrayList<>();
        nameList.add("王瑞鹏");
        nameList.add("申国瑞");
        nameList.add("孙智达");
        nameList.add("施鹏升");
        nameList.add("全体");
        nameList.add("王睿智");
        nameList.add("吴复金");

        List<String> weekList = new ArrayList<>();
        weekList.add("星期日");
        weekList.add("星期一");
        weekList.add("星期二");
        weekList.add("星期三");
        weekList.add("星期四");
        weekList.add("星期五");
        weekList.add("星期六");

        //获取当前星期
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        //根据当前星期获取值日生，周四全体值日，其他人轮流值日，发送消息
        if (day != 4) {
            bot.sendGroupMsg(677715127L, MsgUtils.builder().at(qqNumberList.get(day)).text(String.format("今天是周%s，%s记得值日哦~", weekList.get(day), nameList.get(day))).build(), false);
        } else {
            bot.sendGroupMsg(677715127L, MsgUtils.builder().atAll().text("今天是周四，大家记得值日哦~").build(), false);
        }
    }
}
