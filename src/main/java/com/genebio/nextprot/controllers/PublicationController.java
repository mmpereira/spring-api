package com.genebio.nextprot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.genebio.nextprot.domain.Publication;

@Controller("publication")
public class PublicationController {

	@RequestMapping(value="/list", method=RequestMethod.GET)
	public Publication hello() {
		Publication pub = new Publication();
		pub.setAuthor("daniel");
		pub.setDescription("best api ever");
		
		return pub;
	}
}
