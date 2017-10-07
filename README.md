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

- 添加应用，配置应用信息。

![alt text](https://github.com/MeninaChimp/Tone/blob/master/doc/img/%E5%BA%94%E7%94%A8%E7%AE%A1%E7%90%86.png)

- 创建配置。

![alt text](https://github.com/MeninaChimp/Tone/blob/master/doc/img/%E5%88%9B%E5%BB%BA%E9%85%8D%E7%BD%AE.png)

- 全局发布。

![alt text](https://github.com/MeninaChimp/Tone/blob/master/doc/img/%E5%85%A8%E5%B1%80%E5%8F%91%E5%B8%83.png)

- 灰度发布。

![alt text](https://github.com/MeninaChimp/Tone/blob/master/doc/img/%E7%81%B0%E5%BA%A6%E5%8F%91%E5%B8%83.png)

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
  
实例
------

- 灰度发布实例

![alt text](https://github.com/MeninaChimp/Tone/blob/master/doc/img/%E7%83%AD%E5%8A%A0%E8%BD%BD%E5%8F%91%E5%B8%83%E5%AE%9E%E4%BE%8B.png)


  
