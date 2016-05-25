///<reference path="../../../../../../node_modules/angular2/typings/browser.d.ts"/>
System.register(['angular2/core', 'angular2/http', '../views/export-format.component', './page-by-samples.component', './page-by-project.component', '../services/app/principle-investigator.service', '../services/app/name-id-list.service'], function(exports_1, context_1) {
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
    var core_1, http_1, export_format_component_1, page_by_samples_component_1, page_by_project_component_1, principle_investigator_service_1, name_id_list_service_1;
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
            function (page_by_samples_component_1_1) {
                page_by_samples_component_1 = page_by_samples_component_1_1;
            },
            function (page_by_project_component_1_1) {
                page_by_project_component_1 = page_by_project_component_1_1;
            },
            function (principle_investigator_service_1_1) {
                principle_investigator_service_1 = principle_investigator_service_1_1;
            },
            function (name_id_list_service_1_1) {
                name_id_list_service_1 = name_id_list_service_1_1;
            }],
        execute: function() {
            ExtractorRoot = (function () {
                function ExtractorRoot() {
                    this.title = 'Tour of Heroes';
                }
                ExtractorRoot = __decorate([
                    core_1.Component({
                        selector: 'extractor-root',
                        styleUrls: ['/extractor-ui.css'],
                        directives: [export_format_component_1.ExportFormatComponent,
                            page_by_samples_component_1.SearchCriteriaBySamplesComponent,
                            page_by_project_component_1.PageByProjectComponent],
                        providers: [
                            http_1.HTTP_PROVIDERS,
                            principle_investigator_service_1.PrincipleInvestigatorService,
                            name_id_list_service_1.NameIdListService,
                        ],
                        template: "\n        <div class = \"panel panel-default\">\n        \n           <div class = \"panel-heading\">\n              <h1 class = \"panel-title\">GOBii Extractor</h1>\n           </div>\n           \n            <div class=\"container-fluid\">\n            \n                <div class=\"row\">\n        \n                    <div class=\"col-md-3\"> \n        \n                        <div class=\"sidebar-nav\">\n                          <div class=\"navbar navbar-default\" role=\"navigation\">\n                            <div class=\"navbar-header\">Search Options</div>\n                            <div class=\"navbar-collapse collapse sidebar-navbar-collapse\">\n                              <ul class=\"nav navbar-nav\">\n                                <li><a href=\"#\" class=\"active\">By Project</a></li>\n                                <li><a href=\"#\">By Sample</a></li>\n                                <li><a href=\"#\">By Marker/Haplotype</a></li>\n                                <li><a href=\"#\">By Platform</a></li>\n                                <li><a href=\"#\">By Map Location</a></li>\n                              </ul>\n                            </div><!--/.nav-collapse -->\n                          </div>\n                        </div>\t\t\t\n                    \n                    </div>  <!-- outer grid column 1-->\n                    \n                    <div class=\"col-md-5\"> \n                          \n                            <div class=\"row\">\n                                <div class=\"col-md-12\">\t\t\t\t\t\t\t\n                                    <page-by-project></page-by-project>\n                                </div>\n                            </div> <!-- inner grid row 1 -->\n                            \n                            <div class=\"row\">\n                                <div class=\"col-md-12\">\n                                    <export-format></export-format>\n                                </div>\n                            </div> <!-- inner grid row 2 -->\n        \n                            <div class=\"row\">\n                                <div class=\"col-md-12\">\n                                    SEARCH BUTTON GOES HERE\n                                </div>\n                            </div> <!-- inner grid row 3 -->\n        \n        \n                         \n                    </div>  <!-- outer grid column 2 (inner grid)-->\n                    \n                    <div class=\"col-md-4\">\n                        FILTERS GO HERE\n                    </div>  <!-- outer grid column 3-->\n                    \n                </div> <!-- .row of outer grid -->\n                \n            </div> \n\t" // end template
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