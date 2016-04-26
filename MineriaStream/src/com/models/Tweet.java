package com.models;

import twitter4j.GeoLocation;
import twitter4j.Status;

public class Tweet {

	// Members
	private Status mStatus;
	
	
	public Tweet(Status pStatus) {
		this.mStatus = pStatus;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return this.mStatus.getId();
	}

	/**
	 * @return the user name
	 */
	public String getUserName() {
		return this.mStatus.getUser().getScreenName();
	}

	/**
	 * @return the location where the user profile was created
	 */
	public String getUserLocation() {
		return this.mStatus.getUser().getLocation();
	}
	
	/**
	 * @return the location where the tweet was generated
	 */
	public GeoLocation getLocation() {
		return this.mStatus.getGeoLocation();
	}


	/**
	 * @return the content
	 */
	public String getContent() {
		return this.mStatus.getText();
	}


	@Override
	public String toString()
	{
		return "Id: " 		+ this.getId() + "\n" +
			   "User: " 	+ this.getUserName() + "\n" +
			   "Location: " + this.getUserLocation() + "\n" +
			   "Content: "  + this.getContent();
	}
}
