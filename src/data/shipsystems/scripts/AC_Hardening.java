//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package data.shipsystems.scripts;

import com.fs.starfarer.api.combat.BeamAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.DamageDealtModifier;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.plugins.ShipSystemStatsScript;
import org.lwjgl.util.vector.Vector2f;

public class AC_Hardening extends BaseShipSystemScript {
    public void apply(MutableShipStatsAPI stats, String id, ShipSystemStatsScript.State state, float effectLevel) {
        ShipAPI ship = (ShipAPI)stats.getEntity();
        ship.addListener(new HighScatterAmpDamageDealtMod(ship));
    }

    public void unapply(MutableShipStatsAPI stats, String id) {
        ShipAPI ship = (ShipAPI)stats.getEntity();
        ship.removeListener(new HighScatterAmpDamageDealtMod(ship));
    }

    public ShipSystemStatsScript.StatusData getStatusData(int index, ShipSystemStatsScript.State state, float effectLevel) {
        return index == 0 ? new ShipSystemStatsScript.StatusData("Beams deal hard flux damage", false) : null;
    }

    public static class HighScatterAmpDamageDealtMod implements DamageDealtModifier {
        protected ShipAPI ship;

        public HighScatterAmpDamageDealtMod(ShipAPI ship) {
            this.ship = ship;
        }

        public String modifyDamageDealt(Object param, CombatEntityAPI target, DamageAPI damage, Vector2f point, boolean shieldHit) {
            if (!(param instanceof DamagingProjectileAPI) && param instanceof BeamAPI) {
                damage.setForceHardFlux(true);
            }

            return null;
        }
    }
}
