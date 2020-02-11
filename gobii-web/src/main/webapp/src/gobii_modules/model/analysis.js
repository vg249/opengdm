System.register([], function (exports_1, context_1) {
    "use strict";
    var Analysis;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [],
        execute: function () {
            Analysis = /** @class */ (function () {
                function Analysis(analysisId, analysisName, analysisDescription, anlaysisTypeId, program, programVersion, algorithm, sourceName, sourceVersion, sourceUri, referenceId, timeExecuted, status) {
                    this.analysisId = analysisId;
                    this.analysisName = analysisName;
                    this.analysisDescription = analysisDescription;
                    this.anlaysisTypeId = anlaysisTypeId;
                    this.program = program;
                    this.programVersion = programVersion;
                    this.algorithm = algorithm;
                    this.sourceName = sourceName;
                    this.sourceVersion = sourceVersion;
                    this.sourceUri = sourceUri;
                    this.referenceId = referenceId;
                    this.timeExecuted = timeExecuted;
                    this.status = status;
                }
                return Analysis;
            }());
            exports_1("Analysis", Analysis);
        }
    };
});
//# sourceMappingURL=analysis.js.map