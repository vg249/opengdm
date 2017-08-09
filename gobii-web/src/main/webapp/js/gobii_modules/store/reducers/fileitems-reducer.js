System.register(["reselect", "../actions/fileitem-action"], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    function reducer(state, action) {
        if (state === void 0) { state = initialState; }
        var returnVal = state;
        switch (action.type) {
            case gobiiFileItemAction.LOAD: {
                var gobiiFileItemsPayload = action.payload;
                var newGobiiFileItems = gobiiFileItemsPayload.filter(function (newItem) {
                    return state
                        .fileItems
                        .filter(function (stateItem) {
                        return stateItem.getExtractorItemType() != newItem.getExtractorItemType() &&
                            stateItem.getEntityType() != newItem.getEntityType() &&
                            stateItem.getEntitySubType() != newItem.getEntitySubType() &&
                            stateItem.getItemId() != newItem.getItemId();
                    });
                });
                returnVal = {
                    fileItems: Object.assign({}, state.fileItems, newGobiiFileItems),
                    fileItemUniqueIdsSelected: state.fileItemUniqueIdsSelected
                };
            } // LOAD
            // Technically, and according to the ngrx/store example app,
            // it should be possible for different actions to have a different
            // payload type, such that it's possible for a payload to be a single
            // GobiiFileItem rather than an array of. However, I cannot get this
            // to compile properly. See this issue I submitted:
            // https://github.com/ngrx/platform/issues/255
            // For now in the interest of making progress I am using
            // an array type for all action payloads
            case gobiiFileItemAction.SELECT_FOR_EXTRACT: {
                var gobiiFileItemPayload = action.payload;
                var selectedUniqueItemIds = gobiiFileItemPayload
                    .filter(function (selectedFileItem) {
                    return state
                        .fileItemUniqueIdsSelected
                        .filter(function (selectedFileItemId) { return selectedFileItemId !== selectedFileItem.getFileItemUniqueId(); });
                })
                    .map(function (selectedFileItem) { return selectedFileItem.getFileItemUniqueId(); });
                returnVal = {
                    fileItems: state.fileItems,
                    fileItemUniqueIdsSelected: state.fileItemUniqueIdsSelected.concat(selectedUniqueItemIds) // spread syntax
                };
            } // SELECT_FOR_EXTRACT
            case gobiiFileItemAction.DESELECT_FOR_EXTRACT: {
                var gobiiFileItemPayload_1 = action.payload;
                var newSelectedUniqueItemIds = state
                    .fileItemUniqueIdsSelected
                    .filter(function (selectedId) {
                    return gobiiFileItemPayload_1.filter(function (deselectedItem) { return deselectedItem.getFileItemUniqueId() != selectedId; });
                });
                returnVal = {
                    fileItems: state.fileItems,
                    fileItemUniqueIdsSelected: newSelectedUniqueItemIds
                };
            } // switch()
        }
        return returnVal;
    }
    exports_1("reducer", reducer);
    var reselect_1, gobiiFileItemAction, initialState, getFileItems, getUniqueIds, getSelectedUniqueIds, getSelected, getAll;
    return {
        setters: [
            function (reselect_1_1) {
                reselect_1 = reselect_1_1;
            },
            function (gobiiFileItemAction_1) {
                gobiiFileItemAction = gobiiFileItemAction_1;
            }
        ],
        execute: function () {
            ;
            exports_1("initialState", initialState = {
                fileItemUniqueIdsSelected: [],
                fileItems: [],
            });
            /**
             * Because the data structure is defined within the reducer it is optimal to
             * locate our selector functions at this level. If store is to be thought of
             * as a database, and reducers the tables, selectors can be considered the
             * queries into said database. Remember to keep your selectors small and
             * focused so they can be combined and composed to fit each particular
             * use-case.
             */
            exports_1("getFileItems", getFileItems = function (state) { return state.fileItems; });
            exports_1("getUniqueIds", getUniqueIds = function (state) { return state.fileItems.map(function (fileItem) { return fileItem.getFileItemUniqueId(); }); });
            exports_1("getSelectedUniqueIds", getSelectedUniqueIds = function (state) { return state.fileItemUniqueIdsSelected; });
            exports_1("getSelected", getSelected = reselect_1.createSelector(getFileItems, getSelectedUniqueIds, function (fileItems, selectedUniqueIds) {
                return fileItems.filter(function (fileItem) {
                    selectedUniqueIds.filter(function (uniqueId) { return fileItem.getFileItemUniqueId() === uniqueId; });
                });
            }));
            exports_1("getAll", getAll = reselect_1.createSelector(getFileItems, getUniqueIds, function (entities, ids) {
                return ids.map(function (id) { return entities[id]; });
            }));
        }
    };
});
//# sourceMappingURL=fileitems-reducer.js.map