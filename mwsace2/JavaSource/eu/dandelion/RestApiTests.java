package eu.dandelion;


import java.util.ArrayList;

import org.apache.http.HttpException;

public class RestApiTests {
	
	
	private static String shortText = "Pizza is a flatbread generally topped with tomato sauce and cheese and baked in an oven. "+
			"It is commonly topped with a selection of meats, vegetables and condiments.";
	private static String longText = "Pizza is a flatbread generally topped with tomato sauce and cheese and baked in an oven. "+
			"It is commonly topped with a selection of meats, vegetables and condiments. "+
			"The term was first recorded in the 10th century, in a Latin manuscript from Gaeta in Central Italy. "+
			"The modern pizza was invented in Naples, Italy, and the dish and its variants have since become popular in many areas of the world. "+
			"In 2009, upon Italy's request, Neapolitan pizza was safeguarded in the European Union as a Traditional Speciality Guaranteed dish. "+
			"The Associazione Verace Pizza Napoletana (the True Neapolitan Pizza Association) is a non-profit organization founded in 1984 with headquarters in Naples. "+
			"It promotes and protects the true Neapolitan pizza. "+
			"Pizza is sold fresh, frozen or in portions. "+
			"Various types of ovens are used to cook them and many varieties exist. "+
			"Several similar dishes are prepared from ingredients commonly used in pizza preparation, such as calzone and stromboli. It is a popular fast food item. "+
			"The origin of the word pizza (Italian: pittsa) is uncertain. "+
			"The term pizza first appeared in a Latin text from the southern Italy town of Gaeta in 997 AD, "+
			"which states that a tenant of certain property is to give the bishop of Gaeta duodecim pizze (twelve pizzas) every Christmas Day, and another twelve every Easter Sunday. "+
			"Modern pizza evolved from similar flatbread dishes in Naples, Italy in the 18th or early 19th century. "+
			"Prior to that time, flatbread was often topped with ingredients such as garlic, salt, lard, cheese, and basil. "+
			"It is uncertain when tomatoes were first added and there are many conflicting claims. "+
			"Until about 1830, pizza was sold from open-air stands and out of pizza bakeries, and pizzerias keep this old tradition alive today. "+
			"Antica Pizzeria Port'Alba in Naples is widely regarded as the first pizzeria. "+
			"A popular contemporary legend holds that the archetypal pizza, pizza Margherita, "+
			"was invented in 1889, when the Royal Palace of Capodimonte commissioned the Neapolitan pizzaiolo (pizza maker) Raffaele Esposito "+
			"to create a pizza in honor of the visiting Queen Margherita. Of the three different pizzas he created, "+
			"the Queen strongly preferred a pie swathed in the colors of the Italian flag: red (tomato), green (basil), and white (mozzarella). "+
			"Supposedly, this kind of pizza was then named after the Queen as Pizza Margherita,[ although recent research casts doubt on this legend. "+
			"Pizza was brought to the United States with Italian immigrants in the late nineteenth century; " +
			"and first appeared in areas where Italian immigrants concentrated. The country's first pizzeria, " +
			"Lombardi's, opened in 1905. Following World War II, veterans returning from the Italian Campaign "+
			"after being introduced to Italy's native cuisine proved a ready market for pizza in particular. "+
			"Since then pizza consumption has exploded in the U.S. pizza chains such as Domino's, Pizza Hut, "+
			"and Papa John's, pies from take and bake pizzerias and chilled and frozen from supermarkets, "+
			"make pizza readily available nationwide. "+
			"It is so ubiquitous, thirteen percent of the U.S. population consumes pizza on any given day. ";

	private static String itText = "Berlino ha un piano per prendere in mano i conti di tutti i Paesi dell'Unione europea e, se necessario, farli fallire e guidarne la transizione. A lanciare la sfida all'Ue, come sottolinea anche Federico Fubini sul Corriere della Sera, è il potentissimo governatore della Bundesbank Jens Weidmann: intervistato dal settimanale tedesco Focus, propone misure in perfetta linea con il ministro delle Finanze di Angela Merkel, l'altrettanto potentissimo e ultra-rigorista Wolfgang Schaeuble. In sintesi, la Bundesbank vorrebbe una nuova autorità per un controllo sui conti pubblici dei Paesi dell'euro, separata dalla Commissione di Bruxelles. ";
	
	private static String deText = "Bundesbankvorstandsmitglied Joachim Nagel hat sich dafür ausgesprochen, die sogenannte Anfa-Vereinbarung offenzulegen. Die Vereinbarung legt bilanzielle Obergrenzen fest für finanzielle, nicht-geldpolitische Geschäfte der nationalen Zentralbanken im Eurosystem. In keinem Fall dürfe eine monetäre Staatsfinanzierung erfolgen, sagte Nagel in einem zweiteiligen Interview mit der Börsen-Zeitung.  ";
	
	public static void main(String[] args) {
		
		ArrayList<DandelionDataObject> objList = null;
		EntityExtractionService service = null;
		
		try {
			objList=EntityExtractionService.postText(deText);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} ;
		DBpediaConnect dbpc=new DBpediaConnect();
	for (int i=0; i<objList.size(); i++)	
	{
	// System.out.println(i);
		System.out.println(objList.get(i).getLabel());
		System.out.println(objList.get(i).getUri());
		System.out.println(dbpc.getEng(objList.get(i).getUri()));
	}
	}
}


