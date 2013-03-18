package com.genebio.nextprot.services;

import com.genebio.nextprot.domain.Publication;

public interface PublicationService {
	
	/**
	 * Gets publication by id
	 * @param id
	 * @return
	 */
	public Publication getPublicationById(long id);

}
