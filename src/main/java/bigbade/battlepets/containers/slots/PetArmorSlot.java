package bigbade.battlepets.containers.slots;

import bigbade.battlepets.containers.PetContainer;
import bigbade.battlepets.containers.PetInventoryContainer;
import bigbade.battlepets.entities.PetEntity;
import bigbade.battlepets.skills.Skill;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class PetArmorSlot extends Slot {
    public final PetEntity pet;

    public PetArmorSlot(PetContainer theContainer, IInventory inv, int par3, int par4, int par5, PetEntity thePet) {
        super(inv, par3, par4, par5);
        this.pet = thePet;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        if (!pet.hasSkill(Skill.INVENTORY_ARMOR.id)) {
            return false;
        }

        return super.isItemValid(stack) && isArmor(stack.getItem());
    }

    private boolean isArmor(Item item) {
        return item == Items.DIAMOND_HORSE_ARMOR || item == Items.GOLDEN_HORSE_ARMOR || item == Items.IRON_HORSE_ARMOR;
    }
}
