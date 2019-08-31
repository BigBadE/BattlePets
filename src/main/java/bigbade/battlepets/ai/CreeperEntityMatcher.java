package bigbade.battlepets.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.CreeperEntity;

public class CreeperEntityMatcher implements IEntityMatcher {
    public boolean matches( LivingEntity entity )
    {
        return ( entity instanceof CreeperEntity);
    }
}
