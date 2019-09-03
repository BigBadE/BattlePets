package bigbade.battlepets.client.models;

import bigbade.battlepets.entities.PetEntity;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.entity.model.WolfModel;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelDog<T extends PetEntity> extends EntityModel<PetEntity> {
    private final RendererModel wolfHeadMain;
    private final RendererModel wolfBody;
    private final RendererModel wolfLeg1;
    private final RendererModel wolfLeg2;
    private final RendererModel wolfLeg3;
    private final RendererModel wolfLeg4;
    private final RendererModel wolfTail;
    private final RendererModel wolfMane;

    public ModelDog() {
        float f = 0.0F;
        float f1 = 13.5F;
        this.wolfHeadMain = new RendererModel(this, 0, 0);
        this.wolfHeadMain.addBox(-3.0F, -3.0F, -2.0F, 6, 6, 4, f);
        this.wolfHeadMain.setRotationPoint(-1.0F, f1, -7.0F);
        this.wolfBody = new RendererModel(this, 18, 14);
        this.wolfBody.addBox(-4.0F, -2.0F, -3.0F, 6, 9, 6, f);
        this.wolfBody.setRotationPoint(0.0F, 14.0F, 2.0F);
        this.wolfMane = new RendererModel(this, 21, 0);
        this.wolfMane.addBox(-4.0F, -3.0F, -3.0F, 8, 6, 7, f);
        this.wolfMane.setRotationPoint(-1.0F, 14.0F, 2.0F);
        this.wolfLeg1 = new RendererModel(this, 0, 18);
        this.wolfLeg1.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f);
        this.wolfLeg1.setRotationPoint(-2.5F, 16.0F, 7.0F);
        this.wolfLeg2 = new RendererModel(this, 0, 18);
        this.wolfLeg2.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f);
        this.wolfLeg2.setRotationPoint(0.5F, 16.0F, 7.0F);
        this.wolfLeg3 = new RendererModel(this, 0, 18);
        this.wolfLeg3.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f);
        this.wolfLeg3.setRotationPoint(-2.5F, 16.0F, -4.0F);
        this.wolfLeg4 = new RendererModel(this, 0, 18);
        this.wolfLeg4.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f);
        this.wolfLeg4.setRotationPoint(0.5F, 16.0F, -4.0F);
        this.wolfTail = new RendererModel(this, 9, 18);
        this.wolfTail.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f);
        this.wolfTail.setRotationPoint(-1.0F, 12.0F, 8.0F);
        this.wolfHeadMain.setTextureOffset(16, 14).addBox(-3.0F, -5.0F, 0.0F, 2, 2, 1, f);
        this.wolfHeadMain.setTextureOffset(16, 14).addBox(1.0F, -5.0F, 0.0F, 2, 2, 1, f);
        this.wolfHeadMain.setTextureOffset(0, 10).addBox(-1.5F, 0.0F, -5.0F, 3, 3, 4, f);
    }

    public void render(PetEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        if (this.isChild) {
            float f = 2.0F;
            GlStateManager.pushMatrix();
            GlStateManager.translatef(0.0F, 5.0F * scale, 2.0F * scale);
            this.wolfHeadMain.renderWithRotation(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scalef(0.5F, 0.5F, 0.5F);
            GlStateManager.translatef(0.0F, 24.0F * scale, 0.0F);
            this.wolfBody.render(scale);
            this.wolfLeg1.render(scale);
            this.wolfLeg2.render(scale);
            this.wolfLeg3.render(scale);
            this.wolfLeg4.render(scale);
            this.wolfTail.renderWithRotation(scale);
            this.wolfMane.render(scale);
            GlStateManager.popMatrix();
        } else {
            this.wolfHeadMain.renderWithRotation(scale);
            this.wolfBody.render(scale);
            this.wolfLeg1.render(scale);
            this.wolfLeg2.render(scale);
            this.wolfLeg3.render(scale);
            this.wolfLeg4.render(scale);
            this.wolfTail.renderWithRotation(scale);
            this.wolfMane.render(scale);
        }

    }

    public void setLivingAnimations(PetEntity entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        if (entityIn.getAngry()) {
            this.wolfTail.rotateAngleY = 0.0F;
        } else {
            this.wolfTail.rotateAngleY = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        }

        if (entityIn.isSitting()) {
            this.wolfMane.setRotationPoint(-1.0F, 16.0F, -3.0F);
            this.wolfMane.rotateAngleX = 1.2566371F;
            this.wolfMane.rotateAngleY = 0.0F;
            this.wolfBody.setRotationPoint(0.0F, 18.0F, 0.0F);
            this.wolfBody.rotateAngleX = ((float)Math.PI / 4F);
            this.wolfTail.setRotationPoint(-1.0F, 21.0F, 6.0F);
            this.wolfLeg1.setRotationPoint(-2.5F, 22.0F, 2.0F);
            this.wolfLeg1.rotateAngleX = ((float)Math.PI * 1.5F);
            this.wolfLeg2.setRotationPoint(0.5F, 22.0F, 2.0F);
            this.wolfLeg2.rotateAngleX = ((float)Math.PI * 1.5F);
            this.wolfLeg3.rotateAngleX = 5.811947F;
            this.wolfLeg3.setRotationPoint(-2.49F, 17.0F, -4.0F);
            this.wolfLeg4.rotateAngleX = 5.811947F;
            this.wolfLeg4.setRotationPoint(0.51F, 17.0F, -4.0F);
        } else {
            this.wolfBody.setRotationPoint(0.0F, 14.0F, 2.0F);
            this.wolfBody.rotateAngleX = ((float)Math.PI / 2F);
            this.wolfMane.setRotationPoint(-1.0F, 14.0F, -3.0F);
            this.wolfMane.rotateAngleX = this.wolfBody.rotateAngleX;
            this.wolfTail.setRotationPoint(-1.0F, 12.0F, 8.0F);
            this.wolfLeg1.setRotationPoint(-2.5F, 16.0F, 7.0F);
            this.wolfLeg2.setRotationPoint(0.5F, 16.0F, 7.0F);
            this.wolfLeg3.setRotationPoint(-2.5F, 16.0F, -4.0F);
            this.wolfLeg4.setRotationPoint(0.5F, 16.0F, -4.0F);
            this.wolfLeg1.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            this.wolfLeg2.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
            this.wolfLeg3.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
            this.wolfLeg4.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        }

        this.wolfHeadMain.rotateAngleZ = entityIn.getInterestedAngle(partialTick) + entityIn.getShakeAngle(partialTick, 0.0F);
        this.wolfMane.rotateAngleZ = entityIn.getShakeAngle(partialTick, -0.08F);
        this.wolfBody.rotateAngleZ = entityIn.getShakeAngle(partialTick, -0.16F);
        this.wolfTail.rotateAngleZ = entityIn.getShakeAngle(partialTick, -0.2F);
    }

    public void setRotationAngles(PetEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        this.wolfHeadMain.rotateAngleX = headPitch * ((float)Math.PI / 180F);
        this.wolfHeadMain.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
        this.wolfTail.rotateAngleX = ageInTicks;
    }
}
