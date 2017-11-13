System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var DatasetDatatable;
    return {
        setters: [],
        execute: function () {
            DatasetDatatable = (function () {
                //cars: Car[];
                function DatasetDatatable(store) {
                    this.store = store;
                }
                DatasetDatatable.prototype.ngOnInit = function () {
                    //this.carService.getCarsSmall().then(cars => this.cars = cars);
                };
                return DatasetDatatable;
            }());
            exports_1("DatasetDatatable", DatasetDatatable);
        }
    };
});
//# sourceMappingURL=dataset-datatable.js.map