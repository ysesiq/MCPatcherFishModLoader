package com.prupe.mcpatcher.mal.resource;

import com.prupe.mcpatcher.MCPatcherUtils;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ResourceLocation;
import net.minecraft.ResourcePack;

import java.util.Comparator;
import java.util.regex.Pattern;

@Environment(EnvType.CLIENT)
public class ResourceLocationWithSource extends ResourceLocation {
    private final ResourcePack source;
    private final int order;
    private final boolean isDirectory;

    public ResourceLocationWithSource(ResourcePack source, ResourceLocation resource) {
        super(resource.getResourceDomain(), resource.getResourcePath().replaceFirst("/$", ""));
        this.source = source;
        order = ResourceList.getResourcePackOrder(source);
        isDirectory = resource.getResourcePath().endsWith("/");
    }

    public ResourcePack getSource() {
        return source;
    }

    public int getOrder() {
        return order;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    @Environment(EnvType.CLIENT)
    static class Comparator1 implements Comparator<ResourceLocationWithSource> {
        private final boolean bySource;
        private final String suffixExpr;

        Comparator1() {
            this(false, null);
        }

        Comparator1(boolean bySource, String suffix) {
            this.bySource = bySource;
            this.suffixExpr = MCPatcherUtils.isNullOrEmpty(suffix) ? null : Pattern.quote(suffix) + "$";
        }

        @Override
        public int compare(ResourceLocationWithSource o1, ResourceLocationWithSource o2) {
            int result;
            if (bySource) {
                result = o1.getOrder() - o2.getOrder();
                if (result != 0) {
                    return result;
                }
            }
            String n1 = o1.getResourceDomain();
            String n2 = o2.getResourceDomain();
            result = n1.compareTo(n2);
            if (result != 0) {
                return result;
            }
            String p1 = o1.getResourcePath();
            String p2 = o2.getResourcePath();
            if (suffixExpr != null) {
                String f1 = p1.replaceAll(".*/", "").replaceFirst(suffixExpr, "");
                String f2 = p2.replaceAll(".*/", "").replaceFirst(suffixExpr, "");
                result = f1.compareTo(f2);
                if (result != 0) {
                    return result;
                }
            }
            return p1.compareTo(p2);
        }
    }
}
// ---END EDIT---
