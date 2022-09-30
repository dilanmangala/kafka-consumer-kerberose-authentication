package lk.dialog.ezcash.kafka.kafkaconsumer.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {
	
	@Value(value = "${booststrap.server.config}")
    private String bootStrapServerAddress;
	
	@Value("${kafca.jass}")
	private String jassConfig;

	@Value("${kafca.krb}")
	private String krb5Conf;
	
	@Value("${kafca.group.id}")
	private String groupId;
	
	@Value("${kafca.security.protocol}")
	private String secProtocol;
	
	@Bean
	public ConsumerFactory<String, String> userFactory() {
		
		System.setProperty("java.security.auth.login.config", jassConfig);
		System.setProperty("java.security.krb5.conf", krb5Conf);
		
		Map<String, Object> configs = new HashMap<>();
		configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServerAddress);
		configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configs.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		configs.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, secProtocol);

		
		return new DefaultKafkaConsumerFactory<>(configs);
	}
	
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(userFactory());
		
		return factory;
	}
}
