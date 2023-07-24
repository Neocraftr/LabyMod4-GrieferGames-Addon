package de.neocraftr.griefergames.booster;

import de.neocraftr.griefergames.GrieferGames;
import net.labymod.api.util.I18n;

public class MobBooster extends Booster {
	public MobBooster() {
		super(I18n.translate(GrieferGames.get().namespace()+".hudWidget.gg_booster.mob"), "mob-booster", 2, true);
	}
}
