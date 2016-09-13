# seckill
在线秒杀系统（及相关优化）
一、秒杀系统 技术：
（1）前端的交互设计过程
      1、获取标准的系统时间
      2、判断秒杀是否结束
      3.1、若结束，提示结束
      3.2、如未开始，倒计时面板，执行完 3.3
      4、秒杀地址暴露
      5、点击执行秒杀 ，返回结果
（2）Restful接口设计：
1、站在用户的角度设计service(方法的粒度、参数、返回值)
2、列表页
   详情页
   系统时间
   秒杀地址暴露
   执行秒杀
（3）SpringMVC配置使用技巧：约定大于配置
   1、DTO 在service和Web层传递，jsp和json解析方便。
   2、注解映射驱动、
   3、运行流程
    request ----> DispatcherServlet --》DefaultAnnotation handlermapping
                                    --》DefaultAnnotation handlerAdapter
                                            ---->contrioller
                                            ---->HandlerInterceptor链
                                    --》ModelAndView
                                           ----》ViewResolver ---->view
                                    -->(model + view)response
    response <----
（4）Bootstrap，jquery插件(cookie、countdown)：CDN缓存

二、高并发优化：
CDN：加速用户获取数据：可以是静态数据、视频动态数据
    一般部署到最近的网络节点
    命中CDN后就不需要访问后端服务器
    互联网自身搭建或租用移动、网通

（1）系统时间：java 访问一次内存10 ns，所有系统时间不需要优化
（2）获取秒杀地址：CDN一般适合不易变化；
     但适合服务器端缓存 redis （集群，可以通过业务逻辑控制 ）
     一致性低（超时穿透、改数据库主动更新缓存）
（3）秒杀操作：核心的数据操作；库存问题不一致，所以不适合服务器缓存（事务控制）
（4）一行数据的竞争（大量的update操作）

解决方案：
（1）原子计数器 《---》 存库【redis 、NoSql】
     --》记录消息行为 【记录谁做了什么事】
     --》消息队列
     --》分布式MQ 【阿里巴巴的RocketMQ、ActiveMQ】
     --》消费
     --》数据落地

痛点：
强大的运维团队来维护数据一致性，开发成本（数据一致性、回滚、超时、幂等性难保证（重复秒杀））

用MySQL：一条ID记录update 4万的QPS
java 和数据库通信会存在网络延迟和GC，同一行数据更新是串行化的
所以 行级锁保持时间就会增加

前端控制：按钮重复
动静态分离：CDN缓存（详情页）、后端缓存
事物竞争：减少事务锁时间  存储过程
Contact GitHub API Training Shop Blog About
© 2016 GitHub, Inc. Terms Privacy Security Status Help
