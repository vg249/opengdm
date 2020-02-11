System.register([], function (exports_1, context_1) {
    "use strict";
    var Pagination;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [],
        execute: function () {
            Pagination = /** @class */ (function () {
                function Pagination(currentPage, pageSize, pagedQueryId, queryTime, totalPages) {
                    this.currentPage = currentPage;
                    this.pageSize = pageSize;
                    this.pagedQueryId = pagedQueryId;
                    this.queryTime = queryTime;
                    this.totalPages = totalPages;
                }
                Pagination.fromJSON = function (json) {
                    var currentPage = json.currentPage;
                    var pageSize = json.pageSize;
                    var pagedQueryId = json.pagedQueryId;
                    var queryTime = json.queryTime;
                    var totalPages = json.totalPages;
                    return new Pagination(currentPage, pageSize, pagedQueryId, queryTime, totalPages); // new
                }; // fromJson()
                return Pagination;
            }());
            exports_1("Pagination", Pagination);
        }
    };
});
//# sourceMappingURL=pagination.js.map