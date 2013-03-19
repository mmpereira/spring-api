package com.genebio.nextprot.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.genebio.nextprot.domain.Publication;
import com.genebio.nextprot.service.PublicationService;

@Controller("publicationController")
public class PublicationController {

	@Autowired
	PublicationService publicationService;
	
	@RequestMapping(value="/pub", method=RequestMethod.GET)
	public Publication hello() {
		return publicationService.getPublicationById(6634104);
	}
	
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public List<Publication> hell() {
		return publicationService.getPublicationByTitle("%Correction%");
	}
}
