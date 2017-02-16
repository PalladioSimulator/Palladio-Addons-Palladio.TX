package org.palladiosimulator.transactions.ccsim;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.palladiosimulator.pcmtx.Table;
import org.palladiosimulator.transactions.ccsim.listener.LockingListener;

public class LockManager {

    /**
     * maps {@link Table}s (identified by their id) to their corresponding lock manager.
     * 
     * TODO use Table objects instead of ids?
     */
    private Map<String, TableLockManager> tableManagers;

    public LockManager(Collection<Table> tables) {
        tableManagers = new HashMap<>();
        for (Table table : tables) {
            tableManagers.put(table.getId(), new TableLockManager(table));
        }
    }

    public TableLockManager get(Table table) {
        return tableManagers.get(table.getId());
    }
    
    public void release(LockDescriptor descriptor) {
        TableLockManager tlm = get(descriptor.getLock().getTable());
        tlm.release(descriptor);
    }
    
    public Collection<TableLockManager> getTableLockManagers() {
        return tableManagers.values();
    }
    
    public void addListener(Table table, LockingListener l) {
        get(table).addLockingListener(l);
    }
    
    public void removeListener(Table table, LockingListener l) {
        get(table).removeLockingListener(l);
    }

}
