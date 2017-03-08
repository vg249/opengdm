System.register(["@angular/core", "../model/type-extract-format", "../services/core/file-model-tree-service", "../model/file-item", "../model/type-process", "../model/file-model-node"], function (exports_1, context_1) {
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
    var core_1, type_extract_format_1, file_model_tree_service_1, file_item_1, type_process_1, file_model_node_1, ExportFormatComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_extract_format_1_1) {
                type_extract_format_1 = type_extract_format_1_1;
            },
            function (file_model_tree_service_1_1) {
                file_model_tree_service_1 = file_model_tree_service_1_1;
            },
            function (file_item_1_1) {
                file_item_1 = file_item_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (file_model_node_1_1) {
                file_model_node_1 = file_model_node_1_1;
            }
        ],
        execute: function () {
            ExportFormatComponent = (function () {
                function ExportFormatComponent(_fileModelTreeService) {
                    this._fileModelTreeService = _fileModelTreeService;
                    this.onFormatSelected = new core_1.EventEmitter();
                    this.onError = new core_1.EventEmitter();
                    this.selectedExtractFormat = type_extract_format_1.GobiiExtractFormat.HAPMAP;
                } // ctor
                ExportFormatComponent.prototype.ngOnInit = function () {
                    //this.updateTreeService(GobiiExtractFormat.HAPMAP);
                };
                ExportFormatComponent.prototype.handleResponseHeader = function (header) {
                    this.onError.emit(header);
                };
                ExportFormatComponent.prototype.handleFormatSelected = function (arg) {
                    if (arg.srcElement.checked) {
                        var radioValue = arg.srcElement.value;
                        this.selectedExtractFormat = type_extract_format_1.GobiiExtractFormat[radioValue];
                        this.updateTreeService(this.selectedExtractFormat);
                    }
                    var foo = arg;
                    //this.onContactSelected.emit(this.nameIdList[arg.srcElement.selectedIndex].id);
                };
                ExportFormatComponent.prototype.updateTreeService = function (arg) {
                    var _this = this;
                    this.selectedExtractFormat = arg;
                    var extractFilterTypeFileItem = file_item_1.FileItem
                        .build(this.gobiiExtractFilterType, type_process_1.ProcessType.UPDATE)
                        .setExtractorItemType(file_model_node_1.ExtractorItemType.EXPORT_FORMAT)
                        .setItemId(type_extract_format_1.GobiiExtractFormat[arg])
                        .setItemName(type_extract_format_1.GobiiExtractFormat[type_extract_format_1.GobiiExtractFormat[arg]]);
                    this._fileModelTreeService.put(extractFilterTypeFileItem)
                        .subscribe(null, function (headerResponse) {
                        _this.handleResponseHeader(headerResponse);
                    });
                    //console.log("selected contact itemId:" + arg);
                };
                ExportFormatComponent.prototype.ngOnChanges = function (changes) {
                    if (changes['gobiiExtractFilterType']
                        && (changes['gobiiExtractFilterType'].currentValue != null)
                        && (changes['gobiiExtractFilterType'].currentValue != undefined)) {
                        if (changes['gobiiExtractFilterType'].currentValue != changes['gobiiExtractFilterType'].previousValue) {
                            this.updateTreeService(type_extract_format_1.GobiiExtractFormat.HAPMAP);
                        } // if we have a new filter type
                    } // if filter type changed
                }; // ngonChanges
                return ExportFormatComponent;
            }());
            ExportFormatComponent = __decorate([
                core_1.Component({
                    selector: 'export-format',
                    outputs: ['onFormatSelected', 'onError'],
                    inputs: ['gobiiExtractFilterType'],
                    //directives: [RADIO_GROUP_DIRECTIVES]
                    //  directives: [Alert]
                    template: "\n    \t\t  <label class=\"the-label\">Select Format:</label><BR>\n              &nbsp;&nbsp;&nbsp;<input type=\"radio\" (change)=\"handleFormatSelected($event)\" name=\"format\" value=\"HAPMAP\" checked=\"checked\">Hapmap<br>\n              &nbsp;&nbsp;&nbsp;<input type=\"radio\" (change)=\"handleFormatSelected($event)\" name=\"format\" value=\"FLAPJACK\">FlapJack<br>\n              &nbsp;&nbsp;&nbsp;<input type=\"radio\" (change)=\"handleFormatSelected($event)\" name=\"format\" value=\"META_DATA_ONLY\">Dataset Metadata Only<br>\n\t" // end template
                }),
                __metadata("design:paramtypes", [file_model_tree_service_1.FileModelTreeService])
            ], ExportFormatComponent);
            exports_1("ExportFormatComponent", ExportFormatComponent);
        }
    };
});
//# sourceMappingURL=export-format.component.js.map