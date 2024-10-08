package moddedmite.mcpatcher.mixin.mcpatcherforge.cc.potion;

import net.minecraft.PotionHelper;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import com.prupe.mcpatcher.cc.ColorizeItem;

@Mixin(PotionHelper.class)
public abstract class MixinPotionHelper {

    @ModifyConstant(method = "calcPotionLiquidColor(Ljava/util/Collection;)I", constant = @Constant(intValue = 3694022))
    private static int modifyCalcPotionLiquidColor(int constant) {
        return ColorizeItem.getWaterBottleColor();
    }
}
