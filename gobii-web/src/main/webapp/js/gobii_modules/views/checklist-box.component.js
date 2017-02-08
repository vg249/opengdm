System.register(["@angular/core", "../model/name-id", "../services/core/dto-request.service", "../model/type-process", "../model/event-checkbox"], function (exports_1, context_1) {
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
    var core_1, name_id_1, dto_request_service_1, type_process_1, event_checkbox_1, CheckListBoxComponent;
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
            function (event_checkbox_1_1) {
                event_checkbox_1 = event_checkbox_1_1;
            }
        ],
        execute: function () {
            CheckListBoxComponent = (function () {
                function CheckListBoxComponent(_dtoRequestServiceNameId) {
                    this._dtoRequestServiceNameId = _dtoRequestServiceNameId;
                    this.checkBoxEvents = [];
                    this.onItemChecked = new core_1.EventEmitter();
                    this.onItemSelected = new core_1.EventEmitter();
                    this.onAddMessage = new core_1.EventEmitter();
                } // ctor
                CheckListBoxComponent.prototype.handleItemChecked = function (arg) {
                    var itemToChange = this.checkBoxEvents.filter(function (e) {
                        return e.id == arg.currentTarget.value;
                    })[0];
                    //let indexOfItemToChange:number = this.checkBoxEvents.indexOf(arg.currentTarget.name);
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
                    var checkBoxEvent = new event_checkbox_1.CheckBoxEvent(type_process_1.ProcessType.READ, arg.currentTarget.children[0].value, arg.currentTarget.children[0].name, false);
                    this.onItemSelected.emit(checkBoxEvent);
                };
                CheckListBoxComponent.prototype.setList = function (nameIdList) {
                    // we can get this event whenver the item is clicked, not necessarily when the checkbox
                    var scope$ = this;
                    scope$.nameIdList = nameIdList;
                    if (scope$.nameIdList && (scope$.nameIdList.length > 0)) {
                        scope$.checkBoxEvents = [];
                        scope$.nameIdList.forEach(function (n) {
                            scope$.checkBoxEvents.push(new event_checkbox_1.CheckBoxEvent(type_process_1.ProcessType.CREATE, n.id, n.name, false));
                        });
                    }
                    else {
                        scope$.nameIdList = [new name_id_1.NameId(0, "<none>")];
                    }
                }; // setList()
                CheckListBoxComponent.prototype.ngOnInit = function () {
                };
                CheckListBoxComponent.prototype.ngOnChanges = function (changes) {
                    if (changes['checkBoxEventChange'] && changes['checkBoxEventChange'].currentValue) {
                        this.itemChangedEvent = changes['checkBoxEventChange'].currentValue;
                        if (this.itemChangedEvent) {
                            var itemToChange = this.checkBoxEvents.filter(function (e) {
                                return e.id == changes['checkBoxEventChange'].currentValue.id;
                            })[0];
                            //let indexOfItemToChange:number = this.checkBoxEvents.indexOf(arg.currentTarget.name);
                            if (itemToChange) {
                                itemToChange.processType = changes['checkBoxEventChange'].currentValue.processType;
                                itemToChange.checked = changes['checkBoxEventChange'].currentValue.checked;
                            }
                        }
                    }
                    else if (changes['nameIdList'] && changes['nameIdList'].currentValue) {
                        this.setList(changes['nameIdList'].currentValue);
                    }
                };
                return CheckListBoxComponent;
            }());
            CheckListBoxComponent = __decorate([
                core_1.Component({
                    selector: 'checklist-box',
                    inputs: ['checkBoxEventChange', 'nameIdList'],
                    outputs: ['onItemSelected', 'onItemChecked', 'onAddMessage'],
                    template: "<form>\n                    <div style=\"overflow:auto; height: 80px; border: 1px solid #336699; padding-left: 5px\">\n                        <div *ngFor=\"let checkBoxEvent of checkBoxEvents\" \n                            (click)=handleItemSelected($event) \n                            (hover)=handleItemHover($event)>\n                            <input  type=\"checkbox\" \n                                (click)=handleItemChecked($event)\n                                [checked]=\"checkBoxEvent.checked\"\n                                value={{checkBoxEvent.id}} \n                                name=\"{{checkBoxEvent.name}}\">&nbsp;{{checkBoxEvent.name}}\n                        </div>            \n                    </div>\n                </form>" // end template
                }),
                __metadata("design:paramtypes", [dto_request_service_1.DtoRequestService])
            ], CheckListBoxComponent);
            exports_1("CheckListBoxComponent", CheckListBoxComponent);
        }
    };
});
//# sourceMappingURL=checklist-box.component.js.map