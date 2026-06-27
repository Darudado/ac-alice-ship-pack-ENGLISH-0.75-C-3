//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package data.shipsystems.scripts;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.plugins.ShipSystemStatsScript;
import com.fs.starfarer.api.plugins.ShipSystemStatsScript.State;

public class StrongDrive extends BaseShipSystemScript {
    public void apply(MutableShipStatsAPI stats, String id, ShipSystemStatsScript.State state, float effectLevel) {
        if (state == State.OUT) {
            stats.getMaxSpeed().modifyFlat(id, 0.0F);
            stats.getMaxSpeed().modifyPercent(id, 100.0F * effectLevel);
            stats.getMaxTurnRate().modifyPercent(id, 100.0F * effectLevel);
            stats.getAcceleration().modifyPercent(id, 150.0F * effectLevel);
            stats.getDeceleration().modifyPercent(id, 200.0F);
        } else {
            stats.getMaxSpeed().modifyFlat(id, 50.0F * effectLevel);
            stats.getMaxSpeed().modifyPercent(id, 50.0F * effectLevel);
            stats.getAcceleration().modifyFlat(id, 150.0F * effectLevel);
            stats.getAcceleration().modifyPercent(id, 150.0F * effectLevel);
            stats.getDeceleration().modifyPercent(id, 100.0F * effectLevel);
            stats.getTurnAcceleration().modifyFlat(id, 50.0F * effectLevel);
            stats.getTurnAcceleration().modifyPercent(id, 100.0F * effectLevel);
            stats.getMaxTurnRate().modifyFlat(id, 25.0F * effectLevel);
            stats.getMaxTurnRate().modifyPercent(id, 50.0F * effectLevel);
        }

    }

    public ShipSystemStatsScript.StatusData getStatusData(int index, ShipSystemStatsScript.State state, float effectLevel) {
        if (index == 0) {
            return new ShipSystemStatsScript.StatusData("Maneuverability increased", false);
        } else {
            return index == 1 ? new ShipSystemStatsScript.StatusData("Maximum speed increased", false) : null;
        }
    }

    public void unapply(MutableShipStatsAPI stats, String id) {
        stats.getMaxSpeed().unmodify(id);
        stats.getMaxTurnRate().unmodify(id);
        stats.getTurnAcceleration().unmodify(id);
        stats.getAcceleration().unmodify(id);
        stats.getDeceleration().unmodify(id);
    }
}
