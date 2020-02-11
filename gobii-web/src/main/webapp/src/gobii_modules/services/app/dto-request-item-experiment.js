System.register(["@angular/core", "../../model/type-process"], function (exports_1, context_1) {
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
    var core_1, type_process_1, DtoRequestItemExperiment;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            }
        ],
        execute: function () {
            DtoRequestItemExperiment = /** @class */ (function () {
                function DtoRequestItemExperiment(experimentId) {
                    this.experimentId = experimentId;
                    this.processType = type_process_1.ProcessType.READ;
                    this.experimentId = experimentId;
                }
                DtoRequestItemExperiment.prototype.getUrl = function () {
                    var baseUrl = "gobii/v1/experiments";
                    var returnVal = baseUrl;
                    if (this.experimentId) {
                        returnVal = baseUrl + "/" + this.experimentId;
                    }
                    return returnVal;
                }; // getUrl()
                DtoRequestItemExperiment.prototype.getRequestBody = function () {
                    return JSON.stringify({
                        "processType": type_process_1.ProcessType[this.processType],
                        "experimentId": this.experimentId
                    });
                };
                DtoRequestItemExperiment.prototype.resultFromJson = function (json) {
                    var returnVal = undefined;
                    if (json.payload.data[0]) {
                        returnVal = json.payload.data[0];
                    }
                    // json.payload.data.forEach(item => {
                    //
                    //     returnVal.push(new Experiment(item.experimentId,
                    //         item.experimentName,
                    //         item.experimentCode,
                    //         item.experimentDataFile,
                    //         item.projectId,
                    //         item.platformId,
                    //         item.manifestId,
                    //         item.createdBy,
                    //         item.createdstring,
                    //         item.modifiedBy,
                    //         item.modifiedstring,
                    //         item.status,
                    //         item.platformName
                    //     ));
                    // });
                    return returnVal;
                    //return [new NameId(1, 'foo'), new NameId(2, 'bar')];
                };
                DtoRequestItemExperiment = __decorate([
                    core_1.Injectable(),
                    __metadata("design:paramtypes", [Number])
                ], DtoRequestItemExperiment);
                return DtoRequestItemExperiment;
            }());
            exports_1("DtoRequestItemExperiment", DtoRequestItemExperiment);
        }
    };
});
//# sourceMappingURL=dto-request-item-experiment.js.map