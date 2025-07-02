import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

public class Update implements Comparable<Update>{

	private String type;
	private int version;
	private Date releasedate;
	private String link;
	private Update assDependency;
	private Update jamDependency;
	private Update fibDependency;
	private SimpleDateFormat sdformat;
	
	public Update(String type, int version, String releasedate) {
		this(type, version, releasedate, null);
	}
	
	public Update(String type, int version, String releasedate, String link) {
		this(type, version, releasedate, link, null);
	}
	
	public Update(String type, int version, String releasedate, String link, Update assDependency) {
		this(type, version, releasedate, link, null, null);
	}
	
	public Update(String type, int version, String releasedate, String link, Update assDependency, Update jamDependency) {
		this(type, version, releasedate, link, null, null, null);
	}
	
	public Update(String type, int version, String releasedate, String link, Update assDependency, Update jamDependency, Update fibDependency) {
		this.type = type;
		this.version = version;
		sdformat = new SimpleDateFormat("dd.MM.yyyy");
		try {this.releasedate = sdformat.parse(releasedate);} catch (ParseException e) {
			this.releasedate = new Date();
			JOptionPane.showMessageDialog(null, e.toString());
			e.printStackTrace();
		}
		this.link = link;
		this.assDependency = assDependency;
		this.jamDependency = jamDependency;
		this.fibDependency = fibDependency;
	}

	public Update getAssDependency() {
		return assDependency;
	}
	
	public boolean hasAssDependency() {
		if (this.assDependency == null) return false;
		return true;
	}

	public void setAssDependency(Update assDependency) {
		this.assDependency = assDependency;
	}

	public Update getJamDependency() {
		return jamDependency;
	}
	
	public boolean hasJamDependency() {
		if (this.jamDependency == null) return false;
		return true;
	}

	public void setJamDependency(Update jamDependency) {
		this.jamDependency = jamDependency;
	}

	public Update getFibDependency() {
		return fibDependency;
	}
	
	public boolean hasFibDependency() {
		if (this.fibDependency == null) return false;
		return true;
	}

	public void setFibDependency(Update fibDependency) {
		this.fibDependency = fibDependency;
	}
	
	public int getVersion() {
		return version;
	}
	
	public String getType() {
		return type;
	}

	public String getReleasedate() {
		return sdformat.format(this.releasedate);
	}
	
	public Date getReleasedateAsDate() {
		return this.releasedate;
	}
	
	public String getLink() {
		return this.link;
	}
	
	public boolean hasLink() {
		if (this.link == null) return false;
		return true;
	}
	
	@Override
	public String toString() {
		
		String returnStr = type + " " + this.version + " (" + this.getReleasedate() + ")";

		return (returnStr);
	}
	
	public String getPrintableDependencys() {
		String returnStr = "";
		
		if (this.getAssDependency() != null || this.getJamDependency() != null || this.getFibDependency() != null) {
			returnStr += "  |";
		}
		
		if (this.getAssDependency() != null) {
			returnStr += "  \n  |__ assDependency: " + this.getAssDependency().getVersion() + " (" + this.getAssDependency().getReleasedate() + ")";
		}
		
		if (this.getJamDependency() != null) {
			returnStr += "  \n  |__ jamDependency: " + this.getJamDependency().getVersion() + " (" + this.getJamDependency().getReleasedate() + ")";
		}
		if (this.getFibDependency() != null) {
			returnStr += "  \n  |__ fibDependency: " + this.getFibDependency().getVersion() + " (" + this.getFibDependency().getReleasedate() + ")";
		}
		return returnStr;
	}

	@Override
	public int compareTo(Update o) {
		return getReleasedateAsDate().compareTo(o.getReleasedateAsDate());
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof Update)) return false;
		Update c = (Update) o;
		return c.toString().equals(this.toString());
	}
	
	public int findIndexOfMeIn(ArrayList<Update> list) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getType().equals(this.getType()) == false) continue;
			if (list.get(i).getVersion() == this.getVersion() == false) continue;
			if (list.get(i).getReleasedate().equals(this.getReleasedate()) == false) continue;
			return i;
		}
		return -1;
	}
}