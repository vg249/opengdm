import {Action} from '@ngrx/store';
import {GobiiFileItem} from "../../model/gobii-file-item";
import {FileItemParams} from "../../model/name-id-request-params";
import {GobiiExtractFilterType} from "../../model/type-extractor-filter";
import {NameIdFilterParamTypes} from "../../model/type-nameid-filter-params";

export const LOAD = '[GobiiFileItem] Load';
export const SELECT_FOR_EXTRACT = '[GobiiFileItem] Select';
export const SELECT_FOR_EXTRACT_BY_FILE_ITEM_ID = '[GobiiFileItem] Select File Item Id';
export const DESELECT_FOR_EXTRACT_BY_FILE_ITEM_ID = 'GobiiFileItem] Deselect File Item Id';
export const DESELECT_FOR_EXTRACT = '[GobiiFileItem] DeSelect';
export const SET_ENTITY_FILTER = '[GobiiFileItem] SetEntityFilter';
export const LOAD_FILTERED_ITEMS = '[GobiiFileItem] LoadFilteredItems';
export const DESELECT_ALL = '[GobiiFileItem] DeslectAll';

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

export class LoadFilteredItemsAction implements Action {
    readonly type = LOAD_FILTERED_ITEMS;

    constructor(public payload: {
        gobiiFileItems: GobiiFileItem[],
        filterId: NameIdFilterParamTypes,
        filterValue: string

    }) {
    }
}

export class DeSelectForExtractAction implements Action {
    readonly type = DESELECT_FOR_EXTRACT;

    constructor(public payload: GobiiFileItem) {
    }
}

export class SelectForExtractAction implements Action {
    readonly type = SELECT_FOR_EXTRACT;

    constructor(public payload: GobiiFileItem) {
    }
}

export class SetEntityFilter implements Action {
    readonly type = SET_ENTITY_FILTER;

    constructor(public payload: {
        gobiiExtractFilterType: GobiiExtractFilterType,
        filterId: NameIdFilterParamTypes,
        filterValue: string
    }) {
    }
}

export class SelectByFileItemUniqueId implements Action {
    readonly type = SELECT_FOR_EXTRACT_BY_FILE_ITEM_ID;

    constructor(public payload: string) {
    }
}

export class DeSelectByFileItemUniqueId implements Action {
    readonly type = DESELECT_FOR_EXTRACT_BY_FILE_ITEM_ID;

    constructor(public payload: string) {
    }
}

export class DeSelectAll implements Action {
    readonly type = DESELECT_ALL;

    constructor(public payload: GobiiExtractFilterType) {
    }
}


/**
 * Export a type alias of all actions in this action group
 * so that reducers can easily compose action types
 */
export type All
    = LoadAction
    | LoadFilteredItemsAction
    | SelectForExtractAction
    | DeSelectForExtractAction
    | SetEntityFilter
    | SelectByFileItemUniqueId
    | DeSelectByFileItemUniqueId
    | DeSelectAll;

