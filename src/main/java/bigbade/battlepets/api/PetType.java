package bigbade.battlepets.api;

import bigbade.battlepets.skills.Skill;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.SilverfishEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static net.minecraft.item.Items.*;

public enum PetType {
    CAT(OcelotEntity.class, Arrays.asList(COD, COOKED_COD, TROPICAL_FISH, SALMON, COOKED_SALMON, CHICKEN, COOKED_CHICKEN), new int[]{Skill.HUNGER.id,
            Skill.REPELLANT.id,
            Skill.DEFENSE_FEATHERFALL.id}, new String[]{"textures/entity/cat/red.png",
            "textures/entity/cat/siamese.png",
            "textures/entity/cat/black.png",
            "textures/entity/cat/ocelot.png"}, 0.6f, 0.8f),
    DOG(WolfEntity.class, Arrays.asList(BEEF, COOKED_BEEF, MUTTON, COOKED_MUTTON, PORKCHOP, COOKED_PORKCHOP), new int[]{Skill.HUNGER.id,
            Skill.COMBAT.id,
            Skill.HUNGER_GROSS.id}, new String[]{"textures/entity/wolf/wolf_tame.png",
            "textures/entity/wolf/wolf.png",
            "textures/entity/wolf/wolf_angry.png"}, 0.6f, 0.8f),
    PIG(PigEntity.class, Arrays.asList(CARROT, GOLDEN_CARROT), new int[]{Skill.HUNGER.id,
            Skill.TRAVEL.id,
            Skill.TRAVEL_MOUNTABLE.id}, new String[]{"textures/entity/pig/pig.png"}, 0.6f, 0.8f),
    SLIME(SlimeEntity.class, Arrays.asList(COOKIE, POTATO), new int[]{Skill.HUNGER.id,
            Skill.INVENTORY.id,
            Skill.INVENTORY_PICKUP.id}, new String[]{"textures/entity/slime/slime.png",
            "textures/entity/slime/magmacube.png"}, 0.6f, 0.8f),
    SILVERFISH(SilverfishEntity.class, Arrays.asList(COOKIE, POTATO), new int[]{Skill.HUNGER.id,
            Skill.COMBAT.id,
            Skill.DEFENSE.id}, new String[]{"textures/entity/silverfish.png"}, 0.6f, 0.8f);

    public Class fromEntity;
    public List<Item> food;
    public int[] skills;
    public String[] textures;
    public float width, height;
    private static Random rand = new Random();

    public SoundEvent getLivingSound() {
        if (this.equals(CAT)) {
            return ((rand.nextInt(4) == 0) ? SoundEvents.ENTITY_CAT_PURR : SoundEvents.ENTITY_CAT_AMBIENT);
        } else if (this.equals(DOG)) {
            return SoundEvents.ENTITY_WOLF_AMBIENT;
        } else if (this.equals(PIG)) {
            return SoundEvents.ENTITY_PIG_AMBIENT;
        } else if (this.equals(SLIME)) {
            return SoundEvents.ENTITY_SLIME_SQUISH_SMALL;
        } else if (this.equals(SILVERFISH)) {
            return SoundEvents.ENTITY_SILVERFISH_AMBIENT;
        }

        return null;
    }

    PetType(Class<? extends Entity> from, List<Item> food, int[] skills, String[] textures, float width, float height) {
        fromEntity = from;
        this.food = food;
        this.skills = skills;
        this.textures = textures;
        this.width = width;
        this.height = height;
    }
}
