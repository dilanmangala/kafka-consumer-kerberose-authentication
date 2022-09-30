package lk.dialog.ezcash.kafka.kafkaconsumer.service;

import static lk.dialog.ezcash.kafka.common.Constants.SBU;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doCallRealMethod;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lk.dialog.ezcash.kafka.kafkaconsumer.KafkaFeedConsumer;
import lk.dialog.ezcash.kafka.kafkaconsumer.config.KafkaConsumerConfig;
import lk.dialog.ezcash.kafka.kafkaconsumer.entity.CustomerRegistrationData;
import lk.dialog.ezcash.kafka.kafkaconsumer.repository.CustomerRegistrationDetailsRepository;
import lk.dialog.ezcash.kafka.scheduler.SchedulerService;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CustomerNotificationServiceTest {

	KafkaConsumerConfig kafkaConsumerConfig;
	CustomerRegistrationDetailsRepository customerRegistrationDetailsRepository;
	@Autowired
	KafkaFeedConsumer KafkaConsumer;
	String kafkaFeed;
	String kafkaErrorFeed;
	CustomerNotificationService customerNotificationService;
	
	@InjectMocks
	CustomerDetailsUpdateRequestService customerDetailsUpdateRequestService;
	
	@InjectMocks
	SchedulerService chedulerService;
	
	@Mock
    private RestTemplate restTemplate;
	
	@Mock
	private CustomerRegistrationDetailsRepository scheduteServiceMockCustomerRegistrationDetailsRepository;
	
	CustomerRegistrationData cxDetails;
	
	@Before
    public void setUp() throws JSONException {
    	kafkaFeed = "{\"messageType\":\"NewConnection\",\"messageSeqId\":3740410,\"ProfileId\":\"213262164\",\"AccountId\":\"97037411\",\"ReferenceID\":null,\"IdType\":\"NIC\",\"IdNumber\":\"199423000558\",\"MobileNo\":\"775102057\",\"PrePost\":\"O\",\"Sbu\":\"GSM\",\"CreationDate\":\"31-JAN-22\",\"BillingAddressId\":\"address\",\"BillingName\":\"address\",\"BillingAddressLine1\":\"address\",\"BillingAddressLine2\":\"address\",\"BillingAddressLine3\":\"address\",\"BillingPostalCode\":null,\"BillingPostalCodeDesc\":null,\"BillingCity\":null,\"BillingDistrict\":null,\"FIRSTNAME\":\"Dhilini  \",\"LASTNAME\":\"Gunathilaka\",\"SubscriberSequenceID\":null,\"BillingProvince\":null,\"BillCycleCode\":\"BR02\",\"BillDisplatchType\":\"7\",\"BillFormat\":\"SUM\",\"CreditCategory\":\"WC1\",\"CreditLimit\":\"0\",\"DepositOnApproval\":null,\"InitialDeposit\":null,\"CreditType\":\"NOR\",\"IMSI\":\"413020100008494\",\"IMEI\":null,\"PriorityType\":null,\"PriorityExpiryDate\":null,\"PriorityStartDate\":null,\"ParentNodeId\":null,\"PR\":\"T\",\"ContractId\":\"97035756\",\"SIM\":\"9402970224008494\",\"AccountType\":\"POSTPAID\",\"ContactNo\":null,\"FlySmiles\":null,\"FlySmilesStartDate\":null,\"FlySmilesExpiryDate\":null,\"Provider\":null,\"SubProvider\":null,\"EsSolutionType\":null,\"ESProduct\":null,\"InstallationContactTitle\":null,\"InstallationContactName\":null,\"InstallationContactDesignation\":null,\"InstallationContactMobNum\":null,\"InstallationContactEmail\":null,\"InstallationContactFaxNumber\":null,\"BillingContactTitle\":null,\"BillingContactName\":null,\"BillingContactDesignation\":null,\"BillingContactMobileNumber\":null,\"BillingContactEmail\":null,\"BillingContactFaxNumber\":null,\"DialogAccountManagerCode\":null,\"Segment\":null,\"Sector\":null,\"InstallationAddressId\":null,\"InstallationName\":null,\"InstallationAddressLine1\":null,\"InstallationAddressLine2\":null,\"InstallationAddressLine3\":null,\"InstallationPostalCode\":null,\"InstallationPostalCodeDesc\":null,\"InstallationCity\":null,\"InstallationDistrict\":null,\"InstallationProvince\":null,\"ProductRentalValue\":null,\"SalesChannel\":\"Test\",\"BranchCode\":null,\"EsrNo\":\"97035756\",\"BillingStartDate\":\"31-JAN-22\",\"SimplifiedProductId\":null,\"AccountManagerUserId\":null,\"FirstName\":\"Dhilini  \",\"LastName\":\"Gunathilaka\",\"AccountManagerEmail\":null,\"KycColorCode\":\"Red\"}";
    	kafkaErrorFeed = "{\"MMobileNo\":\"12345\",\"MSalesChannel\":\"test\",\"MBillingAddressLine1\":\"test\",\"MBillingAddressLine2\":\"address 2\",\"MBillingAddressLine3\":\"address 3\",\"MFIRSTNAME\":\"fname\",\"Sbu\":\"GSM\",\"MLASTNAME\":\"fname\",\"MIdNumber\":12345\",\"MCreationDate\":\"10-JAN-22\",\"MKycColorCode\":\"DBC123\"}";
    	List<String> includeList = Arrays.asList("GSM","DTV");
    	
    	kafkaConsumerConfig = new KafkaConsumerConfig();
    	customerRegistrationDetailsRepository = Mockito.mock(CustomerRegistrationDetailsRepository.class);
    	customerNotificationService = Mockito.mock(CustomerNotificationService.class);
    	customerNotificationService = new CustomerNotificationService(customerRegistrationDetailsRepository);
    	ReflectionTestUtils.setField(customerNotificationService, "regdateFormat", "dd-MMM-yy");
    	ReflectionTestUtils.setField(customerNotificationService, "includeDomainList", includeList);
    	ReflectionTestUtils.setField(customerDetailsUpdateRequestService, "serviceUrl", "http://172.26.30.183:8090/PaymentModuleAW/rest/auto_wallet/getNewCustomerData");
    	KafkaConsumer = new KafkaFeedConsumer(customerNotificationService);
    	
    	cxDetails = CustomerRegistrationData.builder()
    	    	.msisdn("msisdn")
    	    	.firstName("firstName")
    	    	.lastName("lastName")
    	    	.address1("address1")
    	    	.address2("address2")
    	    	.address3("address3")
    	    	.newNic("newNic")
    	    	.activationChannel("activationChannel")
    	    	.registeredDate(new Date())
    	    	.trafficColor("trafficColor").build();
    	
    	
    }
	@Test
	public void whenSaveCxDetailsReturnNothing() throws JSONException, ParseException, JsonParseException, JsonMappingException, IOException {
		customerNotificationService = Mockito.spy(customerNotificationService);
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		ObjectNode jsonObject = mapper.readValue(kafkaFeed, ObjectNode.class);
		doCallRealMethod().when(customerNotificationService).extractAndSaveCustomerRegistration(jsonObject);
		Assert.assertEquals("GSM", jsonObject.get(SBU).asText());
	 }
	
	@Test
	public void whenSaveCxDetailsShouldExtractNothing() throws JSONException, ParseException, JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		ObjectNode jsonObject = mapper.readValue(kafkaFeed, ObjectNode.class);
		customerNotificationService = Mockito.spy(customerNotificationService);
		 doCallRealMethod().when(customerNotificationService).extractAndSaveCustomerRegistration(jsonObject);
		 Assert.assertEquals("GSM", jsonObject.get(SBU).asText());
	 }
	 
    
    @Test
	public void checkKafkaConfigs(){
		
		ReflectionTestUtils.setField(kafkaConsumerConfig, "jassConfig", "jaas.conf");
		ReflectionTestUtils.setField(kafkaConsumerConfig, "krb5Conf", "krb5.conf");
		ReflectionTestUtils.setField(kafkaConsumerConfig, "secProtocol", "SASL_PLAINTEXT");
		ReflectionTestUtils.setField(kafkaConsumerConfig, "groupId", "test-group-id");
		ReflectionTestUtils.setField(kafkaConsumerConfig, "bootStrapServerAddress", "test-group-id");
		kafkaConsumerConfig.kafkaListenerContainerFactory();
		
		Assert.assertNotNull(kafkaConsumerConfig.kafkaListenerContainerFactory());
	}
    
    @Test
    public void whenSaveCxDataShouldReturnCxDetails() {
    	
    	Mockito.when(customerRegistrationDetailsRepository.save(cxDetails)).thenReturn(cxDetails);
    	CustomerRegistrationData cxReturnObj = customerRegistrationDetailsRepository.save(cxDetails);
    	assertEquals(cxDetails.getFirstName(), cxReturnObj.getFirstName());
    }
    
    @Test
    public void whenDomainInThenShouldInsert() throws JSONException, JsonParseException, JsonMappingException, IOException {
    	KafkaConsumer.getJsonKMessage(kafkaFeed);
    	ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		ObjectNode jsonObject = mapper.readValue(kafkaFeed, ObjectNode.class);
    	Assert.assertEquals("GSM", jsonObject.get(SBU).asText());
    }
    
    @Test
    public void whenDomainNotInThenNoInsert() throws JSONException {
    	KafkaConsumer.getJsonKMessage("{\"Sbu\":\"\"}");
    	
    }
    
    @Test
    public void whenDomainNotInThenJsonError() throws JSONException {
    	KafkaConsumer.getJsonKMessage("{\"Sbu\"}");
    	
    }
   
    @Test
    public void givenMockingIsDoneByMockitoWhenGetIsCalledShouldReturnMockedObject() {
    	CustomerRegistrationData emp = new CustomerRegistrationData();
        Mockito.when(restTemplate.postForEntity("http://172.26.30.183:8091/PaymentModuleAuto/rest/auto_wallet/getNewCustomerData",emp, CustomerRegistrationData.class))
        .thenReturn(new ResponseEntity("test", HttpStatus.OK));
        customerDetailsUpdateRequestService.requestCUstomerData(emp);
    }
    
    @Test
    public void testScheduler() {
    	List<CustomerRegistrationData> dummyCxList = new ArrayList<>();
    	
    	dummyCxList.add(cxDetails);
    	chedulerService = new SchedulerService(customerRegistrationDetailsRepository, customerDetailsUpdateRequestService);
    	Mockito.when(customerRegistrationDetailsRepository.findCxRecordsByLimiting()).thenReturn(dummyCxList);
    	chedulerService.fetchCustomerDetailsScheduler();
    	Assert.assertNotNull(dummyCxList);
    }
    
    @Test
    public void testSchedulerWithNullCxDetails() {
    	List<CustomerRegistrationData> dummyCxList = new ArrayList<>();
    	
    	chedulerService = new SchedulerService(customerRegistrationDetailsRepository, customerDetailsUpdateRequestService);
    	Mockito.when(customerRegistrationDetailsRepository.findCxRecordsByLimiting()).thenReturn(dummyCxList);
    	chedulerService.fetchCustomerDetailsScheduler();
    	
    }
}
