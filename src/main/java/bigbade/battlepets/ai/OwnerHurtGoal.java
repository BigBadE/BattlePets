package bigbade.battlepets.ai;

import bigbade.battlepets.entities.PetEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.EnumSet;

public class OwnerHurtGoal extends TargetGoal {
    private final PetEntity tameable;
    private LivingEntity attacker;
    private int timestamp;

    public OwnerHurtGoal(PetEntity pet) {
        super(pet, false);
        this.tameable = pet;
        this.setMutexFlags(EnumSet.of(Goal.Flag.TARGET));
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        if (!this.tameable.isSitting()) {
            LivingEntity livingentity = tameable.getEntityWorld().getPlayerByUuid(this.tameable.getOwnerUUID());
            if (livingentity == null) {
                return false;
            } else {
                this.attacker = livingentity.getRevengeTarget();
                int i = livingentity.getRevengeTimer();
                return i != this.timestamp && this.func_220777_a(this.attacker, EntityPredicate.DEFAULT) && shouldAttackEntity(this.attacker, livingentity);
            }
        } else {
            return false;
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.field_75299_d.setAttackTarget(this.attacker);
        LivingEntity livingentity = tameable.getEntityWorld().getPlayerByUuid(this.tameable.getOwnerUUID());
        if (livingentity != null) {
            this.timestamp = livingentity.getRevengeTimer();
        }

        super.startExecuting();
    }

    private boolean shouldAttackEntity(LivingEntity target, LivingEntity owner) {
        if (!(target instanceof CreeperEntity) && !(target instanceof GhastEntity)) {
            if (target instanceof WolfEntity) {
                WolfEntity wolfentity = (WolfEntity)target;
                if (wolfentity.isTamed() && wolfentity.getOwner() == owner) {
                    return false;
                }
            }

            if (target instanceof PlayerEntity && owner instanceof PlayerEntity && !((PlayerEntity)owner).canAttackPlayer((PlayerEntity)target)) {
                return false;
            } else if (target instanceof AbstractHorseEntity && ((AbstractHorseEntity)target).isTame()) {
                return false;
            } else {
                return !(target instanceof CatEntity) || !((CatEntity)target).isTamed();
            }
        } else {
            return false;
        }
    }
}
