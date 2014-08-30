package com.firefueled.elitetrader.model;

import java.util.List;

/**
 * Created by Pablo on 24/08/2014.
 */
public class Station extends Model<Station> {

    public Station(List<TradeItem> tradeItems, String stationName, String stationSystem) {
        super(Station.class);
        this.tradeItems = tradeItems;
        this.stationName = stationName;
        this.stationSystem = stationSystem;
    }

    public Station() { super(Station.class); }

    public String stationName;
    public String stationSystem;
    public List<TradeItem> tradeItems;
}
