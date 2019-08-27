package bigbade.battlepets.api;

import bigbade.battlepets.BattlePets;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Level {
    private static ForgeConfigSpec.ConfigValue<Integer> MaxLevel;
    private static ForgeConfigSpec.ConfigValue<Integer> XPProperty;
    private static ForgeConfigSpec.ConfigValue<String> ItemProperty;

    public static int MAX_LEVEL = 25;

    public static int getLevelExperienceRequirements(int level) {
        return xpReqs.get(level);
    }

    public static List<ItemStack> getLevelItemRequirements(int level) {
        return itemReqs.get(level);
    }

    static void configure(ForgeConfigSpec.Builder builder) {
        MAX_LEVEL = MaxLevel.get();
        MaxLevel = builder
                .comment("If you change this, delete the 'levelReqs' category and let it re-generate")
                .translation("config.level")
                .defineInRange("MaxLevel", 25, 0, Integer.MAX_VALUE);

        Map<Integer, List<ItemStack>> levelItemReqs = new HashMap<Integer, List<ItemStack>>();
        Map<Integer, Integer> levelXpReqs = new HashMap<Integer, Integer>();
        for (int i = 2; i <= Level.MAX_LEVEL; ++i) {
            String coal = "minecraft:coal_block";
            String iron = "minecraft:iron_ingot";
            String redstone = "minecraft:redstone_block";
            String gold = "minecraft:gold_ingot";
            String diamond = "minecraft:diamond";

            String defaultItems = "";
            if (i <= MAX_LEVEL * 0.2) {
                defaultItems += "1x" + coal;
            } else if (i <= MAX_LEVEL * 0.4) {
                defaultItems += "5x" + iron;
            } else if (i <= MAX_LEVEL * 0.6) {
                defaultItems += "3x" + redstone;
            } else if (i <= MAX_LEVEL * 0.8) {
                defaultItems += "5x" + gold;
            } else {
                defaultItems += "2x" + diamond;
            }
            int defaultLevel = i - 1;

            String num = String.format("%02d", i);
            ItemProperty = builder
                    .translation("config.itemprop")
                    .define("ItemProperties", defaultItems);
            if (i == 2)
                builder.comment("Items should be separated by commas, in the format: \"QUANTITY x MOD : NAME @ DATA_VALUE\"\n(DATA_VALUE is optional, but if you don't specify it, do not add the @.)\nFor a list of valid entries, add -Dfml.dumpRegistry to your launch options, and find itemStackRegistry.csv in your minecraft directory.");

            XPProperty = builder
                    .comment("Items should be separated by commas, in the format: \"QUANTITY x MOD : NAME @ DATA_VALUE\"\n(DATA_VALUE is optional, but if you don't specify it, do not add the @.)\nFor a list of valid entries, add -Dfml.dumpRegistry to your launch options, and find itemStackRegistry.csv in your minecraft directory.")
                    .translation("config.xpprop")
                    .defineInRange("XPProperties", defaultLevel, 0, Integer.MAX_VALUE);

            List<ItemStack> stacks = new ArrayList<ItemStack>();
            String itemStr = ItemProperty.get();
            StringTokenizer tokens = new StringTokenizer(itemStr, ",");
            while (tokens.hasMoreTokens()) {
                String token = tokens.nextToken();
                Matcher matcher = item.matcher(token);
                if (!matcher.matches()) {
                    BattlePets.LOGGER.warn("Invalid item requirement \"" + token + "\" for pet level " + i + "! Skipping...");
                    continue;
                }

                int amt = Integer.parseInt(matcher.group(1));
                String modId = matcher.group(2);
                String itemName = matcher.group(3);

                ItemStack stack = new ItemStack(GameRegistry.findRegistry(Item.class).getValue(new ResourceLocation(modId, itemName)), amt);
                if (matcher.groupCount() > 3 && matcher.group(5) != null && matcher.group(5).length() > 0) {
                    stack.setDamage(Integer.parseInt(matcher.group(5)));
                }
                stacks.add(stack);
            }
            levelItemReqs.put(i, stacks);

            levelXpReqs.put(i, XPProperty.get());
        }

        itemReqs = levelItemReqs;
        xpReqs = levelXpReqs;
    }

    private static Map<Integer, List<ItemStack>> itemReqs;
    private static Map<Integer, Integer> xpReqs;

    private static final Pattern item = Pattern.compile("\\s*(\\d+)\\s*x\\s*([a-zA-Z0-9\\-_]+)\\s*:\\s*([a-zA-Z0-9\\-_]+)\\s*(@\\s*([0-9]+)\\s*)?");
}
