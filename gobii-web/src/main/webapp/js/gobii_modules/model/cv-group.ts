import {EntityType} from "./type-entity";

export enum CvGroup {UNKNOWN,
ANALYSIS_TYPE,
DATASET_TYPE,
JOB_STATUS};



export const getCvGroupName = function (cvGroup:CvGroup): string {

    let returnVal: string;

    if(cvGroup === CvGroup.ANALYSIS_TYPE) {

        returnVal = "analysis_type";

    } else if(cvGroup === CvGroup.DATASET_TYPE) {

        returnVal = "dataset_type";

    } else if(cvGroup === CvGroup.JOB_STATUS ) {

        returnVal = "job_status";

    } else {
        returnVal = "UNKNOWN CV GROUP";
    }


    return returnVal;

}

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

