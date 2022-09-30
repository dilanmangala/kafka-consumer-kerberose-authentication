package lk.dialog.ezcash.kafka.kafkaconsumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lk.dialog.ezcash.kafka.kafkaconsumer.service.CustomerNotificationService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaFeedConsumer {
	
	CustomerNotificationService customerNotificationService;
	public KafkaFeedConsumer(CustomerNotificationService customerNotificationService) {
		this.customerNotificationService = customerNotificationService;
	}

	@KafkaListener(topics = "${kafka.topic}", containerFactory = "kafkaListenerContainerFactory")
	public void getJsonKMessage(String jsonStr) {
		log.info("Request received : {}", jsonStr);
		
		try {
			log.info("start listening kafka feed");
			ObjectMapper mapper = new ObjectMapper();
			mapper.setSerializationInclusion(Include.NON_NULL);
			ObjectNode jsonObject = mapper.readValue(jsonStr, ObjectNode.class);
			customerNotificationService.extractAndSaveCustomerRegistration(jsonObject);
		} catch (Exception ex) {
			log.error("Exception in Kafka listner {}", ex.getMessage());
			ex.fillInStackTrace();
		}

	}
}
