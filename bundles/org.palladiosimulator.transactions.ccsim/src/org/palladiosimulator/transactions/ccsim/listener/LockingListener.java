package org.palladiosimulator.transactions.ccsim.listener;

import org.palladiosimulator.transactions.ccsim.LockMode;

public interface LockingListener {

    public void lockedRowsCountChanged(int count);
    
    public void lockedRowsCountPerModeChanged(LockMode mode, int count);
    
    public void meanWaitingQueueLengthChanged(double meanWaitingQueueLength);
    
}
