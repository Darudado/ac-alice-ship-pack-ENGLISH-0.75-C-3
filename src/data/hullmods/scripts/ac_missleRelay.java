package data.hullmods.scripts;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import data.util.AC_Util;

import java.awt.*;

public class ac_missleRelay extends BaseHullMod {
    public static final AC_Util.I18nSection strings = AC_Util.I18nSection.getInstance("HullMod", "AC_");
    public static float SPEED_MULTIPLIER = 250.0F;

    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float opad = 0f;
        Color h = Misc.getHighlightColor();
        float pad = 10.0F;
        float padS = 5.0F;
        tooltip.addSectionHeading("Effect on Ship", Alignment.TMID, 10.0F);
        tooltip.addPara("# Missile weapon top speed + %s", opad, h,
                "" + (int) SPEED_MULTIPLIER);
    }

    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getMissileMaxSpeedBonus().modifyPercent(id, SPEED_MULTIPLIER);
    }

    public boolean affectsOPCosts() {
        return true;
    }

}
