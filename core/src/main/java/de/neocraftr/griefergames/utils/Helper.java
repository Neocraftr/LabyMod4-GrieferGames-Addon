package de.neocraftr.griefergames.utils;

import de.neocraftr.griefergames.GrieferGames;
import de.neocraftr.griefergames.settings.GrieferGamesConfig;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.labymod.api.Laby;
import net.labymod.api.client.chat.advanced.IngameChatTab;
import net.labymod.api.client.chat.filter.ChatFilter;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.screen.widget.attributes.bounds.Bounds;
import net.labymod.api.client.gui.screen.widget.attributes.bounds.PositionedBounds;
import net.labymod.api.configuration.labymod.chat.AdvancedChatMessage;
import net.labymod.api.configuration.labymod.chat.ChatTab;
import net.labymod.api.configuration.labymod.chat.ChatWindow;
import net.labymod.api.configuration.labymod.chat.category.GeneralChatTabConfig;
import net.labymod.api.configuration.labymod.chat.config.ChatWindowConfig;
import net.labymod.api.configuration.labymod.chat.config.RootChatTabConfig;
import net.labymod.api.configuration.labymod.chat.config.RootChatTabConfig.Type;
import net.labymod.api.util.I18n;
import net.labymod.api.util.bounds.DefaultRectangle;
import net.labymod.api.util.bounds.MutableRectangle;
import net.labymod.core.main.LabyMod;

public class Helper {

  private final GrieferGames griefergames;
  private final List<String> cityBuilds = Arrays.asList("extreme", "evil", "nature", "lava", "wasser", "event");
  private final Pattern serverCityBuildRegex = Pattern.compile("^cb([0-9]+)$");
  private final Pattern tablistColoredPrefixRegex = Pattern.compile("(.+\\u2503 (?:§.)+)");

  private final Pattern colorCodePattern = Pattern.compile("(?i)§[0-9A-FK-ORX]");

  public Helper(GrieferGames griefergames) {
        this.griefergames = griefergames;
    }

  /**
   * Searches for the second chat tab with the given name
   * it will be created if its not existing
   * Will skip if chat config is disabled
   * @param name Name of the second chat tab
   */
  public void findSecondChat(String name) {
      if(!griefergames.configuration().chatConfig().isEnabled()) {
        return;
      }
      IngameChatTab secondChat = null;

      for (ChatWindow window : LabyMod.references().advancedChatController().getWindows()) {
          for (ChatTab tab : window.getTabs()) {
              if (tab.getName().equalsIgnoreCase(name) && tab instanceof IngameChatTab) {
                  secondChat = (IngameChatTab) tab;
              }
          }
      }

      if (secondChat == null) {
          secondChat = createNewSecondChat(name);
      }

      if (secondChat.config().filters().get().isEmpty()) {
          // Create dummy filter to prevent LabyMod from sending alle messages to the second chat
          ChatFilter defaultChatFilter = new ChatFilter();
          defaultChatFilter.name().set("GrieferGames-Addon");
          defaultChatFilter.getIncludedTags().add("§chzgwefegsdrutjugiuteghuzazghwu");
          secondChat.config().filters().get().add(defaultChatFilter);
      }

      griefergames.setSecondChat(secondChat);
    }

  /**
   * Creates the new second chat tab with the given name
   * @param name Chat Tab name
   * @return The created chat tab
   */
  private IngameChatTab createNewSecondChat(String name) {
      RootChatTabConfig tabConfig = new RootChatTabConfig(0, Type.CUSTOM, new GeneralChatTabConfig(name));
      ChatWindowConfig config = new ChatWindowConfig(tabConfig);

      Bounds bounds = new PositionedBounds(1000, 1000, 300, 160);
      MutableRectangle rectangle = new DefaultRectangle();
      rectangle.setRight(300);
      rectangle.setBottom(160);

      //DefaultChatWindow window = new DefaultChatWindow(config);
      ChatWindow chatWindow = LabyMod.references().advancedChatController().getOrCreateSecondaryWindow(() -> tabConfig);
      chatWindow.config().setPosition(bounds, rectangle);
      return (IngameChatTab) chatWindow.initializeTab(tabConfig, null, false);
  }

  /**
   * Moves the chat message in the second chat tab
   * @param message Message
   */
  public void displayInSecondChat(AdvancedChatMessage message) {
    if(griefergames.getSecondChat() == null) {
      return;
    }
    griefergames.getSecondChat().handleInput(message);
  }

  /**
   * Converts a component to a string
   * @param component Component
   * @return String
   */
  public String componentToPlainText(Component component) {
      StringBuilder builder = new StringBuilder();
      Laby.references().componentRenderer().getColorStrippingFlattener().flatten(component, builder::append);
      return builder.toString();
  }

  /**
   * Formats a component to a string including format codes
   * @param component Component
   * @return String
   */
  public String componentToFormattedText(Component component) {
      return Laby.references().componentRenderer().legacySectionSerializer().serialize(component);
  }

  /*
    return true if the player is on a citybuild or is in the farmworld.
  */

  /**
   * checks if the player is on a citybuild or is in the farmworld.
   * @param serverName Server name
   * @return true if the player is on a citybuild or is in the farmworld.
   */
  public boolean isCityBuild(String serverName) {
      if (serverName.equals("cb0")) return false;
      if (serverName.startsWith("cb")) return true;
      if (serverName.startsWith("Farmworld")) return true;
      if (serverName.startsWith("Citybuild")) return true;
      return cityBuilds.contains(serverName);
  }

  /**
   * Formats the Server name to a human name
   * @param serverName Server name
   * @return Formatted server name
   */
  public String formatServerName(String serverName) {
      if (serverName == null || serverName.isBlank()) return "";

      if (serverName.equals("event")) {
          return "Event Server";
      } else if (serverName.equals("cb0")) {
          return "CityBuild Zero";
      } else if (serverName.equals("cbe")) {
          return "CityBuild Evil";
      } else if (serverName.equals("extreme") || serverName.equals("nature")) {
          return "CityBuild " + capitalize(serverName);
      } else if (serverName.equals("lava") || serverName.equals("wasser")) {
          return "Farmserver " + capitalize(serverName);
      } else if (serverName.startsWith("cloud_")) {
          String cloudServerName = serverName.replace("cloud_", "");
          cloudServerName = cloudServerName.startsWith("cb") ? cloudServerName.toUpperCase() : capitalize(cloudServerName);
          return "Cloud " + cloudServerName;
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

  /**
   * Capitalizes the first letter of the string
   * @param str String
   * @return Capitalized string
   */
  public String capitalize(String str) {
      return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
  }

  /**
   * Performs all actions to do if the player goes afk
   * @param afk true if the player goes afk
   */
  public void performAfkActions(boolean afk) {
      if (afk) {
          griefergames.displayAddonMessage(Component.text(I18n.translate(griefergames.namespace() + ".messages.afkMessage"), NamedTextColor.GRAY));

          if (griefergames.configuration().automations().afkConfig().afkNick().get() && isCityBuild(griefergames.getSubServer())) {
              String nickname = griefergames.configuration().automations().afkConfig().afkNickname().get();
              if (nickname.isBlank()) {
                  nickname = GrieferGamesConfig.DEFAULT_AFK_NICKNAME;
              }
              nickname = nickname.replace("%name%", Laby.labyAPI().getName());
              if (nickname.length() > 16) {
                  nickname = nickname.substring(0, 16);
              }

              griefergames.sendMessage("/nick " + nickname);
          }
      } else {
          griefergames.displayAddonMessage(Component.text(I18n.translate(griefergames.namespace() + ".messages.afkBackMessage"), NamedTextColor.GRAY));
          if (griefergames.configuration().automations().afkConfig().afkNick().get() && isCityBuild(griefergames.getSubServer())) {
              griefergames.sendMessage("/unnick");
          }
      }
    }

  /**
   * Adds a gradiant to the message
   * @param msg Message
   * @param gradiantPrefix Gradiant prefix
   * @return Message with gradiant
   */
  public String addGradiant(String msg, String gradiantPrefix) {
      ///checks if the msg should be modified
      if (msg.startsWith("/") && !msg.startsWith("/sign")) {
          return msg;
      }

      //split the message into parts | e.g. -> [s, cc, cc, s, cc, ..] (s = string, cc = color code)
      String regexNormalHex = "&[0-9a-fA-F]";
      String regexNormalSixHex = "&#[0-9a-fA-F]{6}";
      String regexGradiantSixHex = gradiantPrefix + "#[0-9a-fA-F]{6}";
      String combinedPattern = regexNormalHex + "|" + regexNormalSixHex + "|" + regexGradiantSixHex;
      Pattern patternGradiant = Pattern.compile(regexGradiantSixHex);
      Pattern patternNormal = Pattern.compile(regexNormalHex + "|" + regexNormalSixHex);
      Pattern pattern = Pattern.compile(combinedPattern);
      Matcher matcher = pattern.matcher(msg);
      ArrayList<String> splitMsg = new ArrayList<>();
      int endIndex = 0;
      while (matcher.find()) {
          String s = msg.substring(endIndex, matcher.start());
          if (!s.isEmpty()) splitMsg.add(s);
          splitMsg.add(matcher.group());
          endIndex = matcher.end();
      }
      if (!msg.substring(endIndex).isEmpty()) splitMsg.add(msg.substring(endIndex));

      //remove unnecessary color codes
      ArrayList<String> correctedList = new ArrayList<>();
      for (int i = 0; i < splitMsg.size(); i++) {
          //if it is a color code -> continue
          if (pattern.matcher(splitMsg.get(i)).find()) continue;
          //executes if it is a text
          //[] {text} -> [text]
          if (i == 0) {
              correctedList.add(splitMsg.get(i));
              continue;
          }
          //[...] {normal code} {text} -> [..., {normal code}, {text}] -- {normal code} only if it isn't the last element!
          if (patternNormal.matcher(splitMsg.get(i - 1)).find()) {
              correctedList.add(splitMsg.get(i - 1));
              correctedList.add(splitMsg.get(i));
              continue;
          }
          //if there is a gradiant color code "##ff00ff" before
          if (patternGradiant.matcher(splitMsg.get(i - 1)).find()) {
              //if it is the last element
              if (i + 1 == splitMsg.size()) {
                  //[] {gradiant code} {text} -> [{normal code}, {text}]
                  correctedList.add(splitMsg.get(i - 1).replaceFirst(gradiantPrefix, "&"));
                  correctedList.add(splitMsg.get(i));
                  continue;
              }
              //if it is not the last element
              if (patternGradiant.matcher(splitMsg.get(i + 1)).find()) {
                  //[] {gradiant code} {text} {gradiant code} -> [{gradiant code}, {text}, {gradiant code}]
                  if (correctedList.isEmpty()) {
                      correctedList.add(splitMsg.get(i - 1));
                      correctedList.add(splitMsg.get(i));
                      correctedList.add(splitMsg.get(i + 1));
                      continue;
                  }
                  if (correctedList.get(correctedList.size() - 1).equals(splitMsg.get(i - 1))) {
                      correctedList.add(splitMsg.get(i));
                      correctedList.add(splitMsg.get(i + 1));
                      continue;
                  }
                  correctedList.add(splitMsg.get(i - 1));
                  correctedList.add(splitMsg.get(i));
                  correctedList.add(splitMsg.get(i + 1));
                  continue;
              }
              if (patternNormal.matcher(splitMsg.get(i + 1)).find()) {
                  correctedList.add(splitMsg.get(i - 1).replaceFirst(gradiantPrefix, "&"));
                  correctedList.add(splitMsg.get(i));
              }
          }
      }

      if (correctedList.size() == 1) {
          return correctedList.get(0);
      }

      int length = 0; // all chars, that are not gradiant color codes
      int lengthGradiant = 0; //chars that get colorized
      for (int i = 0; i < correctedList.size(); i++) {
          if (!pattern.matcher(correctedList.get(i)).find()) {
              if (i > 0) {
                  length = length + correctedList.get(i).length() + correctedList.get(i - 1).length();
                  if (!patternGradiant.matcher(correctedList.get(i - 1)).find()) continue;
                  lengthGradiant = lengthGradiant + correctedList.get(i).length();
              } else {
                  length = length + correctedList.get(i).length();
              }
          }
      }
      int numberOfCodes = (256 - length) / 8;

      if (numberOfCodes == 0) return msg;

      //build the colorized message
      StringBuilder result = new StringBuilder();
      for (int i = 0; i < correctedList.size(); i++) {
          if (pattern.matcher(correctedList.get(i)).find()) continue;
          if (i == 0) {
              result.append(correctedList.get(i));
              continue;
          }
          if (patternNormal.matcher(correctedList.get(i - 1)).find()) {
              result.append(correctedList.get(i - 1));
              result.append(correctedList.get(i));
              continue;
          }
          //is always true
          if (patternGradiant.matcher(correctedList.get(i - 1)).find()) {
              Color start = Color.decode(correctedList.get(i - 1).substring(1));
              Color end = Color.decode(correctedList.get(i + 1).substring(1));
              double ratio = (double) correctedList.get(i).length() / (double) lengthGradiant;
              int codes = 1 + (int) (ratio * (double) numberOfCodes);
              result.append(interpolate(correctedList.get(i), start, end, codes));
          }
      }
      return result.toString();
  }

  /**
   * Interpolates the color of the message
   * @param msg Message
   * @param start Start color
   * @param end End color
   * @param count Count of color codes
   * @return Interpolated message
   */
  private String interpolate(String msg, Color start, Color end, int count) {
        if (count <= 0) return msg;
        if (count > msg.length()) count = msg.length();
        int difRed = end.getRed() - start.getRed();
        int difGreen = end.getGreen() - start.getGreen();
        int difBlue = end.getBlue() - start.getBlue();
        int partLength = msg.length() / count;
        int endPrefix = count - 1;
        if (count == 1) endPrefix = 1;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            Color color = new Color(start.getRed() + difRed * i / endPrefix, start.getGreen() + difGreen * i / endPrefix, start.getBlue() + difBlue * i / endPrefix);
            int startIndex = i * partLength;
            int endIndex = partLength * (i + 1);
            if (i == count - 1) {
                endIndex = msg.length();
            }
            sb.append("&#").append(Integer.toHexString(color.getRGB()).substring(2)).append(msg, startIndex, endIndex);
        }
        return sb.toString();
    }

  /**
   * Removes leading misc or color codes from the msg
   * Also removes leading color codes!
   * @param message Message
   * @return cleared message
   */
  public String removeLeadingMiscCodes(String message) {
    String newMessage = message;
    while (newMessage.startsWith("§")) {
      newMessage = newMessage.substring(2);
    }
    return newMessage;
  }

}
