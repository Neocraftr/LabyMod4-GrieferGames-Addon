package de.neocraftr.griefergames.settings;

import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget.SliderSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingSection;

public class GrieferGamesAutomationsConfig extends Config {

  @SwitchSetting
  private final ConfigProperty<Boolean> hideBoosterMenu = new ConfigProperty<>(false);

  @SwitchSetting
  private final ConfigProperty<Boolean> autoPortal = new ConfigProperty<>(true);

  @SettingSection("afk")

  @SliderSetting(min = 0, max = 60)
  private final ConfigProperty<Integer> afkTime = new ConfigProperty<>(15);

  @SwitchSetting
  private final ConfigProperty<Boolean> afkNick = new ConfigProperty<>(false);

  @TextFieldSetting
  private final ConfigProperty<String> afkNickname = new ConfigProperty<>(GrieferGamesConfig.DEFAULT_AFK_NICKNAME);

  @SwitchSetting
  private final ConfigProperty<Boolean> afkMsgReply = new ConfigProperty<>(true);

  @TextFieldSetting
  private final ConfigProperty<String> afkMsgText = new ConfigProperty<>("Ich bin momentan AFK ;)");

  public ConfigProperty<Boolean> hideBoosterMenu() {
    return hideBoosterMenu;
  }

  public ConfigProperty<Boolean> autoPortal() {
    return autoPortal;
  }

  public ConfigProperty<Integer> afkTime() {
    return afkTime;
  }

  public ConfigProperty<Boolean> afkNick() {
    return afkNick;
  }

  public ConfigProperty<String> afkNickname() {
    return afkNickname;
  }

  public ConfigProperty<Boolean> afkMsgReply() {
    return afkMsgReply;
  }

  public ConfigProperty<String> afkMsgText() {
    return afkMsgText;
  }
}
