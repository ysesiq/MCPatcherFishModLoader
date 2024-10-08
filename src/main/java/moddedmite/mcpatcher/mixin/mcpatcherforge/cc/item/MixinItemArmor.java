package moddedmite.mcpatcher.mixin.mcpatcherforge.cc.item;

import net.minecraft.ItemArmor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import com.prupe.mcpatcher.cc.ColorizeEntity;

@Mixin(ItemArmor.class)
public abstract class MixinItemArmor {

    @ModifyConstant(method = "getColor(Lnet/minecraft/ItemStack;)I", constant = @Constant(intValue = 10511680))
    private int modifyGetColor(int input) {
        return ColorizeEntity.undyedLeatherColor;
    }
}
