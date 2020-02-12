System.register(["reselect", "../actions/treenode-action", "../../model/GobiiTreeNode", "../../model/type-extractor-filter"], function (exports_1, context_1) {
    "use strict";
    var reselect_1, gobiiTreeNodeAction, GobiiTreeNode_1, type_extractor_filter_1, initialState, getGobiiTreeNodes, getGobiiTreeItemIds, getIdsOfActivated, getExtractFilterType, getSelected, getAll, getForSelectedFilter;
    var __moduleName = context_1 && context_1.id;
    function placeNodeInTree(nodeToPlace, treeNodes, gobiiExtractFilterType) {
        let returnVal = false;
        for (let idx = 0; !returnVal && (idx < treeNodes.length); idx++) {
            let currentTreenode = treeNodes[idx];
            if (currentTreenode.getGobiiExtractFilterType() === gobiiExtractFilterType &&
                currentTreenode.compoundIdeEquals(nodeToPlace)
                && currentTreenode.getContainerType() !== GobiiTreeNode_1.ContainerType.STRUCTURE) {
                if (currentTreenode.getContainerType() === GobiiTreeNode_1.ContainerType.NONE) {
                    treeNodes[idx] = nodeToPlace;
                }
                else if (currentTreenode.getContainerType() === GobiiTreeNode_1.ContainerType.DATA) {
                    let containerNode = treeNodes[idx];
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
        let returnVal = null;
        //use an oldfashioned for loop because the lambda based semantics don't have a break;
        for (let idx = 0; (idx < treeNodes.length) && !returnVal; idx++) {
            let currentTreeNode = treeNodes[idx];
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
        let returnVal = null;
        for (let idx = 0; (idx < treeNodes.length) && !returnVal; idx++) {
            let currentTreeNode = treeNodes[idx];
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
    function gobiiTreeNodesReducer(state = initialState, action) {
        let returnVal = state;
        switch (action.type) {
            case gobiiTreeNodeAction.INIT_TREE: {
                const gobiigobiiTreeItemsPayload = action.payload;
                returnVal = {
                    gobiiExtractFilterType: state.gobiiExtractFilterType,
                    gobiiTreeNodesActive: state.gobiiTreeNodesActive,
                    gobiiTreeNodes: gobiigobiiTreeItemsPayload
                };
                break;
            } // INIT_TREE
            case gobiiTreeNodeAction.PLACE_TREE_NODE: {
                const gobiiTreeNodePayload = action.payload;
                // copy the existing
                const newTreeNodes = state.gobiiTreeNodes.slice();
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
                const fileItemUniqueId = action.payload;
                returnVal = {
                    gobiiExtractFilterType: state.gobiiExtractFilterType,
                    gobiiTreeNodesActive: [...state.gobiiTreeNodesActive, ...[fileItemUniqueId]],
                    gobiiTreeNodes: state.gobiiTreeNodes
                };
                break;
            } // ADD_TO_EXTRACT
            case gobiiTreeNodeAction.DEACTIVATE: {
                const gobiiFileItemUniqueId = action.payload;
                const newTreeNodes = state.gobiiTreeNodes.slice();
                const gobiiTreeNodeToDeActivate = findTreeNodeByFIleItemId(newTreeNodes
                    .filter(tn => tn.getGobiiExtractFilterType() === state.gobiiExtractFilterType), gobiiFileItemUniqueId);
                // not all file items are in the tree
                if (gobiiTreeNodeToDeActivate) {
                    let containerType = gobiiTreeNodeToDeActivate.parent ?
                        gobiiTreeNodeToDeActivate.parent.getContainerType() :
                        GobiiTreeNode_1.ContainerType.NONE;
                    if (containerType === GobiiTreeNode_1.ContainerType.NONE
                        || containerType === GobiiTreeNode_1.ContainerType.STRUCTURE) {
                        gobiiTreeNodeToDeActivate.resetLabel();
                    }
                    else {
                        let children = gobiiTreeNodeToDeActivate.parent.getChildren();
                        children.splice(children.indexOf(gobiiTreeNodeToDeActivate, 0), 1);
                    }
                    let newAcxtiveNodeState = state.gobiiTreeNodesActive
                        .filter(gtn => gtn !== gobiiTreeNodeToDeActivate.getId());
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
                const gobiiExtractFilterType = action.payload.gobiiExtractFilterType;
                const gobiiFileItemCompoundId = action.payload.gobiiFileItemCompoundId;
                const icon = action.payload.icon;
                const newTreeNodesState = state.gobiiTreeNodes.slice();
                let treeNodeToMutate = findTreeNodeByCompoundId(newTreeNodesState, gobiiExtractFilterType, gobiiFileItemCompoundId);
                treeNodeToMutate.icon = icon;
                returnVal = {
                    gobiiExtractFilterType: state.gobiiExtractFilterType,
                    gobiiTreeNodesActive: state.gobiiTreeNodesActive,
                    gobiiTreeNodes: newTreeNodesState
                };
                break;
            }
            case gobiiTreeNodeAction.CLEAR_ALL: {
                let newSelectedNodes = state
                    .gobiiTreeNodesActive
                    .filter(id => state
                    .gobiiTreeNodes
                    .find(gtn => gtn.getFileItemId() === id
                    && gtn.getGobiiExtractFilterType() != action.payload));
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
            exports_1("getGobiiTreeNodes", getGobiiTreeNodes = (state) => state.gobiiTreeNodes);
            exports_1("getGobiiTreeItemIds", getGobiiTreeItemIds = (state) => state.gobiiTreeNodes.map(gti => gti.getId()));
            exports_1("getIdsOfActivated", getIdsOfActivated = (state) => state.gobiiTreeNodesActive);
            exports_1("getExtractFilterType", getExtractFilterType = (state) => state.gobiiExtractFilterType);
            exports_1("getSelected", getSelected = reselect_1.createSelector(getGobiiTreeNodes, getIdsOfActivated, getExtractFilterType, (gobiiTreeNodes, selectedUniqueIds, getExtractFilterType) => {
                let returnVal = [];
                gobiiTreeNodes
                    .forEach(function find(gtn) {
                    if (selectedUniqueIds.filter(id => id === gtn.getId()).length > 0) {
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
            exports_1("getAll", getAll = reselect_1.createSelector(getGobiiTreeNodes, getGobiiTreeItemIds, (treeItems, ids) => {
                return ids.map(id => treeItems[id]);
            }));
            exports_1("getForSelectedFilter", getForSelectedFilter = reselect_1.createSelector(getGobiiTreeNodes, getExtractFilterType, (treeItems, extractFilterType) => {
                return treeItems.filter(ti => ti.getGobiiExtractFilterType() === extractFilterType);
            }));
        }
    };
});
//# sourceMappingURL=treenode-reducer.js.map