package org.gobiiproject.gobiiprocess.digester.csv.matrixValidation;

public class ValidationResult {
		public boolean success;
		public int numRows;
		ValidationResult(boolean success,int numRows){
			this.success=success;
			this.numRows=numRows;
		}
		//In-line setter
		ValidationResult success(boolean success){
			this.success=success;
			return this;
		}
	}