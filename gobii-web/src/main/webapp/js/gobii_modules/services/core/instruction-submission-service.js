System.register(["@angular/core", "../../model/type-entity", "../../model/file-model-node", "../../model/type-extractor-filter", "../../store/reducers", "@ngrx/store", "rxjs/Observable"], function (exports_1, context_1) {
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
    var core_1, type_entity_1, file_model_node_1, type_extractor_filter_1, fromRoot, store_1, Observable_1, InstructionSubmissionService;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (file_model_node_1_1) {
                file_model_node_1 = file_model_node_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (fromRoot_1) {
                fromRoot = fromRoot_1;
            },
            function (store_1_1) {
                store_1 = store_1_1;
            },
            function (Observable_1_1) {
                Observable_1 = Observable_1_1;
            }
        ],
        execute: function () {
            InstructionSubmissionService = (function () {
                function InstructionSubmissionService(store) {
                    this.store = store;
                }
                InstructionSubmissionService.prototype.submitReady = function (scope$) {
                    var _this = this;
                    return Observable_1.Observable.create(function (observer) {
                        _this.store.select(fromRoot.getSelectedFileItems)
                            .subscribe(function (all) {
                            var submistReady = false;
                            if (scope$.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET) {
                                submistReady =
                                    all
                                        .filter(function (fi) {
                                        return fi.getGobiiExtractFilterType() === scope$.gobiiExtractFilterType
                                            && fi.getExtractorItemType() === file_model_node_1.ExtractorItemType.ENTITY
                                            && fi.getEntityType() === type_entity_1.EntityType.DataSets;
                                    })
                                        .length > 0;
                            } // if-else on extract type
                            var temp = "foo";
                            observer.next(submistReady);
                        }); // inner subscribe
                    } //observer lambda
                    ); // Observable.crate
                }; // function()
                InstructionSubmissionService = __decorate([
                    core_1.Injectable(),
                    __metadata("design:paramtypes", [store_1.Store])
                ], InstructionSubmissionService);
                return InstructionSubmissionService;
            }());
            exports_1("InstructionSubmissionService", InstructionSubmissionService);
        }
    };
});
//# sourceMappingURL=instruction-submission-service.js.map