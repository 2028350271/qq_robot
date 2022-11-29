package com.mikuac.shiro.task;

import com.alibaba.fastjson2.JSONObject;
import com.mikuac.shiro.core.BotContainer;
import com.mikuac.shiro.handler.EventHandler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * 异步任务
 *
 * @author zero
 * @version $Id: $Id
 */
@Component
public class ShiroAsyncTask {

    @Resource
    private BotContainer botContainer;

    /**
     * 事件上报处理函数
     *
     * @param eventHandler {@link com.mikuac.shiro.handler.EventHandler}
     * @param xSelfId      机器人 QQ
     * @param result       上报的 Json 数据
     */
    @Async("shiroTaskExecutor")
    public void execHandlerMsg(EventHandler eventHandler, long xSelfId, JSONObject result) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        eventHandler.handler(botContainer.robots.get(xSelfId), result);
    }

}
