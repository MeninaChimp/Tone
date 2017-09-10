# Tone
支持热加载，变更监听，代码无侵入，灰度发布的分布式配置管理平台

特性
--------
- 无侵入
- 热加载
- 变更监听
- 灰度发布（待实现）

配置
------

- 配置tone.properties文件

  你需要在resource目录下配置一个tone.properties文件

``` properties
    
    tone.connectAddress: 127.0.0.1:2181
    tone.rootNode=/tone/org/menina/serviceA
    tone.version=1.0.1
    tone.nodes=comm.redis.1

```

使用
------

  开启Tone的配置管理的功能    

```
    @Configuration
    @EnableToneConfig
    public class EnableTone {
    }
```
 
  如果你需要暂时关闭Tone，移除掉@EnableToneConfig即可。
    
扩展
------

  待整理
