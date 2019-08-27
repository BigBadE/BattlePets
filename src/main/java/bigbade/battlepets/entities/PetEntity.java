package bigbade.battlepets.entities;

import bigbade.battlepets.api.Level;
import bigbade.battlepets.api.PetType;
import bigbade.battlepets.registries.EntityRegistry;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public class PetEntity extends AnimalEntity {
    private int level;
    private int skillPoints;
    private boolean sitting;
    private PetType type;
    private UUID ownerUUID;

    public PetEntity(World worldIn, PetType type, UUID ownerUUID) {
        super(EntityRegistry.PETENTITY, worldIn);
        this.type = type;
        level = 0;
        sitting = false;
        skillPoints = 1;
        this.ownerUUID = ownerUUID;
    }

    //TODO add breedable pets
    @Nullable
    @Override
    public AgeableEntity createChild(AgeableEntity ageable) {
        return null;
    }

    public void setOwnerUUID(UUID uuid) {
        this.ownerUUID = uuid;
    }

    public void setPetType(PetType type) {
        this.type = type;
    }

    public void setSitting(boolean sitting) {
        this.sitting = sitting;
    }

    public void setCustomName(String name) {
        setCustomName(new StringTextComponent(name));
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setFreeSkillPoints(int points) {
        this.skillPoints = points;
    }

    public int getLevel() {
        return level;
    }

    public int getFreeSkillPoints() {
        return skillPoints;
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
        compound.putInt("level", level);
        compound.putInt("skillPoints", skillPoints);
        compound.putInt("petType", type.ordinal());

        compound.putBoolean("sitting", sitting);

        compound.putUniqueId("owner", ownerUUID);
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        level = compound.getInt("level");
        skillPoints = compound.getInt("skillPoints");
        type = PetType.values()[compound.getInt("petType")];

        sitting = compound.getBoolean("sitting");

        ownerUUID = compound.getUniqueId("owner");
    }
}
