package data.missions.Ac_demon_blade;


import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.characters.FullName.Gender;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.ids.Personalities;
import com.fs.starfarer.api.impl.campaign.ids.Skills;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;
import com.fs.starfarer.api.plugins.OfficerLevelupPlugin;
import java.util.List;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;


public class MissionDefinition implements MissionDefinitionPlugin {

  @Override
	public void defineMission(MissionDefinitionAPI api) {

		// Set up the fleets
		api.initFleet(FleetSide.PLAYER, "ISS", FleetGoal.ATTACK, false);
		api.initFleet(FleetSide.ENEMY, "KSS", FleetGoal.ATTACK, true);

		// Set a blurb for each fleet
		api.setFleetTagline(FleetSide.PLAYER, "幻影Jane和他那不明真相的勘探队");
		api.setFleetTagline(FleetSide.ENEMY, "心急火燎的Kanta亲卫追兵");
		
		// These show up as items in the bulleted list under 
		// "Tactical Objectives" on the mission detail screen
		api.addBriefingItem("TTS Soma和运输数据核心的ISS Booster不能被摧毁");
		api.addBriefingItem("你的驾驶技术比常规的舰长更高");
		api.addBriefingItem("ISS Booster是破局的关键");
		api.addBriefingItem("小心敌方舰队里那个畸形的傻大个");
		// Set up the player's fleet.

		OfficerLevelupPlugin plugin = (OfficerLevelupPlugin) Global.getSettings().getPlugin("officerLevelUp");

        FactionAPI tritachyon = Global.getSettings().createBaseFaction(Factions.TRITACHYON);
        FleetMemberAPI member;
	    //api.addToFleet(FleetSide.PLAYER, "A_S-F_estucheon_rkt", FleetMemberType.SHIP, "This Is A Test", false);
	    member=api.addToFleet(FleetSide.PLAYER, "Ac_irises_standard", FleetMemberType.SHIP,"ISS Booster", false);
		member=api.addToFleet(FleetSide.PLAYER, "Ac_fulcrum_assault", FleetMemberType.SHIP, false);
		member=api.addToFleet(FleetSide.PLAYER, "Ac_fulcrum_assault", FleetMemberType.SHIP, false);
		member=api.addToFleet(FleetSide.PLAYER, "Ac_Emolly_assault", FleetMemberType.SHIP, "TTS Soma", true);
		//Set ally officer
		PersonAPI officer = tritachyon.createRandomPerson(Gender.FEMALE);
        officer.getStats().setSkillLevel(Skills.SYSTEMS_EXPERTISE, 2);
        officer.getStats().setSkillLevel(Skills.TARGET_ANALYSIS, 2); //target_analysis
        officer.getStats().setSkillLevel(Skills.HELMSMANSHIP, 2);
        officer.getStats().setSkillLevel(Skills.COMBAT_ENDURANCE, 2);
        officer.getStats().setSkillLevel(Skills.GUNNERY_IMPLANTS, 2); //gunnery_implants
        officer.getStats().addXP(plugin.getXPForLevel(5));
        officer.getStats().setLevel(5);
        officer.getName().setFirst("Jane");
        officer.getName().setLast("Gadra");
        officer.setPortraitSprite("graphics/portraits/portrait_corporate06.png");
        officer.setFaction(Factions.TRITACHYON);
        member.setCaptain(officer);
		member=api.addToFleet(FleetSide.PLAYER, "Ac_Alcedo_assault", FleetMemberType.SHIP, false);
		member=api.addToFleet(FleetSide.PLAYER, "Ac_Alcedo_assault", FleetMemberType.SHIP, false);
		member=api.addToFleet(FleetSide.PLAYER, "omen_PD", FleetMemberType.SHIP, false);
		member=api.addToFleet(FleetSide.PLAYER, "omen_PD", FleetMemberType.SHIP, false);


		// Mark player flagship as essential
		api.defeatOnShipLoss("TTS Soma");
        api.defeatOnShipLoss("ISS Booster");
		// Set up the enemy fleet.
        FactionAPI pirates = Global.getSettings().createBaseFaction(Factions.PIRATES);
		member=api.addToFleet(FleetSide.ENEMY, "Ac_Ghoul_standard", FleetMemberType.SHIP, "Kanta's Spear", true);
		member=api.addToFleet(FleetSide.ENEMY, "eradicator_pirates_Overdriven", FleetMemberType.SHIP, false);
		member=api.addToFleet(FleetSide.ENEMY, "eradicator_pirates_Overdriven", FleetMemberType.SHIP, false);
		member=api.addToFleet(FleetSide.ENEMY, "Ac_judge_support", FleetMemberType.SHIP, false);
		member=api.addToFleet(FleetSide.ENEMY, "Ac_judge_support", FleetMemberType.SHIP, false);
		member=api.addToFleet(FleetSide.ENEMY, "Ac_Attker_X_assault", FleetMemberType.SHIP, false);
		member=api.addToFleet(FleetSide.ENEMY, "Ac_Attker_X_assault", FleetMemberType.SHIP, false);
        member=api.addToFleet(FleetSide.ENEMY, "Ac_Bulldog_standard", FleetMemberType.SHIP, false);
        member=api.addToFleet(FleetSide.ENEMY, "Ac_Bulldog_standard", FleetMemberType.SHIP, false);
        member=api.addToFleet(FleetSide.ENEMY, "Ac_Bulldog_standard", FleetMemberType.SHIP, false);
		// api.defeatOnShipLoss("When Not Provided");
		
		// Set up the map.
		float width = 12000f;
		float height = 18000f;
		api.initMap((float)-width/2f, (float)width/2f, (float)-height/2f, (float)height/2f);

		float minX = -width/2;
		float minY = -height/2;

		for (int i = 0; i < 15; i++) {
			float x = (float) Math.random() * width - width/2;
			float y = (float) Math.random() * height - height/2;
			float radius = 100f + (float) Math.random() * 900f;
			api.addNebula(x, y, radius);
		}

		api.addNebula(minX + width * 0.8f - 1000, minY + height * 0.4f, 2000);
		api.addNebula(minX + width * 0.8f - 1000, minY + height * 0.5f, 2000);
		api.addNebula(minX + width * 0.8f - 1000, minY + height * 0.6f, 2000);

		// Add an asteroid field
		api.addAsteroidField(minX + width * 0.3f, minY, 90, 3000f, 20f, 70f, 50);

		// Add some planets.  These are defined in data/config/planets.json.
		api.addPlanet(0, 0, 400f, "black_hole", 500f, true);
	}

}
