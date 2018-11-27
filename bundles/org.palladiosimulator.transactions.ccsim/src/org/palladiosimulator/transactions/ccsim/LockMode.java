package org.palladiosimulator.transactions.ccsim;

public enum LockMode {

    SHARED,

    EXCLUSIVE,

    NONE;

    public static boolean isCompatible(LockMode requested, LockMode granted) {
        if (requested == NONE) {
            throw new RuntimeException("Lock mode NONE is not intended to be requested.");
        }
        if (granted == NONE) {
            return true;
        }
        if (granted == EXCLUSIVE) {
            if (requested == EXCLUSIVE) {
                return true;
            } else if (requested == SHARED) {
                return false;
            }
        }
        if (granted == SHARED) {
            if (requested == EXCLUSIVE) {
                return false;
            } else if (requested == SHARED) {
                return true;
            }
        }
        throw new RuntimeException("Could not determine lock compatibility.");
    }

}
