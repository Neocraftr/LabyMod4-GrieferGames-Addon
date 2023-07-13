package de.neocraftr.griefergames.listener;

import de.neocraftr.griefergames.GrieferGames;
import de.neocraftr.griefergames.chat.events.GGSubServerChangeEvent;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.event.Subscribe;
import net.labymod.api.util.I18n;
import java.util.concurrent.TimeUnit;

public class GGSubServerChangeListener {
  private final GrieferGames griefergames;

  public GGSubServerChangeListener(GrieferGames griefergames) {
    this.griefergames = griefergames;
  }

  @Subscribe
  public void onSubServerChange(GGSubServerChangeEvent event) {
    if(griefergames.helper().isCityBuild(event.subServerName())) {
      if(!griefergames.isCitybuildDelay()) griefergames.setWaitTime(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(15));
      griefergames.setCitybuildDelay(false);

      griefergames.displayAddonMessage(Component.text(
              I18n.translate("griefergames.messages.citybuildJoin").replace("{citybuild}", griefergames.helper().formatServerName(event.subServerName())),
              NamedTextColor.GRAY
          ));
    } else if(event.subServerName().equals("portal")) {
      if(!griefergames.isCitybuildDelay()) griefergames.setWaitTime(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(12));
    } else if(event.subServerName().equals("skyblock")) {
      if(!griefergames.isCitybuildDelay()) griefergames.setWaitTime(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(15));
    }
  }
}
