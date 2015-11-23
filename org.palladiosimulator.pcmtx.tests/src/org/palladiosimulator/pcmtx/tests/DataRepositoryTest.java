/**
 */
package org.palladiosimulator.pcmtx.tests;

import junit.framework.TestCase;

import junit.textui.TestRunner;

import org.palladiosimulator.pcmtx.DataRepository;
import org.palladiosimulator.pcmtx.PcmtxFactory;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Data Repository</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class DataRepositoryTest extends TestCase {

	/**
	 * The fixture for this Data Repository test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DataRepository fixture = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(DataRepositoryTest.class);
	}

	/**
	 * Constructs a new Data Repository test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DataRepositoryTest(String name) {
		super(name);
	}

	/**
	 * Sets the fixture for this Data Repository test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void setFixture(DataRepository fixture) {
		this.fixture = fixture;
	}

	/**
	 * Returns the fixture for this Data Repository test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DataRepository getFixture() {
		return fixture;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
	@Override
	protected void setUp() throws Exception {
		setFixture(PcmtxFactory.eINSTANCE.createDataRepository());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#tearDown()
	 * @generated
	 */
	@Override
	protected void tearDown() throws Exception {
		setFixture(null);
	}

} //DataRepositoryTest
