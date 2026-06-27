package data.hullmods.scripts;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.listeners.AdvanceableListener;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

/**
 * 阿里斯重定向-弹幕
 * 需要阿里斯协议才能安装
 * 与阿里斯重定向-陷阵互斥
 * 
 * 射程限制由ac_specialHullmod_1动态处理
 */
public class ac_specialHullmod_3 extends BaseHullMod {
	
	// 航速惩罚
	public static final float SPEED_PENALTY = 0.33f;
	
	// 零幅能加速的幅能阈值
	public static final float ZERO_FLUX_THRESHOLD = 0.25f;
	
	// 系统激活时的加成
	public static final float PROJECTILE_SPEED_BONUS = 1.0f;
	public static final float DAMAGE_BONUS = 0.2f;
	
	// 目标定位系统的额外射程加成
	public static final float TARGETING_RANGE_BONUS = 0.2f;
	
	// 支持的系统ID
	private static final Set<String> SUPPORTED_SYSTEMS = new HashSet<>();
	static {
		SUPPORTED_SYSTEMS.add("plasmajets");           // 等离子推进器
		SUPPORTED_SYSTEMS.add("maneuveringjets");      // 机动推进器
	}
	
	@Override
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		// 航速惩罚
		stats.getMaxSpeed().modifyMult(id, 1f - SPEED_PENALTY);
		
		// 零幅能加速
		stats.getZeroFluxSpeedBoost().modifyFlat(id, stats.getMaxSpeed().getModifiedValue());
		stats.getZeroFluxMinimumFluxLevel().modifyFlat(id, ZERO_FLUX_THRESHOLD);
	}
	
	@Override
	public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
		// 目标定位系统的额外射程加成
		MutableShipStatsAPI stats = ship.getMutableStats();
		
		if (ship.getVariant().hasHullMod("advancedcore")) {
			// 先进目标定位核心：额外20%射程
			stats.getEnergyWeaponRangeBonus().modifyPercent(id + "_targeting", 20f);
			stats.getBallisticWeaponRangeBonus().modifyPercent(id + "_targeting", 20f);
		} else if (ship.getVariant().hasHullMod("targetingunit")) {
			// 目标定位系统：额外12%射程
			stats.getEnergyWeaponRangeBonus().modifyPercent(id + "_targeting", 12f);
			stats.getBallisticWeaponRangeBonus().modifyPercent(id + "_targeting", 12f);
		}
		
		// 添加动态效果监听器
		ship.addListener(new AliceRedirectBarrageListener(ship));
	}
	
	/**
	 * 动态效果监听器
	 */
	public static class AliceRedirectBarrageListener implements AdvanceableListener {
		private final ShipAPI ship;
		
		public AliceRedirectBarrageListener(ShipAPI ship) {
			this.ship = ship;
		}
		
		@Override
		public void advance(float amount) {
			if (ship.isHulk()) {
				return;
			}
			
			String id = "ac_specialHullmod_3";
			MutableShipStatsAPI stats = ship.getMutableStats();
			
			// 系统激活时的加成
			if (ship.getSystem() != null && SUPPORTED_SYSTEMS.contains(ship.getSystem().getId())) {
				if (ship.getSystem().isActive()) {
					// 弹道速度加成
					stats.getProjectileSpeedMult().modifyPercent(id + "_proj", PROJECTILE_SPEED_BONUS * 100f);
					
					// 伤害加成
					stats.getEnergyWeaponDamageMult().modifyPercent(id + "_damage", DAMAGE_BONUS * 100f);
					stats.getBallisticWeaponDamageMult().modifyPercent(id + "_damage", DAMAGE_BONUS * 100f);
					
					// 强制关闭护盾
					if (ship.getShield() != null) {
						ship.getShield().toggleOff();
					}
				} else {
					// 系统未激活时移除加成
					stats.getProjectileSpeedMult().unmodify(id + "_proj");
					stats.getEnergyWeaponDamageMult().unmodify(id + "_damage");
					stats.getBallisticWeaponDamageMult().unmodify(id + "_damage");
				}
			}
		}
	}
	
	@Override
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "1200";
		if (index == 1) return "" + (int) (SPEED_PENALTY * 100f) + "%";
		if (index == 2) return "" + (int) (ZERO_FLUX_THRESHOLD * 100f);
		if (index == 3) return "" + (int) (PROJECTILE_SPEED_BONUS * 100f) + "%";
		if (index == 4) return "" + (int) (DAMAGE_BONUS * 100f) + "%";
		if (index == 5) return "" + (int) (TARGETING_RANGE_BONUS * 100f) + "%";
		return null;
	}
	
	@Override
	public boolean shouldAddDescriptionToTooltip(HullSize hullSize, ShipAPI ship, boolean isForModSpec) {
		return false;
	}
	
	@Override
	public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, 
										   float width, boolean isForModSpec) {
		float opad = 0f;
		Color h = Misc.getHighlightColor();
		Color bad = Misc.getNegativeHighlightColor();
		
		tooltip.addPara("Alice Redirect: Barrage is a long-range firepower optimization package,"
				+ "sacrificing mobility in exchange for longer range and greater firepower.", opad);
		
		tooltip.addSectionHeading("Range Enhancement", Alignment.MID, opad);
		
		tooltip.addPara("# Alice Protocol weapon range limit extended to %s", opad, h, 
				"1200");
		
		tooltip.addSectionHeading("Mobility Penalty", Alignment.MID, opad);
		
		tooltip.addPara("# Ship top speed reduced by %s, but zero-flux boost is enhanced based on speed", opad, bad,
				"" + (int) (SPEED_PENALTY * 100f) + "%");
		tooltip.addPara("# Zero-flux boost is active as long as flux is no higher than %s", 3f, h,
				"" + (int) (ZERO_FLUX_THRESHOLD * 100f));
		
		tooltip.addSectionHeading("System Synergy", Alignment.MID, opad);
		
		tooltip.addPara("When using Plasma Jets or Maneuvering Jets:", opad);
		tooltip.addPara("# Weapon projectile velocity + %s", 3f, h,
				"" + (int) (PROJECTILE_SPEED_BONUS * 100f) + "%");
		tooltip.addPara("# Damage for all weapons + %s", 3f, h,
				"" + (int) (DAMAGE_BONUS * 100f) + "%");
		tooltip.addPara("# Forcefully disables ship shield during system active state", 3f, bad);
		
		tooltip.addSectionHeading("Targeting Enhancement", Alignment.MID, opad);
		
		tooltip.addPara("With Barrage Mode installed, Target Location System provides additional range bonuses for energy and ballistic weapons:", opad);
		tooltip.addPara("# Advanced Target Location Core: Extra %s range", 3f, h, "20%");
		tooltip.addPara("# Target Location System: Extra %s range", 3f, h, "12%");
	}
	
	@Override
	public String getUnapplicableReason(ShipAPI ship) {
		// 检查是否有阿里斯协议
		if (ship != null && !ship.getVariant().hasHullMod("ac_specialHullmod_1")) {
			return "Requires Alice Protocol";
		}
		
		// 检查是否有阿里斯重定向-陷阵
		if (ship != null && ship.getVariant().hasHullMod("ac_specialHullmod_2")) {
			return "Incompatible with Alice Redirect: Charge";
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
