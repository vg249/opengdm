System.register(["@angular/core", "../model/type-process", "../model/type-extractor-filter", "../store/actions/fileitem-action", "@ngrx/store"], function (exports_1, context_1) {
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
    var core_1, type_process_1, type_extractor_filter_1, fileAction, store_1, CheckListBoxComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
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
                    differs) {
                    this.store = store;
                    this.differs = differs;
                    this.gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN;
                    this.onError = new core_1.EventEmitter();
                    this.differ = differs.find({}).create(null);
                    //this.gobiiFileItems$ = this.store.select(fromRoot.getDatasetsByExperiment);
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
                    // this._nameIdService.get(this.nameIdRequestParams)
                    //     .subscribe(nameIds => {
                    //             if (nameIds && ( nameIds.length > 0 )) {
                    //
                    //                 let fileItems: GobiiFileItem[] = [];
                    //
                    //                 nameIds.forEach(n => {
                    //                     let currentFileItem: GobiiFileItem =
                    //                         GobiiFileItem.build(
                    //                             this.gobiiExtractFilterType,
                    //                             ProcessType.CREATE)
                    //                             .setExtractorItemType(ExtractorItemType.ENTITY)
                    //                             .setEntityType(this.nameIdRequestParams.getEntityType())
                    //                             .setCvFilterType(CvFilterType.UNKNOWN)
                    //                             .setItemId(n.id)
                    //                             .setItemName(n.name)
                    //                             .setChecked(false)
                    //                             .setRequired(false);
                    //
                    //
                    //                     fileItems.push(currentFileItem);
                    //                 });
                    //
                    //                 let loadAction: fileAction.LoadAction = new fileAction.LoadAction(fileItems);
                    //                 this.store.dispatch(loadAction)
                    //
                    //             }
                    //         },
                    //         responseHeader => {
                    //             this.handleHeaderStatus(responseHeader);
                    //         });
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
                    // if (this._nameIdService.validateRequest(this.nameIdRequestParams)) {
                    //     this.setList()
                    // }
                };
                CheckListBoxComponent.prototype.resetList = function () {
                    // if (this._nameIdService.validateRequest(this.nameIdRequestParams)) {
                    //     this.setList();
                    // }
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
                            'gobiiFileItems$',
                            'nameIdRequestParams',
                            'retainHistory'],
                        outputs: ['onError'],
                        template: "\n        <form>\n            <div style=\"overflow:auto; height: 80px; border: 1px solid #336699; padding-left: 5px\">\n                <div *ngFor=\"let gobiiFileItem of gobiiFileItems$ | async\"\n                     (click)=handleItemSelected($event)>\n                    <input type=\"checkbox\"\n                           (click)=handleItemChecked($event)\n                           [checked]=\"gobiiFileItem.getChecked()\"\n                           value={{gobiiFileItem.getItemId()}}\n                    name=\"{{gobiiFileItem.getItemName()}}\">&nbsp;{{gobiiFileItem.getItemName()}}\n                </div>\n            </div>\n        </form>" // end template
                    }),
                    __metadata("design:paramtypes", [store_1.Store,
                        core_1.KeyValueDiffers])
                ], CheckListBoxComponent);
                return CheckListBoxComponent;
            }());
            exports_1("CheckListBoxComponent", CheckListBoxComponent);
        }
    };
});
//# sourceMappingURL=checklist-box.component.js.map