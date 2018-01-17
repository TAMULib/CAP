package edu.tamu.cap.service.ir;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum IRType {
	FEDORA("Fedora"), DSPACE("DSpace");

	private final String gloss;

	private static List<Map<String, String>> values = new ArrayList<Map<String, String>>();

	static {
		for (IRType type : IRType.values()) {
			Map<String, String> valueGloss = new HashMap<String, String>();
			valueGloss.put("value", type.name());
			valueGloss.put("gloss", type.getGloss());
			values.add(valueGloss);
		}
	}

	IRType(String gloss) {
		this.gloss = gloss;
	}

	public String getGloss() {
		return this.gloss;
	}

	public static List<Map<String, String>> getValues() {
		return values;
	}

}
