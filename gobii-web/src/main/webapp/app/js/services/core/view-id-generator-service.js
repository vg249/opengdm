System.register(["./type-control", "@angular/core", "../../model/type-extract-format", "../../model/type-extractor-sample-list"], function (exports_1, context_1) {
    "use strict";
    var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
        var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
        if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
        else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
        return c > 3 && r && Object.defineProperty(target, key, r), r;
    };
    var type_control_1, core_1, type_extract_format_1, type_extractor_sample_list_1, ViewIdGeneratorService;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [
            function (type_control_1_1) {
                type_control_1 = type_control_1_1;
            },
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_extract_format_1_1) {
                type_extract_format_1 = type_extract_format_1_1;
            },
            function (type_extractor_sample_list_1_1) {
                type_extractor_sample_list_1 = type_extractor_sample_list_1_1;
            }
        ],
        execute: function () {
            ViewIdGeneratorService = class ViewIdGeneratorService {
                makeIdNameIdListBoxId(filterParamName) {
                    return type_control_1.TypeControl[type_control_1.TypeControl.NAME_ID_LIST] + "_" + filterParamName;
                }
                makeCheckboxListBoxId(filterParamName) {
                    return type_control_1.TypeControl[type_control_1.TypeControl.CHECKBOX_LIST] + "_" + filterParamName;
                }
                makeDatasetRowCheckboxId(datasetName) {
                    return type_control_1.TypeControl[type_control_1.TypeControl.DATASET_ROW_CHECKBOX] + "_" + datasetName;
                }
                makeExportFormatRadioButtonId(gobiiExtractFormat) {
                    return type_control_1.TypeControl[type_control_1.TypeControl.EXPORT_FORMAT_RADIO_BUTTON] + "_" + type_extract_format_1.GobiiExtractFormat[gobiiExtractFormat];
                } //makeExportFormatRadioButtonId()
                makeStandardId(typeControl) {
                    return type_control_1.TypeControl[typeControl];
                }
                makeSampleListTypeId(gobiiSampleListType) {
                    return type_control_1.TypeControl[type_control_1.TypeControl.SAMPLE_LIST_TYPE_RADIO_BUTTON] + "_" + type_extractor_sample_list_1.GobiiSampleListType[gobiiSampleListType];
                }
                makeMarkerSampleListModeRadioButtonId(typeLabel) {
                    let label = "unknown";
                    if (typeLabel) {
                        label = typeLabel.toUpperCase().replace(" ", "_");
                    }
                    return type_control_1.TypeControl[type_control_1.TypeControl.MARKER_SAMPLE_LIST_MODE_RADIO_BUTTON] + "_" + label;
                }
            };
            ViewIdGeneratorService = __decorate([
                core_1.Injectable()
            ], ViewIdGeneratorService);
            exports_1("ViewIdGeneratorService", ViewIdGeneratorService);
        }
    };
});
//# sourceMappingURL=view-id-generator-service.js.map