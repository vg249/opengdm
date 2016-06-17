import {GobiiDataSetExtract} from "./data-set-extract"

export class GobiiExtractorInstruction {

    constructor(private extractDestinationDirectory:string,
                private dataSetExtracts:GobiiDataSetExtract[]) {

        this.extractDestinationDirectory = extractDestinationDirectory;
        this.dataSetExtracts = dataSetExtracts;
    }

    getExtractDestinationDirectory():string {
        return this.extractDestinationDirectory;
    }

    setExtractDestinationDirectory(value:string) {
        this.extractDestinationDirectory = value;
    }

    getDataSetExtracts():any {
        return this.dataSetExtracts;
    }

    setDataSetExtracts(value:any) {
        this.dataSetExtracts = value;
    }

    public getJson():any {

        let returnVal:any = {};

        returnVal.extractDestinationDirectory = this.extractDestinationDirectory;
        returnVal.dataSetExtracts = [];

        this.dataSetExtracts.forEach(e => {
            returnVal.dataSetExtracts.push(e.getJson());
        });

        return returnVal;
    }

    public static fromJson(json:any):GobiiExtractorInstruction {

        let dataSetExtracts:GobiiDataSetExtract[] = [];


        json.dataSetExtracts.forEach(e => dataSetExtracts.push(GobiiDataSetExtract.fromJson(e)));

        let returnVal:GobiiExtractorInstruction = new GobiiExtractorInstruction(
            json.extractDestinationDirectory,
            dataSetExtracts
        );

        return returnVal;

    }


}