package edu.tamu.cap.model.repo.impl;

import edu.tamu.cap.model.RV;
import edu.tamu.cap.model.repo.RVRepo;
import edu.tamu.cap.model.repo.custom.RVRepoCustom;
import edu.tamu.weaver.data.model.repo.impl.AbstractWeaverRepoImpl;

public class RVRepoImpl extends AbstractWeaverRepoImpl<RV, RVRepo> implements RVRepoCustom {
	@Override
	protected String getChannel() {
		return "/channel/rv";
	}
}