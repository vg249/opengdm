System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var LOAD, ACTIVATE, LoadAction, ActivateForExtractAction;
    return {
        setters: [],
        execute: function () {
            exports_1("LOAD", LOAD = '[GobiiTreeNode] Load');
            exports_1("ACTIVATE", ACTIVATE = '[GobiiTreeNode] Activate');
            /**
             * Every action is comprised of at least a type and an optional
             * payload. Expressing actions as classes enables powerful
             * type checking in fileItemsReducer functions.
             *
             * See Discriminated Unions: https://www.typescriptlang.org/docs/handbook/advanced-types.html#discriminated-unions
             */
            LoadAction = (function () {
                function LoadAction(payload) {
                    this.payload = payload;
                    this.type = LOAD;
                }
                return LoadAction;
            }());
            exports_1("LoadAction", LoadAction);
            ActivateForExtractAction = (function () {
                function ActivateForExtractAction(payload) {
                    this.payload = payload;
                    this.type = ACTIVATE;
                }
                return ActivateForExtractAction;
            }());
            exports_1("ActivateForExtractAction", ActivateForExtractAction);
        }
    };
});
//# sourceMappingURL=treenode-action.js.map