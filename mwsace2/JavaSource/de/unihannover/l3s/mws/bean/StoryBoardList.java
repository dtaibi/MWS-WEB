package de.unihannover.l3s.mws.bean;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import de.unihannover.l3s.mws.dao.StoryboardDao;
import de.unihannover.l3s.mws.dao.TrackDao;
import de.unihannover.l3s.mws.model.Storyboard;
import de.unihannover.l3s.mws.model.Track;

@ManagedBean(name="storyboardlist")
@SessionScoped
public class StoryBoardList {
	
	
	List<Storyboard> list;
	@ManagedProperty(value="#{user}")
	private User user;
	boolean enabled;
	List<Storyboard> studentlist;
	
	public boolean isEnabled() {
		if (this.user.getUtente().getRole().compareTo("teacher")==0)
			return true;
	  return false;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public void loadList() {
		StoryboardDao rdao=new StoryboardDao();
		this.list=rdao.getStoryboardByUser(this.user.getUtente());
		
		TrackDao td=new TrackDao();
		Track track=new Track();
		Calendar c = new GregorianCalendar();
		track.setDate(c.getTime());
		track.setOperation("load_storyboard_list");
		track.setUtente(this.user.getUtente());
		td.addTrack(track);
		
	}
	
	public void delete(Storyboard ricerca){
		StoryboardDao rdao=new StoryboardDao();
		rdao.deleteStoryboard(ricerca);
		
		TrackDao td=new TrackDao();
		Track track=new Track();
		Calendar c = new GregorianCalendar();
		track.setDate(c.getTime());
		track.setOperation("delete_storyboard");
		track.setParam1(ricerca.getNome());
		track.setUtente(this.user.getUtente());
		td.addTrack(track);
		loadList();
	}
	
	public List<Storyboard> getList() {
		return list;
	}

	public void setList(List<Storyboard> list) {
		this.list = list;
	}
	
	public List<Storyboard> getStudentlist() {
		if (this.user.getUtente().getRole().compareTo("teacher")==0){
			StoryboardDao rdao=new StoryboardDao();
			this.studentlist=rdao.getStudentStoryboard();
			
			TrackDao td=new TrackDao();
			Track track=new Track();
			Calendar c = new GregorianCalendar();
			track.setDate(c.getTime());
			track.setOperation("load_student_storyboard_list");
			track.setUtente(this.user.getUtente());
			td.addTrack(track);
		}
		return studentlist;
	}

	public void setStudentlist(List<Storyboard> studentlist) {
		this.studentlist = studentlist;
	}
}
