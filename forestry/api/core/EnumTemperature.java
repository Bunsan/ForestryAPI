/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 * 
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.core;

import com.bioxx.tfc.Core.TFC_Climate;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 *  Many things Forestry use temperature and humidity of a biome to determine whether they can or how they can work or spawn at a given location.
 * 
 *  This enum concerns temperature.
 */
public enum EnumTemperature {
	NONE("None", "habitats/ocean"), ICY("Icy", "habitats/snow"), COLD("Cold", "habitats/taiga"),
	NORMAL("Normal", "habitats/plains"), WARM("Warm", "habitats/jungle"), HOT("Hot", "habitats/desert"), HELLISH("Hellish", "habitats/nether");

	public final String name;
	public final String iconIndex;

	private EnumTemperature(String name, String iconIndex) {
		this.name = name;
		this.iconIndex = iconIndex;
	}

	public String getName() {
		return this.name;
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon() {
		return ForestryAPI.textureManager.getDefault(iconIndex);
	}

	/**
	 * Determines the EnumTemperature given a floating point representation of
	 * Minecraft temperature. Hellish biomes are handled based on their biome
	 * type - check BiomeHelper.isBiomeHellish.
	 * @param rawTemp raw temperature value
	 * @return EnumTemperature corresponding to value of rawTemp
	 */
	public static EnumTemperature getFromValue(float rawTemp)
	{
		if (rawTemp > 40f) {
			return HELLISH;
		}
		else if (rawTemp > 30f) {
			return HOT;
		}
		else if (rawTemp > 20f) {
			return WARM;
		}
		else if (rawTemp > 5f) {
			return NORMAL;
		}
		else if (rawTemp > 0f) {
			return COLD;
		}
		else {
			return ICY;
		}
	}

	public static EnumTemperature getFromCoord(World world, int x, int y, int z)
	{
		return getFromValue(TFC_Climate.getHeightAdjustedTemp(world, x, y, z));
	}

	public static EnumTemperature getFromChunkCoord(World world, int x, int y, int z)
	{
		int i = x * 16, j = y, k = z * 16;

			return getFromValue(TFC_Climate.getHeightAdjustedTemp(world, i, j, k));

	}

	public static EnumTemperature getFromBiome(BiomeGenBase biomeGenBase) {
		if (BiomeHelper.isBiomeHellish(biomeGenBase)) {
			return HELLISH;
		}
		return getFromValue(biomeGenBase.temperature);
	}

	public static EnumTemperature getFromBiome(BiomeGenBase biomeGenBase, int x, int y, int z) {
		if (BiomeHelper.isBiomeHellish(biomeGenBase)) {
			return HELLISH;
		}
		float temperature = biomeGenBase.getFloatTemperature(x, y, z);
		return getFromValue(temperature);
	}
}
