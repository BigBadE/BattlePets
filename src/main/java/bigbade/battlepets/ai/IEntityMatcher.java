package bigbade.battlepets.ai;

import net.minecraft.entity.LivingEntity;

public interface IEntityMatcher {
    public boolean matches(LivingEntity entity);
}
