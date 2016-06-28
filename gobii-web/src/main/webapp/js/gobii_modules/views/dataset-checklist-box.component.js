System.register(["@angular/core", "../model/name-id", "../services/core/dto-request.service", "../services/app/dto-request-item-nameids", "../model/type-process", "../model/type-entity", "../model/event-checkbox", "../services/app/dto-request-item-dataset"], function(exports_1, context_1) {
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
    var core_1, name_id_1, dto_request_service_1, dto_request_item_nameids_1, type_process_1, type_entity_1, event_checkbox_1, dto_request_item_dataset_1;
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
            },
            function (event_checkbox_1_1) {
                event_checkbox_1 = event_checkbox_1_1;
            },
            function (dto_request_item_dataset_1_1) {
                dto_request_item_dataset_1 = dto_request_item_dataset_1_1;
            }],
        execute: function() {
            DataSetCheckListBoxComponent = (function () {
                function DataSetCheckListBoxComponent(_dtoRequestServiceNameId, _dtoRequestServiceDataSetDetail) {
                    this._dtoRequestServiceNameId = _dtoRequestServiceNameId;
                    this._dtoRequestServiceDataSetDetail = _dtoRequestServiceDataSetDetail;
                    this.onItemChecked = new core_1.EventEmitter();
                    this.onItemSelected = new core_1.EventEmitter();
                    this.onAddMessage = new core_1.EventEmitter();
                } // ctor
                DataSetCheckListBoxComponent.prototype.handleItemChecked = function (arg) {
                    var checkEvent = new event_checkbox_1.CheckBoxEvent(arg.currentTarget.checked ? type_process_1.ProcessType.CREATE : type_process_1.ProcessType.DELETE, arg.currentTarget.value, arg.currentTarget.name);
                    this.onItemChecked.emit(checkEvent);
                };
                DataSetCheckListBoxComponent.prototype.handleAddMessage = function (arg) {
                    this.onAddMessage.emit(arg);
                };
                DataSetCheckListBoxComponent.prototype.handleItemSelected = function (arg) {
                    var selectedDataSetId = Number(arg.currentTarget.children[0].value);
                    if (this.previousSelectedItem) {
                        this.previousSelectedItem.style = "";
                    }
                    arg.currentTarget.style = "background-color:#b3d9ff";
                    this.previousSelectedItem = arg.currentTarget;
                    this.setDatasetDetails(selectedDataSetId);
                    this.onItemSelected.emit(selectedDataSetId);
                };
                DataSetCheckListBoxComponent.prototype.setList = function () {
                    // we can get this event whenver the item is clicked, not necessarily when the checkbox
                    var scope$ = this;
                    this._dtoRequestServiceNameId.getResult(new dto_request_item_nameids_1.DtoRequestItemNameIds(type_process_1.ProcessType.READ, type_entity_1.EntityType.DataSetNamesByExperimentId, this.experimentId)).subscribe(function (nameIds) {
                        if (nameIds && (nameIds.length > 0)) {
                            scope$.nameIdList = nameIds;
                            scope$.setDatasetDetails(scope$.nameIdList[0].id);
                        }
                        else {
                            scope$.nameIdList = [new name_id_1.NameId(0, "<none>")];
                        }
                    }, function (dtoHeaderResponse) {
                        dtoHeaderResponse.statusMessages.forEach(function (m) { return scope$.handleAddMessage("Retrieving dataset names by experiment id: "
                            + ": "
                            + m.message); });
                    });
                }; // setList()
                DataSetCheckListBoxComponent.prototype.setDatasetDetails = function (dataSetId) {
                    var scope$ = this;
                    scope$._dtoRequestServiceDataSetDetail.getResult(new dto_request_item_dataset_1.DtoRequestItemDataSet(dataSetId)).subscribe(function (dataSet) {
                        if (dataSet) {
                            scope$.dataSet = dataSet;
                        }
                    }, function (dtoHeaderResponse) {
                        dtoHeaderResponse.statusMessages.forEach(function (m) { return scope$.handleAddMessage(m.message); });
                    });
                }; // setList()
                DataSetCheckListBoxComponent.prototype.ngOnInit = function () {
                    if (this.experimentId) {
                        this.setList();
                    }
                };
                DataSetCheckListBoxComponent.prototype.ngOnChanges = function (changes) {
                    this.experimentId = changes['experimentId'].currentValue;
                    if (this.experimentId) {
                        this.setList();
                    }
                };
                DataSetCheckListBoxComponent = __decorate([
                    core_1.Component({
                        selector: 'dataset-checklist-box',
                        inputs: ['experimentId'],
                        outputs: ['onItemChecked', 'onItemSelected', 'onAddMessage'],
                        template: "<form>\n                    <div style=\"overflow:auto; height: 80px; border: 1px solid #336699; padding-left: 5px\">\n                        <div *ngFor=\"let nameId of nameIdList\" \n                            (click)=handleItemSelected($event) \n                            (hover)=handleItemHover($event)>\n                            <input  type=\"checkbox\" \n                                (click)=handleItemChecked($event)\n                                value={{nameId.id}} \n                                name=\"{{nameId.name}}\">&nbsp;{{nameId.name}}\n                        </div>            \n                    </div>\n                </form>\n                <div *ngIf=\"dataSet\">\n                    <BR>\n                     <fieldset>\n                        Name: {{dataSet.name}}<BR>\n                        Data Table: {{dataSet.dataTable}}<BR>\n                        Data File: {{dataSet.dataFile}}<BR>\n                        Quality Table: {{dataSet.qualityTable}}<BR>\n                        Quality File: {{dataSet.qualityFile}}<BR>\n                      </fieldset> \n                </div>                \n" // end template
                    }), 
                    __metadata('design:paramtypes', [dto_request_service_1.DtoRequestService, dto_request_service_1.DtoRequestService])
                ], DataSetCheckListBoxComponent);
                return DataSetCheckListBoxComponent;
            }());
            exports_1("DataSetCheckListBoxComponent", DataSetCheckListBoxComponent);
        }
    }
});
//# sourceMappingURL=dataset-checklist-box.component.js.map