package bigbade.battlepets.network.packet;

import bigbade.battlepets.entities.PetEntity;
import bigbade.battlepets.network.BattlepetsNetworkHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class TextureChangePacket {
    private final String texture;
    private final int entityID;

    public TextureChangePacket(PacketBuffer buf) {
        this.texture = buf.readString();
        this.entityID = buf.readInt();
    }

    public TextureChangePacket(String texture, int entityID) {
        this.texture = texture;
        this.entityID = entityID;
    }

    public void encode(PacketBuffer buf) {
        buf.writeString(texture);
        buf.writeInt(entityID);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        ((PetEntity) context.get().getSender().world.getEntityByID(entityID)).setTexture(texture);
        context.get().setPacketHandled(true);
    }
}
