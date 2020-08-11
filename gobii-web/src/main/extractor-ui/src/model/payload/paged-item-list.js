System.register([], function (exports_1, context_1) {
    "use strict";
    var PagedFileItemList;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [],
        execute: function () {
            PagedFileItemList = /** @class */ (function () {
                function PagedFileItemList(gobiiFileItems, pagination) {
                    this.gobiiFileItems = gobiiFileItems;
                    this.pagination = pagination;
                }
                return PagedFileItemList;
            }());
            exports_1("PagedFileItemList", PagedFileItemList);
        }
    };
});
//# sourceMappingURL=paged-item-list.js.map