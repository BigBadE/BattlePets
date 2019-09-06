package bigbade.battlepets.containers.slots;

import bigbade.battlepets.containers.PetContainer;
import bigbade.battlepets.containers.PetInventoryContainer;
import bigbade.battlepets.entities.PetEntity;
import bigbade.battlepets.skills.Skill;
import javafx.geometry.Side;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class PetWeaponSlot extends Slot {

    public final PetEntity pet;
    public final PetContainer container;

    public PetWeaponSlot(PetContainer theContainer, IInventory inv, int par3, int par4, int par5, PetEntity thePet) {
        super(inv, par3, par4, par5);
        this.container = theContainer;
        this.pet = thePet;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        if (!pet.hasSkill(Skill.INVENTORY_WEAPON.id)) {
            return false;
        }

        //TODO pet claw item goes here
        return super.isItemValid(stack) /*&& stack.getItem() instanceof ClawItem*/;
    }
}
