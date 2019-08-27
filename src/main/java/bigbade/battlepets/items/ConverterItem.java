package bigbade.battlepets.items;

import bigbade.battlepets.BattlePets;
import bigbade.battlepets.api.Level;
import bigbade.battlepets.api.PetType;
import bigbade.battlepets.entities.PetEntity;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ConverterItem extends Item {
    public ConverterItem() {
        super(new Properties().maxStackSize(1).rarity(Rarity.UNCOMMON).group(BattlePets.TAB));
        setRegistryName("battlepets", "converter");
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        return target instanceof TameableEntity || (target instanceof SlimeEntity && ((SlimeEntity) target).getSlimeSize() == 1 && target.getActivePotionEffect(Effects.WEAKNESS) != null) || target instanceof PigEntity;
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (itemInteractionForEntity(stack, null, target, Hand.MAIN_HAND)) {
            if (target instanceof PetEntity) {
                levelUp((PetEntity) target, attacker);
            } else {
                convertPet(target, attacker);
            }
        }
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.petWand.tooltip.help.convert"));
        tooltip.add(new TranslationTextComponent("item.petWand.tooltip.help.track"));
        tooltip.add(new TranslationTextComponent("item.petWand.tooltip.help.more"));
    }

    private void levelUp(PetEntity pet, LivingEntity attacker) {
        PlayerEntity player = (PlayerEntity) attacker;
        if (pet.getLevel() >= Level.MAX_LEVEL) {
            player.sendMessage(new TranslationTextComponent("chat.pet.level.max"));
            return;
        }

        boolean someMissing = false;
        List<ItemStack> reqs = new ArrayList<ItemStack>();
        int reqLevel = 0;
        if (!player.isCreative()) {
            reqs = Level.getLevelItemRequirements(pet.getLevel() + 1);
            for (ItemStack stack : reqs) {
                if (!hasAmount(player.inventory, stack.getItem(), stack.getCount())) {
                    someMissing = true;
                    player.sendMessage(new TranslationTextComponent("chat.pet.level.missing", stack.getDisplayName(), stack.getCount()));
                }
            }

            reqLevel = Level.getLevelExperienceRequirements(pet.getLevel() + 1);
            if (player.experienceLevel < reqLevel) {
                someMissing = true;
                player.sendMessage(new TranslationTextComponent("chat.pet.level.missing", new TranslationTextComponent("misc.level.name").getFormattedText(), reqLevel));
            }
        }

        if (someMissing) {
            return;
        }

        if (!player.isCreative()) {
            player.addExperienceLevel(-reqLevel);
            for (ItemStack stack : reqs) {
                takeAmount(player.inventory, stack.getItem(), stack.getCount());
            }
        }

        pet.levelUp();
        player.sendMessage(new TranslationTextComponent("chat.pet.level.success"));
    }

    private void convertPet(LivingEntity target, LivingEntity attacker) {
        PlayerEntity player = (PlayerEntity) target;
        TameableEntity tameable = null;
        if (target instanceof TameableEntity) {
            tameable = (TameableEntity) target;
            if (!tameable.isTamed()) {
                player.sendMessage(new TranslationTextComponent("chat.pet.convert.notTamed"));
                return;
            }

            if (!tameable.getOwner().getUniqueID().equals(player.getUniqueID())) {
                player.sendMessage(new TranslationTextComponent("chat.pet.convert.needOwnership"));
                return;
            }
        } else {

            if (target instanceof SlimeEntity) {
                SlimeEntity slime = (SlimeEntity) target;
                if (slime.getSlimeSize() != 1) {
                    System.out.println(slime.getSlimeSize());
                    player.sendMessage(new TranslationTextComponent("chat.pet.convert.notSmallSlime"));
                    return;
                }
            }
            if (!target.isPotionActive(Effects.WEAKNESS)) {
                player.sendMessage(new TranslationTextComponent("chat.pet.convert.weakness"));
                return;
            }
        }

        for (PetType type : PetType.values()) {
            if (!type.fromEntity.equals(target.getClass())) {
                continue;
            }

            PetEntity pet = new PetEntity(target.getEntityWorld(), type, player.getUniqueID());
            pet.setPosition(target.posX, target.posY, target.posZ);
            //pet.setOwnerName( target.getOwnerName() );
            pet.setOwnerUUID(player.getGameProfile().getId());
            pet.setPetType(type);
            if (tameable != null) pet.setSitting(tameable.isSitting());
            if (target.getCustomName() != null) {
                pet.setCustomName(target.getCustomName());
            }

            target.remove();
            target.getEntityWorld().addEntity(pet);

            player.sendMessage(new TranslationTextComponent("chat.pet.convert.success"));
            return;
        }

        player.sendMessage(new TranslationTextComponent("chat.pet.convert.failure"));
    }

    private boolean hasAmount(IInventory inv, Item item, int reqAmount) {
        int amount = 0;
        for (int is = 0; is < inv.getSizeInventory(); ++is) {
            ItemStack stack = inv.getStackInSlot(is);
            if (stack != null && stack.getItem() == item) {
                amount += stack.getCount();
            }
        }

        return (amount >= reqAmount);
    }

    private void takeAmount(IInventory inv, Item item, int reqAmount) {
        int left = reqAmount;
        for (int is = 0; is < inv.getSizeInventory() && left > 0; ++is) {
            ItemStack stack = inv.getStackInSlot(is);
            if (stack != null && stack.getItem() == item) {
                left -= inv.decrStackSize(is, left).getCount();
            }
        }
    }
}
