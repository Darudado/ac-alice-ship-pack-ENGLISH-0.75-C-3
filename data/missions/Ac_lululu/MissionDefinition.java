package data.missions.Ac_lululu;


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
		api.setFleetTagline(FleetSide.PLAYER, "MissingString:[RemnantFleetName]");
		api.setFleetTagline(FleetSide.ENEMY, "MissingString:[TargetFleetName]");
		
		// These show up as items in the bulleted list under 
		// "Tactical Objectives" on the mission detail screen
		api.addBriefingItem("MissingString:[TaskGuiding]");
		// Set up the player's fleet.

		OfficerLevelupPlugin plugin = (OfficerLevelupPlugin) Global.getSettings().getPlugin("officerLevelUp");

        FactionAPI remnants = Global.getSettings().createBaseFaction(Factions.REMNANTS);
        FleetMemberAPI member;
	    //api.addToFleet(FleetSide.PLAYER, "A_S-F_estucheon_rkt", FleetMemberType.SHIP, "This Is A Test", false);
		member=api.addToFleet(FleetSide.PLAYER, "Ac_firelight_standard", FleetMemberType.SHIP, "MissingString:[RemnantShipName]", true);
		//Set ally officer
		PersonAPI officer = remnants.createRandomPerson(Gender.FEMALE);
        officer.getStats().setSkillLevel(Skills.SYSTEMS_EXPERTISE, 2);
        officer.getStats().setSkillLevel(Skills.TARGET_ANALYSIS, 2); //target_analysis
        officer.getStats().setSkillLevel(Skills.HELMSMANSHIP, 2);
        officer.getStats().setSkillLevel(Skills.COMBAT_ENDURANCE, 2);
        officer.getStats().setSkillLevel(Skills.GUNNERY_IMPLANTS, 2); //gunnery_implants
        officer.getStats().setSkillLevel(Skills.MISSILE_SPECIALIZATION, 2);//missile_specialization
        officer.getStats().setSkillLevel(Skills.POLARIZED_ARMOR, 2); //polarized_armor
        officer.setPersonality(Personalities.RECKLESS);
        officer.getStats().addXP(plugin.getXPForLevel(7));
        officer.getStats().setLevel(7);
        officer.getName().setFirst("MissingString:[CoreName]");
        officer.getName().setLast("MissingString:[CoreType]");
        officer.setPortraitSprite("graphics/portraits/portrait_ai2b.png");
        officer.setFaction(Factions.REMNANTS);
        member.setCaptain(officer);

		// Mark player flagship as essential
		api.defeatOnShipLoss("MissingString:[RemnantShipName]");

		// Set up the enemy fleet.
        FactionAPI hegemony = Global.getSettings().createBaseFaction(Factions.HEGEMONY);
		member=api.addToFleet(FleetSide.ENEMY, "onslaught_xiv_Elite", FleetMemberType.SHIP, "MissingString:[HegemonyShipName]", true);
		member=api.addToFleet(FleetSide.ENEMY, "legion_xiv_Elite", FleetMemberType.SHIP, "MissingString:[HegemonyShipName]", false);
		member=api.addToFleet(FleetSide.ENEMY, "dominator_XIV_Elite", FleetMemberType.SHIP, "MissingString:[HegemonyShipName]", false);
		member=api.addToFleet(FleetSide.ENEMY, "dominator_XIV_Elite", FleetMemberType.SHIP, "MissingString:[HegemonyShipName]", false);
		member=api.addToFleet(FleetSide.ENEMY, "eagle_xiv_Elite", FleetMemberType.SHIP, "MissingString:[HegemonyShipName]", false);
		member=api.addToFleet(FleetSide.ENEMY, "eagle_xiv_Elite", FleetMemberType.SHIP, "MissingString:[HegemonyShipName]", false);
		member=api.addToFleet(FleetSide.ENEMY, "falcon_xiv_Elite", FleetMemberType.SHIP, "MissingString:[HegemonyShipName]", false);
		member=api.addToFleet(FleetSide.ENEMY, "falcon_xiv_Elite", FleetMemberType.SHIP, "MissingString:[HegemonyShipName]", false);
		member=api.addToFleet(FleetSide.ENEMY, "enforcer_XIV_Elite", FleetMemberType.SHIP, "MissingString:[HegemonyShipName]", false);
		member=api.addToFleet(FleetSide.ENEMY, "enforcer_XIV_Elite", FleetMemberType.SHIP, "MissingString:[HegemonyShipName]", false);
        member=api.addToFleet(FleetSide.ENEMY, "enforcer_XIV_Elite", FleetMemberType.SHIP, "MissingString:[HegemonyShipName]", false);
        member=api.addToFleet(FleetSide.ENEMY, "enforcer_XIV_Elite", FleetMemberType.SHIP, "MissingString:[HegemonyShipName]", false);

		// api.defeatOnShipLoss("When Not Provided");
		
		// Set up the map.
		float width = 14000f;
		float height = 14000f;
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
		api.addNebula(minX + width * 0.8f - 1000, minY + height * 0.6f, 2000);


		// Add some planets.  These are defined in data/config/planets.json.
		api.addPlanet(0, 0, 300f, "tundra", 400f, true);
	}
}
