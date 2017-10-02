import {Action} from '@ngrx/store';
import {GobiiFileItem} from "../../model/gobii-file-item";
import {FileItemParams} from "../../model/name-id-request-params";
import {GobiiExtractFilterType} from "../../model/type-extractor-filter";
import {NameIdFilterParamTypes} from "../../model/type-nameid-filter-params";
import {HeaderStatusMessage} from "../../model/dto-header-status-message";

export const ADD_STATUS = '[GobiiStatusMessage] Add Status';
export const CLEAR_STATUS = '[GobiiStatusMessage] Clear Status';

/**
 * Every action is comprised of at least a type and an optional
 * payload. Expressing actions as classes enables powerful
 * type checking in fileItemsReducer functions.
 *
 * See Discriminated Unions: https://www.typescriptlang.org/docs/handbook/advanced-types.html#discriminated-unions
 */
export class AddStatusAction implements Action {
    readonly type = ADD_STATUS;

    constructor(public payload: HeaderStatusMessage) {
    }
}

export class ClearStatusesAction implements Action {
    readonly type = CLEAR_STATUS;

    constructor() {
    }
}


/**
 * Export a type alias of all actions in this action group
 * so that reducers can easily compose action types
 */
export type All
    = AddStatusAction |
    ClearStatusesAction;

