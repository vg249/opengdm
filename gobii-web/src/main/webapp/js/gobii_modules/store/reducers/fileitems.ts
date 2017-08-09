import {createSelector} from 'reselect';
import * as gobiiFileItem from '../actions/fileitem';
import {GobiiFileItem} from "../../model/gobii-file-item";
import * as gobiiFileItemAction from "../actions/fileitem";


/***
 * By definition, GobiiFileItems are read-only; we only every get them
 * from the server and display them and select them for an extract.
 * CRUD operations on the entities themselves (e.g., Projects, Experiments, etc.)
 * will be handled each in their own reducer.
 */
export interface State {
    fileItemUniqueIdsSelected: string[];
    fileItems: GobiiFileItem[] ;
};

export const initialState: State = {
    fileItemUniqueIdsSelected: [],
    fileItems: [],
};

export function reducer(state: State = initialState, action: gobiiFileItem.Actions): State {

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

            // const newGobiiFileItemEntities = newGobiiFileItems
            //     .reduce((fileItems: { [id: string]: GobiiFileItem }, gobiiFileItem: GobiiFileItem) => {
            //         return Object.assign(fileItems, {
            //             [gobiiFileItem.getFileItemUniqueId()]: gobiiFileItem
            //         });
            //     }, {});

            returnVal = {
                fileItems: Object.assign({}, state.fileItems, newGobiiFileItems),
                fileItemUniqueIdsSelected: state.fileItemUniqueIdsSelected
            };
        } // LOAD

        case gobiiFileItemAction.SELECT_FOR_EXTRACT: {

            const gobiiFileItemPayload:GobiiFileItem = action.payload;


        }

    } // switch()

    return returnVal;
}

/**
 * Because the data structure is defined within the reducer it is optimal to
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
