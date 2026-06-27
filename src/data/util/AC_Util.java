//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package data.util;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.RepLevel;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.combat.BeamAPI;
import com.fs.starfarer.api.combat.CollisionClass;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.ShieldAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import data.scripts.util.MagicFakeBeam;
import java.util.ArrayList;
import java.util.List;
import org.lazywizard.lazylib.CollisionUtils;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

public class AC_Util {
    //import com.sun.istack.internal.Nullable;


    public static boolean shieldHit(BeamAPI beam, ShipAPI target) {
        return target.getShield() != null && target.getShield().isOn() && target.getShield().isWithinArc(beam.getTo());
    }

    public static Vector2f getShipCollisionPoint(Vector2f segStart, Vector2f segEnd, ShipAPI ship) {
        if (ship.getCollisionClass() == CollisionClass.NONE) {
            return null;
        } else {
            ShieldAPI shield = ship.getShield();
            if (shield != null && !shield.isOff()) {
                Vector2f circleCenter = shield.getLocation();
                float circleRadius = shield.getRadius();
                if (MathUtils.isPointWithinCircle(segStart, circleCenter, circleRadius)) {
                    return shield.isWithinArc(segStart) ? segStart : getCollisionPointEX(segStart, segEnd, ship);
                } else {
                    Vector2f tmp1 = MagicFakeBeam.getCollisionPointOnCircumference(segStart, segEnd, circleCenter, circleRadius);
                    return tmp1 != null && shield.isWithinArc(tmp1) ? tmp1 : getCollisionPointEX(segStart, segEnd, ship);
                }
            } else {
                return getCollisionPointEX(segStart, segEnd, ship);
            }
        }
    }

    public static Vector2f getCollisionPointEX(Vector2f lineStart, Vector2f lineEnd, CombatEntityAPI target) {
        return CollisionUtils.isPointWithinBounds(lineStart, target) ? lineStart : CollisionUtils.getCollisionPoint(lineStart, lineEnd, target);
    }

    public static boolean isPlayerOwned(MarketAPI market) {//判断市场所有权
        return market.getFactionId().contentEquals("player");
    }

    public static boolean isIndependentOwned(MarketAPI market) {//判断市场所有权
        return market.getFactionId().contentEquals("independent");
    }

    public static boolean isPiratesOwned(MarketAPI market) {//判断市场所有权
        return market.getFactionId().contentEquals("pirates");
    }

    public static boolean isPLOwned(MarketAPI market) {//判断市场所有权
        return market.getFactionId().contentEquals("persean_league");
    }

    public static boolean playerHasAtLeastStandingWith(RepLevel minStanding, String factionName) {//判断势力市场最低关系限制
        return Global.getSector().getFaction(factionName).getRelationshipLevel(Global.getSector().getFaction("player")).isAtWorst(minStanding);
    }

    public static class I18nSection {
        private final String category;
        private final String keyPrefix;
        private static final List<I18nSection> sections = new ArrayList();

        public I18nSection(String category, String keyPrefix) {
            this.category = category;
            if (keyPrefix != null) {
                this.keyPrefix = keyPrefix;
            } else {
                this.keyPrefix = "";
            }

            sections.add(this);
        }

        public I18nSection(String category) {
            this(category, (String)null);
        }

        public String format(String keyMainBody,  Object... args) {
            return args != null && args.length > 0 ? this.absFormat(keyMainBody, args) : this.get(keyMainBody);
        }

        public String get() {
            try {
                return Global.getSettings().getString(this.category, this.keyPrefix);
            } catch (Exception var2) {
                return "[NULL]";
            }
        }

        public String get(String key) {
            try {
                return Global.getSettings().getString(this.category, this.keyPrefix + key);
            } catch (Exception var3) {
                return "[NULL]";
            }
        }

        private String absFormat(String key, Object... args) {
            try {
                String result = String.format(this.get(key), args);
                return result;
            } catch (Exception var4) {
                return "[NULL]";
            }
        }

        public static I18nSection getInstance(String category, String keyPrefix) {
            for(I18nSection section : sections) {
                if (section.category.contentEquals(category) && section.keyPrefix.contentEquals(keyPrefix)) {
                    return section;
                }
            }

            return new I18nSection(category, keyPrefix);
        }
    }


}
