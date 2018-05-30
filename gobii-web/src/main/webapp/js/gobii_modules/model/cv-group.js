System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var CvGroup, getCvGroupName;
    return {
        setters: [],
        execute: function () {
            (function (CvGroup) {
                CvGroup[CvGroup["UNKNOWN"] = 0] = "UNKNOWN";
                CvGroup[CvGroup["ANALYSIS_TYPE"] = 1] = "ANALYSIS_TYPE";
                CvGroup[CvGroup["DATASET_TYPE"] = 2] = "DATASET_TYPE";
                CvGroup[CvGroup["JOB_STATUS"] = 3] = "JOB_STATUS";
            })(CvGroup || (CvGroup = {}));
            exports_1("CvGroup", CvGroup);
            ;
            exports_1("getCvGroupName", getCvGroupName = function (cvGroup) {
                var returnVal;
                if (cvGroup === CvGroup.ANALYSIS_TYPE) {
                    returnVal = "analysis_type";
                }
                else if (cvGroup === CvGroup.DATASET_TYPE) {
                    returnVal = "dataset_type";
                }
                else if (cvGroup === CvGroup.JOB_STATUS) {
                    returnVal = "job_status";
                }
                else {
                    returnVal = "UNKNOWN CV GROUP";
                }
                return returnVal;
            });
            // export class CvFilters {
            //
            //
            //     private static cvValues: Map<CvGroup,string> = null;
            //
            //     // These are the cvgroup values for the terms that are to be retrieved
            //     public static get(cvFilterType:CvGroup): string {
            //
            //         if ( this.cvValues === null ) {
            //             this.cvValues = new Map<CvGroup,string>();
            //             this.cvValues[CvGroup.ANALYSIS_TYPE] = "analysis_type";
            //             this.cvValues[CvGroup.DATASET_TYPE] = "dataset_type";
            //             this.cvValues[CvGroup.JOB_STATUS] = "job_status";
            //         }
            //
            //         return this.cvValues[cvFilterType];
            //     }
            //
            // }
        }
    };
});
//# sourceMappingURL=cv-group.js.map