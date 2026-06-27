package data.hullmods.scripts;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;
import com.fs.starfarer.api.util.Misc;

public class AC_SafetyOverrides extends BaseHullMod {

	private static Map speed = new HashMap();
	static {
		speed.put(HullSize.FRIGATE, 20f);
		speed.put(HullSize.DESTROYER, 20f);
		speed.put(HullSize.CRUISER, 20f);
		speed.put(HullSize.CAPITAL_SHIP, 20f);
	}
	
	private static final float PEAK_MULT = 0.33f;
	private static final float FLUX_DISSIPATION_MULT = 1.5f;
	private static final float RANGE_THRESHOLD = 600f;
	private static final float RANGE_MULT = 0.25f;
	private static final float MANUE_MULT = 0.75f;
	private static final float ARMOUR_MULT = 0.66f;
	private static final float DP_MULT = 1.5f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getMaxSpeed().modifyFlat(id, (Float) speed.get(hullSize));
		stats.getAcceleration().modifyFlat(id, (Float) speed.get(hullSize) * 2f);
		stats.getDeceleration().modifyFlat(id, (Float) speed.get(hullSize) * 2f);
		stats.getZeroFluxMinimumFluxLevel().modifyFlat(id, 2f); // set to two, meaning boost is always on 
		
		stats.getFluxDissipation().modifyMult(id, FLUX_DISSIPATION_MULT);
		
		stats.getPeakCRDuration().modifyMult(id, PEAK_MULT);
		stats.getVentRateMult().modifyMult(id, 0f);
		
		stats.getWeaponRangeThreshold().modifyFlat(id, RANGE_THRESHOLD);
		stats.getWeaponRangeMultPastThreshold().modifyMult(id, RANGE_MULT);
		stats.getWeaponTurnRateBonus() .modifyMult(id, MANUE_MULT);
		stats.getZeroFluxSpeedBoost().modifyMult(id, MANUE_MULT);

		stats.getArmorBonus().modifyMult(id, ARMOUR_MULT);
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + ((Float) speed.get(HullSize.CAPITAL_SHIP)).intValue();
		if (index == 1) return Misc.getRoundedValue(FLUX_DISSIPATION_MULT);
		if (index == 2) return "two-thirds";
		if (index == 3) return Misc.getRoundedValue(RANGE_THRESHOLD);
		if (index == 4) return "75%";
		if (index == 5) return "two-thirds";

		return null;
	}

	@Override
	public boolean isApplicableToShip(ShipAPI ship) {
		if (ship.getVariant().getHullSize() != HullSize.CAPITAL_SHIP) return false;
		if (ship.getVariant().hasHullMod(HullMods.CIVGRADE) && !ship.getVariant().hasHullMod(HullMods.MILITARIZED_SUBSYSTEMS)) return false;
		if (ship.getVariant().hasHullMod(HullMods.FLUX_SHUNT)) return false;

		return true;
	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		if (ship.getVariant().getHullSize() != HullSize.CAPITAL_SHIP) {
			return "Can only be installed on Capital Ships";
		}
		if (ship.getVariant().hasHullMod(HullMods.CIVGRADE) && !ship.getVariant().hasHullMod(HullMods.MILITARIZED_SUBSYSTEMS)) {
			return "Cannot be installed on civilian ships (regardless of Militarized Subsystems)";
		}
		if (ship.getVariant().hasHullMod(HullMods.FLUX_SHUNT)) {
			return "Incompatible with Flux Shunt";
		}
		
		return null;
	}
	

	private Color color = new Color(110, 250, 255,255);
	@Override
	public void advanceInCombat(ShipAPI ship, float amount) {
			ship.getEngineController().fadeToOtherColor(this, color, null, 1f, 0.4f);
			ship.getEngineController().extendFlame(this, 0.25f, 0.25f, 0.25f);
	}
}
