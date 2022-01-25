package coms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import coms.process.ComsEvent;
import io.kubemq.sdk.basic.ServerAddressNotSuppliedException;
import io.kubemq.sdk.queue.Message;
import io.kubemq.sdk.queue.Queue;
import io.kubemq.sdk.queue.SendMessageResult;
import io.kubemq.sdk.tools.Converter;

@Component
public class ComsMessageService {
	
	@Autowired
	private Queue queue;
	
	public void sendMessage(ComsEvent event) {
		try {
	        final SendMessageResult result = queue.SendQueueMessage(new Message()
	                .setBody(Converter.ToByteArray(event)));
	    } catch (Exception e) {
	    	System.out.println("Exception posting message: "+e.getMessage());
	    	e.printStackTrace();
	    }
	}
}
