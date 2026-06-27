//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package data.hullmods.scripts;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import data.scripts.util.MagicIncompatibleHullmods;
import data.util.AC_Util;
import data.util.AC_Util.I18nSection;
import java.util.HashSet;
import java.util.Set;

public class AC_HangarDisabled extends BaseHullMod {
    public static final AC_Util.I18nSection strings = I18nSection.getInstance("HullMod", "AC_");
    private static final Set<String> BLOCKED_HULLMODS = new HashSet();

    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 10.0F;
        tooltip.addSectionHeading("Effect on Ship", Alignment.TMID, 10.0F);
        tooltip.addPara(" %s " + strings.get("HangarDisabled_TEXT1"), pad, Misc.getNegativeHighlightColor(), new String[]{"#"});
    }

    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
        for(String tmp : BLOCKED_HULLMODS) {
            if (ship.getVariant().getHullMods().contains(tmp)) {
                MagicIncompatibleHullmods.removeHullmodWithWarning(ship.getVariant(), tmp, "hangardisabled_ac");
            }
        }

    }

    public boolean affectsOPCosts() {
        return true;
    }

    static {
        BLOCKED_HULLMODS.add("converted_hangar");
    }
}
