package bigbade.battlepets.registries;

import bigbade.battlepets.items.ConverterItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder("battlepets")
public class ItemRegistry {

    @ObjectHolder("converter")
    public static Item CONVERTER;

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        System.out.println("REGISTERCONVERTERITEM");
        event.getRegistry().register(new ConverterItem());
    }
}