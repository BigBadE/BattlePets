package bigbade.battlepets.skills;

import bigbade.battlepets.ai.CreeperEntityMatcher;
import bigbade.battlepets.ai.IEntityMatcher;
import bigbade.battlepets.api.AttributeEnemyMatcher;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.item.ItemStack;

public class RepellantSkill extends Skill
{
	public RepellantSkill( int theId, String theName, ItemStack theIcon )
	{
		super( theId, ( theName.equals( "" ) ? "repellant" : ( "repellant." + theName ) ), getPosX( theId, theName ), getPosY( theId, theName ), theIcon );
		
		radius = 6.f;
		type = new CreeperEntityMatcher();
		
		if ( theName.equals( "" ) )
		{
			mainId = id;
		}
		else
		{
			skillReqs = new int[] { mainId };
		}
	}
	
	public RepellantSkill(int theId, String theName, int theLevelReq, CreatureAttribute attr, ItemStack theIcon )
	{
		this( theId, theName, theIcon );
		levelReq = theLevelReq;
		type = new AttributeEnemyMatcher( attr );
		radius = 0.f;
	}
	
	public RepellantSkill( int theId, String theName, int theLevelReq, float theRadius, ItemStack theIcon )
	{
		this( theId, theName, theIcon );
		levelReq = theLevelReq;
		type = null;
		radius = theRadius;
	}
	
	private static float getPosX( int id, String name )
	{
		float base = -3;
		if ( name.equals( "" ) )
		{
			return base;
		}
		
		int diff = id - ( mainId + 1 );
		
		return base - 1.f + ( ( diff % 2 ) * 2.f );
	}
	
	private static float getPosY( int id, String name )
	{
		if ( name.equals( "" ) )
		{
			return 4.5f;
		}
		
		int diff = id - ( mainId + 1 );
		
		return 6 + ( diff / 2 );
	}
	
	public float radius;
	public IEntityMatcher type;
	private static int mainId;
}
