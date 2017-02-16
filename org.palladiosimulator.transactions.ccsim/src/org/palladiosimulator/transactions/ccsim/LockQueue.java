package org.palladiosimulator.transactions.ccsim;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

public class LockQueue {

    private LockMode activeMode;

    private Queue<LockDescriptor> active = new LinkedList<>();

    private Deque<LockDescriptor> waiting = new LinkedList<>();

    public boolean removeActive(LockDescriptor lock) {
        boolean removed = active.remove(lock);
        adjustActiveMode();
        return removed;
    }

    public boolean removeWaiting(LockDescriptor lock) {
        boolean removed = waiting.remove(lock);
        return removed;
    }

    private void adjustActiveMode() {
        if (active.isEmpty()) {
            activeMode = LockMode.NONE;
        } else {
            activeMode = active.peek().getType(); // TODO generalize to > 2 modes
        }
    }

    public LockMode getActiveMode() {
        return activeMode;
    }

    public LockDescriptor peekWaiting() {
        return waiting.peek();
    }

    public LockDescriptor pollWaiting() {
        return waiting.poll();
    }

    public void addActive(LockDescriptor lock) {
        active.add(lock);
        adjustActiveMode();
    }

    public boolean isEmpty() {
        return waiting.isEmpty() && active.isEmpty();
    }

    public void addWaiting(LockDescriptor lockRequest) {
        waiting.add(lockRequest);
    }

    public void addWaitingPriority(LockDescriptor lockRequest) {
        waiting.addFirst(lockRequest);
    }

    public boolean hasWaiting() {
        return !waiting.isEmpty();
    }

    public int getActiveCount() {
        return active.size();
    }

    public Queue<LockDescriptor> getActive() {
        return active;
    }

    public Deque<LockDescriptor> getWaiting() {
        return waiting;
    }

    public int getWaitingCount() {
        return waiting.size();
    }

}
