package com.genebio.nextprot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.genebio.nextprot.domain.Message;

@Controller("hello")
public class HelloController {

	
	@RequestMapping(value="/say", method=RequestMethod.GET)
	public Message hello() {
		Message msg = new Message("hello world");
		return msg;
	}
}
