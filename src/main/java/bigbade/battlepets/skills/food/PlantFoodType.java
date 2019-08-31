package bigbade.battlepets.skills.food;

import net.minecraft.item.ItemStack;

import static net.minecraft.item.Items.*;

public class PlantFoodType extends SpecificFoodType {
    public PlantFoodType() {
        super();

        foods.add(new ItemStack(APPLE));
        foods.add(new ItemStack(GOLDEN_APPLE));
		foods.add(new ItemStack(ENCHANTED_GOLDEN_APPLE));
        foods.add(new ItemStack(POTATO));
        foods.add(new ItemStack(BAKED_POTATO));
        foods.add(new ItemStack(POISONOUS_POTATO));
        foods.add(new ItemStack(CARROT));
        foods.add(new ItemStack(GOLDEN_CARROT));
        foods.add(new ItemStack(MELON));
        foods.add(new ItemStack(BEETROOT));
        foods.add(new ItemStack(CHORUS_FRUIT));
        foods.add(new ItemStack(DRIED_KELP));
    }
}
