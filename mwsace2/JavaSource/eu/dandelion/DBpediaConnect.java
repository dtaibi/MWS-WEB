package eu.dandelion;

import java.io.UnsupportedEncodingException;

import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.QuerySolutionMap;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.Syntax;
import org.apache.jena.rdf.model.impl.ResourceImpl;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;

import de.unihannover.l3s.mws.dao.DbpediacacheDao;
import de.unihannover.l3s.mws.model.Dbpediacache;


public class DBpediaConnect {
	String sparqlEndpoint = "http://dbpedia.org/sparql";

	  // get expression values for uniprot acc Q16850
	  
	  
	  public DBpediaConnect() {
	    
	  }
	  
	  public String getEng(String uri) {
		try {
			uri=java.net.URLDecoder.decode(uri, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (uri.contains("http://en.wikipedia.org/wiki"))
			return java.net.URLDecoder.decode(uri.replace("http://en.wikipedia.org/wiki","http://dbpedia.org/resource"));
		  
		uri=uri.replace("http://de.wikipedia.org/wiki", "http://de.dbpedia.org/resource").replace("http://it.wikipedia.org/wiki", "http://it.dbpedia.org/resource").replace("http://fr.wikipedia.org/wiki", "http://fr.dbpedia.org/resource");
		DbpediacacheDao dbpediaDAO=new DbpediacacheDao();
		Dbpediacache dbpediacache=dbpediaDAO.getCache(uri);
		
		if (dbpediacache==null){ 
		// create the Jena query using the ARQ syntax (has additional support for SPARQL federated queries)
		String sparqlQuery="";
		sparqlQuery = "select distinct ?Concept where {?Concept <http://www.w3.org/2002/07/owl#sameAs> <"+uri+"> . FILTER regex(?Concept, \"^http://dbpedia.org\") } LIMIT 100";
		// System.out.println(sparqlQuery);
		try {
		  Query query = QueryFactory.create(sparqlQuery, Syntax.syntaxSPARQL) ;
		    // we want to bind the ?uniprotAccession variable in the query
		    // to the URI for Q16850 which is http://purl.uniprot.org/uniprot/Q16850
		    //QuerySolutionMap querySolutionMap = new QuerySolutionMap();
		    //querySolutionMap.add("uniprotAccession", new ResourceImpl("http://purl.uniprot.org/uniprot/Q16850"));
		    //ParameterizedSparqlString parameterizedSparqlString = new ParameterizedSparqlString(query.toString(), querySolutionMap);

		    QueryEngineHTTP httpQuery = new QueryEngineHTTP(sparqlEndpoint,query); // parameterizedSparqlString.asQuery());
		    // execute a Select query
		    ResultSet results = httpQuery.execSelect();
		    while (results.hasNext()) {
		      QuerySolution solution = results.next();
		      // get the value of the variables in the select clause
		      String expressionValue = solution.get("Concept").asResource().getURI();
		      // String pValue = solution.get("pvalue").asLiteral().getLexicalForm();
		      // print the output to stdout
		      expressionValue=java.net.URLDecoder.decode(expressionValue);
		      dbpediaDAO.addCache(expressionValue, uri);
		      return expressionValue;
		    }
		    httpQuery.close();
		    String urinew=java.net.URLDecoder.decode(uri.replace("http://de.dbpedia.org/resource/", "de:").replace("http://it.dbpedia.org/resource/", "it:").replace("http://fr.dbpedia.org/resource/", "fr:"));
		    dbpediaDAO.addCache(urinew, uri);
		    return urinew;
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
		}else
			return dbpediacache.getDbpedia();
		
	  }

	  public static void main(String[] args) {
	    new DBpediaConnect();
	  }
}
