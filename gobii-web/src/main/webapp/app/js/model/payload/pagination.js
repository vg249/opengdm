System.register([], function (exports_1, context_1) {
    "use strict";
    var Pagination;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [],
        execute: function () {
            Pagination = class Pagination {
                constructor(currentPage, pageSize, pagedQueryId, queryTime, totalPages) {
                    this.currentPage = currentPage;
                    this.pageSize = pageSize;
                    this.pagedQueryId = pagedQueryId;
                    this.queryTime = queryTime;
                    this.totalPages = totalPages;
                }
                static fromJSON(json) {
                    let currentPage = json.currentPage;
                    let pageSize = json.pageSize;
                    let pagedQueryId = json.pagedQueryId;
                    let queryTime = json.queryTime;
                    let totalPages = json.totalPages;
                    return new Pagination(currentPage, pageSize, pagedQueryId, queryTime, totalPages); // new
                } // fromJson()
            };
            exports_1("Pagination", Pagination);
        }
    };
});
//# sourceMappingURL=pagination.js.map