System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var LOAD, SELECT_FOR_EXTRACT, DESELECT_FOR_EXTRACT, SET_ENTITY_FILTER, LOAD_FILTERED_ITEMS, LoadAction, LoadFilteredItemsAction, DeSelectForExtractAction, SelectForExtractAction, SetEntityFilter;
    return {
        setters: [],
        execute: function () {
            exports_1("LOAD", LOAD = '[GobiiFileItem] Load');
            exports_1("SELECT_FOR_EXTRACT", SELECT_FOR_EXTRACT = '[GobiiFileItem] Select');
            exports_1("DESELECT_FOR_EXTRACT", DESELECT_FOR_EXTRACT = '[GobiiFileItem] DeSelect');
            exports_1("SET_ENTITY_FILTER", SET_ENTITY_FILTER = '[GobiiFileItem] SetEntityFilter');
            exports_1("LOAD_FILTERED_ITEMS", LOAD_FILTERED_ITEMS = '[GobiiFileItem] LoadFilteredItems');
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
            LoadFilteredItemsAction = (function () {
                function LoadFilteredItemsAction(payload) {
                    this.payload = payload;
                    this.type = LOAD_FILTERED_ITEMS;
                }
                return LoadFilteredItemsAction;
            }());
            exports_1("LoadFilteredItemsAction", LoadFilteredItemsAction);
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
            SetEntityFilter = (function () {
                function SetEntityFilter(payload) {
                    this.payload = payload;
                    this.type = SET_ENTITY_FILTER;
                }
                return SetEntityFilter;
            }());
            exports_1("SetEntityFilter", SetEntityFilter);
        }
    };
});
//# sourceMappingURL=fileitem-action.js.map