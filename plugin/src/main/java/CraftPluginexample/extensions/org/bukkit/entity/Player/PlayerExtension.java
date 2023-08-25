package CraftPluginexample.extensions.org.bukkit.entity.Player;

import com.cryptomorin.xseries.XSound;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import org.bukkit.entity.Player;

@Extension
public class PlayerExtension {

  public static void playSuccess(@This Player player) {
    XSound.ENTITY_PLAYER_LEVELUP.play(player);
  }

  public static void playClick(@This Player player) {
    XSound.UI_BUTTON_CLICK.play(player);
  }

  public static void playError(@This Player player) {
    XSound.ENTITY_VILLAGER_NO.play(player);
  }

  public static void playRemove(@This Player player) {
    XSound.ENTITY_ITEM_BREAK.play(player);
  }

  public static void playEnter(@This Player player) {
    XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
  }

}