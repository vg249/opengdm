System.register(["@angular/core", "../model/type-extractor-filter"], function (exports_1, context_1) {
    "use strict";
    var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
        var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
        if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
        else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
        return c > 3 && r && Object.defineProperty(target, key, r), r;
    };
    var __moduleName = context_1 && context_1.id;
    var core_1, type_extractor_filter_1, MarkerSampleCountComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            }
        ],
        execute: function () {
            MarkerSampleCountComponent = (function () {
                function MarkerSampleCountComponent() {
                    this.displayPanel = false;
                    this.displayCounts = false;
                    this.panelCollapsed = true;
                    this.markerCount = "0";
                    this.sampleCount = "0";
                    this.displaySpinner = true;
                }
                MarkerSampleCountComponent.prototype.onBeforeToggle = function ($event) {
                };
                MarkerSampleCountComponent.prototype.initCount = function () {
                    this.displayCounts = false;
                    this.displaySpinner = true;
                };
                MarkerSampleCountComponent.prototype.onAfterToggle = function ($event) {
                    var _this = this;
                    if ($event.collapsed) {
                        this.initCount();
                    }
                    else {
                        setTimeout(function () {
                            _this.displayCounts = true;
                            _this.displaySpinner = false;
                        }, 3000);
                    }
                };
                // gobiiExtractType is not set until you get OnChanges
                MarkerSampleCountComponent.prototype.ngOnChanges = function (changes) {
                    if (changes['gobiiExtractFilterType']
                        && (changes['gobiiExtractFilterType'].currentValue != null)
                        && (changes['gobiiExtractFilterType'].currentValue != undefined)) {
                        if (changes['gobiiExtractFilterType'].currentValue != changes['gobiiExtractFilterType'].previousValue) {
                            if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY) {
                                this.displayPanel = true;
                                this.panelCollapsed = true;
                                this.initCount();
                            }
                            else {
                                this.displayPanel = false;
                            }
                        } // if we have a new filter type
                    } // if filter type changed
                }; // ngonChanges
                MarkerSampleCountComponent = __decorate([
                    core_1.Component({
                        selector: 'marker-sample-count',
                        outputs: [],
                        inputs: ['gobiiExtractFilterType'],
                        template: "\n        <p-panel *ngIf=\"displayPanel\" header=\"Estimated Marker/Sample Count\"\n                 [(toggleable)]=\"displayPanel\"\n                 [(collapsed)]=\"panelCollapsed\"\n                 (onBeforeToggle)=\"onBeforeToggle($event)\"\n                 (onAfterToggle)=\"onAfterToggle($event)\">\n            <p-progressSpinner *ngIf=\"displaySpinner\" [style]=\"{width: '50px', height: '50px'}\"\n                               strokeWidth=\"8\" fill=\"#EEEEEE\"\n                               animationDuration=\"3s\"></p-progressSpinner>\n            <div *ngIf=\"displayCounts\">\n                <p>Total Markers: {{markerCount}}</p>\n                <p>Total Samples: {{sampleCount}}</p>\n            </div>\n        </p-panel>\n    "
                    })
                ], MarkerSampleCountComponent);
                return MarkerSampleCountComponent;
            }());
            exports_1("MarkerSampleCountComponent", MarkerSampleCountComponent);
        }
    };
});
//# sourceMappingURL=marker-sample-count.component.js.map