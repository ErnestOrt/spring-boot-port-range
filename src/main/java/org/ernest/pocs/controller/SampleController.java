package org.ernest.pocs.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

	Logger logger = LoggerFactory.getLogger(SampleController.class);
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public void updateAnalysisStatus() {
		logger.info("Hiiiiii INFO -> " + UUID.randomUUID().toString());
	}
}