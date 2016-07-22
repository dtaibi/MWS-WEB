package de.unihannover.l3s.mws.model;

import java.util.ArrayList;

public class Cloud {
	ArrayList<CloudItem> list=new ArrayList<CloudItem>();
	String name;
	String startdate;
	String title;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ArrayList<CloudItem> getList() {
		return list;
	}

	public void setList(ArrayList<CloudItem> list) {
		this.list = list;
	}
	
	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CloudItem getItemByName(String name){
		for (CloudItem c : this.list)
			if (c.getText().compareTo(name)==0)
				return c;
		return null;
	}
	
	public boolean hasItem(CloudItem ci){
		for (CloudItem c : this.list)
			if ((c.getText().compareTo(ci.getText())==0)&&(Double.compare(c.getValue(),ci.getValue())==0))
				return true;
		return false;
	}
	
	public boolean sameAs(Cloud cloud){
		for (CloudItem c : cloud.list)
			if (!this.hasItem(c))
				return false;
		return true;
	}
	
	public Cloud diff(Cloud cloud){
		Cloud diffcloud=new Cloud();
		diffcloud.setName("diff"+this.name+" - "+cloud.getName());
		diffcloud.setTitle("diff"+this.name+" - "+cloud.getName());
		for (CloudItem c : cloud.list)
			if (!this.hasItem(c))
				diffcloud.list.add(c);
		return diffcloud;
	}
	
	public Cloud bidirectionalDiff(Cloud cloud){
		Cloud diffcloud=new Cloud();
		diffcloud.setName("diff"+this.name+" <-> "+cloud.getName());
		diffcloud.setTitle("diff"+this.name+" <-> "+cloud.getName());
		for (CloudItem c : cloud.list)
			if (this.getItemByName(c.getText())!=null){
				Double diffval=this.getItemByName(c.getText()).getValue()-c.getValue();
				if (diffval!=0.0){
					CloudItem ci=new CloudItem(c.getText(), diffval);
					diffcloud.list.add(ci);
				}
			}else{
				CloudItem ci=new CloudItem("+"+c.getText(), c.getValue());
				diffcloud.list.add(ci);
			}
		
		for (CloudItem c : this.list)
			if (diffcloud.getItemByName(c.getText())==null){
				if (cloud.getItemByName(c.getText())==null){
					CloudItem ci=new CloudItem("-"+c.getText(), c.getValue());
					diffcloud.list.add(ci);
				}
			}
		return diffcloud;
	}
	
	public String getJavascriptData(){
		String tagclouddata=" var words"+name+" = [ ";
		for (CloudItem ci : this.list){
			Double size=ci.getValue(); if (size>12) size=12.0; 
			tagclouddata+=" { text: \""+ci.getText().replace("http://dbpedia.org/resource/", "")+"\", size: "+size*12+"}, ";
			// System.out.println("de;"+key+";"+conceptFreqDe.get(key));
		}
		tagclouddata+="]; d3.layout.cloud().size([1000, 400]).words(words"+name+").rotate(0).font(\"Impact\").fontSize(function(d) { return d.size; }).on(\"end\", draw"+name+").start();";
		return tagclouddata;
	}
	
	public String getJavascriptCloud(){
		String tagcloud=" ";
		tagcloud+=" function draw"+name+"(words) {";
		tagcloud+="    d3.select(\"#demo"+name+"\").append(\"svg\") ";
		tagcloud+="        .attr(\"width\", 1000) ";
		tagcloud+="        .attr(\"height\", 400) ";
		tagcloud+="      .append(\"g\") ";
		tagcloud+="        .attr(\"transform\", \"translate(500,200)\") ";
		tagcloud+="      .selectAll(\"text\") ";
		tagcloud+="        .data(words) ";
		tagcloud+="      .enter().append(\"text\") ";
		tagcloud+="        .style(\"font-size\", function(d) { return d.size + \"px\"; }) ";
		tagcloud+="        .style(\"font-family\", \"Impact\") ";
		tagcloud+="        .style(\"fill\", function(d, i) { return \"#\"+(md5(d.text).substring(0, 6)); }) ";
		tagcloud+="        .attr(\"text-anchor\", \"middle\") ";
		tagcloud+="        .attr(\"transform\", function(d) { ";
		tagcloud+="          return \"translate(\" + [d.x, d.y] + \")rotate(\" + d.rotate + \")\";  ";
		tagcloud+="        }) ";
		tagcloud+="        .text(function(d) { return d.text; }) ";
		tagcloud+="        .on(\"click\", function(d) { ";
		tagcloud+="			show"+name+"(d.text); ";
		tagcloud+="		});; ";
		tagcloud+="  } ";
		return tagcloud;
	}

	@Override
	public String toString() {
		String result=this.title+"\n";
		result+=this.name+"\n";
		for (CloudItem c : this.list)
			result+=c.getText()+","+c.getValue()+"\n";
		return result;
	}
	
	
	
}
