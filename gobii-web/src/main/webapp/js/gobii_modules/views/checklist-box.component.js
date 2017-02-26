System.register(["@angular/core", "../model/name-id", "../services/core/dto-request.service", "../model/type-process", "../model/file-item", "../model/type-entity"], function (exports_1, context_1) {
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
    var core_1, name_id_1, dto_request_service_1, type_process_1, file_item_1, type_entity_1, CheckListBoxComponent;
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
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (file_item_1_1) {
                file_item_1 = file_item_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            }
        ],
        execute: function () {
            CheckListBoxComponent = (function () {
                function CheckListBoxComponent(_dtoRequestServiceNameId) {
                    this._dtoRequestServiceNameId = _dtoRequestServiceNameId;
                    this.fileItemEvents = [];
                    this.entityType = type_entity_1.EntityType.UNKNOWN;
                    this.onItemChecked = new core_1.EventEmitter();
                    this.onItemSelected = new core_1.EventEmitter();
                    this.onAddMessage = new core_1.EventEmitter();
                } // ctor
                CheckListBoxComponent.prototype.handleItemChecked = function (arg) {
                    var itemToChange = this.fileItemEvents.filter(function (e) {
                        return e.itemId == arg.currentTarget.value;
                    })[0];
                    //let indexOfItemToChange:number = this.fileItemEvents.indexOf(arg.currentTarget.name);
                    itemToChange.processType = arg.currentTarget.checked ? type_process_1.ProcessType.CREATE : type_process_1.ProcessType.DELETE;
                    itemToChange.checked = arg.currentTarget.checked;
                    this.onItemChecked.emit(itemToChange);
                }; // handleItemChecked()
                CheckListBoxComponent.prototype.handleAddMessage = function (arg) {
                    this.onAddMessage.emit(arg);
                };
                CheckListBoxComponent.prototype.handleItemSelected = function (arg) {
                    if (this.previousSelectedItem) {
                        this.previousSelectedItem.style = "";
                    }
                    arg.currentTarget.style = "background-color:#b3d9ff";
                    this.previousSelectedItem = arg.currentTarget;
                    var fileItemEvent = new file_item_1.FileItem(type_process_1.ProcessType.READ, this.entityType, arg.currentTarget.children[0].value, arg.currentTarget.children[0].name, false, false);
                    this.onItemSelected.emit(fileItemEvent);
                };
                CheckListBoxComponent.prototype.setList = function (nameIdList) {
                    // we can get this event whenver the item is clicked, not necessarily when the checkbox
                    var scope$ = this;
                    scope$.nameIdList = nameIdList;
                    if (scope$.nameIdList && (scope$.nameIdList.length > 0)) {
                        scope$.entityType = scope$.nameIdList[0].entityType;
                        scope$.fileItemEvents = [];
                        scope$.nameIdList.forEach(function (n) {
                            scope$.fileItemEvents.push(new file_item_1.FileItem(type_process_1.ProcessType.CREATE, scope$.entityType, n.id, n.name, false, false));
                        });
                    }
                    else {
                        scope$.nameIdList = [new name_id_1.NameId("0", "<none>", this.entityType)];
                    }
                }; // setList()
                CheckListBoxComponent.prototype.ngOnInit = function () {
                };
                CheckListBoxComponent.prototype.ngOnChanges = function (changes) {
                    var stupid = "foo";
                    if (changes['fileItemEventChange'] && changes['fileItemEventChange'].currentValue) {
                        this.itemChangedEvent = changes['fileItemEventChange'].currentValue;
                        if (this.itemChangedEvent) {
                            var itemToChange = this.fileItemEvents.filter(function (e) {
                                return e.itemId == changes['fileItemEventChange'].currentValue.itemId;
                            })[0];
                            //let indexOfItemToChange:number = this.fileItemEvents.indexOf(arg.currentTarget.name);
                            if (itemToChange) {
                                itemToChange.processType = changes['fileItemEventChange'].currentValue.processType;
                                itemToChange.checked = changes['fileItemEventChange'].currentValue.checked;
                            }
                        }
                    }
                    else if (changes['nameIdList'] && changes['nameIdList'].currentValue) {
                        this.setList(changes['nameIdList'].currentValue);
                    }
                    else if (changes['entityType'] && changes['entityType'].currentValue) {
                        var enrityTypeString = changes['entityType'].currentValue;
                        this.entityType = type_entity_1.EntityType[enrityTypeString];
                    }
                };
                return CheckListBoxComponent;
            }());
            CheckListBoxComponent = __decorate([
                core_1.Component({
                    selector: 'checklist-box',
                    inputs: ['fileItemEventChange', 'nameIdList'],
                    outputs: ['onItemSelected', 'onItemChecked', 'onAddMessage'],
                    template: "<form>\n                    <div style=\"overflow:auto; height: 80px; border: 1px solid #336699; padding-left: 5px\">\n                        <div *ngFor=\"let fileItemEvent of fileItemEvents\" \n                            (click)=handleItemSelected($event) \n                            (hover)=handleItemHover($event)>\n                            <input  type=\"checkbox\" \n                                (click)=handleItemChecked($event)\n                                [checked]=\"fileItemEvent.checked\"\n                                value={{fileItemEvent.itemId}} \n                                name=\"{{fileItemEvent.itemName}}\">&nbsp;{{fileItemEvent.itemName}}\n                        </div>            \n                    </div>\n                </form>" // end template
                }),
                __metadata("design:paramtypes", [dto_request_service_1.DtoRequestService])
            ], CheckListBoxComponent);
            exports_1("CheckListBoxComponent", CheckListBoxComponent);
        }
    };
});
//# sourceMappingURL=checklist-box.component.js.map