System.register(["@angular/core", "@ngrx/store", "../store/actions/fileitem-action", "../store/actions/history-action", "../services/core/file-item-service", "../services/core/view-id-generator-service"], function (exports_1, context_1) {
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
    var core_1, store_1, fileAction, historyAction, file_item_service_1, view_id_generator_service_1, NameIdListBoxComponent;
    var __moduleName = context_1 && context_1.id;
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
            },
            function (view_id_generator_service_1_1) {
                view_id_generator_service_1 = view_id_generator_service_1_1;
            }
        ],
        execute: function () {
            NameIdListBoxComponent = class NameIdListBoxComponent {
                constructor(store, fileItemService, viewIdGeneratorService) {
                    this.store = store;
                    this.fileItemService = fileItemService;
                    this.viewIdGeneratorService = viewIdGeneratorService;
                    this.controlId = "<NO-ID>";
                    this.previousSelectedItemId = null;
                } // ctor
                ngOnInit() {
                    this.controlId = this.viewIdGeneratorService.makeIdNameIdListBoxId(this.filterParamName);
                    this.fileItems$ = this.fileItemService.getForFilter(this.filterParamName);
                    this
                        .fileItems$
                        .subscribe(items => {
                        if (this.previousSelectedItemId === null && items && items.length > 0) {
                            this.previousSelectedItemId = items[0].getFileItemUniqueId();
                        }
                    }, error => {
                        this.store.dispatch(new historyAction.AddStatusMessageAction(error));
                    });
                }
                handleFileItemSelected(arg) {
                    if (!this.gobiiExtractFilterType) {
                        this.store.dispatch(new historyAction.AddStatusMessageAction("The gobiiExtractFilterType property is not set"));
                    }
                    let newFileItemUniqueId = arg.currentTarget.value;
                    let previousFileItemUniqueId = this.previousSelectedItemId;
                    this.store.dispatch(new fileAction.ReplaceByItemIdAction({
                        filterParamName: this.filterParamName,
                        gobiiExtractFilterType: this.gobiiExtractFilterType,
                        itemIdCurrentlyInExtract: previousFileItemUniqueId,
                        itemIdToReplaceItWith: newFileItemUniqueId
                    }));
                    this.previousSelectedItemId = newFileItemUniqueId;
                }
            }; // class
            NameIdListBoxComponent = __decorate([
                core_1.Component({
                    selector: 'name-id-list-box',
                    inputs: ['gobiiExtractFilterType', 'filterParamName'],
                    outputs: [],
                    template: `<select class="nameIdListBox" 
                       (change)="handleFileItemSelected($event)"
                       id="{{controlId}}">
        <option *ngFor="let fileItem of fileItems$ | async"
                [value]="fileItem.getFileItemUniqueId()"
                [selected]="fileItem.getSelected()"
                title="{{fileItem.getItemName()}}">
            {{fileItem.getItemName().length < 34 ? fileItem.getItemName() : fileItem.getItemName().substr(0,30).concat(" . . .")}}
            
        </option>
    </select>
    ` // end template
                }),
                __metadata("design:paramtypes", [store_1.Store,
                    file_item_service_1.FileItemService,
                    view_id_generator_service_1.ViewIdGeneratorService])
            ], NameIdListBoxComponent);
            exports_1("NameIdListBoxComponent", NameIdListBoxComponent);
        }
    };
});
//# sourceMappingURL=name-id-list-box.component.js.map