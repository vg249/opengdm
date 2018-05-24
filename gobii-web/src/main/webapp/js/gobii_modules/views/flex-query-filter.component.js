System.register(["@angular/core", "../model/type-extractor-filter", "@ngrx/store", "../store/reducers", "../services/core/nameid-file-item-service", "../services/core/filter-service", "../services/core/flex-query-service", "../model/name-id-label-type", "../services/core/filter-params-coll"], function (exports_1, context_1) {
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
    var core_1, type_extractor_filter_1, store_1, fromRoot, nameid_file_item_service_1, filter_service_1, flex_query_service_1, name_id_label_type_1, filter_params_coll_1, FlexQueryFilterComponent;
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
            function (nameid_file_item_service_1_1) {
                nameid_file_item_service_1 = nameid_file_item_service_1_1;
            },
            function (filter_service_1_1) {
                filter_service_1 = filter_service_1_1;
            },
            function (flex_query_service_1_1) {
                flex_query_service_1 = flex_query_service_1_1;
            },
            function (name_id_label_type_1_1) {
                name_id_label_type_1 = name_id_label_type_1_1;
            },
            function (filter_params_coll_1_1) {
                filter_params_coll_1 = filter_params_coll_1_1;
            }
        ],
        execute: function () {
            FlexQueryFilterComponent = (function () {
                function FlexQueryFilterComponent(store, fileItemService, filterService, filterParamsColl, flexQueryService) {
                    this.store = store;
                    this.fileItemService = fileItemService;
                    this.filterService = filterService;
                    this.filterParamsColl = filterParamsColl;
                    this.flexQueryService = flexQueryService;
                    //these are dummy place holders for now
                    this.totalValues = "0";
                    this.enabledStyle = null;
                    this.disabledStyle = { 'background': '#dddddd' };
                    this.currentStyle = this.disabledStyle;
                } // ctor
                FlexQueryFilterComponent.prototype.ngOnInit = function () {
                    var _this = this;
                    this.fileItemsVertexNames$ = this.filterService.getForFilter(this.filterParamNameVertices);
                    this.fileItemsEntityValues$ = this.filterService.getForFilter(this.filterParamNameVertexValues);
                    this.JobId$ = this.store.select(fromRoot.getJobId);
                    // this
                    //     .fileItemsVertexNames$
                    //     .subscribe(items => {
                    //             if (this.previousSelectedItemId === null && items && items.length > 0) {
                    //                 this.previousSelectedItemId = items[0].getFileItemUniqueId()
                    //             }
                    //
                    //             if (items.length > 1) {
                    //                 this.currentStyle = this.enabledStyle;
                    //             } else {
                    //                 this.currentStyle = this.disabledStyle;
                    //             }
                    //
                    //             // if (items[0]) {
                    //             //     this.selectedAllowableEntities = items[0];
                    //             // }
                    //
                    //         },
                    //         error => {
                    //             this.store.dispatch(new historyAction.AddStatusMessageAction(error))
                    //         });
                    this
                        .fileItemsEntityValues$
                        .subscribe(function (items) {
                        _this.totalValues = items.length.toString();
                    });
                    this.setControlState(false);
                    this.store.select(fromRoot.getFileItemsFilters)
                        .subscribe(function (filters) {
                        // you have to reset from state because this control won't see the sibling control's
                        // change event
                        var thisControlVertexfilterParams = _this.filterParamsColl.getFilter(_this.filterParamNameVertices, type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY);
                        var currentVertexFilter = filters[thisControlVertexfilterParams.getQueryName()];
                        if (currentVertexFilter) {
                            if (!currentVertexFilter.targetEntityFilterValue) {
                                _this.selectedVertex = null;
                                _this.selectedVertexValues = null;
                            }
                        }
                        if (!thisControlVertexfilterParams.getPreviousSiblingFileItemParams()) {
                            _this.setControlState(true);
                        }
                        else if (thisControlVertexfilterParams.getPreviousSiblingFileItemParams().getChildFileItemParams().length > 0) {
                            var vertexValuePreviousVertexSelectorParamName = thisControlVertexfilterParams
                                .getPreviousSiblingFileItemParams()
                                .getChildFileItemParams()[0].getQueryName();
                            var previousVertexValuesFilter = filters[vertexValuePreviousVertexSelectorParamName];
                            if (previousVertexValuesFilter && previousVertexValuesFilter.targetEntityFilterValue) {
                                _this.setControlState(true);
                            }
                            else {
                                _this.setControlState(false);
                            }
                        } // if-else there are previous sibling params
                    }); // subscribe to select filters()
                }; // ngInit()
                FlexQueryFilterComponent.prototype.setControlState = function (enabled) {
                    if (enabled) {
                        this.currentStyle = this.enabledStyle;
                    }
                    else {
                        this.currentStyle = this.disabledStyle;
                    }
                };
                FlexQueryFilterComponent.prototype.handleVertexSelected = function (arg) {
                    var _this = this;
                    if (arg.value && arg.value._entity) {
                        var vertexId = void 0;
                        if (arg.value.getNameIdLabelType() === name_id_label_type_1.NameIdLabelType.UNKNOWN) {
                            vertexId = arg.value.getItemId();
                        }
                        else {
                            vertexId = null;
                        }
                        this.flexQueryService.loadSelectedVertexFilter(this.filterParamNameVertices, vertexId);
                    }
                    this.JobId$.subscribe(function (fileItemJobId) {
                        _this.flexQueryService.loadVertexValues(fileItemJobId.getItemId(), arg.value, _this.filterParamNameVertexValues);
                    });
                };
                FlexQueryFilterComponent.prototype.handleVertexValueSelected = function (arg) {
                    var selectedVertexValueGfis = this.selectedVertexValues;
                    this.flexQueryService.loadSelectedVertexValueFilters(this.filterParamNameVertexValues, selectedVertexValueGfis);
                };
                FlexQueryFilterComponent.prototype.ngOnChanges = function (changes) {
                    if (changes['gobiiExtractFilterType']
                        && (changes['gobiiExtractFilterType'].currentValue != null)
                        && (changes['gobiiExtractFilterType'].currentValue != undefined)) {
                        if (changes['gobiiExtractFilterType'].currentValue != changes['gobiiExtractFilterType'].previousValue) {
                            if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY) {
                                // this.flexQueryService.loadVertices(this.filterParamNameVertices);
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
                        template: "\n        <div class=\"panel panel-primary\" [ngStyle]=\"currentStyle\">\n            <div class=\"panel-heading\">\n                <h3 class=\"panel-title\">Filters</h3>\n            </div>\n            <div class=\"panel-body\">\n                <label class=\"the-label\">Entity:</label><BR>\n                <p-dropdown [options]=\"fileItemsVertexNames$ | async\"\n                            [style]=\"{'width': '100%'}\"\n                            optionLabel=\"_itemName\"\n                            [(ngModel)]=\"selectedVertex\"\n                            (onChange)=\"handleVertexSelected($event)\"\n                            [disabled]=\"currentStyle===disabledStyle\">\n                </p-dropdown>\n\n                <BR>\n                <BR>\n                <label class=\"the-label\">Select Entity Values</label><BR>\n                <p-listbox [options]=\"fileItemsEntityValues$ | async\"\n                           [multiple]=\"true\"\n                           [(ngModel)]=\"selectedVertexValues\" \n                           [style]=\"{'width':'100%'}\"\n                           (onChange)=\"handleVertexValueSelected($event)\"\n                           optionLabel=\"_itemName\"\n                           [disabled]=\"currentStyle===disabledStyle\"></p-listbox>\n            </div>\n\n            <div class=\"container\">\n                <p>Count: {{totalValues}} </p>\n                <p>Selected: {{selectedVertexValues ? selectedVertexValues.length : 0}}</p>\n            </div>\n        </div>" // end template
                    }),
                    __metadata("design:paramtypes", [store_1.Store,
                        nameid_file_item_service_1.NameIdFileItemService,
                        filter_service_1.FilterService,
                        filter_params_coll_1.FilterParamsColl,
                        flex_query_service_1.FlexQueryService])
                ], FlexQueryFilterComponent);
                return FlexQueryFilterComponent;
            }());
            exports_1("FlexQueryFilterComponent", FlexQueryFilterComponent);
        }
    };
});
//# sourceMappingURL=flex-query-filter.component.js.map