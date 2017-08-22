System.register(["@angular/core", "../model/type-process", "../model/gobii-file-item", "../model/type-extractor-filter", "../model/cv-filter-type", "../services/core/name-id-service", "../model/file-model-node", "../store/reducers", "../store/actions/fileitem-action", "@ngrx/store"], function (exports_1, context_1) {
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
    var core_1, type_process_1, gobii_file_item_1, type_extractor_filter_1, cv_filter_type_1, name_id_service_1, file_model_node_1, fromRoot, fileAction, store_1, CheckListBoxComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (gobii_file_item_1_1) {
                gobii_file_item_1 = gobii_file_item_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (cv_filter_type_1_1) {
                cv_filter_type_1 = cv_filter_type_1_1;
            },
            function (name_id_service_1_1) {
                name_id_service_1 = name_id_service_1_1;
            },
            function (file_model_node_1_1) {
                file_model_node_1 = file_model_node_1_1;
            },
            function (fromRoot_1) {
                fromRoot = fromRoot_1;
            },
            function (fileAction_1) {
                fileAction = fileAction_1;
            },
            function (store_1_1) {
                store_1 = store_1_1;
            }
        ],
        execute: function () {
            CheckListBoxComponent = (function () {
                function CheckListBoxComponent(store, 
                    //                private _fileModelTreeService: FileModelTreeService,
                    _nameIdService, differs) {
                    this.store = store;
                    this._nameIdService = _nameIdService;
                    this.differs = differs;
                    this.gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN;
                    this.onError = new core_1.EventEmitter();
                    this.differ = differs.find({}).create(null);
                    this.gobiiFileItems$ = this.store.select(fromRoot.getDatasets);
                } // ctor
                CheckListBoxComponent.prototype.handleItemChecked = function (arg) {
                    var itemToChange = arg;
                    //let indexOfItemToChange:number = this.gobiiFileItems.indexOf(arg.currentTarget.name);
                    itemToChange.setProcessType(arg.currentTarget.checked ? type_process_1.ProcessType.CREATE : type_process_1.ProcessType.DELETE);
                    itemToChange.setChecked(arg.currentTarget.checked);
                    if (itemToChange.getChecked()) {
                        this.store.dispatch(new fileAction.SelectForExtractAction(itemToChange));
                    }
                    else {
                        this.store.dispatch(new fileAction.DeSelectForExtractAction(itemToChange));
                    }
                }; // handleItemChecked()
                CheckListBoxComponent.prototype.handleItemSelected = function (arg) {
                    arg.currentTarget.style = "background-color:#b3d9ff";
                };
                CheckListBoxComponent.prototype.handleHeaderStatus = function (headerStatusMessage) {
                    this.onError.emit(headerStatusMessage);
                };
                CheckListBoxComponent.prototype.setList = function () {
                    var _this = this;
                    this._nameIdService.get(this.nameIdRequestParams)
                        .subscribe(function (nameIds) {
                        if (nameIds && (nameIds.length > 0)) {
                            var fileItems_1 = [];
                            nameIds.forEach(function (n) {
                                var currentFileItem = gobii_file_item_1.GobiiFileItem.build(_this.gobiiExtractFilterType, type_process_1.ProcessType.CREATE)
                                    .setExtractorItemType(file_model_node_1.ExtractorItemType.ENTITY)
                                    .setEntityType(_this.nameIdRequestParams.getEntityType())
                                    .setCvFilterType(cv_filter_type_1.CvFilterType.UNKNOWN)
                                    .setItemId(n.id)
                                    .setItemName(n.name)
                                    .setChecked(false)
                                    .setRequired(false);
                                fileItems_1.push(currentFileItem);
                            });
                            var loadAction = new fileAction.LoadAction(fileItems_1);
                            _this.store.dispatch(loadAction);
                        }
                    }, function (responseHeader) {
                        _this.handleHeaderStatus(responseHeader);
                    });
                }; // setList()
                CheckListBoxComponent.prototype.ngOnInit = function () {
                    // this._fileModelTreeService
                    //     .fileItemNotifications()
                    //     .subscribe(eventedFileItem => {
                    //
                    //             if (eventedFileItem) {
                    //                 let itemToChange: GobiiFileItem =
                    //                     this.gobiiFileItems.find(e => {
                    //                         return e.getEntityType() == eventedFileItem.getEntityType()
                    //                             && e.getItemName() == eventedFileItem.getItemName()
                    //                     });
                    //
                    //                 //let indexOfItemToChange:number = this.gobiiFileItems.indexOf(arg.currentTarget.name);
                    //                 if (itemToChange) {
                    //                     itemToChange.setProcessType(eventedFileItem.getProcessType());
                    //                     itemToChange.setChecked(eventedFileItem.getChecked());
                    //                     this.updateCheckedItemHistory(itemToChange);
                    //                 }
                    //             }
                    //         },
                    //         responseHeader => {
                    //             this.handleHeaderStatus(responseHeader);
                    //         });
                    if (this._nameIdService.validateRequest(this.nameIdRequestParams)) {
                        this.setList();
                    }
                };
                CheckListBoxComponent.prototype.resetList = function () {
                    if (this._nameIdService.validateRequest(this.nameIdRequestParams)) {
                        this.setList();
                    }
                };
                CheckListBoxComponent.prototype.ngOnChanges = function (changes) {
                    if (changes['gobiiExtractFilterType']
                        && (changes['gobiiExtractFilterType'].currentValue != null)
                        && (changes['gobiiExtractFilterType'].currentValue != undefined)) {
                        if (changes['gobiiExtractFilterType'].currentValue != changes['gobiiExtractFilterType'].previousValue) {
                            this.nameIdRequestParams.setGobiiExtractFilterType(this.gobiiExtractFilterType);
                            this.resetList();
                            // this.resetList();
                            // this._fileModelTreeService
                            //     .fileItemNotifications()
                            //     .subscribe(fileItem => {
                            //         if (fileItem.getProcessType() === ProcessType.NOTIFY
                            //             && fileItem.getExtractorItemType() === ExtractorItemType.STATUS_DISPLAY_TREE_READY) {
                            //
                            //             this.resetList();
                            //
                            //         }
                            //     });
                        } // if we have a new filter type
                    } // if filter type changed
                };
                CheckListBoxComponent.prototype.ngDoCheck = function () {
                    var changes = this.differ.diff(this.nameIdRequestParams);
                    if (changes) {
                        this.resetList();
                    }
                };
                CheckListBoxComponent = __decorate([
                    core_1.Component({
                        selector: 'checklist-box',
                        inputs: ['gobiiExtractFilterType',
                            'nameIdRequestParams',
                            'retainHistory'],
                        outputs: ['onError'],
                        template: "\n        <form>\n            <div style=\"overflow:auto; height: 80px; border: 1px solid #336699; padding-left: 5px\">\n                <div *ngFor=\"let gobiiFileItem of gobiiFileItems$ | async\"\n                     (click)=handleItemSelected($event)>\n                    <input type=\"checkbox\"\n                           (click)=handleItemChecked($event)\n                           [checked]=\"gobiiFileItem.getChecked()\"\n                           value={{gobiiFileItem.getItemId()}}\n                    name=\"{{gobiiFileItem.getItemName()}}\">&nbsp;{{gobiiFileItem.getItemName()}}\n                </div>\n            </div>\n        </form>" // end template
                    }),
                    __metadata("design:paramtypes", [store_1.Store,
                        name_id_service_1.NameIdService,
                        core_1.KeyValueDiffers])
                ], CheckListBoxComponent);
                return CheckListBoxComponent;
            }());
            exports_1("CheckListBoxComponent", CheckListBoxComponent);
        }
    };
});
//# sourceMappingURL=checklist-box.component.js.map