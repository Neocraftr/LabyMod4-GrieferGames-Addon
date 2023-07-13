package de.neocraftr.griefergames.listener;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import de.neocraftr.griefergames.GrieferGames;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.NetworkPayloadEvent;
import net.labymod.api.event.client.network.server.NetworkPayloadEvent.Side;
import net.labymod.serverapi.protocol.payload.io.PayloadReader;

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
    }
  }
}
