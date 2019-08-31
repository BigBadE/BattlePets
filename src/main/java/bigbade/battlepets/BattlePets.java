package bigbade.battlepets;

import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("battlepets")
public class BattlePets {
    public static Logger LOGGER = LogManager.getLogger("battlepets");
    public static BattlePetsTab TAB = new BattlePetsTab();

    public BattlePets() {

    }
}
