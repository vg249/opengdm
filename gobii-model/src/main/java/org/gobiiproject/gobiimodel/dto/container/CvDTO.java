package org.gobiiproject.gobiimodel.dto.container;

import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityParam;
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

	@GobiiEntityParam(paramName = "cvId")
	public int getCvId() {
		return cv_id;
	}

	@GobiiEntityColumn(columnName = "cv_id")
	public void setcvId(int cv_id) {
		this.cv_id = cv_id;
	}

	@GobiiEntityParam(paramName = "group")
	public String getGroup() {
		return group;
	}

	@GobiiEntityColumn(columnName = "group")
	public void setGroup(String group) {
		this.group = group;
	}

	@GobiiEntityParam(paramName = "term")
	public String getTerm() {
		return term;
	}

	@GobiiEntityColumn(columnName = "term")
	public void setTerm(String term) {
		this.term = term;
	}

	@GobiiEntityParam(paramName = "definition")
	public String getDefinition() {
		return definition;
	}

	@GobiiEntityColumn(columnName = "definition")
	public void setDefinition(String definition) {
		this.definition = definition;
	}

	@GobiiEntityParam(paramName = "rank")
	public int getRank() {
		return rank;
	}

	@GobiiEntityColumn(columnName = "rank")
	public void setRank(int rank) {
		this.rank = rank;
	}

}
