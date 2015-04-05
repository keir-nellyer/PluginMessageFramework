# PluginMessageFramework

Project is still a WIP, the framework can drastically change at any moment, use at your own risk.

PluginMessageFramework is a project evolved from the legacy project [PluginMessageAPI+](https://github.com/iKeirNez/PluginMessageAPI-Plus-LEGACY) and is licensed under the [MIT License](https://tldrlegal.com/license/mit-license).
It aims to provide a seamless plugin message api across multiple implementations allowing plugin messages to be handled as packet objects rather than simple primitive types.

## Requirements

* Java 7 or higher
* [SLF4J](http://www.slf4j.org/) API & Implementation

## Usage

* [Bukkit](pmf-bukkit/README.md)
* Sponge (example coming soon)
* [BungeeCord](pmf-bungee/README.md)

### Creating Packets

```java
public class MyPacket extends Packet {

    private static final long serialVersionUID = 4714156896979723677L;
    
    private String message;
    
    public MyPacket(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }

}
```

Please note that all fields **must** be of a type which implements java.io.Serializable otherwise you may need to customize the serialization and deserialization by implementing your own readObject(ObjectInputStream) and writeObject(ObjectOutputStream) methods.

## Coming Soon

* Dependency injection with [GUICE](https://github.com/google/guice)