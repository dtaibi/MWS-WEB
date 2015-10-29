$(function(){
	
	var timeline = new VMM.Timeline();
	timeline.init("http://localhost:8080/mwsace2/javax.faces.resource/data.json.jsf?ln=timeline");
	// timeline.init('{"headline":"Johnny B Goode","startDate":"2009,1","text":"<i><span>Designer<\/span> & <span>Developer<\/span><\/i>","asset":{"caption":"","credit":"","media":""},"date":[{"headline":"My first experiment in time-lapse photography","startDate":"2009,2","text":"Nature at its finest in this video.","asset":{"caption":"","credit":"","media":""}},{"headline":"http://wikitravel.org/en/Berlin","startDate":"2009,5","text":"","asset":{"caption":"","credit":"","media":"http://localhost:8080/mwsace2/javax.faces.resource/img/icon/icon-wikipedia.png.jsf?ln=timeline"}}],"type":"default"}');

});
