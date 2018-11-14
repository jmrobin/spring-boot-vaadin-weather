package jmr.vaadin.weather.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController
{
	@RequestMapping( "/" )
	public String home()
	{
		return "Spring Boot & Vaadin demo";
	}
}
