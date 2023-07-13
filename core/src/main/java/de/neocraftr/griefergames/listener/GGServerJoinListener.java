package de.neocraftr.griefergames.listener;

import de.neocraftr.griefergames.GrieferGames;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.Style;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.ServerJoinEvent;
import net.labymod.api.notification.Notification;
import net.labymod.api.util.I18n;
import java.util.Timer;
import java.util.TimerTask;

public class GGServerJoinListener {
  private final GrieferGames griefergames;

  public GGServerJoinListener(GrieferGames griefergames) {
    this.griefergames = griefergames;
  }

  @Subscribe
  public void onServerJoin(ServerJoinEvent event) {
    if(event.serverData().address().getHost().endsWith("griefergames.net") ||
        event.serverData().address().getHost().endsWith("griefergames.de")) {
      griefergames.setOnGrieferGames(true);
      griefergames.helper().findSecondChat("2nd Chat");

      Laby.labyAPI().notificationController().push(Notification.builder()
          .title(Component.text("GrieferGames-Addon", NamedTextColor.GOLD))
          .text(Component.text(I18n.translate("griefergames.notifications.addonLoaded")))
          .icon(Icon.texture(ResourceLocation.create("griefergames", "textures/icon.png"))).build());

      // Warn user when advanced chat is disabled.
      if(!Laby.labyAPI().config().ingame().advancedChat().enabled().get()) {
        griefergames.displayAddonMessage(Component.text(I18n.translate("griefergames.messages.advancedChatWarning"),
            Style.builder().color(NamedTextColor.RED).decorate(TextDecoration.BOLD).build()));
        Laby.labyAPI().notificationController().push(Notification.builder()
            .title(Component.text("GrieferGames-Addon", NamedTextColor.GOLD))
            .text(Component.text(I18n.translate("griefergames.notifications.generalError"), NamedTextColor.RED))
            .icon(Icon.texture(ResourceLocation.create("griefergames", "textures/icon.png"))).build());
      }

      if(griefergames.configuration().automations().autoPortal().get()) {
        new Timer().schedule(new TimerTask() {
          @Override
          public void run() {
            griefergames.sendMessage("/portal");
          }
        }, 500);
      }
    }
  }
}
