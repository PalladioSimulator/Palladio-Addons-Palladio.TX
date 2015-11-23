/**
 */
package org.palladiosimulator.pcmtx;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Transaction Isolation</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.palladiosimulator.pcmtx.PcmtxPackage#getTransactionIsolation()
 * @model
 * @generated
 */
public enum TransactionIsolation implements Enumerator {
	/**
	 * The '<em><b>SERIALIZABLE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #SERIALIZABLE_VALUE
	 * @generated
	 * @ordered
	 */
	SERIALIZABLE(0, "SERIALIZABLE", "SERIALIZABLE"),

	/**
	 * The '<em><b>READ COMMITTED</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #READ_COMMITTED_VALUE
	 * @generated
	 * @ordered
	 */
	READ_COMMITTED(1, "READ_COMMITTED", "READ_COMMITTED"),

	/**
	 * The '<em><b>READ UNCOMMITTED</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #READ_UNCOMMITTED_VALUE
	 * @generated
	 * @ordered
	 */
	READ_UNCOMMITTED(2, "READ_UNCOMMITTED", "READ_UNCOMMITTED"),

	/**
	 * The '<em><b>VERSIONING</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #VERSIONING_VALUE
	 * @generated
	 * @ordered
	 */
	VERSIONING(3, "VERSIONING", "VERSIONING");

	/**
	 * The '<em><b>SERIALIZABLE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>SERIALIZABLE</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #SERIALIZABLE
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int SERIALIZABLE_VALUE = 0;

	/**
	 * The '<em><b>READ COMMITTED</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>READ COMMITTED</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #READ_COMMITTED
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int READ_COMMITTED_VALUE = 1;

	/**
	 * The '<em><b>READ UNCOMMITTED</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>READ UNCOMMITTED</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #READ_UNCOMMITTED
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int READ_UNCOMMITTED_VALUE = 2;

	/**
	 * The '<em><b>VERSIONING</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>VERSIONING</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #VERSIONING
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int VERSIONING_VALUE = 3;

	/**
	 * An array of all the '<em><b>Transaction Isolation</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final TransactionIsolation[] VALUES_ARRAY =
		new TransactionIsolation[] {
			SERIALIZABLE,
			READ_COMMITTED,
			READ_UNCOMMITTED,
			VERSIONING,
		};

	/**
	 * A public read-only list of all the '<em><b>Transaction Isolation</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<TransactionIsolation> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Transaction Isolation</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param literal the literal.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static TransactionIsolation get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			TransactionIsolation result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Transaction Isolation</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name the name.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static TransactionIsolation getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			TransactionIsolation result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Transaction Isolation</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the integer value.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static TransactionIsolation get(int value) {
		switch (value) {
			case SERIALIZABLE_VALUE: return SERIALIZABLE;
			case READ_COMMITTED_VALUE: return READ_COMMITTED;
			case READ_UNCOMMITTED_VALUE: return READ_UNCOMMITTED;
			case VERSIONING_VALUE: return VERSIONING;
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final int value;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String name;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String literal;

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private TransactionIsolation(int value, String name, String literal) {
		this.value = value;
		this.name = name;
		this.literal = literal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getValue() {
	  return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
	  return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLiteral() {
	  return literal;
	}

	/**
	 * Returns the literal value of the enumerator, which is its string representation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		return literal;
	}
	
} //TransactionIsolation
