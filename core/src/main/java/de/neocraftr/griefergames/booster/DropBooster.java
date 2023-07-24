package de.neocraftr.griefergames.booster;

import de.neocraftr.griefergames.GrieferGames;
import net.labymod.api.util.I18n;

public class DropBooster extends Booster {
	public DropBooster() {
		super(I18n.translate(GrieferGames.get().namespace()+".hudWidget.gg_booster.drop"), "drops-booster", 5, true);
	}
}
