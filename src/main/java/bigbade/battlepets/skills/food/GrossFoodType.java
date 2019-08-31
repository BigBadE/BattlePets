package bigbade.battlepets.skills.food;

import net.minecraft.item.ItemStack;

import static net.minecraft.item.Items.ROTTEN_FLESH;
import static net.minecraft.item.Items.SPIDER_EYE;

public class GrossFoodType extends SpecificFoodType
{
	public GrossFoodType()
	{
		super();
		
		foods.add( new ItemStack( SPIDER_EYE) );
		foods.add( new ItemStack( ROTTEN_FLESH ) );
	}
}
