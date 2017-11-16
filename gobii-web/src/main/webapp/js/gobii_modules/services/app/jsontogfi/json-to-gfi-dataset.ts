import {GobiiFileItem} from "../../../model/gobii-file-item";
import {JsonToGfi} from "./json-to-gfi";
import {EntityType} from "../../../model/type-entity";
import {ExtractorItemType} from "../../../model/type-extractor-item";
import {ProcessType} from "../../../model/type-process";
import {GobiiExtractFilterType} from "../../../model/type-extractor-filter";
import {DataSet} from "../../../model/dataset";
import {FilterParams} from "../../../model/file-item-params";

export class JsonToGfiDataset implements JsonToGfi {

    constructor(private _filterParams:FilterParams) {
        this._filterParams = _filterParams;
    } //ctor

    public convert(jsonItem): GobiiFileItem {

        let dataset: DataSet = new DataSet(
            jsonItem.id,
            null,
            null,
            jsonItem.experimentId,
            jsonItem.name,
            jsonItem.callingAnalysisId,
            jsonItem.dataTable,
            jsonItem.dataFile,
            jsonItem.qualityTable,
            jsonItem.qualityFile,
            jsonItem.status,
            jsonItem.typeId,
            jsonItem.analysesIds,
            new Date(jsonItem.createdDate),
            jsonItem.jobStatusId,
            jsonItem.jobStatusName,
            jsonItem.jobSubmittedDate ? new Date(jsonItem.jobSubmittedDate) : null);

        // note that we are the parent id to the job status ID. This is because
        // the primary filtering use case is by job status.
        return GobiiFileItem.build(
            GobiiExtractFilterType.UNKNOWN, ProcessType.READ
        ).setExtractorItemType(ExtractorItemType.ENTITY)
            .setEntityType(EntityType.DATASET)
            .setItemName(dataset.name)
            .setItemId(dataset.id.toString())
            .setEntity(dataset);
    }
}