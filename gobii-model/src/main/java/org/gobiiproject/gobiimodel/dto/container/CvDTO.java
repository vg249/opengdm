package org.gobiiproject.gobiimodel.dto.container;

import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.entity.TableColDisplay;
import org.gobiiproject.gobiimodel.entity.TableCv;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Angel on 4/29/2016.
 */
public class CvDTO extends DtoMetaData {

	private int cv_id;
	private String group;
	private String term;
	private String definition;
	private int rank;
	public int getCv_id() {
		return cv_id;
	}
	public void setCv_id(int cv_id) {
		this.cv_id = cv_id;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public String getDefinition() {
		return definition;
	}
	public void setDefinition(String definition) {
		this.definition = definition;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
}
