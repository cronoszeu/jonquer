/*
 * @(#)Npc.java 1.00 28/10/09
 *
 * Copyright Jonquer 2009. All rights reserved.
 */
package jonquer.model;

/**
 * A simple class which contains Npc data.
 *
 * @author Scott Batary
 * @version 1.00, 28/10/09
 */
public class Npc {

    private int id;
    private int ownerid;
    private int playerid;
    private String name;
    private int type;
    private int lookface;
    private int idxserver;
    private int mapid;
    private int cellx;
    private int celly;
    private int task0, task1, task2, task3, task4, task5, task6, task7;
    private int data0, data1, data2, data3;
    private String datastr;
    private int linkid;
    private int life;
    private int maxlife;
    private int base;
    private int sort;
    private int itemid;

    public Npc(int id, int ownerid, int playerid, String name, int type, int lookface, int idxserver, int mapid, int cellx, int celly, int task0, int task1, int task2, int task3, int task4, int task5, int task6, int task7, int data0, int data1, int data2, int data3, String datastr, int linkid, int life, int maxlife, int base, int sort, int itemid) {
        this.id = id;
        this.ownerid = ownerid;
        this.playerid = playerid;
        this.name = name;
        this.type = type;
        this.lookface = lookface;
        this.idxserver = idxserver;
        this.mapid = mapid;
        this.cellx = cellx;
        this.celly = celly;
        this.task0 = task0;
        this.task1 = task1;
        this.task2 = task2;
        this.task3 = task3;
        this.task4 = task4;
        this.task5 = task5;
        this.task6 = task6;
        this.task7 = task7;
        this.data0 = data0;
        this.data1 = data1;
        this.data2 = data2;
        this.data3 = data3;
        this.datastr = datastr;
        this.linkid = linkid;
        this.life = life;
        this.maxlife = maxlife;
        this.base = base;
        this.sort = sort;
        this.itemid = itemid;
    }

    public int getBase() {
        return base;
    }

    public void setBase(int base) {
        this.base = base;
    }

    public int getCellx() {
        return cellx;
    }

    public void setCellx(int cellx) {
        this.cellx = cellx;
    }

    public int getCelly() {
        return celly;
    }

    public void setCelly(int celly) {
        this.celly = celly;
    }

    public int getData0() {
        return data0;
    }

    public void setData0(int data0) {
        this.data0 = data0;
    }

    public int getData1() {
        return data1;
    }

    public void setData1(int data1) {
        this.data1 = data1;
    }

    public int getData2() {
        return data2;
    }

    public void setData2(int data2) {
        this.data2 = data2;
    }

    public int getData3() {
        return data3;
    }

    public void setData3(int data3) {
        this.data3 = data3;
    }

    public String getDatastr() {
        return datastr;
    }

    public void setDatastr(String datastr) {
        this.datastr = datastr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdxserver() {
        return idxserver;
    }

    public void setIdxserver(int idxserver) {
        this.idxserver = idxserver;
    }

    public int getItemid() {
        return itemid;
    }

    public void setItemid(int itemid) {
        this.itemid = itemid;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getLinkid() {
        return linkid;
    }

    public void setLinkid(int linkid) {
        this.linkid = linkid;
    }

    public int getLookface() {
        return lookface;
    }

    public void setLookface(int lookface) {
        this.lookface = lookface;
    }

    public int getMapid() {
        return mapid;
    }

    public void setMapid(int mapid) {
        this.mapid = mapid;
    }

    public int getMaxlife() {
        return maxlife;
    }

    public void setMaxlife(int maxlife) {
        this.maxlife = maxlife;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(int ownerid) {
        this.ownerid = ownerid;
    }

    public int getPlayerid() {
        return playerid;
    }

    public void setPlayerid(int playerid) {
        this.playerid = playerid;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getTask0() {
        return task0;
    }

    public void setTask0(int task0) {
        this.task0 = task0;
    }

    public int getTask1() {
        return task1;
    }

    public void setTask1(int task1) {
        this.task1 = task1;
    }

    public int getTask2() {
        return task2;
    }

    public void setTask2(int task2) {
        this.task2 = task2;
    }

    public int getTask3() {
        return task3;
    }

    public void setTask3(int task3) {
        this.task3 = task3;
    }

    public int getTask4() {
        return task4;
    }

    public void setTask4(int task4) {
        this.task4 = task4;
    }

    public int getTask5() {
        return task5;
    }

    public void setTask5(int task5) {
        this.task5 = task5;
    }

    public int getTask6() {
        return task6;
    }

    public void setTask6(int task6) {
        this.task6 = task6;
    }

    public int getTask7() {
        return task7;
    }

    public void setTask7(int task7) {
        this.task7 = task7;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
