package data.campaign.scripts;


import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.PluginPick;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.CampaignPlugin.PickPriority;
import com.fs.starfarer.api.campaign.econ.MarketAPI;

import data.util.AC_Util;
import org.apache.log4j.Logger;


public class Ac_modPlugin extends BaseModPlugin{
    public static Logger log = Global.getLogger(Ac_modPlugin.class);

    private boolean needsAcSubmarket(MarketAPI market) {//控制市场刷新范围
        //势力需求：散户，海盗，英联
        //市场规模：>=3
        if ((AC_Util.isIndependentOwned(market) || AC_Util.isPiratesOwned(market) || AC_Util.isPLOwned(market)) && market.getSize() >= 3) {
            //设施需求：重工业设施
            if (!market.hasIndustry("heavyindustry") && !market.hasIndustry("orbitalworks")) {
                return false;
            } else {
                return !market.hasSubmarket("Ac");
            }
        } else {
            return false;
        }
    }

    private void ensureInitialGameState() {
        if (!Global.getSector().getMemoryWithoutUpdate().getBoolean("$AcInit")) {
            for(MarketAPI market : Global.getSector().getEconomy().getMarketsCopy()) {
                if (this.needsAcSubmarket(market)) {
                    market.addSubmarket("Ac");
                    log.info("Adding ac Submarket to: " + market.getPrimaryEntity().getFullName() + " in " + market.getPrimaryEntity().getStarSystem().getBaseName());
                }
            }

            Global.getSector().getMemoryWithoutUpdate().set("$AcInit", true);
        }

        for(MarketAPI market : Global.getSector().getEconomy().getMarketsCopy()) {
            if (market.getName().contains("Prism Freeport") && !market.hasSubmarket("Ac")) {//棱镜自由港也会有市场
                market.addSubmarket("Ac");
                log.info("Adding ac Submarket to: " + market.getPrimaryEntity().getFullName() + " in " + market.getPrimaryEntity().getStarSystem().getBaseName());
            }


        }

    }

    public void onNewGameAfterEconomyLoad() {
        this.ensureInitialGameState();
    }
    public void onGameLoad(boolean newGame) {
        this.ensureInitialGameState();
    }
}
