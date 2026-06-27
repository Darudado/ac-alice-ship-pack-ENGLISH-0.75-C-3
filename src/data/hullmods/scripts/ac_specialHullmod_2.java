package data.hullmods.scripts;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.listeners.AdvanceableListener;
import com.fs.starfarer.api.combat.listeners.DamageTakenModifier;
import com.fs.starfarer.api.loading.DamagingExplosionSpec;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

/**
 * 阿里斯重定向-陷阵
 * 需要阿里斯协议才能安装
 * 与阿里斯重定向-弹幕互斥
 * 
 * 射程限制由ac_specialHullmod_1动态处理
 */
public class ac_specialHullmod_2 extends BaseHullMod {
	
	// 装甲和EMP加成
	public static final float MAX_ARMOR_MULT = 1.5f;
	public static final float MAX_EMP_REDUCTION = 0.75f;

    //武器射速加成
    public static final float ROF = 25f;
    public static final float REGEN = 25f;
	
	// 系统激活时的加成
	public static final float REPAIR_RATE_BONUS = 0.5f;
	public static final float MANEUVER_BONUS = 0.5f;
	public static final float FORWARD_SPEED_BONUS = 25f;
	
	// 殉爆伤害减免
	public static final float OVERLOAD_DAMAGE_REDUCTION = 0.9f;
	
	// 支持的系统ID
	private static final Set<String> SUPPORTED_SYSTEMS = new HashSet<>();
	static {
		SUPPORTED_SYSTEMS.add("damper");      // 阻尼力场
		SUPPORTED_SYSTEMS.add("highspeedcharger_ac");    // 高速充能器
		SUPPORTED_SYSTEMS.add("highenergyfocus");     // 高能聚焦
		SUPPORTED_SYSTEMS.add("ammofeed");            // 加速填弹器
	}
	
	@Override
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		// 不在这里处理效果
	}
	
	@Override
	public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
        MutableShipStatsAPI stats = ship.getMutableStats();
        //强化武器射速和回充
        stats.getBallisticRoFMult().modifyPercent(id , ROF);
        stats.getEnergyRoFMult().modifyPercent(id , ROF);
        stats.getBallisticAmmoRegenMult().modifyPercent(id , REGEN);
        stats.getEnergyAmmoRegenMult().modifyPercent(id , REGEN);

		// 添加动态效果监听器
		ship.addListener(new AliceRedirectAssaultListener(ship));
		
		// 添加殉爆伤害监听器
		if (!ship.hasListenerOfClass(OverloadDamageListener.class)) {
			ship.addListener(new OverloadDamageListener());
		}
	}
	
	/**
	 * 殉爆伤害监听器
	 */
	public static class OverloadDamageListener implements DamageTakenModifier {
		@Override
		public String modifyDamageTaken(Object param, CombatEntityAPI target, DamageAPI damage, Vector2f point,
				boolean shieldHit) {
			
			// 处理殉爆伤害
			if (param instanceof DamagingProjectileAPI) {
				DamagingProjectileAPI projectile = (DamagingProjectileAPI) param;
				ShipAPI source = projectile.getSource();
				if (source != null) {
					float explosionRadius = DamagingExplosionSpec.getShipExplosionRadius(source);
					if ((projectile.getCollisionRadius() - explosionRadius) == 0) {
						// 殉爆伤害减免90%
						damage.getModifier().modifyMult("ac_specialHullmod_2_explosion", 1f - OVERLOAD_DAMAGE_REDUCTION);
						return "Alice Redirect: Charge - Reduced Explosion Damage";
					}
				}
			}
			
			return null;
		}
	}
	
	/**
	 * 动态效果监听器
	 */
	public static class AliceRedirectAssaultListener implements AdvanceableListener {
		private final ShipAPI ship;
		
		public AliceRedirectAssaultListener(ShipAPI ship) {
			this.ship = ship;
		}
		
		@Override
		public void advance(float amount) {
			if (ship.isHulk()) {
				return;
			}
			
			String id = "ac_specialHullmod_2";
			MutableShipStatsAPI stats = ship.getMutableStats();
			
			// 1. 基于幅能的装甲和EMP加成
			float flux = ship.getFluxTracker().getCurrFlux();
			float maxFlux = ship.getFluxTracker().getMaxFlux();
			float fluxLevel = maxFlux > 0 ? flux / maxFlux : 0f;
			
			// 幅能越高，加成越高
			float armorMult = 1f + (MAX_ARMOR_MULT - 1f) * fluxLevel;
			float empReduction = MAX_EMP_REDUCTION * fluxLevel;
			
			stats.getArmorDamageTakenMult().modifyMult(id + "_armor", 1f / armorMult);
			stats.getEmpDamageTakenMult().modifyMult(id + "_emp", 1f - empReduction);
			
			// 2. 系统激活时的加成
			if (ship.getSystem() != null && SUPPORTED_SYSTEMS.contains(ship.getSystem().getId())) {
				if (ship.getSystem().isActive()) {
					// 修复速率加成
					stats.getCombatEngineRepairTimeMult().modifyMult(id + "_repair", 1f / (1f + REPAIR_RATE_BONUS));
					stats.getCombatWeaponRepairTimeMult().modifyMult(id + "_repair", 1f / (1f + REPAIR_RATE_BONUS));
					
					// 机动性加成
					stats.getAcceleration().modifyPercent(id + "_maneuver", MANEUVER_BONUS * 100f);
					stats.getDeceleration().modifyPercent(id + "_maneuver", MANEUVER_BONUS * 100f);
					stats.getTurnAcceleration().modifyPercent(id + "_maneuver", MANEUVER_BONUS * 100f);
					stats.getMaxTurnRate().modifyPercent(id + "_maneuver", MANEUVER_BONUS * 100f);
					
					// 额外航速
                    stats.getMaxSpeed().modifyFlat(id + "_forward", FORWARD_SPEED_BONUS);

				} else {
					// 系统未激活时移除加成
					stats.getCombatEngineRepairTimeMult().unmodify(id + "_repair");
					stats.getCombatWeaponRepairTimeMult().unmodify(id + "_repair");
					stats.getAcceleration().unmodify(id + "_maneuver");
					stats.getDeceleration().unmodify(id + "_maneuver");
					stats.getTurnAcceleration().unmodify(id + "_maneuver");
					stats.getMaxTurnRate().unmodify(id + "_maneuver");
					stats.getMaxSpeed().unmodify(id + "_forward");
				}
			}
		}
	}
	
	@Override
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "700";
		if (index == 1) return "" + (int) (MAX_ARMOR_MULT * 100f) + "%";
		if (index == 2) return "" + (int) (MAX_EMP_REDUCTION * 100f) + "%";
		if (index == 3) return "" + (int) (REPAIR_RATE_BONUS * 100f) + "%";
		if (index == 4) return "" + (int) FORWARD_SPEED_BONUS;
		if (index == 5) return "" + (int) (OVERLOAD_DAMAGE_REDUCTION * 100f) + "%";
		return null;
	}
	
	@Override
	public boolean shouldAddDescriptionToTooltip(HullSize hullSize, ShipAPI ship, boolean isForModSpec) {
		return false;
	}
	
	@Override
	public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, 
										   float width, boolean isForModSpec) {
		float opad = 10f;
		Color h = Misc.getHighlightColor();
		Color bad = Misc.getNegativeHighlightColor();
		
		tooltip.addPara("Alice Redirect: Charge is an aggressive close-quarters optimization package,"
				+ "sacrificing range in exchange for strong survivability and mobility.", opad);
		
		tooltip.addSectionHeading("Range Limitation", Alignment.MID, opad);
		
		tooltip.addPara("# Alice Protocol weapon range limit worsened to %s", opad, bad, 
				"700");
		
		tooltip.addSectionHeading("Defense Enhancement", Alignment.MID, opad);
		
		tooltip.addPara("# Higher flux increases armor damage reduction and EMP resistance", opad);
		tooltip.addPara("  - Up to %s x armor damage reduction", 3f, h,
				"" + MAX_ARMOR_MULT + "x");
		tooltip.addPara("  - Up to %s EMP resistance", 3f, h,
				"" + (int) (MAX_EMP_REDUCTION * 100f) + "%");
        tooltip.addPara("# Ammunition explosion damage taken reduced by %s", opad, h,
                "" + (int) (OVERLOAD_DAMAGE_REDUCTION * 100f) + "%");
		
		tooltip.addSectionHeading("System Synergy", Alignment.MID, opad);
		
		tooltip.addPara("When using Damper Field, High Speed Charger, High Energy Focus, or Accelerated Ammo Feeder:", opad);
		tooltip.addPara("# Disabled component repair rate + %s", 3f, h,
				"" + (int) (REPAIR_RATE_BONUS * 100f) + "%");
		tooltip.addPara("# Ship maneuverability + %s", 3f, h,
				"" + (int) (MANEUVER_BONUS * 100f) + "%");
		tooltip.addPara("# Extra %s top speed", 3f, h,
				"" + (int) FORWARD_SPEED_BONUS);
		
		tooltip.addSectionHeading("Weapon Overclocking", Alignment.MID, opad);
		tooltip.addPara("Rate of fire for all weapons + %s", 3f, h ,""+(int)(ROF) + "%");
        tooltip.addPara("Ammo replenishment rate for all weapons + %s", 3f, h ,""+(int)(REGEN) + "%");

	}
	
	@Override
	public String getUnapplicableReason(ShipAPI ship) {
		// 检查是否有阿里斯协议
		if (ship != null && !ship.getVariant().hasHullMod("ac_specialHullmod_1")) {
			return "Requires Alice Protocol";
		}
		
		// 检查是否有阿里斯重定向-弹幕
		if (ship != null && ship.getVariant().hasHullMod("ac_specialHullmod_3")) {
			return "Incompatible with Alice Redirect: Barrage";
		}
		
		return null;
	}
	
	@Override
	public boolean isApplicableToShip(ShipAPI ship) {
		return getUnapplicableReason(ship) == null;
	}
	
	@Override
	public float getTooltipWidth() {
		return 420f;
	}
}
