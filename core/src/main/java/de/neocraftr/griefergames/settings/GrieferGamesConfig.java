package de.neocraftr.griefergames.settings;

import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget.ButtonSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.annotation.SpriteTexture;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.models.OperatingSystem;
import net.labymod.api.util.MethodOrder;

@ConfigName("settings")
@SpriteTexture("icon.png")
public class GrieferGamesConfig extends AddonConfig {

  public static final String DEFAULT_AMP_REPLACEMENT = "[AMP]",
      DEFAULT_AFK_NICKNAME = "AFK_%name%",
      DEFAULT_CHATTIME_FORMAT = "&8[&3%h%&7:&3%m%&7:&3%s%&8]";


  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> autoUpdate = new ConfigProperty<>(true);

  private GrieferGamesChatConfig chatConfig = new GrieferGamesChatConfig();
  private GrieferGamesPaymentsConfig payment = new GrieferGamesPaymentsConfig();
  private GrieferGamesAutomationsConfig automations = new GrieferGamesAutomationsConfig();
  private GrieferGamesFriendsConfig friends = new GrieferGamesFriendsConfig();

  @MethodOrder(after = "friends")
  @ButtonSetting
  public void openWebsite() {
    OperatingSystem.getPlatform().openUrl("https://mc.im1random.org/?addon=GrieferGames");
  }

  @MethodOrder(after = "openWebsite")
  @ButtonSetting
  public void openGithub() {
    OperatingSystem.getPlatform().openUrl("https://github.com/Neocraftr/LabyMod4-GrieferGames-Addon");
  }


  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  public ConfigProperty<Boolean> autoUpdate() {
    return autoUpdate;
  }

  public GrieferGamesChatConfig chatConfig() {
    return chatConfig;
  }

  public GrieferGamesPaymentsConfig payment() {
    return payment;
  }

  public GrieferGamesAutomationsConfig automations() {
    return automations;
  }

  public GrieferGamesFriendsConfig friends() {
    return friends;
  }
}
