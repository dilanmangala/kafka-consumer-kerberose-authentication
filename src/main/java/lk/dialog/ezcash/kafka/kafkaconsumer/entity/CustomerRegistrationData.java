package lk.dialog.ezcash.kafka.kafkaconsumer.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "EW_KAFKA_USER_REG_DATA")
public class CustomerRegistrationData {
	
	@Id
	@Column(name = "MSISDN")
	String msisdn;
	@Column(name = "FIRST_NAME")
	String firstName;
	@Column(name = "LAST_NAME")
	String lastName;
	@Column(name = "ADDRESS1")
	String address1;
	@Column(name = "ADDRESS2")
	String address2;
	@Column(name = "ADDRESS3")
	String address3;
	@Column(name = "NEW_NIC")
	String newNic;
	@Column(name = "CHANNEL")
	String activationChannel;
	@Column(name = "REGISTERED_DATE")
	Date registeredDate;
	@Column(name = "TRAFFIC_COLOR")
	String trafficColor;
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getAddress3() {
		return address3;
	}
	public void setAddress3(String address3) {
		this.address3 = address3;
	}
	public String getNewNic() {
		return newNic;
	}
	public void setNewNic(String newNic) {
		this.newNic = newNic;
	}
	public String getActivationChannel() {
		return activationChannel;
	}
	public void setActivationChannel(String activationChannel) {
		this.activationChannel = activationChannel;
	}
	public Date getRegisteredDate() {
		return registeredDate;
	}
	public void setRegisteredDate(Date registeredDate) {
		this.registeredDate = registeredDate;
	}
	public String getTrafficColor() {
		return trafficColor;
	}
	public void setTrafficColor(String trafficColor) {
		this.trafficColor = trafficColor;
	}
	
	
	

}
