package org.palladiosimulator.transactions.ccsim;

import org.palladiosimulator.pcm.seff.seff_performance.ResourceCall;
import org.palladiosimulator.pcmtx.Table;

public class TableAccess {

    private Table table;

    private int quantity;

    private String type;

    /** the entity access, which is part of the query causing this table access */
    private ResourceCall entityAccess;

    public TableAccess(Table table, int quantity, String type, ResourceCall entityAccess) {
        super();
        this.table = table;
        this.quantity = quantity;
        this.type = type;
        this.entityAccess = entityAccess;
    }

    public Table getTable() {
        return table;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getType() {
        return type;
    }

    public ResourceCall getResourceCall() {
        return entityAccess;
    }

    @Override
    public String toString() {
        return "TableAccess [table=" + table + ", quantity=" + quantity + ", type=" + type + "]";
    }

}
