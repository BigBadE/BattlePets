package bigbade.battlepets.skills;

import bigbade.battlepets.skills.food.FoodType;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SkullItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.HashMap;
import java.util.Map;

import static net.minecraft.item.Items.*;

public class Skill {
    public Skill(int theId, String theName, float px, float py, ItemStack theIcon) {
        id = theId;
        name = theName;
        levelReq = 1;
        x = px;
        y = py;
        icon = theIcon;

        if (skills.containsKey(id)) {
            throw new IllegalArgumentException("Skill " + id + " already exists.");
        }
        skills.put(id, this);
    }

    public Skill(int theId, String theName, float px, float py, ItemStack theIcon, int theLevelReq, int[] theSkillReqs) {
        this(theId, theName, px, py, theIcon);
        levelReq = theLevelReq;
        skillReqs = theSkillReqs;
    }

    public final int id;
    public final String name;
    public final float x;
    public final float y;
    public int levelReq;
    public int[] skillReqs;
    public final ItemStack icon;

    public static final Map<Integer, Skill> skills = new HashMap<Integer, Skill>();

    public static Skill forId(int id) {
        return skills.get(id);
    }

    public static void configure(ForgeConfigSpec.Builder builder) {
        String skillName = new TranslationTextComponent("pet.battlepets.skill.name").getFormattedText();
        for (Skill skill : skills.values()) {
            skill.levelReq = builder
                    .comment("If you change this, delete the 'levelReqs' category and let it re-generate")
                    .translation(skill.name)
                    .defineInRange(new TranslationTextComponent(skill.name).getFormattedText(), skill.levelReq, 0, Integer.MAX_VALUE).get();
        }
    }

    public static Skill HUNGER = new FoodSkill(0, "", FoodType.SPECIES, new ItemStack(PORKCHOP));
    public static Skill HUNGER_OTHER = new FoodSkill(1, "eatOther", FoodType.OTHER_SPECIES, new ItemStack(SALMON));
    public static Skill HUNGER_PLANTS = new FoodSkill(2, "eatPlants", FoodType.PLANTS, new ItemStack(CARROT));
    public static Skill HUNGER_PROCESSED = new FoodSkill(3, "eatProcessed", 5, FoodType.PROCESSED, new ItemStack(COOKIE));
    public static Skill HUNGER_GROSS = new FoodSkill(4, "eatGross", 10, FoodType.GROSS, new ItemStack(ROTTEN_FLESH));

    public static Skill COMBAT = new AttackSkill(5, "", 4, new ItemStack(WOODEN_SWORD));
    public static Skill COMBAT_UPGRADE1 = new AttackSkill(6, "upgrade1", 1, new ItemStack(STONE_SWORD));
    public static Skill COMBAT_UPGRADE2 = new AttackSkill(7, "upgrade2", 5, 1, new ItemStack(IRON_SWORD));
    public static Skill COMBAT_UPGRADE3 = new AttackSkill(8, "upgrade3", 10, 1, new ItemStack(DIAMOND_SWORD));
    public static Skill COMBAT_HOSTILE = new Skill(34, "combat.hostile", 1.5f, 0.4f, new ItemStack(BLAZE_POWDER), 10, new int[]{5});

    public static Skill REPELLANT = new RepellantSkill(9, "", generateSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2Y4ZDkxNTg1YmZhZWExZDBiZWIxYjFiNGU1Zjk4NzYyOTYzOTlhOWZlNWFjOTNkMzE5ZmEyMTI3M2JjMCJ9fX0=", "dbb97ff5-b3d5-4ee6-91eb-1c129046317b"));
    public static Skill REPELLANT_RADIUS = new RepellantSkill(10, "larger", 10, 4.f, new ItemStack(STICK));
    public static Skill REPELLANT_UNDEAD = new RepellantSkill(11, "undead", 6, CreatureAttribute.UNDEAD, new ItemStack(SKELETON_SKULL));
    public static Skill REPELLANT_SPIDERS = new RepellantSkill(12, "spiders", 3, CreatureAttribute.ARTHROPOD, new ItemStack(SPIDER_EYE));

    public static Skill INVENTORY = new Skill(13, "inventory", 4.5f, 0, new ItemStack(CHEST), 1, null);
    public static Skill INVENTORY_UPGRADE1 = new Skill(14, "inventory.upgrade1", 5.5f, 1.5f, new ItemStack(CHEST), 5, new int[]{13});
    public static Skill INVENTORY_UPGRADE2 = new Skill(15, "inventory.upgrade2", 5.5f, 2.5f, new ItemStack(CHEST), 10, new int[]{14});
    public static Skill INVENTORY_UPGRADE3 = new Skill(35, "inventory.upgrade3", 6.5f, 2.5f, new ItemStack(CHEST), 15, new int[]{15});
    public static Skill INVENTORY_UPGRADE4 = new Skill(36, "inventory.upgrade4", 6.5f, 1.5f, new ItemStack(CHEST), 20, new int[]{35});
    public static Skill INVENTORY_FEEDING = new Skill(16, "inventory.selfSufficient", 4.5f, -1.5f, new ItemStack(PUMPKIN_PIE), 10, new int[]{0, 13});
    public static Skill INVENTORY_ARMOR = new Skill(17, "inventory.armor", 3.5f, 1.5f, new ItemStack(GOLDEN_HORSE_ARMOR), 5, new int[]{5, 13});
    public static Skill INVENTORY_WEAPON = new Skill(18, "inventory.weapon", 3.5f, 2.5f, new ItemStack(IRON_SWORD), 17, new int[]{5, 13});
    public static Skill INVENTORY_PICKUP = new Skill(37, "inventory.pickup", 6, 0, new ItemStack(HOPPER), 5, new int[]{13});

    public static Skill DEFENSE = new DefenseSkill(19, "", 1, 0.1f, new ItemStack(LEATHER_CHESTPLATE));
    public static Skill DEFENSE_UPGRADE1 = new DefenseSkill(20, "upgrade1", 5, 0.15f, new ItemStack(IRON_CHESTPLATE));
    public static Skill DEFENSE_UPGRADE2 = new DefenseSkill(21, "upgrade2", 12, 0.25f, new ItemStack(DIAMOND_CHESTPLATE));
    public static Skill DEFENSE_FIRE = new DefenseSkill(22, "fire", 10, new String[]{"inFire", "onFire", "lava"}, new ItemStack(FIRE_CHARGE));
    public static Skill DEFENSE_BREATHLESS = new DefenseSkill(23, "breathless", 5, new String[]{"inWall", "drown"}, new ItemStack(WATER_BUCKET));
    public static Skill DEFENSE_FEATHERFALL = new DefenseSkill(24, "featherFall", 7, new String[]{"fall"}, new ItemStack(FEATHER));

    public static Skill TRAVEL = new SpeedSkill(25, "", 3, 0.2f, new ItemStack(POTION, 1, potionNBT()));
    public static Skill TRAVEL_UPGRADE1 = new SpeedSkill(26, "upgrade1", 15, 0.2f, new ItemStack(POTION, 1, potionNBT()));
    public static Skill TRAVEL_UPGRADE2 = new SpeedSkill(27, "upgrade2", 17, 0.3f, new ItemStack(POTION, 1, potionNBT()));
    public static Skill TRAVEL_MOUNTABLE = new Skill(28, "travel.mountable", 0.5f, 9.f, new ItemStack(SADDLE), 10, new int[]{25});
    public static Skill TRAVEL_SWIMMING = new Skill(31, "travel.aquaticMount", 0.5f, 10.25f, new ItemStack(WATER_BUCKET), 12, new int[]{28});
    public static Skill TRAVEL_SWIMCONTROL = new Skill(32, "travel.swimControl", 1.75f, 10.25f, new ItemStack(WATER_BUCKET), 15, new int[]{31, 23});
    public static Skill TRAVEL_MOUNTJUMP = new Skill(33, "travel.mountJump", 1.75f, 9.f, new ItemStack(FEATHER), 12, new int[]{28});

    public static Skill HEALTH = new HealthSkill(29, "", 7, 10, new ItemStack(GOLDEN_APPLE));
    public static Skill HEALTH_UPGRADE1 = new HealthSkill(30, "upgrade1", 14, 10, new ItemStack(ENCHANTED_GOLDEN_APPLE));

    public static ItemStack generateSkull(String texture, String id) {
        CompoundNBT ownerData = new CompoundNBT();
        ownerData.putString("textures", texture);
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("name", "BattlePetsRepelSkull");
        nbt.putString("id", id);
        nbt.put("Properties", ownerData);
        CompoundNBT skullNBT = new CompoundNBT();
        skullNBT.put("SkullOwner", nbt);
        return new ItemStack(PLAYER_HEAD, 1, skullNBT);
    }

    public static CompoundNBT potionNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("CustomPotionColor", 8171462);
        return nbt;
    }
}