import {Injectable} from "@angular/core";
import {DtoRequestItem} from "./../core/dto-request-item";
import {ProcessType} from "../../model/type-process";
import {DataSet} from "../../model/dataset";
import {GobiiFileItem} from "../../model/gobii-file-item";
import {EntityType} from "../../model/type-entity";
import {FilterParams} from "../../model/file-item-params";
import {JsonToGfi} from "./jsontogfi/json-to-gfi";
import {FilterType} from "../../model/filter-type";


@Injectable()
export class DtoRequestItemGfi implements DtoRequestItem<GobiiFileItem[]> {

    public constructor(private fileItemParams: FilterParams,
                       private id: string = null,
                       private jsonToGfi: JsonToGfi) {
        this.fileItemParams = fileItemParams;
        this.id = id;
        this.jsonToGfi = jsonToGfi;

        if (this.fileItemParams.getFilterType() !== FilterType.ENTITY_LIST
            && this.fileItemParams.getFilterType() !== FilterType.ENTITY_BY_ID) {
            throw new Error("The FileItemParams with ID "
                + this.fileItemParams.getQueryName()
                + " is not of type " + FilterType[FilterType.ENTITY_LIST]
                + " or " + FilterType[FilterType.ENTITY_BY_ID]);
        }
    }

    public getUrl(): string {

        let returnVal: string = "gobii/v1";

        if (this.fileItemParams.getEntityType() === EntityType.DATASET) {
            returnVal += "/datasets";
        }

        if (this.fileItemParams.getFilterType() === FilterType.ENTITY_BY_ID) {
            returnVal += "/" + this.id;
        }

        return returnVal;
    } // getUrl()

    private processType: ProcessType = ProcessType.READ;

    // this is probably not being used anymore
    public getRequestBody(): string {

        return JSON.stringify({
            "processType": ProcessType[this.processType],
            "id": this.id
        })
    }

    public resultFromJson(json): GobiiFileItem[] {

        let returnVal: GobiiFileItem[] = [];
        json.payload.data.forEach(jsonItem => {

            returnVal.push(this.jsonToGfi.convert(jsonItem));

        });

        return returnVal;

    }
} // DtoRequestItemNameIds()







