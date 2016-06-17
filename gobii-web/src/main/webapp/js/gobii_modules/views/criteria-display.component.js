System.register(["@angular/core"], function(exports_1, context_1) {
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
    var CriteriaDisplayComponent;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            }],
        execute: function() {
            CriteriaDisplayComponent = (function () {
                function CriteriaDisplayComponent() {
                    // useg
                    this.gobiiDatasetExtracts = [];
                } // ctor
                CriteriaDisplayComponent.prototype.ngOnInit = function () {
                    return null;
                };
                CriteriaDisplayComponent.prototype.ngOnChanges = function (changes) {
                    this.gobiiDatasetExtracts = changes['gobiiDatasetExtracts'].currentValue;
                };
                CriteriaDisplayComponent = __decorate([
                    core_1.Component({
                        selector: 'criteria-display',
                        inputs: ['gobiiDatasetExtracts'],
                        //directives: [RADIO_GROUP_DIRECTIVES]
                        template: "<div style=\"overflow:auto; height: 80px; border: 1px solid #336699; padding-left: 5px;\">\n                    <div *ngFor=\"let gobiiDataSetExtract of gobiiDatasetExtracts\" >{{gobiiDataSetExtract.dataSetName}}</div>\n                </div>\n" // end template
                    }), 
                    __metadata('design:paramtypes', [])
                ], CriteriaDisplayComponent);
                return CriteriaDisplayComponent;
            }());
            exports_1("CriteriaDisplayComponent", CriteriaDisplayComponent);
        }
    }
});
//# sourceMappingURL=criteria-display.component.js.map