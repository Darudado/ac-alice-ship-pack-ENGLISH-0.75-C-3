package data.missions.Ac_test;

import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;

public class MissionDefinition implements MissionDefinitionPlugin {

  @Override
	public void defineMission(MissionDefinitionAPI api) {

		// Set up the fleets
		api.initFleet(FleetSide.PLAYER, "ACS", FleetGoal.ATTACK, false);
		api.initFleet(FleetSide.ENEMY, "ISS", FleetGoal.ATTACK, true);

		// Set a blurb for each fleet
		api.setFleetTagline(FleetSide.PLAYER, "试用舰船");
		api.setFleetTagline(FleetSide.ENEMY, "测试舰船");
		
		// These show up as items in the bulleted list under 
		// "Tactical Objectives" on the mission detail screen
		api.addBriefingItem("-自由测试AC船包的普通舰船，测试舰船按照装配文件首字母排序");
		
		// Set up the player's fleet.

	    //api.addToFleet(FleetSide.PLAYER, "A_S-F_estucheon_rkt", FleetMemberType.SHIP, "This Is A Test", false);
		api.addToFleet(FleetSide.PLAYER, "Ac_Adriatic_standard", FleetMemberType.SHIP, "Trial", true);
		api.addToFleet(FleetSide.PLAYER, "Ac_Adultdog_standard", FleetMemberType.SHIP, "Trial", false);
		api.addToFleet(FleetSide.PLAYER, "Ac_Alcedo_assault", FleetMemberType.SHIP, "Trial", false);
		api.addToFleet(FleetSide.PLAYER, "Ac_Alice_standard", FleetMemberType.SHIP, "Trial", false); //sup
		api.addToFleet(FleetSide.PLAYER, "Ac_Aphrodite_standard", FleetMemberType.SHIP, "Trial", false); //sup
		api.addToFleet(FleetSide.PLAYER, "Ac_Astle_assault", FleetMemberType.SHIP, "Trial", false);
		api.addToFleet(FleetSide.PLAYER, "Ac_Attker_assault", FleetMemberType.SHIP, "Trial", false); //stk
		api.addToFleet(FleetSide.PLAYER, "Ac_Attker_S_standard", FleetMemberType.SHIP, "Trial", false); //sup2

		api.addToFleet(FleetSide.PLAYER, "Ac_Attker_X_assault", FleetMemberType.SHIP, "Trial", false);
		api.addToFleet(FleetSide.PLAYER, "Ac_Bulldog_assault", FleetMemberType.SHIP, "Trial", false);
		api.addToFleet(FleetSide.PLAYER, "Ac_colossus4_standard", FleetMemberType.SHIP, "Trial", false);
		api.addToFleet(FleetSide.PLAYER, "Ac_Commander_assault", FleetMemberType.SHIP, "Trial", true);

	  	api.addToFleet(FleetSide.PLAYER, "Ac_conquestG1_standard", FleetMemberType.SHIP, "Trial", false); //atk


	  	api.addToFleet(FleetSide.PLAYER, "Ac_Crasher_alcasher", FleetMemberType.SHIP, "Trial", false);
		api.addToFleet(FleetSide.PLAYER, "Ac_Demolisher_assault", FleetMemberType.SHIP, "Trial", false);
		api.addToFleet(FleetSide.PLAYER, "Ac_Deucalion_standard", FleetMemberType.SHIP, "Trial", false);
		
		api.addToFleet(FleetSide.PLAYER, "Ac_Echtena_assault", FleetMemberType.SHIP, "Trial", false);
		api.addToFleet(FleetSide.PLAYER, "Ac_Emolly_assault", FleetMemberType.SHIP, "Trial", false);
		api.addToFleet(FleetSide.PLAYER, "Ac_fulcrum_assault", FleetMemberType.SHIP, "Trial", false); //atk
		api.addToFleet(FleetSide.PLAYER, "Ac_Ghoul_standard", FleetMemberType.SHIP, "Trial", false); //atk
		api.addToFleet(FleetSide.PLAYER, "Ac_Harasser_assault", FleetMemberType.SHIP, "Trial", false); //atk
		api.addToFleet(FleetSide.PLAYER, "Ac_henchman_assault", FleetMemberType.SHIP, "Trial", false); //atk
		api.addToFleet(FleetSide.PLAYER, "Ac_interdiction_assault", FleetMemberType.SHIP, "Trial", false); //atk
		api.addToFleet(FleetSide.PLAYER, "Ac_irises_standard", FleetMemberType.SHIP, "Trial", false); //atk
		api.addToFleet(FleetSide.PLAYER, "Ac_irises_oc_standard", FleetMemberType.SHIP, "Trial", false); //atk
		api.addToFleet(FleetSide.PLAYER, "Ac_irises_os_standard", FleetMemberType.SHIP, "Trial", false); //atk
		api.addToFleet(FleetSide.PLAYER, "Ac_judge_standard", FleetMemberType.SHIP, "Trial", false); //atk
		api.addToFleet(FleetSide.PLAYER, "Ac_Larks_standard", FleetMemberType.SHIP, "Trial", false); //atk
		api.addToFleet(FleetSide.PLAYER, "Ac_liquid_iron_standard", FleetMemberType.SHIP, "Trial", false); //atk
	    api.addToFleet(FleetSide.PLAYER, "Ac_lowtechAurora_standard", FleetMemberType.SHIP, "Trial", false); //atk

	    api.addToFleet(FleetSide.PLAYER, "Ac_LuddicMonster_standard", FleetMemberType.SHIP, "Trial", false); //atk

	  	api.addToFleet(FleetSide.PLAYER, "Ac_Messiah_assalut", FleetMemberType.SHIP, "Trial", false); //atk
		api.addToFleet(FleetSide.PLAYER, "Ac_Minerva_standard", FleetMemberType.SHIP, "Trial", false); //atk
		api.addToFleet(FleetSide.PLAYER, "Ac_Mudrock_assault", FleetMemberType.SHIP, "Trial", false); //atk
		api.addToFleet(FleetSide.PLAYER, "Ac_murderers_assault", FleetMemberType.SHIP, "Trial", false); //atk
		api.addToFleet(FleetSide.PLAYER, "Ac_Mystia_support", FleetMemberType.SHIP, "Trial", false); //atk
		api.addToFleet(FleetSide.PLAYER, "Ac_nightingale_assault", FleetMemberType.SHIP, "Trial", false); //atk
		api.addToFleet(FleetSide.PLAYER, "Ac_Nipponia_standard", FleetMemberType.SHIP, "Trial", false); //atk
		api.addToFleet(FleetSide.PLAYER, "Ac_Nipponia_oc_standard", FleetMemberType.SHIP, "Trial", false); //atk
		api.addToFleet(FleetSide.PLAYER, "Ac_prometheus_3_bomber", FleetMemberType.SHIP, "Trial", false); //atk

	  	api.addToFleet(FleetSide.PLAYER, "Ac_promethus_cv_standard", FleetMemberType.SHIP, "Trial", false); //atk

	  	api.addToFleet(FleetSide.PLAYER, "Ac_Punch_assault", FleetMemberType.SHIP, "Trial", false); //atk
		api.addToFleet(FleetSide.PLAYER, "Ac_puppy_standard", FleetMemberType.SHIP, "Trial", false); //atk
		api.addToFleet(FleetSide.PLAYER, "Ac_Redeemer_assault", FleetMemberType.SHIP, "Trial", false); //atk
		api.addToFleet(FleetSide.PLAYER, "Ac_Robber_assault", FleetMemberType.SHIP, "Trial", false); //atk
		api.addToFleet(FleetSide.PLAYER, "Ac_Rosebush_standard", FleetMemberType.SHIP, "Trial", false); //atk
		api.addToFleet(FleetSide.PLAYER, "Ac_Sower_bomber", FleetMemberType.SHIP, "Trial", false); //atk

	  	api.addToFleet(FleetSide.PLAYER, "Ac_stone_standard", FleetMemberType.SHIP, "Trial", false); //atk

	  	api.addToFleet(FleetSide.PLAYER, "Ac_tracker_standard", FleetMemberType.SHIP, "Trial", false); //atk
		api.addToFleet(FleetSide.PLAYER, "Ac_whitebird_C_bomber", FleetMemberType.SHIP, "Trial", false); //atk
		api.addToFleet(FleetSide.PLAYER, "Ac_whitebird_S_assault", FleetMemberType.SHIP, "Trial", false); //atk
		api.addToFleet(FleetSide.PLAYER, "Ac_Ziz_assault", FleetMemberType.SHIP, "Trial", false); //atk


	  // Set up the enemy fleet.
		api.addToFleet(FleetSide.ENEMY, "doom_Strike", FleetMemberType.SHIP, "Search Dialog", false);

		// api.defeatOnShipLoss("When Not Provided");
		
		// Set up the map.
		float width = 18000f;
		float height = 16000f;
		api.initMap((float)-width/2f, (float)width/2f, (float)-height/2f, (float)height/2f);
		
		float minX = -width/2;
		float minY = -height/2;
		
		// Add an asteroid field
		api.addAsteroidField(minX, minY + height / 2, 0, 10000f,
							 20f, 75f, 120);
		
	}

}
