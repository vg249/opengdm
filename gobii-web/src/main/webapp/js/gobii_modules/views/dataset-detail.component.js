System.register(["@angular/core", "../services/core/dto-request.service", "../services/app/dto-request-item-dataset"], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
        var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
        if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
        else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
        return c > 3 && r && Object.defineProperty(target, key, r), r;
    };
    var __metadata = (this && this.__metadata) || function (k, v) {
        if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
    };
    var core_1, dto_request_service_1, dto_request_item_dataset_1;
    var DatasetDetailBoxComponent;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (dto_request_service_1_1) {
                dto_request_service_1 = dto_request_service_1_1;
            },
            function (dto_request_item_dataset_1_1) {
                dto_request_item_dataset_1 = dto_request_item_dataset_1_1;
            }],
        execute: function() {
            DatasetDetailBoxComponent = (function () {
                function DatasetDetailBoxComponent(_dtoRequestService) {
                    this._dtoRequestService = _dtoRequestService;
                } // ctor
                DatasetDetailBoxComponent.prototype.setDataset = function () {
                    if (this.dataSetId && this.dataSetId > 0) {
                        var scope$_1 = this;
                        this._dtoRequestService.getResult(new dto_request_item_dataset_1.DtoRequestItemDataSet(this.dataSetId)).subscribe(function (dataSet) {
                            if (dataSet) {
                                scope$_1.dataSet = dataSet;
                            }
                        }, function (dtoHeaderResponse) {
                            dtoHeaderResponse.statusMessages.forEach(function (m) { return console.log(m.message); });
                        });
                    } // if we have a legit dataSetId
                }; // setList()
                DatasetDetailBoxComponent.prototype.ngOnInit = function () {
                    this.setDataset();
                };
                DatasetDetailBoxComponent.prototype.ngOnChanges = function (changes) {
                    this.dataSetId = changes['dataSetId'].currentValue;
                    this.setDataset();
                };
                DatasetDetailBoxComponent = __decorate([
                    core_1.Component({
                        selector: 'dataset-detail-box',
                        inputs: ['dataSetId', 'visible'],
                        template: " <div *ngIf=\"dataSet\">\n                     <fieldset>\n                        Name: {{dataSet.name}}<BR>\n                        Data Table: {{dataSet.dataTable}}<BR>\n                        Data File: {{dataSet.dataFile}}<BR>\n                        Quality Table: {{dataSet.qualityTable}}<BR>\n                        Quality File: {{dataSet.qualityFile}}<BR>\n                      </fieldset> \n                </div>" // end template
                    }), 
                    __metadata('design:paramtypes', [dto_request_service_1.DtoRequestService])
                ], DatasetDetailBoxComponent);
                return DatasetDetailBoxComponent;
            }());
            exports_1("DatasetDetailBoxComponent", DatasetDetailBoxComponent);
        }
    }
});
//# sourceMappingURL=dataset-detail.component.js.map