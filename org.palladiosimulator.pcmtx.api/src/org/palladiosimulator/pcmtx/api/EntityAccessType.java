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
	
}
