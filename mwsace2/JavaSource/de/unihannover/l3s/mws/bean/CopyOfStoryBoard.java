package de.unihannover.l3s.mws.bean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import de.unihannover.l3s.mws.dao.StoryboardDao;
import de.unihannover.l3s.mws.dao.TrackDao;
import de.unihannover.l3s.mws.model.Storyboard;
import de.unihannover.l3s.mws.model.StoryboardItem;
import de.unihannover.l3s.mws.model.Track;

@ManagedBean(name="copyofstoryboard")
@ViewScoped
public class CopyOfStoryBoard {
	
	// List<Storyboard> list;
	
	
	// Long sbId;
	
	@ManagedProperty(value="#{storyboardlist}")
	private StoryBoardList storyboardl;
	
	public StoryBoardList getStoryboardl() {
		return storyboardl;
	}

	public void setStoryboardl(StoryBoardList storyboardl) {
		this.storyboardl = storyboardl;
	}
	
	// Storyboard content;
	
	@ManagedProperty(value="#{user}")
	private User user;
	
	
	// private String storyboardsel;
	// private List<Storyboard> userStoryboards;
	// private String storybname;
	// private List<StoryboardItem> newstoryboardlist;
	// private StoryboardItem[] itemByPos;
	// private List<StoryboardItem> storyboardlist;
	
	private String storydata;
	
	/* public List<StoryboardItem> getStoryboardlist() {
		return storyboardlist;
	}

	public void setStoryboardlist(List<StoryboardItem> storyboardlist) {
		this.storyboardlist = storyboardlist;
	} */
	
	public String getStorydata() {
		return storydata;
	}

	public void setStorydata(String storydata) {
		this.storydata = storydata;
	}

	/* public List<Storyboard>  getUserStoryboards() {
		return userStoryboards;
	}

	public void setUserStoryboards(List<Storyboard> userStoryboards) {
		this.userStoryboards = userStoryboards;
	}
	
	public String getStoryboardsel() {
		return storyboardsel;
	}

	public void setStoryboardsel(String storyboardsel) {
		this.storyboardsel = storyboardsel;
	} */
	
	/* public String getStorybname() {
		return storybname;
	}

	public void setStorybname(String storybname) {
		this.storybname = storybname;
	} */
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	/* public Long getSbId() {
		return sbId;
	}

	public void setSbId(Long sbId) {
		this.sbId = sbId;
	} */
	
	
	
	
	/* public void loadList() {
		StoryboardDao rdao=new StoryboardDao();
		this.list=rdao.getStoryboardByUser(this.user.getUtente());
		
		TrackDao td=new TrackDao();
		Track track=new Track();
		Calendar c = new GregorianCalendar();
		track.setDate(c.getTime());
		track.setOperation("load_storyboard_list");
		track.setUtente(this.user.getUtente());
		td.addTrack(track);
		
	} */

	/* public List<Storyboard> getList() {
		return list;
	}

	public void setList(List<Storyboard> list) {
		this.list = list;
	} */
	
	public Storyboard getContent() {
		return storyboardl.getContent();
	}

	/* public void setContent(Storyboard content) {
		this.content = content;
	} */
	
	public void saveComment(StoryboardItem sbi){
		StoryboardDao rd=new StoryboardDao();
		rd.saveStoryboard(sbi);
		System.out.println("Comment:"+sbi.getComment());
		TrackDao td=new TrackDao();
		Track track=new Track();
		Calendar c = new GregorianCalendar();
		track.setDate(c.getTime());
		track.setOperation("save_comment");
		track.setParam1(storyboardl.getContent().getNome());
		track.setUtente(this.user.getUtente());
		td.addTrack(track);
	}
	
	/* @PostConstruct
	public void getSlides(){
		// List<StoryboardItem> results=new ArrayList<StoryboardItem>();
		System.out.println("GET_SLIDES");
		
		// if (!FacesContext.getCurrentInstance().isPostback() || this.sbId==null){
		if (!FacesContext.getCurrentInstance().isPostback()){
				// this.sbId = storyboardl.getSbId();
				// System.out.println("GET_SLIDES_POSTBACK:"+this.sbId);
				System.out.println("GET_SLIDES_POSTBACK_2:"+this.storyboardl.getContent());
				/* if (this.sbId==null){
					this.sbId = Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("storyboardId"));
				} */
					/* if (sbi.getType().compareTo("web")==0)
						sbi.setWebcontent("<iframe width=\"90%\" height=\"90%\" src=\""+sbi.getContent()+"\" ></iframe>");
					else if (sbi.getType().compareTo("image")==0)
						sbi.setWebcontent("<iframe width=\"90%\" height=\"90%\" src=\""+sbi.getContent()+"\" ></iframe>");
					else if (sbi.getType().compareTo("video")==0)
						sbi.setWebcontent("<iframe width=\"90%\" height=\"90%\" src=\""+sbi.getContent()+"\" ></iframe>");
					*/
				// }
				
				// storyboardlist = storyboardl.getContent().getStoryboardItem();
				// this.storybname = storyboardl.getContent().getNome();
		// }
		
		// return results;
	// }

	/* public void saveNewStoryboard(){
		content = null;
		StoryboardDao sbd=new StoryboardDao();
		while (sbd.getStoryboardByName(this.storybname)!=null){
			this.storybname = this.storybname+"_copy";
		}
		
		List<StoryboardItem> clone = new ArrayList<StoryboardItem>(content.getStoryboardItem().size());
	    for(StoryboardItem item: content.getStoryboardItem()) clone.add(item.clone());
		
	    content.getStoryboardItem().clear();
	    content.setStoryboardItem(clone);
		
		saveStoryboard();
	} */
	
	public void saveStoryboard(){
		//if (storyboardl.getContent()==null)
		//	storyboardl.getContent()=new Storyboard();
		// storyboardl.getContent().setNome(this.storybname);
		for (StoryboardItem sbi : storyboardl.getContent().getStoryboardItem()) {
			// sbi.setId(null);
			sbi.setStoryboard(storyboardl.getContent());
		}
		// storyboardl.getContent().setStoryboardItem(storyboardlist);
		storyboardl.getContent().setUtente(this.user.getUtente());
		storyboardl.getContent().setData((new GregorianCalendar()).getTime());
		StoryboardDao rd=new StoryboardDao();
		rd.addStoryboard(storyboardl.getContent());
		System.out.println("Storyboard Saved");
		TrackDao td=new TrackDao();
		Track track=new Track();
		Calendar c = new GregorianCalendar();
		track.setDate(c.getTime());
		track.setOperation("save_storyboard");
		track.setParam1(this.storyboardl.getContent().getNome());
		track.setUtente(this.user.getUtente());
		td.addTrack(track);
	}
	
	/*public void openStoryboard(){
		StoryboardDao sbd=new StoryboardDao();
		content=sbd.getStoryboardById(Long.parseLong(storyboardsel));
		storybname = content.getNome();
		for (StoryboardItem sbi : content.getStoryboardItem()) {
			sbi.setPos(Long.parseLong(""+newstoryboardlist.size()));
			newstoryboardlist.add(sbi);
		}
		storyboardlist = newstoryboardlist;
		
		TrackDao td=new TrackDao();
		Track track=new Track();
		Calendar c = new GregorianCalendar();
		track.setDate(c.getTime());
		track.setOperation("open_storyboard");
		track.setParam1(this.storybname);
		track.setUtente(this.user.getUtente());
		td.addTrack(track);
	}*/
	public void RemoveWebRes(StoryboardItem sr) {
		// int lung= storyboardl.getContent().getStoryboardItem().size(); 
		storyboardl.getContent().getStoryboardItem().remove(sr);
		
		StoryboardDao rd=new StoryboardDao();
		rd.deleteStoryboardItem(sr);
		
		/* int k=0;
		for (int i=0;i<lung;i++) {
			StoryboardItem sbi=getPos(storyboardl.getContent().getStoryboardItem(),i);
			if (sbi!=null){
				sbi.setPos(Long.parseLong(""+k));
				k++;
			}
		} */
		long i=0;
		for (StoryboardItem sbi : storyboardl.getContent().getStoryboardItem()) {
			sbi.setPos(i);
			i++;
		}
		rd.addStoryboard(storyboardl.getContent());
		TrackDao td=new TrackDao();
		Track track=new Track();
		Calendar c = new GregorianCalendar();
		track.setDate(c.getTime());
		track.setOperation("delete_storyboard_item"+sr.getTitle());
		track.setParam1(this.storyboardl.getContent().getNome());
		track.setUtente(this.user.getUtente());
		td.addTrack(track);
	}
	public void reorderList() {
		FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("formtestuni");
		FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("formtest2");
			
		// UIComponent target = event.getComponent().findComponent("yourtarget");  
		// FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add(target.getClientId()
				  
		List<StoryboardItem> newstoryboardlist = new ArrayList<StoryboardItem>();
		System.out.println(storydata);
		storydata=storydata.replace("widget[]=", "");
		storydata=storydata.replace("col1:","");
		String[] pos=storydata.split("&");
		System.out.println("");
		int i=0;
		for (String s : pos) {
			
			int id=Integer.parseInt(s);
			// StoryboardItem sbi=storyboardlist.get(id);
			StoryboardItem sbi=storyboardl.getContent().getStoryboardItem().get(id);
			System.out.println("Reordering:"+s+" - "+sbi.getTitle());
			// StoryboardItem ssbi=sbi.clone();
			sbi.setPos(Long.parseLong(""+i));
			// ssbi.setId(sbi.getId());
			newstoryboardlist.add(sbi);
			i++;
		}
		storyboardl.getContent().getStoryboardItem().clear();
		storyboardl.getContent().setStoryboardItem(newstoryboardlist);
		storyboardl.getContent().setUtente(this.user.getUtente());
		storyboardl.getContent().setData((new GregorianCalendar()).getTime());
		StoryboardDao rd=new StoryboardDao();
		rd.addStoryboard(storyboardl.getContent());
		// storyboardlist = new ArrayList<StoryboardItem>(newstoryboardlist);
		// content.setStoryboardItem(newstoryboardlist);
	}

	/* private StoryboardItem getPos(List<StoryboardItem> list, int id) {
		for (StoryboardItem si : list)
			if (si.getPos()==id)
				return si;
		return null;
	} */
	
	/* public List<StoryboardItem> getItemByPos() {
		itemByPos=new StoryboardItem[storyboardlist.size()];
		List<Integer> listidx=new ArrayList<Integer>(); 
		for (StoryboardItem si : storyboardlist)
			listidx.add(si.getPos().intValue());
		Collections.sort(listidx);
		int i=0;
		for (Integer idx : listidx){
			itemByPos[i] = getPos(storyboardlist,idx);
			i++;
		}
		return Arrays.asList(itemByPos);
	} */
}
