package com.genebio.nextprot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.genebio.nextprot.domain.Message;
import com.genebio.nextprot.service.MessageService;

@Controller("hello")
public class HelloController {
	@Autowired
	MessageService messageService;
	
	@RequestMapping(value="/say", method=RequestMethod.GET)
	public Message hello() {
		return this.messageService.getMessage();
	}
}
