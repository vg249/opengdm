System.register(["@angular/core", "../model/type-extractor-filter", "../services/core/file-model-tree-service", "../model/gobii-file-item", "../model/type-process", "../model/file-model-node", "./entity-labels"], function (exports_1, context_1) {
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
    var core_1, type_extractor_filter_1, file_model_tree_service_1, gobii_file_item_1, type_process_1, file_model_node_1, entity_labels_1, SampleMarkerBoxComponent;
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
            },
            function (entity_labels_1_1) {
                entity_labels_1 = entity_labels_1_1;
            }
        ],
        execute: function () {
            SampleMarkerBoxComponent = (function () {
                function SampleMarkerBoxComponent(_fileModelTreeService) {
                    this._fileModelTreeService = _fileModelTreeService;
                    this.displayChoicePrompt = false;
                    this.uploadSelected = true;
                    this.listSelected = false;
                    this.gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN;
                    this.onSampleMarkerError = new core_1.EventEmitter();
                    this.onMarkerSamplesCompleted = new core_1.EventEmitter();
                    this.currentFileItems = [];
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
                SampleMarkerBoxComponent.prototype.handleSampleMarkerChoicesExist = function () {
                    var _this = this;
                    var returnVal = false;
                    this._fileModelTreeService.getFileItems(this.gobiiExtractFilterType).subscribe(function (fileItems) {
                        var extractorItemTypeListToFind = file_model_node_1.ExtractorItemType.UNKNOWN;
                        var extractorItemTypeFileToFind = file_model_node_1.ExtractorItemType.UNKNOWN;
                        if (_this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE) {
                            extractorItemTypeListToFind = file_model_node_1.ExtractorItemType.SAMPLE_LIST_ITEM;
                            extractorItemTypeFileToFind = file_model_node_1.ExtractorItemType.SAMPLE_FILE;
                        }
                        else if (_this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER) {
                            extractorItemTypeListToFind = file_model_node_1.ExtractorItemType.MARKER_LIST_ITEM;
                            extractorItemTypeFileToFind = file_model_node_1.ExtractorItemType.MARKER_FILE;
                        }
                        _this.currentFileItems = fileItems.filter(function (item) {
                            return ((item.getExtractorItemType() === extractorItemTypeListToFind) ||
                                (item.getExtractorItemType() === extractorItemTypeFileToFind));
                        });
                        if (_this.currentFileItems.length > 0) {
                            _this.extractTypeLabelExisting = entity_labels_1.Labels.instance().treeExtractorTypeLabels[_this.currentFileItems[0].getExtractorItemType()];
                            if (_this.currentFileItems[0].getExtractorItemType() === file_model_node_1.ExtractorItemType.SAMPLE_LIST_ITEM) {
                                _this.extractTypeLabelProposed = entity_labels_1.Labels.instance().treeExtractorTypeLabels[file_model_node_1.ExtractorItemType.SAMPLE_FILE];
                            }
                            else if (_this.currentFileItems[0].getExtractorItemType() === file_model_node_1.ExtractorItemType.MARKER_LIST_ITEM) {
                                _this.extractTypeLabelProposed = entity_labels_1.Labels.instance().treeExtractorTypeLabels[file_model_node_1.ExtractorItemType.MARKER_FILE];
                            }
                            else if (_this.currentFileItems[0].getExtractorItemType() === file_model_node_1.ExtractorItemType.SAMPLE_FILE) {
                                _this.extractTypeLabelProposed = entity_labels_1.Labels.instance().treeExtractorTypeLabels[file_model_node_1.ExtractorItemType.MARKER_LIST_ITEM];
                            }
                            else if (_this.currentFileItems[0].getExtractorItemType() === file_model_node_1.ExtractorItemType.MARKER_FILE) {
                                _this.extractTypeLabelProposed = entity_labels_1.Labels.instance().treeExtractorTypeLabels[file_model_node_1.ExtractorItemType.MARKER_LIST_ITEM];
                            }
                            _this.displayChoicePrompt = true;
                            returnVal = true;
                        }
                        else {
                        }
                    }, function (hsm) {
                        _this.handleStatusHeaderMessage(hsm);
                    });
                    // if (event.currentTarget.defaultValue === "itemArray") {
                    //
                    // } else if (event.currentTarget.defaultValue == "itemFile") {
                    //
                    // }
                    return returnVal;
                };
                SampleMarkerBoxComponent.prototype.handleUserChoice = function (userChoice) {
                    var _this = this;
                    this.displayChoicePrompt = false;
                    if (this.currentFileItems.length > 0 && userChoice === true) {
                        // based on what _was_ the current item, we now make the current selection the other one
                        if (this.currentFileItems[0].getExtractorItemType() === file_model_node_1.ExtractorItemType.MARKER_LIST_ITEM
                            || this.currentFileItems[0].getExtractorItemType() === file_model_node_1.ExtractorItemType.SAMPLE_LIST_ITEM) {
                            this.listSelected = false;
                            this.uploadSelected = true;
                        }
                        else if (this.currentFileItems[0].getExtractorItemType() === file_model_node_1.ExtractorItemType.MARKER_FILE
                            || this.currentFileItems[0].getExtractorItemType() === file_model_node_1.ExtractorItemType.SAMPLE_FILE) {
                            this.listSelected = true;
                            this.uploadSelected = false;
                        }
                        this.currentFileItems.forEach(function (currentFileItem) {
                            currentFileItem.setProcessType(type_process_1.ProcessType.DELETE);
                            _this._fileModelTreeService
                                .put(currentFileItem)
                                .subscribe(function (fmte) {
                            }, function (headerStatusMessage) {
                                _this.handleStatusHeaderMessage(headerStatusMessage);
                            });
                        });
                    }
                };
                SampleMarkerBoxComponent.prototype.handleTextBoxChanged = function (event) {
                    // if there is no existing selected list or file, then this is just a simple setting
                    if (this.handleSampleMarkerChoicesExist() === false) {
                        this.listSelected = true;
                        this.uploadSelected = false;
                    }
                };
                SampleMarkerBoxComponent.prototype.handleOnClickBrowse = function ($event) {
                    if (this.handleSampleMarkerChoicesExist() === false) {
                        this.listSelected = false;
                        this.uploadSelected = true;
                    }
                };
                SampleMarkerBoxComponent.prototype.handleStatusHeaderMessage = function (statusMessage) {
                    this.onSampleMarkerError.emit(statusMessage);
                };
                SampleMarkerBoxComponent.prototype.ngOnInit = function () {
                    //        this.extractTypeLabel = Labels.instance().extractorFilterTypeLabels[this.gobiiExtractFilterType];
                    return null;
                };
                return SampleMarkerBoxComponent;
            }());
            SampleMarkerBoxComponent = __decorate([
                core_1.Component({
                    selector: 'sample-marker-box',
                    inputs: ['gobiiExtractFilterType'],
                    outputs: ['onSampleMarkerError'],
                    template: "<div class=\"container-fluid\">\n            \n                <div class=\"row\">\n                \n                    <div class=\"col-md-2\"> \n                        <input type=\"radio\" \n                            (change)=\"handleOnClickBrowse($event)\" \n                            name=\"listType\" \n                            value=\"itemFile\" \n                            [checked]=\"uploadSelected\">&nbsp;File\n                    </div> \n                    \n                    <div class=\"col-md-8\">\n                        <uploader\n                        [gobiiExtractFilterType] = \"gobiiExtractFilterType\"\n                        (onUploaderError)=\"handleStatusHeaderMessage($event)\"\n                        (onClickBrowse)=\"handleOnClickBrowse($event)\"></uploader>\n                    </div> \n                    \n                 </div>\n                 \n                <div class=\"row\">\n                \n                    <div class=\"col-md-2\">\n                        <input type=\"radio\" \n                            (change)=\"handleTextBoxChanged($event)\" \n                            name=\"listType\" \n                            value=\"itemArray\"\n                            [checked]=\"listSelected\">&nbsp;List\n                    </div> \n                    \n                    <div class=\"col-md-8\">\n                        <text-area\n                        (onTextboxDataComplete)=\"handleTextBoxDataSubmitted($event)\"\n                        (onTextboxClicked)=\"handleTextBoxChanged($event)\"></text-area>\n                    </div> \n                    \n                 </div>\n                 <div>\n                    <p-dialog header=\"{{extractTypeLabelExisting}} Already Selelected\" [(visible)]=\"displayChoicePrompt\" modal=\"modal\" width=\"300\" height=\"300\" responsive=\"true\">\n                        <p>A {{extractTypeLabelExisting}} is already selected. Do you want to remove it and specify a {{extractTypeLabelProposed}} instead?</p>\n                            <p-footer>\n                                <div class=\"ui-dialog-buttonpane ui-widget-content ui-helper-clearfix\">\n                                    <button type=\"button\" pButton icon=\"fa-close\" (click)=\"handleUserChoice(false)\" label=\"No\"></button>\n                                    <button type=\"button\" pButton icon=\"fa-check\" (click)=\"handleUserChoice(true)\" label=\"Yes\"></button>\n                                </div>\n                            </p-footer>\n                    </p-dialog>\n                  </div>\n"
                }),
                __metadata("design:paramtypes", [file_model_tree_service_1.FileModelTreeService])
            ], SampleMarkerBoxComponent);
            exports_1("SampleMarkerBoxComponent", SampleMarkerBoxComponent);
        }
    };
});
//# sourceMappingURL=sample-marker-box.component.js.map