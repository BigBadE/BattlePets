package bigbade.battlepets.skills.food;

import bigbade.battlepets.api.PetType;
import net.minecraft.item.ItemStack;

public abstract class FoodType
{
	public abstract boolean doesMatch(PetType type, ItemStack stack );
	
	public static final FoodType SPECIES = new SpeciesFoodType();
	public static final FoodType OTHER_SPECIES = new OtherSpeciesFoodType();
	public static final FoodType PLANTS = new PlantFoodType();
	public static final FoodType PROCESSED = new ProcessedFoodType();
	public static final FoodType GROSS = new GrossFoodType();
}
