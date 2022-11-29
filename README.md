本数据课程设计项目是由Java SpringBoot开发。

## 使用说明

**重要提示：本使用说明用【】表示参数，使用（）表示命令参数范围约束**

**如：天气** **城市** **第【n****】天气（0<=n<7****）**

**用|****分割多个可选参数** 

**如：天气** **【城市】** **【今天|****明天】天气**

​    **请严格按照格式发送命令消息，无法识别错误消息**

（一）天气预报

（1）天气 【城市】 【今天|明天|后天|七天】天气

​    示例：获取聊城今天天气的命令： 天气 聊城 今天天气

（2）天气 【城市】 第【n】天天气**（****0<=n<7****）**

​       示例：获取聊城第0天天气（即今天天气）：天气 聊城 第0天天气

天气时间从今天编号，今天为第0天，依次增加

​    回复：指定天的天气状况、最低温度和最高温度、如果是今天天气则包含当前时间的温度。

（二）教务系统登录和重新登录

​    （1）登录 【学号】 【密码】

​       示例：登录 2021405102 123456789

（2）重新登录

（三）教务系统成绩

（1）  更新获取成绩 【学年】 【学期】

示例：更新获取成绩 2021 1

（2）  更新获取成绩详情 【学年】 【学期】

示例：更新获取成绩详情 2021 1

（3）  获取成绩 【学年】 【学期】

示例：获取成绩 2021 1

（4）  获取成绩详情 【学年】 【学期】

示例：获取成绩详情 2021 1

（一）中国疫情

（1）  疫情 【城市】

示例：疫情 聊城

（二）知乎热榜

（1）  知乎热榜 【热榜排序】

示例：知乎热榜 1

（2）  知乎热榜 【热榜范围】

示例：知乎热榜 1-10

（3）  知乎热榜 随机

（三）微博热搜榜

（1）  微博热搜榜

（四）智能群聊娱乐机器人

说明：使用说明请在机器人所在群聊发送help，获取说明。

（五）括号匹配检验

（1）  括号匹配检验 【需要检验的字符串】

示例：括号匹配检验{}1254（）

支持匹配的括号：英文字符：(){}<>

（六）赫夫曼编码

（1）  赫夫曼编码 【需要编码的内容】

示例：赫夫曼编码 10086

（2）  赫夫曼解码 【需要解码的内容】

示例：赫夫曼解码 1100010111

（十一）定时值日提醒