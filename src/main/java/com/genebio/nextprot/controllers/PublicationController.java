package com.genebio.nextprot.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.genebio.nextprot.domain.Author;
import com.genebio.nextprot.domain.Publication;
import com.genebio.nextprot.json.JsonUtils;
import com.genebio.nextprot.service.AuthorService;
import com.genebio.nextprot.service.PublicationService;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

@Controller
public class PublicationController {

	@Autowired
	PublicationService publicationService;

	@Autowired
	AuthorService authorService;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Mapping using Jackson (for xml one need to annotated the classes)
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns a publication by an id, example:
	 * http://localhost:8080/spring-api/publications/6634104.json
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/publications/{id}", method = RequestMethod.GET)
	public Publication get(@PathVariable("id") String id) {
		return publicationService.getPublicationById(Long.valueOf(id));
	}

	/**
	 * Returns a list of publications, example:
	 * http://localhost:8080/spring-api/publications/
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/publications/", method = RequestMethod.GET)
	public List<Publication> list() {
		return publicationService.getPublicationByTitle("%correction%");
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Mapping using Apache Velocity templates 
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * http://localhost:8080/spring-api/velocity/6634104/ --> Will use template publcation.vm
	 * http://localhost:8080/spring-api/velocity/6634104.xml --> Will use template publcation.vm.xml
	 * http://localhost:8080/spring-api/velocity/6634104.json --> Will use template publcation.vm.json
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/velocity/{id}", method = RequestMethod.GET)
	public String mp(@PathVariable("id") String id, Model model) {
		
		//Gets the publication by an id
		Publication publication = publicationService.getPublicationById(6634104);

		//Gets the authors of that publication
		List<Author> authors = authorService.getAuthorByPublicationId(publication.getId());

		//Gets the publications of an author
		Map<Long, List<Long>> authorPublicationsMap = new HashMap<Long, List<Long>>();
		for(Author a : authors){
			authorPublicationsMap.put(Long.valueOf(a.getId()), publicationService.getPublicationIdsByAuthor(a.getLastName()));
		}

		//Add the data structures to the view
		model.addAttribute("publication", publication);
		model.addAttribute("authors", authors);
		model.addAttribute("authorPublications", authorPublicationsMap);

		
		return "publication";
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// With model 
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//
	// http://localhost:8080/spring-api/velocity/6634104/
	@RequestMapping(value = "model/{id}", method = RequestMethod.GET)
	@ResponseBody
	@PreAuthorize("hasRole('ROLE_USER')")
//	@PreAuthorize("hasIpAddress('10.2.2.96')")
	public String mj(@PathVariable("id") String id, Model model) {

		ObjectMapper m = JsonUtils.getObjectMapper();
		ObjectNode root = m.createObjectNode();

		//Gets the publication by an id
		Publication publication = publicationService.getPublicationById(6634104);

		root.put("pubId", publication.getId());
		root.put("title", publication.getTitle());
		root.put("text", publication.getAbstractText());

		ArrayNode authorsArray = root.putArray("authors");
		
		//Gets the authors of that publication

		List<Author> authors = authorService.getAuthorByPublicationId(publication.getId());
		for(Author a : authors){
			ObjectNode authorNode = m.createObjectNode();
			ArrayNode publicationsIds = authorNode.putArray("publications");
			
				List<Long> publications = publicationService.getPublicationIdsByAuthor(a.getLastName());
			for(Long pubId : publications){
				m.createObjectNode();
				publicationsIds.add(pubId);
			}
			authorsArray.add(authorNode);
		}

		return JsonUtils.getRepresentationInString(m, root);
	}


	//Return xml ?
	//Return a map ? 
	
	
}
