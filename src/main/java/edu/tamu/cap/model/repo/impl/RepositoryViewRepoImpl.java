package edu.tamu.cap.model.repo.impl;

import edu.tamu.cap.model.RepositoryView;
import edu.tamu.cap.model.repo.RepositoryViewRepo;
import edu.tamu.cap.model.repo.custom.RepositoryViewRepoCustom;
import edu.tamu.weaver.data.model.repo.impl.AbstractWeaverRepoImpl;

public class RepositoryViewRepoImpl extends AbstractWeaverRepoImpl<RepositoryView, RepositoryViewRepo> implements RepositoryViewRepoCustom {
	@Override
	protected String getChannel() {
		return "/channel/repository-view";
	}
}