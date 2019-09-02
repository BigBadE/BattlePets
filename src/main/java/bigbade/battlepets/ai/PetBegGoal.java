package bigbade.battlepets.ai;

import bigbade.battlepets.entities.PetEntity;
import bigbade.battlepets.skills.FoodSkill;
import bigbade.battlepets.skills.Skill;
import net.minecraft.block.PotatoBlock;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.EnumSet;

public class PetBegGoal extends Goal {
    private final PetEntity wolf;
    private PlayerEntity player;
    private final World world;
    private final float minPlayerDistance;
    private int timeoutCounter;
    private final EntityPredicate field_220688_f;

    public PetBegGoal(PetEntity wolf, float minDistance) {
        this.wolf = wolf;
        this.world = wolf.world;
        this.minPlayerDistance = minDistance;
        this.field_220688_f = (new EntityPredicate()).setDistance((double) minDistance).allowInvulnerable().allowFriendlyFire().setSkipAttackChecks();
        this.setMutexFlags(EnumSet.of(Goal.Flag.LOOK));
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        this.player = this.world.func_217370_a(this.field_220688_f, this.wolf);
        return this.player == null ? false : this.hasTemptationItemInHand(this.player);
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting() {
        if (!this.player.isAlive()) {
            return false;
        } else if (this.wolf.getDistanceSq(this.player) > (double) (this.minPlayerDistance * this.minPlayerDistance)) {
            return false;
        } else {
            return this.timeoutCounter > 0 && this.hasTemptationItemInHand(this.player);
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.wolf.setBegging(true);
        this.timeoutCounter = 40 + this.wolf.getRNG().nextInt(40);
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask() {
        this.wolf.setBegging(false);
        this.player = null;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        this.wolf.getLookController().setLookPosition(this.player.posX, this.player.posY + (double) this.player.getEyeHeight(), this.player.posZ, 10.0F, (float) this.wolf.getVerticalFaceSpeed());
        --this.timeoutCounter;
    }

    /**
     * Gets if the Player has the Bone in the hand.
     */
    private boolean hasTemptationItemInHand(PlayerEntity player) {
        for (Hand hand : Hand.values()) {
            ItemStack itemstack = player.getHeldItem(hand);
            if (itemstack.getItem() == Items.BONE) {
                return true;
            }

            if (itemstack.getItem().isFood()) {
                boolean canEat = false;
                for (int id : wolf.getSkills()) {
                    Skill skill = Skill.forId(id);
                    if (!(skill instanceof FoodSkill)) {
                        continue;
                    }
                    FoodSkill foodSkill = (FoodSkill) skill;

                    if (foodSkill.type.doesMatch(wolf.getPetType(), itemstack)) {
                        canEat = true;
                        break;
                    }
                }

                return canEat;
            }
        }

        return false;
    }
}
