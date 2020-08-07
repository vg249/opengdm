System.register(["./type-entity", "./cv-filter-type", "./type-extractor-item", "./gobii-file-item-compound-id"], function (exports_1, context_1) {
    "use strict";
    var __extends = (this && this.__extends) || (function () {
        var extendStatics = function (d, b) {
            extendStatics = Object.setPrototypeOf ||
                ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
                function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
            return extendStatics(d, b);
        };
        return function (d, b) {
            extendStatics(d, b);
            function __() { this.constructor = d; }
            d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
        };
    })();
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
            GobiiFileItemEntityRelation = /** @class */ (function (_super) {
                __extends(GobiiFileItemEntityRelation, _super);
                function GobiiFileItemEntityRelation(extractorItemType, entityType, entitySubType, cvFilterType, cvFilterValue, relatedEntityId) {
                    if (extractorItemType === void 0) { extractorItemType = type_extractor_item_1.ExtractorItemType.UNKNOWN; }
                    if (entityType === void 0) { entityType = type_entity_1.EntityType.UNKNOWN; }
                    if (entitySubType === void 0) { entitySubType = type_entity_1.EntitySubType.UNKNOWN; }
                    if (cvFilterType === void 0) { cvFilterType = cv_filter_type_1.CvFilterType.UNKNOWN; }
                    if (cvFilterValue === void 0) { cvFilterValue = null; }
                    if (relatedEntityId === void 0) { relatedEntityId = null; }
                    var _this = _super.call(this, extractorItemType, entityType, entitySubType, cvFilterType, cvFilterValue) || this;
                    _this.relatedEntityId = relatedEntityId;
                    return _this;
                }
                GobiiFileItemEntityRelation.fromGobiiFileItemCompoundId = function (gobiiFileItemCompoundId) {
                    return new GobiiFileItemEntityRelation(gobiiFileItemCompoundId.getExtractorItemType(), gobiiFileItemCompoundId.getEntityType(), gobiiFileItemCompoundId.getEntitySubType(), gobiiFileItemCompoundId.getCvFilterType(), gobiiFileItemCompoundId.getCvFilterValue());
                };
                GobiiFileItemEntityRelation.prototype.getRelatedEntityId = function () {
                    return this.relatedEntityId;
                };
                GobiiFileItemEntityRelation.prototype.setRelatedEntityId = function (relatedEntityId) {
                    this.relatedEntityId = relatedEntityId;
                    return this;
                };
                return GobiiFileItemEntityRelation;
            }(gobii_file_item_compound_id_1.GobiiFileItemCompoundId));
            exports_1("GobiiFileItemEntityRelation", GobiiFileItemEntityRelation);
        }
    };
});
//# sourceMappingURL=gobii-file-item-entity-relation.js.map