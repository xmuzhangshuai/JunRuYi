package com.junruyi.dao;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import com.junruyi.entities.EquipMent;

import com.junruyi.entities.Location;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "LOCATION".
*/
public class LocationDao extends AbstractDao<Location, Long> {

    public static final String TABLENAME = "LOCATION";

    /**
     * Properties of entity Location.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Latitude = new Property(1, String.class, "latitude", false, "LATITUDE");
        public final static Property Longtitude = new Property(2, String.class, "longtitude", false, "LONGTITUDE");
        public final static Property Address = new Property(3, String.class, "address", false, "ADDRESS");
        public final static Property Losetime = new Property(4, java.util.Date.class, "losetime", false, "LOSETIME");
        public final static Property EquipMentID = new Property(5, long.class, "EquipMentID", false, "EQUIP_MENT_ID");
    };

    private DaoSession daoSession;

    private Query<Location> equipMent_LocationListQuery;

    public LocationDao(DaoConfig config) {
        super(config);
    }
    
    public LocationDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"LOCATION\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"LATITUDE\" TEXT," + // 1: latitude
                "\"LONGTITUDE\" TEXT," + // 2: longtitude
                "\"ADDRESS\" TEXT," + // 3: address
                "\"LOSETIME\" INTEGER," + // 4: losetime
                "\"EQUIP_MENT_ID\" INTEGER NOT NULL );"); // 5: EquipMentID
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"LOCATION\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Location entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String latitude = entity.getLatitude();
        if (latitude != null) {
            stmt.bindString(2, latitude);
        }
 
        String longtitude = entity.getLongtitude();
        if (longtitude != null) {
            stmt.bindString(3, longtitude);
        }
 
        String address = entity.getAddress();
        if (address != null) {
            stmt.bindString(4, address);
        }
 
        java.util.Date losetime = entity.getLosetime();
        if (losetime != null) {
            stmt.bindLong(5, losetime.getTime());
        }
        stmt.bindLong(6, entity.getEquipMentID());
    }

    @Override
    protected void attachEntity(Location entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Location readEntity(Cursor cursor, int offset) {
        Location entity = new Location( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // latitude
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // longtitude
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // address
            cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)), // losetime
            cursor.getLong(offset + 5) // EquipMentID
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Location entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setLatitude(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setLongtitude(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setAddress(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setLosetime(cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)));
        entity.setEquipMentID(cursor.getLong(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Location entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Location entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "locationList" to-many relationship of EquipMent. */
    public List<Location> _queryEquipMent_LocationList(long EquipMentID) {
        synchronized (this) {
            if (equipMent_LocationListQuery == null) {
                QueryBuilder<Location> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.EquipMentID.eq(null));
                equipMent_LocationListQuery = queryBuilder.build();
            }
        }
        Query<Location> query = equipMent_LocationListQuery.forCurrentThread();
        query.setParameter(0, EquipMentID);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getEquipMentDao().getAllColumns());
            builder.append(" FROM LOCATION T");
            builder.append(" LEFT JOIN EQUIP_MENT T0 ON T.\"EQUIP_MENT_ID\"=T0.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected Location loadCurrentDeep(Cursor cursor, boolean lock) {
        Location entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        EquipMent equipMent = loadCurrentOther(daoSession.getEquipMentDao(), cursor, offset);
         if(equipMent != null) {
            entity.setEquipMent(equipMent);
        }

        return entity;    
    }

    public Location loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<Location> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<Location> list = new ArrayList<Location>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<Location> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<Location> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
