package edu.tamu.cap.model.repo.impl;

import org.springframework.beans.factory.annotation.Autowired;

import edu.tamu.cap.model.IR;
import edu.tamu.cap.model.repo.IRRepo;
import edu.tamu.cap.model.repo.custom.IRRepoCustom;
import edu.tamu.weaver.data.model.repo.impl.AbstractWeaverRepoImpl;

public class IRRepoImpl extends AbstractWeaverRepoImpl<IR, IRRepo> implements IRRepoCustom {
	
	@Autowired
    private IRRepo irRepo;

	@Override
	public IR create(String name, String uri) {
		IR ir = irRepo.findByName(name);
        if (ir == null) {
        	ir = irRepo.create(new IR(name, uri));
        }
        return ir;		
	}

	@Override
	protected String getChannel() {
		return "/channel/ir";
	}
}