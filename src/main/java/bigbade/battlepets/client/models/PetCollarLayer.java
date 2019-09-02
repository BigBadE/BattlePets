package bigbade.battlepets.client.models;

import bigbade.battlepets.api.PetType;
import bigbade.battlepets.entities.PetEntity;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.WolfCollarLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.WolfModel;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;

public class PetCollarLayer extends LayerRenderer<PetEntity, EntityModel<PetEntity>> {
    private static final ResourceLocation WOLF_COLLAR = new ResourceLocation("textures/entity/wolf/wolf_collar.png");

    public PetCollarLayer(IEntityRenderer<PetEntity, EntityModel<PetEntity>> p_i50914_1_) {
        super(p_i50914_1_);
    }

    public void render(PetEntity entityIn, float p_212842_2_, float p_212842_3_, float p_212842_4_, float p_212842_5_, float p_212842_6_, float p_212842_7_, float p_212842_8_) {
        if (entityIn.getPetType() == PetType.DOG && !entityIn.isInvisible()) {
            this.bindTexture(WOLF_COLLAR);
            float[] afloat = DyeColor.byId(entityIn.getCollar()).getColorComponentValues();
            GlStateManager.color3f(afloat[0], afloat[1], afloat[2]);
            this.getEntityModel().render(entityIn, p_212842_2_, p_212842_3_, p_212842_5_, p_212842_6_, p_212842_7_, p_212842_8_);
        }
    }

    public boolean shouldCombineTextures() {
        return true;
    }
}
