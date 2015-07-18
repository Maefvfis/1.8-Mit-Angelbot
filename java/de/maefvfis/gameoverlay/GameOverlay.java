package de.maefvfis.gameoverlay;

import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
//import cpw.mods.fml.common.FMLCommonHandler;
//import cpw.mods.fml.common.Mod;
//import cpw.mods.fml.common.SidedProxy;
//import cpw.mods.fml.common.event.FMLInitializationEvent;
//import cpw.mods.fml.common.event.FMLPostInitializationEvent;
//import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import de.maefvfis.gameoverlay.client.handler.GuiOpenHandler;
import de.maefvfis.gameoverlay.client.handler.KeyInputEventHandler;
import de.maefvfis.gameoverlay.client.handler.LightOverlayHandler;
import de.maefvfis.gameoverlay.client.handler.MainTick;
import de.maefvfis.gameoverlay.client.handler.SoundHandler;
import de.maefvfis.gameoverlay.handler.ConfigurationHandler;
import de.maefvfis.gameoverlay.proxy.IProxy;
import de.maefvfis.gameoverlay.reference.Reference;
import de.maefvfis.gameoverlay.utility.LogHelper;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME,version = Reference.MOD_VERSION, guiFactory = Reference.GUI_FACTORY_CLASS)
public class GameOverlay {

    @Mod.Instance(Reference.MOD_ID)
    public static GameOverlay instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static IProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigurationHandler.init(event.getSuggestedConfigurationFile());
        FMLCommonHandler.instance().bus().register(new ConfigurationHandler());
        proxy.registerKeyBindings();
        MinecraftForge.EVENT_BUS.register(new MainTick());
        

        
        
        MinecraftForge.EVENT_BUS.register(new GuiOpenHandler());
        LogHelper.info("PRE ist durch .........................................");
        
        FMLCommonHandler.instance().bus().register(new SoundHandler());
        MinecraftForge.EVENT_BUS.register(new SoundHandler());
        
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(new KeyInputEventHandler());
        MinecraftForge.EVENT_BUS.register(new LightOverlayHandler());
        
        LogHelper.info("INIT ist durch .........................................");
        
        
        //MinecraftForge.EVENT_BUS.register(new SoundHandler());
    }
    

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        LogHelper.info("POST ist durch .........................................");
        
        
        //MinecraftForge.EVENT_BUS.register(new SoundHandler());
    }
}
