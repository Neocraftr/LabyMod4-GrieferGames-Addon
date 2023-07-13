package de.neocraftr.griefergames.listener;

import de.neocraftr.griefergames.GrieferGames;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatMessageSendEvent;
import net.labymod.api.util.I18n;

public class GGMessageSendListener {
  private final GrieferGames griefergames;
  private String lastMessage = "";

  public GGMessageSendListener(GrieferGames griefergames) {
    this.griefergames = griefergames;
  }

  @Subscribe
  public void onSend(ChatMessageSendEvent event) {
    if(!griefergames.isOnGrieferGames()) return;

    if(griefergames.configuration().chatConfig().preventCommandFailure().get()) {
      if(event.getMessage().startsWith("7") && !event.getMessage().equalsIgnoreCase(lastMessage)) {
        griefergames.displayAddonMessage(Component.text(I18n.translate("griefergames.messages.commandFailure"), NamedTextColor.RED));
        lastMessage = event.getMessage();
        event.setCancelled(true);
      } else {
        lastMessage = "";
      }
    }
  }
}
