package moddedmite.mcpatcher.mixin.mcpatcherforge.cc.block;

import net.minecraft.Block;
import net.minecraft.BlockReed;
import net.minecraft.IBlockAccess;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.prupe.mcpatcher.cc.ColorizeBlock;

@Mixin(BlockReed.class)
public abstract class MixinBlockReed {

//    @Inject(method = "colorMultiplier(Lnet/minecraft/IBlockAccess;III)I", at = @At("HEAD"), cancellable = true)
//    private void modifyColorMultiplier(IBlockAccess worldIn, int x, int y, int z, CallbackInfoReturnable<Integer> cir) {
//        if (ColorizeBlock.colorizeBlock((Block) (Object) this, worldIn, x, y, z)) {
//            cir.setReturnValue(ColorizeBlock.blockColor);
//        }
//    }
}
