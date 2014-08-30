package com.firefueled.elitetrader.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.firefueled.elitetrader.R;
import com.firefueled.elitetrader.db.MarketOpenHelper;
import com.firefueled.elitetrader.model.Station;
import com.firefueled.elitetrader.model.TradeItem;
import com.firefueled.elitetrader.util.Constants;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pablo on 24/08/2014.
 */
public class StationExpandableListAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private final LayoutInflater inflater;
    private DB db;
    public LongSparseArray<List<TradeItem>> tradeItems;
    public List<Station> stations;

    public StationExpandableListAdapter (Context context) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        fillData();
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        Station group = (Station) getGroup(groupPosition);

        String stationName = group.stationName;
        String stationSystem = group.stationSystem;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.entry_main_group, parent, false);
        }

        ((TextView) convertView.findViewById(R.id.main_list_group_stationName)).setText(stationName);
        ((TextView) convertView.findViewById(R.id.main_list_group_stationSystem)).setText(stationSystem);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        TradeItem child = (TradeItem) getChild(groupPosition, childPosition);

        String itemName = child.itemName;
        String itemBuy = String.valueOf(child.buyPrice);
        String itemSell = String.valueOf(child.sellPrice);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.entry_main_child, parent, false);
        }

        ((TextView) convertView.findViewById(R.id.main_list_child_itemName)).setText(itemName);
        ((TextView) convertView.findViewById(R.id.main_list_child_itemBuy)).setText(itemBuy);
        ((TextView) convertView.findViewById(R.id.main_list_child_itemSell)).setText(itemSell);

        return convertView;
    }

    @Override
    public int getGroupCount() {
        return stations.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return tradeItems.get(stations.get(groupPosition).id).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return stations.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return tradeItems.get(stations.get(groupPosition).id).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return (groupPosition + 1) * (childPosition + 1);
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private void fillData() {

        // get reference for database
        try {
            this.db = DBFactory.open(context, Constants.DB_NAME);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }

        // fill stations e trade items arrays
        stations = Constants.DB.all(Station.class);

        if (tradeItems == null) {
            tradeItems = new LongSparseArray<List<TradeItem>>();
        }

        for (Station station : stations) {
            tradeItems.put(station.id, station.tradeItems != null ? station.tradeItems : new ArrayList<TradeItem>());
        }
    }

    public void addStation(Station station) {
        stations.add(station);
        tradeItems.put(station.id, station.tradeItems);
        station.save();
        notifyDataSetChanged();
    }

    public void addTradeItem(Station station, TradeItem tradeItem) {
        stations.get(stations.indexOf(station)).tradeItems.add(tradeItem);
    }
}
