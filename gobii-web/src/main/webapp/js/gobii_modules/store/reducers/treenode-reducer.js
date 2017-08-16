System.register(["reselect", "../actions/treenode-action", "../../model/GobiiTreeNode", "../../model/type-extractor-filter"], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    function placeNodeInTree(nodeToPlace, treeNodes) {
        var returnVal = false;
        for (var idx = 0; !returnVal && (idx < treeNodes.length); idx++) {
            var currentTreenode = treeNodes[idx];
            if (currentTreenode.getItemType() === nodeToPlace.getItemType() &&
                currentTreenode.getEntityType() === nodeToPlace.getEntityType() &&
                currentTreenode.getEntitySubType() === nodeToPlace.getEntitySubType() &&
                currentTreenode.getCvFilterType() === nodeToPlace.getCvFilterType() &&
                currentTreenode.getContainerType() !== GobiiTreeNode_1.ContainerType.STRUCTURE) {
                if (currentTreenode.getContainerType() === GobiiTreeNode_1.ContainerType.NONE) {
                    treeNodes[idx] = nodeToPlace;
                }
                else if (currentTreenode.getContainerType() === GobiiTreeNode_1.ContainerType.DATA) {
                    nodeToPlace.parent = nodeToPlace;
                    nodeToPlace.getChildren().push(nodeToPlace);
                }
                returnVal = true;
            }
            else {
                returnVal = placeNodeInTree(nodeToPlace, currentTreenode.getChildren());
            }
        }
        return returnVal;
    }
    function gobiiTreeNodesReducer(state, action) {
        if (state === void 0) { state = initialState; }
        var returnVal = state;
        switch (action.type) {
            case gobiiTreeNodeAction.LOAD_TREE_NODE: {
                var gobiigobiiTreeItemsPayload = action.payload;
                var newGobiigobiiTreeItems = gobiigobiiTreeItemsPayload.filter(function (newItem) {
                    return state
                        .gobiiTreeNodes
                        .filter(function (stateItem) {
                        return stateItem.getItemType() != newItem.getItemType() &&
                            stateItem.getEntityType() != newItem.getEntityType() &&
                            stateItem.getEntitySubType() != newItem.getEntitySubType() &&
                            stateItem.getCvFilterType() != newItem.getCvFilterType();
                    });
                });
                returnVal = {
                    gobiiExtractFilterType: state.gobiiExtractFilterType,
                    gobiiTreeNodesActive: state.gobiiTreeNodesActive,
                    gobiiTreeNodes: state.gobiiTreeNodes.concat(newGobiigobiiTreeItems)
                };
                break;
            } // LOAD_TREE_NODE
            case gobiiTreeNodeAction.PLACE_TREE_NODE: {
                var gobiiTreeNodePayload = action.payload;
                // copy the existing
                var newTreeNodes = state.gobiiTreeNodes.slice();
                if (placeNodeInTree(gobiiTreeNodePayload, newTreeNodes)) {
                    returnVal = {
                        gobiiExtractFilterType: state.gobiiExtractFilterType,
                        gobiiTreeNodesActive: state.gobiiTreeNodesActive,
                        gobiiTreeNodes: newTreeNodes
                    };
                }
                break;
            } // LOAD_TREE_NODE
            case gobiiTreeNodeAction.ACTIVATE: {
                var gobiigobiiTreeItemsPayload = action.payload;
                var treeItemsToDeactivate = gobiigobiiTreeItemsPayload.filter(function (newItem) {
                    return state
                        .gobiiTreeNodes
                        .filter(function (stateItem) {
                        return stateItem.getItemType() != newItem.getItemType() &&
                            stateItem.getEntityType() != newItem.getEntityType() &&
                            stateItem.getEntitySubType() != newItem.getEntitySubType() &&
                            stateItem.getCvFilterType() != newItem.getCvFilterType();
                    });
                });
                treeItemsToDeactivate.forEach(function (gti) { return gti.setActive(false); });
                gobiigobiiTreeItemsPayload.forEach(function (gti) { return gti.setActive(true); });
                returnVal = {
                    gobiiExtractFilterType: state.gobiiExtractFilterType,
                    gobiiTreeNodesActive: gobiigobiiTreeItemsPayload.map(function (gti) { return gti.getId(); }),
                    gobiiTreeNodes: treeItemsToDeactivate.concat(gobiigobiiTreeItemsPayload)
                };
                break;
            } // SELECT_FOR_EXTRACT
            case gobiiTreeNodeAction.SELECT_EXTRACT_TYPE: {
                returnVal = {
                    gobiiExtractFilterType: action.payload,
                    gobiiTreeNodesActive: state.gobiiTreeNodesActive,
                    gobiiTreeNodes: state.gobiiTreeNodes
                };
            }
        }
        return returnVal;
    }
    exports_1("gobiiTreeNodesReducer", gobiiTreeNodesReducer);
    var reselect_1, gobiiTreeNodeAction, GobiiTreeNode_1, type_extractor_filter_1, initialState, getGobiiTreeItems, getGobiiTreeItemIds, getIdsOfActivated, getExtractFilterType, getSelected, getAll, getForSelectedFilter;
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
            exports_1("getGobiiTreeItems", getGobiiTreeItems = function (state) { return state.gobiiTreeNodes; });
            exports_1("getGobiiTreeItemIds", getGobiiTreeItemIds = function (state) { return state.gobiiTreeNodes.map(function (gti) { return gti.getId(); }); });
            exports_1("getIdsOfActivated", getIdsOfActivated = function (state) { return state.gobiiTreeNodesActive; });
            exports_1("getExtractFilterType", getExtractFilterType = function (state) { return state.gobiiExtractFilterType; });
            exports_1("getSelected", getSelected = reselect_1.createSelector(getGobiiTreeItems, getIdsOfActivated, function (gobiiTreeItems, selectedUniqueIds) {
                return gobiiTreeItems.filter(function (gti) {
                    selectedUniqueIds.filter(function (id) { return gti.getId() === id; });
                });
            }));
            exports_1("getAll", getAll = reselect_1.createSelector(getGobiiTreeItems, getGobiiTreeItemIds, function (treeItems, ids) {
                return ids.map(function (id) { return treeItems[id]; });
            }));
            exports_1("getForSelectedFilter", getForSelectedFilter = reselect_1.createSelector(getGobiiTreeItems, getExtractFilterType, function (treeItems, extractFilterType) {
                return treeItems.filter(function (ti) { return ti.getGobiiExtractFilterType() === extractFilterType; });
            }));
        }
    };
});
//# sourceMappingURL=treenode-reducer.js.map