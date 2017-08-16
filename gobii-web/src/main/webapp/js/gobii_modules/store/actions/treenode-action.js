System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var INIT, LOAD_TREE_NODE, PLACE_TREE_NODE, ACTIVATE, SELECT_EXTRACT_TYPE, InitAction, LoadTreeNodeAction, PlaceTreeNodeAction, ActivateForExtractAction, SelectExtractType;
    return {
        setters: [],
        execute: function () {
            exports_1("INIT", INIT = '[GobiiTreeNode] Init');
            exports_1("LOAD_TREE_NODE", LOAD_TREE_NODE = '[GobiiTreeNode] Load Tree Nodes');
            exports_1("PLACE_TREE_NODE", PLACE_TREE_NODE = '[GobiiTreeNode] Add Tree Node');
            exports_1("ACTIVATE", ACTIVATE = '[GobiiTreeNode] Activate');
            exports_1("SELECT_EXTRACT_TYPE", SELECT_EXTRACT_TYPE = '[GobiiTreeNode] Select Extract Type');
            InitAction = (function () {
                function InitAction() {
                    this.type = INIT;
                }
                return InitAction;
            }());
            exports_1("InitAction", InitAction);
            /**
             * Every action is comprised of at least a type and an optional
             * payload. Expressing actions as classes enables powerful
             * type checking in fileItemsReducer functions.
             *
             * See Discriminated Unions: https://www.typescriptlang.org/docs/handbook/advanced-types.html#discriminated-unions
             */
            LoadTreeNodeAction = (function () {
                function LoadTreeNodeAction(payload) {
                    this.payload = payload;
                    this.type = LOAD_TREE_NODE;
                }
                return LoadTreeNodeAction;
            }());
            exports_1("LoadTreeNodeAction", LoadTreeNodeAction);
            PlaceTreeNodeAction = (function () {
                function PlaceTreeNodeAction(payload) {
                    this.payload = payload;
                    this.type = PLACE_TREE_NODE;
                }
                return PlaceTreeNodeAction;
            }());
            exports_1("PlaceTreeNodeAction", PlaceTreeNodeAction);
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