package net.ironhorsedevgroup.mods.immersivebogies;

import net.minecraft.init.Blocks;
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

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // some example code
        logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }
}
