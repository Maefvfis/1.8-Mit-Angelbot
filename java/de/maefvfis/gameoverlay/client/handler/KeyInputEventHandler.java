package de.maefvfis.gameoverlay.client.handler;


import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import de.maefvfis.gameoverlay.client.gui.CreativeInv2;
import de.maefvfis.gameoverlay.client.gui.ModGuiConfig;
import de.maefvfis.gameoverlay.client.settings.Keybindings;
import de.maefvfis.gameoverlay.handler.ConfigurationHandler;
import de.maefvfis.gameoverlay.objects.ChunkImage;
import de.maefvfis.gameoverlay.reference.EntityGridOptions;
import de.maefvfis.gameoverlay.reference.Key;
import de.maefvfis.gameoverlay.utility.LogHelper;

public class KeyInputEventHandler {

    private static Key getPressedKeybinding()
    {
        if (Keybindings.menu.isPressed())
        {
        	Minecraft  mc = Minecraft.getMinecraft();
            mc.displayGuiScreen(new ModGuiConfig(mc.currentScreen));
            return Key.MENU;
        }
        if (Keybindings.creativinv.isPressed())
        {
        	Minecraft  mc = Minecraft.getMinecraft();
            mc.displayGuiScreen(new CreativeInv2(Minecraft.getMinecraft().thePlayer));
            return Key.CREATIVEINV;
        }
        
        if (Keybindings.maptoggle.isPressed()) {
        	//Toggle Map
        	EntityGridOptions.CycleActiveEntity();
        }
        
        
        //if (Minecraft.getMinecraft().gameSettings.keyBindPlayerList.isKeyDown())
        //{
        //	Minecraft.getMinecraft().gameSettings.keyBindPlayerList.unPressAllKeys();
        //	Minecraft  mc = Minecraft.getMinecraft();
         //   mc.displayGuiScreen(new PlayerList(Minecraft.getMinecraft()));
         //   return Key.TEST;
        //}
        
        if(Keybindings.makro_1.isPressed()) {
        	Minecraft.getMinecraft().thePlayer.sendChatMessage(ConfigurationHandler.MakroString1);
        }
        if(Keybindings.makro_2.isPressed()) {
        	Minecraft.getMinecraft().thePlayer.sendChatMessage(ConfigurationHandler.MakroString2);
        }
        if(Keybindings.makro_3.isPressed()) {
        	Minecraft.getMinecraft().thePlayer.sendChatMessage(ConfigurationHandler.MakroString3);
        }
        if(Keybindings.makro_4.isPressed()) {
        	Minecraft.getMinecraft().thePlayer.sendChatMessage(ConfigurationHandler.MakroString4);
        }
        if(Keybindings.makro_5.isPressed()) {
        	Minecraft.getMinecraft().thePlayer.sendChatMessage(ConfigurationHandler.MakroString5);
        }
        if(Keybindings.makro_6.isPressed()) {
        	Minecraft.getMinecraft().thePlayer.sendChatMessage(ConfigurationHandler.MakroString6);
        }
        if(Keybindings.makro_7.isPressed()) {
        	Minecraft.getMinecraft().thePlayer.sendChatMessage(ConfigurationHandler.MakroString7);
        }
        if(Keybindings.makro_8.isPressed()) {
        	Minecraft.getMinecraft().thePlayer.sendChatMessage(ConfigurationHandler.MakroString8);
        }
        if(Keybindings.makro_9.isPressed()) {
        	Minecraft.getMinecraft().thePlayer.sendChatMessage(ConfigurationHandler.MakroString9);
        }
        
        
        
        
        
        return Key.UNKNOWN;
    }

    @SubscribeEvent
    public void handleKeyInputEvent(InputEvent.KeyInputEvent event)
    {
   
        LogHelper.info(getPressedKeybinding());
    }
}