package bigbade.battlepets.registries;

import bigbade.battlepets.entities.PetEntityFactory;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder("battlepets")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityRegistry {

    public static EntityType<AnimalEntity> PETENTITY;

    @SubscribeEvent
    public static void onEntityRegister(RegistryEvent.Register<EntityType<?>> event) {
        PETENTITY = (EntityType<AnimalEntity>) EntityType.Builder.create(new PetEntityFactory(), EntityClassification.CREATURE).build("pet").setRegistryName("battlepets", "pet");
        event.getRegistry().register(PETENTITY);
    }
}
