import {GobiiFileType} from "../type-gobii-file"
import {GobiiExtractFilterType} from "../type-extractor-filter";
import {GobiiSampleListType} from "../type-extractor-sample-list";

export class GobiiDataSetExtract {

    public constructor(public _gobiiFileType: GobiiFileType,
                       public _accolate: boolean,
                       public _dataSetId: number,
                       public _dataSetName: string,
                       public _extractDestinationDirectory: string,
                       public _gobiiExtractFilterType: GobiiExtractFilterType,
                       public _markerList: string[],
                       public _sampleList: string[],
                       public _listFileName: string,
                       public _gobiiSampleListType: GobiiSampleListType,
                       public _gobiiDatasetType: string,
                       public _platformIds: number[]) {

        // this.setGobiiFileType(_gobiiFileType);
        // this.setAccolate(_accolate);
        // this.setDataSetId(_dataSetId);
        // this.setDataSetName(_dataSetName);
        // this.setExtractDestinationDirectory(_extractDestinationDirectory);
        // this.setGobiiFileType(_gobiiExtractFilterType);
        //

    } // ctor 


    public getgobiiFileType(): GobiiFileType {
        return this._gobiiFileType;
    }

    public setgobiiFileType(value: GobiiFileType) {
        this._gobiiFileType = value;
    }

    public getaccolate(): boolean {
        return this._accolate;
    }

    public setaccolate(value: boolean) {
        this._accolate = value;
    }

    public getdataSetId(): number {
        return this._dataSetId;
    }

    public setdataSetId(value: number) {
        this._dataSetId = value;
    }

    public getdataSetName(): string {
        return this._dataSetName;
    }

    public setdataSetName(value: string) {
        this._dataSetName = value;
    }

    public getextractDestinationDirectory(): string {
        return this._extractDestinationDirectory;
    }

    public setextractDestinationDirectory(value: string) {
        this._extractDestinationDirectory = value;
    }

    public getgobiiExtractFilterType(): GobiiExtractFilterType {
        return this._gobiiExtractFilterType;
    }

    public setgobiiExtractFilterType(value: GobiiExtractFilterType) {
        this._gobiiExtractFilterType = value;
    }

    public getmarkerList(): string[] {
        return this._markerList;
    }

    public setmarkerList(value: string[]) {
        this._markerList = value;
    }

    public getsampleList(): string[] {
        return this._sampleList;
    }

    public setsampleList(value: string[]) {
        this._sampleList = value;
    }

    public getlistFileName(): string {
        return this._listFileName;
    }

    public setlistFileName(value: string) {
        this._listFileName = value;
    }

    public getgobiiSampleListType(): GobiiSampleListType {
        return this._gobiiSampleListType;
    }

    public setgobiiSampleListType(value: GobiiSampleListType) {
        this._gobiiSampleListType = value;
    }

    public getgobiiDatasetType(): string {
        return this._gobiiDatasetType;
    }

    public setgobiiDatasetType(value: string) {
        this._gobiiDatasetType = value;
    }

    public getplatformIds(): number[] {
        return this._platformIds;
    }

    public setplatformIds(value: number[]) {
        this._platformIds = value;
    }


    public getJson(): any {

        let returnVal: any = {};

        returnVal._gobiiFileType = this._gobiiFileType;
        returnVal._accolate = this._accolate;
        returnVal._dataSetId = this._dataSetId;
        returnVal._dataSetName = this._dataSetName;
        returnVal._extractDestinationDirectory = this._extractDestinationDirectory;
        returnVal._gobiiExtractFilterType = this._gobiiExtractFilterType;
        returnVal._markerList = this._markerList;
        returnVal._sampleList = this._sampleList;
        returnVal._listFileName = this._listFileName;
        returnVal._gobiiSampleListType = this._gobiiSampleListType;
        returnVal._gobiiDatasetType = this._gobiiDatasetType;
        returnVal._platformIds = this._platformIds;

        return returnVal;
    }

    public static fromJson(json: any): GobiiDataSetExtract {

        let returnVal: GobiiDataSetExtract =
            new GobiiDataSetExtract(
                json._gobiiFileType,
                json._accolate,
                json._dataSetId,
                json._dataSetName,
                json._extractDestinationDirectory,
                json._gobiiExtractFilterType,
                json._markerList,
                json._sampleList,
                json._listFileName,
                json._gobiiSampleListType,
                json._gobiiDatasetType,
                json._platformIds);

        return returnVal;
    }
}