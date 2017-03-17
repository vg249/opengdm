System.register(["@angular/core", "../model/gobii-file-item", "../model/type-process", "../services/core/file-model-tree-service", "../model/type-extractor-filter", "../model/file-model-node", "../services/core/name-id-service"], function (exports_1, context_1) {
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
    var core_1, gobii_file_item_1, type_process_1, file_model_tree_service_1, type_extractor_filter_1, file_model_node_1, name_id_service_1, NameIdListBoxComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (gobii_file_item_1_1) {
                gobii_file_item_1 = gobii_file_item_1_1;
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
            },
            function (name_id_service_1_1) {
                name_id_service_1 = name_id_service_1_1;
            }
        ],
        execute: function () {
            NameIdListBoxComponent = (function () {
                function NameIdListBoxComponent(_nameIdService, _fileModelTreeService, differs) {
                    this._nameIdService = _nameIdService;
                    this._fileModelTreeService = _fileModelTreeService;
                    this.differs = differs;
                    this.notificationSent = false;
                    this.notifyOnInit = false;
                    this.gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN;
                    this.selectedNameId = null;
                    this.onNameIdSelected = new core_1.EventEmitter();
                    this.onError = new core_1.EventEmitter();
                    this.differ = differs.find({}).create(null);
                } // ctor
                NameIdListBoxComponent.prototype.ngOnInit = function () {
                    // entityFilterValue and entityFilter must either have values or be null.
                    // if (this._nameIdService.validateRequest(this.nameIdRequestParams) ) {
                    //     this.initializeNameIds();
                    // }
                };
                NameIdListBoxComponent.prototype.initializeNameIds = function () {
                    var _this = this;
                    var scope$ = this;
                    this._nameIdService.get(this.nameIdRequestParams)
                        .subscribe(function (nameIds) {
                        if (nameIds && (nameIds.length > 0)) {
                            scope$.nameIdList = nameIds;
                            scope$.selectedNameId = nameIds[0].id;
                            if (_this.notifyOnInit
                                && !_this.notificationSent
                                && scope$.nameIdList[0].name != "<none>") {
                                _this.updateTreeService(scope$.nameIdList[0]);
                                _this.notificationSent = true;
                            }
                        }
                    }, function (responseHeader) {
                        _this.handleHeaderStatus(responseHeader);
                    });
                };
                NameIdListBoxComponent.prototype.handleHeaderStatus = function (headerStatusMessage) {
                    this.onError.emit(headerStatusMessage);
                };
                NameIdListBoxComponent.prototype.updateTreeService = function (nameId) {
                    var _this = this;
                    this.onNameIdSelected.emit(nameId);
                    var fileItem = gobii_file_item_1.GobiiFileItem
                        .build(this.gobiiExtractFilterType, type_process_1.ProcessType.UPDATE)
                        .setExtractorItemType(file_model_node_1.ExtractorItemType.ENTITY)
                        .setEntityType(this.nameIdRequestParams.getEntityType())
                        .setEntitySubType(this.nameIdRequestParams.getEntitySubType())
                        .setCvFilterType(this.nameIdRequestParams.getCvFilterType())
                        .setItemId(nameId.id)
                        .setItemName(nameId.name);
                    this._fileModelTreeService.put(fileItem)
                        .subscribe(null, function (headerResponse) {
                        _this.handleHeaderStatus(headerResponse);
                    });
                };
                NameIdListBoxComponent.prototype.handleNameIdSelected = function (arg) {
                    var nameId = this.nameIdList[arg.srcElement.selectedIndex];
                    // let nameId: NameId = new NameId(this.nameIdList[arg.srcElement.selectedIndex].id,
                    //     this.nameIdList[arg.srcElement.selectedIndex].name,
                    //     this.entityType);
                    this.updateTreeService(nameId);
                };
                NameIdListBoxComponent.prototype.ngOnChanges = function (changes) {
                    if (changes['gobiiExtractFilterType']
                        && (changes['gobiiExtractFilterType'].currentValue != null)
                        && (changes['gobiiExtractFilterType'].currentValue != undefined)) {
                        if (changes['gobiiExtractFilterType'].currentValue != changes['gobiiExtractFilterType'].previousValue) {
                            this.notificationSent = false;
                            this.nameIdRequestParams.setGobiiExtractFilterType(this.gobiiExtractFilterType);
                            var scope$_1 = this;
                            this._fileModelTreeService
                                .fileItemNotifications()
                                .subscribe(function (fileItem) {
                                if (fileItem.getProcessType() === type_process_1.ProcessType.NOTIFY
                                    && fileItem.getExtractorItemType() === file_model_node_1.ExtractorItemType.STATUS_DISPLAY_TREE_READY) {
                                    scope$_1.initializeNameIds();
                                }
                            });
                        } // if we have a new filter type
                    } // if filter type changed
                    if (changes['nameIdRequestParams']
                        && (changes['nameIdRequestParams'].currentValue != null)
                        && (changes['nameIdRequestParams'].currentValue != undefined)) {
                    }
                    // we may have gotten a filterValue now so we init if we do
                    // if (this._nameIdService.validateRequest(this.nameIdRequestParams)) {
                    //     this.initializeNameIds();
                    // }
                }; // ngonChanges
                // angular change detection does not do deep comparison of objects that are
                // template properties. So we need to do some specialized change detection
                // references:
                //   https://juristr.com/blog/2016/04/angular2-change-detection/
                //   https://angular.io/docs/ts/latest/api/core/index/DoCheck-class.html
                //   http://blog.angular-university.io/how-does-angular-2-change-detection-really-work/
                //   https://blog.thoughtram.io/angular/2016/02/22/angular-2-change-detection-explained.html#what-causes-change
                NameIdListBoxComponent.prototype.ngDoCheck = function () {
                    var changes = this.differ.diff(this.nameIdRequestParams);
                    if (changes) {
                        if (this._nameIdService.validateRequest(this.nameIdRequestParams)) {
                            this.initializeNameIds();
                        }
                    }
                };
                return NameIdListBoxComponent;
            }()); // class
            NameIdListBoxComponent = __decorate([
                core_1.Component({
                    selector: 'name-id-list-box',
                    inputs: ['gobiiExtractFilterType',
                        'notifyOnInit',
                        'nameIdRequestParams'],
                    outputs: ['onNameIdSelected', 'onError'],
                    template: "<select name=\"users\" (change)=\"handleNameIdSelected($event)\" >\n\t\t\t<option *ngFor=\"let nameId of nameIdList \" \n\t\t\t\tvalue={{nameId.id}}>{{nameId.name}}</option>\n\t\t</select>\n" // end template
                }),
                __metadata("design:paramtypes", [name_id_service_1.NameIdService,
                    file_model_tree_service_1.FileModelTreeService,
                    core_1.KeyValueDiffers])
            ], NameIdListBoxComponent);
            exports_1("NameIdListBoxComponent", NameIdListBoxComponent);
        }
    };
});
//# sourceMappingURL=name-id-list-box.component.js.map