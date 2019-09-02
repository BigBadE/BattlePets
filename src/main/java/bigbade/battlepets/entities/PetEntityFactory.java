package bigbade.battlepets.entities;

import bigbade.battlepets.api.PetType;
import bigbade.battlepets.registries.EntityRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.stream.Stream;

import static net.minecraft.entity.EntityType.applyItemNBT;

public class PetEntityFactory<T extends PetEntity> implements EntityType.IFactory<PetEntity> {
    @Override
    public PetEntity create(EntityType type, World world) {
        System.out.println("FACTORY CALL");
        return new PetEntity(world, PetType.DOG, Minecraft.getInstance().player.getUniqueID());
    }
}
