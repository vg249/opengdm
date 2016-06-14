System.register(["@angular/core", "../services/core/dto-request.service", "../services/app/dto-request-item-experiment"], function(exports_1, context_1) {
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
    var core_1, dto_request_service_1, dto_request_item_experiment_1;
    var ExperimentDetailBoxComponent;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (dto_request_service_1_1) {
                dto_request_service_1 = dto_request_service_1_1;
            },
            function (dto_request_item_experiment_1_1) {
                dto_request_item_experiment_1 = dto_request_item_experiment_1_1;
            }],
        execute: function() {
            ExperimentDetailBoxComponent = (function () {
                function ExperimentDetailBoxComponent(_dtoRequestService) {
                    this._dtoRequestService = _dtoRequestService;
                } // ctor
                ExperimentDetailBoxComponent.prototype.setExperiment = function () {
                    if (this.experimentId && this.experimentId > 0) {
                        var scope$_1 = this;
                        this._dtoRequestService.getResult(new dto_request_item_experiment_1.DtoRequestItemExperiment(this.experimentId)).subscribe(function (experiment) {
                            if (experiment) {
                                scope$_1.experiment = experiment;
                            }
                        }, function (dtoHeaderResponse) {
                            dtoHeaderResponse.statusMessages.forEach(function (m) { return console.log(m.message); });
                        });
                    } // if we have a legit experimentId
                }; // setList()
                ExperimentDetailBoxComponent.prototype.ngOnInit = function () {
                    this.setExperiment();
                };
                ExperimentDetailBoxComponent.prototype.ngOnChanges = function (changes) {
                    this.experimentId = changes['experimentId'].currentValue;
                    this.setExperiment();
                };
                ExperimentDetailBoxComponent = __decorate([
                    core_1.Component({
                        selector: 'experiment-detail-box',
                        inputs: ['experimentId', 'visible'],
                        template: " <div *ngIf=\"experiment\">\n                     <fieldset>\n                        Name: {{experiment.experimentName}}<BR>\n                        Code: {{experiment.experimentCode}}<BR>\n                        Data File: {{experiment.experimentDataFile}}<BR>\n                        Status: {{experiment.status}}<BR>\n                      </fieldset> \n                </div>" // end template
                    }), 
                    __metadata('design:paramtypes', [dto_request_service_1.DtoRequestService])
                ], ExperimentDetailBoxComponent);
                return ExperimentDetailBoxComponent;
            }());
            exports_1("ExperimentDetailBoxComponent", ExperimentDetailBoxComponent);
        }
    }
});
//# sourceMappingURL=experiment-detail-component.js.map