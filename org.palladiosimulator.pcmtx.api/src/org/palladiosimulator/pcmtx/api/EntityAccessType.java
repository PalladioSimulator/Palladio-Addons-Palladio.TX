package org.palladiosimulator.pcmtx.api;

public enum EntityAccessType {

	READ("Read"), INSERT("Insert"), UPDATE("Update");

	private String name;

	EntityAccessType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	/**
	 * @param name
	 * @return the entity access type corresponding to the specified name; {@code null}, if no corresponding entity
	 *         access type exists.
	 */
	public static EntityAccessType from(String name) {
		switch (name) {
		case "Read":
			return READ;
		case "Insert":
			return INSERT;
		case "Update":
			return UPDATE;
		default:
			return null;
		}
	}

}
