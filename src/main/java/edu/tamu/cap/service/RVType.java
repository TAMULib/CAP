package edu.tamu.cap.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.tamu.cap.model.response.RVContext;
import edu.tamu.weaver.context.SpringContext;

public enum RVType {
	FEDORA("Fedora"), DSPACE("DSpace");

	private final String gloss;

	private static List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();

	static {
		for (RVType type : RVType.values()) {
			Map<String, Object> valueGloss = new HashMap<String, Object>();
			valueGloss.put("value", type.name());
			valueGloss.put("gloss", type.getGloss());
			values.add(valueGloss);
		}
	}

	RVType(String gloss) {
		this.gloss = gloss;
	}

	public String getGloss() {
		return this.gloss;
	}

	public static List<Map<String, Object>> getValues() {
	    
	    values.forEach(valueMap->{
	        String thisEnumType = (String) valueMap.get("gloss");
	        RVService<?> rvs = SpringContext.bean(thisEnumType);
	        RVContext rvc = new RVContext();
	        valueMap.putAll(rvs.featureSupport(rvc).getFeatures());
	    });
	    	    
		return values;
	}

}
