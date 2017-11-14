import {Injectable} from "@angular/core";
import {DtoRequestItem} from "./../core/dto-request-item";
import {ProcessType} from "../../model/type-process";
import {DataSet} from "../../model/dataset";
import {GobiiFileItem} from "../../model/gobii-file-item";
import {getGobiiExtractFilterType} from "../../store/reducers/fileitems-reducer";
import {GobiiExtractFilterType} from "../../model/type-extractor-filter";
import {EntityType} from "../../model/type-entity";
import {ExtractorItemType} from "../../model/type-extractor-item";
import {JsonToGfiDataset} from "./jsontogfi/json-to-gfi-dataset";


export enum DataSetSearchType {
    UNKNOWN,
    LIST,
    DETAIL
}


@Injectable()
export class DtoRequestItemDataSet implements DtoRequestItem<GobiiFileItem[]> {

    public constructor(private dataSetSearchType: DataSetSearchType,
                       private dataSetId: number = null) {
        this.dataSetSearchType = dataSetSearchType;
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

    public resultFromJson(json): GobiiFileItem[] {

        let returnVal: GobiiFileItem[] = [];
        let jsonToGfiDataset:JsonToGfiDataset = new JsonToGfiDataset()
        ;
        json.payload.data[0].forEach(jsonItem => {


            returnVal.push(jsonToGfiDataset.convert(jsonItem));

        });

        return returnVal;

    }
} // DtoRequestItemNameIds()







