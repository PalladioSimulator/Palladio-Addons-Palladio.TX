/**
 */
package org.palladiosimulator.pcmtx.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.palladiosimulator.pcm.core.entity.impl.EntityImpl;

import org.palladiosimulator.pcmtx.DataRepository;
import org.palladiosimulator.pcmtx.Database;
import org.palladiosimulator.pcmtx.PcmtxPackage;
import org.palladiosimulator.pcmtx.Table;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Data Repository</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.palladiosimulator.pcmtx.impl.DataRepositoryImpl#getTable <em>Table</em>}</li>
 *   <li>{@link org.palladiosimulator.pcmtx.impl.DataRepositoryImpl#getDatabases <em>Databases</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DataRepositoryImpl extends EntityImpl implements DataRepository {
	/**
	 * The cached value of the '{@link #getTable() <em>Table</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTable()
	 * @generated
	 * @ordered
	 */
	protected EList<Table> table;

	/**
	 * The cached value of the '{@link #getDatabases() <em>Databases</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDatabases()
	 * @generated
	 * @ordered
	 */
	protected EList<Database> databases;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DataRepositoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PcmtxPackage.Literals.DATA_REPOSITORY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Table> getTable() {
		if (table == null) {
			table = new EObjectContainmentEList<Table>(Table.class, this, PcmtxPackage.DATA_REPOSITORY__TABLE);
		}
		return table;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Database> getDatabases() {
		if (databases == null) {
			databases = new EObjectContainmentEList<Database>(Database.class, this, PcmtxPackage.DATA_REPOSITORY__DATABASES);
		}
		return databases;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PcmtxPackage.DATA_REPOSITORY__TABLE:
				return ((InternalEList<?>)getTable()).basicRemove(otherEnd, msgs);
			case PcmtxPackage.DATA_REPOSITORY__DATABASES:
				return ((InternalEList<?>)getDatabases()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PcmtxPackage.DATA_REPOSITORY__TABLE:
				return getTable();
			case PcmtxPackage.DATA_REPOSITORY__DATABASES:
				return getDatabases();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case PcmtxPackage.DATA_REPOSITORY__TABLE:
				getTable().clear();
				getTable().addAll((Collection<? extends Table>)newValue);
				return;
			case PcmtxPackage.DATA_REPOSITORY__DATABASES:
				getDatabases().clear();
				getDatabases().addAll((Collection<? extends Database>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case PcmtxPackage.DATA_REPOSITORY__TABLE:
				getTable().clear();
				return;
			case PcmtxPackage.DATA_REPOSITORY__DATABASES:
				getDatabases().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case PcmtxPackage.DATA_REPOSITORY__TABLE:
				return table != null && !table.isEmpty();
			case PcmtxPackage.DATA_REPOSITORY__DATABASES:
				return databases != null && !databases.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //DataRepositoryImpl
