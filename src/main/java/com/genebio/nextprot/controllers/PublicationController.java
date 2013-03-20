package com.genebio.nextprot.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.genebio.nextprot.domain.Publication;
import com.genebio.nextprot.service.PublicationService;

@Controller("publicationController")
public class PublicationController {

	@Autowired
	PublicationService publicationService;
	
	//http://localhost:8080/spring-api/pub/6634104.xml
	@RequestMapping(value="/pub/{id}", method=RequestMethod.GET)
	public Publication get(@PathVariable("id") String id) {
		return publicationService.getPublicationById(Integer.parseInt(id));
	}

	//http://localhost:8080/spring-api/list.xml?title=correction
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public List<Publication> list(@RequestParam("title") String title) {
		return publicationService.getPublicationByTitle("%"+title+"%");
	}
}
