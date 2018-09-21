package me.skorrloregaming.impl;

public class IpLocationQuery {
	private String country, state, city, isp, ipAddress, endpoint;
	private String[] accounts;

	public IpLocationQuery(String country, String state, String city, String isp, String ipAddress, String endpoint, String[] accounts) {
		this.setCountry(country);
		this.setState(state);
		this.setCity(city);
		this.setIsp(isp);
		this.setIpAddress(ipAddress);
		this.setEndpoint(endpoint);
		this.setAccounts(accounts);
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getIsp() {
		return isp;
	}

	public void setIsp(String isp) {
		this.isp = isp;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String[] getAccounts() {
		return accounts;
	}

	public void setAccounts(String[] accounts) {
		this.accounts = accounts;
	}
}
