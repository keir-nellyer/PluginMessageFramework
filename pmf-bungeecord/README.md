BungeeCord Implementation
============

Usage
-----

Using our custom packet **MyPacket** created in the main [README](/README.md#creating-packets).
In the below example, we are going to pretend that we receive messages from a server and echo them back in caps lock.

```java
public ProxyGateway<ProxiedPlayer, ServerInfo> gateway = BungeeGatewayProvider.getGateway("MyChannelName", ProxySide.SERVER, this);

public void onEnable() {
    gateway.registerListener(this);
}

@PacketHandler
public void onMyPacket(ProxiedPlayer proxiedPlayer, MyPacket myPacket) {
    gateway.sendPacket(proxiedPlayer, new MyPacket(myPacket.getMessage().toUpperCase()));
}
```