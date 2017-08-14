import {createSelector} from 'reselect';
import * as gobiiTreeNodeAction from "../actions/treenode-action";
import {GobiiTreeNode} from "../../model/GobiiTreeNode";
import {GobiiExtractFilterType} from "../../model/type-extractor-filter";


export interface State {
    gobiiExtractFilterType: GobiiExtractFilterType;
    gobiiTreeNodesActive: string[];
    gobiiTreeItems: GobiiTreeNode[] ;
};

export const initialState: State = {
    gobiiExtractFilterType: GobiiExtractFilterType.UNKNOWN,
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
                gobiiExtractFilterType: state.gobiiExtractFilterType,
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
                gobiiExtractFilterType: state.gobiiExtractFilterType,
                gobiiTreeNodesActive: gobiigobiiTreeItemsPayload.map( gti => gti.getId()),
                gobiiTreeItems: [...treeItemsToDeactivate, ...gobiigobiiTreeItemsPayload]
            };

            break;

        } // SELECT_FOR_EXTRACT

        case gobiiTreeNodeAction.SELECT_EXTRACT_TYPE: {

            returnVal = {
                gobiiExtractFilterType: action.payload,
                gobiiTreeNodesActive: state.gobiiTreeNodesActive,
                gobiiTreeItems: state.gobiiTreeItems
            };

        }

    }

    return returnVal;

}

export const getGobiiTreeItems = (state: State) => state.gobiiTreeItems;

export const getGobiiTreeItemIds = (state: State) => state.gobiiTreeItems.map(gti => gti.getId());

export const getIdsOfActivated = (state: State) => state.gobiiTreeNodesActive;

export const getExtractFilterType = (state: State ) => state.gobiiExtractFilterType;

export const getSelected = createSelector(getGobiiTreeItems, getIdsOfActivated, (gobiiTreeItems, selectedUniqueIds) => {
    return gobiiTreeItems.filter(gti => {
        selectedUniqueIds.filter(id => gti.getId() === id)
    });
});

export const getAll = createSelector(getGobiiTreeItems, getGobiiTreeItemIds, (treeItems, ids) => {
    return ids.map(id => treeItems[id]);
});

export const getForSelectedFilter = createSelector(getGobiiTreeItems, getExtractFilterType, (treeItems, extractFilterType) => {
    return treeItems.filter( ti => ti.getGobiiExtractFilterType() === extractFilterType );
});