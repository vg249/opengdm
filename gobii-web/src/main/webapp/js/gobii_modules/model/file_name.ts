import {GobiiExtractFilterType} from "./type-extractor-filter";
export class FileName {

    public static makeUniqueFileId(): string {

        let date: Date = new Date();
        let returnVal: string = date.getFullYear()
            + "_"
            + (date.getMonth() + 1)
            + "_"
            + date.getDay()
            + "_"
            + date.getHours()
            + "_"
            + date.getMinutes()
            + "_"
            + date.getSeconds();


        return returnVal;

    };

    public static makeFileNameFromJobId(gobiiExtractFilterType:GobiiExtractFilterType, jobId:string): string {

        let returnVal:string;

        let suffix = null;

        if( gobiiExtractFilterType === GobiiExtractFilterType.BY_MARKER ) {
            suffix = "_markers";
        } else {
            suffix = "_samples";
        }

        returnVal = jobId + suffix;

        return returnVal;
    }
}   