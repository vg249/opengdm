System.register(["@angular/core", "../model/name-id", "../services/core/dto-request.service", "../model/type-entity", "../services/app/dto-request-item-nameids", "../model/type-entity-filter", "../model/file-item", "../model/type-process", "../services/core/file-model-tree-service", "../model/type-extractor-filter", "../model/file-model-node"], function (exports_1, context_1) {
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
    var core_1, name_id_1, dto_request_service_1, type_entity_1, dto_request_item_nameids_1, type_entity_filter_1, file_item_1, type_process_1, file_model_tree_service_1, type_extractor_filter_1, file_model_node_1, NameIdListBoxComponent;
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
            },
            function (file_item_1_1) {
                file_item_1 = file_item_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (file_model_tree_service_1_1) {
                file_model_tree_service_1 = file_model_tree_service_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (file_model_node_1_1) {
                file_model_node_1 = file_model_node_1_1;
            }
        ],
        execute: function () {
            NameIdListBoxComponent = (function () {
                //private uniqueId:string;
                function NameIdListBoxComponent(_dtoRequestService, _fileModelTreeService) {
                    //this.uniqueId  = Guid.generateUUID();
                    this._dtoRequestService = _dtoRequestService;
                    this._fileModelTreeService = _fileModelTreeService;
                    this.notifyOnInit = false;
                    // DtoRequestItemNameIds expects the value to be null if it's not set (not "UNKNOWN")
                    this.entityType = null;
                    this.entityFilter = null;
                    this.entityFilterValue = null;
                    this.entitySubType = null;
                    this.cvFilterType = null;
                    this.gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN;
                    this.selectedNameId = null;
                    this.onNameIdSelected = new core_1.EventEmitter();
                    this.onError = new core_1.EventEmitter();
                } // ctor
                NameIdListBoxComponent.prototype.ngOnInit = function () {
                    // entityFilterValue and entityFilter must either have values or be null.
                    if (this.isReadyForInit()) {
                        this.initializeNameIds();
                    }
                };
                NameIdListBoxComponent.prototype.isReadyForInit = function () {
                    var returnVal = false;
                    if (this.entityFilter === type_entity_filter_1.EntityFilter.NONE) {
                        this.entityFilter = null;
                        this.entityFilterValue = null;
                        returnVal = true;
                    }
                    else if (this.entityFilter === type_entity_filter_1.EntityFilter.BYTYPEID) {
                        //for filter BYTYPEID we must have a filter value specified by parent
                        returnVal = (this.entityFilterValue != null);
                    }
                    else if (this.entityFilter === type_entity_filter_1.EntityFilter.BYTYPENAME) {
                        //for filter BYTYPENAME we divine the typename algorityhmically for now
                        if (this.entityFilterValue = this.getEntityFilterValue(this.entityType, this.entitySubType)) {
                            returnVal = true;
                        }
                    }
                    return returnVal;
                };
                NameIdListBoxComponent.prototype.initializeNameIds = function () {
                    var _this = this;
                    var scope$ = this;
                    this._dtoRequestService.get(new dto_request_item_nameids_1.DtoRequestItemNameIds(this.entityType, this.entityFilter, this.entityFilterValue)).subscribe(function (nameIds) {
                        if (nameIds && (nameIds.length > 0)) {
                            scope$.nameIdList = nameIds;
                            scope$.selectedNameId = nameIds[0].id;
                            if (_this.notifyOnInit) {
                                _this.updateTreeService(nameIds[0]);
                            }
                        }
                        else {
                            scope$.nameIdList = [new name_id_1.NameId("0", "<none>", scope$.entityType)];
                        }
                    }, function (responseHeader) {
                        _this.handleResponseHeader(responseHeader);
                    });
                };
                NameIdListBoxComponent.prototype.handleResponseHeader = function (header) {
                    this.onError.emit(header);
                };
                NameIdListBoxComponent.prototype.updateTreeService = function (nameId) {
                    var _this = this;
                    this.onNameIdSelected.emit(nameId);
                    var fileItem = file_item_1.FileItem
                        .build(this.gobiiExtractFilterType, type_process_1.ProcessType.UPDATE)
                        .setEntityType(this.entityType)
                        .setEntitySubType(this.entitySubType)
                        .setItemId(nameId.id)
                        .setItemName(nameId.name);
                    this._fileModelTreeService.put(fileItem)
                        .subscribe(null, function (headerResponse) {
                        _this.handleResponseHeader(headerResponse);
                    });
                };
                NameIdListBoxComponent.prototype.handleNameIdSelected = function (arg) {
                    var nameId = this.nameIdList[arg.srcElement.selectedIndex];
                    // let nameId: NameId = new NameId(this.nameIdList[arg.srcElement.selectedIndex].id,
                    //     this.nameIdList[arg.srcElement.selectedIndex].name,
                    //     this.entityType);
                    this.updateTreeService(nameId);
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
                    var _this = this;
                    if (changes['gobiiExtractFilterType']
                        && (changes['gobiiExtractFilterType'].currentValue != null)
                        && (changes['gobiiExtractFilterType'].currentValue != undefined)) {
                        if (changes['gobiiExtractFilterType'].currentValue != changes['gobiiExtractFilterType'].previousValue) {
                            this._fileModelTreeService
                                .fileItemNotifications()
                                .subscribe(function (fileItem) {
                                if (fileItem.getProcessType() === type_process_1.ProcessType.NOTIFY
                                    && fileItem.getExtractorItemType() === file_model_node_1.ExtractorItemType.STATUS_DISPLAY_TREE_READY) {
                                    _this.initializeNameIds();
                                }
                            });
                        } // if we have a new filter type
                    } // if filter type changed
                    // we may have gotten a filterValue now so we init if we do
                    if (this.isReadyForInit()) {
                        this.initializeNameIds();
                    }
                }; // ngonChanges
                return NameIdListBoxComponent;
            }()); // class
            NameIdListBoxComponent = __decorate([
                core_1.Component({
                    selector: 'name-id-list-box',
                    inputs: ['gobiiExtractFilterType',
                        'notifyOnInit',
                        'entityType',
                        'entityFilter',
                        'entityFilterValue',
                        'entitySubType',
                        'cvFilterType'],
                    outputs: ['onNameIdSelected', 'onError'],
                    template: "<select name=\"users\" (change)=\"handleNameIdSelected($event)\" >\n\t\t\t<option *ngFor=\"let nameId of nameIdList \" \n\t\t\t\tvalue={{nameId.id}}>{{nameId.name}}</option>\n\t\t</select>\n" // end template
                }),
                __metadata("design:paramtypes", [dto_request_service_1.DtoRequestService,
                    file_model_tree_service_1.FileModelTreeService])
            ], NameIdListBoxComponent);
            exports_1("NameIdListBoxComponent", NameIdListBoxComponent);
        }
    };
});
//# sourceMappingURL=name-id-list-box.component.js.map