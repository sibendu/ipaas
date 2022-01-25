package coms.block.ui.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import coms.block.ui.model.MediatorRequestBody;

@RestController
public class ServletController {
	@Value("${coms.service.uri}")
	private String serviceUri;
	@RequestMapping("/hello")
	public String index() {
		return new Date() + "\n";
	}
	 @PostMapping(value = "/invoke", consumes = "application/json", produces ="application/json") 
	  public String invoke(@RequestBody MediatorRequestBody request) 
	  { 
		  
		  String serviceuri = this.serviceUri; 
		  if(serviceuri!=null)
		  {
			  WebClient client = WebClient.create(serviceuri); 
			  String response = client.get().uri(request.getOperation()).retrieve().bodyToMono(String.class).block(); 
			  return response; 
		  }
		  else 
			  return "error";
	  }
}
