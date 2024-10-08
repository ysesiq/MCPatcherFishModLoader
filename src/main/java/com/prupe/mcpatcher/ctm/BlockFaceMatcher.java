// +++START EDIT+++
package com.prupe.mcpatcher.ctm;

import com.prupe.mcpatcher.MCPatcherUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.prupe.mcpatcher.ctm.RenderBlockState.*;

@Environment(EnvType.CLIENT)
public class BlockFaceMatcher {
	private final int faces;
	
    public static BlockFaceMatcher create(String propertyValue) {
        if (!MCPatcherUtils.isNullOrEmpty(propertyValue)) {
            String[] values = propertyValue.toLowerCase().split("\\s+");
            
            return new BlockFaceMatcher(values);
        }
        return null;
    }
    
    protected BlockFaceMatcher(String[] values) {
        int flags = 0;
        for (String face : values) {
            if (face.equals("bottom") || face.equals("down")) {
                flags |= (1 << BOTTOM_FACE);
            } else if (face.equals("top") || face.equals("up")) {
                flags |= (1 << TOP_FACE);
            } else if (face.equals("north")) {
                flags |= (1 << NORTH_FACE);
            } else if (face.equals("south")) {
                flags |= (1 << SOUTH_FACE);
            } else if (face.equals("east")) {
                flags |= (1 << EAST_FACE);
            } else if (face.equals("west")) {
                flags |= (1 << WEST_FACE);
            } else if (face.equals("side") || face.equals("sides")) {
                flags |= (1 << NORTH_FACE) | (1 << SOUTH_FACE) | (1 << EAST_FACE) | (1 << WEST_FACE);
            } else if (face.equals("all")) {
                flags = -1;
            }
        }
        faces = flags;
    }

    public boolean match(RenderBlockState renderBlockState) {
        int face = renderBlockState.getTextureFace();
        return face >= 0 && (faces & (1 << face)) != 0;
    }

    protected boolean isAll() {
        return faces == -1;
    }
}
// ---END EDIT---
