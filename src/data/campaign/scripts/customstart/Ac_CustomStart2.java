package data.campaign.scripts.customstart;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.Script;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.rules.MemKeys;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.characters.CharacterCreationData;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.DModManager;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import com.fs.starfarer.api.impl.campaign.rulecmd.FireBest;
import com.fs.starfarer.api.impl.campaign.rulecmd.newgame.NGCAddStartingShipsByFleetType;
import com.fs.starfarer.api.loading.HullModSpecAPI;
import com.fs.starfarer.api.loading.VariantSource;
import com.fs.starfarer.api.util.Misc;
import exerelin.campaign.PlayerFactionStore;
import exerelin.campaign.customstart.CustomStart;
import exerelin.utilities.NexUtils;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Ac_CustomStart2 extends CustomStart {

    protected List<String> ships = new ArrayList<>(Arrays.asList(new String[]{
            "Ac_Dreamer_xiv_elite"
    }));

    private static final Color HIGHLIGHT_COLOR = Global.getSettings().getColor("buttonShortcut");

    @Override
    public void execute(InteractionDialogAPI dialog, Map<String, MemoryAPI> memoryMap) {
        PlayerFactionStore.setPlayerFactionIdNGC(Factions.HEGEMONY);

        CharacterCreationData data = (CharacterCreationData) memoryMap.get(MemKeys.LOCAL).get("$characterData");

        NGCAddStartingShipsByFleetType.generateFleetFromVariantIds(dialog, data, null, ships);
        NGCAddStartingShipsByFleetType.addStartingDModScript(memoryMap.get(MemKeys.LOCAL));

        data.addScript(new Script() {
            @Override
            public void run() {
                CampaignFleetAPI fleet = Global.getSector().getPlayerFleet();
                Random random = new Random(NexUtils.getStartingSeed());

                for (FleetMemberAPI member : fleet.getFleetData().getMembersListCopy()) {
                    ShipVariantAPI v = member.getVariant().clone();
                    v.setSource(VariantSource.REFIT);
                    v.setHullVariantId(Misc.genUID());
                    member.setVariant(v, false, false);

                    Global.getSector().addScript(new EveryFrameScript() {

                        private boolean done = false;

                        @Override
                        public boolean isDone() {
                            return done;
                        }

                        @Override
                        public boolean runWhilePaused() {
                            return true;
                        }

                        @Override
                        public void advance(float amount) {
                            CampaignFleetAPI fleet = Global.getSector().getPlayerFleet();
                            for (FleetMemberAPI member : fleet.getFleetData().getMembersListCopy()) {
                                if (member.getShipName().contentEquals("HSS Flame Sparrow")) {
                                    done = true;
                                    break;
                                }

                                member.setShipName("HSS Flame Sparrow");
                            }
                        }
                    });
                }

                fleet.getFleetData().setSyncNeeded();
                fleet.getFleetData().syncIfNeeded();
            }
        });


        FireBest.fire(null, dialog, memoryMap, "ExerelinNGCStep4");
    }
}
