package org.geoazul.ecommerce.view.shopping;


import com.fasterxml.jackson.databind.JsonNode;

public class ApiData {

		private Boolean success;
	    private Boolean timeseries;
	    private String start_date;
	    private String end_date;
	    private String base;
	    
	    
	    private JsonNode rates;
	    
	    private String unit;
	    
	    
		public Boolean getSuccess() {
			return success;
		}
		public void setSuccess(Boolean success) {
			this.success = success;
		}
		public Boolean getTimeseries() {
			return timeseries;
		}
		public void setTimeseries(Boolean timeseries) {
			this.timeseries = timeseries;
		}
		public String getStart_date() {
			return start_date;
		}
		public void setStart_date(String start_date) {
			this.start_date = start_date;
		}
		public String getEnd_date() {
			return end_date;
		}
		public void setEnd_date(String end_date) {
			this.end_date = end_date;
		}
		public String getBase() {
			return base;
		}
		public void setBase(String base) {
			this.base = base;
		}
		
		public JsonNode getRates() {
			return rates;
		}
		public String getUnit() {
			return unit;
		}
		public void setUnit(String unit) {
			this.unit = unit;
		}
		public void setRates(JsonNode rates) {
			this.rates = rates;
		}
		
		
		
		
	
	
		
	    
	    
	    
	    
	
	    
		
	    
}
