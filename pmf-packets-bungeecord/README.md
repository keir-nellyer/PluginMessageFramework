BungeeCord Packets
=========

This is a small library to aid in the sending of BungeeCord packets to a BungeeCord Proxy.

Usage
----

Initialise the gateway as you would normally (you can find out how to do this from the [usage examples](/README.md#usage)), making sure to use the ```BungeeCordHelper.BUNGEECORD_CHANNEL``` channel.
**Before** sending or receiving any packets, you must swap the payload handler (since BungeeCord sends data a bit differently).

```java
gateway.setPayloadHandler(BungeeCordHelper.getPayloadHandler());
```

This will allow the framework to correctly serialize interpret BungeeCord packets correctly.

After that you can use the PluginMessageFramework as normal, you can find all usable BungeeCord packets [here](src/main/java/com/ikeirnez/pluginmessageframework/bungeecord/packets).
Remember, some packets will also give replies, you can receive them as normal.
Here's an example using Bukkit.

```java
@PacketHandler
public void onPacketUUID(Player senderPlayer, PacketUUID packetUUID) {
    String name = packetUUID.getPlayer();
    if (name == null) { // if null, the UUID is that of the sender
        name = senderPlayer.getName();
    }
    
    System.out.println("The UUID of player " + name + " is " + packetUUID.getUUID());
}
```