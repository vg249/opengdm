System.register(["@angular/core", "../../model/type-process", "../../model/dataset"], function (exports_1, context_1) {
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
    var core_1, type_process_1, dataset_1, DataSetSearchType, DtoRequestItemDataSet;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (dataset_1_1) {
                dataset_1 = dataset_1_1;
            }
        ],
        execute: function () {
            (function (DataSetSearchType) {
                DataSetSearchType[DataSetSearchType["UNKNOWN"] = 0] = "UNKNOWN";
                DataSetSearchType[DataSetSearchType["LIST"] = 1] = "LIST";
                DataSetSearchType[DataSetSearchType["DETAIL"] = 2] = "DETAIL";
            })(DataSetSearchType || (DataSetSearchType = {}));
            exports_1("DataSetSearchType", DataSetSearchType);
            DtoRequestItemDataSet = (function () {
                function DtoRequestItemDataSet(dataSetSearchType, dataSetId) {
                    this.dataSetSearchType = dataSetSearchType;
                    this.dataSetId = dataSetId;
                    this.processType = type_process_1.ProcessType.READ;
                    this.dataSetSearchType = this.dataSetSearchType;
                    this.dataSetId = dataSetId;
                }
                DtoRequestItemDataSet.prototype.getUrl = function () {
                    var baseUrl = "gobii/v1/datasets";
                    var returnVal = baseUrl;
                    if (this.dataSetSearchType === DataSetSearchType.DETAIL)
                        if (this.dataSetId) {
                            returnVal = baseUrl + "/" + this.dataSetId;
                        }
                        else {
                            throw Error("Query type " + DataSetSearchType[this.dataSetSearchType] + " requires a datasetId");
                        }
                    return returnVal;
                }; // getUrl()
                DtoRequestItemDataSet.prototype.getRequestBody = function () {
                    return JSON.stringify({
                        "processType": type_process_1.ProcessType[this.processType],
                        "dataSetId": this.dataSetId
                    });
                };
                DtoRequestItemDataSet.prototype.resultFromJson = function (json) {
                    var returnVal = [];
                    if (this.dataSetSearchType === DataSetSearchType.DETAIL) {
                        if (json.payload.data[0]) {
                            returnVal.push(new dataset_1.DataSet(json.payload.data[0].dataSetId, json.payload.data[0].name, json.payload.data[0].experimentId, json.payload.data[0].callingAnalysisId, json.payload.data[0].dataTable, json.payload.data[0].dataFile, json.payload.data[0].qualityTable, json.payload.data[0].qualityFile, json.payload.data[0].status, json.payload.data[0].typeId, json.payload.data[0].analysesIds));
                        }
                    }
                    else if (this.dataSetSearchType === DataSetSearchType.LIST) {
                        json.payload.data[0].forEach(function (jsonItem) {
                            returnVal.push(new dataset_1.DataSet(json.payload.data[0].dataSetId, jsonItem.name, jsonItem.experimentId, jsonItem.callingAnalysisId, jsonItem.dataTable, jsonItem.dataFile, jsonItem.qualityTable, jsonItem.qualityFile, jsonItem.status, jsonItem.typeId, jsonItem.analysesIds));
                        });
                    }
                    else {
                        throw new Error("Unknown dataset search type: " + DataSetSearchType[this.dataSetSearchType]);
                    }
                    return returnVal;
                    //return [new NameId(1, 'foo'), new NameId(2, 'bar')];
                };
                DtoRequestItemDataSet = __decorate([
                    core_1.Injectable(),
                    __metadata("design:paramtypes", [Number, Number])
                ], DtoRequestItemDataSet);
                return DtoRequestItemDataSet;
            }());
            exports_1("DtoRequestItemDataSet", DtoRequestItemDataSet);
        }
    };
});
//# sourceMappingURL=dto-request-item-dataset.js.map