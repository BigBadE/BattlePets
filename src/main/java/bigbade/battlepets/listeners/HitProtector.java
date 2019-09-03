package bigbade.battlepets.listeners;

import bigbade.battlepets.entities.PetEntity;
import bigbade.battlepets.registries.ItemRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HitProtector {

    @SubscribeEvent
    public static void onAttack(AttackEntityEvent event) {
        if(event.getTarget().world.isRemote)
            return;
        else if(event.getEntity() instanceof PlayerEntity && event.getTarget() instanceof LivingEntity) {
            ItemStack stack = ((PlayerEntity) event.getEntity()).getItemStackFromSlot(EquipmentSlotType.MAINHAND);
            if(stack.getItem().equals(ItemRegistry.CONVERTER)) {
                ItemRegistry.CONVERTER.hitEntity(stack, (LivingEntity) event.getTarget(), (LivingEntity) event.getEntity());
                event.setCanceled(true);
            }
        } else if(event.getEntity() instanceof PlayerEntity && event.getTarget() instanceof PetEntity && ((PetEntity) event.getTarget()).getOwnerUUID().equals(event.getEntityPlayer().getGameProfile().getId()))
            event.setCanceled(true);
    }
}
