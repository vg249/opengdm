System.register(["@angular/core", "../model/type-extractor-filter", "../services/core/file-model-tree-service", "../model/gobii-file-item", "../model/type-process", "../model/file-model-node"], function (exports_1, context_1) {
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
    var core_1, type_extractor_filter_1, file_model_tree_service_1, gobii_file_item_1, type_process_1, file_model_node_1, SampleMarkerBoxComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
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
            SampleMarkerBoxComponent = (function () {
                function SampleMarkerBoxComponent(_fileModelTreeService) {
                    this._fileModelTreeService = _fileModelTreeService;
                    this.gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN;
                    this.onSampleMarkerError = new core_1.EventEmitter();
                    this.onMarkerSamplesCompleted = new core_1.EventEmitter();
                }
                // private handleUserSelected(arg) {
                //     this.onUserSelected.emit(this.nameIdList[arg.srcElement.selectedIndex].id);
                // }
                //
                // constructor(private _dtoRequestService:DtoRequestService<NameId[]>) {
                //
                // } // ctor
                SampleMarkerBoxComponent.prototype.handleTextBoxDataSubmitted = function (items) {
                    var _this = this;
                    var listItemType = this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER ?
                        file_model_node_1.ExtractorItemType.MARKER_LIST_ITEM : file_model_node_1.ExtractorItemType.SAMPLE_LIST_ITEM;
                    items.forEach(function (listItem) {
                        if (listItem && listItem !== "") {
                            _this._fileModelTreeService
                                .put(gobii_file_item_1.GobiiFileItem.build(_this.gobiiExtractFilterType, type_process_1.ProcessType.CREATE)
                                .setExtractorItemType(listItemType)
                                .setItemId(listItem)
                                .setItemName(listItem))
                                .subscribe(null, function (headerStatusMessage) {
                                _this.handleStatusHeaderMessage(headerStatusMessage);
                            });
                        }
                    });
                };
                SampleMarkerBoxComponent.prototype.handleStatusHeaderMessage = function (statusMessage) {
                    this.onSampleMarkerError.emit(statusMessage);
                };
                SampleMarkerBoxComponent.prototype.ngOnInit = function () {
                    return null;
                };
                return SampleMarkerBoxComponent;
            }());
            SampleMarkerBoxComponent = __decorate([
                core_1.Component({
                    selector: 'sample-marker-box',
                    inputs: ['gobiiExtractFilterType'],
                    outputs: ['onSampleMarkerError'],
                    template: "<div class=\"container-fluid\">\n            \n                <div class=\"row\">\n                \n                    <div class=\"col-md-2\"> \n                        <input type=\"radio\" \n                            (change)=\"handleListTypeSelected($event)\" \n                            name=\"listType\" \n                            value=\"uploadFile\" \n                            checked=\"checked\">&nbsp;File\n                    </div> \n                    \n                    <div class=\"col-md-8\">\n                        <uploader\n                        [gobiiExtractFilterType] = \"gobiiExtractFilterType\"\n                        (onUploaderError)=\"handleStatusHeaderMessage($event)\"></uploader>\n                    </div> \n                    \n                 </div>\n                 \n                <div class=\"row\">\n                \n                    <div class=\"col-md-2\">\n                        <input type=\"radio\" \n                            (change)=\"handleListTypeSelected($event)\" \n                            name=\"listType\" \n                            value=\"pasteList\" >&nbsp;List\n                    </div> \n                    \n                    <div class=\"col-md-8\">\n                        <text-area\n                        (onTextboxDataComplete)=\"handleTextBoxDataSubmitted($event)\"></text-area>\n                    </div> \n                    \n                 </div>\n                 \n"
                }),
                __metadata("design:paramtypes", [file_model_tree_service_1.FileModelTreeService])
            ], SampleMarkerBoxComponent);
            exports_1("SampleMarkerBoxComponent", SampleMarkerBoxComponent);
        }
    };
});
//# sourceMappingURL=sample-marker-box.component.js.map