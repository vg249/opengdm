System.register(["@angular/core", "@ngrx/store", "../store/reducers", "../model/type-extractor-filter", "../services/core/file-item-service", "../model/file-item-param-names", "../store/actions/fileitem-action"], function (exports_1, context_1) {
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
    var core_1, store_1, fromRoot, type_extractor_filter_1, file_item_service_1, file_item_param_names_1, fileAction, DatasetDatatableComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (store_1_1) {
                store_1 = store_1_1;
            },
            function (fromRoot_1) {
                fromRoot = fromRoot_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (file_item_service_1_1) {
                file_item_service_1 = file_item_service_1_1;
            },
            function (file_item_param_names_1_1) {
                file_item_param_names_1 = file_item_param_names_1_1;
            },
            function (fileAction_1) {
                fileAction = fileAction_1;
            }
        ],
        execute: function () {
            DatasetDatatableComponent = (function () {
                //cars: Car[];
                function DatasetDatatableComponent(store, fileItemService) {
                    this.store = store;
                    this.fileItemService = fileItemService;
                    this.datasetsFileItems$ = this.store.select(fromRoot.getDatsetEntities);
                    this.nameIdFilterParamTypes = Object.assign({}, file_item_param_names_1.FilterParamNames);
                }
                DatasetDatatableComponent.prototype.selectDataset = function (event, dataSet, datasetOverlayPanel) {
                    //this.selectedCar = car;
                    datasetOverlayPanel.show(event);
                };
                DatasetDatatableComponent.prototype.handleRowChecked = function (checked, selectedDatasetFileItem) {
                    this.handleItemChecked(selectedDatasetFileItem.getFileItemUniqueId(), checked);
                };
                DatasetDatatableComponent.prototype.handleRowSelect = function (event) {
                    var selectedDatasetFileItem = event.data;
                    this.handleItemChecked(selectedDatasetFileItem.getFileItemUniqueId(), event.originalEvent.checked);
                };
                DatasetDatatableComponent.prototype.handleRowUnSelect = function (event) {
                    var selectedDatasetFileItem = event.data;
                    this.handleItemChecked(selectedDatasetFileItem.getFileItemUniqueId(), event.originalEvent.checked);
                };
                DatasetDatatableComponent.prototype.handleOnRowClick = function (event) {
                    var selectedDataset = event.data;
                };
                DatasetDatatableComponent.prototype.handleItemChecked = function (currentFileItemUniqueId, isChecked) {
                    if (isChecked) {
                        this.store.dispatch(new fileAction.AddToExtractByItemIdAction(currentFileItemUniqueId));
                    }
                    else {
                        this.store.dispatch(new fileAction.RemoveFromExractByItemIdAction(currentFileItemUniqueId));
                    }
                }; // handleItemChecked()
                DatasetDatatableComponent.prototype.ngOnInit = function () {
                }; // ngOnInit()
                // gobiiExtractType is not set until you get OnChanges
                DatasetDatatableComponent.prototype.ngOnChanges = function (changes) {
                    if (changes['gobiiExtractFilterType']
                        && (changes['gobiiExtractFilterType'].currentValue != null)
                        && (changes['gobiiExtractFilterType'].currentValue != undefined)) {
                        if (changes['gobiiExtractFilterType'].currentValue != changes['gobiiExtractFilterType'].previousValue) {
                            this.fileItemService.loadEntityList(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.DATASETS);
                            this.fileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.CV_JOB_STATUS, null);
                        } // if we have a new filter type
                    } // if filter type changed
                }; // ngonChanges
                __decorate([
                    core_1.Input(),
                    __metadata("design:type", Number)
                ], DatasetDatatableComponent.prototype, "gobiiExtractFilterType", void 0);
                DatasetDatatableComponent = __decorate([
                    core_1.Component({
                        selector: 'dataset-datatable',
                        inputs: [],
                        outputs: [],
                        template: "\n        <div style=\"border: 1px solid #336699; padding-left: 5px\">\n            <div class=\"container-fluid\" >\n                <div class=\"row\">\n                    <BR>\n                    <label class=\"the-legend\">Filter by Status:&nbsp;</label>\n                    <name-id-list-box\n                            [gobiiExtractFilterType]=\"gobiiExtractFilterType\"\n                            [filterParamName]=\"nameIdFilterParamTypes.CV_JOB_STATUS\">\n                    </name-id-list-box>\n\n                </div> <!--status selector row -->\n                <div class=\"row\">\n                    <p-dataTable [value]=\"datasetsFileItems$ | async\"\n                                 [(selection)]=\"selectedDatasets\"\n                                 (onRowSelect)=\"handleRowSelect($event)\"\n                                 (onRowUnselect)=\"handleRowUnSelect($event)\"\n                                 (onRowClick)=\"handleOnRowClick($event)\"\n                                 dataKey=\"_entity.id\">\n                        <p-column [style]=\"{'width':'30px'}\">\n                            <ng-template let-col let-fi=\"rowData\" pTemplate=\"body\">\n                                <p-checkbox binary=\"true\"\n                                            [ngModel]=\"fi.getSelected()\"\n                                            (onChange)=\"handleRowChecked($event, fi)\"\n                                            [disabled]=\"fi.getEntity().jobStatusName !== 'completed' || (fi.getEntity().jobTypeName !== 'load')\">\n                                </p-checkbox>\n\n                            </ng-template>\n                        </p-column>\n                        <p-column [style]=\"{'width':'10%','text-align':'center'}\">\n                            <ng-template let-col=\"rowData\" pTemplate=\"body\">\n                                <button type=\"button\" pButton (click)=\"selectDataset($event,col,datasetOverlayPanel);\" icon=\"fa-search\"></button>\n                            </ng-template>\n                        </p-column>                      \n                        <p-column field=\"_entity.id\" header=\"Id\" hidden=\"true\"></p-column>\n                        <p-column field=\"_entity.datasetName\" header=\"Name\"></p-column>\n                        <p-column field=\"_entity.jobStatusName\" header=\"Status\"></p-column>\n                        <p-column field=\"_entity.jobTypeName\" header=\"Type\"></p-column>\n                        <p-column field=\"jobSubmittedDate\" header=\"Submitted\">\n                            <ng-template let-col let-fi=\"rowData\" pTemplate=\"body\">\n                                {{fi._entity[col.field] | date:'yyyy-MM-dd HH:mm' }}\n                            </ng-template>\n                        </p-column>\n                    </p-dataTable>\n                    <p-overlayPanel #datasetOverlayPanel appendTo=\"body\" showCloseIcon=\"true\">\n\n                        <!--<p>A bunch of text.A bunch of text. A bunch of text. A bunch of text. A bunch of text. A bunch of text. A bunch of text. A bunch of text. A bunch of text. A bunch of text. A bunch of text.  </p>-->\n                        <fieldset>\n                            <legend>Personalia:</legend>\n                            Name: <input type=\"text\"><br>\n                            Email: <input type=\"text\"><br>\n                            Date of birth: <input type=\"text\">\n                        </fieldset>\n                    </p-overlayPanel>\n                    \n                </div> <!-- table row -->\n            </div><!--container  -->\n            <!--<button type=\"text\" pButton label=\"Basic\" (click)=\"datasetOverlayPanel.toggle($event, actualTarget)\"></button>-->\n            <!--<p (mouseenter)=\"datasetOverlayPanel.show($event,  actualTarget)\">A random paragraph</p>-->\n        </div> <!-- enclosing box  -->\n    " // end template
                    }),
                    __metadata("design:paramtypes", [store_1.Store,
                        file_item_service_1.FileItemService])
                ], DatasetDatatableComponent);
                return DatasetDatatableComponent;
            }());
            exports_1("DatasetDatatableComponent", DatasetDatatableComponent);
        }
    };
});
//# sourceMappingURL=dataset-datatable.component.js.map