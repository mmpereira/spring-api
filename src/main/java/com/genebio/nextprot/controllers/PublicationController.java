package com.genebio.nextprot.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.genebio.nextprot.domain.Author;
import com.genebio.nextprot.domain.Publication;
import com.genebio.nextprot.service.AuthorService;
import com.genebio.nextprot.service.PublicationService;

@Controller
public class PublicationController {

	@Autowired
	PublicationService publicationService;

	@Autowired
	AuthorService authorService;

	// http://localhost:8080/spring-api/pub/6634104.xml
	@RequestMapping(value = "/publications/{id}", method = RequestMethod.GET)
	public Publication get(@PathVariable("id") String id) {
		return publicationService.getPublicationById(Long.valueOf(id));
	}

	// http://localhost:8080/spring-api/list.xml?title=correction
	@RequestMapping(value = "/publications/", method = RequestMethod.GET)
	public List<Publication> list(@RequestParam("title") String title) {
		return publicationService.getPublicationByTitle("%" + title + "%");
	}

	// Velocity viewer

	// http://localhost:8080/spring-api/velocity/6634104.xml
	@RequestMapping(value = "/velocity/{id}", method = RequestMethod.GET)
	public String mp(@PathVariable("id") String id, Model model) {
		Publication publication = publicationService.getPublicationById(6634104);
		model.addAttribute(publication);

		List<Author> authors = authorService.getAuthorByPublicationId(publication.getId());
		model.addAttribute("authors", authors);

		Map<Long, List<Long>> authorPublicationsMap = new HashMap<Long, List<Long>>();
		for(Author a : authors){
			authorPublicationsMap.put(Long.valueOf(a.getId()), publicationService.getPublicationIdsByAuthor(a.getLastName()));
		}
		
		model.addAttribute("authorPublications", authorPublicationsMap);

		return "publication";
	}

}
