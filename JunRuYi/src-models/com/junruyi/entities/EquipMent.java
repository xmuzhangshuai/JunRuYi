package com.junruyi.entities;

import java.util.List;
import com.junruyi.dao.DaoSession;
import de.greenrobot.dao.DaoException;

import com.junruyi.dao.EquipMentDao;
import com.junruyi.dao.LocationDao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "EQUIP_MENT".
 */
public class EquipMent {

    private Long id;
    private String equipMentAddress;
    private String equipMentName;
    private Integer equipMentLogo;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient EquipMentDao myDao;

    private List<Location> locationList;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public EquipMent() {
    }

    public EquipMent(Long id) {
        this.id = id;
    }

    public EquipMent(Long id, String equipMentAddress, String equipMentName, Integer equipMentLogo) {
        this.id = id;
        this.equipMentAddress = equipMentAddress;
        this.equipMentName = equipMentName;
        this.equipMentLogo = equipMentLogo;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getEquipMentDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEquipMentAddress() {
        return equipMentAddress;
    }

    public void setEquipMentAddress(String equipMentAddress) {
        this.equipMentAddress = equipMentAddress;
    }

    public String getEquipMentName() {
        return equipMentName;
    }

    public void setEquipMentName(String equipMentName) {
        this.equipMentName = equipMentName;
    }

    public Integer getEquipMentLogo() {
        return equipMentLogo;
    }

    public void setEquipMentLogo(Integer equipMentLogo) {
        this.equipMentLogo = equipMentLogo;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<Location> getLocationList() {
        if (locationList == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            LocationDao targetDao = daoSession.getLocationDao();
            List<Location> locationListNew = targetDao._queryEquipMent_LocationList(id);
            synchronized (this) {
                if(locationList == null) {
                    locationList = locationListNew;
                }
            }
        }
        return locationList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetLocationList() {
        locationList = null;
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
