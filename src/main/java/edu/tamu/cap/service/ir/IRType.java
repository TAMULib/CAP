package edu.tamu.cap.service.ir;

public enum IRType {
	FEDORA("Fedora");
	
	private final String name;
	
	IRType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
}
