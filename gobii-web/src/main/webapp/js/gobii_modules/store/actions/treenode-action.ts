import {Action} from '@ngrx/store';
import {GobiiTreeNode} from "../../model/GobiiTreeNode";
import {GobiiExtractFilterType} from "../../model/type-extractor-filter";

export const LOAD = '[GobiiTreeNode] Load';
export const ACTIVATE = '[GobiiTreeNode] Activate';
export const SELECT_EXTRACT_TYPE = '[GobiiTreeNode] Select Extract Type';


/**
 * Every action is comprised of at least a type and an optional
 * payload. Expressing actions as classes enables powerful
 * type checking in fileItemsReducer functions.
 *
 * See Discriminated Unions: https://www.typescriptlang.org/docs/handbook/advanced-types.html#discriminated-unions
 */
export class LoadAction implements Action {
    readonly type = LOAD;

    constructor(public payload: GobiiTreeNode[]) {
    }
}


export class ActivateForExtractAction implements Action {
    readonly type = ACTIVATE;

    constructor(public payload: GobiiTreeNode[]) {
    }
}

export class SelectExtractType implements Action {
    readonly type = SELECT_EXTRACT_TYPE;

    constructor(public payload: GobiiExtractFilterType) {
    }
}


/**
 * Export a type alias of all actions in this action group
 * so that reducers can easily compose action types
 */
export type All
    = LoadAction
    | ActivateForExtractAction
    | SelectExtractType;

