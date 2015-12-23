Forge Implementation
============

Usage
-----

Using our custom packet **MyPacket** created in the main [README](/README.md#creating-packets).
In the below example, we are going to pretend that we send a message using a command and can also receive replies, displaying them to the player.

```java
public ClientGateway clientGateway;
public ServerGateway<EntityPlayerMP> serverGateway;

public KeyBinding sendPacket;

@EventHandler
public void preInit(FMLPreInitializationEvent event) {
    if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
        FMLCommonHandler.instance().bus().register(new KeyInputHandler());
        sendPacket = new KeyBinding("key.sendPacket", Keyboard.KEY_P, "key.categories.pmf");
        ClientRegistry.registerKeyBinding(sendPacket);
    }
}

@EventHandler
public void init(FMLInitializationEvent event) {
    if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
        clientGateway = ForgeGatewayProvider.getClientGateway("MyChannelName");
        clientGateway.registerListener(this);
    } else {
        serverGateway = ForgeGatewayProvider.getServerGateway("MyChannelName");
        serverGateway.registerListener(this);
    }
}

@EventHandler
public void serverLoad(FMLServerStartingEvent event) {
    event.registerServerCommand(new TestCommand());
}

@PacketHandler
public void onMyPacket(MyPacket myPacket) {
    if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
        System.out.println("[CLIENT] Received message: " + myPacket.getMessage());
    } else {
        System.out.println("[SERVER] Received message: " + myPacket.getMessage());
    }
}

public class KeyInputHandler {
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (sendPacket.isPressed()) {
            clientGateway.sendPacket(new MyPacket("send from client"));
        }
    }
}

public class TestCommand extends CommandBase {
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public String getCommandName() {
        return "test";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "test";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (sender instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            serverGateway.sendPacket(player, new MyPacket("send from server: " + StringUtils.join(args, " "))); // send a packet containing the arguments used in the command
        }
    }
}
```