package data.missions.Ac_freebird;


import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;


public class MissionDefinition implements MissionDefinitionPlugin {

  @Override
	public void defineMission(MissionDefinitionAPI api) {

		// Set up the fleets
		api.initFleet(FleetSide.PLAYER, "MSS", FleetGoal.ATTACK, false);
		api.initFleet(FleetSide.ENEMY, "TTDS", FleetGoal.ATTACK, true);

		// Set a blurb for each fleet
		api.setFleetTagline(FleetSide.PLAYER, "Wales的巨鹰和他的从者们");
		api.setFleetTagline(FleetSide.ENEMY, "把守跳跃点的余辉自动化舰队");
		
		// These show up as items in the bulleted list under 
		// "Tactical Objectives" on the mission detail screen
		api.addBriefingItem("MSS-Veorfolnir的内部是纳米锻炉，它不能被摧毁");
		api.addBriefingItem("你的旗舰是独一无二的突破手");
		api.addBriefingItem("合理使用监视者-级集群来牵制敌方高危单位，以便逐个击破");
		api.addBriefingItem("保护舰船尾部，敌方的新型正航是一个极大的威胁");
		// Set up the player's fleet.

	    //api.addToFleet(FleetSide.PLAYER, "A_S-F_estucheon_rkt", FleetMemberType.SHIP, "This Is A Test", false);
		api.addToFleet(FleetSide.PLAYER, "Ac_Commander_assault", FleetMemberType.SHIP, "MSS Veorfolnir", true);
        api.addToFleet(FleetSide.PLAYER, "Ac_Sower_support", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.PLAYER, "heron_Strike", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.PLAYER, "heron_Strike", FleetMemberType.SHIP, false);
        api.addToFleet(FleetSide.PLAYER, "monitor_Escort", FleetMemberType.SHIP, false);
        api.addToFleet(FleetSide.PLAYER, "monitor_Escort", FleetMemberType.SHIP, false);
        api.addToFleet(FleetSide.PLAYER, "monitor_Escort", FleetMemberType.SHIP, false);
        api.addToFleet(FleetSide.PLAYER, "monitor_Escort", FleetMemberType.SHIP, false);
        api.addToFleet(FleetSide.PLAYER, "monitor_Escort", FleetMemberType.SHIP, false);

		// Mark player flagship as essential
		api.defeatOnShipLoss("MSS Veorfolnir");

		// Set up the enemy fleet.

		api.addToFleet(FleetSide.ENEMY, "Ac_smolder_assault", FleetMemberType.SHIP, "TTDS Inferno", true);
		api.addToFleet(FleetSide.ENEMY, "Ac_pulse_blaster", FleetMemberType.SHIP, "TTDS Ratatoskr", false);
		api.addToFleet(FleetSide.ENEMY, "Ac_pulse_blaster", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "Ac_afterglow_support", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "Ac_afterglow_support", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "scintilla_Strike", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "scintilla_Strike", FleetMemberType.SHIP, false);


		// api.defeatOnShipLoss("When Not Provided");
		
		// Set up the map.
		float width = 12000f;
		float height = 12000f;
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
		api.addPlanet(0, 0, 200f, "toxic", 250f, true);
	}

}
