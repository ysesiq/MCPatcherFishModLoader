package com.prupe.mcpatcher.renderpass;

import com.prupe.mcpatcher.mal.block.RenderPassAPI;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Block;

@Environment(EnvType.CLIENT)
abstract public class RenderPassMap {
	private static final int[] MAP = new int[]{0, 0, 0, 1, 2, 3};
	
    public static int map18To17(int pass) {
    	return pass > 1 ? MCPatcherToVanilla(pass) : pass;
    }

    public static int map17To18(int pass) {
    	return pass <= 1 ? vanillaToMCPatcher(pass) : pass;
    }
    
    protected static int vanillaToMCPatcher(int pass) {
        for (int i = 0; i < MAP.length; i++) {
            if (MAP[i] == pass) {
                return i;
            }
        }
        return -1;
    }
    
    protected static int MCPatcherToVanilla(int pass) {
        return pass >= 0 && pass < MAP.length ? MAP[pass] : -1;
    }

    protected static int getDefaultRenderPass(Block block) {
    	return vanillaToMCPatcher(block.getRenderBlockPass());
    }

    protected static int getCutoutRenderPass() {
        return RenderPassAPI.SOLID_RENDER_PASS;
    }
}
// ---END EDIT---
