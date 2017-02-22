System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var ItemType, CategoryType, ExtractorSubmissionItem;
    return {
        setters: [],
        execute: function () {
            (function (ItemType) {
                ItemType[ItemType["Category"] = 0] = "Category";
                ItemType[ItemType["Entity"] = 1] = "Entity";
            })(ItemType || (ItemType = {}));
            (function (CategoryType) {
                CategoryType[CategoryType["Container"] = 0] = "Container";
                CategoryType[CategoryType["MarkerList"] = 1] = "MarkerList";
                CategoryType[CategoryType["SampleList"] = 2] = "SampleList";
            })(CategoryType || (CategoryType = {}));
            ExtractorSubmissionItem = (function () {
                function ExtractorSubmissionItem(itemType, categoryType, entityType, childEntityTypes, childCattegoryTypes) {
                    this.itemType = itemType;
                    this.categoryType = categoryType;
                    this.entityType = entityType;
                    this.childEntityTypes = childEntityTypes;
                    this.childCattegoryTypes = childCattegoryTypes;
                }
                return ExtractorSubmissionItem;
            }());
            exports_1("ExtractorSubmissionItem", ExtractorSubmissionItem);
        }
    };
});
//# sourceMappingURL=extractor-submission-item.js.map