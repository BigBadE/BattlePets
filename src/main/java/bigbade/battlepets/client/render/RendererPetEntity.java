package bigbade.battlepets.client.render;

import bigbade.battlepets.BattlePets;
import bigbade.battlepets.api.PetType;
import bigbade.battlepets.client.models.*;
import bigbade.battlepets.entities.PetEntity;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.CatModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

import static org.lwjgl.opengl.GL11.*;

public class RendererPetEntity extends MobRenderer<PetEntity, EntityModel<PetEntity>> {
    public RendererPetEntity() {
        super(Minecraft.getInstance().getRenderManager(), new CatModel(0.0f), 0.45f);
        this.addLayer(new PetCollarLayer(this));
    }

    @Override
    public void doRender(PetEntity pet, double par2, double par4, double par6, float par8, float par9) {
        if (pet.getPetType() == PetType.DOG && pet.isWet()) {
            float f = pet.getBrightness() * pet.getShadingWhileWet(par9);
            GlStateManager.color3f(f, f, f);
        }

        if (pet.getPetType().equals(PetType.CAT)) {
            field_77045_g = catModel;
        } else if (pet.getPetType().equals(PetType.DOG)) {
            field_77045_g = dogModel;
        } else if (pet.getPetType().equals(PetType.PIG)) {
            field_77045_g = pigModel;
        } else if (pet.getPetType().equals(PetType.SLIME)) {
            field_77045_g = slimeModel;
            if (pet.getTexture().contains("magma")) {
                field_77045_g = magmaCubeModel;
            }
        } else if (pet.getPetType().equals(PetType.SILVERFISH)) {
            field_77045_g = silverfishModel;
        }
        Minecraft.getInstance().getTextureManager().bindTexture(getEntityTexture(pet));
        super.doRender(pet, par2, par4, par6, par8, par9);

        // TODO: Render armor

        if (hasSaddle(pet)) {
            Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("battlepets", "textures/entity/" + pet.getPetType().name().toLowerCase() + "/saddle.png"));
            super.doRender(pet, par2, par4, par6, par8, par9);
        }

        //TODO pet armor
        /*
        Minecraft.getMinecraft().renderEngine.bindTexture( new ResourceLocation( "usefulpets:textures/entity/" + pet.getPetType().name + "/armorDiamond.png" ) );
        doRenderLiving( ( EntityLiving ) entity, par2, par4, par6, par8, par9 );
        */

    }

    @Override
    protected ResourceLocation getEntityTexture(PetEntity entity) {
        return new ResourceLocation("battlepets", entity.getTexture());
    }

    @Override
    protected boolean bindEntityTexture(PetEntity par1Entity) {
        this.bindTexture(this.getEntityTexture(par1Entity));
        return true;
    }

    @Override
    protected void preRenderCallback(PetEntity pet, float partialTickTime) {
        int health = (int) pet.getHealth();
        int maxHealth = (int) pet.getMaxHealth();

        if (hasSaddle(pet)) {
            justRendered = !justRendered;
            if (!justRendered) {
                return;
            }
        }

        if (!BattlePets.FANCY_STAT_RENDERER) {
            String hp = "HP: " + health + "/" + maxHealth;
            String food = "Food: " + ((int) pet.getHunger()) + "/" + 20;

            // renderLivingLabel ?
            renderLivingLabel(pet, hp, pet.posX, pet.posY, pet.posZ, 4);
            renderLivingLabel(pet, food, pet.posX, pet.posY - 0.3f, pet.posZ, 4);
        } else {
            double dist = pet.getDistance(Minecraft.getInstance().pointedEntity);
            if (dist > 4.f) {
                return;
            }

            glPushMatrix();
            {
                glTranslated(pet.posX, pet.posY + pet.getHeight() + 0.25f, pet.posZ);
                glNormal3f(0.f, 1.f, 0.f);
                glRotatef(-renderManager.playerViewY, 0.f, 1.f, 0.f);
                glRotatef(renderManager.playerViewX, 1.f, 0.f, 0.f);
                //glScalef( 1.f, 1.f, 1.f );
                glDisable(GL_LIGHTING);
                glDepthMask(false);
                glDisable(GL_DEPTH_TEST);
                glEnable(GL_BLEND);
                glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                glDisable(GL_TEXTURE_2D);

                drawBar(1.f, 1.f, 0.5f, 0.f, 0.f);
                drawBar(1.f, health / (float) maxHealth, 1.f, 0.f, 0.f);
                glTranslatef(0.f, -0.05f, 0.f);
                drawBar(1.f, 1.f, 0.f, 0.25f, 0.f);
                drawBar(1.f, pet.getHunger() / 20, 0.f, 1.f, 0.f);

                glEnable(GL_TEXTURE_2D);
                glDepthMask(true);
                glEnable(GL_DEPTH_TEST);
                glEnable(GL_LIGHTING);
                glDisable(GL_BLEND);
                glColor3f(1.f, 1.f, 1.f);
            }
            glPopMatrix();

            if (pet.hasCustomName()) {
                renderLivingLabel(pet, pet.getCustomName().getFormattedText(), pet.posX, pet.posY, pet.posZ, 4);
            }
        }
    }

    private boolean hasSaddle(PetEntity pet) {
        //ItemStack saddleStack = pet.getPetInventory().getStackInSlot(0);
        //return pet.hasSkill(Skill.TRAVEL_MOUNTABLE.id) && saddleStack != null && saddleStack.getItem() == Items.SADDLE;
        return true;
    }

    private void drawBar(float width, float percent, float r, float g, float b) {
        float halfWidth = width / 2;
        float amount = width * percent;

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder renderer = tess.getBuffer();
        GlStateManager.color4f(r, g, b, 1F);
        renderer.begin(7, DefaultVertexFormats.POSITION);
        renderer.pos(halfWidth, -0.05f, 0.f);
        renderer.pos(halfWidth - amount, -0.05f, 0.f);
        renderer.pos(halfWidth - amount, 0.f, 0.f);
        renderer.pos(halfWidth, 0.f, 0.f);
        tess.draw();
    }

    private final EntityModel catModel = new ModelCat(0.0f);
    private final EntityModel dogModel = new ModelDog();
    private final EntityModel pigModel = new ModelPig();
    private final EntityModel slimeModel = new ModelSlime();
    private final EntityModel magmaCubeModel = new ModelMagmaCube();
    private final EntityModel silverfishModel = new ModelSilverfish();

    private boolean justRendered = false;
}
