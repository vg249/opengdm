///<reference path="../../../../../../typings/index.d.ts"/>
System.register(["@angular/core", "@angular/http", "../views/export-format.component", "../services/app/principle-investigator.service", "../services/core/dto-request.service", "../services/core/authentication.service", "../views/contacts-list-box.component", "../views/project-list-box.component"], function(exports_1, context_1) {
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
    var core_1, http_1, export_format_component_1, principle_investigator_service_1, dto_request_service_1, authentication_service_1, contacts_list_box_component_1, project_list_box_component_1;
    var ExtractorRoot;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (http_1_1) {
                http_1 = http_1_1;
            },
            function (export_format_component_1_1) {
                export_format_component_1 = export_format_component_1_1;
            },
            function (principle_investigator_service_1_1) {
                principle_investigator_service_1 = principle_investigator_service_1_1;
            },
            function (dto_request_service_1_1) {
                dto_request_service_1 = dto_request_service_1_1;
            },
            function (authentication_service_1_1) {
                authentication_service_1 = authentication_service_1_1;
            },
            function (contacts_list_box_component_1_1) {
                contacts_list_box_component_1 = contacts_list_box_component_1_1;
            },
            function (project_list_box_component_1_1) {
                project_list_box_component_1 = project_list_box_component_1_1;
            }],
        execute: function() {
            // import { RouteConfig, ROUTER_DIRECTIVES, ROUTER_PROVIDERS } from 'angular2/router';
            // GOBii Imports
            ExtractorRoot = (function () {
                function ExtractorRoot() {
                    this.title = 'Gobii Web';
                    this.selectedContactId = "5";
                    var foo = "foo";
                }
                ExtractorRoot.prototype.handleContactSelected = function (arg) {
                    this.selectedContactId = arg;
                    //console.log("selected contact id:" + arg);
                };
                ExtractorRoot = __decorate([
                    core_1.Component({
                        selector: 'extractor-root',
                        directives: [export_format_component_1.ExportFormatComponent,
                            contacts_list_box_component_1.ContactsListBoxComponent,
                            project_list_box_component_1.ProjectListBoxComponent],
                        styleUrls: ['/extractor-ui.css'],
                        providers: [
                            http_1.HTTP_PROVIDERS,
                            authentication_service_1.AuthenticationService,
                            dto_request_service_1.DtoRequestService,
                            principle_investigator_service_1.PrincipleInvestigatorService
                        ],
                        template: "\n        <div class = \"panel panel-default\">\n        \n           <div class = \"panel-heading\">\n              <h1 class = \"panel-title\">GOBii Extractor</h1>\n           </div>\n           \n            <div class=\"container-fluid\">\n            \n                <div class=\"row\">\n                    <div class=\"col-md-3\"> \n                        <fieldset class=\"well the-fieldset\">\n                        <legend class=\"the-legend\">Principle Investigator</legend>\n                        <contacts-list-box (onContactSelected)=\"handleContactSelected($event)\"></contacts-list-box>\n                        </fieldset>\n                        <fieldset class=\"well the-fieldset\">\n                        <legend class=\"the-legend\">Projects</legend>\n                        <project-list-box [primaryInvestigatorId] = \"selectedContactId\"></project-list-box>\n                        </fieldset>\n                        \n                    </div>  <!-- outer grid column 1-->\n                    <div class=\"col-md-5\"> \n                            <div class=\"row\">\n                                <div class=\"col-md-12\">\t\t\t\t\t\t\t\n                                    <page-by-project></page-by-project>\n                                </div>\n                            </div> <!-- inner grid row 1 -->\n                            \n                            <div class=\"row\">\n                                <div class=\"col-md-12\">\n                                    <export-format></export-format>\n                                </div>\n                            </div> <!-- inner grid row 2 -->\n        \n                            <div class=\"row\">\n                                <div class=\"col-md-12\">\n                                    SEARCH BUTTON GOES HERE\n                                </div>\n                            </div> <!-- inner grid row 3 -->\n        \n        \n                         \n                    </div>  <!-- outer grid column 2 (inner grid)-->\n                    \n                    <div class=\"col-md-4\">\n                        FILTERS GO HERE\n                    </div>  <!-- outer grid column 3-->\n                    \n                </div> <!-- .row of outer grid -->\n                \n                    <div class=\"row\"><!-- begin .row 2 of outer grid-->\n                        <div class=\"col-md-3\"><!-- begin column 1 of outer grid -->\n                         \n                         </div><!-- end column 1 of outer grid -->\n                    \n                    </div><!-- end .row 2 of outer grid-->\n                \n            </div> \n\t" // end template
                    }), 
                    __metadata('design:paramtypes', [])
                ], ExtractorRoot);
                return ExtractorRoot;
            }());
            exports_1("ExtractorRoot", ExtractorRoot);
        }
    }
});
//# sourceMappingURL=app.extractorroot.js.map