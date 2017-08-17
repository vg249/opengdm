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


function placeNodeInTree(nodeToPlace: GobiiTreeNode, treeNodes: GobiiTreeNode[], gobiiExtractFilterType: GobiiExtractFilterType): boolean {

    let returnVal: boolean = false;

    for (let idx: number = 0; !returnVal && (idx < treeNodes.length); idx++) {

        let currentTreenode: GobiiTreeNode = treeNodes[idx];
        if (currentTreenode.getGobiiExtractFilterType() === gobiiExtractFilterType &&
            currentTreenode.getItemType() === nodeToPlace.getItemType() &&
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
            returnVal = placeNodeInTree(nodeToPlace, currentTreenode.getChildren(), gobiiExtractFilterType);
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
            if (placeNodeInTree(gobiiTreeNodePayload, newTreeNodes, state.gobiiExtractFilterType)) {

                returnVal = {
                    gobiiExtractFilterType: state.gobiiExtractFilterType,
                    gobiiTreeNodesActive: state.gobiiTreeNodesActive,
                    gobiiTreeNodes: newTreeNodes
                };
            }
            break;
        } // LOAD_TREE_NODE


        case gobiiTreeNodeAction.ACTIVATE: {

            const gobiiTreeNodeToActivate: GobiiTreeNode = action.payload;

            returnVal = {
                gobiiExtractFilterType: state.gobiiExtractFilterType,
                gobiiTreeNodesActive: [...state.gobiiTreeNodesActive, ...[gobiiTreeNodeToActivate.getId()]],
                gobiiTreeNodes: state.gobiiTreeNodes
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

export const getGobiiTreeNodes = (state: State) => state.gobiiTreeNodes;

export const getGobiiTreeItemIds = (state: State) => state.gobiiTreeNodes.map(gti => gti.getId());

export const getIdsOfActivated = (state: State) => state.gobiiTreeNodesActive;

export const getExtractFilterType = (state: State) => state.gobiiExtractFilterType;

export const getSelected = createSelector(getGobiiTreeNodes, getIdsOfActivated, (gobiiTreeNodes, selectedUniqueIds) => {

    // this needs to be done in a more filterish way. For now it works
    let returnVal: GobiiTreeNode[] = [];

    gobiiTreeNodes.forEach(n => {

            selectedUniqueIds.forEach(i => {
                if (n.getId() === i)
                    returnVal.push(n);
            })

        }
    );

    return returnVal;
});

export const getAll = createSelector(getGobiiTreeNodes, getGobiiTreeItemIds, (treeItems, ids) => {
    return ids.map(id => treeItems[id]);
});

export const getForSelectedFilter = createSelector(getGobiiTreeNodes, getExtractFilterType, (treeItems, extractFilterType) => {
    return treeItems.filter(ti => ti.getGobiiExtractFilterType() === extractFilterType);
});