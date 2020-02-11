System.register([], function (exports_1, context_1) {
    "use strict";
    var INIT, INIT_TREE, PLACE_TREE_NODE, ACTIVATE, DEACTIVATE, SELECT_EXTRACT_TYPE, CLEAR_ALL, SET_NODE_STATUS, InitAction, InitTree, PlaceTreeNodeAction, ActivateForExtractAction, DeActivateFromExtractAction, ClearAll, SelectExtractType, SetTreeNodeLook;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [],
        execute: function () {
            exports_1("INIT", INIT = '[GobiiTreeNode] Init');
            exports_1("INIT_TREE", INIT_TREE = '[GobiiTreeNode] Load Tree Nodes');
            exports_1("PLACE_TREE_NODE", PLACE_TREE_NODE = '[GobiiTreeNode] Add Tree Node');
            exports_1("ACTIVATE", ACTIVATE = '[GobiiTreeNode] Activate');
            exports_1("DEACTIVATE", DEACTIVATE = '[GobiiTreeNode] Deactivate');
            exports_1("SELECT_EXTRACT_TYPE", SELECT_EXTRACT_TYPE = '[GobiiTreeNode] Select Extract Type');
            exports_1("CLEAR_ALL", CLEAR_ALL = '[GobiiTreeNode] Clear All');
            exports_1("SET_NODE_STATUS", SET_NODE_STATUS = '[GobiiTreeNode] Set Node Status');
            InitAction = /** @class */ (function () {
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
            InitTree = /** @class */ (function () {
                function InitTree(payload) {
                    this.payload = payload;
                    this.type = INIT_TREE;
                }
                return InitTree;
            }());
            exports_1("InitTree", InitTree);
            PlaceTreeNodeAction = /** @class */ (function () {
                function PlaceTreeNodeAction(payload) {
                    this.payload = payload;
                    this.type = PLACE_TREE_NODE;
                }
                return PlaceTreeNodeAction;
            }());
            exports_1("PlaceTreeNodeAction", PlaceTreeNodeAction);
            ActivateForExtractAction = /** @class */ (function () {
                function ActivateForExtractAction(payload) {
                    this.payload = payload;
                    this.type = ACTIVATE;
                }
                return ActivateForExtractAction;
            }());
            exports_1("ActivateForExtractAction", ActivateForExtractAction);
            DeActivateFromExtractAction = /** @class */ (function () {
                //fileitemuniqueid
                function DeActivateFromExtractAction(payload) {
                    this.payload = payload;
                    this.type = DEACTIVATE;
                }
                return DeActivateFromExtractAction;
            }());
            exports_1("DeActivateFromExtractAction", DeActivateFromExtractAction);
            ClearAll = /** @class */ (function () {
                function ClearAll(payload) {
                    this.payload = payload;
                    this.type = CLEAR_ALL;
                }
                return ClearAll;
            }());
            exports_1("ClearAll", ClearAll);
            SelectExtractType = /** @class */ (function () {
                function SelectExtractType(payload) {
                    this.payload = payload;
                    this.type = SELECT_EXTRACT_TYPE;
                }
                return SelectExtractType;
            }());
            exports_1("SelectExtractType", SelectExtractType);
            SetTreeNodeLook = /** @class */ (function () {
                function SetTreeNodeLook(payload) {
                    this.payload = payload;
                    this.type = SET_NODE_STATUS;
                }
                return SetTreeNodeLook;
            }());
            exports_1("SetTreeNodeLook", SetTreeNodeLook);
        }
    };
});
//# sourceMappingURL=treenode-action.js.map