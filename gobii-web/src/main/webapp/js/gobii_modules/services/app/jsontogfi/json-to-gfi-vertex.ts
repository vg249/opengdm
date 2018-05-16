import {GobiiFileItem} from "../../../model/gobii-file-item";
import {JsonToGfi} from "./json-to-gfi";
import {EntityType, entityTypefromString} from "../../../model/type-entity";
import {ExtractorItemType} from "../../../model/type-extractor-item";
import {ProcessType} from "../../../model/type-process";
import {GobiiExtractFilterType} from "../../../model/type-extractor-filter";
import {DataSet} from "../../../model/dataset";
import {FilterParams} from "../../../model/filter-params";
import {GobiiFileItemEntityRelation} from "../../../model/gobii-file-item-entity-relation";
import {FilterParamsColl} from "../../core/filter-params-coll";
import {FilterParamNames} from "../../../model/file-item-param-names";
import {Vertex} from "../../../model/vertex";

export class JsonToGfiVertex implements JsonToGfi {

    constructor(private _filterParams: FilterParams,
                private _filterParamsColl: FilterParamsColl) {
        this._filterParams = _filterParams;
        this._filterParamsColl = _filterParamsColl;
    } //ctor

    public convert(jsonItem): GobiiFileItem {


        let vertex:Vertex = new Vertex(
            jsonItem.vertexId,
            jsonItem.vertexName,
            jsonItem.gobiiEntityNameTypeName,
            jsonItem.cvGroupName,
            []
        );


        let returnVal:GobiiFileItem =
            GobiiFileItem.build(GobiiExtractFilterType.UNKNOWN, ProcessType.READ)
                .setExtractorItemType(ExtractorItemType.VERTEX)
                .setEntityType(EntityType.UNKNOWN)
                .setItemName(vertex.vertexName)
                .setItemId(vertex.vertexId.toString())
                .setEntity(vertex);

        return returnVal;
    }
}