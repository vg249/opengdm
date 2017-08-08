System.register(["reselect", "../actions/fileitem"], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    function reducer(state, action) {
        if (state === void 0) { state = initialState; }
        switch (action.type) {
            case gobiiFileItem.FIND_COMPLETE: {
                var gobiiFileItemsPayload = action.payload;
                var newGobiiFileItems = gobiiFileItemsPayload.filter(function (gobiiFileItem) { return !state.fileItems[gobiiFileItem.id]; });
                var newGobiiFileItemIds = newGobiiFileItems.map(function (gobiiFileItem) { return gobiiFileItem.id; });
                var newGobiiFileItemEntities = newGobiiFileItems.reduce(function (fileItems, gobiiFileItem) {
                    return Object.assign(fileItems, (_a = {},
                        _a[gobiiFileItem.id] = gobiiFileItem,
                        _a));
                    var _a;
                }, {});
                return {
                    fileItemIdsAll: state.fileItemIdsAll.concat(newGobiiFileItemIds),
                    fileItems: Object.assign({}, state.fileItems, newGobiiFileItemEntities),
                    selectedBookId: state.selectedBookId
                };
            }
            case gobiiFileItem.LOAD: {
                var book = action.payload;
                if (state.fileItemIdsAll.indexOf(book.id) > -1) {
                    return state;
                }
                return {
                    fileItemIdsAll: state.fileItemIdsAll.concat([book.id]),
                    fileItems: Object.assign({}, state.fileItems, (_a = {},
                        _a[book.id] = book,
                        _a)),
                    selectedBookId: state.selectedBookId
                };
            }
            case gobiiFileItem.SELECT: {
                return {
                    fileItemIdsAll: state.fileItemIdsAll,
                    fileItems: state.fileItems,
                    selectedBookId: action.payload
                };
            }
            default: {
                return state;
            }
        }
        var _a;
    }
    exports_1("reducer", reducer);
    var reselect_1, gobiiFileItem, initialState, getEntities, getIds, getSelectedId, getSelected, getAll;
    return {
        setters: [
            function (reselect_1_1) {
                reselect_1 = reselect_1_1;
            },
            function (gobiiFileItem_1) {
                gobiiFileItem = gobiiFileItem_1;
            }
        ],
        execute: function () {
            ;
            exports_1("initialState", initialState = {
                fileItemIdsAll: [],
                fileItemIdsSelectedForExtract: [],
                fileItems: {},
            });
            /**
             * Because the data structure is defined within the reducer it is optimal to
             * locate our selector functions at this level. If store is to be thought of
             * as a database, and reducers the tables, selectors can be considered the
             * queries into said database. Remember to keep your selectors small and
             * focused so they can be combined and composed to fit each particular
             * use-case.
             */
            exports_1("getEntities", getEntities = function (state) { return state.fileItems; });
            exports_1("getIds", getIds = function (state) { return state.fileItemIdsAll; });
            exports_1("getSelectedId", getSelectedId = function (state) { return state.selectedBookId; });
            exports_1("getSelected", getSelected = reselect_1.createSelector(getEntities, getSelectedId, function (entities, selectedId) {
                return entities[selectedId];
            }));
            exports_1("getAll", getAll = reselect_1.createSelector(getEntities, getIds, function (entities, ids) {
                return ids.map(function (id) { return entities[id]; });
            }));
        }
    };
});
//# sourceMappingURL=fileitems.js.map