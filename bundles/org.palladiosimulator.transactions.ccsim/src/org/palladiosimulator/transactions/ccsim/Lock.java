package org.palladiosimulator.transactions.ccsim;

import org.palladiosimulator.pcmtx.Table;

public class Lock {

    private Table table;

    /** row, page, table, ... */
    private int resource;

    public Lock(Table table, int resource) {
        if (table == null) {
            throw new NullPointerException("Table may not be null");
        }
        this.table = table;
        this.resource = resource;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + resource;
        result = prime * result + ((table == null) ? 0 : table.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Lock other = (Lock) obj;
        if (resource != other.resource)
            return false;
        if (table == null) {
            if (other.table != null)
                return false;
        } else if (other.table == null || !table.getId().equals(other.table.getId()))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Lock [table=" + table.getEntityName() + ", resource=" + resource + "]";
    }
    
}
