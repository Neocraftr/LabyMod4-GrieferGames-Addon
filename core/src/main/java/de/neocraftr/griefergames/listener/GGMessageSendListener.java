package de.neocraftr.griefergames.listener;

import de.neocraftr.griefergames.GrieferGames;
import de.neocraftr.griefergames.enums.ChatColor;
import de.neocraftr.griefergames.enums.SubServerType;
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
    String msg = event.getMessage();

    if(griefergames.getSubServerType() == SubServerType.REGULAR) {
      if(griefergames.configuration().chatConfig().preventCommandFailure().get()) {
        if(msg.startsWith("7") && !msg.equalsIgnoreCase(lastMessage)) {
          griefergames.displayAddonMessage(Component.text(I18n.translate(griefergames.namespace()+".messages.commandFailure"), NamedTextColor.RED));
          lastMessage = msg;
          event.setCancelled(true);
        } else {
          lastMessage = "";
        }
      }

      if(griefergames.configuration().automations().autoColor().get() != ChatColor.NONE &&
          !msg.startsWith("/") && !msg.startsWith(".") && !msg.startsWith("-")) {
        event.changeMessage("&"+griefergames.configuration().automations().autoColor().get().getColorCode()+msg);
      }
    }

    if (griefergames.getSubServerType() == SubServerType.CLOUD) {
      if (griefergames.configuration().chatConfig().correctCommandCapitalisation().get()) {
        if (msg.startsWith("/")) {
          String[] parts = msg.split(" ");
          String newMsg = parts[0].toLowerCase() + msg.replace(parts[0], "");
          event.changeMessage(newMsg, newMsg);
        }
      }
    }
  }
}