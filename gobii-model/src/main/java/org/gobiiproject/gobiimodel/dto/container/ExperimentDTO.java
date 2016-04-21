package org.gobiiproject.gobiimodel.dto.container;

import org.gobiiproject.gobiimodel.dto.DtoMetaData;

/**
 * Created by Angel on 4/13/2016.
 */
public class ExperimentDTO extends DtoMetaData {
    	private int experimentId;
    	private String experimentName = null;
    	private String experimentCode = null;
    	private String experimentDataFile = null;

		private int projectId;
		private int platformId;
		private int manifestId;
		

		
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
		public int getProjectId() {
			return projectId;
		}
		public void setProjectId(int projectId) {
			this.projectId = projectId;
		}
		public int getPlatformId() {
			return platformId;
		}
		public void setPlatformId(int platformId) {
			this.platformId = platformId;
		}
		public int getManifestId() {
			return manifestId;
		}
		public void setManifestId(int manifestId) {
			this.manifestId = manifestId;
		}
		
		
		
		
}
