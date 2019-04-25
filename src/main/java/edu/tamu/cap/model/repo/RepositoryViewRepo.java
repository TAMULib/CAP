package edu.tamu.cap.model.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import edu.tamu.cap.model.RepositoryView;
import edu.tamu.cap.model.repo.custom.RepositoryViewRepoCustom;
import edu.tamu.weaver.data.model.repo.WeaverRepo;

public interface RepositoryViewRepo extends WeaverRepo<RepositoryView>, RepositoryViewRepoCustom, JpaSpecificationExecutor<RepositoryView> {

	RepositoryView findByName(String name);

	List<RepositoryView> findByRootUriContainingIgnoreCase(String rootUri);

}
