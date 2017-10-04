System.register(["@angular/core", "../model/name-id", "@ngrx/store", "../store/reducers", "../store/actions/fileitem-action", "../model/type-process", "../model/file-model-node"], function (exports_1, context_1) {
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
    var core_1, name_id_1, store_1, fromRoot, fileAction, type_process_1, file_model_node_1, NameIdListBoxComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (name_id_1_1) {
                name_id_1 = name_id_1_1;
            },
            function (store_1_1) {
                store_1 = store_1_1;
            },
            function (fromRoot_1) {
                fromRoot = fromRoot_1;
            },
            function (fileAction_1) {
                fileAction = fileAction_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (file_model_node_1_1) {
                file_model_node_1 = file_model_node_1_1;
            }
        ],
        execute: function () {
            NameIdListBoxComponent = (function () {
                function NameIdListBoxComponent(store, differs) {
                    this.store = store;
                    this.differs = differs;
                    this.onNameIdSelected = new core_1.EventEmitter();
                    this.onError = new core_1.EventEmitter();
                    this.previousSelectedItemId = null;
                    this.differ = differs.find({}).create(null);
                } // ctor
                NameIdListBoxComponent.prototype.ngOnInit = function () {
                    //   this.fileItems$.subscribe( items => console.log("Items count: " + items.length));
                };
                NameIdListBoxComponent.prototype.handleHeaderStatus = function (headerStatusMessage) {
                    this.onError.emit(headerStatusMessage);
                };
                NameIdListBoxComponent.prototype.handleFileItemSelected = function (arg) {
                    var _this = this;
                    var currentFileItemUniqueId = arg.currentTarget.value;
                    this.store.select(fromRoot.getAllFileItems)
                        .subscribe(function (all) {
                        var selectedFileItem = all.find(function (fi) { return fi.getFileItemUniqueId() === currentFileItemUniqueId; });
                        if ((selectedFileItem.getProcessType() !== type_process_1.ProcessType.DUMMY)
                            && (selectedFileItem.getExtractorItemType() !== file_model_node_1.ExtractorItemType.LABEL)) {
                            _this.previousSelectedItemId = currentFileItemUniqueId;
                            _this.store.dispatch(new fileAction.AddToExtractAction(selectedFileItem));
                            _this.onNameIdSelected
                                .emit(new name_id_1.NameId(selectedFileItem.getItemId(), selectedFileItem.getItemName(), selectedFileItem.getEntityType()));
                        }
                        else {
                            if (_this.previousSelectedItemId) {
                                var previousItem = all.find(function (fi) { return fi.getFileItemUniqueId() === _this.previousSelectedItemId; });
                                _this.store.dispatch(new fileAction.RemoveFromExtractAction(previousItem));
                                _this.onNameIdSelected
                                    .emit(new name_id_1.NameId(previousItem.getItemId(), previousItem.getItemName(), previousItem.getEntityType()));
                            }
                        }
                    }).unsubscribe(); //unsubscribe or else this subscribe() keeps the state collection locked and the app freezes really badly
                };
                NameIdListBoxComponent.prototype.ngOnChanges = function (changes) {
                }; // ngonChanges
                NameIdListBoxComponent = __decorate([
                    core_1.Component({
                        selector: 'name-id-list-box',
                        inputs: ['fileItems$'],
                        outputs: ['onNameIdSelected', 'onError'],
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