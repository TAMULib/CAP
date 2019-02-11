package edu.tamu.cap.model.repo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import edu.tamu.cap.model.RV;
import edu.tamu.cap.model.repo.custom.RVRepoCustom;
import edu.tamu.weaver.data.model.repo.WeaverRepo;

public interface RVRepo extends WeaverRepo<RV>, RVRepoCustom, JpaSpecificationExecutor<RV> {

	RV findByName(String name);

}
