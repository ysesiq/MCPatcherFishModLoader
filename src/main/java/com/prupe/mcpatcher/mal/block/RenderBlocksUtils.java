package com.prupe.mcpatcher.mal.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.*;

@Environment(EnvType.CLIENT)
public class RenderBlocksUtils {
	public static boolean enableBetterGrass = false;

	public static Block grassBlock = BlockAPI.getFixedBlock("minecraft:grass");
	public static Block snowBlock = BlockAPI.getFixedBlock("minecraft:snow_layer");
	public static Block craftedSnowBlock = BlockAPI.getFixedBlock("minecraft:snow");

	private static final int COLOR = 0;
	private static final int NONCOLOR = 1;
	private static final int COLOR_AND_NONCOLOR = 2;

	private static final int[] colorMultiplierType = new int[6];
	private static final float[][] nonAOMultipliers = new float[6][3];

	public static final float[] AO_BASE = new float[]{0.5f, 1.0f, 0.8f, 0.8f, 0.6f, 0.6f};

	public static int layerIndex;
	public static Icon blankIcon;

	private static int grassFace;
	private static Icon grassIcon;

	public static void setupColorMultiplier(Block block, IBlockAccess blockAccess, int i, int j, int k,
			boolean haveOverrideTexture, float r, float g, float b) {
		if (haveOverrideTexture || !RenderPassAPI.instance.useColorMultiplierThisPass(block)) {
			colorMultiplierType[0] = COLOR;
			colorMultiplierType[1] = COLOR;
			colorMultiplierType[2] = COLOR;
			colorMultiplierType[3] = COLOR;
			colorMultiplierType[4] = COLOR;
			colorMultiplierType[5] = COLOR;
		} else if (block == grassBlock) {
			colorMultiplierType[0] = NONCOLOR;
			colorMultiplierType[1] = NONCOLOR;
			colorMultiplierType[2] = NONCOLOR;
			colorMultiplierType[3] = NONCOLOR;
			colorMultiplierType[4] = NONCOLOR;
			colorMultiplierType[5] = NONCOLOR;
		} else {
			colorMultiplierType[0] = COLOR;
			colorMultiplierType[1] = COLOR;
			colorMultiplierType[2] = COLOR;
			colorMultiplierType[3] = COLOR;
			colorMultiplierType[4] = COLOR;
			colorMultiplierType[5] = COLOR;
		}
		if (!isAmbientOcclusionEnabled() || BlockAPI.getBlockLightValue(block) != 0) {
			setupColorMultiplier(0, r, g, b);
			setupColorMultiplier(1, r, g, b);
			setupColorMultiplier(2, r, g, b);
			setupColorMultiplier(3, r, g, b);
			setupColorMultiplier(4, r, g, b);
			setupColorMultiplier(5, r, g, b);
		}
	}
	
	public static void setupColorMultiplierForceNoAO(Block block, IBlockAccess blockAccess, int i, int j, int k, float r, float g, float b, boolean useColor) {
		if(useColor) {
			colorMultiplierType[0] = COLOR;
			colorMultiplierType[1] = COLOR;
			colorMultiplierType[2] = COLOR;
			colorMultiplierType[3] = COLOR;
			colorMultiplierType[4] = COLOR;
			colorMultiplierType[5] = COLOR;
		}
		else {
			colorMultiplierType[0] = NONCOLOR;
			colorMultiplierType[1] = NONCOLOR;
			colorMultiplierType[2] = NONCOLOR;
			colorMultiplierType[3] = NONCOLOR;
			colorMultiplierType[4] = NONCOLOR;
			colorMultiplierType[5] = NONCOLOR;
		}
		
		setupColorMultiplier(0, r, g, b);
		setupColorMultiplier(1, r, g, b);
		setupColorMultiplier(2, r, g, b);
		setupColorMultiplier(3, r, g, b);
		setupColorMultiplier(4, r, g, b);
		setupColorMultiplier(5, r, g, b);
	}

	public static void setupColorMultiplier(Block block, int metadata, boolean useColor) {
		if (block == grassBlock || !useColor) {
			colorMultiplierType[0] = NONCOLOR;
			colorMultiplierType[2] = NONCOLOR;
			colorMultiplierType[3] = NONCOLOR;
			colorMultiplierType[4] = NONCOLOR;
			colorMultiplierType[5] = NONCOLOR;
		} else {
			colorMultiplierType[0] = COLOR;
			colorMultiplierType[2] = COLOR;
			colorMultiplierType[3] = COLOR;
			colorMultiplierType[4] = COLOR;
			colorMultiplierType[5] = COLOR;
		}
	}

	private static void setupColorMultiplier(int face, float r, float g, float b) {
		float[] mult = nonAOMultipliers[face];
		float ao = AO_BASE[face];
		mult[0] = ao;
		mult[1] = ao;
		mult[2] = ao;
		if (colorMultiplierType[face] != NONCOLOR) {
			mult[0] *= r;
			mult[1] *= g;
			mult[2] *= b;
		}
	}

	public static boolean useColorMultiplier(int face) {
		layerIndex = 0;
		return useColorMultiplier1(face);
	}

	private static boolean useColorMultiplier1(int face) {
		if (layerIndex == 0) {
			return colorMultiplierType[getFaceIndex(face)] == COLOR;
		} else {
			return colorMultiplierType[getFaceIndex(face)] != NONCOLOR;
		}
	}

	public static boolean useColorMultiplier(boolean useTint, int face) {
		return useTint || (layerIndex++ == 0 && useColorMultiplier1(face));
	}

	public static float getColorMultiplierRed(int face) {
		return nonAOMultipliers[getFaceIndex(face)][0];
	}

	public static float getColorMultiplierGreen(int face) {
		return nonAOMultipliers[getFaceIndex(face)][1];
	}

	public static float getColorMultiplierBlue(int face) {
		return nonAOMultipliers[getFaceIndex(face)][2];
	}

	private static int getFaceIndex(int face) {
		return face < 0 ? 1 : face % 6;
	}

	public static Icon getGrassTexture(Block block, IBlockAccess blockAccess, int i, int j, int k, int face, Icon topIcon) {
		if (!enableBetterGrass || face < 2) {
			return null;
		}
		
		boolean isSnow = isSnowCovered(blockAccess, i, j, k);
		
		switch (face) {
		case 2:
			k--;
			break;

		case 3:
			k++;
			break;

		case 4:
			i--;
			break;

		case 5:
			i++;
			break;

		default:
			return null;
		}
		
		if (block == Block.grass && 
				!(BlockAPI.getBlockAt(blockAccess, i, j - 1, k) == Block.grass)) {
			return null;
		}
		
		if (!(BlockAPI.getBlockAt(blockAccess, i, j - 1, k) == Block.grass)) {
			return null;
		}
		
		if (block == Block.mycelium && 
				!(BlockAPI.getBlockAt(blockAccess, i, j - 1, k) == Block.mycelium)) {
			return null;
		}
		
		if (!(BlockAPI.getBlockAt(blockAccess, i, j - 1, k) == Block.mycelium)) {
			return null;
		}
		
		boolean neighborIsSnow = isSnowCovered(blockAccess, i, j, k) || isSnowCovered(blockAccess, i, j - 1, k);
		
		if (isSnow != neighborIsSnow) {
			return null;
		}
		
		return isSnow ? BlockAPI.getBlockIcon(snowBlock, blockAccess, i, j, k, face) : topIcon;
	}

	public static Icon getGrassIconBTW(Icon origIcon, int face) {
		grassFace = face;
		if (blankIcon != null && colorMultiplierType[face] == COLOR) {
			grassIcon = origIcon;
			return blankIcon;
		} else {
			grassIcon = null;
			return origIcon;
		}
	}

	public static Icon getGrassOverlayIconBTW(Icon origIcon) {
		if (grassIcon != null) {
			Icon t = grassIcon;
			grassIcon = null;
			return t;
		} else if (blankIcon != null && colorMultiplierType[grassFace] == NONCOLOR) {
			return blankIcon;
		} else {
			return origIcon;
		}
	}

	private static boolean isSnowCovered(IBlockAccess blockAccess, int i, int j, int k) {
		Block topBlock = BlockAPI.getBlockAt(blockAccess, i, j + 1, k);
		return topBlock == snowBlock || topBlock == craftedSnowBlock;
	}

	public static boolean isAmbientOcclusionEnabled() {
		return Minecraft.isAmbientOcclusionEnabled();
	}
}
