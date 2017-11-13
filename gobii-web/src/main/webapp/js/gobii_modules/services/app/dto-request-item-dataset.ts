import {Injectable} from "@angular/core";
import {DtoRequestItem} from "./../core/dto-request-item";
import {ProcessType} from "../../model/type-process";
import {DataSet} from "../../model/dataset";


export enum DataSetSearchType {
    UNKNOWN,
    LIST,
    DETAIL
}


@Injectable()
export class DtoRequestItemDataSet implements DtoRequestItem<DataSet[]> {

    public constructor(private dataSetSearchType: DataSetSearchType,
                       private dataSetId: number) {
        this.dataSetSearchType = this.dataSetSearchType;
        this.dataSetId = dataSetId;
    }

    public getUrl(): string {

        let baseUrl: string = "gobii/v1/datasets";

        let returnVal: string = baseUrl;


        if (this.dataSetSearchType === DataSetSearchType.DETAIL)
            if (this.dataSetId) {
                returnVal = baseUrl + "/" + this.dataSetId;
            } else {
                throw Error("Query type " + DataSetSearchType[this.dataSetSearchType] + " requires a datasetId");
            }

        return returnVal;
    } // getUrl()

    private processType: ProcessType = ProcessType.READ;

    public getRequestBody(): string {

        return JSON.stringify({
            "processType": ProcessType[this.processType],
            "dataSetId": this.dataSetId
        })
    }

    public resultFromJson(json): DataSet[] {

        let returnVal: DataSet[] = [];

        if (this.dataSetSearchType === DataSetSearchType.DETAIL) {

            if (json.payload.data[0]) {
                returnVal.push(new DataSet(json.payload.data[0].dataSetId,
                    json.payload.data[0].name,
                    json.payload.data[0].experimentId,
                    json.payload.data[0].callingAnalysisId,
                    json.payload.data[0].dataTable,
                    json.payload.data[0].dataFile,
                    json.payload.data[0].qualityTable,
                    json.payload.data[0].qualityFile,
                    json.payload.data[0].status,
                    json.payload.data[0].typeId,
                    json.payload.data[0].analysesIds));
            }
        } else if (this.dataSetSearchType === DataSetSearchType.LIST) {
            json.payload.data[0].forEach(jsonItem => {
                returnVal.push(new DataSet(json.payload.data[0].dataSetId,
                    jsonItem.name,
                    jsonItem.experimentId,
                    jsonItem.callingAnalysisId,
                    jsonItem.dataTable,
                    jsonItem.dataFile,
                    jsonItem.qualityTable,
                    jsonItem.qualityFile,
                    jsonItem.status,
                    jsonItem.typeId,
                    jsonItem.analysesIds))
            });

        } else {
            throw new Error("Unknown dataset search type: " + DataSetSearchType[this.dataSetSearchType]);
        }

        return returnVal;
        //return [new NameId(1, 'foo'), new NameId(2, 'bar')];
    }


} // DtoRequestItemNameIds() 







