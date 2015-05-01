Bukkit Implementation
============

Usage
-----

Using our custom packet **MyPacket** created in the main [README](/README.md#creating-packets).
In the below example, we are going to pretend that we send a message using a command and can also receive replies, displaying them to the player.

```java
public ServerGateway<Player> gateway;

@Override
public void onEnable() {
    gateway = BukkitGatewayProvider.getGateway("MyChannelName", this);
    gateway.registerListener(this);
}

public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (sender instanceof Player) {
        Player player = (Player) sender;
        gateway.sendPacket(player, new MyPacket(StringUtils.join(args, " "))); // send a packet containing the arguments used in the command
    }
}

@PacketHandler
public void onMyPacket(Player player, MyPacket myPacket) {
    player.sendMessage("Received message: " + myPacket.getMessage());
}
```