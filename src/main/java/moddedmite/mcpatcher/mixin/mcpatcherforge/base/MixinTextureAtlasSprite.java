package moddedmite.mcpatcher.mixin.mcpatcherforge.base;

import java.util.List;

import net.minecraft.TextureAtlasSprite;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TextureAtlasSprite.class)
public abstract class MixinTextureAtlasSprite {

    // In 1.6 this is a List<int[]>
    @Shadow
    public List<int[]> framesTextureData;

    /**
     * @author Mist475 (adapted from Paul Rupe)
     * @reason null check?
     */
    @Overwrite
    public final int getFrameCount() {
        if (this.framesTextureData != null) {
            return this.framesTextureData.size();
        }
        return 1;
    }
}
