System.register(["@angular/core", "@angular/router", "@ngrx/effects", "rxjs/add/operator/switchMap", "rxjs/add/observable/of", "../actions/fileitem-action", "../actions/treenode-action", "../../services/core/tree-structure-service"], function (exports_1, context_1) {
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
    var core_1, router_1, effects_1, fileItemActions, treeNodeActions, tree_structure_service_1, FileItemEffects;
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
            }
        ],
        execute: function () {
            FileItemEffects = (function () {
                // @Effect()
                // setEntityFilter$ = this.actions$
                //     .ofType(fileItemActions.SET_ENTITY_FILTER)
                //     .switchMap((action: fileItemActions.SetEntityFilter)  => {
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
                //                                         .setChecked(false)
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
                function FileItemEffects(actions$, treeStructureService, router) {
                    var _this = this;
                    this.actions$ = actions$;
                    this.treeStructureService = treeStructureService;
                    this.router = router;
                    this.selectForExtract$ = this.actions$
                        .ofType(fileItemActions.SELECT_FOR_EXTRACT)
                        .map(function (action) {
                        var treeNode = _this.treeStructureService.makeTreeNodeFromFileItem(action.payload);
                        return new treeNodeActions.PlaceTreeNodeAction(treeNode);
                    });
                    this.deSelectFromExtract$ = this.actions$
                        .ofType(fileItemActions.DESELECT_FOR_EXTRACT)
                        .map(function (action) {
                        return new treeNodeActions.DeActivateFromExtractAction(action.payload.getFileItemUniqueId());
                    });
                }
                __decorate([
                    effects_1.Effect(),
                    __metadata("design:type", Object)
                ], FileItemEffects.prototype, "selectForExtract$", void 0);
                __decorate([
                    effects_1.Effect(),
                    __metadata("design:type", Object)
                ], FileItemEffects.prototype, "deSelectFromExtract$", void 0);
                FileItemEffects = __decorate([
                    core_1.Injectable(),
                    __metadata("design:paramtypes", [effects_1.Actions,
                        tree_structure_service_1.TreeStructureService,
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