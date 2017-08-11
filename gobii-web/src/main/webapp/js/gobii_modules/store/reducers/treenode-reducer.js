System.register(["reselect", "../actions/treenode-action"], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    function gobiiTreeNodesReducer(state, action) {
        if (state === void 0) { state = initialState; }
        var returnVal = state;
        switch (action.type) {
            case gobiiTreeNodeAction.LOAD: {
                var gobiigobiiTreeItemsPayload = action.payload;
                var newGobiigobiiTreeItems = gobiigobiiTreeItemsPayload.filter(function (newItem) {
                    return state
                        .gobiiTreeItems
                        .filter(function (stateItem) {
                        return stateItem.getItemType() != newItem.getItemType() &&
                            stateItem.getEntityType() != newItem.getEntityType() &&
                            stateItem.getEntitySubType() != newItem.getEntitySubType() &&
                            stateItem.getCvFilterType() != newItem.getCvFilterType();
                    });
                });
                returnVal = {
                    gobiiTreeNodesActive: state.gobiiTreeNodesActive,
                    gobiiTreeItems: state.gobiiTreeItems.concat(newGobiigobiiTreeItems)
                };
                break;
            } // LOAD
            case gobiiTreeNodeAction.ACTIVATE: {
                var gobiigobiiTreeItemsPayload = action.payload;
                var treeItemsToDeactivate = gobiigobiiTreeItemsPayload.filter(function (newItem) {
                    return state
                        .gobiiTreeItems
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
                    gobiiTreeNodesActive: gobiigobiiTreeItemsPayload.map(function (gti) { return gti.getId(); }),
                    gobiiTreeItems: treeItemsToDeactivate.concat(gobiigobiiTreeItemsPayload)
                };
                break;
            } // SELECT_FOR_EXTRACT
        }
        return returnVal;
    }
    exports_1("gobiiTreeNodesReducer", gobiiTreeNodesReducer);
    var reselect_1, gobiiTreeNodeAction, initialState, getGobiiTreeItems, getIds, getIdsOfActivated, getSelected, getAll;
    return {
        setters: [
            function (reselect_1_1) {
                reselect_1 = reselect_1_1;
            },
            function (gobiiTreeNodeAction_1) {
                gobiiTreeNodeAction = gobiiTreeNodeAction_1;
            }
        ],
        execute: function () {
            ;
            exports_1("initialState", initialState = {
                gobiiTreeNodesActive: [],
                gobiiTreeItems: [],
            });
            exports_1("getGobiiTreeItems", getGobiiTreeItems = function (state) { return state.gobiiTreeItems; });
            exports_1("getIds", getIds = function (state) { return state.gobiiTreeItems.map(function (gti) { return gti.getId(); }); });
            exports_1("getIdsOfActivated", getIdsOfActivated = function (state) { return state.gobiiTreeNodesActive; });
            exports_1("getSelected", getSelected = reselect_1.createSelector(getGobiiTreeItems, getIdsOfActivated, function (gobiiTreeItems, selectedUniqueIds) {
                return gobiiTreeItems.filter(function (gti) {
                    selectedUniqueIds.filter(function (id) { return gti.getId() === id; });
                });
            }));
            exports_1("getAll", getAll = reselect_1.createSelector(getGobiiTreeItems, getIds, function (treeItems, ids) {
                return ids.map(function (id) { return treeItems[id]; });
            }));
        }
    };
});
//# sourceMappingURL=treenode-reducer.js.map