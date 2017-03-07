System.register(["@angular/core", "../model/name-id", "../services/core/dto-request.service", "../model/type-entity", "../services/app/dto-request-item-nameids", "../model/type-entity-filter"], function (exports_1, context_1) {
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
    var core_1, name_id_1, dto_request_service_1, type_entity_1, dto_request_item_nameids_1, type_entity_filter_1, NameIdListBoxComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (name_id_1_1) {
                name_id_1 = name_id_1_1;
            },
            function (dto_request_service_1_1) {
                dto_request_service_1 = dto_request_service_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (dto_request_item_nameids_1_1) {
                dto_request_item_nameids_1 = dto_request_item_nameids_1_1;
            },
            function (type_entity_filter_1_1) {
                type_entity_filter_1 = type_entity_filter_1_1;
            }
        ],
        execute: function () {
            NameIdListBoxComponent = (function () {
                function NameIdListBoxComponent(_dtoRequestService) {
                    this._dtoRequestService = _dtoRequestService;
                    // DtoRequestItemNameIds expects the value to be null if it's not set (not "UNKNOWN")
                    this.entityType = null;
                    this.entityFilter = null;
                    this.entityFilterValue = null;
                    this.entitySubType = null;
                    this.cvFilterType = null;
                    this.selectedNameId = null;
                    this.onNameIdSelected = new core_1.EventEmitter();
                } // ctor
                NameIdListBoxComponent.prototype.ngOnInit = function () {
                    var _this = this;
                    // entityFilterValue and entityFilter must either have values or be null.
                    if (this.entityFilter === type_entity_filter_1.EntityFilter.NONE) {
                        this.entityFilter = null;
                        this.entityFilterValue = null;
                    }
                    else if (this.entityFilter === null) {
                        this.entityFilterValue = null;
                    }
                    else {
                        this.entityFilterValue = this.getEntityFilterValue(this.entityType, this.entitySubType);
                    }
                    var scope$ = this;
                    this._dtoRequestService.get(new dto_request_item_nameids_1.DtoRequestItemNameIds(this.entityType, this.entityFilter, this.entityFilterValue)).subscribe(function (nameIds) {
                        if (nameIds && (nameIds.length > 0)) {
                            scope$.nameIdList = nameIds;
                            scope$.selectedNameId = nameIds[0].id;
                            _this.handleNameIdSelected(nameIds[0]);
                        }
                        else {
                            scope$.nameIdList = [new name_id_1.NameId("0", "ERROR NO " + type_entity_1.EntityType[scope$.entityType], scope$.entityType)];
                        }
                    }, function (dtoHeaderResponse) {
                        // dtoHeaderResponse.statusMessages.forEach(m => scope$.messages.push("Rettrieving nameIds: "
                        //     + m.message))
                    });
                };
                NameIdListBoxComponent.prototype.handleNameIdSelected = function (arg) {
                    var nameId = new name_id_1.NameId(this.nameIdList[arg.srcElement.selectedIndex].id, this.nameIdList[arg.srcElement.selectedIndex].name, this.entityType);
                    this.onNameIdSelected.emit(nameId);
                };
                NameIdListBoxComponent.prototype.getEntityFilterValue = function (entityType, entitySubType) {
                    var returnVal = null;
                    if (entityType === type_entity_1.EntityType.Contacts) {
                        if (entitySubType === type_entity_1.EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR) {
                            returnVal = "PI";
                        }
                    }
                    return returnVal;
                };
                NameIdListBoxComponent.prototype.ngOnChanges = function (changes) {
                };
                return NameIdListBoxComponent;
            }());
            NameIdListBoxComponent = __decorate([
                core_1.Component({
                    selector: 'name-id-list-box',
                    inputs: ['entityType', 'entityFilter', 'entitySubType', 'cvFilterType'],
                    outputs: ['onNameIdSelected'],
                    template: "<select name=\"users\" (change)=\"handleNameIdSelected($event)\" >\n\t\t\t<option *ngFor=\"let nameId of nameIdList \" \n\t\t\t\tvalue={{nameId.id}}>{{nameId.name}}</option>\n\t\t</select>\n" // end template
                }),
                __metadata("design:paramtypes", [dto_request_service_1.DtoRequestService])
            ], NameIdListBoxComponent);
            exports_1("NameIdListBoxComponent", NameIdListBoxComponent);
        }
    };
});
//# sourceMappingURL=name-id-list-box.component.js.map