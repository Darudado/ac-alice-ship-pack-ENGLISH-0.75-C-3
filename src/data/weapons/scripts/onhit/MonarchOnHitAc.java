package data.weapons.scripts.onhit;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import com.fs.starfarer.api.loading.DamagingExplosionSpec;
import data.util.AC_ColorData;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

public class MonarchOnHitAc implements OnHitEffectPlugin {
    private static float hullDamage = 525.0F;

    private boolean done = false;


    //33%概率造成额外伤害
    public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
        if (done) return;

        if (target instanceof ShipAPI) {
            ShipAPI ship = (ShipAPI)target;
            if (Math.random() <= (double)0.33F) {
                //engine.spawnEmpArcPierceShields(projectile.getSource(), point, ship, ship, DamageType.ENERGY, dam, emp, 10000.0F, "tachyon_lance_emp_impact", 10.0F, AC_ColorData.N_WHITE, AC_ColorData.N_WHITE);
                DamagingProjectileAPI e = engine.spawnDamagingExplosion(createExplosionSpec(), ship, point);
                Vector2f loc = projectile.getLocation();//获取射弹位置
                e.addDamagedAlready(target);
                done = true;
            }
        }
    }

    public DamagingExplosionSpec createExplosionSpec() {
        DamagingExplosionSpec spec = new DamagingExplosionSpec(
                0.1f, // duration
                60f, // radius
                40f, // coreRadius
                hullDamage, // maxDamage
                hullDamage, // minDamage
                CollisionClass.PROJECTILE_FF, // collisionClass
                CollisionClass.PROJECTILE_FIGHTER, // collisionClassByFighter
                3f, // particleSizeMin
                3f, // particleSizeRange
                0.5f, // particleDuration
                150, // particleCount
                new Color(255,255,255,255), // particleColor
                new Color(100, 208, 255,75)  // explosionColor
        );

        spec.setDamageType(DamageType.FRAGMENTATION);
        spec.setUseDetailedExplosion(false);
        spec.setSoundSetId("explosion_guardian");
        return spec;
    }
}
