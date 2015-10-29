package de.unihannover.l3s.mws.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TextManager {

	public static void main(String[] args){
		TextManager tm=new TextManager();
		List<String> queries=new ArrayList<String>();
		queries.add("secret service");
		String stringa=tm.MultipleTextToCheck(queries, "'Secret Service the actor, who won an Oscar for his performance in 2010’s The King's Speech, is in talks to star in The Secret Service, Matthew Vaughn’s adaptation of ...", 1);
		System.out.println(stringa);
	}
	
	private Integer cotextrange=3;
	
	public void setCotextrange(Integer cotextrange){
			this.cotextrange=cotextrange;
	}
	
	
	public String StripTagsCharArray(String source)
    {
        char[] array = new char[source.length()];
        int arrayIndex = 0;
        boolean inside = false;

        for (int i = 0; i < source.length(); i++)
        {
            char let = source.charAt(i);
            if (let == '<')
            {
                inside = true;
                continue;
            }
            if (let == '>')
            {
                inside = false;
                continue;
            }
            if (!inside)
            {
                array[arrayIndex] = let;
                arrayIndex++;
            }
        }
        return new String(array, 0, arrayIndex);
    }
	
    public String MultipleTextToCheck(List<String> queries, String text_to_check, int type_of_design)
    {
        String result = "";
        String[] words_to_analyze = StripTagsCharArray(text_to_check).split("\\s+"); // Regex.Split(StripTagsCharArray(text_to_check), @"\s+");
        
        List<Integer> index_of_query = new ArrayList<Integer>();
        List<Integer> sx_index = new ArrayList<Integer>();
        List<Integer> dx_index = new ArrayList<Integer>();
        
        String basicColorSX="#0000FF";
        String basicColorCTR="#000";
        String basicColorDX="#FF0000";

        for (String query : queries)
        {
        	int query_lenght=StripTagsCharArray(query).split("\\s+").length;
            if (query.compareTo("")!=0)
            {
                String query_word = query;

                int index_string = text_to_check.toLowerCase().indexOf(query_word.toLowerCase());
                int index=-1;
                // System.out.println(index_string);
                while (index_string >= 0) {
                	int k=0;
                	int lun=0;
                	while(k<words_to_analyze.length && lun<index_string){
                		lun+=words_to_analyze[k].length()+1;
                		// System.out.println(lun+" :: "+k+" "+words_to_analyze[k]);
                		k++;
                	}
                	if (lun > index_string)
                		k--;
                	if (k!=words_to_analyze.length){
                		// System.out.println("SELECTED:"+lun+" :: "+k+" "+words_to_analyze[k]);
                		
                		index = k;
                		index_of_query.add(index);
                		dx_index.add(index + (query_lenght-1) + cotextrange);
                		sx_index.add(index - cotextrange);
                	}
                	index_string = text_to_check.toLowerCase().indexOf(query_word.toLowerCase(), index_string + 1);
                }
                
                /*for (int i = 0; i < words_to_analyze.length; i++)
                {
                    if (words_to_analyze[i].toLowerCase().equals(query_word.toLowerCase()) || words_to_analyze[i].toLowerCase().contains(query_word.toLowerCase()))
                    {
                        index_of_query.add(i);
                        break;
                    }
                }*/
            }
        }
        Collections.sort(index_of_query);
        // Array.Sort(index_of_query);
        // System.out.println("TEXT:"+text_to_check);
        //for (Integer k : index_of_query)
        //{
            // sx_index.set(k, index_of_query.get(k) - 3); // GlobalData.basic_SX_Cotext;
            // dx_index.set(k, index_of_query.get(k) + 3); // GlobalData.basic_DX_Cotext;
        	//sx_index.add(k - 3);
        	
        	// System.out.println("idx:"+k+" SX:"+(k-3)+" DX:"+(k+3));
        //}
        Collections.sort(sx_index);
        Collections.sort(dx_index);
        
        String bold = "";
        String endbold = "";
        /* if (GlobalData.basicBoldCheck)
        {
            bold = "<b>";
            endbold = "</b>";
        } */

        String table = "";
        String endtable = "";
        String td = "";
        String endtd = "";
        if (type_of_design == 1)
        {
            table = "<table cellspacing='20' width='100%'><tr>";
            endtable = "</tr></table>";
            td = "<td>";
            endtd = "</td>";
        }

        result += table;

        int step1 = 0, step2 = 0, step3 = 0, step4 = 0, step5 = 0, p = 0;
        
        for (int i = 0; i < words_to_analyze.length; i++)
        {
            if (p > index_of_query.size()-1){
            	if (step5 == 0) result += td;
                result += "<span>" + words_to_analyze[i] + " </span>";
                step5 = 1;
            }else{
            	// System.out.println("SX_INDEX:"+sx_index.get(p)+" INDEX:"+index_of_query.get(p)+" DX_INDEX:"+dx_index.get(p));
            	
            	if ( (p < index_of_query.size()-1) && i==index_of_query.get(p+1))
            		p++; // if the next search terms is in the range of the one under investigation
                if ((i < sx_index.get(p)) && type_of_design != 1)
                {
                    if (step1 == 0) result += td;
                    result += "<span>" + words_to_analyze[i] + " </span>";
                    step1 = 1;
                }
                else if (i >= sx_index.get(p) && i < index_of_query.get(p))
                {
                    if (step2 == 0 && type_of_design != 1) result += td;
                    if (step2 == 0 && type_of_design == 1) result += "<TD align='right'>";
                    String color = basicColorSX;
                    if (queries.contains(words_to_analyze[i]))
                    {
                        color = basicColorCTR;
                        result += "<span style='color: " + color + ";'>" + bold + words_to_analyze[i] + endbold + " </span>";
                        
                    }
                    else 
                    {
                        result += "<span style='color: " + color + ";'>" + words_to_analyze[i] + " </span>";
                    }
                    // System.out.println(result);
                    step2 = 1;
                }
                else if (i >= index_of_query.get(p) && i <= (dx_index.get(p)-cotextrange))
                {
                    if (step3 == 0 && type_of_design != 1) result += td;
                    if (step3 == 0 && type_of_design == 1) result += endtd + "<TD align='center'>";
                    result += "<span style='color: " + basicColorCTR + ";'>" + bold + words_to_analyze[i] + endbold + " </span>";
                    step3 = 1;
                }
                else if (i > index_of_query.get(p) && i <= dx_index.get(p))
                {
                    if (step4 == 0 && type_of_design != 1) result += td;
                    if (step4 == 0 && type_of_design == 1) result += endtd + "<TD align='left'>";
                    String color = basicColorDX;
                    if (queries.contains(words_to_analyze[i]))
                    {
                        color = basicColorCTR;
                        result += "<span style='color: " + color + ";'>" + bold + words_to_analyze[i] + endbold + " </span>";
                    }
                    else result += "<span style='color: " + color + ";'>" + words_to_analyze[i] + " </span>";
                    step4 = 1;
                }
                else if ((i > dx_index.get(p)) && type_of_design != 1)
                {
                    

                    p++;
                    // to cope with " the H1N1 influenza ||pandemic|| highlights the role public ||fear|| can play in "
                    if ( (p < index_of_query.size()) && (i >= sx_index.get(p) && i < index_of_query.get(p)))
                    {
                        if (step2 == 0 && type_of_design != 1) result += td;
                        if (step2 == 0 && type_of_design == 1) result += "<TD align='right'>";
                        String color = basicColorSX;
                        if (queries.contains(words_to_analyze[i]))
                        {
                            color = basicColorCTR;
                            result += "<span style='color: " + color + ";'>" + bold + words_to_analyze[i] + endbold + " </span>";
                        }
                        else 
                        {
                            result += "<span style='color: " + color + ";'>" + words_to_analyze[i] + " </span>";
                        }

                        step2 = 1;
                    } else {
                    	if (step5 == 0) result += td;
                    	result += "<span>" + words_to_analyze[i] + " </span>";
                    	step5 = 1;
                    }
                }
                result += endtd;
            }
        }

        result += endtable;
        return result;
    }
	
	
	 public String SingleTextToCheck(String query_word, String text_to_check, int type_of_design)
     {
		 
		 // type_of_design == 0 for Plain and html
		 // type_of_design == 1 for Table
         String result = "";
         int index_of_query = -1;
         String basicColorSX="#0000FF";
         String basicColorCTR="#000";
         String basicColorDX="#FF0000";


         
         String[] words_to_analyze = StripTagsCharArray(text_to_check).split("\\s+");
         for (int i = 0; i < words_to_analyze.length; i++)
         {
             if (words_to_analyze[i].toLowerCase().equals(query_word.toLowerCase()) || words_to_analyze[i].toLowerCase().contains(query_word.toLowerCase()))
             {
                 index_of_query = i;
                 break;
             }
         }

         int sx_index = index_of_query - cotextrange; // GlobalData.basic_SX_Cotext;
         int dx_index = index_of_query + cotextrange; // GlobalData.basic_DX_Cotext;

         String bold = "";
         String endbold = "";
         /* if (GlobalData.basicBoldCheck)
         {
             bold = "<b>";
             endbold = "</b>";
         } */

         String table = "";
         String endtable = "";
         String td = "";
         String endtd = "";
         if (type_of_design == 1)
         {
             // table = "<table cellspacing='20' width='" + (GlobalData.currentWB.Width - 4) + "'><tr>";
        	 table = "<table cellspacing='20' width='" + "300" + "'><tr>";
             endtable = "</tr></table>";
             td = "<td style=\"float: right;\">";
             endtd = "</td>";
         }

         result += table;


         int step1 = 0, step2 = 0, step3 = 0, step4 = 0, step5 = 0;
         for (int i = 0; i < words_to_analyze.length; i++)
         {
             if (i < sx_index && type_of_design != 1)
             {
                 if (step1 == 0) result += td;
                 result += "<span>" + words_to_analyze[i] + " </span>";
                 step1 = 1;
             }
             else if (i >= sx_index && i < index_of_query)
             {
                 if (step2 == 0 && type_of_design != 1) result += endtd + td;
                 if (step2 == 0 && type_of_design == 1) result += endtd + "<TD style=\"float: right;\" >";
				result += "<span style='color: " + basicColorSX + ";'>" + words_to_analyze[i] + " </span>";
                 step2 = 1;
             }
             else if (i == index_of_query)
             {
                 if (step3 == 0 && type_of_design != 1) result += endtd + td;
                 if (step3 == 0 && type_of_design == 1) result += endtd + "<TD>";
                 
				result += "<span style='color: " + basicColorCTR + ";'>" + bold + words_to_analyze[i] + endbold + " </span>";
                 step3 = 1;
             }
             else if (i > index_of_query && i <= dx_index)
             {
                 if (step4 == 0 && type_of_design != 1) result += endtd + td;
                 if (step4 == 0 && type_of_design == 1) result += endtd + "<TD style=\"float: left;\" >";
                 
				result += "<span style='color: " + basicColorDX + ";'>" + words_to_analyze[i] + " </span>";
                 step4 = 1;
             }
             else if (i > dx_index && type_of_design != 1)
             {
                 if (step5 == 0) result += endtd + td;
                 result += "<span>" + words_to_analyze[i] + " </span>";
                 step5 = 1;
             }
         }

         result += endtd + endtable;

         return result;
     }
	
	 public String multipleCotextTable(List<String> searchterms, List<String> teasers) {
		 String result = "";
		 for (String t : teasers){
			 String row="";
			 String empty="";
			 for (String st : searchterms){
				 row+=calculateCotextTable(st,t)+"<td></td>";
				 empty+="<td></td><td></td><td></td><td></td>";
			 }
			 if (row.compareTo(empty)!=0)
				 result+="<tr>"+row+"</tr>";
			 
		 }
		 return result;
	 }
	 
	 private String calculateCotextTable(String term, String text_to_check) {
	        String result = "";
	         
	        String basicColorSX="#0000FF";
	        String basicColorCTR="#000";
	        String basicColorDX="#FF0000";
	 
	        String[] terms=StripTagsCharArray(term).split("\\s+");
	       
	            int index_of_query = -1;
	            index_of_query = text_to_check.toLowerCase().indexOf(term.toLowerCase());
	            String[] words_to_analyze = StripTagsCharArray(text_to_check).split("\\s+");

	            	int query_lenght=StripTagsCharArray(term).split("\\s+").length;
	                if (term.compareTo("")!=0)
	                {
	                    String query_word = term;

	                    int index_string = text_to_check.toLowerCase().indexOf(query_word.toLowerCase());
	                    // int index=-1;
	                    // System.out.println(index_string);
	                    while (index_string >= 0) {
	                    	int k=0;
	                    	int lun=0;
	                    	while(k<words_to_analyze.length && lun<index_string){
	                    		lun+=words_to_analyze[k].length()+1;
	                    		// System.out.println(lun+" :: "+k+" "+words_to_analyze[k]);
	                    		k++;
	                    	}
	                    	if (lun > index_string)
	                    		k--;
	                    	if (k!=words_to_analyze.length){
	                    		index_of_query=k;
	                    	}
	                    	index_string = text_to_check.toLowerCase().indexOf(query_word.toLowerCase(), index_string + 1);
	                    }
	           
	                }
	            
	            
	            
	            /* for (int i = 0; i < words_to_analyze.length; i++)
	            {
	                if (words_to_analyze[i].toLowerCase().equals(term.toLowerCase()) || words_to_analyze[i].toLowerCase().contains(term.toLowerCase()))
	                {
	                    index_of_query = i;
	                    System.out.println("IDX Q:"+index_of_query+" WORD:"+words_to_analyze[i]+" "+text_to_check);
	                    break;
	                }
	            } */
	            if (index_of_query != -1){
	                // int sx_index = index_of_query - 3; // GlobalData.basic_SX_Cotext;
	                // int dx_index = index_of_query+terms.length + 3; // GlobalData.basic_DX_Cotext;
	                
	                int dx_index= index_of_query + (query_lenght-1) + cotextrange;
            		int sx_index=index_of_query - cotextrange;
            		
	                String bold = "";
	                String endbold = "";
	                 
	                String td_right = "<td style=\"float: right;white-space:nowrap;\">";
	                String td_left = "<td style=\"float: left;white-space:nowrap;\">";
	                String td_empty = "<td style=\"width:1%;white-space:nowrap;\">";
	                String endtd = "</td>";
	         
	                result+="";
	                int step1 = 0, step2 = 0, step3 = 0, step4 = 0, step5 = 0;
	                for (int i = 0; i < words_to_analyze.length; i++)
	                {
	                    if (i < sx_index )
	                    {
	                        // if (step1 == 0) result += td;
	                        // result += "<span>" + words_to_analyze[i] + " </span>";
	                        step1 = 1;
	                    }
	                    else if (i >= sx_index && i < index_of_query)
	                    {
	                        if (step2 == 0 ) result += td_right;
	                     
	                        result += "<span style='color: " + basicColorSX + ";'>" + words_to_analyze[i] + " </span>";
	                        step2 = 1;
	                    }
	                    else if (i >= index_of_query && i<= (dx_index-cotextrange))
	                    {
	                        if (step2 == 0) {result += td_right; step2=1;}
	                        if (step3 == 0 ) result += endtd + td_empty;
	                           
	                        result += "<span style='color: " + basicColorCTR + ";'>" + bold + words_to_analyze[i] + endbold + " </span>";
	                        step3 = 1;
	                    }
	                    else if (i > index_of_query && i <= dx_index)
	                    {
	                        if (step4 == 0 ) result += endtd + td_left;
	                         
	                        result += "<span style='color: " + basicColorDX + ";'>" + words_to_analyze[i] + " </span>";
	                        step4 = 1;
	                    }
	                    else if (i > dx_index)
	                    {
	                        // if (step5 == 0) result += endtd + td;
	                        // result += "<span>" + words_to_analyze[i] + " </span>";
	                        step5 = 1;
	                    }
	                }
	                if (step4 == 0) {result += td_left; }
	                result += endtd;
	            }else{
	            	result+="<td></td><td></td><td></td>";
	            }
	            // result += "<br/>";
	        
	        return result;
	    }
	 
	 public String cotextTable(String term, List<String> teasers) {
	        String result = "";
	         
	        String basicColorSX="#0000FF";
	        String basicColorCTR="#000";
	        String basicColorDX="#FF0000";
	        
	        String[] terms=StripTagsCharArray(term).split("\\s+");
	 
	       for (String text_to_check : teasers){
	           String[] words_to_analyze = StripTagsCharArray(text_to_check).split("\\s+");
	    	   int index_of_query = text_to_check.toLowerCase().indexOf(term.toLowerCase());
	    	   
	    	   
	    	   
	    	   int query_lenght=StripTagsCharArray(term).split("\\s+").length;
               if (term.compareTo("")!=0)
               {
                   String query_word = term;

                   int index_string = text_to_check.toLowerCase().indexOf(query_word.toLowerCase());
                   while (index_string >= 0) {
                   	int k=0;
                   	int lun=0;
                   	while(k<words_to_analyze.length && lun<index_string){
                   		lun+=words_to_analyze[k].length()+1;
                   		// System.out.println(lun+" :: "+k+" "+words_to_analyze[k]);
                   		k++;
                   	}
                   	if (lun > index_string)
                   		k--;
                   	if (k!=words_to_analyze.length){
                   		index_of_query=k;
                   	}
                   	index_string = text_to_check.toLowerCase().indexOf(query_word.toLowerCase(), index_string + 1);
                   }
          
               }
	    	   
	            /* for (int i = 0; i < words_to_analyze.length; i++)
	            {
	            	System.out.println("IDX Q:"+i+" WORD:"+words_to_analyze[i]+" <"+term+"> ");
	                if (words_to_analyze[i].toLowerCase().equals(term.toLowerCase()) || words_to_analyze[i].toLowerCase().contains(term.toLowerCase()))
	                {
	                    index_of_query = i;
	                    // System.out.println("IDX Q:"+index_of_query+" WORD:"+words_to_analyze[i]+" "+text_to_check);
	                    break;
	                }
	            }*/
	            /*int index = word.indexOf(guess);
while (index >= 0) {
    System.out.println(index);
    index = word.indexOf(guess, index + 1);
}*/
	            if (index_of_query != -1){
	                // int sx_index = index_of_query - 3; // GlobalData.basic_SX_Cotext;
	                // int dx_index = index_of_query+terms.length + 3; // GlobalData.basic_DX_Cotext;
	                
	                int dx_index= index_of_query + (query_lenght-1) + cotextrange;
            		int sx_index=index_of_query - cotextrange;
	         
	                String bold = "";
	                String endbold = "";
	                 
	                String td_right = "<td style=\"float: right;white-space:nowrap;\">";
	                String td_left = "<td style=\"float: left;white-space:nowrap;\">";
	                String td_empty = "<td style=\"width:1%;white-space:nowrap;\">";
	                String endtd = "</td>";
	         
	                result+="<tr>";
	                int step1 = 0, step2 = 0, step3 = 0, step4 = 0, step5 = 0;
	                for (int i = 0; i < words_to_analyze.length; i++)
	                {
	                    if (i < sx_index )
	                    {
	                        // if (step1 == 0) result += td;
	                        // result += "<span>" + words_to_analyze[i] + " </span>";
	                        step1 = 1;
	                    }
	                    else if (i >= sx_index && i < index_of_query)
	                    {
	                        if (step2 == 0 ) result += td_right;
	                     
	                        result += "<span style='color: " + basicColorSX + ";'>" + words_to_analyze[i] + " </span>";
	                        step2 = 1;
	                    }
	                    else if (i >= index_of_query && i<= (dx_index-cotextrange))
	                    {
	                        if (step2 == 0) {result += td_right; step2=1;}
	                        if (step3 == 0 ) result += endtd + td_empty;
	                           
	                        result += "<span style='color: " + basicColorCTR + ";'>" + bold + words_to_analyze[i] + endbold + " </span>";
	                        step3 = 1;
	                    }
	                    else if (i > index_of_query && i <= dx_index)
	                    {
	                        if (step4 == 0 ) result += endtd + td_left;
	                         
	                        result += "<span style='color: " + basicColorDX + ";'>" + words_to_analyze[i] + " </span>";
	                        step4 = 1;
	                    }
	                    else if (i > dx_index)
	                    {
	                        // if (step5 == 0) result += endtd + td;
	                        // result += "<span>" + words_to_analyze[i] + " </span>";
	                        step5 = 1;
	                    }
	                }
	                if (step4 == 0) {result += td_left; }
	                result += endtd+"</tr>";
	            }
	            // result += "<br/>";
	        }
	        return result;
	    }
}
