import {createSelector} from 'reselect';
import {GobiiFileItem} from "../../model/gobii-file-item";
import * as gobiiHistoryAction from "../actions/history-action";
import {HeaderStatusMessage} from "../../model/dto-header-status-message";


/***
 * By definition, GobiiFileItems are read-only; we only every get them
 * from the server and display them and select them for an extract.
 * CRUD operations on the entities themselves (e.g., Projects, Experiments, etc.)
 * will be handled each in their own fileItemsReducer.
 */
export interface State {
    statusMessages: HeaderStatusMessage[];
    jobSubmissions: Map<string, GobiiFileItem[]>;
};

export const initialState: State = {
    statusMessages: [],
    jobSubmissions: new Map()
};


export function historyReducer(state: State = initialState, action: gobiiHistoryAction.All): State {

    let returnVal: State = state;

    switch (action.type) {

        case gobiiHistoryAction.ADD_STATUS: {

            let newHeaderStatusMessage:HeaderStatusMessage = action.payload;

            let newState = state.statusMessages.slice()
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

    } // switch()

    return returnVal;

}


export const getStatuses = (state: State) => state.statusMessages;
export const getJobSubmissions = (state: State) => state.jobSubmissions;

export const getStatusMessages = createSelector(getStatuses, (statusMessages) => {

    // default
    let returnVal: string[];

    returnVal = statusMessages
        .map(hsm => {
            return hsm.message
        });

    return returnVal;

});

export const getLastMessage = createSelector(getStatuses, (statusMessages) => {

    // default
    let returnVal = "";

    if (statusMessages.length > 0) {
        returnVal = statusMessages[statusMessages.length - 1].message
    }

    return returnVal;

});







