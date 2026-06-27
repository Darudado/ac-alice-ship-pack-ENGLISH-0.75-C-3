//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package data.hullmods.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import data.util.AC_Util;
import data.util.AC_Util.I18nSection;

public class AC_ImmobilizationGrid extends BaseHullMod {
    public static final AC_Util.I18nSection strings = I18nSection.getInstance("HullMod", "AC_");

    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 10.0F;
        float padS = 5.0F;
        tooltip.addSectionHeading("Effect on Ship", Alignment.TMID, 10.0F);
        tooltip.addPara(" %s " + strings.get("ImmobilizationGrid_TEXT1"), pad, Misc.getPositiveHighlightColor(), new String[]{"#", "30%"});
        tooltip.addPara(" %s " + strings.get("ImmobilizationGrid_TEXT2"), padS, Misc.getHighlightColor(), new String[]{"#"});
    }

    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getBallisticWeaponFluxCostMod().modifyMult(id, 0.3F);
        stats.getEnergyWeaponFluxCostMod().modifyMult(id, 0.3F);
        stats.getMissileWeaponFluxCostMod().modifyMult(id, 0.3F);
    }

    public void advanceInCombat(ShipAPI ship, float amount) {
        if (ship != null) {
            CombatEngineAPI engine = Global.getCombatEngine();
            if (ship.isAlive()) {
                if (engine != null) {
                    super.advanceInCombat(ship, amount);
                    if (ship.isAlive() && ship.getFluxTracker().getHardFlux() != ship.getFluxTracker().getCurrFlux()) {
                        ship.getFluxTracker().setHardFlux(ship.getFluxTracker().getCurrFlux());
                    }

                }
            }
        }
    }

    public boolean affectsOPCosts() {
        return true;
    }
}
