/*
 * @(#)Portal.java      1.00 11/08/09
 *
 * Copyright 2009 Jonquer. All rights reserved.
 */

package jonquer.model;

/**
 *
 * @author s.bat
 * @version 1.00 11/08/09
 */
public class Portal {
    private int mapid;
    private short cellx;
    private short celly;
    private int target_mapid;
    private short target_cellx;
    private short target_celly;

    public Portal(int mapid, short cellx, short celly,
            int target_mapid, short target_cellx, short target_celly) {
        this.mapid = mapid;
        this.cellx = cellx;
        this.celly = celly;
        this.target_mapid = target_mapid;
        this.target_cellx = target_cellx;
        this.target_celly = target_celly;
    }

    public short getCellx() {
        return cellx;
    }

    public short getCelly() {
        return celly;
    }

    public int getMapid() {
        return mapid;
    }

    public short getTarget_cellx() {
        return target_cellx;
    }

    public short getTarget_celly() {
        return target_celly;
    }

    public int getTarget_mapid() {
        return target_mapid;
    }
}
