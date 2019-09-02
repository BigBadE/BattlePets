package bigbade.battlepets.client.models;

import bigbade.battlepets.entities.PetEntity;
import net.minecraft.client.renderer.entity.model.SlimeModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class ModelSlime extends SlimeModel<PetEntity> {
    public ModelSlime() {
        super(16);
    }

    @Override
    public void render(PetEntity pet, float par2, float par3, float par4, float par5, float par6, float par7) {

        GL11.glPushMatrix();


        if (pet.isSitting()) {
            GL11.glTranslatef(0, 0.75f, 0);
            GL11.glScalef(1.4f, 0.5f, 1.4f);
        }
        GL11.glEnable(GL11.GL_NORMALIZE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1, 1, 1, 1);
        super.render(pet, par2, par3, par4, par5, par6, par7);
        child.render(pet, par2, par3, par4, par5, par6, par7);
        GL11.glDisable(GL11.GL_BLEND);

        GL11.glPopMatrix();
    }

    private SlimeModel child = new SlimeModel<SlimeEntity>(0);
}
