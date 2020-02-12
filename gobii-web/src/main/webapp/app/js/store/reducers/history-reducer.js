System.register(["reselect", "../actions/history-action", "../../model/dto-header-status-message", "../../model/type-status-level"], function (exports_1, context_1) {
    "use strict";
    var reselect_1, gobiiHistoryAction, dto_header_status_message_1, type_status_level_1, initialState, getStatuses, getJobSubmissions, getFiltersRetrieved, getStatusMessages, getLastMessage;
    var __moduleName = context_1 && context_1.id;
    function historyReducer(state = initialState, action) {
        let returnVal = state;
        switch (action.type) {
            case gobiiHistoryAction.ADD_STATUS: {
                let newHeaderStatusMessage = action.payload;
                let newState = state.statusMessages.slice();
                newState.push(newHeaderStatusMessage);
                returnVal = {
                    statusMessages: newState,
                    jobSubmissions: state.jobSubmissions,
                    filtersRetrieved: state.filtersRetrieved
                };
                break;
            } // ADD_STATUS
            case gobiiHistoryAction.ADD_STATUS_MESSAGE: {
                let newHeaderStatusMessage = new dto_header_status_message_1.HeaderStatusMessage(action.payload, type_status_level_1.StatusLevel.UNKNOWN, "");
                let newState = state.statusMessages.slice();
                newState.push(newHeaderStatusMessage);
                returnVal = {
                    statusMessages: newState,
                    jobSubmissions: state.jobSubmissions,
                    filtersRetrieved: state.filtersRetrieved
                };
                break;
            }
            case gobiiHistoryAction.ADD_FILTER_RETRIEVED: {
                let historyPayload = action.payload;
                let newState = state.filtersRetrieved.slice();
                let idx = newState.findIndex(fr => fr.gobiiExtractFilterType === historyPayload.gobiiExtractFilterType
                    && fr.filterId === historyPayload.filterId
                    && fr.filterValue === historyPayload.filterValue);
                if (idx > -1) {
                    newState.splice(idx, 1);
                }
                newState.push(historyPayload);
                returnVal = {
                    statusMessages: state.statusMessages,
                    jobSubmissions: state.jobSubmissions,
                    filtersRetrieved: newState
                };
                break;
            }
            case gobiiHistoryAction.CLEAR_STATUS: {
                returnVal = {
                    statusMessages: [],
                    jobSubmissions: state.jobSubmissions,
                    filtersRetrieved: state.filtersRetrieved
                };
                break;
            } // ADD_STATUS
        } // switch()
        return returnVal;
    }
    exports_1("historyReducer", historyReducer);
    return {
        setters: [
            function (reselect_1_1) {
                reselect_1 = reselect_1_1;
            },
            function (gobiiHistoryAction_1) {
                gobiiHistoryAction = gobiiHistoryAction_1;
            },
            function (dto_header_status_message_1_1) {
                dto_header_status_message_1 = dto_header_status_message_1_1;
            },
            function (type_status_level_1_1) {
                type_status_level_1 = type_status_level_1_1;
            }
        ],
        execute: function () {
            ;
            exports_1("initialState", initialState = {
                statusMessages: [],
                jobSubmissions: new Map(),
                filtersRetrieved: []
            });
            exports_1("getStatuses", getStatuses = (state) => state.statusMessages);
            exports_1("getJobSubmissions", getJobSubmissions = (state) => state.jobSubmissions);
            exports_1("getFiltersRetrieved", getFiltersRetrieved = (state) => state.filtersRetrieved);
            exports_1("getStatusMessages", getStatusMessages = reselect_1.createSelector(getStatuses, (statusMessages) => {
                // default
                let returnVal;
                returnVal = statusMessages
                    .map(hsm => {
                    return hsm.message;
                });
                return returnVal;
            }));
            exports_1("getLastMessage", getLastMessage = reselect_1.createSelector(getStatuses, (statusMessages) => {
                // default
                let returnVal = "";
                if (statusMessages.length > 0) {
                    returnVal = statusMessages[statusMessages.length - 1].message;
                }
                return returnVal;
            }));
        }
    };
});
//# sourceMappingURL=history-reducer.js.map