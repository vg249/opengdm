import {Action} from '@ngrx/store';
import {GobiiFileItem} from "../../model/gobii-file-item";
import {FileItemParams} from "../../model/name-id-request-params";
import {GobiiExtractFilterType} from "../../model/type-extractor-filter";
import {NameIdFilterParamTypes} from "../../model/type-nameid-filter-params";

export const ADD_TO_EXTRACT = '[GobiiFileItem] Add to Extract';
export const ADD_TO_EXTRACT_BY_ITEM_ID = '[GobiiFileItem] Add to Extract by ID';
export const REMOVE_FROM_EXTRACT_BY_ITEM_ID = '[GobiiFileItem] Remove from Extract by ID';
export const REMOVE_FROM_EXTRACT = '[GobiiFileItem] Remove from Extract';
export const SET_FILTER_VALUE = '[GobiiFileItem] Set Filter Value';
export const LOAD_FILE_ITEMS = '[GobiiFileItem] Load File Items';
export const REMOVE_ALL_FROM_EXTRACT = '[GobiiFileItem] Remove all from Extract';

/**
 * Every action is comprised of at least a type and an optional
 * payload. Expressing actions as classes enables powerful
 * type checking in fileItemsReducer functions.
 *
 * See Discriminated Unions: https://www.typescriptlang.org/docs/handbook/advanced-types.html#discriminated-unions
 */
export class LoadFileItemsAction implements Action {
    readonly type = LOAD_FILE_ITEMS;

    constructor(public payload: {
        gobiiFileItems: GobiiFileItem[],
        filterId: NameIdFilterParamTypes,
        filterValue: string

    }) {
    }
}

export class RemoveFromExtractAction implements Action {
    readonly type = REMOVE_FROM_EXTRACT;

    constructor(public payload: GobiiFileItem) {
    }
}

export class AddToExtractAction implements Action {
    readonly type = ADD_TO_EXTRACT;

    constructor(public payload: GobiiFileItem) {
    }
}

export class AddToExtractByItemIdAction implements Action {
    readonly type = ADD_TO_EXTRACT_BY_ITEM_ID;

    constructor(public payload: string) {
    }
}


export class SetFilterValueAction implements Action {
    readonly type = SET_FILTER_VALUE;

    constructor(public payload: {
        gobiiExtractFilterType: GobiiExtractFilterType,
        filterId: NameIdFilterParamTypes,
        filterValue: string
    }) {
    }
}


export class RemoveFromExractByItemIdAction implements Action {
    readonly type = REMOVE_FROM_EXTRACT_BY_ITEM_ID;

    constructor(public payload: string) {
    }
}

export class RemoveAllFromExtractAction implements Action {
    readonly type = REMOVE_ALL_FROM_EXTRACT;

    constructor(public payload: GobiiExtractFilterType) {
    }
}


/**
 * Export a type alias of all actions in this action group
 * so that reducers can easily compose action types
 */
export type All
    = LoadFileItemsAction
    | AddToExtractAction
    | RemoveFromExtractAction
    | SetFilterValueAction
    | AddToExtractByItemIdAction
    | RemoveFromExractByItemIdAction
    | RemoveAllFromExtractAction;

