//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package data.hullmods.scripts;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.DamageTakenModifier;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import data.util.AC_Util;
import data.util.AC_Util.I18nSection;
import org.lwjgl.util.vector.Vector2f;

public class AC_HighDamperArmor extends BaseHullMod {
    protected static float THRESHOLD = 100.0F;
    protected static float BELOW_THRESHOLD_MULT = 0.5F;
    protected static float ABOVE_THRESHOLD_MULT = 0.85F;
    protected static final String KEY = "HighDamperArmor_Debuff";
    public static final AC_Util.I18nSection strings = I18nSection.getInstance("HullMod", "AC_");

    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 10.0F;
        float padS = 5.0F;
        tooltip.addSectionHeading("Effect on Ship", Alignment.TMID, 10.0F);
        tooltip.addPara(" %s " + strings.get("HighDamperArmor_TEXT1"), pad, Misc.getPositiveHighlightColor(), new String[]{"#"});
        tooltip.addPara(" %s " + strings.get("HighDamperArmor_TEXT2"), pad, Misc.getPositiveHighlightColor(), new String[]{"#", "100", "50%"});
        tooltip.addPara(" %s " + strings.get("HighDamperArmor_TEXT3"), pad, Misc.getPositiveHighlightColor(), new String[]{"#", "100", "15%"});
    }

    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
        ship.addListener(new HighDamperArmorListener(ship));
    }

    public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
        return super.getDescriptionParam(index, hullSize);
    }

    public static class HighDamperArmorListener implements DamageTakenModifier {
        private ShipAPI ship;

        public HighDamperArmorListener(ShipAPI ship) {
            this.ship = ship;
        }

        public String modifyDamageTaken(Object param, CombatEntityAPI target, DamageAPI damage, Vector2f point, boolean shieldHit) {
            if (!shieldHit) {
                float cDamage = damage.getDamage();
                if (this.isHullHitQuick(point)) {
                    cDamage *= damage.getType().getHullMult();
                } else {
                    cDamage *= damage.getType().getArmorMult();
                }

                if (cDamage <= AC_HighDamperArmor.THRESHOLD) {
                    damage.getModifier().modifyMult("HighDamperArmor_Debuff", AC_HighDamperArmor.BELOW_THRESHOLD_MULT);
                } else {
                    damage.getModifier().modifyMult("HighDamperArmor_Debuff", AC_HighDamperArmor.ABOVE_THRESHOLD_MULT);
                }

                return "HighDamperArmor_Debuff";
            } else {
                return null;
            }
        }

        private boolean isHullHitQuick(Vector2f point) {
            return this.ship.getArmorGrid().getArmorFraction(this.ship.getArmorGrid().getCellAtLocation(point)[0], this.ship.getArmorGrid().getCellAtLocation(point)[1]) <= 0.01F;
        }
    }
}
