System.register(["@angular/core", "@ngrx/store", "../store/actions/fileitem-action", "../store/actions/history-action", "../services/core/file-item-service"], function (exports_1, context_1) {
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
    var core_1, store_1, fileAction, historyAction, file_item_service_1, FlexQueryFilterComponent;
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
            },
            function (file_item_service_1_1) {
                file_item_service_1 = file_item_service_1_1;
            }
        ],
        execute: function () {
            FlexQueryFilterComponent = (function () {
                function FlexQueryFilterComponent(store, fileItemService) {
                    this.store = store;
                    this.fileItemService = fileItemService;
                    this.previousSelectedItemId = null;
                } // ctor
                FlexQueryFilterComponent.prototype.ngOnInit = function () {
                    var _this = this;
                    this.fileItemsEntityNames$ = this.fileItemService.getForFilter(this.filterParamNameEntities);
                    this.fileItemsEntityValues$ = this.fileItemService.getForFilter(this.filterParamNameEntityValues);
                    this
                        .fileItemsEntityNames$
                        .subscribe(function (items) {
                        if (_this.previousSelectedItemId === null && items && items.length > 0) {
                            _this.previousSelectedItemId = items[0].getFileItemUniqueId();
                        }
                    }, function (error) {
                        _this.store.dispatch(new historyAction.AddStatusMessageAction(error));
                    });
                };
                FlexQueryFilterComponent.prototype.handleFileItemSelected = function (arg) {
                    if (!this.gobiiExtractFilterType) {
                        this.store.dispatch(new historyAction.AddStatusMessageAction("The gobiiExtractFilterType property is not set"));
                    }
                    var newFileItemUniqueId = arg.currentTarget.value;
                    var previousFileItemUniqueId = this.previousSelectedItemId;
                    this.store.dispatch(new fileAction.ReplaceByItemIdAction({
                        filterParamName: this.filterParamNameEntities,
                        gobiiExtractFilterType: this.gobiiExtractFilterType,
                        itemIdCurrentlyInExtract: previousFileItemUniqueId,
                        itemIdToReplaceItWith: newFileItemUniqueId
                    }));
                    this.previousSelectedItemId = newFileItemUniqueId;
                };
                FlexQueryFilterComponent = __decorate([
                    core_1.Component({
                        selector: 'flex-query-filter',
                        inputs: ['gobiiExtractFilterType', 'filterParamNameEntities', 'filterParamNameEntityValues'],
                        outputs: [],
                        template: "\n        <div class=\"panel panel-primary\">\n            <div class=\"panel-heading\">\n                <h3 class=\"panel-title\">Filters</h3>\n            </div>\n            <div class=\"panel-body\">\n                <label class=\"the-label\">Entity:</label><BR>\n                <p-dropdown [options]=\"fileItemsEntityNames$ | async\"\n                            [(ngModel)]=\"selectedAllowableEntities\"\n                            optionLabel=\"_itemName\"></p-dropdown>\n\n                <BR>\n                <BR>\n                <label class=\"the-label\">Select Entity Values</label><BR>\n                <p-listbox [options]=\"fileItemsEntityValues$ | async\"\n                           [(ngModel)]=\"selectedEntityValues\" [style]=\"{'width':'200px'}\"\n                           optionLabel=\"_itemName\"></p-listbox>\n            </div>\n        </div>" // end template
                    }),
                    __metadata("design:paramtypes", [store_1.Store,
                        file_item_service_1.FileItemService])
                ], FlexQueryFilterComponent);
                return FlexQueryFilterComponent;
            }());
            exports_1("FlexQueryFilterComponent", FlexQueryFilterComponent);
        }
    };
});
//# sourceMappingURL=flex-query-filter.component.js.map