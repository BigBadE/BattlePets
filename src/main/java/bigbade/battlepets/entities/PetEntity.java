package bigbade.battlepets.entities;

import bigbade.battlepets.ai.FollowOwnerGoal;
import bigbade.battlepets.ai.SitGoal;
import bigbade.battlepets.ai.*;
import bigbade.battlepets.api.Level;
import bigbade.battlepets.api.PetType;
import bigbade.battlepets.containers.PetContainer;
import bigbade.battlepets.containers.PetInventoryContainer;
import bigbade.battlepets.items.ConverterItem;
import bigbade.battlepets.registries.EntityRegistry;
import bigbade.battlepets.skills.AttackSkill;
import bigbade.battlepets.skills.FoodSkill;
import bigbade.battlepets.skills.Skill;
import com.google.common.primitives.Ints;
import io.netty.buffer.Unpooled;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class PetEntity extends AnimalEntity implements IJumpingMount, INamedContainerProvider {
    private static DataParameter<Integer> level = EntityDataManager.createKey(PetEntity.class, DataSerializers.VARINT);
    private static DataParameter<Integer> skillPoints = EntityDataManager.createKey(PetEntity.class, DataSerializers.VARINT);
    private static DataParameter<Integer> type = EntityDataManager.createKey(PetEntity.class, DataSerializers.VARINT);
    private static DataParameter<Integer> collar = EntityDataManager.createKey(PetEntity.class, DataSerializers.VARINT);
    private static DataParameter<Boolean> sitting = EntityDataManager.createKey(PetEntity.class, DataSerializers.BOOLEAN);
    private static DataParameter<Optional<UUID>> ownerUUID = EntityDataManager.createKey(PetEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    private static DataParameter<String> texture = EntityDataManager.createKey(PetEntity.class, DataSerializers.STRING);
    private PetInventoryContainer inv;
    private float width;
    private float height;
    private List<Integer> skills;

    private float hunger;
    private float saturation;

    private float jumpPower;

    public PetEntity(World worldIn, PetType type, UUID owner) {
        super(EntityRegistry.PETENTITY, worldIn);

        setSize(type.width, type.height);
        inv = new PetInventoryContainer(this);

        skills = Ints.asList(type.skills);

        dataManager.set(PetEntity.type, type.ordinal());

        dataManager.set(ownerUUID, Optional.ofNullable(owner));

        dataManager.set(texture, type.textures[0]);
        goalSelector.addGoal(1, new SwimGoal(this));
        goalSelector.addGoal(2, new SitGoal(this));
        goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
        goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, true));
        goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.0D, 10.0F, 3.5F));
        goalSelector.addGoal(6, new PickupItemsGoal(this, 1.0D));
        goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 0.6D));
        goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 9.0F));
        if (type == PetType.DOG)
            this.goalSelector.addGoal(9, new PetBegGoal(this, 8.0F));
        targetSelector.addGoal(1, new OwnerHurtGoal(this));
        targetSelector.addGoal(3, new OwnerAttackGoal(this));
        targetSelector.addGoal(2, new HurtByTargetGoal(this));
        targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractSkeletonEntity.class, false));
    }

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3F);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return PetType.values()[dataManager.get(type)].getLivingSound();
    }

    @Override
    public int getTalkInterval() {
        return 900;
    }

    @Override
    public void registerData() {
        super.registerData();

        dataManager.register(level, 0);
        dataManager.register(skillPoints, 1);
        dataManager.register(sitting, false);

        dataManager.register(type, 0);
        dataManager.register(ownerUUID, Optional.empty());
        dataManager.register(texture, PetType.DOG.textures[0]);
        dataManager.register(collar, DyeColor.RED.getId());
    }

    //TODO add breedable pets
    @Nullable
    @Override
    public AgeableEntity createChild(AgeableEntity ageable) {
        return null;
    }

    public void setOwnerUUID(UUID uuid) {
        dataManager.set(ownerUUID, Optional.of(uuid));
    }

    public void setPetType(PetType type) {
        dataManager.set(this.type, type.ordinal());
    }

    public void setSitting(boolean sitting) {
        System.out.println("Sitting: " + sitting);
        dataManager.set(this.sitting, sitting);
    }

    public void setCustomName(String name) {
        setCustomName(new StringTextComponent(name));
    }

    public void setLevel(int level) {
        dataManager.set(this.level, level);
    }

    public void setFreeSkillPoints(int points) {
        dataManager.set(this.skillPoints, points);
    }

    public int getLevel() {
        return dataManager.get(level);
    }

    public int getFreeSkillPoints() {
        return dataManager.get(skillPoints);
    }

    public float getHunger() {
        return hunger;
    }

    public float getSaturation() {
        return saturation;
    }

    public boolean isSitting() {
        return dataManager.get(sitting);
    }

    public UUID getOwnerUUID() {
        return dataManager.get(ownerUUID).get();
    }

    public GoalSelector getGoalSelector() {
        return goalSelector;
    }

    public PetType getPetType() {
        return PetType.values()[dataManager.get(type)];
    }

    public List<Integer> getSkills() {
        return skills;
    }

    public void setHunger(float hunger) {
        this.hunger = hunger;
    }

    public void setSaturation(float saturation) {
        this.saturation = saturation;
    }

    public String getTexture() {
        StringBuilder builder = new StringBuilder("textures/entity/");
        PetType petType = PetType.values()[dataManager.get(type)];
        builder.append(petType.name().toLowerCase()).append("/");
        if (petType == PetType.DOG) {
            if (angry) builder.append("angry-");
            else builder.append("tame-");
        }
        builder.append(dataManager.get(texture)).append(".png");
        return builder.toString();
    }

    public void setTexture(String texture) {
        dataManager.set(PetEntity.texture, texture);
    }

    public boolean hasSkill(int id) {
        return skills.contains(id);
    }

    public void useHunger(float amount) {
        float satDiff = Math.min(amount, saturation);
        saturation -= satDiff;
        if (satDiff != amount) {
            hunger = getHunger() - (amount - satDiff);
        }
    }

    public void levelUp() {
        if (getLevel() >= Level.MAX_LEVEL) {
            return;
        }

        setLevel(getLevel() + 1);
        setFreeSkillPoints(getFreeSkillPoints() + 1);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        compound.putInt("level", dataManager.get(level));
        compound.putInt("skillPoints", dataManager.get(skillPoints));
        compound.putInt("petType", dataManager.get(type));
        compound.putInt("collar", dataManager.get(collar));

        compound.putBoolean("sitting", dataManager.get(sitting));

        compound.putUniqueId("owner", dataManager.get(ownerUUID).get());

        compound.putString("texture", dataManager.get(texture));
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        dataManager.set(level, compound.getInt("level"));
        dataManager.set(skillPoints, compound.getInt("skillPoints"));
        dataManager.set(type, compound.getInt("petType"));
        dataManager.set(collar, compound.getInt("collar"));

        dataManager.set(sitting, compound.getBoolean("sitting"));

        dataManager.set(ownerUUID, Optional.of(compound.getUniqueId("owner")));

        dataManager.set(texture, compound.getString("texture"));
    }

    public void fall(float distance, float damageMultiplier) {
        if (distance > 1.0F) {
            this.playSound(SoundEvents.ENTITY_HORSE_LAND, 0.4F, 1.0F);
        }

        int i = MathHelper.ceil((distance * 0.5F - 3.0F) * damageMultiplier);
        if (i > 0 && !hasSkill(Skill.DEFENSE_FEATHERFALL.id)) {
            this.attackEntityFrom(DamageSource.FALL, (float) i);
            if (this.isBeingRidden()) {
                for (Entity entity : this.getRecursivePassengers()) {
                    entity.attackEntityFrom(DamageSource.FALL, (float) i);
                }
            }

            BlockState blockstate = this.world.getBlockState(new BlockPos(this.posX, this.posY - 0.2D - (double) this.prevRotationYaw, this.posZ));
            if (!blockstate.isAir() && !this.isSilent()) {
                SoundType soundtype = blockstate.getSoundType();
                this.world.playSound(null, this.posX, this.posY, this.posZ, soundtype.getStepSound(), this.getSoundCategory(), soundtype.getVolume() * 0.5F, soundtype.getPitch() * 0.75F);
            }

        }
    }

    @Override
    public boolean processInteract(PlayerEntity player, Hand hand) {
        //TODO open pet inventory
        /*if (player.isSneaking() && held.getItem() instanceof ConverterItem) {
            if (player.getUniqueID().equals(getOwnerUUID())) {
                BattlePets.proxy.setPendingPetForGui((world.isRemote ? 0 : 1), this);
                player.openContainer(UsefulPets.instance, UsefulPets.PET_INVENTORY_GUI_ID, world, 0, 0, 0);
            } else {
                player.sendMessage(new TranslationTextComponent("chat.pet.notYours"));
            }
            return false;
        }*/

        if (world.isRemote) {
            return false;
        }

        ItemStack held = player.getHeldItem(Hand.MAIN_HAND);
        if (held.getItem().isFood()) {
            Food food = held.getItem().getFood();

            boolean canEat = false;
            for (int id : skills) {
                Skill skill = Skill.forId(id);
                if (!(skill instanceof FoodSkill)) {
                    continue;
                }
                FoodSkill foodSkill = (FoodSkill) skill;

                if (foodSkill.type.doesMatch(getPetType(), held)) {
                    canEat = true;
                    break;
                }
            }

            if (canEat) {
                setHunger(getHunger() + food.getHealing());
                setSaturation(getSaturation() + food.getSaturation());

                if (!player.isCreative()) {
                    held.shrink(1);
                }
                return true;
            }
        } else if (held.getItem() instanceof ConverterItem) {
            player.sendMessage(new TranslationTextComponent("chat.battlepets.pet.help.level"));
            player.sendMessage(new TranslationTextComponent("chat.battlepets.pet.help.inv"));
        } else if (held.getItem() == Items.AIR && !player.isSneaking() /*&& hasSkill(Skill.TRAVEL_MOUNTABLE.id) && hasSaddle()*/) {
            if (player.getUniqueID().equals(getOwnerUUID())) {
                player.rotationYaw = this.rotationYaw;
                player.rotationPitch = this.rotationPitch;
                player.startRiding(this);
            } else {
                player.sendMessage(new TranslationTextComponent("chat.battlepets.pet.notYours"));
            }
        } else if (held.getItem() instanceof DyeItem) {
            if (player.getUniqueID().equals(getOwnerUUID())) {
                DyeColor dyecolor = ((DyeItem) held.getItem()).getDyeColor();
                if (dyecolor.getId() != this.getCollar()) {
                    dataManager.set(collar, dyecolor.getId());
                    if (!player.abilities.isCreativeMode) {
                        held.shrink(1);
                    }

                    return true;
                }
            } else {
                player.sendMessage(new TranslationTextComponent("chat.battlepets.pet.notYours"));
            }
        } else {
            if (player.getUniqueID().equals(getOwnerUUID())) {
                if (hand == Hand.MAIN_HAND)
                    setSitting(!isSitting());
            } else {
                player.sendMessage(new TranslationTextComponent("chat.battlepets.pet.notYours"));
            }
        }

        super.processInteract(player, hand);
        return false;
    }

    private void setSize(float width, float height) {
        if (width != this.width || height != this.height) {
            float f = this.width;
            this.width = width;
            this.height = height;
            this.setBoundingBox(new AxisAlignedBB(this.getBoundingBox().minX, this.getBoundingBox().minY, this.getBoundingBox().minZ, this.getBoundingBox().minX + (double) this.width, this.getBoundingBox().minY + (double) this.height, this.getBoundingBox().minZ + (double) this.width));

            if (this.width > f && !this.firstUpdate && !this.getEntityWorld().isRemote) {
                this.move(MoverType.SELF, new Vec3d(f - this.width, 0.0D, f - this.width));
            }
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        int damage = 0;
        for (int id : skills) {
            Skill skill = Skill.forId(id);
            if (!(skill instanceof AttackSkill)) {
                continue;
            }
            AttackSkill attack = (AttackSkill) skill;

            damage += attack.damage;
        }

        if (hasSkill(Skill.INVENTORY_WEAPON.id) && inv.getStackInSlot(2) != null) {
            ItemStack stack = inv.getStackInSlot(2);
            //TODO claw
            /*if (stack.getItem() instanceof ClawItem) {
                ClawItem claw = (ClawItem) stack.getItem();
                damage += claw.damage;
            }*/
        }

        return entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float) damage);
    }

    @Override
    public int getTotalArmorValue() {
        if (!hasSkill(Skill.INVENTORY_ARMOR.id) || inv.getStackInSlot(1) == null) {
            return 0;
        }

        ItemStack stack = inv.getStackInSlot(1);
        if (stack.getItem() == Items.IRON_HORSE_ARMOR) {
            return 5;
        } else if (stack.getItem() == Items.GOLDEN_HORSE_ARMOR) {
            return 7;
        } else if (stack.getItem() == Items.DIAMOND_HORSE_ARMOR) {
            return 11;
        }

        return 0;
    }

    @Override
    protected boolean isMovementBlocked() {
        return isSitting();
    }

    @Override
    public void setJumping(boolean jumping) {
        super.setJumping(jumping);
        if (jumping)
            useHunger(0.025f);
    }

    @Override
    public void travel(Vec3d movement) {
        //TODO saddle
        if (this.isBeingRidden() && hasSkill(Skill.TRAVEL_MOUNTABLE.id) && hasSaddle() && !isSitting()/* && this.func_110257_ck()*/) {
            LivingEntity livingentity = (LivingEntity) this.getPassengers().get(0);
            this.rotationYaw = livingentity.rotationYaw;
            this.prevRotationYaw = this.rotationYaw;
            this.rotationPitch = livingentity.rotationPitch * 0.5F;
            this.setRotation(this.rotationYaw, this.rotationPitch);
            this.renderYawOffset = this.rotationYaw;
            this.rotationYawHead = this.renderYawOffset;
            float f = livingentity.moveStrafing * 0.5F;
            float f1 = livingentity.moveForward;
            if (f1 <= 0.0F) {
                f1 *= 0.25F;
            }

            double d0 = 0;

            if (this.jumpPower > 0 && !this.isJumping && onGround) {
                d0 = 0.85f * (double) this.jumpPower;
                if (this.isPotionActive(Effects.JUMP_BOOST)) {
                    d0 += (double) ((float) (this.getActivePotionEffect(Effects.JUMP_BOOST).getAmplifier() + 1) * 0.1F);
                }

                Vec3d vec3d = this.getMotion();
                //this.setMotion(vec3d.x, d0, vec3d.z);
                this.setJumping(true);
                this.isAirBorne = true;
                if (f1 > 0.0F) {
                    float f2 = MathHelper.sin(this.rotationYaw * ((float) Math.PI / 180F));
                    float f3 = MathHelper.cos(this.rotationYaw * ((float) Math.PI / 180F));
                    this.setMotion(this.getMotion().add(-0.4F * f2 * this.jumpPower, 0.0D, (0.4F * f3 * this.jumpPower)));
                    this.playSound(SoundEvents.ENTITY_HORSE_JUMP, 0.4F, 1.0F);
                }

                this.jumpPower = 0.0F;
            }

            this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;
            if (this.canPassengerSteer()) {
                this.setAIMoveSpeed((float) this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue());
                super.travel(new Vec3d(f, movement.y + d0, f1));
            } else if (livingentity instanceof PlayerEntity) {
                this.setMotion(Vec3d.ZERO);
            }

            if (this.onGround) {
                this.jumpPower = 0.0F;
                this.setJumping(false);
            }

            this.prevLimbSwingAmount = this.limbSwingAmount;
            double d2 = this.posX - this.prevPosX;
            double d3 = this.posZ - this.prevPosZ;
            float f4 = MathHelper.sqrt(d2 * d2 + d3 * d3) * 4.0F;
            if (f4 > 1.0F) {
                f4 = 1.0F;
            }

            this.limbSwingAmount += (f4 - this.limbSwingAmount) * 0.4F;
            this.limbSwing += this.limbSwingAmount;
        } else {
            this.jumpMovementFactor = 0.02F;
            super.travel(movement);
        }
    }

    @Override
    public void setJumpPower(int jumpPowerIn) {
        this.jumpPower = jumpPowerIn;
        if (jumpPowerIn < 0) {
            jumpPowerIn = 0;
        } else {
        }

        if (jumpPowerIn >= 90) {
            this.jumpPower = 1.0F;
        } else {
            this.jumpPower = 0.4F + 0.4F * (float) jumpPowerIn / 90.0F;
        }
    }

    @Override
    public boolean canJump() {
        return !hasSkill(Skill.TRAVEL_MOUNTJUMP.id);
    }

    @Override
    public void handleStartJump(int jumpPower) {
        setJumping(true);
    }

    @Override
    public void handleStopJump() {
        setJumping(false);
    }

    public void updatePassenger(Entity passenger) {
        super.updatePassenger(passenger);
        if (passenger instanceof MobEntity) {
            MobEntity mobentity = (MobEntity) passenger;
            this.renderYawOffset = mobentity.renderYawOffset;
        }

    }

    @Override
    public boolean canBeSteered() {
        return true;
    }

    public PetInventoryContainer getPetInventory() {
        return inv;
    }

    public boolean hasSaddle() {
        return inv.getStackInSlot(0).getItem() == Items.SADDLE;
    }

//Wolf methods

    private boolean isWet = false;
    private boolean isShaking = false;
    private float timeWolfIsShaking;
    private float prevTimeWolfIsShaking;
    private float headRotationCourse;
    private float headRotationCourseOld;
    private boolean begging;
    private boolean angry;

    public int getCollar() {
        return dataManager.get(collar);
    }

    public boolean getBegging() {
        return begging;
    }

    public void setBegging(boolean begging) {
        this.begging = begging;
    }

    public boolean getAngry() {
        return angry;
    }

    public void setAngry(boolean angry) {
        this.angry = angry;
    }

    @Override
    public void setAttackTarget(@Nullable LivingEntity entitylivingbaseIn) {
        super.setAttackTarget(entitylivingbaseIn);
        if (entitylivingbaseIn == null) {
            this.setAngry(false);
        } else {
            this.setAngry(true);
        }

    }

    @Override
    public void livingTick() {
        super.livingTick();
        if (dataManager.get(type) == PetType.DOG.ordinal() && !this.world.isRemote && this.isWet && !this.isShaking && !this.hasPath() && this.onGround) {
            this.isShaking = true;
            this.timeWolfIsShaking = 0.0F;
            this.prevTimeWolfIsShaking = 0.0F;
            this.world.setEntityState(this, (byte) 8);
        }

        if (!this.world.isRemote && this.getAttackTarget() == null && getAngry()) {
            this.setAngry(false);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isAlive()) {
            this.headRotationCourseOld = this.headRotationCourse;
            if (begging) {
                this.headRotationCourse += (1.0F - this.headRotationCourse) * 0.4F;
            } else {
                this.headRotationCourse += (0.0F - this.headRotationCourse) * 0.4F;
            }

            if (this.isInWaterRainOrBubbleColumn()) {
                this.isWet = true;
                this.isShaking = false;
                this.timeWolfIsShaking = 0.0F;
                this.prevTimeWolfIsShaking = 0.0F;
            } else if ((this.isWet || this.isShaking) && this.isShaking) {
                if (this.timeWolfIsShaking == 0.0F) {
                    this.playSound(SoundEvents.ENTITY_WOLF_SHAKE, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
                }

                this.prevTimeWolfIsShaking = this.timeWolfIsShaking;
                this.timeWolfIsShaking += 0.05F;
                if (this.prevTimeWolfIsShaking >= 2.0F) {
                    this.isWet = false;
                    this.isShaking = false;
                    this.prevTimeWolfIsShaking = 0.0F;
                    this.timeWolfIsShaking = 0.0F;
                }

                if (this.timeWolfIsShaking > 0.4F) {
                    float f = (float) this.getBoundingBox().minY;
                    int i = (int) (MathHelper.sin((this.timeWolfIsShaking - 0.4F) * (float) Math.PI) * 7.0F);
                    Vec3d vec3d = this.getMotion();

                    for (int j = 0; j < i; ++j) {
                        float f1 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.getWidth() * 0.5F;
                        float f2 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.getWidth() * 0.5F;
                        this.world.addParticle(ParticleTypes.SPLASH, this.posX + (double) f1, (double) (f + 0.8F), this.posZ + (double) f2, vec3d.x, vec3d.y, vec3d.z);
                    }
                }
            }

        }
    }

    @OnlyIn(Dist.CLIENT)
    public float getShadingWhileWet(float p_70915_1_) {
        return 0.75F + MathHelper.lerp(p_70915_1_, this.prevTimeWolfIsShaking, this.timeWolfIsShaking) / 2.0F * 0.25F;
    }

    @OnlyIn(Dist.CLIENT)
    public float getShakeAngle(float p_70923_1_, float p_70923_2_) {
        float f = (MathHelper.lerp(p_70923_1_, this.prevTimeWolfIsShaking, this.timeWolfIsShaking) + p_70923_2_) / 1.8F;
        if (f < 0.0F) {
            f = 0.0F;
        } else if (f > 1.0F) {
            f = 1.0F;
        }

        return MathHelper.sin(f * (float) Math.PI) * MathHelper.sin(f * (float) Math.PI * 11.0F) * 0.15F * (float) Math.PI;
    }

    @OnlyIn(Dist.CLIENT)
    public float getInterestedAngle(float p_70917_1_) {
        return MathHelper.lerp(p_70917_1_, this.headRotationCourseOld, this.headRotationCourse) * 0.15F * (float) Math.PI;
    }

    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return sizeIn.height * 0.8F;
    }

    /**
     * The speed it takes to move the entityliving's rotationPitch through the faceEntity method. This is only currently
     * use in wolves.
     */
    public int getVerticalFaceSpeed() {
        return this.isSitting() ? 20 : super.getVerticalFaceSpeed();
    }

    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 8) {
            this.isShaking = true;
            this.timeWolfIsShaking = 0.0F;
            this.prevTimeWolfIsShaking = 0.0F;
        } else
            super.handleStatusUpdate(id);
    }

    @OnlyIn(Dist.CLIENT)
    public float getTailRotation() {
        return ((float) Math.PI / 5F);
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inventory, PlayerEntity p_createMenu_3_) {
        return new PetContainer(id, inventory, new PacketBuffer(Unpooled.buffer(4)).writeVarInt(getEntityId()));
    }
}