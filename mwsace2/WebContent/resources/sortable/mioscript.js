$(document).ready(function(){
	
	var prevEvent = null;
		  
	     $( ".column" ).sortable({
	       connectWith: ".column",
	       handle: ".portlet-header",
	       cancel: ".portlet-toggle",
	       placeholder: "portlet-placeholder ui-corner-all",
	       update : function (event,ui) {
	 		          if(event.timeStamp == prevEvent){
	 		              return null;
	 		          }
	 				
	 		          prevEvent = event.timeStamp;
	 					var data11="";
	 		          $(".column").each(function(){
	 		              data11=data11+$(this).attr('id')+':'+$(this).sortable("serialize");
	 		              
	 		          });
	 		      orderList(data11);
	 		          
	 	    		}
	 			    
	     });
	    	
	    	$( ".portlet" )
	      .addClass( "ui-widget ui-widget-content ui-helper-clearfix ui-corner-all" );
	      
	      $( ".portlet-header" )
	        .addClass( "ui-widget-header ui-corner-all" );
	        
	     $( ".portlet-header" ).prepend( "<span class=\"ui-icon ui-icon-minusthick portlet-toggle\"></span>");
	     

	     
});
