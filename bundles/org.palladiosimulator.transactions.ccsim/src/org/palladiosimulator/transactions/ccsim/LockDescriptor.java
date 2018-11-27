package org.palladiosimulator.transactions.ccsim;

import java.util.HashSet;
import java.util.Set;

public class LockDescriptor {

    private Lock lock;

    private ITransaction tx;

    private LockMode type;

    private Set<LockDescriptor> strongerLocks;

    private Set<LockDescriptor> weakerLocks;

    public LockDescriptor(ITransaction tx, Lock lock, LockMode type) {
        this.tx = tx;
        this.lock = lock;
        this.type = type;
    }

    public ITransaction getTx() {
        return tx;
    }

    public Lock getLock() {
        return lock;
    }

    public LockMode getType() {
        return type;
    }

    // TODO
    public Set<LockDescriptor> getStrongerLocks() {
        if (strongerLocks == null) {
            // TODO get lock types from LockType enum?
            strongerLocks = new HashSet<>();
            if (type == LockMode.SHARED) {
                strongerLocks.add(new LockDescriptor(tx, lock, LockMode.EXCLUSIVE));
            }
        }
        return strongerLocks;
    }

    public Set<LockDescriptor> getWeakerLocks() {
        if (weakerLocks == null) {
            // TODO get lock types from LockType enum?
            weakerLocks = new HashSet<>();
            if (type == LockMode.EXCLUSIVE) {
                weakerLocks.add(new LockDescriptor(tx, lock, LockMode.SHARED));
            }
        }
        return weakerLocks;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((lock == null) ? 0 : lock.hashCode());
        result = prime * result + ((tx == null) ? 0 : tx.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        LockDescriptor other = (LockDescriptor) obj;
        if (lock == null) {
            if (other.lock != null)
                return false;
        } else if (!lock.equals(other.lock))
            return false;
        if (tx == null) {
            if (other.tx != null)
                return false;
        } else if (!tx.equals(other.tx))
            return false;
        if (type != other.type)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "LockDescriptor [lock=" + lock + ", type=" + type + ", tx=" + tx + "]";
    }
    
    

}
