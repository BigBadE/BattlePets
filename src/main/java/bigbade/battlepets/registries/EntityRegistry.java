package bigbade.battlepets.registries;

import bigbade.battlepets.client.render.RendererPetEntity;
import bigbade.battlepets.entities.PetEntity;
import bigbade.battlepets.entities.PetEntityFactory;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.spawner.WanderingTraderSpawner;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder("battlepets")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityRegistry {

    public static EntityType<PetEntity> PETENTITY;

    @SubscribeEvent
    public static void onEntityRegister(RegistryEvent.Register<EntityType<?>> event) {
        PETENTITY = (EntityType<PetEntity>) EntityType.Builder.create(new PetEntityFactory(), EntityClassification.CREATURE).build("pet").setRegistryName("battlepets", "pet");
        RenderingRegistry.registerEntityRenderingHandler(PetEntity.class, manager -> new RendererPetEntity());
        event.getRegistry().register(PETENTITY);
    }
}
