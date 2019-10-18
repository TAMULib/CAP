package edu.tamu.cap.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.tamu.cap.model.response.RepositoryViewContext;
import edu.tamu.cap.service.repositoryview.RepositoryViewService;
import edu.tamu.weaver.context.SpringContext;

public enum RepositoryViewType {
    FEDORA("Fedora"), DSPACE("DSpace");

    private final String gloss;

    private static List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();

    static {
        for (RepositoryViewType type : RepositoryViewType.values()) {
            Map<String, Object> valueGloss = new HashMap<String, Object>();
            valueGloss.put("value", type.name());
            valueGloss.put("gloss", type.getGloss());
            values.add(valueGloss);
        }
    }

    RepositoryViewType(String gloss) {
        this.gloss = gloss;
    }

    public String getGloss() {
        return this.gloss;
    }

    public static List<Map<String, Object>> getValues() {

        values.forEach(valueMap -> {
            String thisEnumType = (String) valueMap.get("gloss");
            RepositoryViewService<?> repositoryViews = SpringContext.bean(thisEnumType);
            RepositoryViewContext repositoryViewContext = new RepositoryViewContext();
            valueMap.putAll(repositoryViews.featureSupport(repositoryViewContext).getFeatures());
        });

        return values;
    }

}
