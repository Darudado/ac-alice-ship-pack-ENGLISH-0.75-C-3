//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package data.weapons.scripts.onhit;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import data.util.AC_ColorData;
import org.lwjgl.util.vector.Vector2f;

public class ThundersuiteOnHitEffectAc implements OnHitEffectPlugin {
    public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
        if (target instanceof ShipAPI && !shieldHit) {
            ShipAPI ship = (ShipAPI)target;
            if (Math.random() <= (double)0.5F) {
                float emp = projectile.getEmpAmount();
                float dam = projectile.getDamageAmount();
                engine.spawnEmpArcPierceShields(projectile.getSource(), point, ship, ship, DamageType.ENERGY, dam, emp, 10000.0F, "tachyon_lance_emp_impact", 10.0F, AC_ColorData.N_WHITE, AC_ColorData.N_WHITE);
            }
        }

    }
}
