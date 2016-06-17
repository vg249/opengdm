import {GobiiDataSetExtract} from "./data-set-extract"

export class GobiiExtractorInstruction {

    constructor(private extractDestinationDirectory:string,
                private dataSetExtracts:GobiiDataSetExtract[],
                private contactId:number,
                private contactEmail:string) {

        this.extractDestinationDirectory = extractDestinationDirectory;
        this.dataSetExtracts = dataSetExtracts;
    }

    public getExtractDestinationDirectory():string {
        return this.extractDestinationDirectory;
    }

    public setExtractDestinationDirectory(value:string) {
        this.extractDestinationDirectory = value;
    }

    public getDataSetExtracts():any {
        return this.dataSetExtracts;
    }

    public setDataSetExtracts(value:any) {
        this.dataSetExtracts = value;
    }

    public getContactId():Number {
        return this.contactId;
    }

    public setContactId(contactId:number):void {
        this.contactId = contactId;
    }

    public setContactEmail(contactEmail:string):void {
        this.contactEmail = contactEmail;
    }

    public getContactEmail(): string {
        return this.contactEmail;
    }

    public getJson():any {

        let returnVal:any = {};

        returnVal.extractDestinationDirectory = this.extractDestinationDirectory;
        returnVal.contactId = this.contactId;
        returnVal.contactEmail = this.contactEmail;

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
            dataSetExtracts,
            json.contactId,
            json.contactEmail
        );

        return returnVal;

    }


}