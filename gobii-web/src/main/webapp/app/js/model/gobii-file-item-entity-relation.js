System.register(["./type-entity", "./cv-filter-type", "./type-extractor-item", "./gobii-file-item-compound-id"], function (exports_1, context_1) {
    "use strict";
    var type_entity_1, cv_filter_type_1, type_extractor_item_1, gobii_file_item_compound_id_1, GobiiFileItemEntityRelation;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (cv_filter_type_1_1) {
                cv_filter_type_1 = cv_filter_type_1_1;
            },
            function (type_extractor_item_1_1) {
                type_extractor_item_1 = type_extractor_item_1_1;
            },
            function (gobii_file_item_compound_id_1_1) {
                gobii_file_item_compound_id_1 = gobii_file_item_compound_id_1_1;
            }
        ],
        execute: function () {
            GobiiFileItemEntityRelation = class GobiiFileItemEntityRelation extends gobii_file_item_compound_id_1.GobiiFileItemCompoundId {
                constructor(extractorItemType = type_extractor_item_1.ExtractorItemType.UNKNOWN, entityType = type_entity_1.EntityType.UNKNOWN, entitySubType = type_entity_1.EntitySubType.UNKNOWN, cvFilterType = cv_filter_type_1.CvFilterType.UNKNOWN, cvFilterValue = null, relatedEntityId = null) {
                    super(extractorItemType, entityType, entitySubType, cvFilterType, cvFilterValue);
                    this.relatedEntityId = relatedEntityId;
                }
                static fromGobiiFileItemCompoundId(gobiiFileItemCompoundId) {
                    return new GobiiFileItemEntityRelation(gobiiFileItemCompoundId.getExtractorItemType(), gobiiFileItemCompoundId.getEntityType(), gobiiFileItemCompoundId.getEntitySubType(), gobiiFileItemCompoundId.getCvFilterType(), gobiiFileItemCompoundId.getCvFilterValue());
                }
                getRelatedEntityId() {
                    return this.relatedEntityId;
                }
                setRelatedEntityId(relatedEntityId) {
                    this.relatedEntityId = relatedEntityId;
                    return this;
                }
            };
            exports_1("GobiiFileItemEntityRelation", GobiiFileItemEntityRelation);
        }
    };
});
//# sourceMappingURL=gobii-file-item-entity-relation.js.map