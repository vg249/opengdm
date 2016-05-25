System.register(['angular2/core'], function(exports_1, context_1) {
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
    var core_1;
    var ExportFormatComponent;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            }],
        execute: function() {
            ExportFormatComponent = (function () {
                function ExportFormatComponent() {
                } // ctor
                ExportFormatComponent.prototype.ngOnInit = function () {
                    /*
                            let id = +this._routeParams.get('id');
                            this._heroService.getHero(id)
                              .then(hero => this.hero = hero);
                    */
                };
                ExportFormatComponent = __decorate([
                    core_1.Component({
                        selector: 'export-format',
                        //  inputs: ['hero'],
                        //directives: [RADIO_GROUP_DIRECTIVES]
                        //  directives: [Alert]
                        template: "\n\t\t<form>\n\t\t\t<fieldset class=\"well the-fieldset\">\n\t\t\t<legend class=\"the-legend\">Export Format</legend>\n\t\t\t\t<div class=\"control-group\">\n\t\t\t\t\t\t\t  <input type=\"radio\" name=\"Hapmap\" value=\"Hapmap\">Hapmap<br>\n\t\t\t\t\t\t\t  <input type=\"radio\" name=\"FlapJack\" value=\"FlapJack\">FlapJack<br>\n\t\t\t\t\t\t\t  <input type=\"radio\" name=\"VCF\" value=\"VCF\">VCF<br>\n\t\t\t\t\t\t\t  <input type=\"radio\" name=\"HDF5\" value=\"HDF5\">HDF5<br>\n\t\t\t\t\t\t\t  <input type=\"radio\" name=\"PLINK CSV\" value=\"PLINK CSV\">PLINK CSV<br>\n\t\t\t\t</div>\n\t\t\t</fieldset>\n\t\t\t\n\t\t</form>\n\t" // end template
                    }), 
                    __metadata('design:paramtypes', [])
                ], ExportFormatComponent);
                return ExportFormatComponent;
            }());
            exports_1("ExportFormatComponent", ExportFormatComponent);
        }
    }
});
//# sourceMappingURL=export-format.component.js.map