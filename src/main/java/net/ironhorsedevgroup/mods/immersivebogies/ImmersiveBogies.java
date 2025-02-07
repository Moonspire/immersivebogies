package net.ironhorsedevgroup.mods.immersivebogies;

import minecrafttransportsimulator.packloading.CustomClassifications;
import net.ironhorsedevgroup.mods.immersivebogies.vehicle.JSONRollingStock;
import net.ironhorsedevgroup.mods.immersivebogies.registry.MTSObjects;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = ImmersiveBogies.MODID, name = ImmersiveBogies.NAME, version = ImmersiveBogies.VERSION)
public class ImmersiveBogies
{
    public static final String MODID = "immersivebogies";
    public static final String NAME = "Immersive Bogies";
    public static final String VERSION = "1.0-SNAPSHOT";

    public static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();

        CustomClassifications.register(JSONRollingStock.CLASSIFICATION);

        //MTSObjects.addDefaultItems();
        MTSObjects.addPackItems(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    }
}
