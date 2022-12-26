package com.example.examplemod;

import com.example.examplemod.networking.FabricNetworkHandler;
import com.example.examplemod.networking.data.NetworkHandler;
import com.example.examplemod.networking.data.Side;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class ExampleMod implements ModInitializer {

    @Override
    public void onInitialize() {

        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        Constants.LOG.info("Hello Fabric world!");
        NetworkHandler handler;
        if( EnvType.CLIENT.equals(FabricLoader.getInstance().getEnvironmentType())) {
            handler = new FabricNetworkHandler(Side.CLIENT);
        } else {
            handler = new FabricNetworkHandler(Side.SERVER);
        }
        CommonClass.init(handler);
    }
}
