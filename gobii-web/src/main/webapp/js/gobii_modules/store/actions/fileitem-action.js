System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var ADD_TO_EXTRACT, ADD_TO_EXTRACT_BY_ITEM_ID, REMOVE_FROM_EXTRACT_BY_ITEM_ID, REMOVE_FROM_EXTRACT, SET_FILTER_VALUE, LOAD_FILE_ITEMS, REMOVE_ALL_FROM_EXTRACT, LoadFileItemsAction, RemoveFromExtractAction, AddToExtractAction, AddToExtractByItemIdAction, SetFilterValueAction, RemoveFromExractByItemIdAction, RemoveAllFromExtractAction;
    return {
        setters: [],
        execute: function () {
            exports_1("ADD_TO_EXTRACT", ADD_TO_EXTRACT = '[GobiiFileItem] Add to Extract');
            exports_1("ADD_TO_EXTRACT_BY_ITEM_ID", ADD_TO_EXTRACT_BY_ITEM_ID = '[GobiiFileItem] Add to Extract by ID');
            exports_1("REMOVE_FROM_EXTRACT_BY_ITEM_ID", REMOVE_FROM_EXTRACT_BY_ITEM_ID = '[GobiiFileItem] Remove from Extract by ID');
            exports_1("REMOVE_FROM_EXTRACT", REMOVE_FROM_EXTRACT = '[GobiiFileItem] Remove from Extract');
            exports_1("SET_FILTER_VALUE", SET_FILTER_VALUE = '[GobiiFileItem] Set Filter Value');
            exports_1("LOAD_FILE_ITEMS", LOAD_FILE_ITEMS = '[GobiiFileItem] Load File Items');
            exports_1("REMOVE_ALL_FROM_EXTRACT", REMOVE_ALL_FROM_EXTRACT = '[GobiiFileItem] Remove all from Extract');
            /**
             * Every action is comprised of at least a type and an optional
             * payload. Expressing actions as classes enables powerful
             * type checking in fileItemsReducer functions.
             *
             * See Discriminated Unions: https://www.typescriptlang.org/docs/handbook/advanced-types.html#discriminated-unions
             */
            LoadFileItemsAction = (function () {
                function LoadFileItemsAction(payload) {
                    this.payload = payload;
                    this.type = LOAD_FILE_ITEMS;
                }
                return LoadFileItemsAction;
            }());
            exports_1("LoadFileItemsAction", LoadFileItemsAction);
            RemoveFromExtractAction = (function () {
                function RemoveFromExtractAction(payload) {
                    this.payload = payload;
                    this.type = REMOVE_FROM_EXTRACT;
                }
                return RemoveFromExtractAction;
            }());
            exports_1("RemoveFromExtractAction", RemoveFromExtractAction);
            AddToExtractAction = (function () {
                function AddToExtractAction(payload) {
                    this.payload = payload;
                    this.type = ADD_TO_EXTRACT;
                }
                return AddToExtractAction;
            }());
            exports_1("AddToExtractAction", AddToExtractAction);
            AddToExtractByItemIdAction = (function () {
                function AddToExtractByItemIdAction(payload) {
                    this.payload = payload;
                    this.type = ADD_TO_EXTRACT_BY_ITEM_ID;
                }
                return AddToExtractByItemIdAction;
            }());
            exports_1("AddToExtractByItemIdAction", AddToExtractByItemIdAction);
            SetFilterValueAction = (function () {
                function SetFilterValueAction(payload) {
                    this.payload = payload;
                    this.type = SET_FILTER_VALUE;
                }
                return SetFilterValueAction;
            }());
            exports_1("SetFilterValueAction", SetFilterValueAction);
            RemoveFromExractByItemIdAction = (function () {
                function RemoveFromExractByItemIdAction(payload) {
                    this.payload = payload;
                    this.type = REMOVE_FROM_EXTRACT_BY_ITEM_ID;
                }
                return RemoveFromExractByItemIdAction;
            }());
            exports_1("RemoveFromExractByItemIdAction", RemoveFromExractByItemIdAction);
            RemoveAllFromExtractAction = (function () {
                function RemoveAllFromExtractAction(payload) {
                    this.payload = payload;
                    this.type = REMOVE_ALL_FROM_EXTRACT;
                }
                return RemoveAllFromExtractAction;
            }());
            exports_1("RemoveAllFromExtractAction", RemoveAllFromExtractAction);
        }
    };
});
//# sourceMappingURL=fileitem-action.js.map