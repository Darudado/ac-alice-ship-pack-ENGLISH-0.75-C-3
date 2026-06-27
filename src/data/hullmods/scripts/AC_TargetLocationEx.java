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
import data.scripts.util.MagicIncompatibleHullmods;
import data.util.AC_Util;
import data.util.AC_Util.I18nSection;
import java.util.HashSet;
import java.util.Set;

public class AC_TargetLocationEx extends BaseHullMod {
    public static final AC_Util.I18nSection strings = I18nSection.getInstance("HullMod", "AC_");
    public static final float RangeBonus = 80.0F;
    private static final Set<String> BLOCKED_HULLMODS = new HashSet();

    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 10.0F;
        tooltip.addSectionHeading("Effect on Ship", Alignment.TMID, 10.0F);
        tooltip.addPara(" %s " + strings.get("TargetLocationEx_TEXT1"), pad, Misc.getPositiveHighlightColor(), new String[]{"#", "80%"});
        tooltip.addSectionHeading("Limitations & Conflicts", Alignment.TMID, 10.0F);
        tooltip.addPara(" %s " + strings.get("TargetLocationEx_TEXT2"), pad, Misc.getNegativeHighlightColor(), new String[]{"#"});
    }

    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getBallisticWeaponRangeBonus().modifyPercent(id, 80.0F);
        stats.getEnergyWeaponRangeBonus().modifyPercent(id, 80.0F);
    }

    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
        for(String tmp : BLOCKED_HULLMODS) {
            if (ship.getVariant().getHullMods().contains(tmp)) {
                MagicIncompatibleHullmods.removeHullmodWithWarning(ship.getVariant(), tmp, "targetlocationex_ac");
            }
        }

    }

    public boolean affectsOPCosts() {
        return true;
    }

    static {
        BLOCKED_HULLMODS.add("targetingunit");
        BLOCKED_HULLMODS.add("dedicated_targeting_core");
    }
}
