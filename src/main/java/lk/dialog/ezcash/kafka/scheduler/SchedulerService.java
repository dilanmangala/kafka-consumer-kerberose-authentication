package lk.dialog.ezcash.kafka.scheduler;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lk.dialog.ezcash.kafka.kafkaconsumer.entity.CustomerRegistrationData;
import lk.dialog.ezcash.kafka.kafkaconsumer.repository.CustomerRegistrationDetailsRepository;
import lk.dialog.ezcash.kafka.kafkaconsumer.service.CustomerDetailsUpdateRequestService;
import lombok.extern.slf4j.Slf4j;
/**
 * Scheduler Implementation
 * @author Dilan_105248
 *
 */
@Service
@Slf4j
public class SchedulerService {
	
	CustomerRegistrationDetailsRepository customerRegistrationDetailsRepository;
	CustomerDetailsUpdateRequestService customerDetailsUpdateRequestService;
	
	public SchedulerService(CustomerRegistrationDetailsRepository customerRegistrationDetailsRepository,
			CustomerDetailsUpdateRequestService customerDetailsUpdateRequestService) {
		this.customerRegistrationDetailsRepository = customerRegistrationDetailsRepository;
		this.customerDetailsUpdateRequestService = customerDetailsUpdateRequestService;
	}
	

	@Scheduled(cron = "${cxdata.request.call.scheduler}")
	public void fetchCustomerDetailsScheduler() {
		try {
			
			log.info("start fetching customer details scheduler");
			List<CustomerRegistrationData> getCxDetailsFindByDomain = customerRegistrationDetailsRepository
					.findCxRecordsByLimiting();
			if (!getCxDetailsFindByDomain.isEmpty()) {
				log.info("{}", getCxDetailsFindByDomain.size());
				executeCustomerDetails(getCxDetailsFindByDomain);
			} else {
				log.info("no records for requesting in the database");
			}
			log.info("end fetching customer details scheduler");
		} catch (Exception e) {
			log.error("exception in fetch customer details scheduler {}", e.getMessage());
		}
	}

	/**
	 * This method is used to execute fetched customer details
	 * 
	 * @param cxDetailsList, this list contains fetched customer details
	 */
	private void executeCustomerDetails(List<CustomerRegistrationData> cxDetailsList) {
		log.info("start executing customer details");
		for (CustomerRegistrationData cxItems : cxDetailsList) {
			customerDetailsUpdateRequestService.requestCUstomerData(cxItems);
		}
		log.info("start executing customer details");
	}
}
