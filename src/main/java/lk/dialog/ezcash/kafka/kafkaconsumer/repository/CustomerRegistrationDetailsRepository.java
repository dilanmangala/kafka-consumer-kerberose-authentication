package lk.dialog.ezcash.kafka.kafkaconsumer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import lk.dialog.ezcash.kafka.kafkaconsumer.entity.CustomerRegistrationData;

@Repository
public interface CustomerRegistrationDetailsRepository extends JpaRepository<CustomerRegistrationData, String>{
	
	@Query(value = "select * from ew_kafka_user_reg_data where rownum<=500 order by msisdn", nativeQuery = true)
	public List<CustomerRegistrationData> findCxRecordsByLimiting();
}
