package lk.dialog.ezcash.kafka.kafkaconsumer.service;

import static lk.dialog.ezcash.kafka.common.Constants.BILLING_ADDRESS_LINE_1;
import static lk.dialog.ezcash.kafka.common.Constants.BILLING_ADDRESS_LINE_2;
import static lk.dialog.ezcash.kafka.common.Constants.BILLING_ADDRESS_LINE_3;
import static lk.dialog.ezcash.kafka.common.Constants.CREATION_DATE;
import static lk.dialog.ezcash.kafka.common.Constants.FIRST_NAME;
import static lk.dialog.ezcash.kafka.common.Constants.ID_NUMBER;
import static lk.dialog.ezcash.kafka.common.Constants.KYC_COLOR_CODE;
import static lk.dialog.ezcash.kafka.common.Constants.LAST_NAME;
import static lk.dialog.ezcash.kafka.common.Constants.MOBILE_NO;
import static lk.dialog.ezcash.kafka.common.Constants.SALES_CHANNEL;
import static lk.dialog.ezcash.kafka.common.Constants.SBU;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;

import lk.dialog.ezcash.kafka.kafkaconsumer.entity.CustomerRegistrationData;
import lk.dialog.ezcash.kafka.kafkaconsumer.repository.CustomerRegistrationDetailsRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerNotificationService {

	@Value(value = "${registration.date.format}")
	private String regdateFormat;

	@Value("#{'${include.domain.list}'.split(',')}")
	private List<String> includeDomainList;
	
	CustomerRegistrationDetailsRepository customerRegistrationDetailsRepository;

	public CustomerNotificationService(CustomerRegistrationDetailsRepository customerRegistrationDetailsRepository) {
		this.customerRegistrationDetailsRepository = customerRegistrationDetailsRepository;
	}
	
	/**
	 * This method is used to extract and save kafka feed data
	 * @param jsonObject
	 * @throws ParseException
	 */
	@Async
	public void extractAndSaveCustomerRegistration(ObjectNode jsonObject) throws ParseException {
		log.info("start extracting and saving customer registration details");
		if (includeDomainList.contains(jsonObject.get(SBU).asText())) {
			CustomerRegistrationData customerRegistrationData = extractCxDetails(jsonObject);
			if (customerRegistrationData != null && customerRegistrationData.getMsisdn() != null) {
				log.debug("start Save cx reg data : {}", customerRegistrationData.toString());
				customerRegistrationDetailsRepository.save(customerRegistrationData);
				log.debug("end Save cx reg data : {}", customerRegistrationData.toString());
				log.info("Successfully saved cx registration details for {}", customerRegistrationData.getMsisdn());
			}
		} else {
			log.info("no record for inserting in the kafka feed for domains - {}",
					String.join(",", includeDomainList));
		}
		log.info("end extrating and saving customer registration details");
		

	}
	
	/**
	 * This method is used to extract kafka feed and set extracted values to customer object
	 * @param jsonObject
	 * @return
	 * @throws ParseException
	 */
	public CustomerRegistrationData extractCxDetails(ObjectNode jsonObject) throws ParseException {
		log.info("start extrating kafka feed data");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(regdateFormat);
		CustomerRegistrationData customerRegistrationData = new CustomerRegistrationData();

		if (isNullAndHas(MOBILE_NO, jsonObject)) {
			customerRegistrationData.setMsisdn(jsonObject.get(MOBILE_NO).asText());
		}
		log.info("exracted msisnd for registration details {}", customerRegistrationData.getMsisdn());

		if (isNullAndHas(SALES_CHANNEL, jsonObject)) {
			customerRegistrationData.setActivationChannel(jsonObject.get(SALES_CHANNEL).asText());
		}
		if (isNullAndHas(BILLING_ADDRESS_LINE_1, jsonObject)) {
			customerRegistrationData.setAddress1(jsonObject.get(BILLING_ADDRESS_LINE_1).asText());
		}
		if (isNullAndHas(BILLING_ADDRESS_LINE_2, jsonObject)) {
			customerRegistrationData.setAddress2(jsonObject.get(BILLING_ADDRESS_LINE_2).asText());
		}
		if (isNullAndHas(BILLING_ADDRESS_LINE_3, jsonObject)) {
			customerRegistrationData.setAddress3(jsonObject.get(BILLING_ADDRESS_LINE_3).asText());
		}
		if (isNullAndHas(FIRST_NAME, jsonObject)) {
			customerRegistrationData.setFirstName(jsonObject.get(FIRST_NAME).asText());
		}
		if (isNullAndHas(LAST_NAME, jsonObject)) {
			customerRegistrationData.setLastName(jsonObject.get(LAST_NAME).asText());
		}
		if (isNullAndHas(ID_NUMBER, jsonObject)) {
			customerRegistrationData.setNewNic(jsonObject.get(ID_NUMBER).asText());
		}
		if (isNullAndHas(CREATION_DATE, jsonObject)) {
			String createDate = jsonObject.get(CREATION_DATE).asText();
			Date dateCreateDate = simpleDateFormat.parse(createDate);
			customerRegistrationData.setRegisteredDate(dateCreateDate);
		}
		if (isNullAndHas(KYC_COLOR_CODE, jsonObject)) {
			customerRegistrationData.setTrafficColor(jsonObject.get(KYC_COLOR_CODE).asText());
		}
		
		log.info("end extrating kafka feed data");
		return customerRegistrationData;
	}
	

	/**
	 * This method is used to check key is containing json object and not null
	 * 
	 * @param key
	 * @param jsonObject
	 * @return
	 */
	private boolean isNullAndHas(String key, ObjectNode jsonObject) {
		return jsonObject.has(key) && !jsonObject.get(key).asText().equals("null") && !"".equals(jsonObject.get(key).asText());
	}
}
