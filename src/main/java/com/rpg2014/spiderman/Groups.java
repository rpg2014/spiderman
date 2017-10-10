package com.rpg2014.spiderman;

public enum Groups {
	HANNAHS_BIG_GROUP("30989422","HANNAHS_GROUP_BOT_ID"),
	DADS("16371762","DADS_BOT_ID"),
	TEST("32030393","TEST_BOT_ID"),
	GUYS_GROUP("35059244","GUYS_BOT_ID"),
	CLUB_DROWNING("23993100","DROWNING_BOT_ID");
	
	
	
	
	private String botID;
	private String groupID;
	
	
	private Groups(String groupID,String envName) {
		
		this.groupID = groupID;
		if(Boolean.valueOf(System.getenv("ON_HEROKU"))) {
			this.botID = System.getenv(envName);
		}else {
			botID = SpidermanProperties.getBotID(envName);
		}
	}
	
	public String getBotID() {
		return botID;
	}
	public String getGroupID() {
		return groupID;
	}
	
	
	public static Groups Of(String groupId) {
		Groups[] groups = Groups.values();
		for(Groups g : groups) {
			if(groupId.equals(g.getGroupID())) {
				return g;
			}
		}
		return Groups.TEST;
	}

}
