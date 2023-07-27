package de.neocraftr.griefergames.listener;

import de.neocraftr.griefergames.GrieferGames;
import de.neocraftr.griefergames.chat.events.GGSubServerChangeEvent;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.network.server.ServerData;
import net.labymod.api.event.Subscribe;
import net.labymod.api.thirdparty.discord.DiscordActivity;
import net.labymod.api.util.I18n;
import net.labymod.core.labyconnect.DefaultLabyConnect;
import net.labymod.core.labyconnect.protocol.packets.PacketPlayServerStatusUpdate;
import net.labymod.core.thirdparty.discord.DefaultDiscordApp;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class GGSubServerChangeListener {
  private final GrieferGames griefergames;

  public GGSubServerChangeListener(GrieferGames griefergames) {
    this.griefergames = griefergames;
  }

  @Subscribe
  public void onSubServerChange(GGSubServerChangeEvent event) {
    String formattedServerName = griefergames.helper().formatServerName(event.subServerName());

    griefergames.boosterController().resetBoosters();

    if(!griefergames.configuration().automations().autoPortal().get() || !griefergames.isFirstTimeInLobby()) {
      // Discord RPC
      if(griefergames.configuration().friends().discordShowSubServerEnabled().get()) {
        DiscordActivity previousActivety = Laby.references().discordApp().getDisplayedActivity();
        if(previousActivety != null) {
          DiscordActivity activety = DiscordActivity.builder(griefergames, previousActivety).state(formattedServerName).start().build();
          DefaultDiscordApp discordApp = (DefaultDiscordApp) Laby.references().discordApp();
          discordApp.displayServerActivity(Laby.labyAPI().serverController().getCurrentServerData(), activety);
        }
      }

      // LabyChat Status
      if(griefergames.configuration().friends().labyChatShowSubServerEnabled().get() &&
          Laby.references().labyConnect().isConnected()) {
        ServerData serverData = Laby.labyAPI().serverController().getCurrentServerData();
        PacketPlayServerStatusUpdate packet = new PacketPlayServerStatusUpdate(serverData.address().getHost(), serverData.address().getPort(), "GrieferGames "+formattedServerName, false);
        DefaultLabyConnect labyConnect = (DefaultLabyConnect) Laby.references().labyConnect();
        labyConnect.sendPacket(packet);
      }
    }

    if(griefergames.helper().isCityBuild(event.subServerName())) {
      if(!griefergames.isCitybuildDelay()) griefergames.setWaitTime(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(15));
      griefergames.setCitybuildDelay(false);

      griefergames.displayAddonMessage(Component.text(
              I18n.translate(griefergames.namespace()+".messages.citybuildJoin").replace("{citybuild}", formattedServerName),
              NamedTextColor.GRAY
          ));
    } else if(event.subServerName().equals("portal")) {
      if(!griefergames.isCitybuildDelay()) griefergames.setWaitTime(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(12));
    } else if(event.subServerName().equals("skyblock")) {
      if(!griefergames.isCitybuildDelay()) griefergames.setWaitTime(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(15));
    } else if(event.subServerName().equals("lobby")) {
      if(griefergames.isFirstTimeInLobby() && griefergames.configuration().automations().autoPortal().get()) {
        new Timer().schedule(new TimerTask() {
          @Override
          public void run() {
            griefergames.sendMessage("/portal");
          }
        }, 500);
      }
    }

    griefergames.setFirstTimeInLobby(false);
  }
}
