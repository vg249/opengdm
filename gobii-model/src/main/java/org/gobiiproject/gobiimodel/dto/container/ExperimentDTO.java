package org.gobiiproject.gobiimodel.dto.container;

import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.project.ProjectProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Angel on 4/13/2016.
 */
public class ExperimentDTO extends DtoMetaData {
    	private int experimentId;
    	private String experimentName = null;
    	private String experimentCode = null;
    	private String experimentDataFile = null;

		private Map<String,String> projects = null;
		private Map<String,String> platforms = null;
		private Map<String,String> manifest = null;
		

		
		public int getExperimentId() {
			return experimentId;
		}
		public void setExperimentId(int experimentId) {
			this.experimentId = experimentId;
		}
		public String getExperimentName() {
			return experimentName;
		}
		public void setExperimentName(String experimentName) {
			this.experimentName = experimentName;
		}
		public String getExperimentCode() {
			return experimentCode;
		}
		public void setExperimentCode(String experimentCode) {
			this.experimentCode = experimentCode;
		}
		public String getExperimentDataFile() {
			return experimentDataFile;
		}
		public void setExperimentDataFile(String dataFile) {
			this.experimentDataFile = dataFile;
		}
		public Map<String, String> getProjects() {
			return projects;
		}
		public void setProjects(Map<String, String> projects) {
			this.projects = projects;
		}
		public Map<String, String> getPlatforms() {
			return platforms;
		}
		public void setPlatforms(Map<String, String> platforms) {
			this.platforms = platforms;
		}
		public Map<String, String> getManifest() {
			return manifest;
		}
		public void setManifest(Map<String, String> manifest) {
			this.manifest = manifest;
		}
		
		
		
}
