package com.example.examplemod.networking;


import com.example.examplemod.networking.data.PacketContainer;
import com.example.examplemod.networking.data.PacketContext;
import com.example.examplemod.networking.packets.ExamplePacketOne;

import com.example.examplemod.networking.packets.ExamplePacketTwo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class PacketRegistration
{
    protected final Map<Class<?>, PacketContainer<?>> PACKET_MAP = new HashMap<>();

    /**
     * Add your packets here for registration.
     */
    public PacketRegistration()
    {
        registerPacket(ExamplePacketOne.CHANNEL, ExamplePacketOne.class, ExamplePacketOne::encode, ExamplePacketOne::decode, ExamplePacketOne::handle);
        registerPacket(ExamplePacketTwo.CHANNEL, ExamplePacketTwo.class, ExamplePacketTwo::encode, ExamplePacketTwo::decode, ExamplePacketTwo::handle);
    }

    protected <T> void registerPacket(ResourceLocation packetIdentifier, Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, Consumer<PacketContext<T>> handler)
    {
        PacketContainer<T> container = new PacketContainer<>(packetIdentifier, messageType, encoder, decoder, handler);
        PACKET_MAP.put(messageType, container);
    }
}
