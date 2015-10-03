package de.maefvfis.gameoverlay.proxy;


import de.maefvfis.gameoverlay.client.handler.SoundHandler;
import de.maefvfis.gameoverlay.client.renderer.Events;
import de.maefvfis.gameoverlay.client.renderer.Renderer;
import de.maefvfis.gameoverlay.client.renderer.SpawnerHighlight;
import de.maefvfis.gameoverlay.client.settings.Keybindings;
import de.maefvfis.gameoverlay.handler.ConfigurationHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerKeyBindings() {
        ClientRegistry.registerKeyBinding(Keybindings.menu);
        ClientRegistry.registerKeyBinding(Keybindings.creativinv);
        ClientRegistry.registerKeyBinding(Keybindings.maptoggle);
        ClientRegistry.registerKeyBinding(Keybindings.SaveChunk);
        ClientRegistry.registerKeyBinding(Keybindings.makro_1);
        ClientRegistry.registerKeyBinding(Keybindings.makro_2);
        ClientRegistry.registerKeyBinding(Keybindings.makro_3);
        ClientRegistry.registerKeyBinding(Keybindings.makro_4);
        ClientRegistry.registerKeyBinding(Keybindings.makro_5);
        ClientRegistry.registerKeyBinding(Keybindings.makro_6);
        ClientRegistry.registerKeyBinding(Keybindings.makro_7);
        ClientRegistry.registerKeyBinding(Keybindings.makro_8);
        ClientRegistry.registerKeyBinding(Keybindings.makro_9);

        MinecraftForge.EVENT_BUS.register(new Renderer(Minecraft.getMinecraft()));
        FMLCommonHandler.instance().bus().register(new Events());
        
        
        FMLCommonHandler.instance().bus().register(new SpawnerHighlight());
        MinecraftForge.EVENT_BUS.register(new SpawnerHighlight());
        
    }
    
    
}
