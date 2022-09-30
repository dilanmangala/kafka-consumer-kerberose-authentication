package lk.dialog.ezcash.kafka.kafkaconsumer.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;

import com.google.common.util.concurrent.RateLimiter;

import lk.dialog.ezcash.kafka.dto.RequestDto;
import lk.dialog.ezcash.kafka.kafkaconsumer.entity.CustomerRegistrationData;
import lombok.extern.slf4j.Slf4j;
/**
 * CustomerDetailsUpdateRequestService Implementation
 * @author Dilan_105248
 *
 */
@Service
@Slf4j
public class CustomerDetailsUpdateRequestService {
	
	@Value(value = "${validation.service.url}")
	private String serviceUrl;
	
	@Value(value = "${validation.service.user}")
	private String serviceUser;
	
	@Value(value = "${validation.service.password}")
	private String servicePassword;
	

	/**
	 * This method is used to request auto wallet API call by sending customer registration details
	 * @param customerRegistrationData this data is taken from db table
	 */
	public void requestCUstomerData(CustomerRegistrationData customerRegistrationData) {
		//RateLimiter rateLimiter = RateLimiter.create(5);
		log.info("start calling API for MSISDN {}", customerRegistrationData.getMsisdn());
		try {
			RequestDto requestDto = new RequestDto();
			BeanUtils.copyProperties(customerRegistrationData, requestDto);
			
			//rateLimiter.acquire();
			HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    headers.add("userName", serviceUser);
		    headers.add("password", servicePassword);
		    
		    String msisdn = customerRegistrationData.getMsisdn();
		    HttpEntity<RequestDto> request = new HttpEntity<>(requestDto, headers);
		    
			log.info("Call rest service for {}", msisdn);
			log.debug("Call rest service for MSISDN :{} ,  url {}:  Request {}: ", msisdn,serviceUrl, request);
		    
		    AsyncRestTemplate restTemplate = new AsyncRestTemplate(); 
		    ListenableFuture<ResponseEntity<String>> forEntity = restTemplate.postForEntity(serviceUrl, request , String.class);
		    
		    log.info("Request for {} forwerded to rest api", msisdn);
		    
		    ResponseEntity<String> response = forEntity.get();
		    log.info("Status Code : {}", response.getStatusCode());
		    log.info("Response : {}", response.toString());
		    
		} catch (Exception e) {
			log.info("error calling API for MSISDN {}", customerRegistrationData.getMsisdn());
			log.error("exception in requesting customer details {}", e.getMessage());
		}
		
		
		
	}
}
