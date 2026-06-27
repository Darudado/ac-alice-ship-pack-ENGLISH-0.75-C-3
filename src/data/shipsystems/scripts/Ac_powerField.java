package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.util.Misc;
import data.util.AC_ColorData;
import data.util.AC_Util;

import java.awt.*;
import java.util.List;

public class Ac_powerField  extends BaseShipSystemScript {
    public static float ROF_MULT = 0.5f;
    public static float REPAIR_MULT = 0.5f;

    public static Object KEY_SHIP = new Object();
    public static Object KEY_TARGET = new Object();

    protected static float RANGE = 2000f;

    public static Color TEXT_COLOR = AC_ColorData.SOUL_GREEN;
    public static Color JITTER_COLOR = new Color(50, 217, 255,75);
    public static Color JITTER_UNDER_COLOR = new Color(100, 201, 255,155);

    public static class TargetData {
        public ShipAPI ship;
        public ShipAPI target;
        public EveryFrameCombatPlugin targetEffectPlugin;
        public float currROFMult;
        public float currREPAIRMult;
        public float currDISCIPATEMult;
        public float elaspedAfterInState;
        public TargetData(ShipAPI ship, ShipAPI target) {
            this.ship = ship;
            this.target = target;
        }
    }

    @Override
    public boolean isUsable(ShipSystemAPI system, ShipAPI ship) {
        //if (true) return true;
        ShipAPI target = findTarget(ship);
        return target != null && target != ship;
    }

    @Override
    public String getInfoText(ShipSystemAPI system, ShipAPI ship) {
        if (system.isOutOfAmmo()) return null;
        if (system.getState() != ShipSystemAPI.SystemState.IDLE) return null;

        ShipAPI target = findTarget(ship);
        if (target != null && target != ship) {
            return "Volt Scrambler - Ready";
        }
        if ((target == null) && ship.getShipTarget() != null) {
            return "Volt Scrambler - Out of Range";
        }
        return "Volt Scrambler - No Target";
    }

    public static float getMaxRange(ShipAPI ship) {
        return ship.getMutableStats().getSystemRangeBonus().computeEffective(RANGE);
    }

    protected ShipAPI findTarget(ShipAPI ship) {
        float range = getMaxRange(ship);
        boolean player = ship == Global.getCombatEngine().getPlayerShip();
        ShipAPI target = ship.getShipTarget();

        if (ship.getShipAI() != null && ship.getAIFlags().hasFlag(ShipwideAIFlags.AIFlags.TARGET_FOR_SHIP_SYSTEM)){
            target = (ShipAPI) ship.getAIFlags().getCustom(ShipwideAIFlags.AIFlags.TARGET_FOR_SHIP_SYSTEM);
            if (target != null && target.getOriginalOwner() == ship.getOriginalOwner()) target = null;
        }

        if (target != null) {
            float dist = Misc.getDistance(ship.getLocation(), target.getLocation());
            float radSum = ship.getCollisionRadius() + target.getCollisionRadius();
            if (dist > range + radSum) target = null;
        } else {
            if (target == null || target.getOwner() == ship.getOwner()) {
                if (player) {
                    target = Misc.findClosestShipEnemyOf(ship, ship.getMouseTarget(), ShipAPI.HullSize.FRIGATE, range, true);
                } else {
                    Object test = ship.getAIFlags().getCustom(ShipwideAIFlags.AIFlags.MANEUVER_TARGET);
                    if (test instanceof ShipAPI) {
                        target = (ShipAPI) test;
                        float dist = Misc.getDistance(ship.getLocation(), target.getLocation());
                        float radSum = ship.getCollisionRadius() + target.getCollisionRadius();
                        if (dist > range + radSum || target.isFighter()) target = null;
                        if (target != null && target.getOriginalOwner() == ship.getOriginalOwner()) target = null;
                    }
                }
            }
        }

        if (target != null && target.isFighter()) target = null;
        if (target == null) {
            target = Misc.findClosestShipEnemyOf(ship, ship.getLocation(), ShipAPI.HullSize.FRIGATE, range, true);
        }

        return target;
    }

    public StatusData getStatusData(int index, State state, float effectLevel) {
        if (effectLevel > 0) {
            if (index == 0) {
                float rofMult = 1f - (1f - ROF_MULT ) * effectLevel;
                return new StatusData("" + (int)((rofMult - 1f) * 100f) + "% target rate of fire and repair rate", false);
            }
        }
        return null;
    }

    //战术系统效果：对方武器射速/组件修复速度降低50%
    public void apply(MutableShipStatsAPI stats, final String id, State state, float effectLevel) {
        ShipAPI ship = null;
        if (stats.getEntity() instanceof ShipAPI) {
            ship = (ShipAPI) stats.getEntity();
        } else {
            return;
        }

        final String targetDataKey = ship.getId() + "_ac_entropy_target_data";

        Object targetDataObj = Global.getCombatEngine().getCustomData().get(targetDataKey);
        if (state == State.IN && targetDataObj == null) {
            ShipAPI target = findTarget(ship);
            Global.getCombatEngine().getCustomData().put(targetDataKey, new TargetData(ship, target));
            if (target != null) {
                if (target.getFluxTracker().showFloaty() ||
                        ship == Global.getCombatEngine().getPlayerShip() ||
                        target == Global.getCombatEngine().getPlayerShip()) {
                    target.getFluxTracker().showOverloadFloatyIfNeeded("Volt Scrambled!", TEXT_COLOR, 4f, true);
                }
            }
        } else if (state == State.IDLE && targetDataObj != null) {
            Global.getCombatEngine().getCustomData().remove(targetDataKey);
            ((TargetData)targetDataObj).currROFMult = 1f;
            ((TargetData)targetDataObj).currREPAIRMult = 1f;
            targetDataObj = null;
        }
        if (targetDataObj == null || ((TargetData) targetDataObj).target == null) return;

        final TargetData targetData = (TargetData) targetDataObj;
        //targetData.currDamMult = 1f + (DAM_MULT - 1f) * effectLevel;
        targetData.currROFMult = 1f - (1f-ROF_MULT) * effectLevel;
        targetData.currREPAIRMult = 1f + (1f-REPAIR_MULT) * effectLevel;
        //System.out.println("targetData.currDamMult: " + targetData.currDamMult);
        if (targetData.targetEffectPlugin == null) {
            targetData.targetEffectPlugin = new BaseEveryFrameCombatPlugin() {
                @Override
                public void advance(float amount, List<InputEventAPI> events) {
                    if (Global.getCombatEngine().isPaused()) return;
                    if (targetData.target == Global.getCombatEngine().getPlayerShip()) {
                        Global.getCombatEngine().maintainStatusForPlayerShip(KEY_TARGET,
                                targetData.ship.getSystem().getSpecAPI().getIconSpriteName(),
                                targetData.ship.getSystem().getDisplayName(),
                                "" + (int)((targetData.currROFMult - 1f) * 100f) + "% rate of fire / repair rate", true);
                    }

                    if (((targetData.currROFMult >= 1f)&&(targetData.currREPAIRMult <= 1f)) || !targetData.ship.isAlive()) {
                        targetData.target.getMutableStats().getEnergyRoFMult().unmodify(id);
                        targetData.target.getMutableStats().getBallisticRoFMult().unmodify(id);
                        targetData.target.getMutableStats().getMissileRoFMult().unmodify(id);
                        targetData.target.getMutableStats().getCombatEngineRepairTimeMult().unmodify(id);
                        targetData.target.getMutableStats().getCombatWeaponRepairTimeMult().unmodify(id);
                        Global.getCombatEngine().removePlugin(targetData.targetEffectPlugin);
                    } else {
//                        targetData.target.getMutableStats().getHullDamageTakenMult().modifyMult(id, targetData.currDamMult);
//                        targetData.target.getMutableStats().getArmorDamageTakenMult().modifyMult(id, targetData.currDamMult);
//                        targetData.target.getMutableStats().getShieldDamageTakenMult().modifyMult(id, targetData.currDamMult);
//                        targetData.target.getMutableStats().getEmpDamageTakenMult().modifyMult(id, targetData.currDamMult);
                        targetData.target.getMutableStats().getEnergyRoFMult().modifyMult(id, targetData.currROFMult);
                        targetData.target.getMutableStats().getBallisticRoFMult().modifyMult(id, targetData.currROFMult);
                        targetData.target.getMutableStats().getMissileRoFMult().modifyMult(id, targetData.currROFMult);
                        targetData.target.getMutableStats().getCombatWeaponRepairTimeMult().modifyMult(id, targetData.currREPAIRMult);
                        targetData.target.getMutableStats().getCombatEngineRepairTimeMult().modifyMult(id, targetData.currREPAIRMult);
                    }
                }
            };
            Global.getCombatEngine().addPlugin(targetData.targetEffectPlugin);
        }


        if (effectLevel > 0) {
            if (state != State.IN) {
                targetData.elaspedAfterInState += Global.getCombatEngine().getElapsedInLastFrame();
            }
            float shipJitterLevel = 0;
            if (state == State.IN) {
                shipJitterLevel = effectLevel;
            } else {
                float durOut = 0.5f;
                shipJitterLevel = Math.max(0, durOut - targetData.elaspedAfterInState) / durOut;
            }
            float targetJitterLevel = effectLevel;

            float maxRangeBonus = 50f;
            float jitterRangeBonus = shipJitterLevel * maxRangeBonus;

            Color color = JITTER_COLOR;
            if (shipJitterLevel > 0) {
                //ship.setJitterUnder(KEY_SHIP, JITTER_UNDER_COLOR, shipJitterLevel, 21, 0f, 3f + jitterRangeBonus);
                ship.setJitter(KEY_SHIP, color, shipJitterLevel, 4, 0f, 0 + jitterRangeBonus * 1f);
            }

            if (targetJitterLevel > 0) {
                //target.setJitterUnder(KEY_TARGET, JITTER_UNDER_COLOR, targetJitterLevel, 5, 0f, 15f);
                targetData.target.setJitter(KEY_TARGET, color, targetJitterLevel, 3, 0f, 5f);
            }
        }
    }

    public void unapply(MutableShipStatsAPI stats, String id) {

    }
}
