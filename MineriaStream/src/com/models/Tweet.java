package com.models;

import twitter4j.Status;

public class Tweet {

	// Members
	private long mId;
	private String mUserName;
	private String mUserLocation;
	private String mContent;
	
	
	public Tweet(){
	}
	
	public Tweet(Status pStatus) {
		this.mId = pStatus.getId();
		this.mUserLocation = pStatus.getUser().getLocation();
		this.mContent  = pStatus.getText();
		this.mUserName = pStatus.getUser().getScreenName();
	}



	/**
	 * @return the id
	 */
	public long getId() {
		return mId;
	}

	/**
	 * @param pId the id to set
	 */
	public void setId(long pId) {
		this.mId = pId;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return mUserName;
	}

	/**
	 * @param pUserName the userName to set
	 */
	public void setUserName(String pUserName) {
		this.mUserName = pUserName;
	}

	/**
	 * @return the userLocation
	 */
	public String getUserLocation() {
		return mUserLocation;
	}

	/**
	 * @param pUserLocation the userLocation to set
	 */
	public void setUserLocation(String pUserLocation) {
		this.mUserLocation = pUserLocation;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return mContent;
	}

	/**
	 * @param pContent the content to set
	 */
	public void setContent(String pContent) {
		this.mContent = pContent;
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
