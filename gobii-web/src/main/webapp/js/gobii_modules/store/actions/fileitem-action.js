System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var LOAD, SELECT_FOR_EXTRACT, DESELECT_FOR_EXTRACT, LoadAction, DeSelectForExtractAction, SelectForExtractAction;
    return {
        setters: [],
        execute: function () {
            exports_1("LOAD", LOAD = '[GobiiFileItem] Load');
            exports_1("SELECT_FOR_EXTRACT", SELECT_FOR_EXTRACT = '[GobiiFileItem] Select');
            exports_1("DESELECT_FOR_EXTRACT", DESELECT_FOR_EXTRACT = '[GobiiFileItem] DeSelect');
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
            DeSelectForExtractAction = (function () {
                function DeSelectForExtractAction(payload) {
                    this.payload = payload;
                    this.type = DESELECT_FOR_EXTRACT;
                }
                return DeSelectForExtractAction;
            }());
            exports_1("DeSelectForExtractAction", DeSelectForExtractAction);
            SelectForExtractAction = (function () {
                function SelectForExtractAction(payload) {
                    this.payload = payload;
                    this.type = SELECT_FOR_EXTRACT;
                }
                return SelectForExtractAction;
            }());
            exports_1("SelectForExtractAction", SelectForExtractAction);
        }
    };
});
//# sourceMappingURL=fileitem-action.js.map