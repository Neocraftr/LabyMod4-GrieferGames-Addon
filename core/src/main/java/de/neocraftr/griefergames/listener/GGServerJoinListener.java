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
      griefergames.setLastActivety(System.currentTimeMillis());
      griefergames.setAfk(false);
      griefergames.setWaitTime(0);
      griefergames.setCitybuildDelay(false);
      griefergames.setNickname(null);
      griefergames.setHideBoosterMenu(false);
      griefergames.setFirstTimeInLobby(true);
      griefergames.helper().findSecondChat("2nd Chat");

      Laby.labyAPI().notificationController().push(Notification.builder()
          .title(Component.text("GrieferGames-Addon", NamedTextColor.GOLD))
          .text(Component.text(I18n.translate(griefergames.namespace()+".notifications.addonLoaded").replace("{version}", "v"+griefergames.addonInfo().getVersion())))
          .icon(Icon.texture(ResourceLocation.create(griefergames.namespace(), "textures/icon.png"))).build());

      // Warn user when advanced chat is disabled.
      if(!Laby.labyAPI().config().ingame().advancedChat().enabled().get()) {
        griefergames.displayAddonMessage(Component.text(I18n.translate(griefergames.namespace()+".messages.advancedChatWarning"),
            Style.builder().color(NamedTextColor.RED).decorate(TextDecoration.BOLD).build()));
        Laby.labyAPI().notificationController().push(Notification.builder()
            .title(Component.text("GrieferGames-Addon", NamedTextColor.GOLD))
            .text(Component.text(I18n.translate(griefergames.namespace()+".notifications.generalError"), NamedTextColor.RED))
            .icon(Icon.texture(ResourceLocation.create(griefergames.namespace(), "textures/error.png"))).build());
      }

      if(griefergames.updater().isUpdateAvailable()) {
        if(griefergames.configuration().autoUpdate().get()) {
          Laby.labyAPI().notificationController().push(Notification.builder()
              .title(Component.text("GrieferGames-Addon", NamedTextColor.GOLD))
              .text(Component.text(I18n.translate(griefergames.namespace()+".notifications.update.ready").replace("{version}", "v"+griefergames.updater().getNewVersion())))
              .icon(Icon.texture(ResourceLocation.create(griefergames.namespace(), "textures/update.png"))).build());
        } else {
          Laby.labyAPI().notificationController().push(Notification.builder()
              .title(Component.text("GrieferGames-Addon", NamedTextColor.GOLD))
              .text(Component.text(I18n.translate(griefergames.namespace()+".notifications.update.available").replace("{version}", "v"+griefergames.updater().getNewVersion())))
              .icon(Icon.texture(ResourceLocation.create(griefergames.namespace(), "textures/update.png"))).build());
        }
      }
    }
  }
}
