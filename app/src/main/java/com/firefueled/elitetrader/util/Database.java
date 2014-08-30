package com.firefueled.elitetrader.util;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.ArrayUtils;

import android.content.Context;
import android.util.Log;

import com.firefueled.elitetrader.model.BaseModel;
import com.firefueled.elitetrader.model.Model;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

public class Database {
    private DB db;
    private SecureRandom secureRandom;

    public Database(Context ctx) {
        try {
            db = DBFactory.open(ctx, Constants.DB_NAME);
            secureRandom = new SecureRandom();

        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }
    public long getObjectId(){
        return secureRandom.nextLong();
    }

    public <T> List<T> all(Class<T> type){
        String rawData;
        String indexKey = getIndexKey(type);
        String[] keys;
        ArrayList<T> result = new ArrayList<T>();

        try {
            if(!db.exists(indexKey)) return result;

            keys = db.getArray(indexKey, String.class);
            for (String key : keys) {
                rawData = db.get(key);
                result.add(fromJSON(type, rawData));
            }
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
        return result;
    }

    public <T> T find(Class<T> type, long id) {
        return this.find(type, String.valueOf(id));
    }

    public <T> T find(Class<T> type, String id) {
        String key = getKey(type, id);

        try {
            if(!db.exists(key)) return null;

            String rawData = db.get(key);
            return fromJSON(type, rawData);

        } catch (SnappydbException e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> T save(Class<T> type, T model){
        String key = getKey(type, model);

        try {
            String rawData = toJSON(type, model);

            db.put(key, rawData);
            appendIntoIndex(type, key);

            return model;

        } catch (SnappydbException e) {
            Log.d("WEIRD", "ER");
            e.printStackTrace();
            return null;
        }
    }

    public <T> List<T> where(Class<T> type, Predicate<T> predicate){

        Collection<T> all = all(type);

        return (List<T>) CollectionUtils.select(all, predicate);
    }

    public <T> T findWhere(Class<T> type, Predicate<T> predicate){

        List<T> all = where(type, predicate);

        if (all.size() > 0)
            return all.get(0);
        else
            return null;
    }

    public <T> T destroy(Class<T> type, T model){
        String key = getKey(type, model);

        try {
            if(!db.exists(key)) return null;

            removeFromIndex(type, key);
            db.del(key);

            return model;

        } catch (SnappydbException e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> Boolean destroyAll(Class<T> type){
        List<T> items = all(type);
        for (T item : items) {
            destroy(type, item);
        }
        return true;
    }

    public <T> String toJSON(Class<T> type, T model){
        return Constants.GSON.toJson(model);
    }

    public <T> T fromJSON(Class<T> type, String rawData) {
        return Constants.GSON.fromJson(rawData, type);
    }

    private <T> String getTypeName(Class<T> type){
        return type.getSimpleName().toLowerCase(Locale.getDefault());
    }

    private <T> String getKey(Class<T> type, Object t){
        return getKey(type, ((Model) t).id);
    }

    private <T> String getKey(Class<T> type, long t){
        return getKey(type, String.valueOf(t));
    }

    private <T> String getKey(Class<T> type, String t){
        return getTypeName(type) + "-" + t;
    }

    private <T> String getIndexKey(Class<T> type){
        return getTypeName(type) + "-keys";
    }

    private <T> void appendIntoIndex(Class<T> type, String key) throws SnappydbException {
        String indexKey = getIndexKey(type);

        String[] keys = db.exists(indexKey) ? db.getArray(indexKey, String.class) : new String[0];
        db.put(indexKey, ArrayUtils.add(keys, key));
    }

    private <T> void removeFromIndex(Class<T> type, String key) throws SnappydbException {
        String indexKey = getIndexKey(type);

        String[] keys = db.exists(indexKey) ? db.getArray(indexKey, String.class) : new String[0];
        db.put(indexKey, ArrayUtils.removeElement(keys, key));
    }
}
