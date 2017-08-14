System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var LOAD, ACTIVATE, SELECT_EXTRACT_TYPE, LoadAction, ActivateForExtractAction, SelectExtractType;
    return {
        setters: [],
        execute: function () {
            exports_1("LOAD", LOAD = '[GobiiTreeNode] Load');
            exports_1("ACTIVATE", ACTIVATE = '[GobiiTreeNode] Activate');
            exports_1("SELECT_EXTRACT_TYPE", SELECT_EXTRACT_TYPE = '[GobiiTreeNode] Select Extract Type');
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
            SelectExtractType = (function () {
                function SelectExtractType(payload) {
                    this.payload = payload;
                    this.type = SELECT_EXTRACT_TYPE;
                }
                return SelectExtractType;
            }());
            exports_1("SelectExtractType", SelectExtractType);
        }
    };
});
//# sourceMappingURL=treenode-action.js.map