package bigbade.battlepets.containers;

import bigbade.battlepets.entities.PetEntity;
import bigbade.battlepets.skills.Skill;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.UUID;

public class PetInventoryContainer implements IInventory {
    public PetInventoryContainer(PetEntity thePet) {
        pet = thePet;
    }

    @Override
    public int getSizeInventory() {
        int base = 3;
        base += 3 * (pet.hasSkill(Skill.INVENTORY.id) ? 1 : 0);
        base += 3 * (pet.hasSkill(Skill.INVENTORY_UPGRADE1.id) ? 1 : 0);
        base += 3 * (pet.hasSkill(Skill.INVENTORY_UPGRADE2.id) ? 1 : 0);
        base += 3 * (pet.hasSkill(Skill.INVENTORY_UPGRADE3.id) ? 1 : 0);
        base += 3 * (pet.hasSkill(Skill.INVENTORY_UPGRADE4.id) ? 1 : 0);

        return base;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return stacks[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        // From InventoryBasic
        if (stacks[slot] != null) {
            ItemStack itemstack;

            if (stacks[slot].getCount() <= amount) {
                itemstack = stacks[slot];
                stacks[slot] = null;
                this.markDirty();
                return itemstack;
            } else {
                itemstack = stacks[slot].split(amount);

                if (stacks[slot].getCount() == 0) {
                    stacks[slot] = null;
                }

                this.markDirty();
                return itemstack;
            }
        } else {
            return null;
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (this.stacks[index] != null)
        {
            ItemStack itemstack = this.stacks[index];
            this.stacks[index] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        stacks[slot] = stack;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() { }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return pet.getOwnerUUID().equals(player.getGameProfile().getId());
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack stack) {
        // TODO
        return true;
    }

    @Override
    public void clear() {

    }

    private PetEntity pet;
    private ItemStack[] stacks = new ItemStack[18];

    public String getName() {
        return new TranslationTextComponent("gui.pet.inventory").getFormattedText();
    }
}
