package data.campaign.scripts.submarkets;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.econ.SubmarketAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.submarkets.BaseSubmarketPlugin;
import data.util.AC_Util;

public class Ac_submarketPlugin extends BaseSubmarketPlugin{
    private final RepLevel MIN_STANDING;
    public Ac_submarketPlugin() {
        this.MIN_STANDING = RepLevel.WELCOMING;
    }
    public void initing(SubmarketAPI submarket) {
        super.init(submarket);
    }

    public float getTariff(){ //关税
        switch (this.market.getFaction().getRelationshipLevel(Global.getSector().getFaction("player"))){
            case COOPERATIVE:
                return 0.75F;
            case FRIENDLY:
                return 1F;
            case WELCOMING:
                return 1.25F;
            default:
                return 1.5F;
        }
    }

    private boolean isPlayerOwned() {
        return AC_Util.isPlayerOwned(this.market);
    }

    private boolean isIndependentOwned() {
        return AC_Util.isIndependentOwned(this.market);
    }

    private boolean isPiratesOwned() {
        return AC_Util.isPiratesOwned(this.market);
    }

    private boolean isPLOwned() {
        return AC_Util.isPLOwned(this.market);
    }

    private boolean isPlayerInGoodStandingWith(String factionName) {
        return AC_Util.playerHasAtLeastStandingWith(this.MIN_STANDING, factionName);
    }

    public String getIllegalTransferText(FleetMemberAPI member, SubmarketPlugin.TransferAction action) {
        return "No returns or refunds accepted.";
    }

    public String getIllegalTransferText(CargoStackAPI stack, SubmarketPlugin.TransferAction action) {
        return "No returns or refunds accepted.";
    }

    public boolean isParticipatesInEconomy() {
        return false;
    }

    public boolean isIllegalOnSubmarket(CargoStackAPI stack, SubmarketPlugin.TransferAction action) {
        return action == TransferAction.PLAYER_SELL;
    }

    public boolean isIllegalOnSubmarket(String commodityId, SubmarketPlugin.TransferAction action) {
        return action == TransferAction.PLAYER_SELL;
    }

    public boolean isIllegalOnSubmarket(FleetMemberAPI member, SubmarketPlugin.TransferAction action) {
        return action == TransferAction.PLAYER_SELL;
    }

    public String getTooltipAppendix(CoreUIAPI coreUIAPI){//显示不同情况下的市场准入文字
        if(this.isEnabled(coreUIAPI)){
            return super.getTooltipAppendix(coreUIAPI);
        }
        else {
            RepLevel level = this.market.getFaction().getRelationshipLevel(Global.getSector().getFaction("player"));
            if(!this.getMarket().isPlayerOwned() && !this.isIndependentOwned() && !this.isPiratesOwned() && !this.isPLOwned()){
                return "Due to a change in government, this shop is temporarily closed.";
            } else if (Global.getSector().getPlayerFleet().isTransponderOn()) {
                return "Transponder must be turned off. You wouldn't want COMSEC to find this place, would you?";
            } else {
                if(this.isPiratesOwned()){//市场开放取决于和散户的关系
                    return !this.isPlayerInGoodStandingWith("independent") ? "Requires: " + "independent" + " - " + this.MIN_STANDING.getDisplayName().toLowerCase() : "Unknown";
                }
                if(this.isPLOwned()){
                    return !this.isPlayerInGoodStandingWith("independent") ? "Requires: " + "independent" + " - " + this.MIN_STANDING.getDisplayName().toLowerCase() : "Unknown";
                }
                return !this.isPlayerInGoodStandingWith("independent") ? "Requires: " + this.market.getFaction().getDisplayName() + " - " + this.MIN_STANDING.getDisplayName().toLowerCase() : "Unknown";
            }
        }
    }

    public boolean isEnabled(CoreUIAPI ui) {//控制市场开放
        //市场开放取决于和散户的关系
        if (this.isPlayerOwned()) {
            return this.isPlayerInGoodStandingWith("independent");
        } else {
            return !Global.getSector().getPlayerFleet().isTransponderOn() ? this.isPlayerInGoodStandingWith("independent") : false;
        }
    }

    public void updateCargoPrePlayerInteraction() {//控制市场商品的更新
        this.sinceLastCargoUpdate = 0.0F;
        if (this.okToUpdateShipsAndWeapons()) {
            this.sinceSWUpdate = 0.0F;
            this.getCargo().getMothballedShips().clear();
            this.getCargo().clear();
            float quality = 1.25F;
            FactionDoctrineAPI doctrineOverride = this.submarket.getFaction().getDoctrine().clone();
            doctrineOverride.setShipSize(3);
            this.addShips(this.submarket.getFaction().getId(), 160.0F, 40.0F, 40.0F, 10.0F, 10.0F, 40.0F, quality, 0.0F, FactionAPI.ShipPickMode.PRIORITY_THEN_ALL, doctrineOverride);
            this.addShip("Ac_frigate1_standard",false,0.0F);
            this.addShip("Ac_frigate1_standard",false,0.0F);
            this.addShip("Ac_destroyer1_standard",false,0.0F);
            this.addShip("Ac_destroyer1_standard",false,0.0F);
            this.addShip("Ac_cruiser1_standard",false,0.0F);
            this.addShip("Ac_cruiser2_fortress",false,0.0F);
            this.addShip("Ac_capital1_assault",false,0.0F);

            this.addWeapons(14, 32, 5, this.submarket.getFaction().getId());
            this.addFighters(2, 5, 5, this.submarket.getFaction().getId());
        }

        this.getCargo().sort();
    }
}
