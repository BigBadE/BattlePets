package bigbade.battlepets.containers.slots;

import bigbade.battlepets.containers.PetContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class SaddleSlot extends Slot {
    public SaddleSlot(PetContainer theContainer, IInventory inv, int par3, int par4, int par5) {
        super(inv, par3, par4, par5);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return super.isItemValid(stack) && stack.getItem() == Items.SADDLE && !this.getHasStack();
    }
}