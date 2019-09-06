package bigbade.battlepets.client.gui;

import bigbade.battlepets.api.PetType;
import bigbade.battlepets.containers.PetContainer;
import bigbade.battlepets.entities.PetEntity;
import bigbade.battlepets.skills.Skill;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkHooks;
import org.lwjgl.opengl.GL11;

public class PetScreen extends ContainerScreen<PetContainer> {
    private static final ResourceLocation GUITEXTURE = new ResourceLocation("battlepets:textures/gui/pet.png");
    private IInventory field_110413_u;
    private IInventory field_110412_v;
    private PetEntity pet;
    private float mouseX;
    private float mouseY;
    private int currTexIndex = 0;
    private Button texButton;

    private static final int SKILLS_BUTTON_ID = 0;
    private static final int TEXTURE_BUTTON_ID = 1;

    public PetScreen(PetContainer container, PlayerInventory playerInventory, ITextComponent name) {
        super(container, playerInventory, container.getName());
    }

    @Override
    public void init() {
        super.init();

        Minecraft mc = Minecraft.getInstance();

        buttons.clear();
        buttons.add(new Button(SKILLS_BUTTON_ID, guiLeft + 8, guiTop + 39, 48, new TranslationTextComponent("gui.pet.skills").getFormattedText(), (button) -> {
            mc.player.closeScreen();
            //TODO skill inv
            //NetworkHooks.openGui(mc.player, new CulinaryWorkbenchContainerProvider(pos));
        }));

        buttons.add(texButton = new Button(TEXTURE_BUTTON_ID, guiLeft + 8, guiTop - 28, xSize - 16, "<...>", (button) -> {
            PetType type = pet.getPetType();
            if (++currTexIndex >= type.textures.length) {
                currTexIndex = 0;
            }

            String tex = type.textures[currTexIndex];
            pet.setTexture(tex);

            updateTextureButton(false);
        }));
        updateTextureButton(true);
    }


    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
        drawString(fontRenderer, container.getName().getFormattedText(), 8, 6, 4210752);
        drawString(fontRenderer, playerInventory.hasCustomName() ? playerInventory.getCustomName().getFormattedText() : playerInventory.getName().getFormattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bindTexture(GUITEXTURE);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        blit(k, l, 0, 0, this.xSize, this.ySize);

        if (pet.hasSkill(Skill.INVENTORY_ARMOR.id)) {
            blit(k + 7 + 18, l + 17, 0, 220, 18, 18);
        }
        if (pet.hasSkill(Skill.TRAVEL_MOUNTABLE.id)) {
            blit(k + 7, l + 17, 18, 220, 18, 18);
        }
        if (pet.hasSkill(Skill.INVENTORY_WEAPON.id)) {
            blit(k + 7 + 36, l + 17, 36, 220, 18, 18);
        }

        if (pet.hasSkill(Skill.INVENTORY.id)) {
            int count = (pet.getPetInventory().getSizeInventory() - 3) / 3;

            int rows = Math.min(count, 3);
            blit(k + 115, l + 17, 0, 166, 54, 18 * rows);

            int cols = Math.max(count - 3, 0);
            blit(k + 115 + 18 * 3, l + 17, 18 * 3, 166, 18 * cols, 54);
        }

        InventoryScreen.drawEntityOnScreen(k + 88, l + 60, 17, (float) (k + 51) - this.mouseX, (float) (l + 75 - 50) - this.mouseY, this.pet);
    }

    @Override
    public void render(int mx, int my, float par3) {
        this.mouseX = (float) mx;
        this.mouseY = (float) my;
        super.render(mx, my, par3);
    }

    private void updateTextureButton(boolean initial) {
        if (initial) {
            int tex = 0;
            for(int i = 0; i < pet.getPetType().textures.length; i++)
                if(pet.getPetType().textures[i].equals(pet.getTexture()))
                    tex = i;
            currTexIndex = tex;
        }

        String full = pet.getTexture();
        String file = full.substring(full.lastIndexOf('/') + 1);
        String filename = file.substring(0, file.lastIndexOf('.'));

        texButton.setMessage(new TranslationTextComponent("pet.battlepets.texture").getFormattedText().replace("%s", new TranslationTextComponent("pet.texture." + pet.getPetType().name().toLowerCase() + "." + filename).getFormattedText()));
    }
}
