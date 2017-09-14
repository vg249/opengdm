System.register(["@angular/core", "@angular/router", "@ngrx/effects", "rxjs/add/operator/switchMap", "rxjs/add/observable/of", "../actions/fileitem-action", "../actions/treenode-action", "../../services/core/tree-structure-service", "../reducers", "rxjs/Observable", "@ngrx/store"], function (exports_1, context_1) {
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
    var core_1, router_1, effects_1, fileItemActions, treeNodeActions, tree_structure_service_1, fromRoot, Observable_1, store_1, FileItemEffects;
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
            function (fromRoot_1) {
                fromRoot = fromRoot_1;
            },
            function (Observable_1_1) {
                Observable_1 = Observable_1_1;
            },
            function (store_1_1) {
                store_1 = store_1_1;
            }
        ],
        execute: function () {
            FileItemEffects = (function () {
                // @Effect()
                // setEntityFilter$ = this.actions$
                //     .ofType(fileItemActions.SET_FILTER_VALUE)
                //     .switchMap((action: fileItemActions.SetFilterValueAction)  => {
                //
                //         let payload = action.payload;
                //
                //         return Observable.create(observer => {
                //
                //
                //             this.nameIdService.get(payload.nameIdRequestParams)
                //                 .subscribe(nameIds => {
                //                         if (nameIds && ( nameIds.length > 0 )) {
                //
                //
                //                             nameIds.forEach(n => {
                //                                 let currentFileItem: GobiiFileItem =
                //                                     GobiiFileItem.build(
                //                                         payload.gobiiExtractFilterType,
                //                                         ProcessType.CREATE)
                //                                         .setExtractorItemType(ExtractorItemType.ENTITY)
                //                                         .setEntityType(payload.nameIdRequestParams.getEntityType())
                //                                         .setCvFilterType(CvFilterType.UNKNOWN)
                //                                         .setItemId(n.id)
                //                                         .setItemName(n.name)
                //                                         .setSelected(false)
                //                                         .setRequired(false)
                //                                         .setParentEntityType(payload.nameIdRequestParams.getRefTargetEntityType())
                //                                         .setParentItemId(payload.nameIdRequestParams.getFkEntityFilterValue());
                //
                //                                 //fileItems.push(currentFileItem);
                //                                 observer.next(currentFileItem);
                //
                //                             });
                //
                //                             //new fileItemActions.LoadAction(fileItems);
                //                         }
                //                     },
                //                     responseHeader => {
                //                         console.log(responseHeader);
                //                     });
                //
                //         }).map( gfi => {
                //             return new fileItemActions.LoadAction([gfi]);
                //         })
                //
                //
                //     }); //switch map
                function FileItemEffects(actions$, treeStructureService, store, router) {
                    var _this = this;
                    this.actions$ = actions$;
                    this.treeStructureService = treeStructureService;
                    this.store = store;
                    this.router = router;
                    // this effect acts on the content of the tree nodes (e.g., their names and icons so forth)
                    // and then dispatches them to the tree node reducer. The tree node reducer holds the nodes
                    // in state. Thus, the hierarchical arrangement of nodes is managed by the reducer in
                    // accordance with how the nodes are defined by the tree service.
                    this.selectForExtract$ = this.actions$
                        .ofType(fileItemActions.ADD_TO_EXTRACT)
                        .map(function (action) {
                        var treeNode = _this.treeStructureService.makeTreeNodeFromFileItem(action.payload);
                        return new treeNodeActions.PlaceTreeNodeAction(treeNode);
                    });
                    this.selectForExtractByFileItemId$ = this.actions$
                        .ofType(fileItemActions.ADD_TO_EXTRACT_BY_ITEM_ID)
                        .switchMap(function (action) {
                        // this is scary. The store is the single source of truth. The only way to get the fileItem for
                        // the fileitem id is to get it from the store, and for that to work here, we need to wrap the
                        // select in an Observable.
                        return Observable_1.Observable.create(function (observer) {
                            var fileItemUniqueId = action.payload;
                            _this.store.select(fromRoot.getAllFileItems)
                                .subscribe(function (all) {
                                var fileItem = all.find(function (fi) { return fi.getFileItemUniqueId() === fileItemUniqueId; });
                                var treeNode = _this.treeStructureService.makeTreeNodeFromFileItem(fileItem);
                                observer.next(treeNode);
                                observer.complete();
                            });
                        }).map(function (gfi) {
                            return new treeNodeActions.PlaceTreeNodeAction(gfi);
                        });
                    } //switchMap()
                    );
                    this.deSelectFromExtract$ = this.actions$
                        .ofType(fileItemActions.REMOVE_FROM_EXTRACT)
                        .map(function (action) {
                        return new treeNodeActions.DeActivateFromExtractAction(action.payload.getFileItemUniqueId());
                    });
                    this.deSelectFromExtractById$ = this.actions$
                        .ofType(fileItemActions.REMOVE_FROM_EXTRACT_BY_ITEM_ID)
                        .map(function (action) {
                        return new treeNodeActions.DeActivateFromExtractAction(action.payload);
                    });
                    this.deselectAll$ = this.actions$
                        .ofType(fileItemActions.REMOVE_ALL_FROM_EXTRACT)
                        .map(function (action) {
                        return new treeNodeActions.ClearAll(action.payload);
                    });
                    this.loadFileItem = this.actions$
                        .ofType(fileItemActions.LOAD_FILE_ITEM)
                        .map(function (action) {
                        if (action.payload.selectForExtract) {
                            return new fileItemActions.AddToExtractAction(action.payload.gobiiFileItem);
                        }
                    });
                }
                __decorate([
                    effects_1.Effect(),
                    __metadata("design:type", Object)
                ], FileItemEffects.prototype, "selectForExtract$", void 0);
                __decorate([
                    effects_1.Effect(),
                    __metadata("design:type", Object)
                ], FileItemEffects.prototype, "selectForExtractByFileItemId$", void 0);
                __decorate([
                    effects_1.Effect(),
                    __metadata("design:type", Object)
                ], FileItemEffects.prototype, "deSelectFromExtract$", void 0);
                __decorate([
                    effects_1.Effect(),
                    __metadata("design:type", Object)
                ], FileItemEffects.prototype, "deSelectFromExtractById$", void 0);
                __decorate([
                    effects_1.Effect(),
                    __metadata("design:type", Object)
                ], FileItemEffects.prototype, "deselectAll$", void 0);
                __decorate([
                    effects_1.Effect(),
                    __metadata("design:type", Object)
                ], FileItemEffects.prototype, "loadFileItem", void 0);
                FileItemEffects = __decorate([
                    core_1.Injectable(),
                    __metadata("design:paramtypes", [effects_1.Actions,
                        tree_structure_service_1.TreeStructureService,
                        store_1.Store,
                        router_1.Router])
                ], FileItemEffects);
                return FileItemEffects;
            }());
            exports_1("FileItemEffects", FileItemEffects);
            /*
            
            
                @Effect()
                setEntityFilter$ = this.actions$
                    .ofType(fileItemActions.SET_FILTER_VALUE)
                    .map((action: fileItemActions.SetFilterValueAction) => {
            
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
                                                    .setSelected(false)
                                                    .setRequired(false)
                                                    .setParentEntityType(nameIdRequestParams.getRefTargetEntityType())
                                                    .setParentItemId(nameIdRequestParams.getFkEntityFilterValue());
            
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