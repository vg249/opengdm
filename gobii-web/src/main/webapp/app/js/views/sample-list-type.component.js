System.register(["@angular/core", "../model/type-extractor-filter", "../model/type-extractor-sample-list", "../model/gobii-file-item", "../model/type-process", "../model//type-extractor-item", "../store/reducers", "@ngrx/store", "../services/core/file-item-service", "../services/core/view-id-generator-service"], function (exports_1, context_1) {
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
    var core_1, type_extractor_filter_1, type_extractor_sample_list_1, gobii_file_item_1, type_process_1, type_extractor_item_1, fromRoot, store_1, file_item_service_1, view_id_generator_service_1, SampleListTypeComponent;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (type_extractor_sample_list_1_1) {
                type_extractor_sample_list_1 = type_extractor_sample_list_1_1;
            },
            function (gobii_file_item_1_1) {
                gobii_file_item_1 = gobii_file_item_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (type_extractor_item_1_1) {
                type_extractor_item_1 = type_extractor_item_1_1;
            },
            function (fromRoot_1) {
                fromRoot = fromRoot_1;
            },
            function (store_1_1) {
                store_1 = store_1_1;
            },
            function (file_item_service_1_1) {
                file_item_service_1 = file_item_service_1_1;
            },
            function (view_id_generator_service_1_1) {
                view_id_generator_service_1 = view_id_generator_service_1_1;
            }
        ],
        execute: function () {
            SampleListTypeComponent = class SampleListTypeComponent {
                constructor(store, fileItemService, viewIdGeneratorService) {
                    this.store = store;
                    this.fileItemService = fileItemService;
                    this.viewIdGeneratorService = viewIdGeneratorService;
                    this.gobiiSampleListType = type_extractor_sample_list_1.GobiiSampleListType;
                    this.onHeaderStatusMessage = new core_1.EventEmitter();
                    this.gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN;
                    this.sampleListType$ = this.store.select(fromRoot.getSelectedSampleType);
                } // ctor
                handleSampleTypeSelected(radioValue) {
                    let gobiiSampleListType = type_extractor_sample_list_1.GobiiSampleListType[radioValue];
                    this.fileItemService
                        .replaceFileItemByCompoundId(gobii_file_item_1.GobiiFileItem.build(this.gobiiExtractFilterType, type_process_1.ProcessType.CREATE)
                        .setExtractorItemType(type_extractor_item_1.ExtractorItemType.SAMPLE_LIST_TYPE)
                        .setItemName(type_extractor_sample_list_1.GobiiSampleListType[gobiiSampleListType])
                        .setItemId(type_extractor_sample_list_1.GobiiSampleListType[gobiiSampleListType]));
                }
                ngOnInit() {
                }
                ngOnChanges(changes) {
                }
            };
            SampleListTypeComponent = __decorate([
                core_1.Component({
                    selector: 'sample-list-type',
                    inputs: ['gobiiExtractFilterType'],
                    outputs: ['onHeaderStatusMessage'],
                    encapsulation: core_1.ViewEncapsulation.Emulated,
                    styleUrls: ["js/node_modules/primeng/resources/themes/omega/theme.css",
                        "js/node_modules/primeng/resources/primeng.css",
                        "js/node_modules/bootswatch/cerulean/bootstrap.min.css"],
                    template: `
        <form>
            <label class="the-legend">List Item Type:&nbsp;</label>
            <BR>
            <div class="ui-g" style="width:250px;margin-bottom:5px">
                <div class="ui-g-12" style="height:30px">
                    <p-radioButton
                            (ngModelChange)="handleSampleTypeSelected($event)"
                            [ngModel]="(sampleListType$ | async).getItemId()"
                            name="listType"
                            value="GERMPLASM_NAME"
                            label="Germplasm Name"
                            [id]="viewIdGeneratorService.makeSampleListTypeId(gobiiSampleListType.GERMPLASM_NAME)">
                    </p-radioButton>
                </div>
                <div class="ui-g-12" style="height:30px">
                    <p-radioButton
                            (ngModelChange)="handleSampleTypeSelected($event)"
                            [ngModel]="(sampleListType$ | async).getItemId()"
                            name="listType"
                            value="EXTERNAL_CODE"
                            label="External Code"
                            [id]="viewIdGeneratorService.makeSampleListTypeId(gobiiSampleListType.EXTERNAL_CODE)">
                    </p-radioButton>
                </div>
                <div class="ui-g-12" style="height:30px">
                    <p-radioButton
                            (ngModelChange)="handleSampleTypeSelected($event)"
                            [ngModel]="(sampleListType$ | async).getItemId()"
                            name="listType"
                            value="DNA_SAMPLE"
                            label="DNA Sample"
                            [id]="viewIdGeneratorService.makeSampleListTypeId(gobiiSampleListType.DNA_SAMPLE)">
                    </p-radioButton>
                </div>
            </div>
        </form>` // end template
                }),
                __metadata("design:paramtypes", [store_1.Store,
                    file_item_service_1.FileItemService,
                    view_id_generator_service_1.ViewIdGeneratorService])
            ], SampleListTypeComponent);
            exports_1("SampleListTypeComponent", SampleListTypeComponent);
        }
    };
});
//# sourceMappingURL=sample-list-type.component.js.map