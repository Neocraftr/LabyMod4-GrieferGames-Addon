package de.neocraftr.griefergames.listener;

import de.neocraftr.griefergames.GrieferGames;
import de.neocraftr.griefergames.chat.events.GGSubServerChangeEvent;
import de.neocraftr.griefergames.enums.SubServerType;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.scoreboard.TabList;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.scoreboard.TabListUpdateEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GGTablistListener {
  private final GrieferGames griefergames;
  private final Pattern tabListServerRegex = Pattern.compile("Current Server: (.+?)-", Pattern.MULTILINE);

  public GGTablistListener(GrieferGames griefergames) {
    this.griefergames = griefergames;
  }

  @Subscribe
  public void onTablistUpdate(TabListUpdateEvent event) {
    TabList tablist = Laby.labyAPI().minecraft().getTabList();
    if(tablist == null) return;
    Component header = tablist.header();
    if(header != null) {
      String headerText = griefergames.helper().componentToPlainText(header);
      Matcher matcher = tabListServerRegex.matcher(headerText);
      if(matcher.find()) {
        String subServerName = "cloud_"+matcher.group(1).toLowerCase();
        griefergames.setSubServerType(SubServerType.CLOUD);
        if(!griefergames.getSubServer().equals(subServerName)) {
          griefergames.setSubServer(subServerName);
          GGSubServerChangeEvent changeEvent = new GGSubServerChangeEvent(subServerName);
          Laby.labyAPI().eventBus().fire(changeEvent);
        }
      }
    }
  }
}
