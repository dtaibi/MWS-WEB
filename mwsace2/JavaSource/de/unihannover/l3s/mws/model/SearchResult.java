package de.unihannover.l3s.mws.model;

public class SearchResult {

    	public enum SearchType { Web, Image, Video }
    	public enum ViewType { Html, Plain, Table }
	
    	private String query;
        private String[] queries; // for discontinuous
        private String title;
        private String url;
        
		public String getQuery() {
			return query;
		}
		public void setQuery(String query) {
			this.query = query;
		}
		public String[] getQueries() {
			return queries;
		}
		public void setQueries(String[] queries) {
			this.queries = queries;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
        
        
        
    
}
