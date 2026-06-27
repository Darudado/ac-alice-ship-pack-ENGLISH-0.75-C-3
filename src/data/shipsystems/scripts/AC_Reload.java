//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package data.shipsystems.scripts;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponType;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.plugins.ShipSystemStatsScript;
import com.fs.starfarer.api.plugins.ShipSystemStatsScript.State;

public class AC_Reload extends BaseShipSystemScript {
    private boolean once = true;
    private ShipAPI ship = null;

    public void apply(MutableShipStatsAPI stats, String id, ShipSystemStatsScript.State state, float effectLevel) {
        if (this.ship == null) {
            if (!(stats.getEntity() instanceof ShipAPI)) {
                return;
            }

            this.ship = (ShipAPI)stats.getEntity();
        }

        if (this.once && state == State.ACTIVE) {
            for(WeaponAPI weapon : this.ship.getAllWeapons()) {
                if (!weapon.isDecorative() && weapon.usesAmmo() && weapon.getType() != WeaponType.MISSILE && weapon.getType() != WeaponType.COMPOSITE && weapon.getType() != WeaponType.SYNERGY) {
                    weapon.setAmmo(weapon.getMaxAmmo());
                }
            }

            this.once = false;
        }

    }

    public void unapply(MutableShipStatsAPI stats, String id) {
        if (this.ship == null) {
            if (!(stats.getEntity() instanceof ShipAPI)) {
                return;
            }

            this.ship = (ShipAPI)stats.getEntity();
        }

        this.once = true;
    }

    public ShipSystemStatsScript.StatusData getStatusData(int index, ShipSystemStatsScript.State state, float effectLevel) {
        return index == 0 ? new ShipSystemStatsScript.StatusData("Reloading ammunition...", false) : null;
    }
}
