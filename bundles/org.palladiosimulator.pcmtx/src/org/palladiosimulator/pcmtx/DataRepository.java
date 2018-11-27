/**
 */
package org.palladiosimulator.pcmtx;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.palladiosimulator.pcm.core.entity.Entity;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Data Repository</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.palladiosimulator.pcmtx.DataRepository#getTable <em>Table</em>}</li>
 *   <li>{@link org.palladiosimulator.pcmtx.DataRepository#getDatabases <em>Databases</em>}</li>
 * </ul>
 *
 * @see org.palladiosimulator.pcmtx.PcmtxPackage#getDataRepository()
 * @model
 * @generated
 */
public interface DataRepository extends EObject, Entity {
	/**
	 * Returns the value of the '<em><b>Table</b></em>' containment reference list.
	 * The list contents are of type {@link org.palladiosimulator.pcmtx.Table}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Table</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Table</em>' containment reference list.
	 * @see org.palladiosimulator.pcmtx.PcmtxPackage#getDataRepository_Table()
	 * @model containment="true"
	 * @generated
	 */
	EList<Table> getTable();

	/**
	 * Returns the value of the '<em><b>Databases</b></em>' containment reference list.
	 * The list contents are of type {@link org.palladiosimulator.pcmtx.Database}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Databases</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Databases</em>' containment reference list.
	 * @see org.palladiosimulator.pcmtx.PcmtxPackage#getDataRepository_Databases()
	 * @model containment="true"
	 * @generated
	 */
	EList<Database> getDatabases();

} // DataRepository
