package com.firefueled.elitetrader.model;

import com.firefueled.elitetrader.util.Constants;
import com.firefueled.elitetrader.util.Database;

import java.util.List;

import org.apache.commons.collections4.Predicate;

public abstract class Model<T> {
    private static Database __db = null;
    private transient Class<T> __type;

    public long id;

    public Model(Class<T> type){
        if(__db == null){ __db = Constants.DB; }
        this.id = (int) __db.getObjectId();
        this.__type = type;
    }

    public List<T> all(){
        return __db.all(__type);
    }

    public T find(long id) {
        return __db.find(__type, id);
    }

    public T find(String id) {
        return __db.find(__type, id);
    }

    @SuppressWarnings("unchecked")
    public T save(){
        return __db.save(__type, (T) this);
    }

    public List<T> where(Predicate<T> predicate){
        return __db.where(__type, predicate);
    }

    public T findwhere(Predicate<T> predicate){
        return __db.findWhere(__type, predicate);
    }

    @SuppressWarnings("unchecked")
    public T destroy(){
        return __db.destroy(__type, (T) this);
    }

    public Boolean destroyAll(){
        return __db.destroyAll(__type);
    }

    public String toJSON(T model){
        return __db.toJSON(__type, model);
    }

    public T fromJSON(String rawData) {
        return __db.fromJSON(__type, rawData);
    }
}