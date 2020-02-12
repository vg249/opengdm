System.register([], function (exports_1, context_1) {
    "use strict";
    var PagedFileItemList;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [],
        execute: function () {
            PagedFileItemList = class PagedFileItemList {
                constructor(gobiiFileItems, pagination) {
                    this.gobiiFileItems = gobiiFileItems;
                    this.pagination = pagination;
                }
            };
            exports_1("PagedFileItemList", PagedFileItemList);
        }
    };
});
//# sourceMappingURL=paged-item-list.js.map