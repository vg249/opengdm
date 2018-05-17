System.register(["@angular/core", "../model/type-extractor-filter", "@ngrx/store", "../store/reducers", "../store/actions/history-action", "../services/core/nameid-file-item-service", "../services/core/filter-service", "../services/core/flex-query-service"], function (exports_1, context_1) {
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
    var core_1, type_extractor_filter_1, store_1, fromRoot, historyAction, nameid_file_item_service_1, filter_service_1, flex_query_service_1, FlexQueryFilterComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (store_1_1) {
                store_1 = store_1_1;
            },
            function (fromRoot_1) {
                fromRoot = fromRoot_1;
            },
            function (historyAction_1) {
                historyAction = historyAction_1;
            },
            function (nameid_file_item_service_1_1) {
                nameid_file_item_service_1 = nameid_file_item_service_1_1;
            },
            function (filter_service_1_1) {
                filter_service_1 = filter_service_1_1;
            },
            function (flex_query_service_1_1) {
                flex_query_service_1 = flex_query_service_1_1;
            }
        ],
        execute: function () {
            FlexQueryFilterComponent = (function () {
                function FlexQueryFilterComponent(store, fileItemService, filterService, flexQueryService) {
                    this.store = store;
                    this.fileItemService = fileItemService;
                    this.filterService = filterService;
                    this.flexQueryService = flexQueryService;
                    this.totalValues = "0";
                    this.enabledStyle = null;
                    this.disabledStyle = { 'background': '#dddddd' };
                    this.currentStyle = this.disabledStyle;
                    this.previousSelectedItemId = null;
                } // ctor
                FlexQueryFilterComponent.prototype.ngOnInit = function () {
                    var _this = this;
                    this.fileItemsEntityNames$ = this.filterService.getForFilter(this.filterParamNameVertices);
                    this.fileItemsEntityValues$ = this.filterService.getForFilter(this.filterParamNameVertexValues);
                    this.JobId$ = this.store.select(fromRoot.getJobId);
                    this
                        .fileItemsEntityNames$
                        .subscribe(function (items) {
                        if (_this.previousSelectedItemId === null && items && items.length > 0) {
                            _this.previousSelectedItemId = items[0].getFileItemUniqueId();
                        }
                        if (items.length > 1) {
                            _this.currentStyle = _this.enabledStyle;
                        }
                        else {
                            _this.currentStyle = _this.disabledStyle;
                        }
                        // if (items[0]) {
                        //     this.selectedAllowableEntities = items[0];
                        // }
                    }, function (error) {
                        _this.store.dispatch(new historyAction.AddStatusMessageAction(error));
                    });
                    this
                        .fileItemsEntityValues$
                        .subscribe(function (items) {
                        _this.totalValues = items.length.toString();
                    });
                };
                FlexQueryFilterComponent.prototype.handleVertexSelected = function (arg) {
                    var _this = this;
                    var vertexId = null;
                    if (arg.value._entity && arg.value._entity.vertexId) {
                        vertexId = arg.value._entity.vertexId;
                    }
                    // else {
                    //     this.selectedAllowableEntities = null;
                    // }
                    this.JobId$.subscribe(function (fileItemJobId) {
                        var destinationVertex = arg.value._entity;
                        //                destinationVertex.entityType = destinationVertex.entityType;
                        _this.flexQueryService.loadVertexValues(fileItemJobId.getItemId(), destinationVertex, _this.filterParamNameVertexValues);
                    });
                    // this.filterService.loadFilter(this.gobiiExtractFilterType,
                    //     this.filterParamNameVertices,
                    //     vertexId);
                    if (!this.gobiiExtractFilterType) {
                        this.store.dispatch(new historyAction.AddStatusMessageAction("The gobiiExtractFilterType property is not set"));
                    }
                };
                FlexQueryFilterComponent.prototype.ngOnChanges = function (changes) {
                    if (changes['gobiiExtractFilterType']
                        && (changes['gobiiExtractFilterType'].currentValue != null)
                        && (changes['gobiiExtractFilterType'].currentValue != undefined)) {
                        if (changes['gobiiExtractFilterType'].currentValue != changes['gobiiExtractFilterType'].previousValue) {
                            if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY) {
                                this.flexQueryService.loadVertices(this.filterParamNameVertices);
                            }
                        } // if we have a new filter type
                    } // if filter type changed
                }; // ngonChanges
                FlexQueryFilterComponent = __decorate([
                    core_1.Component({
                        selector: 'flex-query-filter',
                        inputs: ['gobiiExtractFilterType', 'filterParamNameVertices', 'filterParamNameVertexValues'],
                        outputs: [],
                        styleUrls: ["css/extractor-ui.css"],
                        template: "\n        <div class=\"panel panel-primary\" [ngStyle]=\"currentStyle\">\n            <div class=\"panel-heading\">\n                <h3 class=\"panel-title\">Filters</h3>\n            </div>\n            <div class=\"panel-body\">\n                <label class=\"the-label\">Entity:</label><BR>\n                <p-dropdown [options]=\"fileItemsEntityNames$ | async\"\n                            [style]=\"{'width': '100%'}\"\n                            optionLabel=\"_itemName\"\n                            (onChange)=\"handleVertexSelected($event)\"\n                            [disabled]=\"currentStyle===disabledStyle\">\n                </p-dropdown>\n\n                <BR>\n                <BR>\n                <label class=\"the-label\">Select Entity Values</label><BR>\n                <p-listbox [options]=\"fileItemsEntityValues$ | async\"\n                           [multiple]=\"true\"\n                           [(ngModel)]=\"selectedEntityValues\" [style]=\"{'width':'100%'}\"\n                           optionLabel=\"_itemName\"\n                           [disabled]=\"currentStyle===disabledStyle\"></p-listbox>\n            </div>\n\n            <div class=\"container\">\n                <p>Count: {{totalValues}} </p>\n                <p>Selected: {{selectedEntityValues ? selectedEntityValues.length : 0}}</p>\n            </div>\n        </div>" // end template
                    }),
                    __metadata("design:paramtypes", [store_1.Store,
                        nameid_file_item_service_1.NameIdFileItemService,
                        filter_service_1.FilterService,
                        flex_query_service_1.FlexQueryService])
                ], FlexQueryFilterComponent);
                return FlexQueryFilterComponent;
            }());
            exports_1("FlexQueryFilterComponent", FlexQueryFilterComponent);
        }
    };
});
//# sourceMappingURL=flex-query-filter.component.js.map