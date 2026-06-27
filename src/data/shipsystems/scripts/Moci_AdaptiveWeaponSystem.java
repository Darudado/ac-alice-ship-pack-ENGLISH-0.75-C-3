package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponType;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

/**
 * import org.omg.PortableServer.POAManagerPackage.State;
 * 自适应武器系统
 * 根据MAIN_01和MAIN_02槽位上的武器类型提供不同加成
 * 
 * 加成规则：
 * - 导弹武器：+100航速 和 +50%机动性
 * - 实弹武器：+50%实弹和能量武器射速，+50%耗散
 * - 能量武器：+50%实弹和能量武器伤害
 * 
 * 注意：两个槽位装不同类型武器可以同时获得两种加成
 *       两个槽位装相同类型武器只获得一层加成
 */
public class Moci_AdaptiveWeaponSystem extends BaseShipSystemScript {
    
    // 加成参数
    private static final float MISSILE_SPEED_BONUS = 100f; // 导弹模式：航速加成
    private static final float MISSILE_MANEUVER_BONUS = 0.5f; // 导弹模式：机动性加成（50%）
    
    private static final float BALLISTIC_ROF_BONUS = 0.5f; // 实弹模式：射速加成（50%）
    private static final float BALLISTIC_DISSIPATE_BONUS = 0.5f; //实弹模式：耗散加成

    private static final float ENERGY_DAMAGE_BONUS = 0.5f; // 能量模式：伤害加成（50%）
    
    // 武器光效颜色（只有实弹和能量武器有光效）
    private static final Color BALLISTIC_GLOW_COLOR = new Color(255, 200, 100, 255); // 实弹：橙色
    private static final Color ENERGY_GLOW_COLOR = new Color(100, 150, 255, 255); // 能量：蓝色
    
    // 引擎光晕颜色
    private Color engineGlowColor = new Color(100, 200, 255, 255);
    
    // 缓存的武器类型集合
    private Set<WeaponType> cachedWeaponTypes = null;
    private boolean cacheValid = false;
    
    @Override
    public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
        ShipAPI ship = (ShipAPI) stats.getEntity();
        if (ship == null) {
            return;
        }
        
        CombatEngineAPI engine = Global.getCombatEngine();
        if (engine == null) {
            return;
        }
        
        // 检测MAIN_01和MAIN_02槽位的武器类型
        Set<WeaponType> weaponTypes = getMainWeaponTypes(ship);
        
        // 根据武器类型应用不同加成
        boolean hasMissile = weaponTypes.contains(WeaponType.MISSILE);
        boolean hasBallistic = weaponTypes.contains(WeaponType.BALLISTIC);
        boolean hasEnergy = weaponTypes.contains(WeaponType.ENERGY);
        
        // 导弹模式：+100航速 和 +50%机动性
        if (hasMissile) {
            stats.getMaxSpeed().modifyFlat(id, MISSILE_SPEED_BONUS * effectLevel);
            stats.getAcceleration().modifyPercent(id, MISSILE_MANEUVER_BONUS * 100f * effectLevel);
            stats.getDeceleration().modifyPercent(id, MISSILE_MANEUVER_BONUS * 100f * effectLevel);
            stats.getTurnAcceleration().modifyPercent(id, MISSILE_MANEUVER_BONUS * 100f * effectLevel);
            stats.getMaxTurnRate().modifyPercent(id, MISSILE_MANEUVER_BONUS * 100f * effectLevel);
        }
        
        // 实弹模式：+50%实弹和能量武器射速
        if (hasBallistic) {
            stats.getBallisticRoFMult().modifyPercent(id, BALLISTIC_ROF_BONUS * 100f * effectLevel);
            stats.getEnergyRoFMult().modifyPercent(id, BALLISTIC_ROF_BONUS * 100f * effectLevel);
        }
        
        // 能量模式：+50%实弹和能量武器伤害
        if (hasEnergy) {
            stats.getBallisticWeaponDamageMult().modifyPercent(id, ENERGY_DAMAGE_BONUS * 100f * effectLevel);
            stats.getEnergyWeaponDamageMult().modifyPercent(id, ENERGY_DAMAGE_BONUS * 100f * effectLevel);
        }
        
        // 等离子推进器特效
        if (state == State.IN || state == State.ACTIVE) {
            // 引擎颜色渐变和火焰延长
            ship.getEngineController().fadeToOtherColor(this, engineGlowColor, new Color(0, 0, 0, 0), effectLevel, 0.67f);
            ship.getEngineController().extendFlame(this, 2f * effectLevel, 0f * effectLevel, 0f * effectLevel);
        }
        
        // 武器光效
        if (state == State.IN || state == State.ACTIVE) {
            applyWeaponGlow(ship, effectLevel, weaponTypes);
        } else if (state == State.OUT) {
            // OUT阶段清除武器光效
            clearWeaponGlow(ship);
        }
    }
    
    @Override
    public void unapply(MutableShipStatsAPI stats, String id) {
        // 移除所有加成
        stats.getMaxSpeed().unmodify(id);
        stats.getAcceleration().unmodify(id);
        stats.getDeceleration().unmodify(id);
        stats.getTurnAcceleration().unmodify(id);
        stats.getMaxTurnRate().unmodify(id);
        
        stats.getBallisticRoFMult().unmodify(id);
        stats.getEnergyRoFMult().unmodify(id);
        
        stats.getBallisticWeaponDamageMult().unmodify(id);
        stats.getEnergyWeaponDamageMult().unmodify(id);
        
        // 清除武器光效
        ShipAPI ship = (ShipAPI) stats.getEntity();
        if (ship != null) {
            clearWeaponGlow(ship);
        }
        
        // 清除缓存
        cacheValid = false;
    }
    
    /**
     * 获取MAIN_01和MAIN_02槽位的武器类型
     * 返回一个Set，自动去重相同类型
     */
    private Set<WeaponType> getMainWeaponTypes(ShipAPI ship) {
        // 如果缓存有效，直接返回
        if (cacheValid && cachedWeaponTypes != null) {
            return cachedWeaponTypes;
        }
        
        Set<WeaponType> types = new HashSet<>();
        
        // 检查MAIN_01槽位
        WeaponAPI main01 = getWeaponInSlot(ship, "MAIN_01");
        if (main01 != null && !main01.isDecorative()) {
            types.add(main01.getType());
        }
        
        // 检查MAIN_02槽位
        WeaponAPI main02 = getWeaponInSlot(ship, "MAIN_02");
        if (main02 != null && !main02.isDecorative()) {
            types.add(main02.getType());
        }
        
        // 更新缓存
        cachedWeaponTypes = types;
        cacheValid = true;
        
        return types;
    }
    
    /**
     * 获取指定槽位的武器
     */
    private WeaponAPI getWeaponInSlot(ShipAPI ship, String slotId) {
        for (WeaponAPI weapon : ship.getAllWeapons()) {
            if (weapon.getSlot() != null && slotId.equals(weapon.getSlot().getId())) {
                return weapon;
            }
        }
        return null;
    }
    
    /**
     * 应用武器光效
     * 只对MAIN_01和MAIN_02槽位的实弹和能量武器应用光效
     */
    private void applyWeaponGlow(ShipAPI ship, float effectLevel, Set<WeaponType> weaponTypes) {
        // 获取MAIN_01和MAIN_02的武器
        WeaponAPI main01 = getWeaponInSlot(ship, "MAIN_01");
        WeaponAPI main02 = getWeaponInSlot(ship, "MAIN_02");
        
        // 为MAIN_01武器添加光效
        if (main01 != null && !main01.isDecorative() && 
            (main01.getType() == WeaponType.BALLISTIC || main01.getType() == WeaponType.ENERGY)) {
            Color glowColor = getGlowColorForWeaponType(main01.getType());
            main01.setGlowAmount(effectLevel, glowColor);
        }
        
        // 为MAIN_02武器添加光效
        if (main02 != null && !main02.isDecorative() && 
            (main02.getType() == WeaponType.BALLISTIC || main02.getType() == WeaponType.ENERGY)) {
            Color glowColor = getGlowColorForWeaponType(main02.getType());
            main02.setGlowAmount(effectLevel, glowColor);
        }
    }
    
    /**
     * 清除武器光效
     */
    private void clearWeaponGlow(ShipAPI ship) {
        WeaponAPI main01 = getWeaponInSlot(ship, "MAIN_01");
        WeaponAPI main02 = getWeaponInSlot(ship, "MAIN_02");
        
        if (main01 != null && !main01.isDecorative() && 
            (main01.getType() == WeaponType.BALLISTIC || main01.getType() == WeaponType.ENERGY)) {
            main01.setGlowAmount(0, null);
        }
        
        if (main02 != null && !main02.isDecorative() && 
            (main02.getType() == WeaponType.BALLISTIC || main02.getType() == WeaponType.ENERGY)) {
            main02.setGlowAmount(0, null);
        }
    }
    
    /**
     * 根据武器类型获取光效颜色
     */
    private Color getGlowColorForWeaponType(WeaponType type) {
        switch (type) {
            case BALLISTIC:
                return BALLISTIC_GLOW_COLOR;
            case ENERGY:
                return ENERGY_GLOW_COLOR;
            default:
                return Color.WHITE;
        }
    }
    
    @Override
    public StatusData getStatusData(int index, State state, float effectLevel) {
        ShipAPI ship = (ShipAPI) Global.getCombatEngine().getPlayerShip();
        if (ship == null) {
            return null;
        }
        
        Set<WeaponType> weaponTypes = getMainWeaponTypes(ship);
        
        // 显示当前激活的加成
        if (index == 0 && weaponTypes.contains(WeaponType.MISSILE)) {
            return new StatusData("+ " + (int)MISSILE_SPEED_BONUS + " top speed, + " + (int)(MISSILE_MANEUVER_BONUS * 100) + "% maneuverability", false);
        }
        
        if (index == 1 && weaponTypes.contains(WeaponType.BALLISTIC)) {
            return new StatusData("+ " + (int)(BALLISTIC_ROF_BONUS * 100) + "% ballistic/energy rate of fire", false);
        }
        
        if (index == 2 && weaponTypes.contains(WeaponType.ENERGY)) {
            return new StatusData("+ " + (int)(ENERGY_DAMAGE_BONUS * 100) + "% ballistic/energy damage", false);
        }
        
        return null;
    }
}
