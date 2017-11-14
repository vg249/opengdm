import {GobiiFileItem} from "../../../model/gobii-file-item";
import {JsonToGfi} from "./json-to-gfi";
import {EntityType} from "../../../model/type-entity";
import {ExtractorItemType} from "../../../model/type-extractor-item";
import {ProcessType} from "../../../model/type-process";
import {GobiiExtractFilterType} from "../../../model/type-extractor-filter";
import {DataSet} from "../../../model/dataset";

export class JsonToGfiDataset implements JsonToGfi {

    public convert(jsonItem): GobiiFileItem {

        let dataset: DataSet = new DataSet(jsonItem.datasetId,
            jsonItem.name,
            jsonItem.experimentId,
            jsonItem.callingAnalysisId,
            jsonItem.dataTable,
            jsonItem.dataFile,
            jsonItem.qualityTable,
            jsonItem.qualityFile,
            jsonItem.status,
            jsonItem.typeId,
            jsonItem.analysesIds);

        return GobiiFileItem.build(
            GobiiExtractFilterType.UNKNOWN, ProcessType.READ
        ).setExtractorItemType(ExtractorItemType.ENTITY)
            .setEntityType(EntityType.DATASET)
            .setItemName(dataset.name)
            .setItemId(dataset.datasetId.toString())
            .setEntity(dataset);
    }
}