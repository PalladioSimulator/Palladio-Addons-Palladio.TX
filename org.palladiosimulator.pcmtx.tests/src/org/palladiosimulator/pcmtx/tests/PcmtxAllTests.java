/**
 */
package org.palladiosimulator.pcmtx.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test suite for the '<em><b>Pcmtx</b></em>' model.
 * <!-- end-user-doc -->
 * @generated
 */
public class PcmtxAllTests extends TestSuite {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static Test suite() {
		TestSuite suite = new PcmtxAllTests("Pcmtx Tests");
		suite.addTest(PcmtxTests.suite());
		return suite;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PcmtxAllTests(String name) {
		super(name);
	}

} //PcmtxAllTests
