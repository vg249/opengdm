System.register(["@angular/core", "@ngrx/store", "../store/actions/fileitem-action", "../store/actions/history-action"], function (exports_1, context_1) {
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
    var core_1, store_1, fileAction, historyAction, NameIdListBoxComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (store_1_1) {
                store_1 = store_1_1;
            },
            function (fileAction_1) {
                fileAction = fileAction_1;
            },
            function (historyAction_1) {
                historyAction = historyAction_1;
            }
        ],
        execute: function () {
            NameIdListBoxComponent = (function () {
                function NameIdListBoxComponent(store, differs) {
                    this.store = store;
                    this.differs = differs;
                    // private handleHeaderStatus(headerStatusMessage: HeaderStatusMessage) {
                    //
                    //     this.onError.emit(headerStatusMessage);
                    // }
                    this.previousSelectedItemId = null;
                    this.differ = differs.find({}).create(null);
                } // ctor
                NameIdListBoxComponent.prototype.ngOnInit = function () {
                    var _this = this;
                    //   this.fileItems$.subscribe( items => console.log("Items count: " + items.length));
                    this
                        .fileItems$
                        .subscribe(function (items) {
                        if (items && items.length > 0) {
                            _this.previousSelectedItemId = items[0].getFileItemUniqueId();
                        }
                    }, function (error) {
                        _this.store.dispatch(new historyAction.AddStatusMessageAction(error));
                    });
                };
                NameIdListBoxComponent.prototype.handleFileItemSelected = function (arg) {
                    var currentFileItemUniqueId = arg.currentTarget.value;
                    // this.store.select(fromRoot.getAllFileItems)
                    //     .subscribe(all => {
                    //         let selectedFileItem: GobiiFileItem = all.find(fi => fi.getFileItemUniqueId() === currentFileItemUniqueId);
                    //
                    if (this.previousSelectedItemId) {
                        //let previousItem: GobiiFileItem = all.find(fi => fi.getFileItemUniqueId() === this.previousSelectedItemId);
                        // this.store.dispatch(new fileAction.RemoveFromExtractAction(previousItem));
                    }
                    // if (( selectedFileItem.getProcessType() !== ProcessType.DUMMY )
                    //     && (selectedFileItem.getExtractorItemType() !== ExtractorItemType.LABEL)) {
                    //
                    //     //this.store.dispatch(new fileAction.AddToExtractAction(selectedFileItem));
                    //
                    //     this.onNameIdSelected
                    //         .emit(new NameId(selectedFileItem.getItemId(),
                    //             selectedFileItem.getItemName(),
                    //             selectedFileItem.getEntityType()));
                    //
                    // }
                    this.store.dispatch(new fileAction.ReplaceInExtractByItemIdAction({
                        gobiiExtractFilterType: this.gobiiExtractFilterType,
                        itemIdCurrentlyInExtract: this.previousSelectedItemId,
                        itemIdToReplaceItWith: currentFileItemUniqueId
                    }));
                    this.previousSelectedItemId = currentFileItemUniqueId;
                    //            }).unsubscribe(); //unsubscribe or else this subscribe() keeps the state collection locked and the app freezes really badly
                };
                NameIdListBoxComponent.prototype.ngOnChanges = function (changes) {
                }; // ngonChanges
                NameIdListBoxComponent = __decorate([
                    core_1.Component({
                        selector: 'name-id-list-box',
                        inputs: ['fileItems$', 'gobiiExtractFilterType'],
                        outputs: [],
                        template: "<select (change)=\"handleFileItemSelected($event)\">\n        <option *ngFor=\"let fileItem of fileItems$ | async\"\n                [value]=\"fileItem.getFileItemUniqueId()\"\n                [selected]=\"fileItem.getSelected()\">{{fileItem.getItemName()}}\n        </option>\n    </select>\n    " // end template
                    }),
                    __metadata("design:paramtypes", [store_1.Store,
                        core_1.KeyValueDiffers])
                ], NameIdListBoxComponent);
                return NameIdListBoxComponent;
            }());
            exports_1("NameIdListBoxComponent", NameIdListBoxComponent);
        }
    };
});
//# sourceMappingURL=name-id-list-box.component.js.map