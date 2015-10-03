package de.maefvfis.gameoverlay.client.handler;



import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.client.event.sound.PlaySoundSourceEvent;
import net.minecraftforge.client.event.sound.PlayStreamingSourceEvent;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import de.maefvfis.gameoverlay.client.gui.CopyOfGrid2D;
import de.maefvfis.gameoverlay.client.gui.EntityGrid;
import de.maefvfis.gameoverlay.client.gui.Grid2D;
import de.maefvfis.gameoverlay.client.gui.InfoIngameGui;
import de.maefvfis.gameoverlay.client.gui.ShowItemUsage;
import de.maefvfis.gameoverlay.handler.ConfigurationHandler;
import de.maefvfis.gameoverlay.utility.LogHelper;
@SideOnly(Side.CLIENT)





public class SoundHandler {
	
	public Minecraft mc = Minecraft.getMinecraft();
	public int delay = 0;
	public int GridSize = 2;
	float hookX;
	float hookY;
	float hookZ;
	long randomTicks;
	int ticks;
	
	boolean anderleine = false;
	int randomTickDelayMin = 5;
	int randomTickDelayMax = 10;
	int randomTickDelay = 0;
	boolean gogogo = false;
	boolean wait4go = false;
	EntityFishHook Fishhook = new EntityFishHook(mc.theWorld);
	//public void onRenderChunkViewer(TickEvent.ServerTickEvent event) {
	//public void onRenderChunkViewer(RenderGameOverlayEvent.Text event) {
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public void onRenderChunkViewer(RenderGameOverlayEvent.Text event) {
		
		
		if(mc.gameSettings.showDebugInfo || !mc.inGameHasFocus) 
			return;
		
		if(ConfigurationHandler.myAngler.equalsIgnoreCase("OFF")) 
			return;
		
		ItemStack itemstack = mc.thePlayer.getCurrentEquippedItem();

		if (itemstack == null || itemstack.getItem() != Items.fishing_rod)
			return;
		
		
		if(!isFishing() && ConfigurationHandler.myAngler.equalsIgnoreCase("Auto") && gogogo) {
			mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
			gogogo = false;
		}
		else {
			if(anderleine && randomTickDelay < Fishhook.ticksExisted) {
				mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
				anderleine = false;
				wait4go = true;
				randomTicks = System.currentTimeMillis() + ((ThreadLocalRandom.current().nextInt(randomTickDelayMin,randomTickDelayMax) / 10) * 1000);
			}
		}

		
		if(!wait4go)
			return;
		
		if(System.currentTimeMillis() >= randomTicks) {
			
			wait4go = false;
			gogogo = true;
		} else {
			ticks++;
		}

	}
	
	
	
	public boolean isFishing() {
		
		World world = mc.theWorld;
		int X = MathHelper.floor_double(mc.thePlayer.posX);
		int Z = MathHelper.floor_double(mc.thePlayer.posZ);
		Chunk chunk;
		
		for (int i1 = 0; i1 <= GridSize; i1++)
		{
			for (int i2 = 0; i2 <= GridSize; i2++)
			{
				chunk = mc.theWorld.getChunkFromBlockCoords(new BlockPos(X + this.XOffset(i1),0, Z + this.ZOffset(i2)));
				int result = 0;
				for (Object o : world.loadedEntityList) {
					
					
					if (is_aligned(chunk,((Entity) o).getPosition()) && o instanceof EntityFishHook && ((EntityFishHook) o).angler == mc.thePlayer && ((EntityFishHook) o).isInWater()) {
						Fishhook = ((EntityFishHook) o);
						return true;
						
					} 
				}
			}
		}
		return false;
	}
	
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onPlaySound(PlaySoundEvent event) {
		
		if (mc.gameSettings.showDebugInfo || !mc.inGameHasFocus)
			return;
		
		//mc.thePlayer.addChatComponentMessage(new ChatComponentText(event.name));
		
		if(event.name.equalsIgnoreCase("random.splash") && isFishing()) {
			
			//mc.thePlayer.addChatComponentMessage(new ChatComponentText("SPLISHSPLASH"));
			
			
			if(isintollerance(event.sound.getXPosF(),(float)Fishhook.posX,0.2F) && isintollerance(event.sound.getZPosF(),(float)Fishhook.posZ,0.2F) && isintollerance(event.sound.getYPosF(),(float)Fishhook.posY,0.2F)) {
				mc.thePlayer.addChatComponentMessage(new ChatComponentText("Lecker Fisch :)"));
				anderleine = true;
				randomTickDelay = Fishhook.ticksExisted + ThreadLocalRandom.current().nextInt(randomTickDelayMin,randomTickDelayMax);
				
			}
		}
	}
	
	public boolean isintollerance(float value1, float value2, float tollerance) {
		
		float diff = value1 - value2;
		if(diff < 0)
			diff = diff *(-1);

		if(diff <= tollerance)
			return true;

		return false;
	}
	
	public int ZOffset(int count) {
		if(count < GridSize / 2) return ((GridSize / 2) - count) * -16;
		if(count > GridSize / 2) return (count - (GridSize / 2)) * 16;
		return 0;
	}

	public int XOffset(int count) {
		if(count < GridSize / 2) return ((GridSize / 2) - count) * 16;
		if(count > GridSize / 2) return (count - (GridSize / 2)) * -16;
		return 0;
	}
	public boolean is_aligned(Chunk chunk, BlockPos entity) {
		ChunkCoordIntPair ChunkPos = chunk.getChunkCoordIntPair();
		if(entity.getZ() >= ChunkPos.getZStart() && entity.getZ() <= ChunkPos.getZEnd()) {
			if(entity.getX() >= ChunkPos.getXStart() && entity.getX() <= ChunkPos.getXEnd()) {
				return true;
			}
		}
		return false;
	}
	

}