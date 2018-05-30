import {EntitySubType, EntityType} from "./type-entity";
import {CvGroup} from "./cv-group";
import {GobiiExtractFilterType} from "./type-extractor-filter";
import {ExtractorItemType} from "./type-extractor-item";
import {GobiiFileItemCompoundId} from "./gobii-file-item-compound-id";

export class GobiiFileItemEntityRelation extends GobiiFileItemCompoundId {

    constructor(extractorItemType: ExtractorItemType = ExtractorItemType.UNKNOWN,
                entityType: EntityType = EntityType.UNKNOWN,
                entitySubType: EntitySubType = EntitySubType.UNKNOWN,
                cvFilterType: CvGroup = CvGroup.UNKNOWN,
                cvFilterValue:string = null,
                private relatedEntityId: string = null) {

        super(extractorItemType, entityType, entitySubType, cvFilterType, cvFilterValue);
    }

    public static fromGobiiFileItemCompoundId(gobiiFileItemCompoundId: GobiiFileItemCompoundId): GobiiFileItemEntityRelation {

        return new GobiiFileItemEntityRelation(gobiiFileItemCompoundId.getExtractorItemType(),
            gobiiFileItemCompoundId.getEntityType(),
            gobiiFileItemCompoundId.getEntitySubType(),
            gobiiFileItemCompoundId.getCvGroup(),
            gobiiFileItemCompoundId.getCvFilterValue());

    }

    public getRelatedEntityId(): string {
        return this.relatedEntityId;
    }

    public setRelatedEntityId(relatedEntityId: string): GobiiFileItemEntityRelation {
        this.relatedEntityId = relatedEntityId;
        return this;
    }

}
