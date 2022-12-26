package com.example.examplemod.networking;

import com.example.examplemod.networking.data.NetworkHandler;
import com.example.examplemod.networking.data.PacketContainer;
import com.example.examplemod.networking.data.PacketContext;
import com.example.examplemod.networking.data.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.minecraftforge.network.NetworkDirection.PLAY_TO_CLIENT;

public class ForgeNetworkHandler extends PacketRegistration implements NetworkHandler
{
    private final Map<Class<?>, SimpleChannel> CHANNELS = new HashMap<>();

    public ForgeNetworkHandler()
    {
        PACKET_MAP.forEach((clazz, packetContainer) -> registerPacket(packetContainer));
    }

    private <T> void registerPacket(PacketContainer<T> container)
    {
        SimpleChannel channel = NetworkRegistry.ChannelBuilder
                .named(container.packetIdentifier())
                .clientAcceptedVersions((a) -> true)
                .serverAcceptedVersions((a) -> true)
                .networkProtocolVersion(() -> "1")
                .simpleChannel();
        channel.registerMessage(0, container.messageType(), container.encoder(), container.decoder(), buildHandler(container.handler()));
        CHANNELS.put(container.messageType(), channel);
    }

    public <T> void sendToServer(T packet)
    {
        SimpleChannel channel = CHANNELS.get(packet.getClass());
        Connection connection = Minecraft.getInstance().getConnection().getConnection();
        if (channel.isRemotePresent(connection))
        {
            channel.sendToServer(packet);
        }
    }

    public <T> void sendToClient(T packet, ServerPlayer player)
    {
        SimpleChannel channel = CHANNELS.get(packet.getClass());
        Connection connection = player.connection.connection;
        if (channel.isRemotePresent(connection))
        {
            channel.sendTo(packet, player.connection.connection, PLAY_TO_CLIENT);
        }
    }

    private <T> BiConsumer<T, Supplier<NetworkEvent.Context>> buildHandler(Consumer<PacketContext<T>> handler)
    {
        return (message, ctx) -> {
            ctx.get().enqueueWork(() -> {
                Side side = ctx.get().getDirection().getReceptionSide().isServer() ? Side.SERVER : Side.CLIENT;
                ServerPlayer player = ctx.get().getSender();
                handler.accept(new PacketContext<>(player, message, side));
            });
            ctx.get().setPacketHandled(true);
        };
    }
}
