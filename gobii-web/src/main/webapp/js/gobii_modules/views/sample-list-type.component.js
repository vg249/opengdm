System.register(["@angular/core", "../model/type-extractor-filter", "../model/type-extractor-sample-list", "../services/core/file-model-tree-service", "../model/gobii-file-item", "../model/type-process", "../model/file-model-node"], function (exports_1, context_1) {
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
    var core_1, type_extractor_filter_1, type_extractor_sample_list_1, file_model_tree_service_1, gobii_file_item_1, type_process_1, file_model_node_1, SampleListTypeComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (type_extractor_sample_list_1_1) {
                type_extractor_sample_list_1 = type_extractor_sample_list_1_1;
            },
            function (file_model_tree_service_1_1) {
                file_model_tree_service_1 = file_model_tree_service_1_1;
            },
            function (gobii_file_item_1_1) {
                gobii_file_item_1 = gobii_file_item_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (file_model_node_1_1) {
                file_model_node_1 = file_model_node_1_1;
            }
        ],
        execute: function () {
            SampleListTypeComponent = (function () {
                function SampleListTypeComponent(_fileModelTreeService) {
                    this._fileModelTreeService = _fileModelTreeService;
                    this.onSampleListTypeSelected = new core_1.EventEmitter();
                    this.onHeaderStatusMessage = new core_1.EventEmitter();
                    this.gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN;
                } // ctor
                SampleListTypeComponent.prototype.handleExportTypeSelected = function (arg) {
                    if (arg.srcElement.checked) {
                        var radioValue = arg.srcElement.value;
                        var gobiiSampleListType = type_extractor_sample_list_1.GobiiSampleListType[radioValue];
                        this.submitSampleListTypeToService(gobiiSampleListType);
                        this.onSampleListTypeSelected.emit(gobiiSampleListType);
                    }
                };
                SampleListTypeComponent.prototype.submitSampleListTypeToService = function (gobiiSampleListType) {
                    var _this = this;
                    this._fileModelTreeService
                        .put(gobii_file_item_1.GobiiFileItem.build(this.gobiiExtractFilterType, type_process_1.ProcessType.CREATE)
                        .setExtractorItemType(file_model_node_1.ExtractorItemType.SAMPLE_LIST_TYPE)
                        .setItemName(type_extractor_sample_list_1.GobiiSampleListType[gobiiSampleListType])
                        .setItemId(type_extractor_sample_list_1.GobiiSampleListType[gobiiSampleListType]))
                        .subscribe(null, function (he) {
                        _this.onHeaderStatusMessage.emit(he);
                    });
                };
                SampleListTypeComponent.prototype.ngOnInit = function () {
                    return null;
                };
                SampleListTypeComponent.prototype.ngOnChanges = function (changes) {
                    this.submitSampleListTypeToService(type_extractor_sample_list_1.GobiiSampleListType.GERMPLASM_NAME);
                    var scope$ = this;
                    this._fileModelTreeService
                        .fileItemNotifications()
                        .subscribe(function (fileItem) {
                        if (fileItem.getProcessType() === type_process_1.ProcessType.NOTIFY
                            && fileItem.getExtractorItemType() === file_model_node_1.ExtractorItemType.STATUS_DISPLAY_TREE_READY) {
                            scope$.submitSampleListTypeToService(type_extractor_sample_list_1.GobiiSampleListType.GERMPLASM_NAME);
                        }
                    });
                };
                return SampleListTypeComponent;
            }());
            SampleListTypeComponent = __decorate([
                core_1.Component({
                    selector: 'sample-list-type',
                    inputs: ['gobiiExtractFilterType'],
                    outputs: ['onSampleListTypeSelected', 'onHeaderStatusMessage'],
                    template: "<label class=\"the-label\">Export By:</label><BR>\n                  <input type=\"radio\" (change)=\"handleExportTypeSelected($event)\" name=\"format\" value=\"GERMPLASM_NAME\" checked=\"checked\">Germplasm Name<BR>\n                  <input type=\"radio\" (change)=\"handleExportTypeSelected($event)\" name=\"format\" value=\"EXTERNAL_CODE\">External Code<BR>\n                  <input type=\"radio\" (change)=\"handleExportTypeSelected($event)\" name=\"format\" value=\"DNA_SAMPLE\">DNA Sample<BR>" // end template
                }),
                __metadata("design:paramtypes", [file_model_tree_service_1.FileModelTreeService])
            ], SampleListTypeComponent);
            exports_1("SampleListTypeComponent", SampleListTypeComponent);
        }
    };
});
//# sourceMappingURL=sample-list-type.component.js.map