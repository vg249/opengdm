System.register(["@angular/core", "../model/name-id", "../services/core/dto-request.service", "../services/app/dto-request-item-nameids", "../model/type-process", "../model/type-entity"], function(exports_1, context_1) {
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
    var core_1, name_id_1, dto_request_service_1, dto_request_item_nameids_1, type_process_1, type_entity_1;
    var DataSetCheckListBoxComponent;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (name_id_1_1) {
                name_id_1 = name_id_1_1;
            },
            function (dto_request_service_1_1) {
                dto_request_service_1 = dto_request_service_1_1;
            },
            function (dto_request_item_nameids_1_1) {
                dto_request_item_nameids_1 = dto_request_item_nameids_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            }],
        execute: function() {
            DataSetCheckListBoxComponent = (function () {
                function DataSetCheckListBoxComponent(_nameIdListService) {
                    this._nameIdListService = _nameIdListService;
                    this.onItemSelected = new core_1.EventEmitter();
                } // ctor
                DataSetCheckListBoxComponent.prototype.handleItemSelected = function (arg) {
                    var checked = arg.currentTarget.children[0].checked;
                    var dataSetId = arg.currentTarget.children[0].value;
                    var dataSetName = arg.currentTarget.children[0].name;
                    //this.onExperimentSelected.emit(this.nameIdList[arg.srcElement.selectedIndex].id);
                };
                DataSetCheckListBoxComponent.prototype.setList = function () {
                    var scope$ = this;
                    this._nameIdListService.getNameIds(new dto_request_item_nameids_1.DtoRequestItemNameIds(type_process_1.ProcessType.READ, type_entity_1.EntityType.DataSetNamesByExperimentId, this.experimentId)).subscribe(function (nameIds) {
                        if (nameIds && (nameIds.length > 0)) {
                            scope$.nameIdList = nameIds;
                        }
                        else {
                            scope$.nameIdList = [new name_id_1.NameId(0, "<none>")];
                        }
                    }, function (dtoHeaderResponse) {
                        dtoHeaderResponse.statusMessages.forEach(function (m) { return console.log(m.message); });
                    });
                }; // setList()
                DataSetCheckListBoxComponent.prototype.ngOnInit = function () {
                    this.setList();
                };
                DataSetCheckListBoxComponent.prototype.ngOnChanges = function (changes) {
                    this.experimentId = changes['experimentId'].currentValue;
                    this.setList();
                };
                DataSetCheckListBoxComponent = __decorate([
                    core_1.Component({
                        selector: 'dataset-checklist-box',
                        inputs: ['experimentId'],
                        template: "<form>\n                    <div style=\"overflow:auto; height: 80px; border: 1px solid #336699; padding-left: 5px\">\n \n                        <div *ngFor=\"let nameId of nameIdList\" (click)=handleItemSelected($event)>\n                            <input  type=\"checkbox\" \n                                \n                                value={{nameId.id}} \n                                name=\"{{nameId.name}}\">&nbsp;{{nameId.name}}\n                        </div>            \n     \n                    </div>        \n                </form>\n" // end template
                    }), 
                    __metadata('design:paramtypes', [dto_request_service_1.DtoRequestService])
                ], DataSetCheckListBoxComponent);
                return DataSetCheckListBoxComponent;
            }());
            exports_1("DataSetCheckListBoxComponent", DataSetCheckListBoxComponent);
        }
    }
});
//# sourceMappingURL=dataset-checklist-box.component.js.map