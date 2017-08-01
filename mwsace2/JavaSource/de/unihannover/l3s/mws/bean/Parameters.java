package de.unihannover.l3s.mws.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parameters {
	private List<String> modifierList=new ArrayList<String>(
			Arrays.asList("none", "log", "log1p", "log2p", "ln", "ln1p", "ln2p", "square", "sqrt", "reciprocal"));
	
	private List<String> score_modeList=new ArrayList<String>(
			Arrays.asList("multiply","sum","avg","first","max","min"));
	
	private List<String> boost_modeList=new ArrayList<String>(
			Arrays.asList("multiply","replace","sum","avg","max","min"));

	public List<String> getModifierList() {
		return modifierList;
	}

	public void setModifierList(List<String> modifierList) {
		this.modifierList = modifierList;
	}

	public List<String> getScore_modeList() {
		return score_modeList;
	}

	public void setScore_modeList(List<String> score_modeList) {
		this.score_modeList = score_modeList;
	}

	public List<String> getBoost_modeList() {
		return boost_modeList;
	}

	public void setBoost_modeList(List<String> boost_modeList) {
		this.boost_modeList = boost_modeList;
	}
	
	
}
