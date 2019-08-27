package bigbade.battlepets;

import bigbade.battlepets.registries.ItemRegistry;
import com.sun.java.accessibility.util.java.awt.TextComponentTranslator;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;

public class BattlePetsTab extends ItemGroup {
    public BattlePetsTab() {
        super(new TranslationTextComponent("gui.tab.name").getFormattedText());
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ItemRegistry.CONVERTER);
    }
}
