package bigbade.battlepets.api;

import bigbade.battlepets.BattlePets;
import bigbade.battlepets.client.render.RendererPetEntity;
import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final General GENERAL = new General(BUILDER);
    public static final ForgeConfigSpec spec = BUILDER.build();

    public static class General {
        public General(ForgeConfigSpec.Builder builder) {
            builder.push("General");
            Level.configure(builder);
            ForgeConfigSpec.ConfigValue<Boolean> FancyStats = builder
                    .comment("Display fancy stats over a pet's head.")
                    .translation("config.fancystats")
                    .define("FancyStats", true);
            BattlePets.FANCY_STAT_RENDERER = FancyStats.get();
            builder.pop();
        }
    }
}
