import {createSelector} from 'reselect';
import {GobiiFileItem} from "../../model/gobii-file-item";
import * as gobiiFileItemAction from "../actions/fileitem-action";


/***
 * By definition, GobiiFileItems are read-only; we only every get them
 * from the server and display them and select them for an extract.
 * CRUD operations on the entities themselves (e.g., Projects, Experiments, etc.)
 * will be handled each in their own fileItemsReducer.
 */
export interface State {
    fileItemUniqueIdsSelected: string[];
    fileItems: GobiiFileItem[] ;
};

export const initialState: State = {
    fileItemUniqueIdsSelected: [],
    fileItems: [],
};

export function fileItemsReducer(state: State = initialState, action: gobiiFileItemAction.All): State {

    let returnVal: State = state;

    switch (action.type) {

        case gobiiFileItemAction.LOAD: {
            const gobiiFileItemsPayload = action.payload;

            const newGobiiFileItems = gobiiFileItemsPayload.filter(newItem =>
                state
                    .fileItems
                    .filter(stateItem =>
                        stateItem.getExtractorItemType() != newItem.getExtractorItemType() &&
                        stateItem.getEntityType() != newItem.getEntityType() &&
                        stateItem.getEntitySubType() != newItem.getEntitySubType() &&
                        stateItem.getItemId() != newItem.getItemId()
                    )
            );

            returnVal = {
                fileItemUniqueIdsSelected: state.fileItemUniqueIdsSelected,
                fileItems: [...state.fileItems, ...newGobiiFileItems]
            };

            break;
        } // LOAD

        // Technically, and according to the ngrx/store example app,
        // it should be possible for different actions to have a different
        // payload type, such that it's possible for a payload to be a single
        // GobiiFileItem rather than an array of. However, I cannot get this
        // to compile properly. See this issue I submitted:
        // https://github.com/ngrx/platform/issues/255
        // For now in the interest of making progress I am using
        // an array type for all action payloads
        case gobiiFileItemAction.SELECT_FOR_EXTRACT: {

            const gobiiFileItemPayload: GobiiFileItem[] = action.payload;
            const selectedUniqueItemIds = gobiiFileItemPayload
                .filter(selectedFileItem =>
                    state
                        .fileItemUniqueIdsSelected
                        .filter(selectedFileItemId => selectedFileItemId !== selectedFileItem.getFileItemUniqueId()))
                .map(selectedFileItem => selectedFileItem.getFileItemUniqueId());

            returnVal = {
                fileItems: state.fileItems,
                fileItemUniqueIdsSelected: [...state.fileItemUniqueIdsSelected, ...selectedUniqueItemIds] // spread syntax
            };

            break;
        } // SELECT_FOR_EXTRACT

        case gobiiFileItemAction.DESELECT_FOR_EXTRACT: {

            const gobiiFileItemPayload: GobiiFileItem[] = action.payload;
            const newSelectedUniqueItemIds = state
                .fileItemUniqueIdsSelected
                .filter(selectedId =>
                    gobiiFileItemPayload.filter(deselectedItem => deselectedItem.getFileItemUniqueId() != selectedId)
                );


            returnVal = {
                fileItems: state.fileItems,
                fileItemUniqueIdsSelected: newSelectedUniqueItemIds
            };

            break;

        } // switch()

    }

    return returnVal;

}

/**
 * Because the data structure is defined within the fileItemsReducer it is optimal to
 * locate our selector functions at this level. If store is to be thought of
 * as a database, and reducers the tables, selectors can be considered the
 * queries into said database. Remember to keep your selectors small and
 * focused so they can be combined and composed to fit each particular
 * use-case.
 */

export const getFileItems = (state: State) => state.fileItems;

export const getUniqueIds = (state: State) => state.fileItems.map(fileItem => fileItem.getFileItemUniqueId());

export const getSelectedUniqueIds = (state: State) => state.fileItemUniqueIdsSelected;

export const getSelected = createSelector(getFileItems, getSelectedUniqueIds, (fileItems, selectedUniqueIds) => {
    return fileItems.filter(fileItem => {
        selectedUniqueIds.filter(uniqueId => fileItem.getFileItemUniqueId() === uniqueId)
    });
});

export const getAll = createSelector(getFileItems, getUniqueIds, (entities, ids) => {
    return ids.map(id => entities[id]);
});
