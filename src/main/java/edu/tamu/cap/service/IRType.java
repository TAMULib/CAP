package edu.tamu.cap.service;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.tamu.cap.model.response.IRContext;
import edu.tamu.weaver.context.SpringContext;

public enum IRType {
	FEDORA("Fedora"), DSPACE("DSpace");

	private final String gloss;

	private static List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();

	static {
		for (IRType type : IRType.values()) {
			Map<String, Object> valueGloss = new HashMap<String, Object>();
			valueGloss.put("value", type.name());
			valueGloss.put("gloss", type.getGloss());
			
//			IRService<?> irservice = SpringContext.bean(type.getGloss());
//			valueGloss.put("features", irservice.featureSupport(new IRContext()).getFeatures());
			
			values.add(valueGloss);
		}
	}

	IRType(String gloss) {
		this.gloss = gloss;
	}

	public String getGloss() {
		return this.gloss;
	}

	public static List<Map<String, Object>> getValues() {
		return values;
	}

}
