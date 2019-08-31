package bigbade.battlepets.skills.food;

import net.minecraft.item.ItemStack;

import static net.minecraft.item.Items.*;

public class ProcessedFoodType extends SpecificFoodType {
    public ProcessedFoodType() {
        super();

        foods.add(new ItemStack(COOKIE));
        foods.add(new ItemStack(PUMPKIN_PIE));
        foods.add(new ItemStack(BREAD));
        foods.add(new ItemStack(MUSHROOM_STEM));
        foods.add(new ItemStack(BEETROOT_SOUP));
        foods.add(new ItemStack(RABBIT_STEW));
        foods.add(new ItemStack(SUSPICIOUS_STEW));
    }
}
