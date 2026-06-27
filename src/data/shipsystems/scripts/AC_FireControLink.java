package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineLayers;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.AdvanceableListener;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.plugins.ShipSystemStatsScript;
import com.fs.starfarer.api.util.Misc;
import data.util.AC_ColorData;
import java.util.List;
import org.lazywizard.lazylib.combat.CombatUtils;
import org.lwjgl.util.vector.Vector2f;
import org.magiclib.util.MagicRender;

public class AC_FireControLink extends BaseShipSystemScript {
    int num = 0;

    public void apply(MutableShipStatsAPI stats, String id, ShipSystemStatsScript.State state, float effectLevel) {
        ShipAPI ship = (ShipAPI)stats.getEntity();
        ship.addListener(new FireControlLink1(ship));
        SpriteAPI effect = Global.getSettings().getSprite("fx", "HUAN1");
        Vector2f size = new Vector2f(6000.0F, 6000.0F);
        if (this.num == 0) {
            MagicRender.objectspace(effect, ship, new Vector2f(), new Vector2f(), size, ship.getRenderOffset(), -180.0F, 33.0F, true, Misc.scaleAlpha(AC_ColorData.HIGH_BLUE, Math.min(0.5F, 0.8F)), 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5F, 9.0F, 0.5F, true, CombatEngineLayers.BELOW_SHIPS_LAYER, 770, 1);
            ++this.num;
        }

    }

    public void unapply(MutableShipStatsAPI stats, String id) {
        ShipAPI ship = (ShipAPI)stats.getEntity();
        ship.removeListener(new FireControlLink1(ship));
        this.num = 0;
    }

    public ShipSystemStatsScript.StatusData getStatusData(int index, ShipSystemStatsScript.State state, float effectLevel) {
        return index == 0 ? new ShipSystemStatsScript.StatusData("+20% range for non-capital allies", false) : null;
    }

    public static class FireControlLink1 implements AdvanceableListener {
        protected ShipAPI ship;
        protected String id = "FireControlLink1";

        public FireControlLink1(ShipAPI ship) {
            this.ship = ship;
        }

        public void advance(float amount) {
            List<ShipAPI> ships = CombatUtils.getShipsWithinRange(this.ship.getLocation(), 3000.0F);
            if (!ships.isEmpty()) {
                for(ShipAPI ship2 : ships) {
                    if (ship2.isAlive() && !ship2.isCapital() && ship2.getOwner() == 0) {
                        ship2.getMutableStats().getEnergyWeaponRangeBonus().modifyPercent(this.id, 20.0F);
                        ship2.getMutableStats().getBallisticWeaponRangeBonus().modifyPercent(this.id, 20.0F);
                    }
                }
            }

        }
    }
}
