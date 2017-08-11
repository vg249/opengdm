import {Action} from '@ngrx/store';
import {GobiiTreeNode} from "../../model/GobiiTreeNode";

export const LOAD = '[GobiiTreeNode] Load';
export const ACTIVATE = '[GobiiTreeNode] Activate';


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


/**
 * Export a type alias of all actions in this action group
 * so that reducers can easily compose action types
 */
export type All
    = LoadAction
    | ActivateForExtractAction;

