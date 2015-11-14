package com.junruyi.entities;

import com.junruyi.dao.DaoSession;
import de.greenrobot.dao.DaoException;

import com.junruyi.dao.EquipMentDao;
import com.junruyi.dao.LocationDao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "LOCATION".
 */
public class Location {

    private Long id;
    private String latitude;
    private String longtitude;
    private String address;
    private java.util.Date losetime;
    private long EquipMentID;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient LocationDao myDao;

    private EquipMent equipMent;
    private Long equipMent__resolvedKey;


    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public Location() {
    }

    public Location(Long id) {
        this.id = id;
    }

    public Location(Long id, String latitude, String longtitude, String address, java.util.Date losetime, long EquipMentID) {
        this.id = id;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.address = address;
        this.losetime = losetime;
        this.EquipMentID = EquipMentID;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getLocationDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public java.util.Date getLosetime() {
        return losetime;
    }

    public void setLosetime(java.util.Date losetime) {
        this.losetime = losetime;
    }

    public long getEquipMentID() {
        return EquipMentID;
    }

    public void setEquipMentID(long EquipMentID) {
        this.EquipMentID = EquipMentID;
    }

    /** To-one relationship, resolved on first access. */
    public EquipMent getEquipMent() {
        long __key = this.EquipMentID;
        if (equipMent__resolvedKey == null || !equipMent__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            EquipMentDao targetDao = daoSession.getEquipMentDao();
            EquipMent equipMentNew = targetDao.load(__key);
            synchronized (this) {
                equipMent = equipMentNew;
            	equipMent__resolvedKey = __key;
            }
        }
        return equipMent;
    }

    public void setEquipMent(EquipMent equipMent) {
        if (equipMent == null) {
            throw new DaoException("To-one property 'EquipMentID' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.equipMent = equipMent;
            EquipMentID = equipMent.getId();
            equipMent__resolvedKey = EquipMentID;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
