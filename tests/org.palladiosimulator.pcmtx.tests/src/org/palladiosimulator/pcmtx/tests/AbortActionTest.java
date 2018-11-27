/**
 */
package org.palladiosimulator.pcmtx.tests;

import junit.framework.TestCase;

import junit.textui.TestRunner;

import org.palladiosimulator.pcmtx.AbortAction;
import org.palladiosimulator.pcmtx.PcmtxFactory;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Abort Action</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class AbortActionTest extends TestCase {

	/**
	 * The fixture for this Abort Action test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AbortAction fixture = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(AbortActionTest.class);
	}

	/**
	 * Constructs a new Abort Action test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AbortActionTest(String name) {
		super(name);
	}

	/**
	 * Sets the fixture for this Abort Action test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void setFixture(AbortAction fixture) {
		this.fixture = fixture;
	}

	/**
	 * Returns the fixture for this Abort Action test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AbortAction getFixture() {
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
		setFixture(PcmtxFactory.eINSTANCE.createAbortAction());
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

} //AbortActionTest
