package com.prupe.mcpatcher.mal.tessellator;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Tessellator;

@Environment(EnvType.CLIENT)
public class TessellatorAPI {
    public static Tessellator getTessellator() {
        return Tessellator.instance;
    }

    public static void startDrawingQuads(Tessellator tessellator) {
    	tessellator.startDrawingQuads();
    }

    public static void addVertexWithUV(Tessellator tessellator, double x, double y, double z, double u, double v) {
    	tessellator.addVertexWithUV(x, y, z, u, v);
    }

    public static void setColorOpaque_F(Tessellator tessellator, float r, float g, float b) {
    	tessellator.setColorOpaque_F(r, g, b);
    }

    public static void draw(Tessellator tessellator) {
    	tessellator.draw();
    }
}
