package me.skorrloregaming.impl;

import org.json.JSONObject;

public class Vote {
	private String serviceName;
	private String username;
	private String address;
	private String timeStamp;
	private final long localTimestamp;

	@Deprecated
	public Vote() {
		localTimestamp = System.currentTimeMillis();
	}

	public Vote(String serviceName, String username, String address, String timeStamp) {
		this(serviceName, username, address, timeStamp, System.currentTimeMillis());
	}

	public Vote(String serviceName, String username, String address, String timeStamp, long localTimestamp) {
		this.serviceName = serviceName;
		this.username = username;
		this.address = address;
		this.timeStamp = timeStamp;
		this.localTimestamp = localTimestamp;
	}

	private static String getTimestamp(JSONObject object) {
		try {
			return Long.toString(object.getLong("timestamp"));
		} catch (Exception e) {
			return object.getString("timestamp");
		}
	}

	public Vote(JSONObject jsonObject) {
		this(jsonObject.getString("serviceName"), jsonObject.getString("username"), jsonObject.getString("address"), getTimestamp(jsonObject), (jsonObject.has("localTimestamp") ? jsonObject.getLong("localTimestamp") : System.currentTimeMillis()));
	}

	@Override
	public String toString() {
		return "Vote (from:" + serviceName + " username:" + username + " address:" + address + " timeStamp:" + timeStamp + " localTimestamp:" + localTimestamp + ")";
	}

	@Deprecated
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceName() {
		return serviceName;
	}

	@Deprecated
	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	@Deprecated
	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return address;
	}

	@Deprecated
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public long getLocalTimestamp() {
		return localTimestamp;
	}

	public JSONObject serialize() {
		JSONObject ret = new JSONObject();
		ret.put("serviceName", serviceName);
		ret.put("username", username);
		ret.put("address", address);
		ret.put("timestamp", timeStamp);
		ret.put("localTimestamp", localTimestamp);
		return ret;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Vote vote = (Vote) o;
		if (!serviceName.equals(vote.serviceName))
			return false;
		if (!username.equals(vote.username))
			return false;
		if (!address.equals(vote.address))
			return false;
		return timeStamp.equals(vote.timeStamp);
	}

	@Override
	public int hashCode() {
		int result = serviceName.hashCode();
		result = 31 * result + username.hashCode();
		result = 31 * result + address.hashCode();
		result = 31 * result + timeStamp.hashCode();
		return result;
	}
}
