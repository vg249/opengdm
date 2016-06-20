System.register(["@angular/core", "../services/core/dto-request.service", "../services/app/dto-request-item-project"], function(exports_1, context_1) {
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
    var core_1, dto_request_service_1, dto_request_item_project_1;
    var ProjectListBoxComponent;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (dto_request_service_1_1) {
                dto_request_service_1 = dto_request_service_1_1;
            },
            function (dto_request_item_project_1_1) {
                dto_request_item_project_1 = dto_request_item_project_1_1;
            }],
        execute: function() {
            ProjectListBoxComponent = (function () {
                function ProjectListBoxComponent(_dtoRequestServiceNameId, _dtoRequestServiceProject) {
                    this._dtoRequestServiceNameId = _dtoRequestServiceNameId;
                    this._dtoRequestServiceProject = _dtoRequestServiceProject;
                    this.onProjectSelected = new core_1.EventEmitter();
                } // ctor
                ProjectListBoxComponent.prototype.handleProjectSelected = function (arg) {
                    var selectedProjectId = this.nameIdList[arg.srcElement.selectedIndex].id;
                    this.setProjectDetails(selectedProjectId);
                    this.onProjectSelected.emit(selectedProjectId);
                };
                ProjectListBoxComponent.prototype.setList = function () {
                    // let scope$ = this;
                    // this._dtoRequestServiceNameId.getResult(new DtoRequestItemNameIds(ProcessType.READ,
                    //     EntityType.Project,
                    //     this.primaryInvestigatorId)).subscribe(nameIds => {
                    //         if (nameIds && ( nameIds.length > 0 )) {
                    //             scope$.nameIdList = nameIds;
                    //             scope$.setProjectDetails(scope$.nameIdList[0].id);
                    //
                    //         } else {
                    //             scope$.nameIdList = [new NameId(0, "<none>")];
                    //         }
                    //     },
                    //     dtoHeaderResponse => {
                    //         dtoHeaderResponse.statusMessages.forEach(m => console.log(m.message))
                    //     });
                }; // setList()
                ProjectListBoxComponent.prototype.setProjectDetails = function (projectId) {
                    var scope$ = this;
                    this._dtoRequestServiceProject.getResult(new dto_request_item_project_1.DtoRequestItemProject(Number(projectId)))
                        .subscribe(function (project) {
                        if (project) {
                            scope$.project = project;
                        }
                    }, function (dtoHeaderResponse) {
                        dtoHeaderResponse.statusMessages.forEach(function (m) { return console.log(m.message); });
                    });
                };
                ProjectListBoxComponent.prototype.ngOnInit = function () {
                    //this.setList();
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
                    //        console.log('ngOnChanges - myProp = ' + changes['primaryInvestigatorId'].currentValue);
                };
                ProjectListBoxComponent = __decorate([
                    core_1.Component({
                        selector: 'project-list-box',
                        inputs: ['primaryInvestigatorId', 'nameIdList'],
                        outputs: ['onProjectSelected'],
                        template: "<select name=\"projects\" \n                    (change)=\"handleProjectSelected($event)\">\n                    <option *ngFor=\"let nameId of nameIdList \" \n                    value={{nameId.id}}>{{nameId.name}}</option>\n\t\t        </select>\n                <div *ngIf=\"project\">\n                    <BR>\n                     <fieldset class=\"form-group\">\n                        Name: {{project.projectName}}<BR>\n                        Description: {{project.projectDescription}}<BR>\n                      </fieldset> \n                </div>\t\t        \n" // end template
                    }), 
                    __metadata('design:paramtypes', [dto_request_service_1.DtoRequestService, dto_request_service_1.DtoRequestService])
                ], ProjectListBoxComponent);
                return ProjectListBoxComponent;
            }());
            exports_1("ProjectListBoxComponent", ProjectListBoxComponent);
        }
    }
});
//# sourceMappingURL=project-list-box.component.js.map