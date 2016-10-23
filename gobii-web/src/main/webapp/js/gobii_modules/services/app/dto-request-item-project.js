System.register(["@angular/core", "../../model/type-process"], function(exports_1, context_1) {
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
    var core_1, type_process_1;
    var DtoRequestItemProject;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            }],
        execute: function() {
            DtoRequestItemProject = (function () {
                function DtoRequestItemProject(projectId) {
                    this.projectId = projectId;
                    this.processType = type_process_1.ProcessType.READ;
                    this.projectId = projectId;
                }
                DtoRequestItemProject.prototype.getUrl = function () {
                    var baseUrl = "brapi/v1/projects";
                    var returnVal = baseUrl;
                    if (this.projectId) {
                        returnVal = baseUrl + "/" + this.projectId;
                    }
                    return returnVal;
                }; // getUrl()
                DtoRequestItemProject.prototype.getRequestBody = function () {
                    return JSON.stringify({
                        "processType": type_process_1.ProcessType[this.processType],
                        "projectId": this.projectId
                    });
                };
                DtoRequestItemProject.prototype.resultFromJson = function (json) {
                    var returnVal = undefined;
                    if (json.payload.data[0]) {
                        returnVal = json.payload.data[0];
                    }
                    // json.payload.data.forEach(item => {
                    //
                    //
                    //     returnVal.push(new Project(item.projectId,
                    //         item.projectName,
                    //         item.projectCode,
                    //         item.projectDescription,
                    //         item.piContact,
                    //         item.createdBy,
                    //         item.createdstring,
                    //         item.modifiedBy,
                    //         item.modifiedstring,
                    //         item.projectStatus
                    //     ));
                    // });
                    return returnVal;
                };
                DtoRequestItemProject = __decorate([
                    core_1.Injectable(), 
                    __metadata('design:paramtypes', [Number])
                ], DtoRequestItemProject);
                return DtoRequestItemProject;
            }());
            exports_1("DtoRequestItemProject", DtoRequestItemProject); // DtoRequestItemNameIds() 
        }
    }
});
//# sourceMappingURL=dto-request-item-project.js.map