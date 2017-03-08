System.register(["@angular/core", "../services/core/dto-request.service", "../services/app/dto-request-item-project", "../model/type-extractor-filter", "../model/type-entity", "../model/type-entity-filter", "../model/cv-filter-type"], function (exports_1, context_1) {
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
    var core_1, dto_request_service_1, dto_request_item_project_1, type_extractor_filter_1, type_entity_1, type_entity_filter_1, cv_filter_type_1, ProjectListBoxComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (dto_request_service_1_1) {
                dto_request_service_1 = dto_request_service_1_1;
            },
            function (dto_request_item_project_1_1) {
                dto_request_item_project_1 = dto_request_item_project_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (type_entity_filter_1_1) {
                type_entity_filter_1 = type_entity_filter_1_1;
            },
            function (cv_filter_type_1_1) {
                cv_filter_type_1 = cv_filter_type_1_1;
            }
        ],
        execute: function () {
            ProjectListBoxComponent = (function () {
                function ProjectListBoxComponent(_dtoRequestServiceProject) {
                    this._dtoRequestServiceProject = _dtoRequestServiceProject;
                    this.gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN;
                    // *** You cannot use an Enum directly as a template type parameter, so we need
                    //     to assign them to properties
                    this.entityTypeForTemplates = type_entity_1.EntityType;
                    this.entityFilterForTemplates = type_entity_filter_1.EntityFilter;
                    this.entitySubTypeForTemplates = type_entity_1.EntitySubType;
                    this.cvFilterTypeForTemplates = cv_filter_type_1.CvFilterType;
                    this.onProjectSelected = new core_1.EventEmitter();
                    this.onAddHeaderStatus = new core_1.EventEmitter();
                } // ctor
                ProjectListBoxComponent.prototype.handleProjectSelected = function (arg) {
                    var selectedProjectId = arg.id;
                    this.setProjectDetails(selectedProjectId);
                    this.onProjectSelected.emit(selectedProjectId);
                };
                ProjectListBoxComponent.prototype.handleHeaderStatus = function (arg) {
                    this.onAddHeaderStatus.emit(arg);
                };
                ProjectListBoxComponent.prototype.setProjectDetails = function (projectId) {
                    var scope$ = this;
                    this._dtoRequestServiceProject.get(new dto_request_item_project_1.DtoRequestItemProject(Number(projectId)))
                        .subscribe(function (projects) {
                        if (projects[0]) {
                            scope$.project = projects[0];
                            scope$.primaryInvestigatorId = String(projects[0].piContact);
                            scope$.setPiName();
                        }
                    }, function (headerStatusMessage) {
                        scope$.handleHeaderStatus(headerStatusMessage);
                    });
                };
                ProjectListBoxComponent.prototype.ngOnInit = function () {
                    var foo = "foo";
                };
                ProjectListBoxComponent.prototype.setPiName = function () {
                    var _this = this;
                    this.primaryInvestigatorName = undefined;
                    if (this.primaryInvestigatorId && this.nameIdListPIs) {
                        this.nameIdListPIs.forEach(function (n) {
                            if (n.id === _this.primaryInvestigatorId) {
                                _this.primaryInvestigatorName = n.name;
                            }
                        });
                    }
                };
                ProjectListBoxComponent.prototype.ngOnChanges = function (changes) {
                    if (changes['primaryInvestigatorId'] && changes['primaryInvestigatorId'].currentValue) {
                        this.primaryInvestigatorId = changes['primaryInvestigatorId'].currentValue;
                    }
                    if (changes['nameIdList']) {
                        if (changes['nameIdList'].currentValue) {
                            this.nameIdList = changes['nameIdList'].currentValue;
                            this.setProjectDetails(this.nameIdList[0].id);
                        }
                    }
                    if (changes['nameIdListPIs']) {
                        if (changes['nameIdListPIs'].currentValue) {
                            this.nameIdListPIs = changes['nameIdListPIs'].currentValue;
                        }
                    }
                    //
                };
                return ProjectListBoxComponent;
            }());
            ProjectListBoxComponent = __decorate([
                core_1.Component({
                    selector: 'project-list-box',
                    inputs: ['primaryInvestigatorId', 'nameIdList', 'nameIdListPIs', 'gobiiExtractFilterType'],
                    outputs: ['onProjectSelected', 'onAddHeaderStatus'],
                    template: "<name-id-list-box\n                    [gobiiExtractFilterType] = \"gobiiExtractFilterType\"\n                    [entityType]=\"entityTypeForTemplates.Projects\"\n                    [entityFilter] = \"entityFilterForTemplates.BYTYPEID\"\n                    [entityFilterValue] = \"primaryInvestigatorId\"\n                    (onNameIdSelected) = \"handleProjectSelected($event)\"\n                    (onError) = \"handleHeaderStatus($event)\">\n                </name-id-list-box>\n\t\t        \n                <div *ngIf=\"project\">\n                    <BR>\n                     <fieldset class=\"form-group\">\n                        <b>Name:</b> {{project.projectName}}<BR>\n                        <b>Description:</b> {{project.projectDescription}}<BR>\n                        <b>Principle Investigator:</b> {{primaryInvestigatorName}}\n                      </fieldset> \n                </div>\t\t        \n" // end template
                }),
                __metadata("design:paramtypes", [dto_request_service_1.DtoRequestService])
            ], ProjectListBoxComponent);
            exports_1("ProjectListBoxComponent", ProjectListBoxComponent);
        }
    };
});
//# sourceMappingURL=project-list-box.component.js.map