package bigbade.battlepets.client.models;

import bigbade.battlepets.entities.PetEntity;
import net.minecraft.client.renderer.entity.model.PigModel;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class ModelPig extends PigModel<PetEntity> {
    @Override
    public void setLivingAnimations(PetEntity pet, float par2, float par3, float par4) {
        super.setLivingAnimations(pet, par2, par3, par4);

        if (pet.isSitting()) {
            field_78149_c.rotateAngleZ = 3.14f / 2;
            field_78146_d.rotateAngleZ = -3.14f / 2;
            field_78147_e.rotateAngleZ = 3.14f / 2;
            field_78144_f.rotateAngleZ = -3.14f / 2;
        } else {
            field_78149_c.rotateAngleZ = 0;
            field_78146_d.rotateAngleZ = 0;
            field_78147_e.rotateAngleZ = 0;
            field_78144_f.rotateAngleZ = 0;
        }
    }

    @Override
    public void render(PetEntity pet, float par2, float par3, float par4, float par5, float par6, float par7) {
        GL11.glPushMatrix();

        if (pet.isSitting()) {
            GL11.glTranslatef(0.f, 0.35f, 0.f);
        }
        super.render(pet, par2, par3, par4, par5, par6, par7);

        GL11.glPopMatrix();
    }
}
