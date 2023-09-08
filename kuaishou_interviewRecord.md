# 快手二面

> 描述一个你任职到现在最有挑战性的一个项目？

- ~ 在公司工作三年有余，具有挑战性的项目当属老系统Java化（后面这个老系统叫Doctor）。公司创始之初，除开负责页面展示的avatar外，只有本部门的这一个后端项目，和公司有关的一切业务、系统都是从Doctor摘分出去的，这就导致项目不仅仅是一行一行的语言翻译，还需要重新考虑实体的划分、数据库拆分；同时线上正在运行系统还需要考虑上线的影响范围、数据导入时新数据的双写问题；大局观上还要考虑拆分后的微服务间的分层问题、容错问题等。  
  ~ **比较头疼过的有以下几个点：**  
  1）老表拆分后的系统划分：太多了管理成本高、太少了拆分意义不大，需求迭代成本依旧不低；  
  2）没有大数据的支持，拆分后没有办法像原来一样从不同的维度进行数据筛选排序；  
  3）虽然本系统提供的大多数为读类接口，但是其他系统读取后写入的情况比较多，如何从开发角度尽可能的保证系统上线前后的接口返回数据一致性，减少测试压力也很关键；  
  4）原本Doctor系统因为请求多、接口响应慢，抽调了几十台机器在线上提供服务，对于单量并不如互联网大厂的我们这已经是一个不小的数字，拆分为新的结构之后，如何用更少的资源、更合理的缓存 / 数据库设计，来减少不必要的资源浪费。

> 假设数据库现在有 `idx_a_b` 、`idx_a_c` 两个联合索引，现在有一条SQL是 `select * from table where a = :xxx`，会走哪个联合索引？为什么？

- ~ 这个要看情况，首先联合索引是要走最左缀匹配的，即对于 `idx_a_b` 和 `idx_a_c` 来说一定要有 "a = :xxx" 这个条件；满足了最左原则之后，就要看MySQL的优化器怎么选择执行代价最低的方案：  
~ 1）扫描行数；2）是否使用了临时表；3）是否使用文件排序；  
~ 后两者肯定不符合我们题设的需求，那么就要看 `idx_a_b` 和 `idx_a_c` 索引的索引基数了，一般会选基数大的，代表区分度比较高，具体基数可以用 `show index from tableName` 查看。
- ~ **_特殊情况是指，增删特别频繁的表可能会有问题，可以使用 analyze table tableName 语句进行分析_**

> 如果是 `select b from table where a = :xxx` 呢？

- ~ 走AB的联合索引，因为这样就不用回表了，直接从索引上获取我们所需要的数据。

> MySQL是怎么做主从复制的？

- ~ Master记录binlog并发送, 然后Slave用收到的binlog刷数据。

> 你们服务上线部署主要关注哪些指标？

- ~ CPU波动、内存波动、Sentry量、Zabbix上的机器状态、数据库压力、接口Status非零占比。

> 服务器部署的话CPU、内存是多少？

- ~ 4核8G。

> 用的什么垃圾回收器？

- ~ G1。

> 只有G1可以调整垃圾回收的时间吗？

- ~ 嗯对，招牌功能。

> G1和CMS有什么区别吗？

- ~ CMS号称是最低用户停顿时间的垃圾收集器，在标记GCRoots和关联存活对象的时候是和用户行为并行的，只在重标记的时候会短暂的暂停用户行为，但是如果FullGC的过程中再次发生FullGC，就会转为SerialOld进行垃圾回收；  
~ G1则是可以根据使用者的意愿一定程度上自由调整垃圾回收的时间。它把整个堆分为多个Region，以Region为单位进行垃圾回收，当时间有限时，优先收集那些收集时间短的Region。

> 你提到过你们用MemoryCache导致频繁GC，为什么？

- ~ 为了列表筛选而作的数据聚合系统，在展示前台数据的时候会进行大量的大对象组装，白天访问量上升的时候，MemoryCache使用内存过多导致JVM进行频繁GC。

> 不用MemoryCache的话，就不会有这个问题了吗？

- ~ 放弃MemoryCache之后，改用Redis做方法级缓存仍有这个问题，只不过压力来到了Redis，Redis的占用量长时间处于临界值，也是不健康的表现。

> 想要看一个服务的GC情况要怎么看？

> 为什么看服务器GC情况要暂停服务？

> 平常的异常GC情况要怎么看？

> 有一个微博、朋友圈，关注了十个人，业务需求是你要看到最新的十条朋友圈，你要怎么去设计表结构、查询、缓存？

> 大V的热点数据，缓存你要怎么设置？

> 假如用Redis缓存，热点数据要怎么处理？Redis已经扛不住了要怎么处理？

> Redis的大Key遇到过吗，会有什么问题？

> 算法：1）两两交换链表节点并返回：1->2->3->4 ===> 2->1->4->3；2）给定一个二叉树，返回他的前序遍历结果加和，时间复杂度是多少？