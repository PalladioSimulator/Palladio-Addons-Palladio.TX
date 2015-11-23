/**
 */
package org.palladiosimulator.pcmtx;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Database</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.palladiosimulator.pcmtx.Database#getIsolation <em>Isolation</em>}</li>
 *   <li>{@link org.palladiosimulator.pcmtx.Database#getTimeout <em>Timeout</em>}</li>
 * </ul>
 *
 * @see org.palladiosimulator.pcmtx.PcmtxPackage#getDatabase()
 * @model
 * @generated
 */
public interface Database extends EObject {
	/**
	 * Returns the value of the '<em><b>Isolation</b></em>' attribute.
	 * The default value is <code>"READ_COMMITTED"</code>.
	 * The literals are from the enumeration {@link org.palladiosimulator.pcmtx.TransactionIsolation}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Isolation</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Isolation</em>' attribute.
	 * @see org.palladiosimulator.pcmtx.TransactionIsolation
	 * @see #setIsolation(TransactionIsolation)
	 * @see org.palladiosimulator.pcmtx.PcmtxPackage#getDatabase_Isolation()
	 * @model default="READ_COMMITTED"
	 * @generated
	 */
	TransactionIsolation getIsolation();

	/**
	 * Sets the value of the '{@link org.palladiosimulator.pcmtx.Database#getIsolation <em>Isolation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Isolation</em>' attribute.
	 * @see org.palladiosimulator.pcmtx.TransactionIsolation
	 * @see #getIsolation()
	 * @generated
	 */
	void setIsolation(TransactionIsolation value);

	/**
	 * Returns the value of the '<em><b>Timeout</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Timeout</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Timeout</em>' attribute.
	 * @see #setTimeout(int)
	 * @see org.palladiosimulator.pcmtx.PcmtxPackage#getDatabase_Timeout()
	 * @model default="0"
	 * @generated
	 */
	int getTimeout();

	/**
	 * Sets the value of the '{@link org.palladiosimulator.pcmtx.Database#getTimeout <em>Timeout</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Timeout</em>' attribute.
	 * @see #getTimeout()
	 * @generated
	 */
	void setTimeout(int value);

} // Database
