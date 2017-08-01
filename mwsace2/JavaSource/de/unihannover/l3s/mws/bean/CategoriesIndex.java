package de.unihannover.l3s.mws.bean;

import java.util.HashMap;
import java.util.Map;

public class CategoriesIndex {
	
	public class CategorySettings {
		private boolean included=false;
		private boolean hasnegativeBoost=false;
		private boolean mustnot=false;
		private double positiveBoost=1.0;
		
		public boolean isIncluded() {
			return included;
		}
		public void setIncluded(boolean included) {
			this.included = included;
		}
		public boolean isHasNegativeBoost() {
			return hasnegativeBoost;
		}
		public void setHasNegativeBoost(boolean hasnegativeBoost) {
			this.hasnegativeBoost = hasnegativeBoost;
		}
		public double getPositiveBoost() {
			return positiveBoost;
		}
		public void setPositiveBoost(double positiveBoost) {
			this.positiveBoost = positiveBoost;
		}
		public boolean isMustnot() {
			return mustnot;
		}
		public void setMustnot(boolean mustnot) {
			this.mustnot = mustnot;
		}
		
		
	}
	
	private Map<String, CategorySettings> catmap=new HashMap<String, CategorySettings>() {{
	    put("education",new CategorySettings());
	    put("socialnetworking",new CategorySettings());
	    put("streamingmedia",new CategorySettings());
	    put("searchenginesandportals",new CategorySettings());
	    put("uncategorized",new CategorySettings());
	    put("newsandmedia",new CategorySettings());
	    put("messageboardsandforums",new CategorySettings());
	    put("blogsandpersonal",new CategorySettings());
	    put("shopping",new CategorySettings());
	    put("entertainment",new CategorySettings());
	    put("business",new CategorySettings());
	    put("mediasharing",new CategorySettings());
	    put("personals",new CategorySettings());
	    put("informationtech",new CategorySettings());
	    put("economyandfinance",new CategorySettings());
	    put("travel",new CategorySettings());
	    put("foodandrecipes",new CategorySettings());
	    put("contentserver",new CategorySettings());
	    put("sports",new CategorySettings());
	    put("health",new CategorySettings());
	}};
				
	public Map<String,CategorySettings> getCatMap() {
		return catmap;
	}

}
