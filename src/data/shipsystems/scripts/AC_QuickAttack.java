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
import java.util.ArrayList;
import java.util.List;

public class AC_QuickAttack extends BaseShipSystemScript {
    protected static float FLUX_COST = 0.25F;
    protected static List<WeaponAPI.WeaponType> PERMISSION = new ArrayList();
    private ShipAPI ship = null;

    public void apply(MutableShipStatsAPI stats, String id, ShipSystemStatsScript.State state, float effectLevel) {
        stats.getBallisticWeaponFluxCostMod().modifyMult(id, 0.75F);
        stats.getEnergyWeaponFluxCostMod().modifyMult(id, 0.75F);
        if (this.ship == null) {
            if (!(stats.getEntity() instanceof ShipAPI)) {
                return;
            }

            this.ship = (ShipAPI)stats.getEntity();
        }

        if (this.ship != null) {
            for(WeaponAPI weapon : this.ship.getAllWeapons()) {
                if (!weapon.isDecorative()) {
                    if (state.equals(State.IN)) {
                        weapon.setForceNoFireOneFrame(true);
                    } else if (state.equals(State.ACTIVE)) {
                        if (weapon.getSlot().isHardpoint()) {
                            if (PERMISSION.contains(weapon.getType())) {
                                stats.getBallisticRoFMult().modifyPercent(id, 500.0F);
                            } else {
                                weapon.setForceNoFireOneFrame(true);
                            }
                        } else {
                            weapon.setForceNoFireOneFrame(true);
                        }
                    }
                }
            }
        }

    }

    public void unapply(MutableShipStatsAPI stats, String id) {
        if (this.ship == null && stats.getEntity() instanceof ShipAPI) {
            this.ship = (ShipAPI)stats.getEntity();
        }

        stats.getBallisticWeaponFluxCostMod().unmodify(id);
        stats.getEnergyWeaponFluxCostMod().unmodify(id);
        stats.getBallisticRoFMult().unmodify(id);
    }

    public ShipSystemStatsScript.StatusData getStatusData(int index, ShipSystemStatsScript.State state, float effectLevel) {
        if (index == 0) {
            return new ShipSystemStatsScript.StatusData("-25% ballistic flux generation", false);
        } else if (index == 1) {
            return new ShipSystemStatsScript.StatusData("+500% ballistic rate of fire after short delay", false);
        } else {
            return index == 2 ? new ShipSystemStatsScript.StatusData("Other weapons disabled", true) : null;
        }
    }

    static {
        PERMISSION.add(WeaponType.BALLISTIC);
    }
}
