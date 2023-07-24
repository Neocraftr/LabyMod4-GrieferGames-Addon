package de.neocraftr.griefergames.listener;

import de.neocraftr.griefergames.GrieferGames;
import de.neocraftr.griefergames.chat.events.GGChatProcessEvent;
import de.neocraftr.griefergames.enums.SubServerType;
import net.labymod.api.Laby;
import net.labymod.api.configuration.labymod.chat.AdvancedChatMessage;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.event.client.chat.advanced.AdvancedChatTabMessageEvent;

public class GGMessageReceiveListener {
  private final GrieferGames griefergames;

  public GGMessageReceiveListener(GrieferGames griefergames) {
    this.griefergames = griefergames;
  }

  @Subscribe
  public void onMessage(ChatReceiveEvent event) {
    if(!griefergames.isOnGrieferGames()) return;
    //System.out.println(GsonComponentSerializer.gson().serialize(event.message()));

    if(griefergames.getSubServerType() == SubServerType.REGULAR) {
      GGChatProcessEvent processEvent = new GGChatProcessEvent(event.chatMessage());
      Laby.labyAPI().eventBus().fire(processEvent);
      if(processEvent.isCancelled()) {
        event.setCancelled(true);
      } else if(processEvent.isSecondChat()) {
        griefergames.helper().displayInSecondChat(AdvancedChatMessage.chat(processEvent.getMessage()));
        if(!processEvent.isKeepInRegularChat()) event.setCancelled(true);
      }

      if(event.chatMessage().getPlainText().equals("[Switcher] Daten heruntergeladen!")) {
        griefergames.setHideBoosterMenu(true);
        griefergames.sendMessage("/booster");
      }
    }
  }

  @Subscribe
  public void onTabMessage(AdvancedChatTabMessageEvent event) {
    if(event.message().chatMessage().metadata().has("gg_custom_background")) {
      event.message().metadata().set("custom_background",
          (Integer)event.message().chatMessage().metadata().get("gg_custom_background"));
    }
  }
}
