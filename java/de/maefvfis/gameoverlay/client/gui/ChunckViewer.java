package de.maefvfis.gameoverlay.client.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import sun.security.ssl.Debug;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.chunk.Chunk;
import de.maefvfis.gameoverlay.handler.ConfigurationHandler;
import de.maefvfis.gameoverlay.objects.Output;
import de.maefvfis.gameoverlay.objects.PlayerTextures;

public class ChunckViewer {
	
	private List whitelist = Arrays.asList(ConfigurationHandler.PlayerGridWhitelist.split(","));
	public WorldClient world = Minecraft.getMinecraft().theWorld;
	public ChunckViewer() {
		
	}
	
	public List ListPlayers(Chunk chunk) {
		List <String> result = new ArrayList();
		for (Object o : world.loadedEntityList) {
			if (o instanceof EntityPlayer && is_aligned(chunk,((Entity) o).getPosition())) {
				if(!whitelist.contains(((EntityPlayer) o).getName())) {
					result.add(((EntityPlayer) o).getName());
				}
			} 
		}
		return result;
	}
	
	
	public int countEntity(Class<?> instance, Chunk chunk, Boolean IsWither) {
		int result = 0;
		for (Object o : world.loadedEntityList) {
			if (instance.isInstance(o) && is_aligned(chunk,((Entity) o).getPosition())) {
				if(IsWither && instance.isInstance(o)) {
					if(((EntitySkeleton) o).getSkeletonType() == 1) {
						result++;
					}
				} else if(o instanceof EntityPlayer) {
					if(!whitelist.contains(((EntityPlayer) o).getName())) {
						result++;
					}
				} else {
					result++;
				}
			} 
		}
		return result;
	}
	
	
	
	
	public List<Output> ListEntity(Class<?> instance, Chunk chunk, Boolean IsWither) {
		List <Output> result = new ArrayList<Output>();
		for (Object o : world.loadedEntityList) {
			if (instance.isInstance(o) && is_aligned(chunk,((Entity) o).getPosition())) {
				if(IsWither && instance.isInstance(o)) {
					if(((EntitySkeleton) o).getSkeletonType() == 1) {
						result.add(new Output(((Entity) o).getEntityId(),String.valueOf(MathHelper.floor_double(((Entity) o).posX))+", "+String.valueOf(MathHelper.floor_double(((Entity) o).posZ))+" ("+String.valueOf(MathHelper.floor_double(((Entity) o).posY))+")"));
					}
				} else if(o instanceof EntityPlayer) {
					if(!whitelist.contains(((EntityPlayer) o).getName())) {
						
					}
				} else {
					result.add(new Output(((Entity) o).getEntityId(),String.valueOf(MathHelper.floor_double(((Entity) o).posX))+", "+String.valueOf(MathHelper.floor_double(((Entity) o).posZ))+" ("+String.valueOf(MathHelper.floor_double(((Entity) o).posY))+")"));
				}
			} 
		}
		return result;
	}

	public List ListEntity(Class<?> instance, Chunk chunk) {
		return ListEntity(instance,chunk,false);
	}
	

	
	
	public List<choords> ListTileEntitysOnChunkChoords(Class<?> instance, Chunk chunk) {
		List<choords> result = new ArrayList<choords>();
		HashMap chunkTileEntityMap = (HashMap) chunk.getTileEntityMap();
		Iterator iterator = chunkTileEntityMap.values().iterator();

        while (iterator.hasNext())
        {
            TileEntity tileentity = (TileEntity)iterator.next();
            if (instance.isInstance(tileentity)) {
            	String text;
            	text = "";
            	for( int i = 0; i < ((TileEntitySign) tileentity).signText.length; i++) {
            		text = text + " | " + i + " " + ((TileEntitySign) tileentity).signText[i];
            	}
            	//Debug.println("" + text , "" + ((TileEntitySign) tileentity).signText.length);
            	
            	result.add(new choords(tileentity.getPos().getX(),tileentity.getPos().getZ(),tileentity.getPos().getY(),null));
            }
        }
        
		
		return result;
	}
	
	public List<choords> ListEntitysOnChunkChoords(Class<?> instance, Chunk chunk, Boolean IsWither) {
		List<choords> result = new ArrayList<choords>();
		
		for (Object o : world.loadedEntityList) {
			if (instance.isInstance(o) && is_aligned(chunk,((Entity) o).getPosition())) {
					
					
					
					if(IsWither && instance.isInstance(o)) {
						if(((EntitySkeleton) o).getSkeletonType() == 1) {
							result.add(new choords(MathHelper.floor_double(((EntitySkeleton) o).posX),MathHelper.floor_double(((EntitySkeleton) o).posZ),MathHelper.floor_double(((EntitySkeleton) o).posY),null));
						}
					} else if(o instanceof EntityPlayer) {
						Debug.println("", ((EntityPlayer) o).getName());
						
						//DedeNator.mach(((EntityPlayer) o));
						if(!whitelist.contains(((EntityPlayer) o).getName()) && Minecraft.getMinecraft().thePlayer.getName() != ((EntityPlayer) o).getName()) {
							
							
							
							
							// Add ResourceLocation to PlayerTextures.List
							ResourceLocation resourcelocation = null;
							Minecraft minecraft = Minecraft.getMinecraft();
							Map map = minecraft.getSkinManager().loadSkinFromCache(((EntityPlayer) o).getGameProfile());
							if (map.containsKey(Type.SKIN))
		                    {
								resourcelocation = minecraft.getSkinManager().loadSkin((MinecraftProfileTexture)map.get(Type.SKIN), Type.SKIN);
								//resourcelocation = minecraft.func_152342_ad().func_152792_a((MinecraftProfileTexture)map.get(Type.SKIN), Type.SKIN);
		                    }
							//
							result.add(new choords(MathHelper.floor_double(((EntityPlayer) o).posX),MathHelper.floor_double(((EntityPlayer) o).posZ),MathHelper.floor_double(((EntityPlayer) o).posY),resourcelocation));
							
						}
					} else {
						result.add(new choords(MathHelper.floor_double(((Entity) o).posX),MathHelper.floor_double(((Entity) o).posZ),MathHelper.floor_double(((Entity) o).posY),null));
					}
					
					
					
					
				
			}
		}
		return result;
	}
	public List<choords> ListEntitysOnChunkChoords(Class<?> instance, Chunk chunk) {
		return ListEntitysOnChunkChoords(instance,chunk,false);
	}
	public class choords {
		private final ResourceLocation Steve = new ResourceLocation("textures/entity/steve.png");
		public int x;
		public int z;
		public int y;
		public ResourceLocation resourcelocation;
		
		public choords(int x1, int z1, int y1, ResourceLocation resourcelocation2) {
			this.x = x1;
			this.z = z1;
			this.y = y1;
			if(resourcelocation2 == null) {
				resourcelocation2 = Steve;
			}
			this.resourcelocation = resourcelocation2;
		}
	}
	

	public int countEntity(Class<?> instance, Chunk chunk) {
		return countEntity(instance,chunk,false);
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
