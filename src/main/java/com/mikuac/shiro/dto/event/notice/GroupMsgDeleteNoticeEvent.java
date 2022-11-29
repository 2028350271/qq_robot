package com.mikuac.shiro.dto.event.notice;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Created on 2021/7/8.
 *
 * @author Zero
 * @version $Id: $Id
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class GroupMsgDeleteNoticeEvent extends NoticeEvent {

    @JSONField(name = "group_id")
    private long groupId;

    @JSONField(name = "user_id")
    private long userId;

    @JSONField(name = "operator_id")
    private long operatorId;

    @JSONField(name = "message_id")
    private long msgId;

}