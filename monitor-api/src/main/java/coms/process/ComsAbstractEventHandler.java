package coms.process;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import coms.model.ProcessInstance;
import coms.service.ComsProcessService;

public abstract class ComsAbstractEventHandler{
	
	@Autowired
    private ComsProcessService jobService;
	
	public ComsAbstractEventHandler(ComsProcessService jobService) {
		this.jobService = jobService;
	}

	public ComsProcessService getJobService() {
		return jobService;
	}

	public void setJobService(ComsProcessService jobService) {
		this.jobService = jobService;
	}
}
