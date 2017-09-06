System.register(["@angular/core", "../model/name-id", "../model/type-extractor-filter", "../services/core/name-id-service", "@ngrx/store", "../store/actions/fileitem-action", "../services/core/file-item-service"], function (exports_1, context_1) {
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
    var core_1, name_id_1, type_extractor_filter_1, name_id_service_1, store_1, fileAction, file_item_service_1, NameIdListBoxComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (name_id_1_1) {
                name_id_1 = name_id_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (name_id_service_1_1) {
                name_id_service_1 = name_id_service_1_1;
            },
            function (store_1_1) {
                store_1 = store_1_1;
            },
            function (fileAction_1) {
                fileAction = fileAction_1;
            },
            function (file_item_service_1_1) {
                file_item_service_1 = file_item_service_1_1;
            }
        ],
        execute: function () {
            NameIdListBoxComponent = (function () {
                function NameIdListBoxComponent(store, _nameIdService, fileItemService, 
                    //                private _fileModelTreeService: FileModelTreeService,
                    differs) {
                    this.store = store;
                    this._nameIdService = _nameIdService;
                    this.fileItemService = fileItemService;
                    this.differs = differs;
                    this.gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN;
                    this.notifyOnInit = false;
                    this.doTreeNotifications = true;
                    this.onNameIdSelected = new core_1.EventEmitter();
                    this.onError = new core_1.EventEmitter();
                    this.differ = differs.find({}).create(null);
                    //      this.fileItems$ = store.select(fromRoot.getAllFileItems);
                    //       this.fileItems$ = store.select(fromRoot.getMapsets);
                } // ctor
                // private notificationSent = false;
                NameIdListBoxComponent.prototype.ngOnInit = function () {
                };
                NameIdListBoxComponent.prototype.handleHeaderStatus = function (headerStatusMessage) {
                    this.onError.emit(headerStatusMessage);
                };
                NameIdListBoxComponent.prototype.handleFileItemSelected = function (arg) {
                    var currentFileItemUniqueId = arg.currentTarget.value;
                    var selectedFileItem = this.fileItemService.getFIleItemForUniqueFileItemId(currentFileItemUniqueId);
                    this.store.dispatch(new fileAction.SelectByFileItemUniqueId(currentFileItemUniqueId));
                    this.onNameIdSelected
                        .emit(new name_id_1.NameId(selectedFileItem.getItemId(), selectedFileItem.getItemName(), selectedFileItem.getEntityType()));
                    // if (this.currentSelection.getItemId() !== "0") {
                    //     this.currentSelection.setProcessType(ProcessType.DELETE);
                    //     this.updateTreeService(this.currentSelection);
                    // }
                    //        let gobiiFileItem: GobiiFileItem = this.fileItemList[arg.srcElement.selectedIndex]
                    //         let gobiiFileItem: GobiiFileItem = this.fileItemList.find(fi => {
                    //             return fi.getItemId() === this.selectedFileItemId
                    //         });
                    //
                    //         if (gobiiFileItem.getItemId() != "0") {
                    //             gobiiFileItem.setProcessType(ProcessType.UPDATE);
                    //             this.updateTreeService(gobiiFileItem);
                    //         }
                    //
                    //         this.currentSelection = gobiiFileItem;
                };
                NameIdListBoxComponent.prototype.ngOnChanges = function (changes) {
                    if (changes['gobiiExtractFilterType']
                        && (changes['gobiiExtractFilterType'].currentValue != null)
                        && (changes['gobiiExtractFilterType'].currentValue != undefined)) {
                        // this.fileItems$.subscribe(null, null, () => {
                        //
                        // });
                        if (changes['gobiiExtractFilterType'].currentValue != changes['gobiiExtractFilterType'].previousValue) {
                            //this.notificationSent = false;
                            //this.nameIdRequestParams.setGobiiExtractFilterType(this.gobiiExtractFilterType);
                            // let scope$ = this;
                            // this._fileModelTreeService
                            //     .fileItemNotifications()
                            //     .subscribe(fileItem => {
                            //         if (fileItem.getProcessType() === ProcessType.NOTIFY
                            //             && fileItem.getExtractorItemType() === ExtractorItemType.STATUS_DISPLAY_TREE_READY) {
                            //
                            //             scope$.initializeFileItems();
                            //
                            //
                            //         }
                            //     });
                        } // if we have a new filter type
                    } // if filter type changed
                    if (changes['nameIdRequestParams']
                        && (changes['nameIdRequestParams'].currentValue != null)
                        && (changes['nameIdRequestParams'].currentValue != undefined)) {
                    }
                }; // ngonChanges
                // angular change detection does not do deep comparison of objects that are
                // template properties. So we need to do some specialized change detection
                // references:
                //   https://juristr.com/blog/2016/04/angular2-change-detection/
                //   https://angular.io/docs/ts/latest/api/core/index/DoCheck-class.html
                //   http://blog.angular-university.io/how-does-angular-2-change-detection-really-work/
                //   https://blog.thoughtram.io/angular/2016/02/22/angular-2-change-detection-explained.html#what-causes-change
                NameIdListBoxComponent.prototype.ngDoCheck = function () {
                    var changes = this.differ.diff(this.nameIdRequestParams);
                    if (changes) {
                        if (this._nameIdService.validateRequest(this.nameIdRequestParams)) {
                            //this.initializeFileItems();
                        }
                    }
                };
                NameIdListBoxComponent = __decorate([
                    core_1.Component({
                        selector: 'name-id-list-box',
                        inputs: ['fileItems$',
                            'gobiiExtractFilterType',
                            'notifyOnInit',
                            'nameIdRequestParams',
                            'doTreeNotifications'],
                        outputs: ['onNameIdSelected', 'onError'],
                        template: "<select (change)=\"handleFileItemSelected($event)\">\n        <option *ngFor=\"let fileItem of fileItems$ | async\"\n                [value]=\"fileItem.getFileItemUniqueId()\">{{fileItem.getItemName()}}\n        </option>\n    </select>\n    " // end template
                    }),
                    __metadata("design:paramtypes", [store_1.Store,
                        name_id_service_1.NameIdService,
                        file_item_service_1.FileItemService,
                        core_1.KeyValueDiffers])
                ], NameIdListBoxComponent);
                return NameIdListBoxComponent;
            }());
            exports_1("NameIdListBoxComponent", NameIdListBoxComponent);
        }
    };
});
//# sourceMappingURL=name-id-list-box.component.js.map