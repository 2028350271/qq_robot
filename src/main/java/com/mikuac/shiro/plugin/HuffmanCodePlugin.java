package com.mikuac.shiro.plugin;

import com.alibaba.fastjson2.JSONObject;
import com.mikuac.shiro.annotation.PrivateMessageHandler;
import com.mikuac.shiro.annotation.Shiro;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.PrivateMessageEvent;
import com.mikuac.shiro.entity.HuffmanCode;
import com.mikuac.shiro.entity.Node;
import com.mikuac.shiro.service.HuffmanCodeService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;

import static java.util.Arrays.sort;

@Shiro
@Component
@Slf4j
public class HuffmanCodePlugin {
    @Autowired
    private HuffmanCodeService huffmanCodeService;

    @PrivateMessageHandler(cmd = "^赫夫曼编码.*")
    public void fun1(@NotNull Bot bot, @NotNull PrivateMessageEvent event) {
        String str = event.getMessage().substring(event.getMessage().indexOf(" ") + 1);
        StringBuilder rawcode = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            rawcode.append(Integer.toBinaryString(c));
        }
        StringBuilder message = new StringBuilder();
        HashMap<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (map.containsKey(c)) {
                map.put(c, map.get(c) + 1);
            } else {
                map.put(c, 1);
            }
        }
        message.append("字符出现次数：").append(map);
        //用hashmap存储每个字符的权重
        Node[] nodes = new Node[map.size()];
        int i = 0;
        for (Character c : map.keySet()) {
            nodes[i] = new Node(c, map.get(c));
            i++;
        }
        sort(nodes);
        for (Node node : nodes) {
            message.append("字符").append((node.c == ' ' ? "' '" : node.c)).append("出现次数为").append(node.weight).append("    ");
        }
        message.append("\n");

        Node root = Node.creatTree(nodes);

        //获取哈夫曼编码对应规则
        HashMap<Character, String> huffmanCode = new HashMap<>();
        root.getHuffmanCode(huffmanCode, "");
        message.append("哈夫曼编码规则：").append(huffmanCode).append("\n");

        //编码
        StringBuilder code = new StringBuilder();
        for (int j = 0; j < str.length(); j++) {
            char c = str.charAt(j);
            code.append(huffmanCode.get(c));
        }
        message.append("编码后的字符串：").append(code).append("\n");

        double compressibility = (double) code.length() / rawcode.length();
        //保留2位小数
        BigDecimal b = new BigDecimal(compressibility);
        compressibility = b.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
        message.append("压缩率:").append(compressibility * 100).append("%");

        bot.sendPrivateMsg(event.getUserId(), message.toString(), false);
        //root转换为json形式，引用类型递归转换
        JSONObject jsonObject1 = treenodeToJson(root);
        //实体类HuffmanCode
        HuffmanCode huffmanCode1 = new HuffmanCode();
        huffmanCode1.setQqnumber(String.valueOf(event.getUserId()));
        huffmanCode1.setJsontree(jsonObject1.toString());
        huffmanCodeService.saveOrUpdate(huffmanCode1);
    }

    public JSONObject treenodeToJson(Node node) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("c", node.c);
        jsonObject.put("weight", node.weight);
        if (node.left != null) {
            jsonObject.put("left", treenodeToJson(node.left));
        }
        if (node.right != null) {
            jsonObject.put("right", treenodeToJson(node.right));
        }
        return jsonObject;
    }

    public Node jsonToTreenode(JSONObject jsonObject) {
        Node node = new Node();
        node.c = (char) jsonObject.get("c").toString().charAt(0);
        node.weight = (int) jsonObject.get("weight");
        if (jsonObject.get("left") != null) {
            node.left = jsonToTreenode((JSONObject) jsonObject.get("left"));
        }
        if (jsonObject.get("right") != null) {
            node.right = jsonToTreenode((JSONObject) jsonObject.get("right"));
        }
        return node;
    }

    @PrivateMessageHandler(cmd = "^赫夫曼解码.*")
    public void fun2(@NotNull Bot bot, @NotNull PrivateMessageEvent event) {
        String str = event.getMessage().substring(event.getMessage().indexOf(" ") + 1);
        //解码
        StringBuilder hfcodeafter = new StringBuilder();
        StringBuilder message = new StringBuilder();
        //对code进行解码
        JSONObject jsonObject = null;
        HuffmanCode huffmanCode = huffmanCodeService.getById(event.getUserId());
        jsonObject = JSONObject.parseObject(huffmanCode.getJsontree());
        Node root = jsonToTreenode(jsonObject);
        Node temp = root;
        for (int j = 0; j < str.length(); j++) {
            char c = str.charAt(j);
            if (c == '0') {
                temp = temp.left;
            } else {
                temp = temp.right;
            }
            if (temp.left == null && temp.right == null) {
                hfcodeafter.append(temp.c);
                temp = root;
            }
        }
        message.append("哈夫曼解码结果:").append(hfcodeafter).append("\n");
        StringBuilder rawcode = new StringBuilder();
        for (int i = 0; i < hfcodeafter.length(); i++) {
            char c = hfcodeafter.charAt(i);
            rawcode.append(Integer.toBinaryString(c));
        }
        double compressibility = (double) str.length() / rawcode.length();
        //保留2位小数
        BigDecimal b = new BigDecimal(compressibility);
        compressibility = b.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
        message.append("压缩率:").append(compressibility * 100 + "%");
        bot.sendPrivateMsg(event.getUserId(), message.toString(), false);
    }
}
