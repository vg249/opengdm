System.register(["./type-control"], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var type_control_1, ViewIdGeneratorService;
    return {
        setters: [
            function (type_control_1_1) {
                type_control_1 = type_control_1_1;
            }
        ],
        execute: function () {
            ViewIdGeneratorService = (function () {
                function ViewIdGeneratorService() {
                }
                ViewIdGeneratorService.makeIdNameIdListBox = function (filterParamName) {
                    return type_control_1.TypeControl[type_control_1.TypeControl.NAME_ID_LIST] + "_" + filterParamName;
                };
                return ViewIdGeneratorService;
            }());
            exports_1("ViewIdGeneratorService", ViewIdGeneratorService);
        }
    };
});
//# sourceMappingURL=view-id-generator-service.js.map