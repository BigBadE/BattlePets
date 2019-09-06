package bigbade.battlepets;

import bigbade.battlepets.client.gui.PetScreen;
import bigbade.battlepets.registries.ContainerRegistry;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("battlepets")
public class BattlePets {
    public static Logger LOGGER = LogManager.getLogger("battlepets");
    public static BattlePetsTab TAB = new BattlePetsTab();

    public static boolean FANCY_STAT_RENDERER;

    public BattlePets() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::preInit);
    }

    private void preInit(final FMLClientSetupEvent event) {
        ScreenManager.registerFactory(ContainerRegistry.PETCONTAINER, PetScreen::new);
    }
}
