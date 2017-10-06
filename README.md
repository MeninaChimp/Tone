# Tone
支持热加载，变更监听，代码无侵入，灰度发布的分布式配置管理平台

特性
--------
- 无侵入
- 热加载
- 变更监听
- 灰度发布
- 网络闪断恢复监听
- 权限管理（待实现）

配置
------

- 配置tone.properties文件

  你需要在resource目录下配置一个tone.properties文件

``` properties
    
    tone.connectAddress: 127.0.0.1:2181
    tone.apps: sso,redis-A,mongodb-A

```

使用
------

控制台：
---


客户端：
---

 - 引入依赖：
 
 ```
    <dependency>
        <groupId>org.menina</groupId>
        <artifactId>tone-client</artifactId>
        <version>{lastest.version}</version>
    </dependency>
```
  

 - 开启Tone的配置管理的功能. 

```
    @Configuration
    @EnableToneConfig
    public class EnableTone {
    }
```
 
 - 需要热加载的属性，打上@Refresh注解，注解的值是你需要监听的配置key:

```
    @Refresh("redis.port")
    private Integer needHotLoad;
```
    打上@Refresh注解的属性需要提供Set方法,属性所在的类的实例需要注入到Spring.
   
 - 需要监听变更，实现Listener接口，并注入到Spring。
   
```
    @Slf4j
    @Component
    public class ChangeListener implements Listener{
        @Override
        public void propertyChange(ListenerChain listenerChain, Map<String, String> map) {
            log.info("ChangeListener:" + map.toString());
            listenerChain.invoke(map);
        }
    }
```
  
扩展
------

  
