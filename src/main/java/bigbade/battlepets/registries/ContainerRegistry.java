package bigbade.battlepets.registries;

import bigbade.battlepets.client.gui.PetScreen;
import bigbade.battlepets.containers.PetContainer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder("battlepets")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ContainerRegistry {

    @ObjectHolder("petcontainer")
    public static ContainerType<PetContainer> PETCONTAINER;

    @SubscribeEvent
    public static void onContainerRegister(RegistryEvent.Register<ContainerType<?>> event) {
        event.getRegistry().register(IForgeContainerType.create(PetContainer::new).setRegistryName("petcontainer"));
    }
}
