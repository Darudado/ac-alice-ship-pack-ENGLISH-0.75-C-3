//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package data.shipsystems.scripts;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.plugins.ShipSystemStatsScript;

public class AC_OverLoad extends BaseShipSystemScript {
    public void apply(MutableShipStatsAPI stats, String id, ShipSystemStatsScript.State state, float effectLevel) {
        stats.getBallisticRoFMult().modifyPercent(id, 25.0F);
        stats.getBallisticWeaponDamageMult().modifyPercent(id, 25.0F);
        stats.getEnergyRoFMult().modifyPercent(id, 25.0F);
        stats.getEnergyWeaponDamageMult().modifyPercent(id, 25.0F);
    }

    public void unapply(MutableShipStatsAPI stats, String id) {
        stats.getBallisticRoFMult().unmodify(id);
        stats.getBallisticWeaponDamageMult().unmodify(id);
        stats.getEnergyRoFMult().unmodify(id);
        stats.getEnergyWeaponDamageMult().unmodify(id);
    }

    public ShipSystemStatsScript.StatusData getStatusData(int index, ShipSystemStatsScript.State state, float effectLevel) {
        if (index == 0) {
            return new ShipSystemStatsScript.StatusData("+25% non-missile rate of fire", false);
        } else {
            return index == 1 ? new ShipSystemStatsScript.StatusData("+25% non-missile damage", false) : null;
        }
    }
}
