# quarkus-redis-klock
Redis-based quarkus distributed lock extension
## Quick start
- 1、Introduce maven coordinates

```
        <dependency>
            <groupId>org.github.keking</groupId>
            <artifactId>quarkus-redis-klock-ext</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
```
- 2、Add the following configuration in the application.properties file

```
#klock
quarkus.klock=true
quarkus.klock.redis.database=12
quarkus.klock.redis.password=sasa
quarkus.klock.redis.address=redis://192.168.1.204:6379
```
By default, the switch of klock extension is turned off, and you need to use the [quarkus.klock=true] configuration to manually turn it on.

- 3、How to use

```
@Singleton
public class ServiceA {

    @Klock
    public String hello( @KlockKey String name, @KlockKey(fieldName = "name") User user){
        return "hello " + name;
    }
}
```

As in the code example above, the klock distributed lock quarkus extension is driven by adding annotations. @klock indicates that a distributed lock is added to this method. The name of the lock is: (default: full class name + method name, through the name attribute Designated) + designated business Key. Use the @KlockKey annotation to mark the locked business key, and try to reduce the strength of the lock while meeting business needs. If the input parameter is an object, you can use fieldName to specify an attribute value in the object to be obtained as the business Key,
The above represents the use of the name attribute value in the user object as the business key. The same business key will be locked, and different business keys will be released

## Other resources

spring boot version：https://github.com/kekingcn/spring-boot-klock-starter