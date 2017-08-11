import {createSelector} from 'reselect';
import * as gobiiTreeNodeAction from "../actions/treenode-action";
import {GobiiTreeNode} from "../../model/GobiiTreeNode";


export interface State {
    gobiiTreeNodesActive: string[];
    gobiiTreeItems: GobiiTreeNode[] ;
};

export const initialState: State = {
    gobiiTreeNodesActive: [],
    gobiiTreeItems: [],
};

export function gobiiTreeNodesReducer(state: State = initialState, action: gobiiTreeNodeAction.All): State {

    let returnVal: State = state;

    switch (action.type) {

        case gobiiTreeNodeAction.LOAD: {
            const gobiigobiiTreeItemsPayload = action.payload;

            const newGobiigobiiTreeItems = gobiigobiiTreeItemsPayload.filter(newItem =>
                state
                    .gobiiTreeItems
                    .filter(stateItem =>
                        stateItem.getItemType() != newItem.getItemType() &&
                        stateItem.getEntityType() != newItem.getEntityType() &&
                        stateItem.getEntitySubType() != newItem.getEntitySubType() &&
                        stateItem.getCvFilterType() != newItem.getCvFilterType()
                    )
            );

            returnVal = {
                gobiiTreeNodesActive: state.gobiiTreeNodesActive,
                gobiiTreeItems: [...state.gobiiTreeItems, ...newGobiigobiiTreeItems]
            };

            break;
        } // LOAD


        case gobiiTreeNodeAction.ACTIVATE: {

            const gobiigobiiTreeItemsPayload = action.payload;

            const treeItemsToDeactivate = gobiigobiiTreeItemsPayload.filter(newItem =>
                state
                    .gobiiTreeItems
                    .filter(stateItem =>
                        stateItem.getItemType() != newItem.getItemType() &&
                        stateItem.getEntityType() != newItem.getEntityType() &&
                        stateItem.getEntitySubType() != newItem.getEntitySubType() &&
                        stateItem.getCvFilterType() != newItem.getCvFilterType()
                    )
            );

            treeItemsToDeactivate.forEach(gti => gti.setActive(false));
            gobiigobiiTreeItemsPayload.forEach(gti => gti.setActive(true));

            returnVal = {
                gobiiTreeNodesActive: gobiigobiiTreeItemsPayload.map( gti => gti.getId()),
                gobiiTreeItems: [...treeItemsToDeactivate, ...gobiigobiiTreeItemsPayload]
            };

            break;

        } // SELECT_FOR_EXTRACT

    }

    return returnVal;

}

export const getGobiiTreeItems = (state: State) => state.gobiiTreeItems;

export const getIds = (state: State) => state.gobiiTreeItems.map(gti => gti.getId());

export const getIdsOfActivated = (state: State) => state.gobiiTreeNodesActive;

export const getSelected = createSelector(getGobiiTreeItems, getIdsOfActivated, (gobiiTreeItems, selectedUniqueIds) => {
    return gobiiTreeItems.filter(gti => {
        selectedUniqueIds.filter(id => gti.getId() === id)
    });
});

export const getAll = createSelector(getGobiiTreeItems, getIds, (treeItems, ids) => {
    return ids.map(id => treeItems[id]);
});
