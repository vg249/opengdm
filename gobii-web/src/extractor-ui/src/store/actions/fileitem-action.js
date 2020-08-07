System.register([], function (exports_1, context_1) {
    "use strict";
    var ADD_TO_EXTRACT, ADD_TO_EXTRACT_BY_ITEM_ID, REPLACE_BY_ITEM_ID, REPLACE_ITEM_OF_SAME_COMPOUND_ID, REMOVE_FROM_EXTRACT_BY_ITEM_ID, REMOVE_FROM_EXTRACT, SET_EXTRACT_TYPE, LOAD_FILE_ITEM_LIST_WITH_FILTER, LOAD_FILE_ITEM, LOAD_FILTER, REMOVE_ALL_FROM_EXTRACT, LoadFileItemListWithFilterAction, LoadFilterAction, LoadFileItemtAction, RemoveFromExtractAction, AddToExtractAction, AddToExtractByItemIdAction, ReplaceByItemIdAction, ReplaceItemOfSameCompoundIdAction, SetExtractType, RemoveFromExractByItemIdAction, RemoveAllFromExtractAction;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [],
        execute: function () {
            exports_1("ADD_TO_EXTRACT", ADD_TO_EXTRACT = '[GobiiFileItem] Add to Extract');
            exports_1("ADD_TO_EXTRACT_BY_ITEM_ID", ADD_TO_EXTRACT_BY_ITEM_ID = '[GobiiFileItem] Add to Extract by ID');
            exports_1("REPLACE_BY_ITEM_ID", REPLACE_BY_ITEM_ID = '[GobiiFileItem] Replace In Extract by ID');
            exports_1("REPLACE_ITEM_OF_SAME_COMPOUND_ID", REPLACE_ITEM_OF_SAME_COMPOUND_ID = '[GobiiFileItem] Replace In Extract by compound ID');
            exports_1("REMOVE_FROM_EXTRACT_BY_ITEM_ID", REMOVE_FROM_EXTRACT_BY_ITEM_ID = '[GobiiFileItem] Remove from Extract by ID');
            exports_1("REMOVE_FROM_EXTRACT", REMOVE_FROM_EXTRACT = '[GobiiFileItem] Remove from Extract');
            exports_1("SET_EXTRACT_TYPE", SET_EXTRACT_TYPE = '[GobiiFileItem] Set ExtractType');
            exports_1("LOAD_FILE_ITEM_LIST_WITH_FILTER", LOAD_FILE_ITEM_LIST_WITH_FILTER = '[GobiiFileItem] Load File Item List With Filter');
            exports_1("LOAD_FILE_ITEM", LOAD_FILE_ITEM = '[GobiiFileItem] Load File Item');
            exports_1("LOAD_FILTER", LOAD_FILTER = '[GobiiFileItem] Load Filter List');
            exports_1("REMOVE_ALL_FROM_EXTRACT", REMOVE_ALL_FROM_EXTRACT = '[GobiiFileItem] Remove all from Extract');
            /**
             * Every action is comprised of at least a type and an optional
             * payload. Expressing actions as classes enables powerful
             * type checking in fileItemsReducer functions.
             *
             * See Discriminated Unions: https://www.typescriptlang.org/docs/handbook/advanced-types.html#discriminated-unions
             */
            LoadFileItemListWithFilterAction = /** @class */ (function () {
                function LoadFileItemListWithFilterAction(payload) {
                    this.payload = payload;
                    this.type = LOAD_FILE_ITEM_LIST_WITH_FILTER;
                }
                return LoadFileItemListWithFilterAction;
            }());
            exports_1("LoadFileItemListWithFilterAction", LoadFileItemListWithFilterAction);
            // export class LoadFileItemListAction implements Action {
            //     readonly type = LOAD_FILE_ITEM_LIST;
            //
            //     constructor(public payload: {
            //         gobiiFileItems: GobiiFileItem[]
            //     }) {
            //     }
            // }
            LoadFilterAction = /** @class */ (function () {
                function LoadFilterAction(payload) {
                    this.payload = payload;
                    this.type = LOAD_FILTER;
                }
                return LoadFilterAction;
            }());
            exports_1("LoadFilterAction", LoadFilterAction);
            LoadFileItemtAction = /** @class */ (function () {
                function LoadFileItemtAction(payload) {
                    this.payload = payload;
                    this.type = LOAD_FILE_ITEM;
                }
                return LoadFileItemtAction;
            }());
            exports_1("LoadFileItemtAction", LoadFileItemtAction);
            RemoveFromExtractAction = /** @class */ (function () {
                function RemoveFromExtractAction(payload) {
                    this.payload = payload;
                    this.type = REMOVE_FROM_EXTRACT;
                }
                return RemoveFromExtractAction;
            }());
            exports_1("RemoveFromExtractAction", RemoveFromExtractAction);
            AddToExtractAction = /** @class */ (function () {
                function AddToExtractAction(payload) {
                    this.payload = payload;
                    this.type = ADD_TO_EXTRACT;
                }
                return AddToExtractAction;
            }());
            exports_1("AddToExtractAction", AddToExtractAction);
            AddToExtractByItemIdAction = /** @class */ (function () {
                function AddToExtractByItemIdAction(payload) {
                    this.payload = payload;
                    this.type = ADD_TO_EXTRACT_BY_ITEM_ID;
                }
                return AddToExtractByItemIdAction;
            }());
            exports_1("AddToExtractByItemIdAction", AddToExtractByItemIdAction);
            ReplaceByItemIdAction = /** @class */ (function () {
                function ReplaceByItemIdAction(payload) {
                    this.payload = payload;
                    this.type = REPLACE_BY_ITEM_ID;
                }
                return ReplaceByItemIdAction;
            }());
            exports_1("ReplaceByItemIdAction", ReplaceByItemIdAction);
            ReplaceItemOfSameCompoundIdAction = /** @class */ (function () {
                function ReplaceItemOfSameCompoundIdAction(payload) {
                    this.payload = payload;
                    this.type = REPLACE_ITEM_OF_SAME_COMPOUND_ID;
                }
                return ReplaceItemOfSameCompoundIdAction;
            }());
            exports_1("ReplaceItemOfSameCompoundIdAction", ReplaceItemOfSameCompoundIdAction);
            SetExtractType = /** @class */ (function () {
                function SetExtractType(payload) {
                    this.payload = payload;
                    this.type = SET_EXTRACT_TYPE;
                }
                return SetExtractType;
            }());
            exports_1("SetExtractType", SetExtractType);
            RemoveFromExractByItemIdAction = /** @class */ (function () {
                function RemoveFromExractByItemIdAction(payload) {
                    this.payload = payload;
                    this.type = REMOVE_FROM_EXTRACT_BY_ITEM_ID;
                }
                return RemoveFromExractByItemIdAction;
            }());
            exports_1("RemoveFromExractByItemIdAction", RemoveFromExractByItemIdAction);
            RemoveAllFromExtractAction = /** @class */ (function () {
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