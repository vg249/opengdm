System.register(["./type-entity", "./cv-filter-type", "./type-extractor-item"], function (exports_1, context_1) {
    "use strict";
    var type_entity_1, cv_filter_type_1, type_extractor_item_1, GobiiFileItemCompoundId;
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
            }
        ],
        execute: function () {
            GobiiFileItemCompoundId = class GobiiFileItemCompoundId {
                constructor(_extractorItemType = type_extractor_item_1.ExtractorItemType.UNKNOWN, _entityType = type_entity_1.EntityType.UNKNOWN, _entitySubType = type_entity_1.EntitySubType.UNKNOWN, _cvFilterType = cv_filter_type_1.CvFilterType.UNKNOWN, _cvFilterValue = null, _isExtractCriterion = true) {
                    this._extractorItemType = _extractorItemType;
                    this._entityType = _entityType;
                    this._entitySubType = _entitySubType;
                    this._cvFilterType = _cvFilterType;
                    this._cvFilterValue = _cvFilterValue;
                    this._isExtractCriterion = _isExtractCriterion;
                    if (this._cvFilterType === null) {
                        this._cvFilterType = cv_filter_type_1.CvFilterType.UNKNOWN;
                    }
                    if (this._extractorItemType == null) {
                        this._extractorItemType = type_extractor_item_1.ExtractorItemType.UNKNOWN;
                    }
                    if (this._entityType == null) {
                        this._entityType = type_entity_1.EntityType.UNKNOWN;
                    }
                    if (this._entitySubType == null) {
                        this._entitySubType = type_entity_1.EntitySubType.UNKNOWN;
                    }
                }
                compoundIdeEquals(gobiiFileItemCompoundId) {
                    return this.getExtractorItemType() === gobiiFileItemCompoundId.getExtractorItemType()
                        && this.getEntityType() === gobiiFileItemCompoundId.getEntityType()
                        && this.getEntitySubType() === gobiiFileItemCompoundId.getEntitySubType()
                        && this.getCvFilterType() === gobiiFileItemCompoundId.getCvFilterType()
                        && this.getCvFilterValue() === gobiiFileItemCompoundId.getCvFilterValue();
                }
                getExtractorItemType() {
                    return this._extractorItemType;
                }
                setExtractorItemType(value) {
                    if (value != null) {
                        this._extractorItemType = value;
                    }
                    else {
                        this._extractorItemType = type_extractor_item_1.ExtractorItemType.UNKNOWN;
                    }
                    return this;
                }
                getEntityType() {
                    return this._entityType;
                }
                setEntityType(value) {
                    if (value != null) {
                        this._entityType = value;
                    }
                    else {
                        this._entityType = type_entity_1.EntityType.UNKNOWN;
                    }
                    return this;
                }
                getEntitySubType() {
                    return this._entitySubType;
                }
                setEntitySubType(value) {
                    if (value != null) {
                        this._entitySubType = value;
                    }
                    else {
                        this._entitySubType = type_entity_1.EntitySubType.UNKNOWN;
                    }
                    return this;
                }
                getCvFilterType() {
                    return this._cvFilterType;
                }
                setCvFilterType(value) {
                    if (value != null) {
                        this._cvFilterType = value;
                    }
                    else {
                        this._cvFilterType = cv_filter_type_1.CvFilterType.UNKNOWN;
                    }
                    return this;
                }
                getCvFilterValue() {
                    return this._cvFilterValue;
                }
                setCvFilterValue(value) {
                    this._cvFilterValue = value;
                    return this;
                }
                getIsExtractCriterion() {
                    return this._isExtractCriterion;
                }
                setIsExtractCriterion(value) {
                    this._isExtractCriterion = value;
                    return this;
                }
            };
            exports_1("GobiiFileItemCompoundId", GobiiFileItemCompoundId);
        }
    };
});
//# sourceMappingURL=gobii-file-item-compound-id.js.map