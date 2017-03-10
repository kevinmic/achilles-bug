package com.checkbug;

import info.archinnov.achilles.annotations.ClusteringColumn;
import info.archinnov.achilles.annotations.PartitionKey;
import info.archinnov.achilles.annotations.Table;

@Table(table = MyTable.TABLE_NAME)
public class MyTable {
    public static final String TABLE_NAME = "mytable";

    @PartitionKey
    private String myKey;

    @ClusteringColumn
    private String myCCol1;

    @ClusteringColumn(2)
    private String myCCol2;

    private String myvar;

    public String getMyKey() {
        return myKey;
    }

    public void setMyKey(String myKey) {
        this.myKey = myKey;
    }

    public String getMyCCol1() {
        return myCCol1;
    }

    public void setMyCCol1(String myCCol1) {
        this.myCCol1 = myCCol1;
    }

    public String getMyCCol2() {
        return myCCol2;
    }

    public void setMyCCol2(String myCCol2) {
        this.myCCol2 = myCCol2;
    }

    public String getMyvar() {
        return myvar;
    }

    public void setMyvar(String myvar) {
        this.myvar = myvar;
    }
}
