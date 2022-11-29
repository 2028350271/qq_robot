package com.mikuac.shiro.handler;

import com.alibaba.fastjson2.JSON;
import com.mikuac.shiro.core.BotContainer;
import com.mikuac.shiro.core.BotFactory;
import com.mikuac.shiro.core.CoreEvent;
import com.mikuac.shiro.properties.WebSocketProperties;
import com.mikuac.shiro.task.ShiroAsyncTask;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Created on 2021/7/16.
 *
 * @author Zero
 * @version $Id: $Id
 */
@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {

    private final static String API_RESULT_KEY = "echo";

    private static final String FAILED_STATUS = "failed";

    private static final String RESULT_STATUS_KEY = "status";

    private final EventHandler eventHandler;

    private final BotFactory botFactory;

    private final ActionHandler actionHandler;

    private final ShiroAsyncTask shiroAsyncTask;

    private final BotContainer botContainer;

    @Resource
    private WebSocketProperties webSocketProperties;

    @Autowired
    private CoreEvent coreEvent;

    /**
     * 构造函数
     *
     * @param eventHandler   {@link com.mikuac.shiro.handler.EventHandler}
     * @param botFactory     {@link com.mikuac.shiro.core.BotFactory}
     * @param actionHandler  {@link com.mikuac.shiro.handler.ActionHandler}
     * @param shiroAsyncTask {@link com.mikuac.shiro.task.ShiroAsyncTask}
     * @param botContainer   {@link com.mikuac.shiro.core.BotContainer}
     */
    public WebSocketHandler(EventHandler eventHandler, BotFactory botFactory, ActionHandler actionHandler, ShiroAsyncTask shiroAsyncTask, BotContainer botContainer) {
        this.eventHandler = eventHandler;
        this.botFactory = botFactory;
        this.actionHandler = actionHandler;
        this.shiroAsyncTask = shiroAsyncTask;
        this.botContainer = botContainer;
    }

    /**
     * 获取连接的 QQ 号
     *
     * @param session {@link WebSocketSession}
     * @return QQ 号
     */
    private long parseSelfId(WebSocketSession session) {
        String botId = session.getHandshakeHeaders().getFirst("x-self-id");
        if (botId == null || botId.isEmpty()) {
            return 0L;
        }
        try {
            return Long.parseLong(botId);
        } catch (Exception e) {
            return 0L;
        }
    }

    /**
     * 访问密钥检查
     *
     * @param session WebSocketSession
     * @return 是否验证通过
     */
    private boolean checkToken(WebSocketSession session) {
        String token = webSocketProperties.getAccessToken();
        if (token.isEmpty()) {
            return true;
        }
        String clientToken = session.getHandshakeHeaders().getFirst("authorization");
        if (clientToken == null || clientToken.isEmpty()) {
            return false;
        }
        return token.equals(clientToken);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) {
        try {
            long xSelfId = parseSelfId(session);
            if (xSelfId == 0L) {
                log.error("Get client self account failed");
                session.close();
                return;
            }
            if (!checkToken(session)) {
                log.error("Access token invalid");
                session.close();
                return;
            }
            if (!coreEvent.session(session)) {
                session.close();
                return;
            }
            val bot = botFactory.createBot(xSelfId, session);
            botContainer.robots.put(xSelfId, bot);
            log.info("Account {} connected", xSelfId);
            coreEvent.online(bot);
        } catch (IOException e) {
            log.error("Websocket session close exception");
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus status) {
        long xSelfId = parseSelfId(session);
        if (xSelfId == 0L) {
            return;
        }
        if (botContainer.robots.containsKey(xSelfId)) {
            botContainer.robots.remove(xSelfId);
            log.warn("Account {} disconnected", xSelfId);
            coreEvent.offline(xSelfId);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void handleTextMessage(@NotNull WebSocketSession session, @NotNull TextMessage message) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        long xSelfId = parseSelfId(session);
        val result = JSON.parseObject(message.getPayload());
        log.debug("[Event] {}", result.toJSONString());
        // if resp contains echo field, this resp is action resp, else event resp.
        if (result.containsKey(API_RESULT_KEY)) {
            if (FAILED_STATUS.equals(result.get(RESULT_STATUS_KEY))) {
                log.error("Request failed: {}", result.get("wording"));
            }
            actionHandler.onReceiveActionResp(result);
        } else {
            shiroAsyncTask.execHandlerMsg(eventHandler, xSelfId, result);
        }
    }

}
