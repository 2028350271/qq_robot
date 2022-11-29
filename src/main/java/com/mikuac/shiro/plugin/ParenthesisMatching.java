package com.mikuac.shiro.plugin;

import com.mikuac.shiro.annotation.PrivateMessageHandler;
import com.mikuac.shiro.annotation.Shiro;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.PrivateMessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Stack;
import java.util.regex.Matcher;

@Shiro
@Component
@Slf4j
public class ParenthesisMatching {

    @PrivateMessageHandler(cmd = "^括号匹配检验.*")
    public void fun1(@NotNull Bot bot, @NotNull PrivateMessageEvent event, @NotNull Matcher matcher) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        //括号匹配检验程序
        String str = event.getMessage().substring(event.getMessage().indexOf(" ") + 1);
        log.info(str);
        HashMap<Character, Character> map = new HashMap<>();
        map.put('<', '>');
        map.put('(', ')');
        map.put('{', '}');
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (stack.isEmpty()) {
                stack.push(c);
            } else {
                char top = stack.peek();
                if (map.containsKey(top) && c == map.get(top)) {
                    stack.pop();
                } else {
                    stack.push(c);
                }
            }
        }
        if (stack.isEmpty()) {
            bot.sendPrivateMsg(event.getUserId(), "括号匹配成功", false);
        } else {
            bot.sendPrivateMsg(event.getUserId(), "括号匹配失败", false);
        }
    }
}