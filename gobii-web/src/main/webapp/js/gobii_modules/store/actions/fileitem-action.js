System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var ADD_TO_EXTRACT, ADD_TO_EXTRACT_BY_ITEM_ID, REPLACE_IN_EXTRACT_BY_ITEM_ID, REPLACE_ITEM_OF_SAME_COMPOUND_ID, REMOVE_FROM_EXTRACT_BY_ITEM_ID, REMOVE_FROM_EXTRACT, SET_EXTRACT_TYPE, LOAD_FILE_ITEM_LIST, LOAD_FILE_ITEM, REMOVE_ALL_FROM_EXTRACT, LoadFileItemListAction, LoadFileItemtAction, RemoveFromExtractAction, AddToExtractAction, AddToExtractByItemIdAction, ReplaceInExtractByItemIdAction, ReplaceItemOfSameCompoundIdAction, SetExtractType, RemoveFromExractByItemIdAction, RemoveAllFromExtractAction;
    return {
        setters: [],
        execute: function () {
            exports_1("ADD_TO_EXTRACT", ADD_TO_EXTRACT = '[GobiiFileItem] Add to Extract');
            exports_1("ADD_TO_EXTRACT_BY_ITEM_ID", ADD_TO_EXTRACT_BY_ITEM_ID = '[GobiiFileItem] Add to Extract by ID');
            exports_1("REPLACE_IN_EXTRACT_BY_ITEM_ID", REPLACE_IN_EXTRACT_BY_ITEM_ID = '[GobiiFileItem] Replace In Extract by ID');
            exports_1("REPLACE_ITEM_OF_SAME_COMPOUND_ID", REPLACE_ITEM_OF_SAME_COMPOUND_ID = '[GobiiFileItem] Replace In Extract by compound ID');
            exports_1("REMOVE_FROM_EXTRACT_BY_ITEM_ID", REMOVE_FROM_EXTRACT_BY_ITEM_ID = '[GobiiFileItem] Remove from Extract by ID');
            exports_1("REMOVE_FROM_EXTRACT", REMOVE_FROM_EXTRACT = '[GobiiFileItem] Remove from Extract');
            exports_1("SET_EXTRACT_TYPE", SET_EXTRACT_TYPE = '[GobiiFileItem] Set ExtractType');
            exports_1("LOAD_FILE_ITEM_LIST", LOAD_FILE_ITEM_LIST = '[GobiiFileItem] Load File Item lIST');
            exports_1("LOAD_FILE_ITEM", LOAD_FILE_ITEM = '[GobiiFileItem] Load File Item');
            exports_1("REMOVE_ALL_FROM_EXTRACT", REMOVE_ALL_FROM_EXTRACT = '[GobiiFileItem] Remove all from Extract');
            /**
             * Every action is comprised of at least a type and an optional
             * payload. Expressing actions as classes enables powerful
             * type checking in fileItemsReducer functions.
             *
             * See Discriminated Unions: https://www.typescriptlang.org/docs/handbook/advanced-types.html#discriminated-unions
             */
            LoadFileItemListAction = (function () {
                function LoadFileItemListAction(payload) {
                    this.payload = payload;
                    this.type = LOAD_FILE_ITEM_LIST;
                }
                return LoadFileItemListAction;
            }());
            exports_1("LoadFileItemListAction", LoadFileItemListAction);
            LoadFileItemtAction = (function () {
                function LoadFileItemtAction(payload) {
                    this.payload = payload;
                    this.type = LOAD_FILE_ITEM;
                }
                return LoadFileItemtAction;
            }());
            exports_1("LoadFileItemtAction", LoadFileItemtAction);
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
            ReplaceInExtractByItemIdAction = (function () {
                function ReplaceInExtractByItemIdAction(payload) {
                    this.payload = payload;
                    this.type = REPLACE_IN_EXTRACT_BY_ITEM_ID;
                }
                return ReplaceInExtractByItemIdAction;
            }());
            exports_1("ReplaceInExtractByItemIdAction", ReplaceInExtractByItemIdAction);
            ReplaceItemOfSameCompoundIdAction = (function () {
                function ReplaceItemOfSameCompoundIdAction(payload) {
                    this.payload = payload;
                    this.type = REPLACE_ITEM_OF_SAME_COMPOUND_ID;
                }
                return ReplaceItemOfSameCompoundIdAction;
            }());
            exports_1("ReplaceItemOfSameCompoundIdAction", ReplaceItemOfSameCompoundIdAction);
            SetExtractType = (function () {
                function SetExtractType(payload) {
                    this.payload = payload;
                    this.type = SET_EXTRACT_TYPE;
                }
                return SetExtractType;
            }());
            exports_1("SetExtractType", SetExtractType);
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