//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.plugins.ShipSystemStatsScript;
import com.fs.starfarer.api.plugins.ShipSystemStatsScript.State;
import java.awt.Color;

public class TemporalShellWeak extends BaseShipSystemScript {
    public static final float MAX_TIME_MULT = 3.0F;
    public static final float MIN_TIME_MULT = 0.1F;
    public static final float DAM_MULT = 0.1F;
    public static final Color JITTER_COLOR = new Color(90, 165, 255, 55);
    public static final Color JITTER_UNDER_COLOR = new Color(90, 165, 255, 155);

    public void apply(MutableShipStatsAPI stats, String id, ShipSystemStatsScript.State state, float effectLevel) {
        ShipAPI ship = null;
        boolean player = false;
        if (stats.getEntity() instanceof ShipAPI) {
            ship = (ShipAPI)stats.getEntity();
            player = ship == Global.getCombatEngine().getPlayerShip();
            id = id + "_" + ship.getId();
            float jitterLevel = effectLevel;
            float jitterRangeBonus = 0.0F;
            float maxRangeBonus = 10.0F;
            if (state == State.IN) {
                jitterLevel = effectLevel / (1.0F / ship.getSystem().getChargeUpDur());
                if (jitterLevel > 1.0F) {
                    jitterLevel = 1.0F;
                }

                jitterRangeBonus = jitterLevel * maxRangeBonus;
            } else if (state == State.ACTIVE) {
                jitterLevel = 1.0F;
                jitterRangeBonus = maxRangeBonus;
            } else if (state == State.OUT) {
                jitterRangeBonus = effectLevel * maxRangeBonus;
            }

            jitterLevel = (float)Math.sqrt((double)jitterLevel);
            effectLevel *= effectLevel;
            ship.setJitter(this, JITTER_COLOR, jitterLevel, 3, 0.0F, 0.0F + jitterRangeBonus);
            ship.setJitterUnder(this, JITTER_UNDER_COLOR, jitterLevel, 25, 0.0F, 7.0F + jitterRangeBonus);
            float shipTimeMult = 1.0F + effectLevel;
            stats.getTimeMult().modifyMult(id, shipTimeMult);
            if (player) {
                Global.getCombatEngine().getTimeMult().modifyMult(id, 1.0F / shipTimeMult);
            } else {
                Global.getCombatEngine().getTimeMult().unmodify(id);
            }

            ship.getEngineController().fadeToOtherColor(this, JITTER_COLOR, new Color(0, 0, 0, 0), effectLevel, 0.5F);
            ship.getEngineController().extendFlame(this, -0.25F, -0.25F, -0.25F);
        }

    }

    public void unapply(MutableShipStatsAPI stats, String id) {
        ShipAPI ship = null;
        boolean player = false;
        if (stats.getEntity() instanceof ShipAPI) {
            ship = (ShipAPI)stats.getEntity();
            player = ship == Global.getCombatEngine().getPlayerShip();
            id = id + "_" + ship.getId();
            Global.getCombatEngine().getTimeMult().unmodify(id);
            stats.getTimeMult().unmodify(id);
        }

    }

    public ShipSystemStatsScript.StatusData getStatusData(int index, ShipSystemStatsScript.State state, float effectLevel) {
        float shipTimeMult = 1.0F + 2.0F * effectLevel;
        return index == 0 ? new ShipSystemStatsScript.StatusData("Time flow altered", false) : null;
    }
}
