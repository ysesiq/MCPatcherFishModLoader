package moddedmite.mcpatcher.mixin.mcpatcherforge.cit.world;

import jss.notfine.util.itembreakparticles.IRenderGlobalSpawnItemBreakParticle;
import jss.notfine.util.itembreakparticles.IWorldSpawnItemBreakParticle;
import net.minecraft.EnumParticle;
import net.minecraft.ItemStack;
import net.minecraft.IWorldAccess;
import net.minecraft.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(value = World.class)
public abstract class MixinWorld implements IWorldSpawnItemBreakParticle {

    public void spawnItemBreakParticle(ItemStack itemStack, String particleName, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        for (int i = 0; i < worldAccesses.size(); ++i) {
            IWorldAccess access = (IWorldAccess)worldAccesses.get(i);
            if (access instanceof IRenderGlobalSpawnItemBreakParticle) {
                ((IRenderGlobalSpawnItemBreakParticle)access).spawnItemBreakParticle(itemStack, x, y, z, velocityX, velocityY, velocityZ);
            } else {
                access.spawnParticle(EnumParticle.valueOf(particleName), x, y, z, velocityX, velocityY, velocityZ);
            }
        }
    }

    @Shadow
    protected List worldAccesses;

}
