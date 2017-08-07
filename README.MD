
![](https://raw.githubusercontent.com/cainsyake/erp/master/src/main/resources/static/images/logo.png)
<br>
MAET-ERP沙盘 基于B/S架构的ERP电子沙盘平台
===
此平台提供多教学班同时在线进行ERP实训，教师可上传自定义的规则、市场使用或从平台规则库及市场库调用所需规则市场。<br>
利用自由的规则，可以开展形式多样的ERP实训。<br>
当前版本（V0.0.1）可支持最多5种产品、5种原料、5个区域市场的规则，足够覆盖大部分的实训需求。
<br><br>
技术栈：Springboot + Mysql + Hibernate + Bootstrap + Jquery
<br>
平台主体架构<br>
平台管理 --> 教学班管理 --> 子用户运营
<br><br>
业务处理逻辑：页面 --> JS处理 --> ajax提交至后台Controller --> 分发至Service -->返回数据至JS --> JS输出数据至页面
<br>

<br><br>
开发状态：<br>
平台管理：已完成常用模块开发<br>
教学班管理：已完成部分初始化模块开发<br>
子用户运营：经营模块大部分业务已编写完成，选单、竞单模块，以及财务报表模块尚在设计中。
<br><br>
由于现在只有一个人在开发，需要同时进行后端和前端的开发，因此对于这份文档还没有很多时间去完善，请谅解。<br>
如果对于这个项目有任何疑问或建议欢迎提[ISSUE](https://github.com/cainsyake/erp/issues/new)进行反馈，或发送邮件至开发者的邮箱：**cainsyake@gmail.com**。<br>
感谢你对本项目的关注与支持，谢谢。
