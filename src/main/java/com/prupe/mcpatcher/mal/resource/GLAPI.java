package com.prupe.mcpatcher.mal.resource;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GLContext;

@Environment(EnvType.CLIENT)
public class GLAPI {
	private static final boolean useGlBlendFuncSeparate = GLContext.getCapabilities().OpenGL14;
	
    public static void glBindTexture(int texture) {
        if (texture >= 0) {
        	GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        }
    }

    public static void glBlendFunc(int src, int dst) {
        glBlendFuncSeparate(src, dst, GL11.GL_ONE, GL11.GL_ZERO);
    }

    public static void glBlendFuncSeparate(int src, int dst, int srcAlpha, int dstAlpha) {
    	if (useGlBlendFuncSeparate) {
            GL14.glBlendFuncSeparate(src, dst, srcAlpha, dstAlpha);
        } else {
            GL11.glBlendFunc(src, dst);
        }
    }

    public static void glAlphaFunc(int func, float ref) {
    	GL11.glAlphaFunc(func, ref);
    }

    public static void glDepthFunc(int func) {
    	GL11.glDepthFunc(func);
    }

    public static void glDepthMask(boolean enable) {
    	GL11.glDepthMask(enable);
    }

    public static void glColor4f(float r, float g, float b, float a) {
    	GL11.glColor4f(r, g, b, a);
    }

    public static void glClearColor(float r, float g, float b, float a) {
    	GL11.glClearColor(r, g, b, a);
    }

    public static int getBoundTexture() {
        return GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
    }

    public static void deleteTexture(int texture) {
        if (texture >= 0) {
            GL11.glDeleteTextures(texture);
        }
    }
}
