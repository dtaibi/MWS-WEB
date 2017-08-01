/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package de.unihannover.l3s.mws.bean;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.unihannover.l3s.es.ESearchDataObject;
import de.unihannover.l3s.es.ESearchPost;
import de.unihannover.l3s.mws.bean.CategoriesIndex.CategorySettings;
import de.unihannover.l3s.mws.dao.TrackDao;
import de.unihannover.l3s.mws.dao.UtenteDao;
import de.unihannover.l3s.mws.model.Generalsettings;
import de.unihannover.l3s.mws.model.SearchResult;
import de.unihannover.l3s.mws.model.SearchWebResult;
import de.unihannover.l3s.mws.model.Track;
import info.debatty.java.stringsimilarity.Jaccard;
import de.unihannover.l3s.mws.util.TextManager;


@ManagedBean(name="archivesearchstudent2")
@ViewScoped
public class ArchiveSearchStudent2 implements Serializable {

	String[] typelist=new String[]{"organisation","socialperson","organization","work","creativework","person","naturalperson","location","place","populatedplace","company","settlement","city","administrativeregion","region","administrativearea","writtenwork","officeholder","musicgroup","architecturalstructure","film","movie","periodicalliterature","musicalwork","software","artist","country","group","band","televisionshow","broadcaster","infrastructure","website","webpage","newspaper","educationalinstitution","educationalorganization","musicalartist","routeoftransportation","single","athlete","collegeoruniversity","university","fictionalcharacter","televisionstation","road","magazine","writer","album","musicalbum","sportsteam","species","building","bank","bankorcreditunion","event","eukaryote","societalevent","politician","bibo","book","ontology","naturalplace","governmentagency","animal","royalty","broadcastnetwork","park","bodyofwater","scientist","food","functionalsubstance","militaryconflict","stream","device","river","riverbodyofwater","philosopher","comedian","disease","legislature","ethnicgroup","product","soccerclub","politicalparty","comicscharacter","chemicalsubstance","chemicalobject","mammal","town","videogame","plant","informationappliance","activity","meanoftransportation","designedartifact","timeinterval","holiday","sportfacility","profitorganisation","gridironfootballplayer","radiostation","stadium","stadiumorarena","sport","televisionepisode","tvepisode","baseballplayer","programminglanguage","school","airport","americanfootballteam","radioprogram","americanfootballplayer","drug","song","musicrecording","bridge","topicalconcept","concept","island","soccerplayer","genre","musicgenre","sportsevent","sportsleague","landmarksorhistoricalbuildings","soapcharacter","recordlabel","station","historicplace","economist","cleric","automobile","militaryperson","wrestler","publisher","convention","language","basketballteam","criminal","president","award","journalist","militaryunit","comicscreator","chemicalcompound","basketballplayer","play","venue","colour","congressman","celestialbody","baseballteam","comic","museum","beverage","weapon","bird","protectedarea","tradeunion","soccerleague","senator","chef","wintersportplayer","anatomicalstructure","presenter","actor","governor","lake","lakebodyofwater","saint","musical","icehockeyplayer","planet","airline","fashiondesigner","christianbishop","radiohost","religious","currency","publictransitsystem","insect","judge","hockeyteam","manga","village","memberofparliament","tennisplayer","shoppingmall","shoppingcenter","mountainrange","model","cartoon","rugbyclub","coach","soccermanager","aircraft","ship","game","railwayline","motorsportracer","mountain","clericaladministrativeregion","diocese","anime","collegecoach","golfplayer","academicjournal","monarch","cricketer","racingdriver","artificialsatellite","satellite","baseballleague","continent","snookerplayer","horse","racehorse","mixedmartialartsevent","festival","noble","filmfestival","murderer","comedygroup","militarystructure","tournament","martialartist","library","rugbyplayer","televisionseason","fish","boxer","nationalfootballleagueevent","powerstation","worldheritagesite","earthquake","naturalevent","olympics","theatre","prison","biomolecule","protein","wineregion","crustacean","australianrulesfootballplayer","adultactor","cultivatedvariety","cyclist","wrestlingevent","mollusca","golfleague","hospital","hotel","mayor","nascardriver","architect","swimmer","dam","primeminister","artwork","astronaut","chessplayer","racecourse","racetrack","rugbyleague","buscompany","religiousbuilding","mineral","conifer","historicbuilding","formulaoneracer","sportsseason","restaurant","reptile","sportsteamseason","skiarea","skiresort","basketballleague","womenstennisassociationtournament","floweringplant","lawfirm","businessperson","sea","seabodyofwater","pope","soccertournament","fungus","volcano","footballleagueseason","nationalfootballleagueseason","brain","ambassador","amusementparkattraction","fashion","gymnast","arachnid","election","race","cricketteam","australianfootballteam","beautyqueen","golftournament","name","spacestation","givenname","medician","grape","unitofwork","situation","playboyplaymate","case","legalcase","supremecourtoftheunitedstatescase","tennistournament","locomotive","cardinal","monument","photographer","rocket","timeperiod","year","constellation","gaelicgamesplayer","hollywoodcartoon","rollercoaster","horserace","americanfootballcoach","curler","screenwriter","pokerplayer","figureskater","canal","americanfootballleague","mountainpass","volleyballplayer","animangacharacter","formulaoneteam","artistdiscography","televisionhost","galaxy","star","motorcycle","motorcyclerider","dartsplayer","winery","embryology","painter","amphibian","engineer","jockey","bacteria","enzyme","roadjunction","cyclingrace","comicstrip","bone","crater","train","skier","railwaystation","historian","roadtunnel","automobileengine","college","engine","biologicaldatabase","brewery","database","icehockeyleague","informationobject","eurovisionsongcontestentry","lacrosseplayer","grandprix","spaceshuttle","ncaateamseason","bodybuilder","organisationmember","sportsteammember","valley","guitarist","instrumentalist","badmintonplayer","golfcourse","glacier","musicfestival","olympicresult","sportcompetitionresult","nerve","cheese","garden","snookerchamp","muscle","spacecraft","castle","tennisleague","canadianfootballteam","lighthouse","tower","horsetrainer","beachvolleyballplayer","tunnel","greenalga","mythologicalfigure","siteofspecialscientificinterest","speedwayrider","poem","fern","cyclingteam","project","researchproject","planexecution","squashplayer","tabletennisplayer","amateurboxer","horserider","cave","footballmatch","nationalcollegiateathleticassociationathlete","entomologist","poet","lacrosseleague","handballplayer","artery","skater","ligament","waterride","classicalmusicartist","railwaytunnel","motorsportseason","classicalmusiccomposition","netballplayer","archaea","speedwayteam","cycad","vein","canadianfootballleague","chancellor","olympicevent","voiceactor","inlinehockeyleague","baronet","britishroyalty","launchpad","moss","surname","sumowrestler","volleyballcoach","cricketground","autoracingleague","canoeist","clubmoss","handballteam","rower","speedwayleague","volleyballleague","asteroid","fieldhockeyleague","gnetophytes","pololeague","softballleague","gene","humangene"};
	private boolean usesub=false;
	private boolean exactmatch=false;
	private boolean withtitle=false;
	private boolean useseed=false;
	private boolean useanntitle=false;
	private boolean significantterm=false;
	private String searchquery="";
	private Double jthreshold=0.8;
	private Double negativeboost=0.0;
	private String typeselected=null;
	
	private List<String> searchterms;
	@ManagedProperty(value="#{user}")
	private User user;

	private String name;
	
	private String tagclouddata="";
	private String tagclouddatait="";
	private String tagclouddataes="";
	private String tagclouddataen="";
	private ArrayList<SearchResult> searchResult1=null;
	private String radiowebnews;
	
	private String clickedTag="";
	private String clickedLink="";
	private ArrayList<SearchResult> searchResultEn=null;
	private ArrayList<SearchResult> searchResultWeb=null;
	private Map<String,ArrayList<SearchResult>> searchResultWebMap=new HashMap<String, ArrayList<SearchResult>>();
	
	ArrayList<SearchResult> resultItList=null;
	
	WikipediaAnnotation wkann=new WikipediaAnnotation();
	
	Double minconfidence;
	Integer resultnumber=10;
	
	Double boostTitle;
	Double boostAnn;
	Double boostAnnTitle;
	Double boostContent;
	
	ArrayList<String> excludesel = new ArrayList<String>();
	ArrayList<String> includeFullText = new ArrayList<String>();
	
	
	Map<String,Integer> categoryFreq= new HashMap<String,Integer>();
	Map<String,Integer> categoryFreqDE= new HashMap<String,Integer>();
	ArrayList<String> categoryFreqList = new ArrayList<String>();
	ArrayList<String> categoryFreqListDE = new ArrayList<String>();
	Map<String, Integer> conceptFreqDe=new HashMap<String, Integer>();
	Map<String, Integer> conceptFreq=new HashMap<String, Integer>();
	
	private String resultId="0";
	
	private String siteSetId;
	private String siteText;
	
	private String searchsettings="";
	
	private List<String> teasers;
	private String scenepopup;
	
	private String sformsel;
	
	//private String startdate="20111203";
	//private String enddate="20121009";
	
	private CategoriesIndex catmap=new CategoriesIndex();
	private String modifier="none";
	private String tannomodifier="none";
	private String score_mode;
	private String boost_mode;
	private Double factor;
	private Double tannofactor;
	private boolean functionscoreInTitle=false;
	private String field_value_factor;
	private Parameters funcscore=new Parameters();
	
	private Date sdate=new Date(111,11,3);
	private Date edate=new Date(112,9,9);
	
	
	
	public boolean isSignificantterm() {
		return significantterm;
	}

	public void setSignificantterm(boolean significantterm) {
		this.significantterm = significantterm;
	}

	public List<String> getTypelist() {
		return Arrays.asList(this.typelist);
	}
	
	public String getTypeselected() {
		return typeselected;
	}


	public void setTypeselected(String typeselected) {
		this.typeselected = typeselected;
	}


	public Date getEdate() {
		return edate;
	}


	public void setEdate(Date edate) {
		this.edate = edate;
	}


	public Date getSdate() {
		return sdate;
	}


	public void setSdate(Date sdate) {
		this.sdate = sdate;
	}


	public String getTannomodifier() {
		return tannomodifier;
	}


	public void setTannomodifier(String tannomodifier) {
		this.tannomodifier = tannomodifier;
	}


	public Double getTannofactor() {
		return tannofactor;
	}


	public void setTannofactor(Double tannofactor) {
		this.tannofactor = tannofactor;
	}


	public String getField_value_factor() {
		return field_value_factor;
	}


	public void setField_value_factor(String field_value_factor) {
		this.field_value_factor = field_value_factor;
	}


	public Parameters getFuncscore() {
		return funcscore;
	}


	public void setFuncscore(Parameters funcscore) {
		this.funcscore = funcscore;
	}


	public String getModifier() {
		return modifier;
	}


	public void setModifier(String modifier) {
		this.modifier = modifier;
	}


	public String getScore_mode() {
		return score_mode;
	}


	public void setScore_mode(String score_mode) {
		this.score_mode = score_mode;
	}


	public String getBoost_mode() {
		return boost_mode;
	}


	public void setBoost_mode(String boost_mode) {
		this.boost_mode = boost_mode;
	}


	public Double getFactor() {
		return factor;
	}


	public void setFactor(Double factor) {
		this.factor = factor;
	}


	public boolean isFunctionscoreInTitle() {
		return functionscoreInTitle;
	}


	public void setFunctionscoreInTitle(boolean functionscoreInTitle) {
		this.functionscoreInTitle = functionscoreInTitle;
	}


	public Double getNegativeboost() {
		return negativeboost;
	}


	public void setNegativeboost(Double negativeboost) {
		this.negativeboost = negativeboost;
	}


	public Map<String,CategorySettings> getCatmap() {
		return catmap.getCatMap();
	}

	public void saveCats(){
		
	}
	
	public String getSearchquery() {
		searchquery="";
		String filter="";
		String should="";
		String mustnot="";
		String must="";
		
		ArrayList<String> positivecat=new ArrayList<String>();
		ArrayList<String> notincluded=new ArrayList<String>();
		ArrayList<String> filterpart=new ArrayList<String>();
		ArrayList<String> pospart=new ArrayList<String>();
		ArrayList<String> mustnotpart=new ArrayList<String>();
		
		for (String cat : this.getCatmap().keySet()){
			CategorySettings cset=this.getCatmap().get(cat);
			if (cset.isIncluded()){
				if(cset.isMustnot())
					notincluded.add("{\"term\":{\"category\":\""+cat+"\"}}");
				else
				{
					if (cat.compareTo("education")!=0)
						positivecat.add("{\"match\":{\"category\":{\"query\":\""+cat+"\",\"boost\":"+cset.getPositiveBoost()+"}}}");
					else {
						positivecat.add("{\"match\":{\"category\":{\"query\":\""+cat+"\",\"boost\":"+cset.getPositiveBoost()+"}}}");
						positivecat.add("{\"match\":{\"category\":{\"query\":\""+cat+"d\",\"boost\":"+cset.getPositiveBoost()+"}}}");
					}
				}
			}	
		}
		
		
		
		String sub="";
		if (this.exactmatch)
			sub=".sub";
		
		String term="terms";
		if (this.significantterm)
			term="significant_terms";
		
		String annotation="annotation";
		String content="content";
		if (this.isWithtitle()){
			annotation="titleAnno";
			content="title";
		}
		
		
		
		SimpleDateFormat dt1 = new SimpleDateFormat("yyyyMMdd");
		String range="{\"range\":{\"timestamp.sub\":{\"gte\":"+dt1.format(this.sdate)+"000000,\"lte\":"+dt1.format(this.edate)+"000000}}}";
		
		String seed="{\"term\":{\"pldType\":\"seed\"}}";
		if (this.isUseseed())
			must="\"must\":["+range+","+seed+"]";
		else
			must="\"must\":["+range+"]";
		
		
		
		// JSONObject filtered=new JSONObject();
		// JSONObject query1=new JSONObject();
		JSONObject nested=new JSONObject();
		JSONObject nestedobj=new JSONObject();
		// JSONObject query2=new JSONObject();
		JSONObject wildcard=new JSONObject();
		JSONObject annotype=new JSONObject();
		JSONObject bool=new JSONObject();
		
		JSONObject match=new JSONObject();
		JSONObject match2=new JSONObject();
		JSONObject content_=new JSONObject();
		JSONObject content_2=new JSONObject();
		JSONObject shouldval=new JSONObject();
		JSONObject toquery=new JSONObject();
		JSONObject annot=new JSONObject();
		JSONObject annot2=new JSONObject();
		
		try {
			annotype.put(annotation+".resources.types", "*"+this.typeselected);
			wildcard.put("wildcard", annotype);
			if ( (searchterms.size()==0) || (searchterms.get(0).compareTo("")==0) ){
				// annotype.put(annotation+".resources.types", "*"+this.typeselected);
				// wildcard.put("wildcard", annotype);
				// query2.put("query", wildcard);
				should="\"should\":[]";
				nestedobj.put("path", annotation+".resources");
				nestedobj.put("query", wildcard);
				nested.put("nested", nestedobj);
				mustnot="\"must_not\":[]";
				toquery=nested;
			}else{
				JSONArray shouldarr=new JSONArray();
				mustnot="\"must_not\":[]";
				content_.put("query", searchterms.get(0));
				content_.put("boost", this.boostContent);
				content_2.put(content,content_);
				match.put("match", content_2);
				shouldarr.put(match);
				nestedobj.put("path", annotation+".resources");
				annot.put("query", searchterms.get(0));
				annot.put("boost", this.boostAnn);
				annot2.put(annotation+".resources.surfaceForm"+sub,annot);
				match2.put("match",annot2);
				nestedobj.put("query", match2);
				nested.put("nested", nestedobj);
				shouldarr.put(nested);
				shouldval.put("should", shouldarr);
				bool.put("bool", shouldval);
				// {"bool":{"should":[
				// {"match":{"content":{"query":"naomi wolf","boost":1.0}}},
				// {"nested":{"path":"annotation.resources",
				// "query":{"match":{"annotation.resources.surfaceForm.sub":{"query":"naomi wolf","boost":5}}}}}
				// ]}}
				toquery=bool;
			}
			
			// query1.put("query", nested);
			// filtered.put("filtered", query1);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (notincluded.size()>0)
			mustnotpart.add(String.join(",", notincluded));		
		if (mustnotpart.size()>0)
			mustnot="\"must_not\":["+String.join(",", mustnotpart)+"]";
		
		if (positivecat.size()>0)
			pospart.add(String.join(" , ", positivecat));
		if (pospart.size()>0)
			should="\"should\":["+String.join(" , ", pospart)+"]";
		
		if (should.compareTo("")!=0)
			filterpart.add(should);
		filterpart.add(must);
		if (mustnot.compareTo("")!=0)
			filterpart.add(mustnot);
		filter ="\"bool\":{"+String.join(",", filterpart)+"}";
		
		JSONObject aggs=new JSONObject();
		JSONObject nested_aggs=new JSONObject();
		JSONObject path_aggs=new JSONObject();
		JSONObject etype_aggs=new JSONObject();
		
		JSONObject gbe_aggs=new JSONObject();
		JSONObject aggs3=new JSONObject();
		JSONObject terms=new JSONObject();
		JSONObject fields=new JSONObject();
		JSONObject order=new JSONObject();
		
		try {
			order.put("_count","desc");
			fields.put("field",annotation+".resources.surfaceForm"+sub);
			fields.put("size",this.resultnumber);
			fields.put("order", order);
			terms.put(term, fields);
			gbe_aggs.put("GroupByEntities", terms);
			aggs3.put("filter", wildcard);
			aggs3.put("aggs", gbe_aggs);
			etype_aggs.put("EntityType", aggs3);
			path_aggs.put("path", annotation+".resources");
			nested_aggs.put("nested",path_aggs);
			nested_aggs.put("aggs", etype_aggs);
			aggs.put("NestedResources Path",nested_aggs);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		// "aggs":{"NestedResources Path":{"nested":{"path":"annotation.resources"},
		// "aggs":{"EntityType":{"filter":{"wildcard":{"annotation.resources.types":"*newspaper"}},
		// "aggs":{"GroupByEntities":{"terms":{
		// "field":"annotation.resources.surfaceForm.sub","size":10,"order":{"_count":"desc"}}}}}}}}
		
		searchquery="{\"size\":"+"0"+", \"query\":{\"filtered\":{\"query\":"+toquery.toString()+",\"filter\":{"+filter+"}}}, \"aggs\": "+aggs.toString()+" }";
		
		return searchquery;
	}
	
	public String getSearchqueryNO() {
		if (this.field_value_factor!=null)
			if (this.field_value_factor.compareTo("")==0)
				this.field_value_factor=null;
		
		String searchkey="";
		if (searchterms.size()==0)
			searchkey="<KEYWORD>";
		else
			if (searchterms.get(0).compareTo("")==0)
				searchkey="<KEYWORD>";
			else
				searchkey=searchterms.get(0);
		
		if (this.sformsel!=null && ((searchterms.size()==0)||(searchterms.get(0).compareTo("")==0) ))
			searchkey=this.sformsel;
		
		String boosting="";
		String filter="";
		String positive="";
		String negative="";
		String mustnot="";
		
		String must="";
		String should="";
		SimpleDateFormat dt1 = new SimpleDateFormat("yyyyMMdd");
		
		// String range="{\"range\":{\"timestamp.sub\":{\"gte\":"+this.startdate.replace("/", "")+"000000,\"lte\":"+this.enddate.replace("/", "")+"000000}}}";
		String range="{\"range\":{\"timestamp.sub\":{\"gte\":"+dt1.format(this.sdate)+"000000,\"lte\":"+dt1.format(this.edate)+"000000}}}";
		
		String seed="{\"term\":{\"pldType\":\"seed\"}}";
		if (this.isUseseed())
			must="\"must\":["+range+","+seed+"]";
		else
			must="\"must\":["+range+"]";
		
		ArrayList<String> positivecat=new ArrayList<String>();
		ArrayList<String> negativecat=new ArrayList<String>();
		ArrayList<String> notincluded=new ArrayList<String>();
		
		for (String cat : this.getCatmap().keySet()){
			CategorySettings cset=this.getCatmap().get(cat);
			if (cset.isIncluded()){
				if (cset.isHasNegativeBoost())
					negativecat.add("{\"match\":{\"category\":\""+cat+"\"}}");
				else {
					if(cset.isMustnot())
						notincluded.add("{\"term\":{\"category\":\""+cat+"\"}}");
					else
					{
						if (cat.compareTo("education")!=0)
							positivecat.add("{\"match\":{\"category\":{\"query\":\""+cat+"\",\"boost\":"+cset.getPositiveBoost()+"}}}");
						else {
							positivecat.add("{\"match\":{\"category\":{\"query\":\""+cat+"\",\"boost\":"+cset.getPositiveBoost()+"}}}");
							positivecat.add("{\"match\":{\"category\":{\"query\":\""+cat+"d\",\"boost\":"+cset.getPositiveBoost()+"}}}");
						}
					}
				}
			}
				
		}
		if (searchkey.compareTo("")!=0)	{
				positive="";
				String nested="";
				String nestedtitle="";
				ArrayList<String> pospart=new ArrayList<String>();
			
				String sub="";
				if (this.exactmatch)
					sub=".sub";
				
				if (this.isUsesub()) {
					String annsearchkey="";
					if (this.sformsel==null || this.sformsel.compareTo("")==0)
						annsearchkey=searchkey;
					else
						annsearchkey=this.sformsel;
					nested="{\"nested\":{\"path\":\"annotation.resources\",\"query\":{\"match\":{\"annotation.resources.surfaceForm"+sub+"\":{\"query\":\""+annsearchkey+"\",\"boost\":"+this.boostAnn+"}}}}}";
				}
					
					/* if (this.isUsesub())
					if (this.exactmatch)
						pospart.add("{\"match\":{\"annotation.resources.surfaceForm.sub\":{\"query\":\""+this.sformsel+"\",\"boost\":"+this.boostAnn+"}}}");
					else
						pospart.add("{\"nested\":{\"path\":\"annotation.resources\",\"query\":{\"match\":{\"annotation.resources.surfaceForm\":{\"query\":\""+this.sformsel+"\",\"boost\":"+this.boostAnn+"}}}}}");
				*/
				if (this.isUseanntitle()){
					String annsearchkey="";
					if (this.sformsel==null || this.sformsel.compareTo("")==0)
						annsearchkey=searchkey;
					else
						annsearchkey=this.sformsel;
					nestedtitle="{\"nested\":{\"path\":\"titleAnno.resources\",\"query\":{\"match\":{\"titleAnno.resources.surfaceForm"+sub+"\":{\"query\":\""+annsearchkey+"\",\"boost\":"+this.boostAnn+"}}}}}";
				}
					
					// pospart.add("{\"nested\":{\"path\":\"titleAnno.resources\",\"query\":{\"match\":{\"titleAnno.resources.surfaceForm\":{\"query\":\""+this.sformsel+"\",\"boost\":"+this.boostAnnTitle+"}}}}}");
				
				ArrayList<String> filterpart=new ArrayList<String>();
				
				negative="";
				if (negativecat.size()>0){
					negative = String.join(" , ", negativecat);
					negative="\"negative\":["+negative+"],\"negative_boost\": "+this.negativeboost*(-1);
					if (this.isWithtitle())
						pospart.add("{\"match\":{\"content\":{\"query\":\""+searchkey+"\",\"boost\":"+this.boostContent+"}}},{\"match\":{\"title\":{\"query\":\""+searchkey+"\",\"boost\":"+this.boostTitle+"}}}");
					else
						if (this.usesub)
							pospart.add("{\"match\":{\"content\":{\"query\":\""+searchkey+"\",\"boost\":"+this.boostContent+"}}}");
						else
							pospart.add("{\"match\":{\"query\":\""+searchkey+"\"}}");
					
					//if (!this.isWithtitle())
					//	pospart.add("{\"match\":{\"query\":\""+searchkey+"\"}}");
					if (positivecat.size()>0)
						pospart.add(String.join(" , ", positivecat));
					
					if (nested.compareTo("")!=0){
						if (nestedtitle.compareTo("")!=0)
							should = "\"should\":["+nested+","+nestedtitle+","+String.join(" , ", pospart);
						else
							should = "\"should\":["+nested+","+String.join(" , ", pospart);
					}else {
						if (nestedtitle.compareTo("")!=0)
							should = "\"should\":["+nestedtitle+","+String.join(" , ", pospart);
						else
							should = "\"should\":["+String.join(" , ", pospart);
					}
					
					should=should+"]";
					positive="\"positive\":{\"bool\":{"+should+"}}";
					boosting=positive+","+negative;
					boosting="\"boosting\":{"+boosting+"}";
				}else{
					// filterpart.add("\"should\":["+String.join(" , ", positivecat)+"]");
					
					if (this.isWithtitle()){
						// pospart.add("{\"match\":{\"content\":{\"query\":\""+searchkey+"\",\"boost\":"+this.boostContent+"}}},{\"match\":{\"title\":{\"query\":\""+searchkey+"\",\"boost\":"+this.boostTitle+"}}}");
						// should = "\"should\":["+String.join(" , ", pospart)+"]";
						boosting="\"bool\":{ "
								+ "\"should\":[{\"match\":{\"content\":{\"query\":\""+searchkey+"\",\"boost\":"+this.boostContent+"}}},"
								+ "{\"match\":{\"title\":{\"query\":\""+searchkey+"\",\"boost\":"+this.boostTitle+"}}}";
						if (nested.compareTo("")!=0)
							boosting=boosting+","+ nested;
						if (nestedtitle.compareTo("")!=0)
							boosting=boosting+","+ nestedtitle;
						boosting=boosting+" ]}";
						if (positivecat.size()>0)
							pospart.add(String.join(" , ", positivecat));
					}else if (this.isUsesub()){
						boosting="\"bool\":{ "
								+ "\"should\":[{\"match\":{\"content\":{\"query\":\""+searchkey+"\",\"boost\":"+this.boostContent+"}}}";
						if (nested.compareTo("")!=0)
							boosting=boosting+","+ nested;
						if (nestedtitle.compareTo("")!=0)
							boosting=boosting+","+ nestedtitle;
						boosting=boosting+" ]}";
						if (positivecat.size()>0)
							pospart.add(String.join(" , ", positivecat));
					} else{ 
						if (positivecat.size()>0)
							pospart.add(String.join(" , ", positivecat));
						should = String.join(" , ", positivecat);
						boosting="\"query\":\""+searchkey+"\"";
						boosting="\"query_string\":{"+boosting+"}";
					}
					filterpart.add("\"should\":["+String.join(" , ", pospart)+"]");
				}
				
				filterpart.add(must);
				
				/* if ((this.)&&(this.functionscoreInTitle)){
					value_factor+=
							 "{\"field_value_factor\":{\"field\":\"annotation.resources.tf\",\"modifier\": \""+this.modifier+"\",\"factor\":"+this.factor+"}}"
							+ ","
							+ "{\"field_value_factor\":{\"field\":\"titleAnno.resources.tf\",\"modifier\": \""+this.tannomodifier+"\",\"factor\":"+this.tannofactor+"}}";
				}else{
					if (this.field_value_factor)
						value_factor+="\"field\":\"annotation.resources.tf\",\"modifier\": \""+this.modifier+"\",\"factor\":"+this.factor;
					if (this.functionscoreInTitle)
						value_factor+="\"field\":\"titleAnno.resources.tf\",\"modifier\": \""+this.tannomodifier+"\",\"factor\":"+this.tannofactor;
				} */
				
				
				
				ArrayList<String> mustnotpart=new ArrayList<String>();
				
				
				if (notincluded.size()>0)
					mustnotpart.add(String.join(",", notincluded));
				
				if (this.field_value_factor!=null){
					if (this.field_value_factor.compareTo("anno")==0)
						mustnotpart.add("{\"term\":{\"hasAnnotation\":\"false\"}}");
					if (this.field_value_factor.compareTo("titleAnno")==0)
						mustnotpart.add("{\"term\":{\"hasTitleAnnotation\":\"false\"}}");
				}
				
				if (mustnotpart.size()>0){
					mustnot="\"must_not\":["+String.join(",", mustnotpart)+"]";
					filterpart.add(mustnot);
				}
				
				filter ="\"bool\":{"+String.join(",", filterpart)+"}";
				
				
				
			}
		
		searchquery="{\"size\":"+this.resultnumber+", \"query\":{ "+boosting+" },\"filter\":{"+filter+"}}";
		
		String path="";
		String filterpath="";
		String match="";
		String filtermatch="";
		String field_="";
		
		if (this.field_value_factor!=null){
			if (this.field_value_factor.compareTo("anno")==0){
				path="annotation.resources";
				if (this.exactmatch){
					match="annotation.resources.surfaceForm.sub";
					filtermatch="titleAnno.resources.surfaceForm.sub";
				}else{
					match="annotation.resources.surfaceForm";
					filtermatch="titleAnno.resources.surfaceForm";
				}
				filterpath="titleAnno.resources";
				field_="annotation.resources.tf";
			}
			if (this.field_value_factor.compareTo("titleAnno")==0){
				path="titleAnno.resources";
				if (this.exactmatch){
					match="titleAnno.resources.surfaceForm.sub";
					filtermatch="annotation.resources.surfaceForm.sub";
				}else{
					match="titleAnno.resources.surfaceForm";
					filtermatch="annotation.resources.surfaceForm";
				}
				filterpath="annotation.resources";
				field_="titleAnno.resources.tf";
			}
			
			String withtitle="";
			if (this.isWithtitle())
				withtitle="{\"match\":{\"content\":{\"query\":\""+searchkey+"\",\"boost\":"+this.boostContent+"}}},{\"match\":{\"title\":{\"query\":\""+searchkey+"\",\"boost\":"+this.boostTitle+"}}}";
			
			ArrayList<String> shouldpart=new ArrayList<String>();
			if (this.useanntitle){
				shouldpart.add("{\"nested\":{\"path\":\""+filterpath+"\",\"query\":{\"match\":{\""+filtermatch+"\":{\"query\":\""+searchkey+"\",\"boost\":"+this.boostAnn+"}}}}}");
			}
			if (this.isWithtitle())
				shouldpart.add(withtitle);
			if (positivecat.size()>0)
				shouldpart.add(String.join(" , ", positivecat));
			
			should="\"should\":["+ String.join(" , ", shouldpart)+"]";
			
			if (negativecat.size()>0){
				negative = String.join(" , ", negativecat);
				negative="\"negative\":["+negative+"],\"negative_boost\": "+this.negativeboost*(-1);
				
				boosting="\"nested\":{\"path\":\""+path+"\",\"query\":{\"function_score\":{\"query\":{"
						+ "\"boosting\":{\"positive\":{\"bool\":{\"should\":[{\"match\":{\""+match+"\":{\"query\":\""+searchkey+"\",\"boost\":"+this.boostAnn+"}}}]}}"
						+ ","+negative+"}},"
						+ "\"field_value_factor\":{\"field\":\""+field_+"\",\"modifier\": \""+this.modifier+"\",\"factor\":"+this.factor+"},\"score_mode\":\""+this.score_mode+"\",\"boost_mode\": \""+this.boost_mode+"\"}}}";
			}else{
				boosting="\"nested\":{\"path\":\""+path+"\",\"query\":{\"function_score\":{\"query\":{\"bool\":{\"should\":[{\"match\":{\""+match+"\":{\"query\":\""+searchkey+"\",\"boost\":"+this.boostAnn+"}}}]}},"
						+ "\"field_value_factor\":{\"field\":\""+field_+"\",\"modifier\": \""+this.modifier+"\",\"factor\":"+this.factor+"},\"score_mode\":\""+this.score_mode+"\",\"boost_mode\": \""+this.boost_mode+"\"}}}";
			}
			ArrayList<String> filterpart=new ArrayList<String>();
			filterpart.add(should);
			filterpart.add(must);
			filterpart.add(mustnot);
			filter ="\"bool\":{"+String.join(",", filterpart)+"}";
			
			searchquery="{\"size\":"+this.resultnumber+", \"query\":{ "+boosting+" },\"filter\":{"+filter+"}}";
		}
		
		return searchquery;
	}

	public String getSearchqueryOLD() {
		String searchkey="";
		if (searchterms.size()==0)
			searchkey="<KEYWORD>";
		else
			if (searchterms.get(0).compareTo("")==0)
				searchkey="<KEYWORD>";
			else
				searchkey=searchterms.get(0);
		
		String boosting="";
		String filter="";
		String positive="";
		String negative="";
		String mustnot="";
		String value_factor="";
		String must="";
		SimpleDateFormat dt1 = new SimpleDateFormat("yyyyMMdd");
		
		// String range="{\"range\":{\"timestamp.sub\":{\"gte\":"+this.startdate.replace("/", "")+"000000,\"lte\":"+this.enddate.replace("/", "")+"000000}}}";
		String range="{\"range\":{\"timestamp.sub\":{\"gte\":"+dt1.format(this.sdate)+"000000,\"lte\":"+dt1.format(this.edate)+"000000}}}";
		
		String seed="{\"term\":{\"pldType\":\"seed\"}}";
		if (this.isUseseed())
			must="\"must\":["+range+","+seed+"]";
		else
			must="\"must\":["+range+"]";
		
		ArrayList<String> positivecat=new ArrayList<String>();
		ArrayList<String> negativecat=new ArrayList<String>();
		ArrayList<String> notincluded=new ArrayList<String>();
		
		for (String cat : this.getCatmap().keySet()){
			CategorySettings cset=this.getCatmap().get(cat);
			if (cset.isIncluded()){
				if (cset.isHasNegativeBoost())
					negativecat.add("{\"match\":{\"category\":\""+cat+"\"}}");
				else {
					if(cset.isMustnot())
						notincluded.add("{\"term\":{\"category\":\""+cat+"\"}}");
					else
					{
						if (cat.compareTo("education")!=0)
							positivecat.add("{\"match\":{\"category\":{\"query\":\""+cat+"\",\"boost\":"+cset.getPositiveBoost()+"}}}");
						else {
							positivecat.add("{\"match\":{\"category\":{\"query\":\""+cat+"\",\"boost\":"+cset.getPositiveBoost()+"}}}");
							positivecat.add("{\"match\":{\"category\":{\"query\":\""+cat+"d\",\"boost\":"+cset.getPositiveBoost()+"}}}");
						}
					}
				}
			}
				
		}
		/*System.out.println("----");
		System.out.println(searchterms.size());
		System.out.println(positivecat.size());
		System.out.println(negativecat.size());
		System.out.println(notincluded.size());
		System.out.println("----");
		*/
		// if (searchterms.size()!=0)
		if (searchkey.compareTo("")!=0)	{
			// if(searchterms.get(0).compareTo("")!=0){
				positive="";
				ArrayList<String> pospart=new ArrayList<String>();
				if (this.isWithtitle())
					pospart.add("{\"match\":{\"content\":{\"query\":\""+searchkey+"\",\"boost\":"+this.boostContent+"}}},{\"match\":{\"title\":{\"query\":\""+searchkey+"\",\"boost\":"+this.boostTitle+"}}}");
				else
					pospart.add("{\"match\":{\"query\":\""+searchkey+"\"}}");
				
				String sub="";
				if (this.exactmatch)
					sub=".sub";
				
				if (this.isUsesub()){
					pospart.add("{\"match\":{\"annotation.resources.surfaceForm"+sub+"\":{\"query\":\""+this.sformsel+"\",\"boost\":"+this.boostAnn+"}}}");
				}
				
				if (this.isUseanntitle())
					pospart.add("{\"match\":{\"titleAnno.resources.surfaceForm"+sub+"\":{\"query\":\""+this.sformsel+"\",\"boost\":"+this.boostAnnTitle+"}}}");
				
				if (positivecat.size()>0)
					pospart.add(String.join(" , ", positivecat));
				
				positive="\"positive\":{\"bool\":{\"should\":["+String.join(" , ", pospart)+"]}}";
				
				boosting=positive;
				
				negative="";
				if (negativecat.size()>0){
					negative = String.join(" , ", negativecat);
					negative="\"negative\":["+negative+"],\"negative_boost\": "+this.negativeboost*(-1);					
					boosting=positive+","+negative;
				}
				
				/* if ((this.field_value_factor)&&(this.functionscoreInTitle)){
					value_factor+=
							 "{\"field_value_factor\":{\"field\":\"annotation.resources.tf\",\"modifier\": \""+this.modifier+"\",\"factor\":"+this.factor+"}}"
							+ ","
							+ "{\"field_value_factor\":{\"field\":\"titleAnno.resources.tf\",\"modifier\": \""+this.tannomodifier+"\",\"factor\":"+this.tannofactor+"}}";
				}else{ */
					
					//	value_factor+="\"field\":\"annotation.resources.tf\",\"modifier\": \""+this.modifier+"\",\"factor\":"+this.factor;
					//if (this.functionscoreInTitle)
					//	value_factor+="\"field\":\"titleAnno.resources.tf\",\"modifier\": \""+this.tannomodifier+"\",\"factor\":"+this.tannofactor;
				// }
				/*
				 * if ((this.score_mode!=null)&&(this.score_mode.compareTo("")!=0))
					boosting+=", \"score_mode\":\""+this.score_mode+"\"";
				
				if ((this.boost_mode!=null)&&(this.boost_mode.compareTo("")!=0))
					boosting+=", \"boost_mode\": \""+this.boost_mode+"\"";
				if ((this.score_mode!=null)||(this.boost_mode!=null)||(this.field_value_factor)||(this.functionscoreInTitle))
					boosting +="}";
				*/
				// boosting +="}";
				// "field_value_factor":{"field":"annotation.resources.tf","modifier": "ln2p","factor":1},"score_mode":"multiply","boost_mode": "multiply"
				
				ArrayList<String> filterpart=new ArrayList<String>();
				filterpart.add(must);
				
				ArrayList<String> mustnotpart=new ArrayList<String>();
				
				
				if (notincluded.size()>0)
					mustnotpart.add(String.join(",", notincluded));
				
				if (this.field_value_factor!=null)
					if (this.field_value_factor.compareTo("anno")==0)
						mustnotpart.add("{\"term\":{\"hasAnnotation\":\"false\"}}");
					else
						mustnotpart.add("{\"term\":{\"hasTitleAnnotation\":\"false\"}}");
				
				/*if (this.field_value_factor)
					mustnotpart.add("{\"term\":{\"hasAnnotation\":\"false\"}}");
				if (this.functionscoreInTitle)
					mustnotpart.add("{\"term\":{\"hasTitleAnnotation\":\"false\"}}");
					*/
				
				if (mustnotpart.size()>0){
					mustnot="\"must_not\":["+String.join(",", mustnotpart)+"]";
					filterpart.add(mustnot);
				}
				
				filter ="\"bool\":{"+String.join(",", filterpart)+"}";
				
				
				
			}
		
		/*/if ((this.field_value_factor)&&(this.functionscoreInTitle))
			searchquery="{\"size\":"+this.resultnumber+", \"query\":{ \"function_score\":{\"query\":{ \"boosting\":{"+boosting+"}},\"functions\":["+value_factor+"],\"score_mode\":\""+this.score_mode+"\",\"boost_mode\":\""+this.boost_mode+"\" }},\"filter\":{"+filter+"}}";
		else {
			if ((this.field_value_factor)||(this.functionscoreInTitle))
				searchquery="{\"size\":"+this.resultnumber+", \"query\":{ \"function_score\":{\"query\":{ \"boosting\":{"+boosting+"}},\"field_value_factor\":{"+value_factor+"},\"score_mode\":\""+this.score_mode+"\",\"boost_mode\":\""+this.boost_mode+"\" }},\"filter\":{"+filter+"}}";
			else 
				searchquery="{\"size\":"+this.resultnumber+", \"query\":{ \"boosting\":{"+boosting+"}},\"filter\":{"+filter+"}}";
		}*/
		if (this.field_value_factor!=null){
			if (this.field_value_factor.compareTo("anno")==0)
				value_factor+="\"field\":\"annotation.resources.tf\",\"modifier\": \""+this.modifier+"\",\"factor\":"+this.factor;
			if (this.field_value_factor.compareTo("titleAnno")==0)
				value_factor+="\"field\":\"titleAnno.resources.tf\",\"modifier\": \""+this.tannomodifier+"\",\"factor\":"+this.tannofactor;
		}
		if (this.field_value_factor!=null)
			if (this.field_value_factor.compareTo("anno")==0)
				searchquery="{\"size\":"+this.resultnumber+", \"query\":{ \"function_score\":{\"query\":{ \"boosting\":{"+boosting+"}},\"field_value_factor\":{"+value_factor+"},\"score_mode\":\""+this.score_mode+"\",\"boost_mode\":\""+this.boost_mode+"\" }},\"filter\":{"+filter+"}}";
			else
				searchquery="{\"size\":"+this.resultnumber+", \"query\":{ \"boosting\":{"+boosting+"}},\"filter\":{"+filter+"}}";
		//searchquery="{\"size\":"+this.resultnumber+", \"query\":{\"function_score\":{\"query\":{\"bool\":{\"should\":[{"+annotationQuery+"},{"+contentQuery+"},{"+titleQuery+"}]}},\"filter\":{\"bool\":{\"must\":[{\"range\":{\"timestamp.sub\":{\"gte\":"+this.startdate.replace("/", "")+"000000,\"lte\":"+this.enddate.replace("/", "")+"000000}}}]],"
		//+"\"must_not\":[{\"term\":{\"hasAnnotation\":\"false\"}}]}},\"field_value_factor\":{\"field\":\"annotation.resources.tf\",\"modifier\": \"ln2p\",\"factor\":1},\"score_mode\":\"multiply\",\"boost_mode\": \"multiply\"}}}";
		
		return searchquery;
	}
	
	public Double getBoostContent() {
		return boostContent;
	}


	public void setBoostContent(Double boostContent) {
		this.boostContent = boostContent;
	}


	public Double getBoostAnnTitle() {
		return boostAnnTitle;
	}


	public void setBoostAnnTitle(Double boostAnnTitle) {
		this.boostAnnTitle = boostAnnTitle;
	}


	public Double getJthreshold() {
		return jthreshold;
	}


	public void setJthreshold(Double jthreshold) {
		this.jthreshold = jthreshold;
	}


	public boolean isUseanntitle() {
		return useanntitle;
	}


	public void setUseanntitle(boolean useanntitle) {
		this.useanntitle = useanntitle;
	}


	public boolean isExactmatch() {
		return exactmatch;
	}


	public void setExactmatch(boolean exactmatch) {
		this.exactmatch = exactmatch;
	}


	public void setSearchquery(String searchquery) {
		this.searchquery = searchquery;
	}


	public Double getBoostTitle() {
		return boostTitle;
	}


	public void setBoostTitle(Double boostTitle) {
		this.boostTitle = boostTitle;
	}


	public Double getBoostAnn() {
		return boostAnn;
	}


	public void setBoostAnn(Double boostAnn) {
		this.boostAnn = boostAnn;
	}


	public boolean isUseseed() {
		return useseed;
	}


	public void setUseseed(boolean useseed) {
		this.useseed = useseed;
	}


	public boolean isWithtitle() {
		return withtitle;
	}


	public void setWithtitle(boolean withtitle) {
		this.withtitle = withtitle;
	}


	/*public String getStartdate() {
		return startdate;
	}


	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}


	public String getEnddate() {
		return enddate;
	}


	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}*/


	public String getSformsel() {
		return sformsel;
	}


	public void setSformsel(String sformsel) {
		this.sformsel = sformsel;
	}


	public List<String> getSurfaceformList() {
		return wkann.getAnnotationList();
	}

	
	public String getScenepopup() {
		return scenepopup;
	}

	public void setScenepopup(String scenepopup) {
		this.scenepopup = scenepopup;
	}

	public ArrayList<SearchResult> getSearchResult1() {
		return searchResult1;
	}

	public void setSearchResult1(ArrayList<SearchResult> searchResult1) {
		this.searchResult1 = searchResult1;
	}
	
	public Map<String, Integer> getConceptFreqDe() {
		return conceptFreqDe;
	}

	public Map<String, Integer> getConceptFreq() {
		return conceptFreq;
	}

	public ArrayList<String> getCategoryFreqList() {
		return categoryFreqList;
	}

	public void setCategoryFreqList(ArrayList<String> categoryFreqList) {
		this.categoryFreqList = categoryFreqList;
	}

	public ArrayList<String> getIncludeFullText() {
		return includeFullText;
	}

	public void setIncludeFullText(ArrayList<String> includeFullText) {
		this.includeFullText = includeFullText;
	}

	public ArrayList<String> getExcludesel() {
		return excludesel;
	}

	public void setExcludesel(ArrayList<String> excludesel) {
		this.excludesel = excludesel;
	}

	public Integer getResultnumber() {
		return resultnumber;
	}

	public void setResultnumber(Integer resultnumber) {
		this.resultnumber = resultnumber;
	}

	public Double getMinconfidence() {
		return minconfidence;
	}

	public void setMinconfidence(Double minconfidence) {
		this.minconfidence = minconfidence;
	}

	public String getRadiowebnews() {
		return radiowebnews;
	}

	public void setRadiowebnews(String radiowebnews) {
		this.radiowebnews = radiowebnews;
	}
	
	public String getSiteSetId() {
		return siteSetId;
	}

	public void setSiteSetId(String siteSetId) {
		this.siteSetId = siteSetId;
	}
	public String getSiteText() {
		return siteText;
	}

	public void setSiteText(String siteText) {
		this.siteText = siteText;
	}
	
	public String getClickedLink() {
		return clickedLink;
	}

	public void setClickedLink(String clickedLink) {
		this.clickedLink = clickedLink;
	}

	public void savelocalesettings(){
		List<Generalsettings> toremove=new ArrayList<Generalsettings>();
		
		for (Generalsettings gs : this.getUser().getUtente().getGeneralsettings()){
			if ( (gs.getType().compareTo("lang")==0) || (gs.getType().compareTo("loc")==0) )
				toremove.add(gs);
		}
		for (Generalsettings gs : toremove){
			this.getUser().getUtente().getGeneralsettings().remove(gs);
		}
		
		this.searchsettings = "";
		loadRestriction();
		UtenteDao udao=new UtenteDao();
		udao.updateUtente(this.getUser().getUtente());
	}
	
	public String cotext(String term){
        TextManager tmgr=new TextManager();
        tmgr.setCotextrange(this.user.getUtente().getCotextrange());
        return tmgr.cotextTable(term,teasers);
    }
	
	public String multiplecotext(){
        TextManager tmgr=new TextManager();
        tmgr.setCotextrange(this.user.getUtente().getCotextrange());
        return tmgr.multipleCotextTable(searchterms, teasers);
    }
	
	public void loadRestriction(){
		searchsettings="";
		searchsettings+="<b>Search type:</b> "+this.radiowebnews+"<br />";
		// searchsettings+="<b>Confidence level:</b> "+this.minconfidence+"<br />";
		searchsettings+="<b>Number of results:</b> "+this.resultnumber+"<br />";
		if (this.excludesel.size()>0){
			searchsettings+="<b>Exclude:</b> ";
			for (String s:this.excludesel)
				searchsettings+=s+" ";
			searchsettings+="<br />";
		}
		
		if (this.includeFullText.size()>0){
			searchsettings+="<b>Include Full Text:</b> ";
			for (String s:this.includeFullText)
				searchsettings+=s+" ";
			searchsettings+="<br />";
		}
		
	}
	
	
	public ArrayList<String> getCategoryFreqListDE() {
		return categoryFreqListDE;
	}

	public void setCategoryFreqListDE(ArrayList<String> categoryFreqListDE) {
		this.categoryFreqListDE = categoryFreqListDE;
	}



	public String getResultId() {
		return resultId;
	}

	public void setResultId(String resultId) {
		this.resultId = resultId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getTagclouddata() {
		return tagclouddata;
	}

	public void setTagclouddata(String tagclouddata) {
		this.tagclouddata = tagclouddata;
	}

	public String getTagclouddatait() {
		return tagclouddatait;
	}

	public void setTagclouddatait(String tagclouddatait) {
		this.tagclouddatait = tagclouddatait;
	}

	public String getTagclouddataes() {
		return tagclouddataes;
	}

	public void setTagclouddataes(String tagclouddataes) {
		this.tagclouddataes = tagclouddataes;
	}

	public String getTagclouddataen() {
		return tagclouddataen;
	}

	public void setTagclouddataen(String tagclouddataen) {
		this.tagclouddataen = tagclouddataen;
	}
	
	public String getClickedTag() {
		return clickedTag;
	}

	public void setClickedTag(String clickedTag) {
		this.clickedTag = clickedTag;
	}

	public void setSearchResultWeb(ArrayList<SearchResult> searchResultWeb) {
		this.searchResultWeb = searchResultWeb;
	}

	public ArrayList<SearchResult> getSearchResultEn() {
		return searchResultEn;
	}

	public void setSearchResultEn(ArrayList<SearchResult> searchResultEn) {
		this.searchResultEn = searchResultEn;
	}

	public String getSearchsettings() {
		return searchsettings;
	}

	public void setSearchsettings(String searchsettings) {
		this.searchsettings = searchsettings;
	}
	
	
	public boolean isUsesub() {
		return usesub;
	}

	public void setUsesub(boolean usesub) {
		this.usesub = usesub;
	}

	@PostConstruct
    public void init() {
		
		
		radiowebnews="Archive";
		minconfidence=0.6;
		resultnumber=10;
		boostAnn=2.0;
		boostTitle=3.0;
		boostContent=1.0;
		boostAnnTitle=1.0;
		includeFullText.add("true");
		
		searchterms = new ArrayList<String>();
		searchterms.add("");
		searchterms.add("");
		searchterms.add("");
		searchterms.add("");
		
		teasers = new ArrayList<String>();
		loadRestriction();
		
		
	
		
	}
	
    public void extend() {
    	searchterms.add("");
    }
	
      
    public List<String> getSearchterms() {
		return searchterms;
	}


	public void setSearchterms(List<String> searchterms) {
		this.searchterms = searchterms;
	}

	

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ArrayList<SearchResult> resultList(String lang){
		return searchResultWebMap.get(lang);
	}
	
	public ArrayList<SearchResult> getSearchResultWeb(){
		if (searchResultWeb!=null)
			System.out.println("searchResultWeb: "+searchResultWeb.size());
		else
			System.out.println("searchResultWeb: null");
		return searchResultWeb;
	}
	
		
	
public String searcAll(int nuovo) throws IOException{
		searchterms.removeAll(Collections.singleton(""));
		ESearchPost eSearch= new ESearchPost("http://130.75.152.117/esearch/asimijaz_arch2950_nestedwt/_search?pretty&search_type=dfs_query_then_fetch");
		ArrayList<ESearchDataObject> objList=(ArrayList<ESearchDataObject>) eSearch.searhByContent2(this.getSearchquery());
		
		searchResultWeb = new ArrayList<SearchResult>();
		
		for (ESearchDataObject anr : objList){
				SearchWebResult r=new SearchWebResult();
				r.setDescription(anr.getContent());
				r.setUrl(anr.getUrl());
				searchResultWeb.add(r);
		}			  
		searchResult1=new ArrayList<SearchResult>(searchResultWeb);
		Track track=new Track();
        track.setDate((new GregorianCalendar()).getTime());
        track.setOperation("archivesearchelastic2");
        track.setParam1("");
        track.setParam2("");
        track.setParam3("");
        track.setUtente(this.user.getUtente());
        TrackDao td=new TrackDao();
        td.addTrack(track);
			
		if (searchterms.size()==0) searchterms.add("");
		
		tagclouddata=" var wordsen = [ ";
		tagclouddata+="]; d3.layout.cloud().size([1000, 500]).words(wordsen).rotate(0).font(\"Impact\").fontSize(function(d) { return d.size; }).on(\"end\", drawen).start();";
		return "archiveSearchStudent2";
	}
	
}