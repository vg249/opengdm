System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var DataSet;
    return {
        setters: [],
        execute: function () {
            DataSet = (function () {
                function DataSet(id, piContactId, projectId, experimentId, name, callingAnalysisId, dataTable, dataFile, qualityTable, qualityFile, status, typeId, analysesIds, createdDate, jobStatusId, jobStatusName, jobTypeId, jobTypeName, jobSubmittedDate) {
                    this.id = id;
                    this.piContactId = piContactId;
                    this.projectId = projectId;
                    this.experimentId = experimentId;
                    this.name = name;
                    this.callingAnalysisId = callingAnalysisId;
                    this.dataTable = dataTable;
                    this.dataFile = dataFile;
                    this.qualityTable = qualityTable;
                    this.qualityFile = qualityFile;
                    this.status = status;
                    this.typeId = typeId;
                    this.analysesIds = analysesIds;
                    this.createdDate = createdDate;
                    this.jobStatusId = jobStatusId;
                    this.jobStatusName = jobStatusName;
                    this.jobTypeId = jobTypeId;
                    this.jobTypeName = jobTypeName;
                    this.jobSubmittedDate = jobSubmittedDate;
                }
                return DataSet;
            }());
            exports_1("DataSet", DataSet);
        }
    };
});
//# sourceMappingURL=dataset.js.map