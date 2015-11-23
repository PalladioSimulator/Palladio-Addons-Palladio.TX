/**
 */
package org.palladiosimulator.pcmtx;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.palladiosimulator.pcm.resourcetype.ResourcetypePackage;

import org.palladiosimulator.pcm.seff.SeffPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.palladiosimulator.pcmtx.PcmtxFactory
 * @model kind="package"
 * @generated
 */
public interface PcmtxPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "pcmtx";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://palladiosimulator.org/PalladioComponentModel/Transactional/1.0";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "pcmtx";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	PcmtxPackage eINSTANCE = org.palladiosimulator.pcmtx.impl.PcmtxPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.palladiosimulator.pcmtx.impl.EntityTypeImpl <em>Entity Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.palladiosimulator.pcmtx.impl.EntityTypeImpl
	 * @see org.palladiosimulator.pcmtx.impl.PcmtxPackageImpl#getEntityType()
	 * @generated
	 */
	int ENTITY_TYPE = 0;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_TYPE__ID = ResourcetypePackage.RESOURCE_TYPE__ID;

	/**
	 * The feature id for the '<em><b>Entity Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_TYPE__ENTITY_NAME = ResourcetypePackage.RESOURCE_TYPE__ENTITY_NAME;

	/**
	 * The feature id for the '<em><b>Unit</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_TYPE__UNIT = ResourcetypePackage.RESOURCE_TYPE__UNIT;

	/**
	 * The feature id for the '<em><b>Resource Provided Roles Resource Interface Providing Entity</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_TYPE__RESOURCE_PROVIDED_ROLES_RESOURCE_INTERFACE_PROVIDING_ENTITY = ResourcetypePackage.RESOURCE_TYPE__RESOURCE_PROVIDED_ROLES_RESOURCE_INTERFACE_PROVIDING_ENTITY;

	/**
	 * The feature id for the '<em><b>Resource Repository Resource Type</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_TYPE__RESOURCE_REPOSITORY_RESOURCE_TYPE = ResourcetypePackage.RESOURCE_TYPE__RESOURCE_REPOSITORY_RESOURCE_TYPE;

	/**
	 * The number of structural features of the '<em>Entity Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_TYPE_FEATURE_COUNT = ResourcetypePackage.RESOURCE_TYPE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.palladiosimulator.pcmtx.impl.DataRepositoryImpl <em>Data Repository</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.palladiosimulator.pcmtx.impl.DataRepositoryImpl
	 * @see org.palladiosimulator.pcmtx.impl.PcmtxPackageImpl#getDataRepository()
	 * @generated
	 */
	int DATA_REPOSITORY = 1;

	/**
	 * The feature id for the '<em><b>Table</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_REPOSITORY__TABLE = 0;

	/**
	 * The feature id for the '<em><b>Databases</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_REPOSITORY__DATABASES = 1;

	/**
	 * The number of structural features of the '<em>Data Repository</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_REPOSITORY_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.palladiosimulator.pcmtx.impl.TableImpl <em>Table</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.palladiosimulator.pcmtx.impl.TableImpl
	 * @see org.palladiosimulator.pcmtx.impl.PcmtxPackageImpl#getTable()
	 * @generated
	 */
	int TABLE = 2;

	/**
	 * The feature id for the '<em><b>Rows</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TABLE__ROWS = 0;

	/**
	 * The feature id for the '<em><b>Types</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TABLE__TYPES = 1;

	/**
	 * The feature id for the '<em><b>Database</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TABLE__DATABASE = 2;

	/**
	 * The number of structural features of the '<em>Table</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TABLE_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.palladiosimulator.pcmtx.impl.DatabaseImpl <em>Database</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.palladiosimulator.pcmtx.impl.DatabaseImpl
	 * @see org.palladiosimulator.pcmtx.impl.PcmtxPackageImpl#getDatabase()
	 * @generated
	 */
	int DATABASE = 3;

	/**
	 * The feature id for the '<em><b>Isolation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATABASE__ISOLATION = 0;

	/**
	 * The feature id for the '<em><b>Timeout</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATABASE__TIMEOUT = 1;

	/**
	 * The number of structural features of the '<em>Database</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATABASE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.palladiosimulator.pcmtx.impl.CommitActionImpl <em>Commit Action</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.palladiosimulator.pcmtx.impl.CommitActionImpl
	 * @see org.palladiosimulator.pcmtx.impl.PcmtxPackageImpl#getCommitAction()
	 * @generated
	 */
	int COMMIT_ACTION = 4;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMIT_ACTION__ID = SeffPackage.ABSTRACT_INTERNAL_CONTROL_FLOW_ACTION__ID;

	/**
	 * The feature id for the '<em><b>Entity Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMIT_ACTION__ENTITY_NAME = SeffPackage.ABSTRACT_INTERNAL_CONTROL_FLOW_ACTION__ENTITY_NAME;

	/**
	 * The feature id for the '<em><b>Predecessor Abstract Action</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMIT_ACTION__PREDECESSOR_ABSTRACT_ACTION = SeffPackage.ABSTRACT_INTERNAL_CONTROL_FLOW_ACTION__PREDECESSOR_ABSTRACT_ACTION;

	/**
	 * The feature id for the '<em><b>Successor Abstract Action</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMIT_ACTION__SUCCESSOR_ABSTRACT_ACTION = SeffPackage.ABSTRACT_INTERNAL_CONTROL_FLOW_ACTION__SUCCESSOR_ABSTRACT_ACTION;

	/**
	 * The feature id for the '<em><b>Resource Demanding Behaviour Abstract Action</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMIT_ACTION__RESOURCE_DEMANDING_BEHAVIOUR_ABSTRACT_ACTION = SeffPackage.ABSTRACT_INTERNAL_CONTROL_FLOW_ACTION__RESOURCE_DEMANDING_BEHAVIOUR_ABSTRACT_ACTION;

	/**
	 * The feature id for the '<em><b>Resource Demand Action</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMIT_ACTION__RESOURCE_DEMAND_ACTION = SeffPackage.ABSTRACT_INTERNAL_CONTROL_FLOW_ACTION__RESOURCE_DEMAND_ACTION;

	/**
	 * The feature id for the '<em><b>Infrastructure Call Action</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMIT_ACTION__INFRASTRUCTURE_CALL_ACTION = SeffPackage.ABSTRACT_INTERNAL_CONTROL_FLOW_ACTION__INFRASTRUCTURE_CALL_ACTION;

	/**
	 * The feature id for the '<em><b>Resource Call Action</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMIT_ACTION__RESOURCE_CALL_ACTION = SeffPackage.ABSTRACT_INTERNAL_CONTROL_FLOW_ACTION__RESOURCE_CALL_ACTION;

	/**
	 * The number of structural features of the '<em>Commit Action</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMIT_ACTION_FEATURE_COUNT = SeffPackage.ABSTRACT_INTERNAL_CONTROL_FLOW_ACTION_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.palladiosimulator.pcmtx.impl.AbortActionImpl <em>Abort Action</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.palladiosimulator.pcmtx.impl.AbortActionImpl
	 * @see org.palladiosimulator.pcmtx.impl.PcmtxPackageImpl#getAbortAction()
	 * @generated
	 */
	int ABORT_ACTION = 5;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABORT_ACTION__ID = SeffPackage.ABSTRACT_INTERNAL_CONTROL_FLOW_ACTION__ID;

	/**
	 * The feature id for the '<em><b>Entity Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABORT_ACTION__ENTITY_NAME = SeffPackage.ABSTRACT_INTERNAL_CONTROL_FLOW_ACTION__ENTITY_NAME;

	/**
	 * The feature id for the '<em><b>Predecessor Abstract Action</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABORT_ACTION__PREDECESSOR_ABSTRACT_ACTION = SeffPackage.ABSTRACT_INTERNAL_CONTROL_FLOW_ACTION__PREDECESSOR_ABSTRACT_ACTION;

	/**
	 * The feature id for the '<em><b>Successor Abstract Action</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABORT_ACTION__SUCCESSOR_ABSTRACT_ACTION = SeffPackage.ABSTRACT_INTERNAL_CONTROL_FLOW_ACTION__SUCCESSOR_ABSTRACT_ACTION;

	/**
	 * The feature id for the '<em><b>Resource Demanding Behaviour Abstract Action</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABORT_ACTION__RESOURCE_DEMANDING_BEHAVIOUR_ABSTRACT_ACTION = SeffPackage.ABSTRACT_INTERNAL_CONTROL_FLOW_ACTION__RESOURCE_DEMANDING_BEHAVIOUR_ABSTRACT_ACTION;

	/**
	 * The feature id for the '<em><b>Resource Demand Action</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABORT_ACTION__RESOURCE_DEMAND_ACTION = SeffPackage.ABSTRACT_INTERNAL_CONTROL_FLOW_ACTION__RESOURCE_DEMAND_ACTION;

	/**
	 * The feature id for the '<em><b>Infrastructure Call Action</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABORT_ACTION__INFRASTRUCTURE_CALL_ACTION = SeffPackage.ABSTRACT_INTERNAL_CONTROL_FLOW_ACTION__INFRASTRUCTURE_CALL_ACTION;

	/**
	 * The feature id for the '<em><b>Resource Call Action</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABORT_ACTION__RESOURCE_CALL_ACTION = SeffPackage.ABSTRACT_INTERNAL_CONTROL_FLOW_ACTION__RESOURCE_CALL_ACTION;

	/**
	 * The number of structural features of the '<em>Abort Action</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABORT_ACTION_FEATURE_COUNT = SeffPackage.ABSTRACT_INTERNAL_CONTROL_FLOW_ACTION_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.palladiosimulator.pcmtx.TransactionIsolation <em>Transaction Isolation</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.palladiosimulator.pcmtx.TransactionIsolation
	 * @see org.palladiosimulator.pcmtx.impl.PcmtxPackageImpl#getTransactionIsolation()
	 * @generated
	 */
	int TRANSACTION_ISOLATION = 6;

	/**
	 * The meta object id for the '{@link org.palladiosimulator.pcmtx.TransactionScope <em>Transaction Scope</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.palladiosimulator.pcmtx.TransactionScope
	 * @see org.palladiosimulator.pcmtx.impl.PcmtxPackageImpl#getTransactionScope()
	 * @generated
	 */
	int TRANSACTION_SCOPE = 7;


	/**
	 * Returns the meta object for class '{@link org.palladiosimulator.pcmtx.EntityType <em>Entity Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Entity Type</em>'.
	 * @see org.palladiosimulator.pcmtx.EntityType
	 * @generated
	 */
	EClass getEntityType();

	/**
	 * Returns the meta object for class '{@link org.palladiosimulator.pcmtx.DataRepository <em>Data Repository</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Data Repository</em>'.
	 * @see org.palladiosimulator.pcmtx.DataRepository
	 * @generated
	 */
	EClass getDataRepository();

	/**
	 * Returns the meta object for the containment reference list '{@link org.palladiosimulator.pcmtx.DataRepository#getTable <em>Table</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Table</em>'.
	 * @see org.palladiosimulator.pcmtx.DataRepository#getTable()
	 * @see #getDataRepository()
	 * @generated
	 */
	EReference getDataRepository_Table();

	/**
	 * Returns the meta object for the containment reference list '{@link org.palladiosimulator.pcmtx.DataRepository#getDatabases <em>Databases</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Databases</em>'.
	 * @see org.palladiosimulator.pcmtx.DataRepository#getDatabases()
	 * @see #getDataRepository()
	 * @generated
	 */
	EReference getDataRepository_Databases();

	/**
	 * Returns the meta object for class '{@link org.palladiosimulator.pcmtx.Table <em>Table</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Table</em>'.
	 * @see org.palladiosimulator.pcmtx.Table
	 * @generated
	 */
	EClass getTable();

	/**
	 * Returns the meta object for the attribute '{@link org.palladiosimulator.pcmtx.Table#getRows <em>Rows</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Rows</em>'.
	 * @see org.palladiosimulator.pcmtx.Table#getRows()
	 * @see #getTable()
	 * @generated
	 */
	EAttribute getTable_Rows();

	/**
	 * Returns the meta object for the reference list '{@link org.palladiosimulator.pcmtx.Table#getTypes <em>Types</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Types</em>'.
	 * @see org.palladiosimulator.pcmtx.Table#getTypes()
	 * @see #getTable()
	 * @generated
	 */
	EReference getTable_Types();

	/**
	 * Returns the meta object for the reference '{@link org.palladiosimulator.pcmtx.Table#getDatabase <em>Database</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Database</em>'.
	 * @see org.palladiosimulator.pcmtx.Table#getDatabase()
	 * @see #getTable()
	 * @generated
	 */
	EReference getTable_Database();

	/**
	 * Returns the meta object for class '{@link org.palladiosimulator.pcmtx.Database <em>Database</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Database</em>'.
	 * @see org.palladiosimulator.pcmtx.Database
	 * @generated
	 */
	EClass getDatabase();

	/**
	 * Returns the meta object for the attribute '{@link org.palladiosimulator.pcmtx.Database#getIsolation <em>Isolation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Isolation</em>'.
	 * @see org.palladiosimulator.pcmtx.Database#getIsolation()
	 * @see #getDatabase()
	 * @generated
	 */
	EAttribute getDatabase_Isolation();

	/**
	 * Returns the meta object for the attribute '{@link org.palladiosimulator.pcmtx.Database#getTimeout <em>Timeout</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Timeout</em>'.
	 * @see org.palladiosimulator.pcmtx.Database#getTimeout()
	 * @see #getDatabase()
	 * @generated
	 */
	EAttribute getDatabase_Timeout();

	/**
	 * Returns the meta object for class '{@link org.palladiosimulator.pcmtx.CommitAction <em>Commit Action</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Commit Action</em>'.
	 * @see org.palladiosimulator.pcmtx.CommitAction
	 * @generated
	 */
	EClass getCommitAction();

	/**
	 * Returns the meta object for class '{@link org.palladiosimulator.pcmtx.AbortAction <em>Abort Action</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Abort Action</em>'.
	 * @see org.palladiosimulator.pcmtx.AbortAction
	 * @generated
	 */
	EClass getAbortAction();

	/**
	 * Returns the meta object for enum '{@link org.palladiosimulator.pcmtx.TransactionIsolation <em>Transaction Isolation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Transaction Isolation</em>'.
	 * @see org.palladiosimulator.pcmtx.TransactionIsolation
	 * @generated
	 */
	EEnum getTransactionIsolation();

	/**
	 * Returns the meta object for enum '{@link org.palladiosimulator.pcmtx.TransactionScope <em>Transaction Scope</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Transaction Scope</em>'.
	 * @see org.palladiosimulator.pcmtx.TransactionScope
	 * @generated
	 */
	EEnum getTransactionScope();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	PcmtxFactory getPcmtxFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.palladiosimulator.pcmtx.impl.EntityTypeImpl <em>Entity Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.palladiosimulator.pcmtx.impl.EntityTypeImpl
		 * @see org.palladiosimulator.pcmtx.impl.PcmtxPackageImpl#getEntityType()
		 * @generated
		 */
		EClass ENTITY_TYPE = eINSTANCE.getEntityType();

		/**
		 * The meta object literal for the '{@link org.palladiosimulator.pcmtx.impl.DataRepositoryImpl <em>Data Repository</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.palladiosimulator.pcmtx.impl.DataRepositoryImpl
		 * @see org.palladiosimulator.pcmtx.impl.PcmtxPackageImpl#getDataRepository()
		 * @generated
		 */
		EClass DATA_REPOSITORY = eINSTANCE.getDataRepository();

		/**
		 * The meta object literal for the '<em><b>Table</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_REPOSITORY__TABLE = eINSTANCE.getDataRepository_Table();

		/**
		 * The meta object literal for the '<em><b>Databases</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_REPOSITORY__DATABASES = eINSTANCE.getDataRepository_Databases();

		/**
		 * The meta object literal for the '{@link org.palladiosimulator.pcmtx.impl.TableImpl <em>Table</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.palladiosimulator.pcmtx.impl.TableImpl
		 * @see org.palladiosimulator.pcmtx.impl.PcmtxPackageImpl#getTable()
		 * @generated
		 */
		EClass TABLE = eINSTANCE.getTable();

		/**
		 * The meta object literal for the '<em><b>Rows</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TABLE__ROWS = eINSTANCE.getTable_Rows();

		/**
		 * The meta object literal for the '<em><b>Types</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TABLE__TYPES = eINSTANCE.getTable_Types();

		/**
		 * The meta object literal for the '<em><b>Database</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TABLE__DATABASE = eINSTANCE.getTable_Database();

		/**
		 * The meta object literal for the '{@link org.palladiosimulator.pcmtx.impl.DatabaseImpl <em>Database</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.palladiosimulator.pcmtx.impl.DatabaseImpl
		 * @see org.palladiosimulator.pcmtx.impl.PcmtxPackageImpl#getDatabase()
		 * @generated
		 */
		EClass DATABASE = eINSTANCE.getDatabase();

		/**
		 * The meta object literal for the '<em><b>Isolation</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DATABASE__ISOLATION = eINSTANCE.getDatabase_Isolation();

		/**
		 * The meta object literal for the '<em><b>Timeout</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DATABASE__TIMEOUT = eINSTANCE.getDatabase_Timeout();

		/**
		 * The meta object literal for the '{@link org.palladiosimulator.pcmtx.impl.CommitActionImpl <em>Commit Action</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.palladiosimulator.pcmtx.impl.CommitActionImpl
		 * @see org.palladiosimulator.pcmtx.impl.PcmtxPackageImpl#getCommitAction()
		 * @generated
		 */
		EClass COMMIT_ACTION = eINSTANCE.getCommitAction();

		/**
		 * The meta object literal for the '{@link org.palladiosimulator.pcmtx.impl.AbortActionImpl <em>Abort Action</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.palladiosimulator.pcmtx.impl.AbortActionImpl
		 * @see org.palladiosimulator.pcmtx.impl.PcmtxPackageImpl#getAbortAction()
		 * @generated
		 */
		EClass ABORT_ACTION = eINSTANCE.getAbortAction();

		/**
		 * The meta object literal for the '{@link org.palladiosimulator.pcmtx.TransactionIsolation <em>Transaction Isolation</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.palladiosimulator.pcmtx.TransactionIsolation
		 * @see org.palladiosimulator.pcmtx.impl.PcmtxPackageImpl#getTransactionIsolation()
		 * @generated
		 */
		EEnum TRANSACTION_ISOLATION = eINSTANCE.getTransactionIsolation();

		/**
		 * The meta object literal for the '{@link org.palladiosimulator.pcmtx.TransactionScope <em>Transaction Scope</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.palladiosimulator.pcmtx.TransactionScope
		 * @see org.palladiosimulator.pcmtx.impl.PcmtxPackageImpl#getTransactionScope()
		 * @generated
		 */
		EEnum TRANSACTION_SCOPE = eINSTANCE.getTransactionScope();

	}

} //PcmtxPackage
