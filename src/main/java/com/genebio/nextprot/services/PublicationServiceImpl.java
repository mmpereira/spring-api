package com.genebio.nextprot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.genebio.nextprot.dao.PublicationDAO;
import com.genebio.nextprot.domain.Publication;

@Component("publicationService")
public class PublicationServiceImpl implements PublicationService{

	@Autowired
	private PublicationDAO publicationDAO;
	
	public Publication getPublicationById(long id){
		return publicationDAO.getPublicationById(id);
	}

}
