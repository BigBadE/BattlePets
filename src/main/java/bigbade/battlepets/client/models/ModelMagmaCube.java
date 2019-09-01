package bigbade.battlepets.client.models;

import bigbade.battlepets.entities.PetEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.MagmaCubeModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class ModelMagmaCube extends EntityModel<PetEntity> {
	private final RendererModel[] segments = new RendererModel[8];
	private final RendererModel core;
	public ModelMagmaCube() {
		for(int i = 0; i < this.segments.length; ++i) {
			int j = 0;
			int k = i;
			if (i == 2) {
				j = 24;
				k = 10;
			} else if (i == 3) {
				j = 24;
				k = 19;
			}

			this.segments[i] = new RendererModel(this, j, k);
			this.segments[i].addBox(-4.0F, (float)(16 + i), -4.0F, 8, 1, 8);
		}

		this.core = new RendererModel(this, 0, 16);
		this.core.addBox(-2.0F, 18.0F, -2.0F, 4, 4, 4);
	}
    @Override
    public void setLivingAnimations(PetEntity entity, float par2, float par3, float partialTick) {
		//float f = MathHelper.lerp(partialTick, entity.prevSquishFactor, entity.squishFactor);
		float f = 0;
		//if (f < 0.0F) {
		//	f = 0.0F;
		//}

		for(int i = 0; i < this.segments.length; ++i) {
			this.segments[i].rotationPointY = (float)(-(4 - i)) * f * 1.7F;
		}
    }

	@Override
	public void render(PetEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		GL11.glPushMatrix();

		PetEntity pet = (PetEntity) entity;
		if (pet.isSitting()) {
			GL11.glTranslatef(0, 0.75f, 0);
			GL11.glScalef(1.4f, 0.5f, 1.4f);
		}

		// Does it need all this slime code? I have no clue.
		GL11.glEnable(GL11.GL_NORMALIZE);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1, 1, 1, 1);
		this.setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		this.core.render(scale);

		for (RendererModel renderermodel : segments) {
			renderermodel.render(scale);
		}
		GL11.glDisable(GL11.GL_BLEND);

		GL11.glPopMatrix();
	}
}
