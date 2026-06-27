package data.hullmods.scripts;

import java.awt.Color;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponSize;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponType;
import com.fs.starfarer.api.combat.listeners.WeaponBaseRangeModifier;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

/**
 * 阿里斯协议船体改装模块
 * 效果：
 * 1. 小/中型非导弹武器的基础射程+200/+100
 * 2. 所有非导弹武器的基础射程限制在900之内
 */
public class ac_specialHullmod_1 extends BaseHullMod {
	
	// 射程相关参数
	public static final float SMALL_RANGE_BONUS = 200f;  // 小型武器射程加成
    public static final float MID_RANGE_BONUS = 100f;  // 中型武器射程加成

    public static final float MAX_RANGE_LIMIT = 900f;    // 射程上限
	
	@Override
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
	}
	
	@Override
	public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
		// 添加射程修改监听器
		ship.addListener(new AliceProtocolRangeModifier());
	}
	
	/**
	 * 阿里斯协议射程修改器
	 */
	public static class AliceProtocolRangeModifier implements WeaponBaseRangeModifier {
		
		@Override
		public float getWeaponBaseRangePercentMod(ShipAPI ship, WeaponAPI weapon) {
			return 0;
		}
		
		@Override
		public float getWeaponBaseRangeMultMod(ShipAPI ship, WeaponAPI weapon) {
			return 1f;
		}
		
		@Override
		public float getWeaponBaseRangeFlatMod(ShipAPI ship, WeaponAPI weapon) {
			// 安全检查
			if (weapon.getSpec() == null) {
				return 0f;
			}
			
			// 只影响非导弹武器
			if (weapon.getType() == WeaponType.MISSILE) {
				return 0f;
			}
			
			float bonus = 0f;
			float rangeLimit = MAX_RANGE_LIMIT; // 默认900
			
			// 检查是否安装了重定向船插，调整射程限制
			if (ship.getVariant().hasHullMod("ac_specialHullmod_2")) {
				// 陷阵模式：限制恶化至700
				rangeLimit = 700f;
			} else if (ship.getVariant().hasHullMod("ac_specialHullmod_3")) {
				// 弹幕模式：限制放宽至1200
				rangeLimit = 1200f;
			}
			
			// 小型武器获得射程加成
			if (weapon.getSize() == WeaponSize.SMALL) {
				bonus += SMALL_RANGE_BONUS;
			}

            // 小型武器获得射程加成
            if (weapon.getSize() == WeaponSize.MEDIUM) {
                bonus += MID_RANGE_BONUS;
            }
			
			// 检查射程上限限制
			float currentRange = weapon.getSpec().getMaxRange();
			if (currentRange + bonus > rangeLimit) {
				bonus = rangeLimit - currentRange;
			}
			
			// 确保加成不为负数
			if (bonus < 0) {
				bonus = 0;
			}
			
			return bonus;
		}
	}
	
	@Override
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) SMALL_RANGE_BONUS;
        if (index == 1) return "" + (int) MID_RANGE_BONUS;
        if (index == 2) return "" + (int) MAX_RANGE_LIMIT;
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
		
		tooltip.addPara("\nThis protocol, supporting external crew cabins and standard retrofitted hangars, not only kicks out standard Remnant drone subsystems, but also offers extra bonuses,"
				+ "\nBase Effect: Increases small weapon range through precise fire control algorithms.", opad);
		
		tooltip.addSectionHeading("Range Enhancement", Alignment.MID, opad);
		
		tooltip.addPara("# Small non-missile weapon base range + %s", opad, h, 
				"" + (int) SMALL_RANGE_BONUS);

        tooltip.addPara("# Medium non-missile weapon base range + %s", opad, h,
                "" + (int) MID_RANGE_BONUS);

		tooltip.addPara("# All non-missile weapon base range limited to %s", 3f, h,
				"" + (int) MAX_RANGE_LIMIT);
		
		tooltip.addSectionHeading("Note", Alignment.MID, opad);
		
		tooltip.addPara("Since it increases base range, this modifier will be further amplified by percentage modifiers from other hullmods and skills."
				+ "", opad);

        tooltip.addPara("Meanwhile, this hullmod supports installing two other redirect hullmods.",opad);
	}
	
	@Override
	public float getTooltipWidth() {
		return 400f;
	}
}
