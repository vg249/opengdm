System.register(["@angular/core", "../../model/type-process", "../../model/filter-params", "../../model/filter-type", "../../model/file-item-param-names", "../../model/payload/paged-item-list", "../../model/payload/pagination"], function (exports_1, context_1) {
    "use strict";
    var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
        var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
        if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
        else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
        return c > 3 && r && Object.defineProperty(target, key, r), r;
    };
    var __metadata = (this && this.__metadata) || function (k, v) {
        if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
    };
    var core_1, type_process_1, filter_params_1, filter_type_1, file_item_param_names_1, paged_item_list_1, pagination_1, DtoRequestItemGfiPaged;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (filter_params_1_1) {
                filter_params_1 = filter_params_1_1;
            },
            function (filter_type_1_1) {
                filter_type_1 = filter_type_1_1;
            },
            function (file_item_param_names_1_1) {
                file_item_param_names_1 = file_item_param_names_1_1;
            },
            function (paged_item_list_1_1) {
                paged_item_list_1 = paged_item_list_1_1;
            },
            function (pagination_1_1) {
                pagination_1 = pagination_1_1;
            }
        ],
        execute: function () {
            DtoRequestItemGfiPaged = /** @class */ (function () {
                function DtoRequestItemGfiPaged(fileItemParams, id, jsonToGfi) {
                    if (id === void 0) { id = null; }
                    this.fileItemParams = fileItemParams;
                    this.id = id;
                    this.jsonToGfi = jsonToGfi;
                    this.processType = type_process_1.ProcessType.READ;
                    this.fileItemParams = fileItemParams;
                    this.id = id;
                    this.jsonToGfi = jsonToGfi;
                    if (this.fileItemParams.getFilterType() !== filter_type_1.FilterType.ENTITY_LIST
                        && this.fileItemParams.getFilterType() !== filter_type_1.FilterType.ENTITY_BY_ID) {
                        throw new Error("The FileItemParams with ID "
                            + this.fileItemParams.getQueryName()
                            + " is not of type " + filter_type_1.FilterType[filter_type_1.FilterType.ENTITY_LIST]
                            + " or " + filter_type_1.FilterType[filter_type_1.FilterType.ENTITY_BY_ID]);
                    }
                }
                DtoRequestItemGfiPaged.prototype.getUrl = function () {
                    var returnVal = "gobii/v1";
                    if (this.fileItemParams.getQueryName() === file_item_param_names_1.FilterParamNames.DATASET_LIST_PAGED) {
                        returnVal += "/datasets";
                    }
                    returnVal += "?pageSize=" + this.fileItemParams.getPageSize().toString() +
                        "&pageNo=" + this.fileItemParams.getPageNum().toString() +
                        "&queryId=" + this.fileItemParams.getPagedQueryId();
                    return returnVal;
                }; // getUrl()
                // this is probably not being used anymore
                DtoRequestItemGfiPaged.prototype.getRequestBody = function () {
                    return JSON.stringify({
                        "processType": type_process_1.ProcessType[this.processType],
                        "id": this.id
                    });
                };
                DtoRequestItemGfiPaged.prototype.resultFromJson = function (json) {
                    var _this = this;
                    var fileItems = [];
                    var currentPage = json.header.pagination.currentPage;
                    json.payload.data.forEach(function (jsonItem) {
                        var currentFileItem = _this.jsonToGfi.convert(jsonItem);
                        currentFileItem.setPageNumber(currentPage);
                        fileItems.push(currentFileItem);
                    });
                    var returnVal = new paged_item_list_1.PagedFileItemList(fileItems, pagination_1.Pagination.fromJSON(json.header.pagination));
                    return returnVal;
                };
                DtoRequestItemGfiPaged = __decorate([
                    core_1.Injectable(),
                    __metadata("design:paramtypes", [filter_params_1.FilterParams, String, Object])
                ], DtoRequestItemGfiPaged);
                return DtoRequestItemGfiPaged;
            }());
            exports_1("DtoRequestItemGfiPaged", DtoRequestItemGfiPaged);
        }
    };
});
//# sourceMappingURL=dto-request-item-gfi-paged.js.map