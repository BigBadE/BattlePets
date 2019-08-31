package bigbade.battlepets.ai;

import bigbade.battlepets.entities.PetEntity;
import bigbade.battlepets.skills.Skill;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.List;

public class PickupItemsGoal extends Goal {
    private PetEntity thePet;
    private LivingEntity theOwner;
    World theWorld;
    private double field_75336_f;
    private GoalSelector petPathfinder;
    private int field_75343_h;
    private boolean field_75344_i;

    public PickupItemsGoal(PetEntity pet, double par2) {
        this.thePet = pet;
        this.theWorld = pet.getEntityWorld();
        this.field_75336_f = par2;
        this.petPathfinder = pet.getGoalSelector();
        this.setMutexFlags(EnumSet.of(Flag.MOVE));
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        LivingEntity entitylivingbase = thePet.getEntityWorld().getPlayerByUuid(this.thePet.getOwnerUUID());

        if (entitylivingbase == null) {
            return false;
        } else if (this.thePet.isSitting()) {
            return false;
        } else if (!thePet.hasSkill(Skill.INVENTORY_PICKUP.id)) {
            return false;
        }/*
        else if (this.thePet.getDistanceSqToEntity(entitylivingbase) < (double)(this.minDist * this.minDist))
        {
            return false;
        }*/ else {
            this.theOwner = entitylivingbase;
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting() {
        return this.thePet.hasPath() && !this.thePet.isSitting();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.field_75343_h = 0;
        this.field_75344_i = false;
        thePet.getGoalSelector().getRunningGoals().forEach((goal) -> {
            if (goal.func_220772_j() instanceof WaterAvoidingRandomWalkingGoal) field_75344_i = true;
        });
        thePet.getGoalSelector().addGoal(7, new WaterAvoidingRandomWalkingGoal(thePet, 0.6));
    }

    /**
     * Resets the task
     */
    public void resetTask() {
        this.theOwner = null;
        this.thePet.getNavigator().clearPath();
        if (field_75344_i)
            thePet.getGoalSelector().addGoal(7, new WaterAvoidingRandomWalkingGoal(thePet, 0.6));
    }

    /**
     * Updates the task
     */
    public void updateTask() {
        if (!this.thePet.isSitting()) {
            if (--this.field_75343_h <= 0) {
                this.field_75343_h = 10;

                final double R = 15;
                List nearby = theWorld.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(thePet.posX - R, thePet.posY - R, thePet.posZ - R, thePet.posX + R, thePet.posY + R, thePet.posZ + R));
                ItemEntity closest = null;
                for (Object obj : nearby) {
                    ItemEntity item = (ItemEntity) obj;
                    /*if (thePet.hasRoomForItem(item.getItem()) <= 0) {
                        continue;
                    }*/

                    if (closest == null) closest = item;
                    else if (thePet.getDistanceSq(item) < thePet.getDistanceSq(closest)) {
                        closest = item;
                    }
                }


                if (closest != null && !this.thePet.getNavigator().tryMoveToXYZ(closest.posX, closest.posY, closest.posZ, this.field_75336_f)) {
                    if (!this.thePet.getLeashed()) {
                        if (this.thePet.getDistanceSq(this.theOwner) >= 144.0D || thePet.posY < 0) {
                            int i = floor_double(this.theOwner.posX) - 2;
                            int j = floor_double(this.theOwner.posZ) - 2;
                            int k = floor_double(this.theOwner.getBoundingBox().minY);

                            for (int l = 0; l <= 4; ++l) {
                                for (int i1 = 0; i1 <= 4; ++i1) {
                                    if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && !this.theWorld.getBlockState(new BlockPos(i + l, k - 1, j + i1)).getMaterial().blocksMovement() && !this.theWorld.getBlockState(new BlockPos(i + l, k, j + i1)).getMaterial().blocksMovement() && !this.theWorld.getBlockState(new BlockPos(i + l, k + 1, j + i1)).getMaterial().blocksMovement()) {
                                        {
                                            this.thePet.setLocationAndAngles((float) (i + l) + 0.5F, k, (float) (j + i1) + 0.5F, this.thePet.rotationYaw, this.thePet.rotationPitch);
                                            this.thePet.getNavigator().clearPath();
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private int floor_double(double value) {
        int i = (int) value;
        return value < (double)i ? i - 1 : i;
    }
}
