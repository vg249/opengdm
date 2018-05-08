System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var ExtractorItemType;
    return {
        setters: [],
        execute: function () {
            (function (ExtractorItemType) {
                ExtractorItemType[ExtractorItemType["UNKNOWN"] = 0] = "UNKNOWN";
                ExtractorItemType[ExtractorItemType["ENTITY"] = 1] = "ENTITY";
                ExtractorItemType[ExtractorItemType["MARKER_FILE"] = 2] = "MARKER_FILE";
                ExtractorItemType[ExtractorItemType["MARKER_LIST_ITEM"] = 3] = "MARKER_LIST_ITEM";
                ExtractorItemType[ExtractorItemType["SAMPLE_LIST_ITEM"] = 4] = "SAMPLE_LIST_ITEM";
                ExtractorItemType[ExtractorItemType["SAMPLE_FILE"] = 5] = "SAMPLE_FILE";
                ExtractorItemType[ExtractorItemType["EXPORT_FORMAT"] = 6] = "EXPORT_FORMAT";
                ExtractorItemType[ExtractorItemType["CROP_TYPE"] = 7] = "CROP_TYPE";
                ExtractorItemType[ExtractorItemType["STATUS_DISPLAY_TREE_READY"] = 8] = "STATUS_DISPLAY_TREE_READY";
                ExtractorItemType[ExtractorItemType["JOB_ID"] = 9] = "JOB_ID";
                ExtractorItemType[ExtractorItemType["SAMPLE_LIST_TYPE"] = 10] = "SAMPLE_LIST_TYPE";
                ExtractorItemType[ExtractorItemType["LABEL"] = 11] = "LABEL";
                ExtractorItemType[ExtractorItemType["CLEAR_TREE"] = 12] = "CLEAR_TREE";
                ExtractorItemType[ExtractorItemType["MARKER_GROUP_LIST_ITEM"] = 13] = "MARKER_GROUP_LIST_ITEM";
                ExtractorItemType[ExtractorItemType["TREE_STRUCTURE"] = 14] = "TREE_STRUCTURE";
                ExtractorItemType[ExtractorItemType["VERTEX"] = 15] = "VERTEX";
            })(ExtractorItemType || (ExtractorItemType = {}));
            exports_1("ExtractorItemType", ExtractorItemType);
        }
    };
});
//# sourceMappingURL=type-extractor-item.js.map