System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var FIND, FIND_COMPLETE, LOAD, SELECT_FOR_EXTRACT, DESELECT_FOR_EXTRACT, FindAction, FindCompleteAction, LoadAction, SelectForExtractAction, DeSelectForExtractAction;
    return {
        setters: [],
        execute: function () {
            exports_1("FIND", FIND = '[GobiiFileItem] Find');
            exports_1("FIND_COMPLETE", FIND_COMPLETE = '[GobiiFileItem] Search Complete');
            exports_1("LOAD", LOAD = '[GobiiFileItem] Load');
            exports_1("SELECT_FOR_EXTRACT", SELECT_FOR_EXTRACT = '[GobiiFileItem] Select');
            exports_1("DESELECT_FOR_EXTRACT", DESELECT_FOR_EXTRACT = '[GobiiFileItem] Select');
            /**
             * Every action is comprised of at least a type and an optional
             * payload. Expressing actions as classes enables powerful
             * type checking in reducer functions.
             *
             * See Discriminated Unions: https://www.typescriptlang.org/docs/handbook/advanced-types.html#discriminated-unions
             */
            FindAction = (function () {
                function FindAction(payload) {
                    this.payload = payload;
                    this.type = FIND;
                }
                return FindAction;
            }());
            exports_1("FindAction", FindAction);
            FindCompleteAction = (function () {
                function FindCompleteAction(payload) {
                    this.payload = payload;
                    this.type = FIND_COMPLETE;
                }
                return FindCompleteAction;
            }());
            exports_1("FindCompleteAction", FindCompleteAction);
            LoadAction = (function () {
                function LoadAction(payload) {
                    this.payload = payload;
                    this.type = LOAD;
                }
                return LoadAction;
            }());
            exports_1("LoadAction", LoadAction);
            SelectForExtractAction = (function () {
                function SelectForExtractAction(payload) {
                    this.payload = payload;
                    this.type = SELECT_FOR_EXTRACT;
                }
                return SelectForExtractAction;
            }());
            exports_1("SelectForExtractAction", SelectForExtractAction);
            DeSelectForExtractAction = (function () {
                function DeSelectForExtractAction(payload) {
                    this.payload = payload;
                    this.type = DESELECT_FOR_EXTRACT;
                }
                return DeSelectForExtractAction;
            }());
            exports_1("DeSelectForExtractAction", DeSelectForExtractAction);
        }
    };
});
//# sourceMappingURL=fileitem.js.map