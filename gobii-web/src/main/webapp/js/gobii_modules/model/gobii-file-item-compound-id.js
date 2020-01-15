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
            GobiiFileItemCompoundId = /** @class */ (function () {
                function GobiiFileItemCompoundId(_extractorItemType, _entityType, _entitySubType, _cvFilterType, _cvFilterValue, _isExtractCriterion) {
                    if (_extractorItemType === void 0) { _extractorItemType = type_extractor_item_1.ExtractorItemType.UNKNOWN; }
                    if (_entityType === void 0) { _entityType = type_entity_1.EntityType.UNKNOWN; }
                    if (_entitySubType === void 0) { _entitySubType = type_entity_1.EntitySubType.UNKNOWN; }
                    if (_cvFilterType === void 0) { _cvFilterType = cv_filter_type_1.CvFilterType.UNKNOWN; }
                    if (_cvFilterValue === void 0) { _cvFilterValue = null; }
                    if (_isExtractCriterion === void 0) { _isExtractCriterion = true; }
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
                GobiiFileItemCompoundId.prototype.compoundIdeEquals = function (gobiiFileItemCompoundId) {
                    return this.getExtractorItemType() === gobiiFileItemCompoundId.getExtractorItemType()
                        && this.getEntityType() === gobiiFileItemCompoundId.getEntityType()
                        && this.getEntitySubType() === gobiiFileItemCompoundId.getEntitySubType()
                        && this.getCvFilterType() === gobiiFileItemCompoundId.getCvFilterType()
                        && this.getCvFilterValue() === gobiiFileItemCompoundId.getCvFilterValue();
                };
                GobiiFileItemCompoundId.prototype.getExtractorItemType = function () {
                    return this._extractorItemType;
                };
                GobiiFileItemCompoundId.prototype.setExtractorItemType = function (value) {
                    if (value != null) {
                        this._extractorItemType = value;
                    }
                    else {
                        this._extractorItemType = type_extractor_item_1.ExtractorItemType.UNKNOWN;
                    }
                    return this;
                };
                GobiiFileItemCompoundId.prototype.getEntityType = function () {
                    return this._entityType;
                };
                GobiiFileItemCompoundId.prototype.setEntityType = function (value) {
                    if (value != null) {
                        this._entityType = value;
                    }
                    else {
                        this._entityType = type_entity_1.EntityType.UNKNOWN;
                    }
                    return this;
                };
                GobiiFileItemCompoundId.prototype.getEntitySubType = function () {
                    return this._entitySubType;
                };
                GobiiFileItemCompoundId.prototype.setEntitySubType = function (value) {
                    if (value != null) {
                        this._entitySubType = value;
                    }
                    else {
                        this._entitySubType = type_entity_1.EntitySubType.UNKNOWN;
                    }
                    return this;
                };
                GobiiFileItemCompoundId.prototype.getCvFilterType = function () {
                    return this._cvFilterType;
                };
                GobiiFileItemCompoundId.prototype.setCvFilterType = function (value) {
                    if (value != null) {
                        this._cvFilterType = value;
                    }
                    else {
                        this._cvFilterType = cv_filter_type_1.CvFilterType.UNKNOWN;
                    }
                    return this;
                };
                GobiiFileItemCompoundId.prototype.getCvFilterValue = function () {
                    return this._cvFilterValue;
                };
                GobiiFileItemCompoundId.prototype.setCvFilterValue = function (value) {
                    this._cvFilterValue = value;
                    return this;
                };
                GobiiFileItemCompoundId.prototype.getIsExtractCriterion = function () {
                    return this._isExtractCriterion;
                };
                GobiiFileItemCompoundId.prototype.setIsExtractCriterion = function (value) {
                    this._isExtractCriterion = value;
                    return this;
                };
                return GobiiFileItemCompoundId;
            }());
            exports_1("GobiiFileItemCompoundId", GobiiFileItemCompoundId);
        }
    };
});
//# sourceMappingURL=gobii-file-item-compound-id.js.map