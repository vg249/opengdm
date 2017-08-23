System.register(["@angular/core", "@angular/router", "@ngrx/effects", "rxjs/add/operator/switchMap", "rxjs/add/observable/of", "../actions/fileitem-action", "../actions/treenode-action", "../../services/core/tree-structure-service", "../../services/core/name-id-service", "../../model/cv-filter-type", "../../model/file-model-node", "../../model/gobii-file-item", "../../model/type-process", "rxjs/Observable"], function (exports_1, context_1) {
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
    var core_1, router_1, effects_1, fileItemActions, treeNodeActions, tree_structure_service_1, name_id_service_1, cv_filter_type_1, file_model_node_1, gobii_file_item_1, type_process_1, Observable_1, FileItemEffects;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (router_1_1) {
                router_1 = router_1_1;
            },
            function (effects_1_1) {
                effects_1 = effects_1_1;
            },
            function (_1) {
            },
            function (_2) {
            },
            function (fileItemActions_1) {
                fileItemActions = fileItemActions_1;
            },
            function (treeNodeActions_1) {
                treeNodeActions = treeNodeActions_1;
            },
            function (tree_structure_service_1_1) {
                tree_structure_service_1 = tree_structure_service_1_1;
            },
            function (name_id_service_1_1) {
                name_id_service_1 = name_id_service_1_1;
            },
            function (cv_filter_type_1_1) {
                cv_filter_type_1 = cv_filter_type_1_1;
            },
            function (file_model_node_1_1) {
                file_model_node_1 = file_model_node_1_1;
            },
            function (gobii_file_item_1_1) {
                gobii_file_item_1 = gobii_file_item_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (Observable_1_1) {
                Observable_1 = Observable_1_1;
            }
        ],
        execute: function () {
            FileItemEffects = (function () {
                function FileItemEffects(actions$, treeStructureService, nameIdService, router) {
                    var _this = this;
                    this.actions$ = actions$;
                    this.treeStructureService = treeStructureService;
                    this.nameIdService = nameIdService;
                    this.router = router;
                    this.loadFileItems$ = this.actions$
                        .ofType(fileItemActions.SELECT_FOR_EXTRACT)
                        .map(function (action) {
                        var treeNode = _this.treeStructureService.makeTreeNodeFromFileItem(action.payload);
                        return new treeNodeActions.PlaceTreeNodeAction(treeNode);
                    });
                    this.setEntityFilter$ = this.actions$
                        .ofType(fileItemActions.SET_ENTITY_FILTER)
                        .switchMap(function (action) {
                        var payload = action.payload;
                        // return Observable.of( { }
                        // );
                        return Observable_1.Observable.create(function (observer) {
                            _this.nameIdService.get(payload.nameIdRequestParams)
                                .subscribe(function (nameIds) {
                                if (nameIds && (nameIds.length > 0)) {
                                    nameIds.forEach(function (n) {
                                        var currentFileItem = gobii_file_item_1.GobiiFileItem.build(payload.gobiiExtractFilterType, type_process_1.ProcessType.CREATE)
                                            .setExtractorItemType(file_model_node_1.ExtractorItemType.ENTITY)
                                            .setEntityType(payload.nameIdRequestParams.getEntityType())
                                            .setCvFilterType(cv_filter_type_1.CvFilterType.UNKNOWN)
                                            .setItemId(n.id)
                                            .setItemName(n.name)
                                            .setChecked(false)
                                            .setRequired(false)
                                            .setParentEntityType(payload.nameIdRequestParams.getRefTargetEntityType())
                                            .setParentItemId(payload.nameIdRequestParams.getEntityFilterValue());
                                        //fileItems.push(currentFileItem);
                                        observer.next(currentFileItem);
                                    });
                                    //new fileItemActions.LoadAction(fileItems);
                                }
                            }, function (responseHeader) {
                                console.log(responseHeader);
                            });
                        }).map(function (gfi) {
                            return new fileItemActions.LoadAction([gfi]);
                        });
                    }); //switch map
                }
                __decorate([
                    effects_1.Effect(),
                    __metadata("design:type", Object)
                ], FileItemEffects.prototype, "loadFileItems$", void 0);
                __decorate([
                    effects_1.Effect(),
                    __metadata("design:type", Object)
                ], FileItemEffects.prototype, "setEntityFilter$", void 0);
                FileItemEffects = __decorate([
                    core_1.Injectable(),
                    __metadata("design:paramtypes", [effects_1.Actions,
                        tree_structure_service_1.TreeStructureService,
                        name_id_service_1.NameIdService,
                        router_1.Router])
                ], FileItemEffects);
                return FileItemEffects;
            }());
            exports_1("FileItemEffects", FileItemEffects);
            /*
            
            
                @Effect()
                setEntityFilter$ = this.actions$
                    .ofType(fileItemActions.SET_ENTITY_FILTER)
                    .map((action: fileItemActions.SetEntityFilter) => {
            
                        }
                    );
            
            *
            *                 this.nameIdService.get(nameIdRequestParams)
                            .subscribe(nameIds => {
                                    if (nameIds && ( nameIds.length > 0 )) {
            
                                        let fileItems: GobiiFileItem[] = [];
            
                                        nameIds.forEach(n => {
                                            let currentFileItem: GobiiFileItem =
                                                GobiiFileItem.build(
                                                    gobiiExtractFilterType,
                                                    ProcessType.CREATE)
                                                    .setExtractorItemType(ExtractorItemType.ENTITY)
                                                    .setEntityType(nameIdRequestParams.getEntityType())
                                                    .setCvFilterType(CvFilterType.UNKNOWN)
                                                    .setItemId(n.id)
                                                    .setItemName(n.name)
                                                    .setChecked(false)
                                                    .setRequired(false)
                                                    .setParentEntityType(nameIdRequestParams.getRefTargetEntityType())
                                                    .setParentItemId(nameIdRequestParams.getEntityFilterValue());
            
                                            fileItems.push(currentFileItem);
            
                                        });
            
                                        return new fileItemActions.LoadAction(fileItems);
                                    }
                                },
                                responseHeader => {
                                    console.log(responseHeader);
                                });
            */
        }
    };
});
//# sourceMappingURL=file-item-effects.js.map