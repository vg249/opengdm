System.register(["./type-entity", "./cv-filter-type"], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var type_entity_1, cv_filter_type_1, ExtractorItemType, ExtractorCategoryType, CardinalityType, StatusTreeTemplate;
    return {
        setters: [
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (cv_filter_type_1_1) {
                cv_filter_type_1 = cv_filter_type_1_1;
            }
        ],
        execute: function () {
            (function (ExtractorItemType) {
                ExtractorItemType[ExtractorItemType["CATEGORY"] = 0] = "CATEGORY";
                ExtractorItemType[ExtractorItemType["ENTITY"] = 1] = "ENTITY";
                ExtractorItemType[ExtractorItemType["MARKER_LIST"] = 2] = "MARKER_LIST";
                ExtractorItemType[ExtractorItemType["SAMPLE_LIST"] = 3] = "SAMPLE_LIST";
                ExtractorItemType[ExtractorItemType["EXPORT_FORMAT"] = 4] = "EXPORT_FORMAT";
                ExtractorItemType[ExtractorItemType["CROP_TYPE"] = 5] = "CROP_TYPE";
            })(ExtractorItemType || (ExtractorItemType = {}));
            exports_1("ExtractorItemType", ExtractorItemType);
            (function (ExtractorCategoryType) {
                ExtractorCategoryType[ExtractorCategoryType["CONTAINER"] = 0] = "CONTAINER";
                ExtractorCategoryType[ExtractorCategoryType["ENTITY_CONTAINER"] = 1] = "ENTITY_CONTAINER";
                ExtractorCategoryType[ExtractorCategoryType["LEAF"] = 2] = "LEAF";
            })(ExtractorCategoryType || (ExtractorCategoryType = {}));
            exports_1("ExtractorCategoryType", ExtractorCategoryType);
            (function (CardinalityType) {
                CardinalityType[CardinalityType["ZERO_OR_ONE"] = 0] = "ZERO_OR_ONE";
                CardinalityType[CardinalityType["ZERO_OR_MORE"] = 1] = "ZERO_OR_MORE";
                CardinalityType[CardinalityType["ONE_ONLY"] = 2] = "ONE_ONLY";
                CardinalityType[CardinalityType["ONE_OR_MORE"] = 3] = "ONE_OR_MORE";
                CardinalityType[CardinalityType["MORE_THAN_ONE"] = 4] = "MORE_THAN_ONE";
            })(CardinalityType || (CardinalityType = {}));
            exports_1("CardinalityType", CardinalityType);
            StatusTreeTemplate = (function () {
                function StatusTreeTemplate(itemType) {
                    this._children = [];
                    this._alternatePeerTypes = [];
                    this._cardinality = CardinalityType.ZERO_OR_MORE;
                    this._itemType = ExtractorItemType.ENTITY;
                    this._categoryType = ExtractorCategoryType.LEAF;
                    this._entityType = type_entity_1.EntityType.UNKNOWN;
                    this._cvFilterType = cv_filter_type_1.CvFilterType.UKNOWN;
                    this._itemType = itemType;
                }
                StatusTreeTemplate.build = function (itemType) {
                    return new StatusTreeTemplate(itemType);
                };
                StatusTreeTemplate.prototype.getAlternatePeerTypes = function () {
                    return this._alternatePeerTypes;
                };
                StatusTreeTemplate.prototype.setAlternatePeerTypes = function (value) {
                    this._alternatePeerTypes = value;
                    return this;
                };
                StatusTreeTemplate.prototype.getCardinality = function () {
                    return this._cardinality;
                };
                StatusTreeTemplate.prototype.setCardinality = function (value) {
                    this._cardinality = value;
                    return this;
                };
                StatusTreeTemplate.prototype.getChildren = function () {
                    return this._children;
                };
                StatusTreeTemplate.prototype.addChild = function (child) {
                    this._children.push(child);
                    return this;
                };
                StatusTreeTemplate.prototype.getItemType = function () {
                    return this._itemType;
                };
                StatusTreeTemplate.prototype.setItemType = function (value) {
                    this._itemType = value;
                    return this;
                };
                StatusTreeTemplate.prototype.getCategoryType = function () {
                    return this._categoryType;
                };
                StatusTreeTemplate.prototype.setCategoryType = function (value) {
                    this._categoryType = value;
                    return this;
                };
                StatusTreeTemplate.prototype.getCategoryName = function () {
                    return this._categoryName;
                };
                StatusTreeTemplate.prototype.setCategoryName = function (value) {
                    this._categoryName = value;
                    return this;
                };
                StatusTreeTemplate.prototype.getEntityType = function () {
                    return this._entityType;
                };
                StatusTreeTemplate.prototype.setEntityType = function (value) {
                    this._entityType = value;
                    return this;
                };
                StatusTreeTemplate.prototype.getEntityName = function () {
                    return this._entityName;
                };
                StatusTreeTemplate.prototype.setEntityName = function (value) {
                    this._entityName = value;
                    return this;
                };
                StatusTreeTemplate.prototype.getCvFilterType = function () {
                    return this._cvFilterType;
                };
                StatusTreeTemplate.prototype.setCvFilterType = function (value) {
                    this._cvFilterType = value;
                    return this;
                };
                return StatusTreeTemplate;
            }());
            exports_1("StatusTreeTemplate", StatusTreeTemplate);
        }
    };
});
//# sourceMappingURL=extractor-submission-item.js.map