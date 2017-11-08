import {Action} from '@ngrx/store';
import {GobiiFileItem} from "../../model/gobii-file-item";
import {FileItemParams} from "../../model/name-id-request-params";
import {GobiiExtractFilterType} from "../../model/type-extractor-filter";
import {NameIdFilterParamTypes} from "../../model/type-nameid-filter-params";

export const ADD_TO_EXTRACT = '[GobiiFileItem] Add to Extract';
export const ADD_TO_EXTRACT_BY_ITEM_ID = '[GobiiFileItem] Add to Extract by ID';
export const REPLACE_IN_EXTRACT_BY_ITEM_ID = '[GobiiFileItem] Replace In Extract by ID';
export const REPLACE_ITEM_OF_SAME_COMPOUND_ID = '[GobiiFileItem] Replace In Extract by compound ID';
export const REMOVE_FROM_EXTRACT_BY_ITEM_ID = '[GobiiFileItem] Remove from Extract by ID';
export const REMOVE_FROM_EXTRACT = '[GobiiFileItem] Remove from Extract';
export const SET_EXTRACT_TYPE = '[GobiiFileItem] Set ExtractType';
export const LOAD_FILE_ITEM_LIST = '[GobiiFileItem] Load File Item List';
export const LOAD_FILE_ITEM_LIST_WITH_FILTER = '[GobiiFileItem] Load File Item List With Filter';
export const LOAD_FILE_ITEM = '[GobiiFileItem] Load File Item';
export const LOAD_FILTER = '[GobiiFileItem] Load Filter List';
export const REMOVE_ALL_FROM_EXTRACT = '[GobiiFileItem] Remove all from Extract';

/**
 * Every action is comprised of at least a type and an optional
 * payload. Expressing actions as classes enables powerful
 * type checking in fileItemsReducer functions.
 *
 * See Discriminated Unions: https://www.typescriptlang.org/docs/handbook/advanced-types.html#discriminated-unions
 */
export class LoadFileItemListWithFilterAction implements Action {
    readonly type = LOAD_FILE_ITEM_LIST_WITH_FILTER;

    constructor(public payload: {
        gobiiFileItems: GobiiFileItem[],
        filterId: NameIdFilterParamTypes,
        filter: { gobiiExtractFilterType: GobiiExtractFilterType, filterValue: string, entityLasteUpdated: Date }

    }) {
    }
}


export class LoadFileItemListAction implements Action {
    readonly type = LOAD_FILE_ITEM_LIST;

    constructor(public payload: {
        gobiiFileItems: GobiiFileItem[]
    }) {
    }
}

export class LoadFilterAction implements Action {
    readonly type = LOAD_FILTER;

    constructor(public payload: {
        filterId: NameIdFilterParamTypes,
        filter: { gobiiExtractFilterType: GobiiExtractFilterType, filterValue: string, entityLasteUpdated: Date }
    }) {
    }
}

export class LoadFileItemtAction implements Action {
    readonly type = LOAD_FILE_ITEM;

    constructor(public payload: {
        gobiiFileItem: GobiiFileItem,
        selectForExtract: boolean
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

export class ReplaceInExtractByItemIdAction implements Action {
    readonly type = REPLACE_IN_EXTRACT_BY_ITEM_ID;

    constructor(public payload: {
        gobiiExtractFilterType: GobiiExtractFilterType,
        itemIdCurrentlyInExtract: string,
        itemIdToReplaceItWith: string

    }) {
    }
}

export class ReplaceItemOfSameCompoundIdAction implements Action {
    readonly type = REPLACE_ITEM_OF_SAME_COMPOUND_ID;

    constructor(public payload: {
        gobiiFileitemToReplaceWith: GobiiFileItem,
        selectForExtract: boolean

    }) {
    }
}

export class SetExtractType implements Action {
    readonly type = SET_EXTRACT_TYPE;

    constructor(public payload: {
        gobiiExtractFilterType: GobiiExtractFilterType
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
    = LoadFileItemListWithFilterAction
    | AddToExtractAction
    | RemoveFromExtractAction
    | AddToExtractByItemIdAction
    | RemoveFromExractByItemIdAction
    | RemoveAllFromExtractAction
    | LoadFileItemtAction
    | SetExtractType
    | ReplaceInExtractByItemIdAction
    | ReplaceItemOfSameCompoundIdAction
    | LoadFilterAction
    | LoadFileItemListAction;

