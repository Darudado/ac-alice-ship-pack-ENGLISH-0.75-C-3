//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package data.hullmods.scripts;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponSize;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponType;
import com.fs.starfarer.api.combat.listeners.WeaponBaseRangeModifier;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import data.util.AC_Util;
import data.util.AC_Util.I18nSection;

public class AC_EnergyIncreaseSmall extends BaseHullMod {
    public static final AC_Util.I18nSection strings = I18nSection.getInstance("HullMod", "AC_");
    public static float RANGE_BONUS = 200.0F;

    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 10.0F;
        tooltip.addSectionHeading("Effect on Ship", Alignment.TMID, 10.0F);
        tooltip.addPara(" %s " + strings.get("EnergyIncreaseSmall_TEXT1"), pad, Misc.getPositiveHighlightColor(), new String[]{"#", "200"});
    }

    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
        ship.addListener(new AC_EnergyIncreaseSmallRangeModifier());
    }

    public boolean affectsOPCosts() {
        return true;
    }

    public static class AC_EnergyIncreaseSmallRangeModifier implements WeaponBaseRangeModifier {
        public float getWeaponBaseRangePercentMod(ShipAPI ship, WeaponAPI weapon) {
            return 0.0F;
        }

        public float getWeaponBaseRangeMultMod(ShipAPI ship, WeaponAPI weapon) {
            return 1.0F;
        }

        public float getWeaponBaseRangeFlatMod(ShipAPI ship, WeaponAPI weapon) {
            if (weapon.isBeam()) {
                return 0.0F;
            } else if (weapon.getType() != WeaponType.ENERGY && weapon.getType() != WeaponType.HYBRID) {
                return 0.0F;
            } else {
                return weapon.getSize() != WeaponSize.SMALL ? 0.0F : AC_EnergyIncreaseSmall.RANGE_BONUS;
            }
        }
    }
}
