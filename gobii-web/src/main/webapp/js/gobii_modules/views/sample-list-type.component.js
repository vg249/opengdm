System.register(["@angular/core", "../model/type-extractor-filter", "../model/type-extractor-sample-list", "../model/gobii-file-item", "../model/type-process", "../model//type-extractor-item", "../store/reducers", "@ngrx/store", "../services/core/file-item-service"], function (exports_1, context_1) {
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
    var core_1, type_extractor_filter_1, type_extractor_sample_list_1, gobii_file_item_1, type_process_1, type_extractor_item_1, fromRoot, store_1, file_item_service_1, SampleListTypeComponent;
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
            }
        ],
        execute: function () {
            SampleListTypeComponent = (function () {
                function SampleListTypeComponent(store, fileItemService) {
                    this.store = store;
                    this.fileItemService = fileItemService;
                    this.onHeaderStatusMessage = new core_1.EventEmitter();
                    this.gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN;
                    this.sampleListType$ = this.store.select(fromRoot.getSelectedSampleType);
                } // ctor
                SampleListTypeComponent.prototype.handleSampleTypeSelected = function (radioValue) {
                    var gobiiSampleListType = type_extractor_sample_list_1.GobiiSampleListType[radioValue];
                    this.fileItemService
                        .loadFileItem(gobii_file_item_1.GobiiFileItem.build(this.gobiiExtractFilterType, type_process_1.ProcessType.CREATE)
                        .setExtractorItemType(type_extractor_item_1.ExtractorItemType.SAMPLE_LIST_TYPE)
                        .setItemName(type_extractor_sample_list_1.GobiiSampleListType[gobiiSampleListType])
                        .setItemId(type_extractor_sample_list_1.GobiiSampleListType[gobiiSampleListType]), true);
                };
                SampleListTypeComponent.prototype.ngOnInit = function () {
                };
                SampleListTypeComponent.prototype.ngOnChanges = function (changes) {
                };
                SampleListTypeComponent = __decorate([
                    core_1.Component({
                        selector: 'sample-list-type',
                        inputs: ['gobiiExtractFilterType'],
                        outputs: ['onHeaderStatusMessage'],
                        template: "\n        <form>\n            <label class=\"the-legend\">List Item Type:&nbsp;</label>\n            <BR><input type=\"radio\"\n                       (ngModelChange)=\"handleSampleTypeSelected($event)\"\n                       [ngModel]=\"(sampleListType$ | async).getItemId()\"\n                       name=\"listType\"\n                       value=\"GERMPLASM_NAME\">\n            <label for=\"GERMPLASM_NAME\" class=\"the-legend\">Germplasm Name</label>\n            <BR><input type=\"radio\"\n                       (ngModelChange)=\"handleSampleTypeSelected($event)\"\n                       [ngModel]=\"(sampleListType$ | async).getItemId()\"\n                       name=\"listType\"\n                       value=\"EXTERNAL_CODE\">\n            <label for=\"EXTERNAL_CODE\" class=\"the-legend\">External Code</label>\n            <BR><input type=\"radio\"\n                       (ngModelChange)=\"handleSampleTypeSelected($event)\"\n                       [ngModel]=\"(sampleListType$ | async).getItemId()\"\n                       name=\"listType\"\n                       value=\"DNA_SAMPLE\">\n            <label for=\"DNA_SAMPLE\" class=\"the-legend\">DNA Sample</label>\n        </form>" // end template
                    }),
                    __metadata("design:paramtypes", [store_1.Store,
                        file_item_service_1.FileItemService])
                ], SampleListTypeComponent);
                return SampleListTypeComponent;
            }());
            exports_1("SampleListTypeComponent", SampleListTypeComponent);
        }
    };
});
//# sourceMappingURL=sample-list-type.component.js.map