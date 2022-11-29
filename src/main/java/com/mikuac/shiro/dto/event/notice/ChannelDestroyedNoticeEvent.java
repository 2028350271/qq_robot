package com.mikuac.shiro.dto.event.notice;

import com.alibaba.fastjson2.annotation.JSONField;
import com.mikuac.shiro.dto.action.response.ChannelInfoResp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <p>ChannelDestroyedNoticeEvent class.</p>
 *
 * @author Zero
 * @version $Id: $Id
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class ChannelDestroyedNoticeEvent extends NoticeEvent {

    /**
     * 频道ID
     */
    @JSONField(name = "guild_id")
    private String guildId;

    /**
     * 子频道ID
     */
    @JSONField(name = "channel_id")
    private String channelId;

    /**
     * 操作者ID
     */
    @JSONField(name = "operator_id")
    private String operatorId;

    /**
     * 频道信息
     */
    @JSONField(name = "channel_info")
    private ChannelInfoResp channelInfo;

}
