System.register(["reselect", "../actions/history-action", "../../model/dto-header-status-message", "../../model/type-status-level"], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    function historyReducer(state, action) {
        if (state === void 0) { state = initialState; }
        var returnVal = state;
        switch (action.type) {
            case gobiiHistoryAction.ADD_STATUS: {
                var newHeaderStatusMessage = action.payload;
                var newState = state.statusMessages.slice();
                newState.push(newHeaderStatusMessage);
                returnVal = {
                    statusMessages: newState,
                    jobSubmissions: state.jobSubmissions
                };
                break;
            } // ADD_STATUS
            case gobiiHistoryAction.CLEAR_STATUS: {
                returnVal = {
                    statusMessages: [],
                    jobSubmissions: state.jobSubmissions
                };
                break;
            } // ADD_STATUS
            case gobiiHistoryAction.ADD_STATUS_MESSAGE: {
                var newHeaderStatusMessage = new dto_header_status_message_1.HeaderStatusMessage(action.payload, type_status_level_1.StatusLevel.UNKNOWN, "");
                var newState = state.statusMessages.slice();
                newState.push(newHeaderStatusMessage);
                returnVal = {
                    statusMessages: newState,
                    jobSubmissions: state.jobSubmissions
                };
                break;
            }
        } // switch()
        return returnVal;
    }
    exports_1("historyReducer", historyReducer);
    var reselect_1, gobiiHistoryAction, dto_header_status_message_1, type_status_level_1, initialState, getStatuses, getJobSubmissions, getStatusMessages, getLastMessage;
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
                jobSubmissions: new Map()
            });
            exports_1("getStatuses", getStatuses = function (state) { return state.statusMessages; });
            exports_1("getJobSubmissions", getJobSubmissions = function (state) { return state.jobSubmissions; });
            exports_1("getStatusMessages", getStatusMessages = reselect_1.createSelector(getStatuses, function (statusMessages) {
                // default
                var returnVal;
                returnVal = statusMessages
                    .map(function (hsm) {
                    return hsm.message;
                });
                return returnVal;
            }));
            exports_1("getLastMessage", getLastMessage = reselect_1.createSelector(getStatuses, function (statusMessages) {
                // default
                var returnVal = "";
                if (statusMessages.length > 0) {
                    returnVal = statusMessages[statusMessages.length - 1].message;
                }
                return returnVal;
            }));
        }
    };
});
//# sourceMappingURL=history-reducer.js.map