import {Action} from '@ngrx/store';
import {GobiiFileItem} from "../../model/gobii-file-item";

export const LOAD = '[GobiiFileItem] Load';
export const SELECT_FOR_EXTRACT = '[GobiiFileItem] Select';
export const DESELECT_FOR_EXTRACT = '[GobiiFileItem] DeSelect';


/**
 * Every action is comprised of at least a type and an optional
 * payload. Expressing actions as classes enables powerful
 * type checking in fileItemsReducer functions.
 *
 * See Discriminated Unions: https://www.typescriptlang.org/docs/handbook/advanced-types.html#discriminated-unions
 */
export class LoadAction implements Action {
    readonly type = LOAD;

    constructor(public payload: GobiiFileItem[]) {
    }
}


export class DeSelectForExtractAction implements Action {
    readonly type = DESELECT_FOR_EXTRACT;

    constructor(public payload: GobiiFileItem[]) {
    }
}

export class SelectForExtractAction implements Action {
    readonly type = SELECT_FOR_EXTRACT;

    constructor(public payload: GobiiFileItem) {
    }
}


/**
 * Export a type alias of all actions in this action group
 * so that reducers can easily compose action types
 */
export type All
    = LoadAction
    | SelectForExtractAction
    | DeSelectForExtractAction;

