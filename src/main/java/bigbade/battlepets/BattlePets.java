package bigbade.battlepets;

import bigbade.battlepets.network.BattlePetsPacketHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("battlepets")
public class BattlePets {
    public static Logger LOGGER = LogManager.getLogger("battlepets");
    public static BattlePetsTab TAB = new BattlePetsTab();

    public static boolean FANCY_STAT_RENDERER;

    public BattlePets() {
        BattlePetsPacketHandler.registerChannel();
    }
}
