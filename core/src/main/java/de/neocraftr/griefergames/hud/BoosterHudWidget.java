package de.neocraftr.griefergames.hud;

import de.neocraftr.griefergames.GrieferGames;
import de.neocraftr.griefergames.booster.Booster;
import de.neocraftr.griefergames.hud.BoosterHudWidget.BoosterHudWidgetConfig;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.gui.hud.hudwidget.HudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.SimpleHudWidget;
import net.labymod.api.client.gui.hud.position.HudSize;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.widget.widgets.input.color.ColorPickerWidget.ColorPickerSetting;
import net.labymod.api.client.render.font.ComponentRendererBuilder;
import net.labymod.api.client.render.font.RenderableComponent;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import java.util.List;

public class BoosterHudWidget extends SimpleHudWidget<BoosterHudWidgetConfig> {
  private final GrieferGames griefergames;

  public BoosterHudWidget(GrieferGames griefergames) {
    super("gg_booster", BoosterHudWidgetConfig.class);
    this.griefergames = griefergames;

    bindCategory(griefergames.getHudWidgetCategory());
    setIcon(Icon.texture(ResourceLocation.create(griefergames.namespace(), "textures/hud/booster.png")));
  }

  @Override
  public void render(Stack stack, MutableMouse mouse, float partialTicks, boolean isEditorContext, HudSize size) {
    if(stack == null) return;
    ComponentRendererBuilder componentRenderer = Laby.labyAPI().renderPipeline().componentRenderer().builder().useFloatingPointPosition(false);

    int y = 0;
    int width = 0;
    boolean rightBound = anchor().isRight();

    List<Booster> boosters = isEditorContext && !griefergames.boosterController().isActiveBooster() ?
        griefergames.boosterController().getDummyBoosters() : griefergames.boosterController().getBoosters();
    for(Booster booster : boosters) {
      if(booster.getCount() == 0) continue;

      int x = 0;
      booster.getIcon().render(stack, rightBound ? size.getWidth() - 18 - x : x, y, 18, 18);
      x += 23;

      Component nameComponent = Component.text(booster.getName(), TextColor.color(getConfig().nameColor().get()));
      if(booster.getCount() > 1) {
        nameComponent = nameComponent.append(Component.text(" x"+booster.getCount(), TextColor.color(getConfig().amplifierColor().get())));
      }
      Component timeComponent = Component.text(booster.getDurationString(),
          booster.shouldHighlightDuration() ? NamedTextColor.RED : TextColor.color(getConfig().durationColor().get()));

      RenderableComponent rendererHeader = RenderableComponent.of(nameComponent);
      RenderableComponent rendererTime = RenderableComponent.of(timeComponent);

      componentRenderer.pos(rightBound ? (size.getWidth() - x) - rendererHeader.getWidth() : x, y + 1).text(rendererHeader).render(stack);
      y = y + (int)rendererHeader.getHeight();
      width = (int)Math.max(width, x + rendererHeader.getWidth());

      componentRenderer.pos(rightBound ? (size.getWidth() - x) - rendererTime.getWidth() : x, y + 1).text(rendererTime).render(stack);
      y = y + (int)rendererTime.getHeight();
      width = (int)Math.max(width, x + rendererTime.getWidth());

      y += 3;
    }

    size.set(width, y);
  }

  @Override
  public boolean isVisibleInGame() {
    return griefergames.isOnGrieferGames() && griefergames.configuration().enabled().get();
  }

  public static class BoosterHudWidgetConfig extends HudWidgetConfig {
    @ColorPickerSetting
    private final ConfigProperty<Integer> nameColor = new ConfigProperty<>(NamedTextColor.WHITE.getValue());
    @ColorPickerSetting
    private final ConfigProperty<Integer> durationColor = new ConfigProperty<>(NamedTextColor.YELLOW.getValue());
    @ColorPickerSetting
    private final ConfigProperty<Integer> amplifierColor = new ConfigProperty<>(NamedTextColor.BLUE.getValue());

    public ConfigProperty<Integer> nameColor() {
      return nameColor;
    }

    public ConfigProperty<Integer> durationColor() {
      return durationColor;
    }

    public ConfigProperty<Integer> amplifierColor() {
      return amplifierColor;
    }
  }
}
