import {GobiiFileType} from "../type-gobii-file"

export class GobiiDataSetExtract {

    constructor(dataSetId:number, dataSetName:string) {
        this.dataSetName = dataSetName;
        this.dataSetId = dataSetId;
    } // ctor 

    private gobiiFileType:GobiiFileType = null;
    private accolate:boolean = false;
    private dataSetName:string = null;
    private dataSetId:number = null;


    public getGobiiFileType():GobiiFileType {
        return this.gobiiFileType;
    }

    public  setGobiiFileType(gobiiFileType:GobiiFileType):void {
        this.gobiiFileType = gobiiFileType;
    }

    public isAccolate():boolean {
        return this.accolate;
    }

    public setAccolate(accolate:boolean):void {
        this.accolate = accolate;
    }

    public getDataSetName():string {
        return this.dataSetName;
    }

    public setDataSetName(dataSetName:string):void {
        this.dataSetName = dataSetName;
    }

    public getDataSetId():number {
        return this.dataSetId;
    }

    public setDataSetId(dataSetId:number):void {
        this.dataSetId = dataSetId;
    }
}