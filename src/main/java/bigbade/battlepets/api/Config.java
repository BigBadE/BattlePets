package bigbade.battlepets.api;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final General GENERAL = new General(BUILDER);
    public static final ForgeConfigSpec spec = BUILDER.build();

    public static class General {
        public General(ForgeConfigSpec.Builder builder) {
            builder.push("General");
            Level.configure(builder);
            builder.pop();
        }
    }
}
