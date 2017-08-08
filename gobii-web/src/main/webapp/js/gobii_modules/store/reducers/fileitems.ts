import { createSelector } from 'reselect';
import * as gobiiFileItem from '../actions/fileitem';
import {GobiiFileItem} from "../../model/gobii-file-item";


/***
 * By definition, GobiiFileItems are read-only; we only every get them
 * from the server and display them and select them for an extract.
 * CRUD operations on the entities themselves (e.g., Projects, Experiments, etc.)
 * will be handled each in their own reducer.
 */
export interface State {
  fileItemIdsAll: string[];
  fileItemIdsSelectedForExtract: string[];
  fileItems: { [id: string]: GobiiFileItem };
};

export const initialState: State = {
  fileItemIdsAll: [],
  fileItemIdsSelectedForExtract: [],
  fileItems: {},
};

export function reducer(state:State = initialState, action: gobiiFileItem.Actions ): State {

  switch (action.type) {

    case gobiiFileItem.FIND_COMPLETE: {
      const gobiiFileItemsPayload = action.payload;
      const newGobiiFileItems = gobiiFileItemsPayload.filter(gobiiFileItem => !state.fileItems[gobiiFileItem.id]);

      const newGobiiFileItemIds = newGobiiFileItems.map(gobiiFileItem => gobiiFileItem.id);
      const newGobiiFileItemEntities = newGobiiFileItems.reduce((fileItems: { [id: string]: GobiiFileItem}, gobiiFileItem: GobiiFileItem) => {
        return Object.assign(fileItems, {
          [gobiiFileItem.id]: gobiiFileItem
        });
      }, {});

      return {
        fileItemIdsAll: [ ...state.fileItemIdsAll, ...newGobiiFileItemIds ],
        fileItems: Object.assign({}, state.fileItems, newGobiiFileItemEntities),
        selectedBookId: state.selectedBookId
      };
    }

    case gobiiFileItem.LOAD: {
      const book = action.payload;

      if (state.fileItemIdsAll.indexOf(book.id) > -1) {
        return state;
      }

      return {
        fileItemIdsAll: [ ...state.fileItemIdsAll, book.id ],
        fileItems: Object.assign({}, state.fileItems, {
          [book.id]: book
        }),
        selectedBookId: state.selectedBookId
      };
    }

    case gobiiFileItem.SELECT: {
      return {
        fileItemIdsAll: state.fileItemIdsAll,
        fileItems: state.fileItems,
        selectedBookId: action.payload
      };
    }

    default: {
      return state;
    }
  }
}

/**
 * Because the data structure is defined within the reducer it is optimal to
 * locate our selector functions at this level. If store is to be thought of
 * as a database, and reducers the tables, selectors can be considered the
 * queries into said database. Remember to keep your selectors small and
 * focused so they can be combined and composed to fit each particular
 * use-case.
 */

export const getEntities = (state: State) => state.fileItems;

export const getIds = (state: State) => state.fileItemIdsAll;

export const getSelectedId = (state: State) => state.selectedBookId;

export const getSelected = createSelector(getEntities, getSelectedId, (entities, selectedId) => {
  return entities[selectedId];
});

export const getAll = createSelector(getEntities, getIds, (entities, ids) => {
  return ids.map(id => entities[id]);
});
