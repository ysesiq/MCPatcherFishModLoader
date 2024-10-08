package moddedmite.mcpatcher.mixin.mcpatcherforge.cc.world;

import net.minecraft.EntityLivingBase;
import net.minecraft.Vec3;
import net.minecraft.WorldProvider;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.prupe.mcpatcher.cc.ColorizeWorld;
import com.prupe.mcpatcher.cc.Colorizer;

@Mixin(WorldProvider.class)
public abstract class MixinWorldProvider {

    @Inject(method = "getFogColor", at = @At("HEAD"))
    private void modifyGetFogColor1(float par1, float par2, EntityLivingBase viewer, CallbackInfoReturnable<Vec3> cir, @Share("computeFogColor") LocalBooleanRef computeFogColor) {
        computeFogColor.set(ColorizeWorld.computeFogColor((WorldProvider) (Object) this, par1));
    }

    @ModifyConstant(method = "getFogColor", constant = @Constant(floatValue = 0.7529412F))
    private float modifyGetFogColor1(float constant, @Share("computeFogColor") LocalBooleanRef computeFogColor) {
        if (computeFogColor.get()) {
            return Colorizer.setColor[0];
        }
        return constant;
    }

    @ModifyConstant(method = "getFogColor", constant = @Constant(floatValue = 0.84705883F))
    private float modifyGetFogColor2(float constant, @Share("computeFogColor") LocalBooleanRef computeFogColor) {
        if (computeFogColor.get()) {
            return Colorizer.setColor[1];
        }
        return constant;
    }

    @ModifyConstant(
        method = "getFogColor",
        constant = @Constant(floatValue = 1.0F, ordinal = 2))
    private float modifyGetFogColor3(float constant, @Share("computeFogColor") LocalBooleanRef computeFogColor) {
        if (computeFogColor.get()) {
            return Colorizer.setColor[2];
        }
        return constant;
    }
}
