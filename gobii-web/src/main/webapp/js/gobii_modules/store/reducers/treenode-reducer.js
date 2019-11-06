System.register(["reselect", "../actions/treenode-action", "../../model/GobiiTreeNode", "../../model/type-extractor-filter"], function (exports_1, context_1) {
    "use strict";
    var __spreadArrays = (this && this.__spreadArrays) || function () {
        for (var s = 0, i = 0, il = arguments.length; i < il; i++) s += arguments[i].length;
        for (var r = Array(s), k = 0, i = 0; i < il; i++)
            for (var a = arguments[i], j = 0, jl = a.length; j < jl; j++, k++)
                r[k] = a[j];
        return r;
    };
    var reselect_1, gobiiTreeNodeAction, GobiiTreeNode_1, type_extractor_filter_1, initialState, getGobiiTreeNodes, getGobiiTreeItemIds, getIdsOfActivated, getExtractFilterType, getSelected, getAll, getForSelectedFilter;
    var __moduleName = context_1 && context_1.id;
    function placeNodeInTree(nodeToPlace, treeNodes, gobiiExtractFilterType) {
        var returnVal = false;
        for (var idx = 0; !returnVal && (idx < treeNodes.length); idx++) {
            var currentTreenode = treeNodes[idx];
            if (currentTreenode.getGobiiExtractFilterType() === gobiiExtractFilterType &&
                currentTreenode.compoundIdeEquals(nodeToPlace)
                && currentTreenode.getContainerType() !== GobiiTreeNode_1.ContainerType.STRUCTURE) {
                if (currentTreenode.getContainerType() === GobiiTreeNode_1.ContainerType.NONE) {
                    treeNodes[idx] = nodeToPlace;
                }
                else if (currentTreenode.getContainerType() === GobiiTreeNode_1.ContainerType.DATA) {
                    var containerNode = treeNodes[idx];
                    nodeToPlace.parent = containerNode;
                    containerNode.getChildren().push(nodeToPlace);
                    containerNode.expanded = true;
                }
                returnVal = true;
            }
            else {
                returnVal = placeNodeInTree(nodeToPlace, currentTreenode.getChildren(), gobiiExtractFilterType);
            }
        }
        return returnVal;
    }
    function findTreeNodeByFIleItemId(treeNodes, fileItemUniqueId) {
        var returnVal = null;
        //use an oldfashioned for loop because the lambda based semantics don't have a break;
        for (var idx = 0; (idx < treeNodes.length) && !returnVal; idx++) {
            var currentTreeNode = treeNodes[idx];
            if (currentTreeNode.getFileItemId() === fileItemUniqueId) {
                returnVal = currentTreeNode;
            }
            else {
                returnVal = findTreeNodeByFIleItemId(currentTreeNode.getChildren(), fileItemUniqueId);
            }
        }
        return returnVal;
    }
    function findTreeNodeByCompoundId(treeNodes, gobiiExtractFilterType, gobiiFileItemCompoundId) {
        var returnVal = null;
        for (var idx = 0; (idx < treeNodes.length) && !returnVal; idx++) {
            var currentTreeNode = treeNodes[idx];
            if (currentTreeNode.getGobiiExtractFilterType() === gobiiExtractFilterType
                && gobiiFileItemCompoundId.compoundIdeEquals(currentTreeNode)) {
                returnVal = currentTreeNode;
            }
            else {
                returnVal = findTreeNodeByCompoundId(currentTreeNode.getChildren(), gobiiExtractFilterType, gobiiFileItemCompoundId);
            }
        }
        return returnVal;
    }
    function gobiiTreeNodesReducer(state, action) {
        if (state === void 0) { state = initialState; }
        var returnVal = state;
        switch (action.type) {
            case gobiiTreeNodeAction.INIT_TREE: {
                var gobiigobiiTreeItemsPayload = action.payload;
                returnVal = {
                    gobiiExtractFilterType: state.gobiiExtractFilterType,
                    gobiiTreeNodesActive: state.gobiiTreeNodesActive,
                    gobiiTreeNodes: gobiigobiiTreeItemsPayload
                };
                break;
            } // INIT_TREE
            case gobiiTreeNodeAction.PLACE_TREE_NODE: {
                var gobiiTreeNodePayload = action.payload;
                // copy the existing
                var newTreeNodes = state.gobiiTreeNodes.slice();
                if (placeNodeInTree(gobiiTreeNodePayload, newTreeNodes, state.gobiiExtractFilterType)) {
                    returnVal = {
                        gobiiExtractFilterType: state.gobiiExtractFilterType,
                        gobiiTreeNodesActive: state.gobiiTreeNodesActive,
                        gobiiTreeNodes: newTreeNodes
                    };
                }
                break;
            } // INIT_TREE
            case gobiiTreeNodeAction.ACTIVATE: {
                var fileItemUniqueId = action.payload;
                returnVal = {
                    gobiiExtractFilterType: state.gobiiExtractFilterType,
                    gobiiTreeNodesActive: __spreadArrays(state.gobiiTreeNodesActive, [fileItemUniqueId]),
                    gobiiTreeNodes: state.gobiiTreeNodes
                };
                break;
            } // ADD_TO_EXTRACT
            case gobiiTreeNodeAction.DEACTIVATE: {
                var gobiiFileItemUniqueId = action.payload;
                var newTreeNodes = state.gobiiTreeNodes.slice();
                var gobiiTreeNodeToDeActivate_1 = findTreeNodeByFIleItemId(newTreeNodes
                    .filter(function (tn) { return tn.getGobiiExtractFilterType() === state.gobiiExtractFilterType; }), gobiiFileItemUniqueId);
                // not all file items are in the tree
                if (gobiiTreeNodeToDeActivate_1) {
                    var containerType = gobiiTreeNodeToDeActivate_1.parent ?
                        gobiiTreeNodeToDeActivate_1.parent.getContainerType() :
                        GobiiTreeNode_1.ContainerType.NONE;
                    if (containerType === GobiiTreeNode_1.ContainerType.NONE
                        || containerType === GobiiTreeNode_1.ContainerType.STRUCTURE) {
                        gobiiTreeNodeToDeActivate_1.resetLabel();
                    }
                    else {
                        var children = gobiiTreeNodeToDeActivate_1.parent.getChildren();
                        children.splice(children.indexOf(gobiiTreeNodeToDeActivate_1, 0), 1);
                    }
                    var newAcxtiveNodeState = state.gobiiTreeNodesActive
                        .filter(function (gtn) { return gtn !== gobiiTreeNodeToDeActivate_1.getId(); });
                    returnVal = {
                        gobiiExtractFilterType: state.gobiiExtractFilterType,
                        gobiiTreeNodesActive: newAcxtiveNodeState,
                        gobiiTreeNodes: newTreeNodes
                    };
                }
                else {
                    returnVal = {
                        gobiiExtractFilterType: state.gobiiExtractFilterType,
                        gobiiTreeNodesActive: state.gobiiTreeNodesActive,
                        gobiiTreeNodes: state.gobiiTreeNodes
                    };
                }
                break;
            } // ADD_TO_EXTRACT
            case gobiiTreeNodeAction.SELECT_EXTRACT_TYPE: {
                returnVal = {
                    gobiiExtractFilterType: action.payload,
                    gobiiTreeNodesActive: state.gobiiTreeNodesActive,
                    gobiiTreeNodes: state.gobiiTreeNodes
                };
                break;
            } // SELECT_EXTRACT_TYPE
            case gobiiTreeNodeAction.SET_NODE_STATUS: {
                var gobiiExtractFilterType = action.payload.gobiiExtractFilterType;
                var gobiiFileItemCompoundId = action.payload.gobiiFileItemCompoundId;
                var icon = action.payload.icon;
                var newTreeNodesState = state.gobiiTreeNodes.slice();
                var treeNodeToMutate = findTreeNodeByCompoundId(newTreeNodesState, gobiiExtractFilterType, gobiiFileItemCompoundId);
                treeNodeToMutate.icon = icon;
                returnVal = {
                    gobiiExtractFilterType: state.gobiiExtractFilterType,
                    gobiiTreeNodesActive: state.gobiiTreeNodesActive,
                    gobiiTreeNodes: newTreeNodesState
                };
                break;
            }
            case gobiiTreeNodeAction.CLEAR_ALL: {
                var newSelectedNodes = state
                    .gobiiTreeNodesActive
                    .filter(function (id) { return state
                    .gobiiTreeNodes
                    .find(function (gtn) { return gtn.getFileItemId() === id
                    && gtn.getGobiiExtractFilterType() != action.payload; }); });
                returnVal = {
                    gobiiExtractFilterType: state.gobiiExtractFilterType,
                    gobiiTreeNodesActive: newSelectedNodes,
                    gobiiTreeNodes: state.gobiiTreeNodes
                };
                break;
            }
        }
        return returnVal;
    }
    exports_1("gobiiTreeNodesReducer", gobiiTreeNodesReducer);
    return {
        setters: [
            function (reselect_1_1) {
                reselect_1 = reselect_1_1;
            },
            function (gobiiTreeNodeAction_1) {
                gobiiTreeNodeAction = gobiiTreeNodeAction_1;
            },
            function (GobiiTreeNode_1_1) {
                GobiiTreeNode_1 = GobiiTreeNode_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            }
        ],
        execute: function () {
            ;
            exports_1("initialState", initialState = {
                gobiiExtractFilterType: type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN,
                gobiiTreeNodesActive: [],
                gobiiTreeNodes: [],
            });
            exports_1("getGobiiTreeNodes", getGobiiTreeNodes = function (state) { return state.gobiiTreeNodes; });
            exports_1("getGobiiTreeItemIds", getGobiiTreeItemIds = function (state) { return state.gobiiTreeNodes.map(function (gti) { return gti.getId(); }); });
            exports_1("getIdsOfActivated", getIdsOfActivated = function (state) { return state.gobiiTreeNodesActive; });
            exports_1("getExtractFilterType", getExtractFilterType = function (state) { return state.gobiiExtractFilterType; });
            exports_1("getSelected", getSelected = reselect_1.createSelector(getGobiiTreeNodes, getIdsOfActivated, getExtractFilterType, function (gobiiTreeNodes, selectedUniqueIds, getExtractFilterType) {
                var returnVal = [];
                gobiiTreeNodes
                    .forEach(function find(gtn) {
                    if (selectedUniqueIds.filter(function (id) { return id === gtn.getId(); }).length > 0) {
                        returnVal.push(gtn);
                    }
                    gtn.getChildren().forEach(find);
                });
                return returnVal;
                // this needs to be done in a more filterish way. For now it works
                // let returnVal: GobiiTreeNode[] =
                //     gobiiTreeNodes
                //         .filter(gtn => gtn.getGobiiExtractFilterType() === getExtractFilterType)
                //         .filter(function find(gtn) {
                //             let returnVal: boolean = selectedUniqueIds.filter(id => id === gtn.getId()).length > 0;
                //             if (!returnVal) {
                //                 returnVal = ( gtn.getContainerType() != ContainerType.STRUCTURE )
                //                     && (gtn.getChildren().filter(find).length > 0);
                //             }
                //
                //             return returnVal;
                //         });
                //
                // return returnVal;
            }));
            //    let returnVal: GobiiTreeNode[] = [];
            // gobiiTreeNodes
            //     .filter(gtn => gtn.getGobiiExtractFilterType() === getExtractFilterType )
            //     .forEach(n => {
            //         selectedUniqueIds.forEach(i => {
            //             if (n.getId() === i)
            //                 returnVal.push(n);
            //         })
            //
            //     }
            // );
            exports_1("getAll", getAll = reselect_1.createSelector(getGobiiTreeNodes, getGobiiTreeItemIds, function (treeItems, ids) {
                return ids.map(function (id) { return treeItems[id]; });
            }));
            exports_1("getForSelectedFilter", getForSelectedFilter = reselect_1.createSelector(getGobiiTreeNodes, getExtractFilterType, function (treeItems, extractFilterType) {
                return treeItems.filter(function (ti) { return ti.getGobiiExtractFilterType() === extractFilterType; });
            }));
        }
    };
});
//# sourceMappingURL=treenode-reducer.js.map