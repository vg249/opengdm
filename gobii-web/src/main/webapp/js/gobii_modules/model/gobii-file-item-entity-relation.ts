import {EntitySubType, EntityType} from "./type-entity";
import {CvFilterType} from "./cv-filter-type";
import {GobiiExtractFilterType} from "./type-extractor-filter";
import {ExtractorItemType} from "./type-extractor-item";
import {GobiiFileItemCompoundId} from "./gobii-file-item-compound-id";

export class GobiiFileItemEntityRelation extends GobiiFileItemCompoundId {

    constructor(extractorItemType: ExtractorItemType = ExtractorItemType.UNKNOWN,
                entityType: EntityType = EntityType.UNKNOWN,
                entitySubType: EntitySubType = EntitySubType.UNKNOWN,
                cvFilterType: CvFilterType = CvFilterType.UNKNOWN,
                cvFilterValue:string = null,
                private relatedEntityIds: string = null) {

        super(extractorItemType, entityType, entitySubType, cvFilterType, cvFilterValue);
    }

    public static fromGobiiFileItemCompoundId(gobiiFileItemCompoundId: GobiiFileItemCompoundId): GobiiFileItemEntityRelation {

        return new GobiiFileItemEntityRelation(gobiiFileItemCompoundId.getExtractorItemType(),
            gobiiFileItemCompoundId.getEntityType(),
            gobiiFileItemCompoundId.getEntitySubType(),
            gobiiFileItemCompoundId.getCvFilterType(),
            null);

    }

    public getRelatedEntityId(): string {
        return this.relatedEntityIds;
    }

    public setRelatedEntityId(relatedEntityId: string): GobiiFileItemEntityRelation {
        this.relatedEntityIds = relatedEntityId;
        return this;
    }

}
