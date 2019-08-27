package bigbade.battlepets.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.WolfEntity;

public enum PetType {
    CAT(OcelotEntity.class),
    DOG(WolfEntity.class),
    PIG(PigEntity.class),
    SLIME(SlimeEntity.class);

    public Class fromEntity;

    PetType(Class<? extends Entity> from) {
        fromEntity = from;
    }
}
