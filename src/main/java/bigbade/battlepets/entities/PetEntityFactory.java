package bigbade.battlepets.entities;

import bigbade.battlepets.api.PetType;
import bigbade.battlepets.registries.EntityRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class PetEntityFactory implements EntityType.IFactory<Entity> {
    @Override
    public Entity create(EntityType type, World world) {
        return new PetEntity(world, PetType.DOG, null);
    }
}
