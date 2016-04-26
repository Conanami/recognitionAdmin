#androidMonitor

录音系统  接口

1，  安卓手机 从 服务器获取 电话号码，
2，  手机 拨打电话， 拨打结束后，上传服务器 设置 手机号状态  以及 对应 录音文件。
3，  识别线程 从 数据库获取 尚未识别的录音记录。 返回识别结果。


蒲公英 发布安装密码 789789

表结构设计：
reqid  自增长主键
merchid 商户号
batchid	 批次号
mobile 手机号码
zjmobile 主叫号码
dataurl 录音文件存放地址
status  状态 空 或者
		1 表示 尚未领取 ，
		2  表示 已经领取 ，
		3 表示 已经拨打，
		4 表示 已经识别。
		5 表示录音文件丢失,
		9 号码异常
		8 已经写回数据库
		11 拨打重试失败
		12 识别重试 失败

result 分析结果
		默认为空，
		0 表示 未知，
		1 表示 正常，
		2 表示 欠费停机
		3 表示空号，
		4 表示关机
		-1 表示尚未处理,
		-2 表示无声
		8 表示需要重新拨打
		9 表示需要重新识别
		21 表示 讯飞识别失败

manualresult  人工听录音结果


createtime 创建时间
receivetime  领取时间
calltime 拨打时间
recogtime 识别时间
totalcalltime 总呼叫时间
pickuptime 开始领取时间


查看服务器性能
ps aux | sort -r -nk 4 | head

查看数据库状态
show variables like '%max_connections%';
show status like '%connections%';
show processlist;


SELECT  SEC_TO_TIME(a.minutes*60), COUNT(*) from (

SELECT TIME_TO_SEC(recogs.receivetime) DIV 60 AS minutes, mobile FROM recogs WHERE ISNULL(receivetime)=FALSE
) a  GROUP BY a.minutes

电话拨打规则
01开头的手机号码，要去掉0
010 是允许的。 其他01 打头要去掉 0
10 打头的要加 0
2 或者更大 打头的，要 加0


UPDATE recogs SET manualresult=-1 WHERE  `status` = 4 and batchid='up0406164037_5_2'


UPDATE recogs SET `status`=3 , result=NULL, recogtime=NULL WHERE ISNULL(calltime)=FALSE and `status`=4  and batchid='0307163229_15'


UPDATE recogs SET `status`=1 , result = -1 , receivetime = NULL , calltime =NULL , recogtime=NULL , dataurl=NULL


use mysql
grant all privileges on *.* to root@'%' identified by '******';
grant all privileges on *.* to root@'localhost' identified by '******';
flush privileges;



兆能相关

上海市黄浦区北京东路668号科技京城西楼12G

兆能资产

无线网络：
LK
y19891115


数据库连接
192.168.0.18

ZH
ZH


导入数据的时候，将兆能的数据 mtmcontact 表里面的， 导入 zncontact， 里面。 对应字段
batchid， caseno， serino， prelation, pname, ptel, telck, ptel1, tel1ck 。

导入的时候，是将查询结果 逐笔完全导入。

分析 批次导入的实际性能。



债务人 所有电话  有效，  写入 A
			无效
				判断联系人所有电话 只要有一个 019 ，写入B
				联系人全部无效 没有019， 但是有012， 写入C
				连012,019 都没有，就写入 D


1,  查询已经完成 电话号码识别，但是尚未赋值案件状态的 案件。 如果sql麻烦，只查询 已经完成 债务人电话号码识别案件 也行。
2， 对每个案件，查询所有电话号码状态，计算案件状态，写回。  如果有尚未识别的电话号码则跳过。
3， 如果有案件状态写回，则写回 兆能的数据表。

导出一份 兆能的 案件数据表。

mvn install:install-file -Dfile=sqljdbc42.jar -Dpackaging=jar -DgroupId=com.microsoft.sqlserver -DartifactId=sqljdbc4 -Dversion=4.2

1 表示正常
2 表示 欠费停机
		3 表示空号，
		4 表示关机

写回状态：

暂停使用 006
空号 008
关机 012
暂不接听 019

update mtmcontact set TelCk='' where ptel like '%'+1111

update mtmcontact set Tel1Ck='' where ptel1 like '%'+1111




