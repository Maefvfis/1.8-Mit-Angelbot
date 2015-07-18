package de.maefvfis.gameoverlay.client.renderer;

import de.maefvfis.gameoverlay.client.renderer.vector.Vector4i;
import de.maefvfis.gameoverlay.handler.ConfigurationHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

import org.lwjgl.input.Keyboard;

import sun.security.ssl.Debug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Events {
	public static final List<Vector4i> SPAWN_LIST = new ArrayList<Vector4i>();

	private final Minecraft minecraft = Minecraft.getMinecraft();
	private final Frustum frustrum = new Frustum();
	private final AxisAlignedBB boundingBox = AxisAlignedBB.fromBounds(0, 0, 0, 0, 0, 0);

	private int ticks = -1;

	@SubscribeEvent
	public void tick(ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			onTick();
		}
	}


	public boolean onTick() {
		this.minecraft.mcProfiler.startSection("msh");

		if (--this.ticks < 0) {
			this.ticks = ConfigurationHandler.updateRate;

			if (this.minecraft.theWorld != null && ConfigurationHandler.renderSpawns) {
				SPAWN_LIST.clear();

				this.frustrum.setPosition(PlayerPosition.x, PlayerPosition.y, PlayerPosition.z);

				World world = this.minecraft.theWorld;

				int lowX, lowY, lowZ, highX, highY, highZ, x, y, z;

				lowX = (int) (Math.floor(PlayerPosition.x) - ConfigurationHandler.renderRangeXZ);
				highX = (int) (Math.floor(PlayerPosition.x) + ConfigurationHandler.renderRangeXZ);
				lowY = (int) (Math.floor(PlayerPosition.y) - ConfigurationHandler.renderRangeYBellow);
				highY = (int) (Math.floor(PlayerPosition.y) + ConfigurationHandler.renderRangeYAbove);
				lowZ = (int) (Math.floor(PlayerPosition.z) - ConfigurationHandler.renderRangeXZ);
				highZ = (int) (Math.floor(PlayerPosition.z) + ConfigurationHandler.renderRangeXZ);

				for (y = lowY; y <= highY; y++) {
					for (x = lowX; x <= highX; x++) {
						for (z = lowZ; z <= highZ; z++) {
							if (!this.frustrum.isBoundingBoxInFrustum(this.boundingBox.fromBounds(x, y, z, x + 1, y + 1, z + 1))) {
								continue;
							}

							if (getCanSpawnHere(world, x, y, z)) {
								SPAWN_LIST.add(new Vector4i(x, y, z, 1));
							}
						}
					}
				}
			}
		}

		this.minecraft.mcProfiler.endSection();

		return true;
	}


	private boolean getCanSpawnHere(World world, int x, int y, int z) {
		Chunk chunk = Minecraft.getMinecraft().theWorld.getChunkFromBlockCoords(new BlockPos(x, 0, z));
		if(!chunk.isLoaded()) { return false; }
		
		Block block = chunk.getBlock(x, y - 1, z);
		if (block == null || block == Blocks.air || block.getMaterial().isLiquid() || !block.canCreatureSpawn(world,new BlockPos( x, y-1, z),SpawnPlacementType.ON_GROUND)) {
			return false;
		}

		
		//Debug.println(""+chunk.getSavedLightValue(EnumSkyBlock.Block, x & 15, y+1, z & 15), "");
		if(chunk.getLightFor(EnumSkyBlock.BLOCK, new BlockPos(x & 15, y+1, z & 15)) >= 8) {
			return false;
		}

		return true;
	}

}
