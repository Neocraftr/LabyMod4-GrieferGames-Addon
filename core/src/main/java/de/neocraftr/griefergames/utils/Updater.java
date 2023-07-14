package de.neocraftr.griefergames.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.neocraftr.griefergames.GrieferGames;
import net.labymod.api.Laby;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.lifecycle.GameShutdownEvent;
import net.labymod.api.models.addon.info.InstalledAddonInfo;
import net.labymod.api.util.io.web.request.Request;
import java.io.File;

public class Updater {
  private GrieferGames griefergames;
  private boolean updateAvailable = false;
  private String updateUrl = null;
  private String newVersion = null;

  public Updater(GrieferGames griefergames) {
    this.griefergames = griefergames;
  }

  public void checkForUpdates() {
    InstalledAddonInfo addonInfo = griefergames.addonInfo();
    if(addonInfo.getVersion().endsWith("-beta")) {
      griefergames.logger().error(GrieferGames.LOG_PREFIX+"Beta version, updater disabled.");
      return;
    }

    Request.ofGson(JsonArray.class).url("https://mc.im1random.org/api/addons/addons-laby4.json", "test").async().execute(response ->{
      if(response.hasException()) {
        griefergames.logger().error(GrieferGames.LOG_PREFIX+"Update check failed: "+response.exception().getMessage());
        return;
      }

      for(JsonElement entry : response.get()) {
        JsonObject addon = entry.getAsJsonObject();

        if(addon.has("namespace") && addon.get("namespace").getAsString().equals(addonInfo.getNamespace())) {
          newVersion = addon.get("version").getAsString();
          if(!newVersion.equals(addonInfo.getVersion())) {
            JsonElement versionLinks = addon.get("versions").getAsJsonObject().get(Laby.labyAPI().minecraft().getVersion());
            if(versionLinks != null) {
              updateAvailable = true;
              updateUrl = versionLinks.getAsJsonObject().get("download").getAsString();
              griefergames.logger().info(GrieferGames.LOG_PREFIX+"Update to version v"+newVersion+" available.");
            } else {
              griefergames.logger().error(GrieferGames.LOG_PREFIX+"Update check failed: No version available for Minecraft "+Laby.labyAPI().minecraft().getVersion());
            }
          } else {
            griefergames.logger().info(GrieferGames.LOG_PREFIX+"Addon is up to date.");
          }
          break;
        }
      }
    });
  }

  public void performUpdate() {
    if(!updateAvailable || updateUrl == null) return;
    if(griefergames.addonInfo().getFileName() == null) {
      griefergames.logger().error(GrieferGames.LOG_PREFIX+"Update not possible, this is probably caused by the addon being started in a development environment.");
      return;
    }

    griefergames.logger().info(GrieferGames.LOG_PREFIX+"Performing update to version v"+newVersion);

    File addonFile = new File("labymod-neo/addons/", griefergames.addonInfo().getFileName());
    File tempFile = new File("labymod-neo/griefergames-update.jar.temp");
    if(tempFile.exists()) {
      tempFile.delete();
    }

    try {
      Request.ofFile(tempFile.toPath()).url(updateUrl).executeSync();
    } catch(Exception e) {
      griefergames.logger().error(GrieferGames.LOG_PREFIX+"Update failed: "+e.getMessage());
      return;
    }

    addonFile.delete();
    tempFile.renameTo(addonFile);
    griefergames.logger().info(GrieferGames.LOG_PREFIX+"Update successfully installed:");
  }

  public boolean isUpdateAvailable() {
    return updateAvailable;
  }

  public String getNewVersion() {
    return newVersion;
  }

  @Subscribe
  public void onShutdown(GameShutdownEvent event) {
    performUpdate();
  }
}
