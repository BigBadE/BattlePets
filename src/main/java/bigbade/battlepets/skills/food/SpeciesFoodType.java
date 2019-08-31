package bigbade.battlepets.skills.food;

import bigbade.battlepets.api.PetType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SpeciesFoodType extends FoodType {
    @Override
    public boolean doesMatch(PetType type, ItemStack stack) {
        for (Item food : type.food) {
            if (stack.getItem().equals(food)) {
                return true;
            }
        }

        return false;
    }
}
