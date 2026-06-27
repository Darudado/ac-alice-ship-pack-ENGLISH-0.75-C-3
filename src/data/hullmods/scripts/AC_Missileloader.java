//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package data.hullmods.scripts;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import data.util.AC_Util;
import data.util.AC_Util.I18nSection;

public class AC_Missileloader extends BaseHullMod {
    public static final AC_Util.I18nSection strings = I18nSection.getInstance("HullMod", "AC_");
    public static float ROF = 25.0F;
    public static float REGEN = -25.0F;

    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 10.0F;
        float padS = 5.0F;
        tooltip.addSectionHeading("Effect on Ship", Alignment.TMID, 10.0F);
        tooltip.addPara(" %s " + strings.get("Missileloader_TEXT1"), pad, Misc.getPositiveHighlightColor(), new String[]{"#", "25%"});
        tooltip.addPara(" %s " + strings.get("Missileloader_TEXT2"), padS, Misc.getPositiveHighlightColor(), new String[]{"#", "50%"});
    }

    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getMissileRoFMult().modifyPercent(id, ROF);
        stats.getMissileAmmoRegenMult().modifyPercent(id, REGEN);
    }

    public boolean affectsOPCosts() {
        return true;
    }
}
