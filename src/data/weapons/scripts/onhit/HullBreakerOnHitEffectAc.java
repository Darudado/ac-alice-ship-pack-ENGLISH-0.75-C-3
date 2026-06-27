package data.weapons.scripts.onhit;

import java.awt.Color;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import data.util.AC_ColorData;
import org.lwjgl.util.vector.Vector2f;
public class HullBreakerOnHitEffectAc implements OnHitEffectPlugin{

    private static float hullDamage = 5;

    public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
        if(target instanceof ShipAPI && !shieldHit) {
            ShipAPI ship = (ShipAPI) target;//锁定舰船目标
            if(ship.isAlive())
            {
                float newHp = target.getHitpoints() - hullDamage;
                //在目标结构值>最大结构值的20%，且大于500时，触发强制扣血效果
                if (target.getHitpoints() > 500 && target.getHitpoints() > target.getMaxHitpoints()*0.2)
                    target.setHitpoints(newHp);
                //否则产生一道10w伤害的能量电弧，足以瞬间摧毁对手，完成斩杀
                else{
                    Vector2f loc = projectile.getLocation();//获取射弹位置
                    if(!ship.isFighter())
                        engine.addFloatingText(loc,"Central Reactor Explosion!!!",35f,AC_ColorData.DUST_RED, target ,1f,1f);//达斯战舰时才会飙字幕。v1和v2控制字幕出现次数，都设置为1f就行
                    engine.spawnEmpArcPierceShields(projectile.getSource(), point, ship, ship, DamageType.ENERGY, 100000, 0, 10000.0F, "tachyon_lance_emp_impact", 10.0F, AC_ColorData.LIGHT_CRIMSON, AC_ColorData.LIGHT_CRIMSON);
                }
            }
        }
    }
}
