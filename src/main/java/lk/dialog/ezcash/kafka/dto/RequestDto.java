package lk.dialog.ezcash.kafka.dto;

import java.util.Date;
/**
 * 
 * @author Dilan_105248
 *
 */
public class RequestDto {

	private String msisdn;

	private String firstName;

	private String lastName;

	private String address1;

	private String address2;
	
	private String address3;

	private String newNic;

	private String trafficColor;

	private Date registeredDate;

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

	public String getTrafficColor() {
		return trafficColor;
	}

	public void setTrafficColor(String trafficColor) {
		this.trafficColor = trafficColor;
	}

	public Date getRegisteredDate() {
		return registeredDate;
	}

	public void setRegisteredDate(Date registeredDate) {
		this.registeredDate = registeredDate;
	}
	
	
}
