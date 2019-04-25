package com.example.demo;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexCon {

	@RequestMapping("/")
	public String index() {
		return T.a();
	}

	@RequestMapping("/t")
	public String t() {
		return T.ak();
	}

	@RequestMapping("/t/*")
	public String ts() {
		return T.a();
	}

	@RequestMapping("/a")
	public String a() {
		return T.ak();
	}

	
}