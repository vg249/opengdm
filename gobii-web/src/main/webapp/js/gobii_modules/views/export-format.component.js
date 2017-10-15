System.register(["@angular/core", "../model/type-extract-format", "../model/gobii-file-item", "../model/type-process", "../model/file-model-node", "../model/type-extractor-filter", "@ngrx/store", "../services/core/file-item-service", "rxjs/Observable"], function (exports_1, context_1) {
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
    var core_1, type_extract_format_1, gobii_file_item_1, type_process_1, file_model_node_1, type_extractor_filter_1, store_1, file_item_service_1, Observable_1, ExportFormatComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_extract_format_1_1) {
                type_extract_format_1 = type_extract_format_1_1;
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
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (store_1_1) {
                store_1 = store_1_1;
            },
            function (file_item_service_1_1) {
                file_item_service_1 = file_item_service_1_1;
            },
            function (Observable_1_1) {
                Observable_1 = Observable_1_1;
            }
        ],
        execute: function () {
            ExportFormatComponent = (function () {
                function ExportFormatComponent(store, fileItemService) {
                    this.store = store;
                    this.fileItemService = fileItemService;
                    this.onFormatSelected = new core_1.EventEmitter();
                    this.onError = new core_1.EventEmitter();
                } // ctor
                // private nameIdList: NameId[];
                // private selectedNameId: string = null;
                ExportFormatComponent.prototype.ngOnInit = function () {
                    // in the current version, this doesn't work: each component in the page
                    // is initialized once at a time. Thus, even though the tree is being built
                    // built in the tree component's constructor, nginit() here still is triggered
                    // before the tree is instanced. Thus the format is not displayed in the tree because
                    // the tree isn't there yet to receive the event from the dataservice. ngInit() works
                    // to initialize the submitted as user in the tree is because it is calling a web service
                    // with an observer: while the service is being called, the rest of the control hierarchy is
                    // being built, so there is just enough enough latency in the web service call that the tree
                    // is there when the fileItem is posted to the tree model service. I have providen this
                    // because in the commented out code below, I make a call to the same webservice then
                    // post the GobiiFileItem for the format, and lo and behold, the tree is there and gets properly
                    // initialized with the format type. The only to make this is work is to post the format change
                    // to the model service _after_ the tree calls oncomplete. If we want to encapsulate all the
                    // service communication in the child components, the tree service will have to accommodate
                    // notification events to which these components will subscribe.
                    // let scope$ = this;
                    // this._dtoRequestService.get(new DtoRequestItemNameIds(
                    //     EntityType.Contacts,
                    //     null,
                    //     null)).subscribe(nameIds => {
                    //         if (nameIds && ( nameIds.length > 0 )) {
                    //             scope$.nameIdList = nameIds;
                    //             scope$.selectedNameId = nameIds[0].id;
                    //             this.updateTreeService(GobiiExtractFormat.HAPMAP);
                    //             //this.updateTreeService(nameIds[0]);
                    //         } else {
                    //             //scope$.nameIdList = [new NameId("0", "ERROR NO " + EntityType[scope$.entityType], scope$.entityType)];
                    //         }
                    //     },
                    //     responseHeader => {
                    //         this.handleResponseHeader(responseHeader);
                    //     });
                    // so, for now, this dispensation solves the problem. But I suspect it only works because the
                    // the tree component just happens to be the last one to be processed because it's at the end
                    // of the control tree, so it's the last one to get the property binding updates. If it were
                    // at the top of the control tree, we would have the reverse problem in that it would send out
                    // the of TREE_READY before the sibling components had been bound to their property values,
                    // and the component initialization would not work. perhaps. Tne work around for that would be that
                    // in this callback, we would check that the temlate-bound parameters had values; if they did not
                    // we would set a flag in this component saying, tree is ready; in ngInit, after the component properties
                    // are bound, we would check whether that flag is set, and if it was, then we would send
                    // the tree notification. I _think_ that would cover all the contingencies, but it's ugly.
                    // I am not sure whether reactive forms would address this issue.
                    //this.setDefault();
                };
                ExportFormatComponent.prototype.setDefault = function () {
                    //this.updateTreeService(GobiiExtractFormat.HAPMAP);
                    //this.fileFormat = "FLAPJACK";
                };
                ExportFormatComponent.prototype.handleResponseHeader = function (header) {
                    this.onError.emit(header);
                };
                ExportFormatComponent.prototype.handleFormatSelected = function (arg) {
                    this.updateTreeService(arg);
                };
                ExportFormatComponent.prototype.updateTreeService = function (arg) {
                    var formatItem = gobii_file_item_1.GobiiFileItem
                        .build(this.gobiiExtractFilterType, type_process_1.ProcessType.UPDATE)
                        .setExtractorItemType(file_model_node_1.ExtractorItemType.EXPORT_FORMAT)
                        .setItemId(arg)
                        .setItemName(type_extract_format_1.GobiiExtractFormat[type_extract_format_1.GobiiExtractFormat[arg]]);
                    this.fileItemService.loadFileItem(formatItem, true);
                    // this._fileModelTreeService.put(formatItem)
                    //     .subscribe(
                    //         null,
                    //         headerResponse => {
                    //             this.handleResponseHeader(headerResponse)
                    //         });
                    //
                    //console.log("selected contact itemId:" + arg);
                };
                ExportFormatComponent.prototype.ngOnChanges = function (changes) {
                    // this._fileModelTreeService
                    //     .fileItemNotifications()
                    //     .subscribe(fileItem => {
                    //
                    //         if (fileItem.getProcessType() === ProcessType.NOTIFY &&
                    //             ((fileItem.getExtractorItemType() === ExtractorItemType.STATUS_DISPLAY_TREE_READY)
                    //             || (fileItem.getExtractorItemType() === ExtractorItemType.CLEAR_TREE) )) {
                    //             this.setDefault();
                    //         }
                    //     });
                    if (changes['gobiiExtractFilterType']
                        && (changes['gobiiExtractFilterType'].currentValue != null)
                        && (changes['gobiiExtractFilterType'].currentValue != undefined)) {
                        if (changes['gobiiExtractFilterType'].currentValue != changes['gobiiExtractFilterType'].previousValue) {
                            var labelSuffix = " Metadata";
                            if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET) {
                                this.metaDataExtractname = "Dataset" + labelSuffix;
                            }
                            else if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER) {
                                this.metaDataExtractname = "Marker" + labelSuffix;
                            }
                            else if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE) {
                                this.metaDataExtractname = "Sample" + labelSuffix;
                            }
                            //                this.setDefault(); // dispatches new format
                        } // if we have a new filter type
                    } // if filter type changed
                    // if (changes['fileFormat$']
                    //     && ( changes['fileFormat$'].currentValue != null )
                    //     && ( changes['fileFormat$'].currentValue != undefined )) {
                    //
                    //     if (changes['fileFormat$'].currentValue != changes['gobiiExtractFilterType'].previousValue) {
                    //
                    //         console.log("Asynch:" + this.fileFormat$)
                    //
                    //     }
                    // }
                }; // ngonChanges
                __decorate([
                    core_1.Input(),
                    __metadata("design:type", Observable_1.Observable)
                ], ExportFormatComponent.prototype, "fileFormat$", void 0);
                ExportFormatComponent = __decorate([
                    core_1.Component({
                        selector: 'export-format',
                        outputs: ['onFormatSelected', 'onError'],
                        inputs: ['gobiiExtractFilterType'],
                        //directives: [RADIO_GROUP_DIRECTIVES]
                        //  directives: [Alert]
                        template: "\n        <form>\n            <label class=\"the-legend\">Select Format:&nbsp;</label>\n            <BR><input type=\"radio\"\n                       (ngModelChange)=\"handleFormatSelected($event)\"\n                       [ngModel]=\"(fileFormat$  | async).getItemId()\"\n                       name=\"fileFormat\"\n                       value=\"HAPMAP\">\n            <label for=\"HAPMAP\" class=\"the-legend\">Hapmap</label>\n            <BR><input type=\"radio\"\n                       (ngModelChange)=\"handleFormatSelected($event)\"\n                       [ngModel]=\"(fileFormat$  | async).getItemId()\"\n                       name=\"fileFormat\"\n                       value=\"FLAPJACK\">\n            <label for=\"FLAPJACK\" class=\"the-legend\">Flapjack</label>\n            <BR><input type=\"radio\"\n                       (ngModelChange)=\"handleFormatSelected($event)\"\n                       [ngModel]=\"(fileFormat$  | async).getItemId()\"\n                       name=\"fileFormat\"\n                       value=\"META_DATA_ONLY\">\n            <label for=\"META_DATA_ONLY\" class=\"the-legend\">{{metaDataExtractname}}</label>\n        </form>" // end template
                    })
                    /**
                     * In the template you will notice some slight of hand to get the property value of the
                     * GobiiFileItem for ngModel. In my original implementation, the selector from the reducer
                     * returned a scalar string value. From that selector I would get the initial state value
                     * but not subsequent values, even though the select itself was executing under the debugger.
                     * I spent many hours trying to track this issue down and even created the reproduce-radio
                     * project to try to isolate the issue. But it seemed to be working. Then I found ngrx/platform
                     * issue # 208: https://github.com/ngrx/platform/issues/208
                     * This is precisely the issue that was reported. My versions match those of the test project so I don't
                     * know why this is happening. I will try to reproduce the issue in the test project as a good citizen.
                     * In the meantime, we need to stick to returning actual state objects rather than scalar primtive values
                     * from it.
                     */
                    ,
                    __metadata("design:paramtypes", [store_1.Store,
                        file_item_service_1.FileItemService])
                ], ExportFormatComponent);
                return ExportFormatComponent;
            }());
            exports_1("ExportFormatComponent", ExportFormatComponent);
        }
    };
});
//# sourceMappingURL=export-format.component.js.map