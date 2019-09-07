package bigbade.battlepets.containers;

import bigbade.battlepets.containers.slots.PetArmorSlot;
import bigbade.battlepets.containers.slots.PetWeaponSlot;
import bigbade.battlepets.containers.slots.SaddleSlot;
import bigbade.battlepets.entities.PetEntity;
import bigbade.battlepets.registries.ContainerRegistry;
import bigbade.battlepets.skills.Skill;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class PetContainer extends Container {
    private PetEntity pet;

    private IInventory petInventory;

    public PetContainer(int id, PlayerInventory playerInventory, PacketBuffer data) {
        super(ContainerRegistry.PETCONTAINER, id);

        pet = (PetEntity) playerInventory.player.world.getEntityByID(data.getInt(0));

        this.petInventory = pet.getPetInventory();

        byte b0 = 3;
        petInventory.openInventory(playerInventory.player);
        int j;
        int k;
        //*
        if (pet.hasSkill(Skill.TRAVEL_MOUNTABLE.id))
            this.addSlot(new SaddleSlot(this, petInventory, 0, 8, 18));
        if (pet.hasSkill(Skill.INVENTORY_ARMOR.id))
            this.addSlot(new PetArmorSlot(this, petInventory, 1, 26, 18, pet));
        if (pet.hasSkill(Skill.INVENTORY_WEAPON.id))
            this.addSlot(new PetWeaponSlot(this, petInventory, 2, 44, 18, pet));

        if (pet.hasSkill(Skill.INVENTORY.id)) {
            int invAmount = (pet.getPetInventory().getSizeInventory() - 3);
            for (j = 0; j < Math.min(invAmount / 3, 3); ++j) {
                for (k = 0; k < 3; ++k) {
                    this.addSlot(new Slot(petInventory, 3 + j * 3 + k, 116 + k * 18, 18 + j * 18));
                }
            }
            for (j = 0; j < Math.max(invAmount / 3 - 3, 0); ++j) {
                for (k = 0; k < 3; ++k) {
                    this.addSlot(new Slot(petInventory, 3 + 9 + j * 3 + k, 116 + 18 * 3 + j * 18, 18 + k * 18));
                }
            }
        }

        bindPlayerInventory(playerInventory);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerEntity) {
        return this.petInventory.isUsableByPlayer(playerEntity) && this.pet.isAlive() && this.pet.getDistance(playerEntity) < 8.0F;
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int slot) {
        return null;

        // USE THIS: ( InventoryPlayer.getHotbarSize() * 4 )
    	/*
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(par2);

        // TODO will check later
        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par2 < this.field_111243_a.getSizeInventory())
            {
                if (!this.mergeItemStack(itemstack1, this.field_111243_a.getSizeInventory(), this.inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (this.getSlot(2).isItemValid(itemstack1) && !this.getSlot(2).getHasStack())
            {
                if (!this.mergeItemStack(itemstack1, 2, 3, false))
                {
                    return null;
                }
            }
            else if (this.getSlot(1).isItemValid(itemstack1) && !this.getSlot(1).getHasStack())
            {
                if (!this.mergeItemStack(itemstack1, 1, 2, false))
                {
                    return null;
                }
            }
            else if (this.getSlot(0).isItemValid(itemstack1))
            {
                if (!this.mergeItemStack(itemstack1, 0, 1, false))
                {
                    return null;
                }
            }
            else if (this.field_111243_a.getSizeInventory() <= 2 || !this.mergeItemStack(itemstack1, 2, this.field_111243_a.getSizeInventory(), false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return itemstack;
        */
    }

    /**
     * Called when the container is closed.
     */
    @Override
    public void onContainerClosed(PlayerEntity player) {
        super.onContainerClosed(player);
    }

    private void bindPlayerInventory(PlayerInventory player) {
        for (int iy = 0; iy < 3; iy++) {
            for (int ix = 0; ix < PlayerInventory.getHotbarSize(); ix++) {
                addSlot(new Slot(player, ix + iy * 9 + PlayerInventory.getHotbarSize(), 8 + ix * 18, 84 + iy * 18));
            }
        }

        for (int ix = 0; ix < PlayerInventory.getHotbarSize(); ix++) {
            addSlot(new Slot(player, ix, 8 + ix * 18, 142));
        }
    }

    public ITextComponent getName() {
        if (pet.hasCustomName()) {
            return pet.getCustomName();
        }
        return new StringTextComponent("pet " + pet.getPetType().name().toLowerCase());
    }

    public PetEntity getPet() {
        return pet;
    }
}
