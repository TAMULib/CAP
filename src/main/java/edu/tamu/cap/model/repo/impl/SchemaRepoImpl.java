package edu.tamu.cap.model.repo.impl;

import edu.tamu.cap.model.Schema;
import edu.tamu.cap.model.repo.SchemaRepo;
import edu.tamu.cap.model.repo.custom.SchemaRepoCustom;
import edu.tamu.weaver.data.model.repo.impl.AbstractWeaverRepoImpl;

public class SchemaRepoImpl extends AbstractWeaverRepoImpl<Schema, SchemaRepo> implements SchemaRepoCustom {
	@Override
	protected String getChannel() {
		return "/channel/schema";
	}
}