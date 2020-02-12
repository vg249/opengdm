System.register(["@angular/core", "./dto-request.service", "../../model/filter-type", "../../model/cv-filter-type", "../../model/type-entity", "rxjs", "../app/dto-request-item-nameids"], function (exports_1, context_1) {
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
    var core_1, dto_request_service_1, filter_type_1, cv_filter_type_1, type_entity_1, rxjs_1, dto_request_item_nameids_1, NameIdService;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (dto_request_service_1_1) {
                dto_request_service_1 = dto_request_service_1_1;
            },
            function (filter_type_1_1) {
                filter_type_1 = filter_type_1_1;
            },
            function (cv_filter_type_1_1) {
                cv_filter_type_1 = cv_filter_type_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (rxjs_1_1) {
                rxjs_1 = rxjs_1_1;
            },
            function (dto_request_item_nameids_1_1) {
                dto_request_item_nameids_1 = dto_request_item_nameids_1_1;
            }
        ],
        execute: function () {
            NameIdService = class NameIdService {
                constructor(_dtoRequestService) {
                    this._dtoRequestService = _dtoRequestService;
                } // ctor
                getEntityFilterValue(nameIdRequestParams) {
                    let returnVal = null;
                    if (nameIdRequestParams.getEntityType() === type_entity_1.EntityType.CONTACT) {
                        if (nameIdRequestParams.getEntitySubType() === type_entity_1.EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR) {
                            returnVal = "PI";
                        }
                    }
                    else if (nameIdRequestParams.getEntityType() === type_entity_1.EntityType.CV) {
                        if (nameIdRequestParams.getCvFilterType() != null && nameIdRequestParams.getCvFilterType() != cv_filter_type_1.CvFilterType.UNKNOWN) {
                            returnVal = cv_filter_type_1.CvFilters.get(cv_filter_type_1.CvFilterType.DATASET_TYPE);
                        }
                    }
                    return returnVal;
                }
                get(filterParams, relatedEntityFilterValue) {
                    return rxjs_1.Observable.create(observer => {
                        let filterType = filterParams.getFilterType() === filter_type_1.FilterType.NONE ? null : filterParams.getFilterType();
                        let cvFilterValue = null;
                        if (filterType === filter_type_1.FilterType.NAMES_BY_TYPEID) {
                            cvFilterValue = relatedEntityFilterValue;
                        }
                        else if (filterType === filter_type_1.FilterType.NAMES_BY_TYPE_NAME) {
                            cvFilterValue = filterParams.getCvFilterValue();
                        }
                        this._dtoRequestService.get(new dto_request_item_nameids_1.DtoRequestItemNameIds(filterParams.getEntityType(), filterType, cvFilterValue))
                            .subscribe(nameIds => {
                            let nameIdsToReturn = [];
                            if (nameIds && (nameIds.length > 0)) {
                                nameIdsToReturn = nameIds;
                            }
                            // else {
                            //     nameIdsToReturn = [new NameId("0", "<none>", nameIdRequestParams.getEntityType())];
                            // }
                            observer.next(nameIdsToReturn);
                            observer.complete();
                        }, responseHeader => {
                            responseHeader.status.statusMessages.forEach(headerStatusMessage => {
                                observer.error(headerStatusMessage);
                            });
                        });
                    });
                }
            };
            NameIdService = __decorate([
                core_1.Injectable(),
                __metadata("design:paramtypes", [dto_request_service_1.DtoRequestService])
            ], NameIdService);
            exports_1("NameIdService", NameIdService);
        }
    };
});
//# sourceMappingURL=name-id-service.js.map