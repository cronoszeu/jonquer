package jonquer.model.def;

public class COMonsterSpawnDef {
    
    private int id = 0;
    private int mapid = 0;
    private int bound_x = 0;
    private int bound_y = 0;
    private int bound_cx = 0;
    private int bound_cy = 0;
    private int maxNpcs = 0;
    private int rest_secs = 0;
    private int respawnTime = 0;
    private int npctype = 0;
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getMapid() {
        return mapid;
    }
    public void setMapid(int mapid) {
        this.mapid = mapid;
    }
    public int getBound_x() {
        return bound_x;
    }
    public void setBound_x(int boundX) {
        bound_x = boundX;
    }
    public int getBound_y() {
        return bound_y;
    }
    public void setBound_y(int boundY) {
        bound_y = boundY;
    }
    public int getBound_cx() {
        return bound_cx;
    }
    public void setBound_cx(int boundCx) {
        bound_cx = boundCx;
    }
    public int getBound_cy() {
        return bound_cy;
    }
    public void setBound_cy(int boundCy) {
        bound_cy = boundCy;
    }
    public int getMaxNpcs() {
        return maxNpcs;
    }
    public void setMaxNpcs(int maxNpcs) {
        this.maxNpcs = maxNpcs;
    }
    public int getRest_secs() {
        return rest_secs;
    }
    public void setRest_secs(int restSecs) {
        rest_secs = restSecs;
    }
    public int getRespawnTime() {
        return respawnTime;
    }
    public void setRespawnTime(int respawnTime) {
        this.respawnTime = respawnTime;
    }
    public int getNpctype() {
        return npctype;
    }
    public void setNpctype(int npctype) {
        this.npctype = npctype;
    }

}
