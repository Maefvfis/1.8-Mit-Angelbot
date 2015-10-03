package de.maefvfis.gameoverlay.client.gui;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.chunk.Chunk;
import de.maefvfis.gameoverlay.handler.ConfigurationHandler;
import de.maefvfis.gameoverlay.objects.CSVSchildManager;
import de.maefvfis.gameoverlay.objects.Output;
import de.maefvfis.gameoverlay.objects.PlayerTextures;
import de.maefvfis.gameoverlay.objects.ShopSchild;

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
		TileEntityMobSpawner Spawner;
        while (iterator.hasNext())
        {
            TileEntity tileentity = (TileEntity)iterator.next();
            if (instance.isInstance(tileentity)) {
            	Spawner = (TileEntityMobSpawner)tileentity;
            	result.add(new choords(MathHelper.floor_double(Spawner.getPos().getX()),MathHelper.floor_double(Spawner.getPos().getZ()),MathHelper.floor_double(Spawner.getPos().getY()),null));
            }
            
        }
		
		
		
		
		
		
		return result;
	}
	
	
	public float extractprice(String string) {
		
		string = string.replaceAll("B", "");
		string = string.replaceAll("S", "");
		string = string.replaceAll(" ", "");
		//string = string.replaceAll(".", ",");
		//Debug.println("",string);
		return Float.valueOf(string);

		
		
	}
	

	public boolean isShopSign(TileEntitySign Sign) {
		
		
		if(Sign.signText[0] == null || Sign.signText[1] == null || Sign.signText[2] == null || Sign.signText[3] == null) 
			return false;
		
		
		if(Sign.signText[0].getUnformattedText().replaceAll(" ", "").equalsIgnoreCase(""))
			return false;
		
		if(Sign.signText[3].getUnformattedText().replaceAll(" ", "").equalsIgnoreCase(""))
			return false;
		
		if(!isInteger(Sign.signText[1].getUnformattedText()))
			return false;
		

		
		String preiszeile = Sign.signText[2].getUnformattedText();
		
		
		
		if(preiszeile.contains(":")) {
			String[]preise = preiszeile.split(":");
			if(!istPreis(preise[0]) || !istPreis(preise[1])) 
				
				return false;
			
			//Debug.println("","" + preise[1]);
		} else {
			if(!istPreis(preiszeile)) 
				return false;
		}
		
		
		
		
		
		return true;
		
	}
	
	
	
	public boolean istPreis(String string) {
		
		if(string.matches("B ([0-9]+).([0-9]+)|S ([0-9]+).([0-9]+)|B ([0-9]+)|S ([0-9]+)|([0-9]+).([0-9]+) B|([0-9]+).([0-9]+) S|([0-9]+) S|([0-9]+) B")) {
			return true;
		}
		
		
		return false;
		
	}
	
	public List<choords> ListEntitysOnChunkChoords(Class<?> instance, Chunk chunk, Boolean IsWither, Boolean IsTile) {
		
		if(IsTile) {
			
			return ListTileEntitysOnChunkChoords(instance, chunk);
		} else {
			return ListEntitysOnChunkChoords(instance, chunk, IsWither);
		}
		
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
						//Debug.println("", ((EntityPlayer) o).getName());
						
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
		return ListEntitysOnChunkChoords(instance,chunk,false,false);
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
	
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    return true;
	}
	
}
