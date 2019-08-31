package bigbade.battlepets.skills.food;

import bigbade.battlepets.api.PetType;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SpecificFoodType extends FoodType {
    public SpecificFoodType() {
    }

    @Override
    public boolean doesMatch(PetType type, ItemStack stack) {
        for (ItemStack food : foods) {
            if (stack.getItem().equals(food.getItem())) {
                return true;
            }
        }

        return false;
    }

    protected List<ItemStack> foods = new ArrayList<>();
}
