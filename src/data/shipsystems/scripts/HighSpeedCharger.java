//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package data.shipsystems.scripts;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.plugins.ShipSystemStatsScript;

public class HighSpeedCharger extends BaseShipSystemScript {
    public void apply(MutableShipStatsAPI stats, String id, ShipSystemStatsScript.State state, float effectLevel) {
        stats.getEnergyWeaponFluxCostMod().modifyPercent(id, -25.0F);
        stats.getEnergyRoFMult().modifyPercent(id, 50.0F);
    }

    public void unapply(MutableShipStatsAPI stats, String id) {
        stats.getEnergyWeaponDamageMult().unmodify(id);
        stats.getEnergyRoFMult().unmodify(id);
    }

    public ShipSystemStatsScript.StatusData getStatusData(int index, ShipSystemStatsScript.State state, float effectLevel) {
        if (index == 0) {
            return new ShipSystemStatsScript.StatusData("+50% energy rate of fire", false);
        } else {
            return index == 1 ? new ShipSystemStatsScript.StatusData("-25% energy flux generation", false) : null;
        }
    }
}
