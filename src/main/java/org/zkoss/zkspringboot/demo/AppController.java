package org.zkoss.zkspringboot.demo;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AppController {

	@RequestMapping("/secure/*")
	public String oidc(final Map<String, Object> map) {
		return "secure/minimal";
	}
}
