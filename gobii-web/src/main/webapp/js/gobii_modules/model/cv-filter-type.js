System.register([], function (exports_1, context_1) {
    "use strict";
    var CvFilterType, CvFilters;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [],
        execute: function () {
            (function (CvFilterType) {
                CvFilterType[CvFilterType["UNKNOWN"] = 0] = "UNKNOWN";
                CvFilterType[CvFilterType["ANALYSIS_TYPE"] = 1] = "ANALYSIS_TYPE";
                CvFilterType[CvFilterType["DATASET_TYPE"] = 2] = "DATASET_TYPE";
                CvFilterType[CvFilterType["JOB_STATUS"] = 3] = "JOB_STATUS";
            })(CvFilterType || (CvFilterType = {}));
            exports_1("CvFilterType", CvFilterType);
            ;
            CvFilters = /** @class */ (function () {
                function CvFilters() {
                }
                // These are the cvgroup values for the terms that are to be retrieved
                CvFilters.get = function (cvFilterType) {
                    if (this.cvValues === null) {
                        this.cvValues = new Map();
                        this.cvValues[CvFilterType.ANALYSIS_TYPE] = "analysis_type";
                        this.cvValues[CvFilterType.DATASET_TYPE] = "dataset_type";
                        this.cvValues[CvFilterType.JOB_STATUS] = "job_status";
                    }
                    return this.cvValues[cvFilterType];
                };
                CvFilters.cvValues = null;
                return CvFilters;
            }());
            exports_1("CvFilters", CvFilters);
        }
    };
});
//# sourceMappingURL=cv-filter-type.js.map