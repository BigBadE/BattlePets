package bigbade.battlepets;

import bigbade.battlepets.registries.EntityRegistry;
import bigbade.battlepets.registries.ItemRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("battlepets")
public class BattlePets {
    public static Logger LOGGER = LogManager.getLogger("battlepets");
    public static BattlePetsTab TAB = new BattlePetsTab();

    public BattlePets() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(EntityRegistry::onEntityRegister);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ItemRegistry::onItemRegister);
    }
}
