package de.neocraftr.griefergames.listener;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.neocraftr.griefergames.GrieferGames;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.serializer.legacy.LegacyComponentSerializer;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.NetworkPayloadEvent;
import net.labymod.api.event.client.network.server.NetworkPayloadEvent.Side;
import net.labymod.core.main.LabyMod;
import net.labymod.core.main.serverapi.model.LabyModSubtitle;
import net.labymod.serverapi.protocol.model.display.Subtitle;
import net.labymod.serverapi.protocol.payload.io.PayloadReader;
import java.util.UUID;

public class GGServerMessageListener {
  private final GrieferGames griefergames;
  private final Gson gson = new Gson();

  public GGServerMessageListener(GrieferGames griefergames) {
    this.griefergames = griefergames;
  }

  @Subscribe
  public void onServerMessage(NetworkPayloadEvent event) {
    if(!griefergames.isOnGrieferGames()) return;

    if(event.side() == Side.RECEIVE &&
        event.identifier().getNamespace().equals("mysterymod") &&
        event.identifier().getPath().equals("mm")) {

      PayloadReader reader = new PayloadReader(event.getPayload());
      String messageKey = reader.readString();
      String messageJson = reader.readString();

      //System.out.println(messageKey+" "+messageJson);

      JsonElement message;
      try {
        message = gson.fromJson(messageJson, JsonElement.class);
      } catch(Exception e) {
        System.err.println("Exception while parsing MysteryMod message: exception="+e.getMessage()+", message="+messageJson);
        return;
      }

      /*if(messageKey.equals("mysterymod_user_check")) {
        // Activate MysteryMod perks
        Laby.labyAPI().serverController().sendPayload(event.identifier(), event.getPayload());
      }*/

      if(messageKey.equals("redstone")) {
        String redstoneState = message.getAsJsonObject().get("status").getAsString();
        griefergames.setRedstoneActive(redstoneState.equals("0"));
      }

      if(messageKey.equals("user_subtitle")) {
        JsonObject subtitleData = message.getAsJsonArray().get(0).getAsJsonObject();

        String text = Laby.labyAPI().minecraft().componentMapper().translateColorCodes(subtitleData.get("text").getAsString());
        Component component = LegacyComponentSerializer.legacySection().deserialize(text);

        Subtitle subtitle = new LabyModSubtitle(UUID.fromString(subtitleData.get("targetId").getAsString()), 1.2, component);
        LabyMod.references().subtitleService().addSubtitle(subtitle);
      }
    }
  }
}
