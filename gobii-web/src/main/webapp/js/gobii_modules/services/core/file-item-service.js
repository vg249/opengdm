System.register(["@angular/core", "../../model/file-model-node", "../../model/cv-filter-type", "../../model/gobii-file-item", "../../model/type-process", "./name-id-service", "../../store/actions/fileitem-action", "@ngrx/store"], function (exports_1, context_1) {
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
    var __moduleName = context_1 && context_1.id;
    var core_1, file_model_node_1, cv_filter_type_1, gobii_file_item_1, type_process_1, name_id_service_1, fileItemActions, store_1, FileItemService;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (file_model_node_1_1) {
                file_model_node_1 = file_model_node_1_1;
            },
            function (cv_filter_type_1_1) {
                cv_filter_type_1 = cv_filter_type_1_1;
            },
            function (gobii_file_item_1_1) {
                gobii_file_item_1 = gobii_file_item_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (name_id_service_1_1) {
                name_id_service_1 = name_id_service_1_1;
            },
            function (fileItemActions_1) {
                fileItemActions = fileItemActions_1;
            },
            function (store_1_1) {
                store_1 = store_1_1;
            }
        ],
        execute: function () {
            FileItemService = (function () {
                function FileItemService(nameIdService, store) {
                    this.nameIdService = nameIdService;
                    this.store = store;
                }
                FileItemService.prototype.loadNameIdsToFileItems = function (gobiiExtractFilterType, nameIdRequestParams) {
                    var _this = this;
                    this.nameIdService.get(nameIdRequestParams)
                        .subscribe(function (nameIds) {
                        if (nameIds && (nameIds.length > 0)) {
                            var fileItems_1 = [];
                            nameIds.forEach(function (n) {
                                var currentFileItem = gobii_file_item_1.GobiiFileItem.build(gobiiExtractFilterType, type_process_1.ProcessType.CREATE)
                                    .setExtractorItemType(file_model_node_1.ExtractorItemType.ENTITY)
                                    .setEntityType(nameIdRequestParams.getEntityType())
                                    .setCvFilterType(cv_filter_type_1.CvFilterType.UNKNOWN)
                                    .setItemId(n.id)
                                    .setItemName(n.name)
                                    .setChecked(false)
                                    .setRequired(false)
                                    .setParentEntityType(nameIdRequestParams.getRefTargetEntityType())
                                    .setParentItemId(nameIdRequestParams.getEntityFilterValue());
                                fileItems_1.push(currentFileItem);
                            });
                            var loadAction = new fileItemActions.LoadFilteredItemsAction({
                                gobiiFileItems: fileItems_1,
                                nameIdRequestParams: nameIdRequestParams,
                            });
                            _this.store.dispatch(loadAction);
                        }
                    }, function (responseHeader) {
                        //this.handleHeaderStatus(responseHeader);
                        console.log(responseHeader);
                    });
                };
                FileItemService = __decorate([
                    core_1.Injectable(),
                    __metadata("design:paramtypes", [name_id_service_1.NameIdService,
                        store_1.Store])
                ], FileItemService);
                return FileItemService;
            }());
            exports_1("FileItemService", FileItemService);
        }
    };
});
//# sourceMappingURL=file-item-service.js.map