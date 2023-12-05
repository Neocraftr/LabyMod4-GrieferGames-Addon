package de.neocraftr.griefergames.settings;

import de.neocraftr.griefergames.enums.RealnamePosition;
import de.neocraftr.griefergames.enums.Sounds;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.color.ColorPickerWidget.ColorPickerSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.ParentSwitch;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingSection;
import net.labymod.api.util.Color;

public class GrieferGamesChatConfig extends Config {

  @ParentSwitch
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<Boolean>(true);

  // ONLY 1.8
  @SwitchSetting
  private final ConfigProperty<Boolean> clickToReply = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> preventCommandFailure = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> correctCommandCapitalisation = new ConfigProperty<>(true);

  // Only 1.8
  @SwitchSetting
  private final ConfigProperty<Boolean> betterIgnoreList = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> showPrefixInDisplayName = new ConfigProperty<>(true);

  @SettingSection("highlightMessages")

  @SwitchSetting
  private final ConfigProperty<Boolean> plotChatRight = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> privateChatRight = new ConfigProperty<>(true);

  @DropdownSetting
  private final ConfigProperty<Sounds> privateChatSound = new ConfigProperty<>(Sounds.POP);

  // 1.8
  @DropdownSetting
  private final ConfigProperty<RealnamePosition> realnamePosition = new ConfigProperty<>(
      RealnamePosition.DEFAULT);

  @SwitchSetting
  private final ConfigProperty<Boolean> highlightMentions = new ConfigProperty<>(true);

  @ColorPickerSetting
  private final ConfigProperty<Color> mentionColor = new ConfigProperty<>(Color.ofRGB(121, 178, 255));

  @DropdownSetting
  private final ConfigProperty<Sounds> mentionSound = new ConfigProperty<>(Sounds.NONE);

  @SwitchSetting
  private final ConfigProperty<Boolean> highlightTPA = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> itemRemoverChatRight = new ConfigProperty<>(false);

  @SwitchSetting
  private final ConfigProperty<Boolean> itemRemoverLastTimeHover = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> itemRemoverNotification = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> mobRemoverChatRight = new ConfigProperty<>(false);

  @SwitchSetting
  private final ConfigProperty<Boolean> mobRemoverLastTimeHover = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> mobRemoverNotification = new ConfigProperty<>(true);

  @SettingSection("blockMessages")

  @SwitchSetting
  private final ConfigProperty<Boolean> hideVoteMessages = new ConfigProperty<>(false);

  @SwitchSetting
  private final ConfigProperty<Boolean> hideNewsMessages = new ConfigProperty<>(false);

  @SwitchSetting
  private final ConfigProperty<Boolean> hideBlankLines = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> hideSupremeBlankLines = new ConfigProperty<>(true);

  @SettingSection("magicPrefix")

  @SwitchSetting
  private final ConfigProperty<Boolean> ampClantagEnabled = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> ampEnabled = new ConfigProperty<>(true);

  @TextFieldSetting
  private final ConfigProperty<String> ampReplacement = new ConfigProperty<>(GrieferGamesConfig.DEFAULT_AMP_REPLACEMENT);

  @SettingSection("chatTime")

  @SwitchSetting
  private final ConfigProperty<Boolean> showChatTime = new ConfigProperty<>(false);

  @SwitchSetting
  private final ConfigProperty<Boolean> chatTimeAfterMessage = new ConfigProperty<>(false);

  @TextFieldSetting
  private final ConfigProperty<String> chatTimeFormat = new ConfigProperty<>(GrieferGamesConfig.DEFAULT_CHATTIME_FORMAT);

  public boolean isPreventCommandFailure() {
    return enabled.get() && preventCommandFailure.get();
  }

  public ConfigProperty<Boolean> correctCommandCapitalisation() {
    return correctCommandCapitalisation;
  }

  public boolean isBetterIgnoreList() {
    return enabled.get() && betterIgnoreList.get();
  }

  public boolean isShowPrefixInDisplayName() {
    return enabled.get() && showPrefixInDisplayName.get();
  }

  public boolean isPlotChatRight() {
    return enabled.get() && plotChatRight.get();
  }

  public boolean isClickToReply() {
    return enabled.get() && clickToReply.get();
  }

  public boolean isPrivateChatRight() {
    return enabled.get() && privateChatRight.get();
  }

  public Sounds getPrivateChatSound() {
    if(!enabled.get()) return Sounds.NONE;
    return privateChatSound.get();
  }

  public RealnamePosition getRealnamePosition() {
    if(!enabled.get()) return RealnamePosition.DEFAULT;
    return realnamePosition.get();
  }

  public boolean isHighlightMentions() {
    return enabled.get() && highlightMentions.get();
  }

  public Color getMentionColor() {
    return mentionColor.get();
  }

  public Sounds getMentionSound() {
    if(!enabled.get()) return Sounds.NONE;
    return mentionSound.get();
  }

  public ConfigProperty<Boolean> highlightTPA() {
    return highlightTPA;
  }

  public ConfigProperty<Boolean> itemRemoverChatRight() {
    return itemRemoverChatRight;
  }

  public ConfigProperty<Boolean> itemRemoverLastTimeHover() {
    return itemRemoverLastTimeHover;
  }

  public ConfigProperty<Boolean> itemRemoverNotification() {
    return itemRemoverNotification;
  }

  public ConfigProperty<Boolean> mobRemoverChatRight() {
    return mobRemoverChatRight;
  }

  public ConfigProperty<Boolean> mobRemoverLastTimeHover() {
    return mobRemoverLastTimeHover;
  }

  public ConfigProperty<Boolean> mobRemoverNotification() {
    return mobRemoverNotification;
  }

  public ConfigProperty<Boolean> hideVoteMessages() {
    return hideVoteMessages;
  }

  public ConfigProperty<Boolean> hideNewsMessages() {
    return hideNewsMessages;
  }

  public ConfigProperty<Boolean> hideBlankLines() {
    return hideBlankLines;
  }

  public ConfigProperty<Boolean> hideSupremeBlankLines() {
    return hideSupremeBlankLines;
  }

  public ConfigProperty<Boolean> ampClantagEnabled() {
    return ampClantagEnabled;
  }

  public ConfigProperty<Boolean> ampEnabled() {
    return ampEnabled;
  }

  public ConfigProperty<String> ampReplacement() {
    return ampReplacement;
  }

  public ConfigProperty<Boolean> showChatTime() {
    return showChatTime;
  }

  public ConfigProperty<Boolean> chatTimeAfterMessage() {
    return chatTimeAfterMessage;
  }

  public ConfigProperty<String> chatTimeFormat() {
    return chatTimeFormat;
  }
}
