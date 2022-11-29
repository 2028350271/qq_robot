package com.mikuac.shiro.dto.action.response;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 * <p>EssenceMsgResp class.</p>
 *
 * @author Zero
 * @version $Id: $Id
 */
@Data
public class EssenceMsgResp {

    @JSONField(name = "sender_id")
    private long senderId;

    @JSONField(name = "sender_nick")
    private String senderNick;

    @JSONField(name = "sender_time")
    private long senderTime;

    @JSONField(name = "operator_id")
    private long operatorId;

    @JSONField(name = "operator_nick")
    private String operatorNick;

    @JSONField(name = "operator_time")
    private String operatorTime;

    @JSONField(name = "message_id")
    private int messageId;

}
