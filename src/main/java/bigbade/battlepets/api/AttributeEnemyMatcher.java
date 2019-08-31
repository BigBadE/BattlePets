package bigbade.battlepets.api;

import bigbade.battlepets.ai.IEntityMatcher;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.LivingEntity;

public class AttributeEnemyMatcher implements IEntityMatcher {
    public AttributeEnemyMatcher(CreatureAttribute theAttr) {
        attr = theAttr;
    }

    public boolean matches(LivingEntity entity) {
        return entity.getCreatureAttribute() == attr;
    }

    public final CreatureAttribute attr;
}

