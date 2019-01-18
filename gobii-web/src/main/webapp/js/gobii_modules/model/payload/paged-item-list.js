System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var PagedFileItemList;
    return {
        setters: [],
        execute: function () {
            PagedFileItemList = (function () {
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