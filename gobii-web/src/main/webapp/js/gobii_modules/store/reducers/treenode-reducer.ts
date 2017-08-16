import {createSelector} from 'reselect';
import * as gobiiTreeNodeAction from "../actions/treenode-action";
import {ContainerType, GobiiTreeNode} from "../../model/GobiiTreeNode";
import {GobiiExtractFilterType} from "../../model/type-extractor-filter";


export interface State {
    gobiiExtractFilterType: GobiiExtractFilterType;
    gobiiTreeNodesActive: string[];
    gobiiTreeNodes: GobiiTreeNode[] ;
};

export const initialState: State = {
    gobiiExtractFilterType: GobiiExtractFilterType.UNKNOWN,
    gobiiTreeNodesActive: [],
    gobiiTreeNodes: [],
};


function placeNodeInTree(nodeToPlace: GobiiTreeNode, treeNodes: GobiiTreeNode[]): boolean {

    let returnVal: boolean = false;

    for (let idx: number = 0; !returnVal && (idx < treeNodes.length); idx++) {

        let currentTreenode: GobiiTreeNode = treeNodes[idx];
        if (currentTreenode.getItemType() === nodeToPlace.getItemType() &&
            currentTreenode.getEntityType() === nodeToPlace.getEntityType() &&
            currentTreenode.getEntitySubType() === nodeToPlace.getEntitySubType() &&
            currentTreenode.getCvFilterType() === nodeToPlace.getCvFilterType() &&
            currentTreenode.getContainerType() !== ContainerType.STRUCTURE
        ) {
            if (currentTreenode.getContainerType() === ContainerType.NONE) {
                treeNodes[idx] = nodeToPlace;
            } else if (currentTreenode.getContainerType() === ContainerType.DATA) {
                nodeToPlace.parent = nodeToPlace;
                nodeToPlace.getChildren().push(nodeToPlace);
            }

            returnVal = true;

        } else {
            returnVal = placeNodeInTree(nodeToPlace, currentTreenode.getChildren());
        }
    }

    return returnVal;
}

export function gobiiTreeNodesReducer(state: State = initialState, action: gobiiTreeNodeAction.All): State {

    let returnVal: State = state;

    switch (action.type) {

        case gobiiTreeNodeAction.LOAD_TREE_NODE: {
            const gobiigobiiTreeItemsPayload = action.payload;

            const newGobiigobiiTreeItems = gobiigobiiTreeItemsPayload.filter(newItem =>
                state
                    .gobiiTreeNodes
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
                gobiiTreeNodes: [...state.gobiiTreeNodes, ...newGobiigobiiTreeItems]
            };

            break;
        } // LOAD_TREE_NODE


        case gobiiTreeNodeAction.PLACE_TREE_NODE: {

            const gobiiTreeNodePayload: GobiiTreeNode = action.payload;

            // copy the existing
            const newTreeNodes = state.gobiiTreeNodes.slice();
            if (placeNodeInTree(gobiiTreeNodePayload, newTreeNodes)) {

                returnVal = {
                    gobiiExtractFilterType: state.gobiiExtractFilterType,
                    gobiiTreeNodesActive: state.gobiiTreeNodesActive,
                    gobiiTreeNodes: newTreeNodes
                };
            }
            break;
        } // LOAD_TREE_NODE


        case gobiiTreeNodeAction.ACTIVATE: {

            const gobiigobiiTreeItemsPayload = action.payload;

            const treeItemsToDeactivate = gobiigobiiTreeItemsPayload.filter(newItem =>
                state
                    .gobiiTreeNodes
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
                gobiiTreeNodesActive: gobiigobiiTreeItemsPayload.map(gti => gti.getId()),
                gobiiTreeNodes: [...treeItemsToDeactivate, ...gobiigobiiTreeItemsPayload]
            };

            break;

        } // SELECT_FOR_EXTRACT

        case gobiiTreeNodeAction.SELECT_EXTRACT_TYPE: {

            returnVal = {
                gobiiExtractFilterType: action.payload,
                gobiiTreeNodesActive: state.gobiiTreeNodesActive,
                gobiiTreeNodes: state.gobiiTreeNodes
            };

        }

    }

    return returnVal;

}

export const getGobiiTreeItems = (state: State) => state.gobiiTreeNodes;

export const getGobiiTreeItemIds = (state: State) => state.gobiiTreeNodes.map(gti => gti.getId());

export const getIdsOfActivated = (state: State) => state.gobiiTreeNodesActive;

export const getExtractFilterType = (state: State) => state.gobiiExtractFilterType;

export const getSelected = createSelector(getGobiiTreeItems, getIdsOfActivated, (gobiiTreeItems, selectedUniqueIds) => {
    return gobiiTreeItems.filter(gti => {
        selectedUniqueIds.filter(id => gti.getId() === id)
    });
});

export const getAll = createSelector(getGobiiTreeItems, getGobiiTreeItemIds, (treeItems, ids) => {
    return ids.map(id => treeItems[id]);
});

export const getForSelectedFilter = createSelector(getGobiiTreeItems, getExtractFilterType, (treeItems, extractFilterType) => {
    return treeItems.filter(ti => ti.getGobiiExtractFilterType() === extractFilterType);
});