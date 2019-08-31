package bigbade.battlepets.ai;

import bigbade.battlepets.entities.PetEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;

import java.util.EnumSet;

public class SitGoal extends Goal {
    public SitGoal(PetEntity thePet) {
        pet = thePet;
        setMutexFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        if (pet.isInWater()) {
            return false;
        } else if (!pet.onGround) {
            return false;
        } else {
            LivingEntity entitylivingbase = pet.getEntityWorld().getPlayerByUuid(pet.getOwnerUUID());
            return entitylivingbase == null ? true : (pet.getDistanceSq(entitylivingbase) < 144.0D && entitylivingbase.getRevengeTarget() != null ? false : sitting);
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        pet.getNavigator().clearPath();
    }

    @Override
    public void resetTask() {
    }


    public void setSitting(boolean par1) {
        this.sitting = par1;
    }

    private PetEntity pet;
    private boolean sitting = false;
}
