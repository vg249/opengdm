System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var ExtractorItemType, ExtractorCategoryType, ExtractorSubmissionItem;
    return {
        setters: [],
        execute: function () {
            (function (ExtractorItemType) {
                ExtractorItemType[ExtractorItemType["Category"] = 0] = "Category";
                ExtractorItemType[ExtractorItemType["Entity"] = 1] = "Entity";
            })(ExtractorItemType || (ExtractorItemType = {}));
            exports_1("ExtractorItemType", ExtractorItemType);
            (function (ExtractorCategoryType) {
                ExtractorCategoryType[ExtractorCategoryType["Container"] = 0] = "Container";
                ExtractorCategoryType[ExtractorCategoryType["MarkerList"] = 1] = "MarkerList";
                ExtractorCategoryType[ExtractorCategoryType["SampleList"] = 2] = "SampleList";
            })(ExtractorCategoryType || (ExtractorCategoryType = {}));
            exports_1("ExtractorCategoryType", ExtractorCategoryType);
            ExtractorSubmissionItem = (function () {
                function ExtractorSubmissionItem(itemType) {
                    this._itemType = itemType;
                }
                ExtractorSubmissionItem.build = function (itemType) {
                    return new ExtractorSubmissionItem(itemType);
                };
                ExtractorSubmissionItem.prototype.getItemType = function () {
                    return this._itemType;
                };
                ExtractorSubmissionItem.prototype.setItemType = function (value) {
                    this._itemType = value;
                    return this;
                };
                ExtractorSubmissionItem.prototype.getCategoryType = function () {
                    return this._categoryType;
                };
                ExtractorSubmissionItem.prototype.setCategoryType = function (value) {
                    this._categoryType = value;
                    return this;
                };
                ExtractorSubmissionItem.prototype.getCategoryName = function () {
                    return this._categoryName;
                };
                ExtractorSubmissionItem.prototype.setCategoryName = function (value) {
                    this._categoryName = value;
                    return this;
                };
                ExtractorSubmissionItem.prototype.getEntityType = function () {
                    return this._entityType;
                };
                ExtractorSubmissionItem.prototype.setEntityType = function (value) {
                    this._entityType = value;
                    return this;
                };
                ExtractorSubmissionItem.prototype.getCvFilterType = function () {
                    return this._cvFilterType;
                };
                ExtractorSubmissionItem.prototype.setCvFilterType = function (value) {
                    this._cvFilterType = value;
                    return this;
                };
                ExtractorSubmissionItem.prototype.getChildEntityTypes = function () {
                    return this._childEntityTypes;
                };
                ExtractorSubmissionItem.prototype.setChildEntityTypes = function (value) {
                    this._childEntityTypes = value;
                    return this;
                };
                ExtractorSubmissionItem.prototype.getChildCattegoryTypes = function () {
                    return this._childCattegoryTypes;
                };
                ExtractorSubmissionItem.prototype.setChildCattegoryTypes = function (value) {
                    this._childCattegoryTypes = value;
                    return this;
                };
                ExtractorSubmissionItem.prototype.getRequired = function () {
                    return this._required;
                };
                ExtractorSubmissionItem.prototype.setRequired = function (value) {
                    this._required = value;
                    return this;
                };
                return ExtractorSubmissionItem;
            }());
            exports_1("ExtractorSubmissionItem", ExtractorSubmissionItem);
        }
    };
});
//# sourceMappingURL=extractor-submission-item.js.map