package bigbade.battlepets.skills.food;

import bigbade.battlepets.api.PetType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class OtherSpeciesFoodType extends FoodType {
    @Override
    public boolean doesMatch(PetType type, ItemStack stack) {
        for (PetType other : PetType.values()) {
            if (other.equals(type)) {
                continue;
            }

            for (Item food : other.food) {
                if (stack.getItem().equals(food)) {
                    return true;
                }
            }
        }

        return false;
    }
}
