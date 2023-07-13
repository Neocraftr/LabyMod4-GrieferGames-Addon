package de.neocraftr.griefergames.utils;

import de.neocraftr.griefergames.GrieferGames;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.filter.ChatFilter;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.screen.widget.attributes.bounds.Bounds;
import net.labymod.api.client.gui.screen.widget.attributes.bounds.PositionedBounds;
import net.labymod.api.configuration.labymod.chat.AdvancedChatMessage;
import net.labymod.api.configuration.labymod.chat.ChatTab;
import net.labymod.api.configuration.labymod.chat.ChatWindow;
import net.labymod.api.configuration.labymod.chat.category.GeneralChatTabConfig;
import net.labymod.api.configuration.labymod.chat.config.ChatWindowConfig;
import net.labymod.api.configuration.labymod.chat.config.RootChatTabConfig;
import net.labymod.api.configuration.labymod.chat.config.RootChatTabConfig.Type;
import net.labymod.api.util.bounds.DefaultRectangle;
import net.labymod.api.util.bounds.MutableRectangle;
import net.labymod.core.client.chat.advanced.DefaultChatWindow;
import net.labymod.core.configuration.labymod.chat.IngameChatTab;
import net.labymod.core.main.LabyMod;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {
  private final GrieferGames griefergames;
  private final List<String> cityBuilds = Arrays.asList("extreme", "evil", "nature", "lava", "wasser", "event");
  private Pattern serverCityBuildRegex = Pattern.compile("^cb([0-9]+)$");

  public Helper(GrieferGames griefergames) {
    this.griefergames = griefergames;
  }

  public void findSecondChat(String name) {
    IngameChatTab secondChat = null;

    for(ChatWindow window : LabyMod.references().advancedChatController().getWindows()) {
      for(ChatTab tab : window.getTabs()) {
        if(tab.getName().equalsIgnoreCase(name) && tab instanceof IngameChatTab) {
          secondChat = (IngameChatTab) tab;
        }
      }
    }

    if(secondChat == null) {
      secondChat = createNewSecondChat(name);
    }

    if(secondChat.config().filters().get().isEmpty()) {
      // Create dummy filter to prevent LabyMod from sending alle messages to the second chat
      ChatFilter defaultChatFilter = new ChatFilter();
      defaultChatFilter.name().set("GrieferGames-Addon");
      defaultChatFilter.getIncludedTags().add("§chzgwefegsdrutjugiuteghuzazghwu");
      secondChat.config().filters().get().add(defaultChatFilter);
    }

    griefergames.setSecondChat(secondChat);
  }

  public IngameChatTab createNewSecondChat(String name) {
    RootChatTabConfig tabConfig = new RootChatTabConfig(0, Type.CUSTOM, new GeneralChatTabConfig(name));
    ChatWindowConfig config = new ChatWindowConfig(tabConfig);

    Bounds bounds = new PositionedBounds(1000, 1000, 300, 160);
    MutableRectangle rectangle = new DefaultRectangle();
    rectangle.setRight(300);
    rectangle.setBottom(160);
    config.setPosition(bounds, rectangle);

    DefaultChatWindow window = new DefaultChatWindow(config);
    LabyMod.references().advancedChatController().addWindow(window);
    return (IngameChatTab)window.initializeTab(tabConfig, null, false);
  }

  public void displayInSecondChat(AdvancedChatMessage message) {
    int limit = griefergames.getSecondChat().config().chatLimit().get();
    for (int i=griefergames.getSecondChat().getMessages().size(); i >= limit; --i) {
      griefergames.getSecondChat().getMessages().remove(i - 1);
    }
    griefergames.getSecondChat().getMessages().add(0, message);
  }

  public String componentToPlainText(Component component) {
    StringBuilder builder = new StringBuilder();
    Laby.references().componentRenderer().getColorStrippingFlattener().flatten(component, builder::append);
    return builder.toString();
  }

  public String componentToFormattedText(Component component) {
    return Laby.references().componentRenderer().legacySectionSerializer().serialize(component);
  }

  public boolean isCityBuild(String serverName) {
    if(serverName.equals("cb0")) return false;
    if(serverName.startsWith("cb")) return true;
    return cityBuilds.contains(serverName);
  }

  public String formatServerName(String serverName) {
    if (serverName == null || serverName.isBlank()) return "";

    if(serverName.equals("event")) {
      return "Event Server";
    } else if (serverName.equals("cb0")) {
      return "CityBuild Zero";
    } else if (serverName.equals("cbe")) {
      return "CityBuild Evil";
    } else if (serverName.equals("extreme") || serverName.equals("nature")) {
      return "CityBuild " + capitalize(serverName);
    } else if (serverName.equals("lava") || serverName.equals("wasser")) {
      return "Farmserver " + capitalize(serverName);
    } else {
      Matcher matcher = serverCityBuildRegex.matcher(serverName.toLowerCase());
      if (matcher.find()) {
        String cbNum = matcher.group(1);
        return "CityBuild " + cbNum;
      } else {
        return capitalize(serverName);
      }
    }
  }

  public String capitalize(String str) {
    return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
  }
}
