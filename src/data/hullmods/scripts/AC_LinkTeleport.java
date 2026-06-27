//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package data.hullmods.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.CombatEngineLayers;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;
import data.util.AC_ColorData;
import java.util.HashMap;
import java.util.Map;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;
import org.magiclib.util.MagicRender;

public class AC_LinkTeleport extends BaseHullMod {
    protected static final String KEY = "LINK_TELEPORT_KEY";
    protected static final String KEY_INFO = "LINK_TELEPORT_INFO_KEY";
    private boolean runOnce = false;
    private final IntervalUtil GlowSS = new IntervalUtil(10.0F, 10.0F);
    protected static final float RANGE = 2000.0F;

    public void advanceInCombat(ShipAPI ship, float amount) {
        this.GlowSS.advance(amount);
        if (!this.runOnce) {
            this.runOnce = true;
        }

        if (ship.getSystem() != null) {
            if (ship.getSystem().isChargeup()) {
                this.getInfoProvider(ship).advance(ship, amount);
            }

            if (ship.getSystem().isActive() && this.isTeleportAvailable(ship)) {
                LinkTeleportInfoProvider info = this.getInfoProvider(ship);

                for(ShipAPI f : info.getFightersInfo().keySet()) {
                    f.getLocation().set(Vector2f.add(ship.getLocation(), (Vector2f)info.getFightersInfo().get(f), (Vector2f)null));
                }

                this.setTeleportAvailable(ship, false);
            }

            if (ship.getSystem().isChargedown()) {
                this.setTeleportAvailable(ship, true);
            }
        }

        if (ship == Global.getCombatEngine().getPlayerShip()) {
            SpriteAPI effect2 = Global.getSettings().getSprite("fx", "HUAN2");
            Vector2f size = new Vector2f(4000.0F, 4000.0F);
            if (this.GlowSS.intervalElapsed()) {
                MagicRender.objectspace(effect2, ship, new Vector2f(), new Vector2f(), size, ship.getRenderOffset(), -180.0F, 33.0F, true, Misc.scaleAlpha(AC_ColorData.B_WHITE, Math.min(0.3F, 0.3F)), 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5F, 9.0F, 0.5F, true, CombatEngineLayers.BELOW_SHIPS_LAYER, 770, 1);
            }
        }

    }

    private boolean isTeleportAvailable(ShipAPI ship) {
        boolean a = false;
        if (ship.getCustomData() != null && ship.getCustomData().containsKey("LINK_TELEPORT_KEY")) {
            a = (Boolean)ship.getCustomData().get("LINK_TELEPORT_KEY");
        }

        return a;
    }

    private void setTeleportAvailable(ShipAPI ship, boolean available) {
        ship.setCustomData("LINK_TELEPORT_KEY", available);
    }

    private LinkTeleportInfoProvider getInfoProvider(ShipAPI ship) {
        if (ship.getCustomData() != null && ship.getCustomData().containsKey("LINK_TELEPORT_INFO_KEY")) {
            return (LinkTeleportInfoProvider)ship.getCustomData().get("LINK_TELEPORT_INFO_KEY");
        } else {
            LinkTeleportInfoProvider info = new LinkTeleportInfoProvider();
            ship.getCustomData().put("LINK_TELEPORT_INFO_KEY", info);
            return info;
        }
    }

    public static class LinkTeleportInfoProvider {
        private Map<ShipAPI, Vector2f> fighters = new HashMap();
        private IntervalUtil timer = new IntervalUtil(0.05F, 0.05F);

        public Map<ShipAPI, Vector2f> getFightersInfo() {
            return this.fighters;
        }

        public void advance(ShipAPI ship, float amount) {
            this.timer.advance(amount);
            if (this.timer.intervalElapsed()) {
                for(ShipAPI f : Global.getCombatEngine().getShips()) {
                    if (f.isFighter() && f.getOwner() == ship.getOwner() && MathUtils.getDistanceSquared(ship.getLocation(), f.getLocation()) < 4000000.0F) {
                        this.fighters.put(f, Vector2f.sub(f.getLocation(), ship.getLocation(), (Vector2f)null));
                    }
                }

            }
        }
    }
}
