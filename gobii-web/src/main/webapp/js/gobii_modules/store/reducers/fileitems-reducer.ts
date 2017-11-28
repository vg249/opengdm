import {createSelector} from 'reselect';
import {GobiiFileItem} from "../../model/gobii-file-item";
import * as gobiiFileItemAction from "../actions/fileitem-action";
import {ExtractorItemType} from "../../model/type-extractor-item";
import {EntitySubType, EntityType} from "../../model/type-entity";
import {FilterParamNames} from "../../model/file-item-param-names";
import {ProcessType} from "../../model/type-process";
import {Labels} from "../../views/entity-labels";
import {GobiiExtractFilterType} from "../../model/type-extractor-filter";
import {GobiiExtractFormat} from "../../model/type-extract-format";
import {CvFilterType} from "../../model/cv-filter-type";
import {GobiiSampleListType} from "../../model/type-extractor-sample-list";
import {DataSet} from "../../model/dataset";
import {GobiiFileItemCompoundId} from "../../model/gobii-file-item-compound-id";
import {FilterParams} from "../../model/file-item-params";


/***
 * By definition, GobiiFileItems are read-only; we only every get them
 * from the server and display them and select them for an extract.
 * CRUD operations on the entities themselves (e.g., Projects, Experiments, etc.)
 * will be handled each in their own fileItemsReducer.
 */
export interface State {
    gobiiExtractFilterType: GobiiExtractFilterType,
    uniqueIdsOfExtractFileItems: string[];
    allFileItems: GobiiFileItem[] ;
    filters: {
        [id: string]: {
            gobiiExtractFilterType: GobiiExtractFilterType,
            gobiiCompoundUniqueId: GobiiFileItemCompoundId,
            filterValue: string,
            entityLasteUpdated: Date
        }
    };
};

export const initialState: State = {
    gobiiExtractFilterType: GobiiExtractFilterType.UNKNOWN,
    uniqueIdsOfExtractFileItems: [],
    allFileItems: [],
    filters: {}
};

function addToExtractItems(state: State, gobiiFileItem: GobiiFileItem): State {


    if (!state.allFileItems.find(fi => fi.getFileItemUniqueId() === gobiiFileItem.getFileItemUniqueId())) {

        console.log("Item is not in the collection: " + gobiiFileItem.getItemName() + " of type " + Labels.instance().treeExtractorTypeLabels[gobiiFileItem.getExtractorItemType()]);
    }

    gobiiFileItem.setSelected(true);

    let newSelectedUniqueIdsState: string[] = state.uniqueIdsOfExtractFileItems.slice();

    if (!newSelectedUniqueIdsState.find(id => id === gobiiFileItem.getFileItemUniqueId())) {
        newSelectedUniqueIdsState.push(gobiiFileItem.getFileItemUniqueId());
    }


    let returnVal: State = {
        gobiiExtractFilterType: state.gobiiExtractFilterType,
        allFileItems: state.allFileItems,
        uniqueIdsOfExtractFileItems: newSelectedUniqueIdsState,
        filters: state.filters
    };

    return returnVal;
}

function removeFromExtractItems(state: State, gobiiFileItem: GobiiFileItem): State {

    gobiiFileItem.setSelected(false);
    let newSelectedUniqueIdsState: string[] = state.uniqueIdsOfExtractFileItems.slice();

    // if we don't find it, we don't raise an error: the intent was to remove, but it just wasn't there,
    // so the state when this function exits is as expected
    let idx: number = newSelectedUniqueIdsState.findIndex(id => id === gobiiFileItem.getFileItemUniqueId())
    if (idx > -1) {
        newSelectedUniqueIdsState.splice(idx, 1);
    }

    let returnVal: State = {
        gobiiExtractFilterType: state.gobiiExtractFilterType,
        allFileItems: state.allFileItems,
        uniqueIdsOfExtractFileItems: newSelectedUniqueIdsState,
        filters: state.filters
    };

    return returnVal;

}


export function fileItemsReducer(state: State = initialState, action: gobiiFileItemAction.All): State {

    let returnVal: State = state;

    switch (action.type) {


        case gobiiFileItemAction.LOAD_FILE_ITEM: {
            const gobiiFileItemPayloadItem = action.payload.gobiiFileItem;

            let newFileItemsItemsState: GobiiFileItem[] = state.allFileItems.slice();
            const newSelectedFileItemUniqueIdsState: string[] = state.uniqueIdsOfExtractFileItems.slice();


            let existingItem: GobiiFileItem = newFileItemsItemsState.find(stateItem =>
                stateItem.compoundIdeEquals(gobiiFileItemPayloadItem)
                && stateItem.getItemId() === gobiiFileItemPayloadItem.getItemId()
            );

            if (existingItem) {

                let existingSelectedUniqueId: string = newSelectedFileItemUniqueIdsState
                    .find(id => id === existingItem.getFileItemUniqueId());

                if (existingSelectedUniqueId) {
                    newSelectedFileItemUniqueIdsState.splice(newSelectedFileItemUniqueIdsState.indexOf(existingSelectedUniqueId), 1);
                    newSelectedFileItemUniqueIdsState.push(gobiiFileItemPayloadItem.getFileItemUniqueId());
                }

                newFileItemsItemsState = newFileItemsItemsState.filter(fi => fi.getFileItemUniqueId() != existingItem.getFileItemUniqueId());
                newFileItemsItemsState.push(gobiiFileItemPayloadItem)


            } else {
                newFileItemsItemsState.push(gobiiFileItemPayloadItem);
            }

            returnVal = {
                gobiiExtractFilterType: state.gobiiExtractFilterType,
                uniqueIdsOfExtractFileItems: newSelectedFileItemUniqueIdsState,
                allFileItems: newFileItemsItemsState,
                filters: state.filters
            };

            break;
        } // LOAD_FILE_ITEM

        // case gobiiFileItemAction.LOAD_FILTER: {
        //     const filterId = action.payload.filterId.toString();
        //     const filterValue = action.payload.filter;
        //
        //
        //     let newFilterState = Object.assign({}, state.filters);
        //     newFilterState[filterId] = filterValue;
        //
        //
        //     returnVal = {
        //         gobiiExtractFilterType: state.gobiiExtractFilterType,
        //         uniqueIdsOfExtractFileItems: state.uniqueIdsOfExtractFileItems,
        //         allFileItems: state.allFileItems,
        //         filters: newFilterState
        //     };
        //
        //     break;
        // } // LOAD_FILTER

        // case gobiiFileItemAction.LOAD_FILE_ITEM_LIST: {
        //     const gobiiFileItemsPayload = action.payload.gobiiFileItems;
        //
        //     const newGobiiFileItems = gobiiFileItemsPayload.filter(newItem =>
        //         state
        //             .allFileItems
        //             .filter(stateItem =>
        //                 (
        //                     stateItem.getGobiiExtractFilterType() === newItem.getGobiiExtractFilterType() &&
        //                     stateItem.getExtractorItemType() === newItem.getExtractorItemType() &&
        //                     stateItem.getEntityType() === newItem.getEntityType() &&
        //                     stateItem.getEntitySubType() === newItem.getEntitySubType() &&
        //                     stateItem.getCvFilterType() === newItem.getCvFilterType() &&
        //                     stateItem.getItemId() === newItem.getItemId()
        //                 )
        //             ).length === 0
        //     );
        //
        //     returnVal = {
        //         gobiiExtractFilterType: state.gobiiExtractFilterType,
        //         uniqueIdsOfExtractFileItems: state.uniqueIdsOfExtractFileItems,
        //         allFileItems: [...state.allFileItems, ...newGobiiFileItems],
        //         filters: state.filters
        //     };
        //
        //     break;
        // } // LOAD_FILE_ITEM_LIST

        case gobiiFileItemAction.LOAD_FILE_ITEM_LIST_WITH_FILTER: {
            const gobiiFileItemsPayload = action.payload.gobiiFileItems;
            const filterId = action.payload.filterId.toString();
            const filterValue = action.payload.filter;

            const newGobiiFileItems = gobiiFileItemsPayload.filter(newItem =>
                state
                    .allFileItems
                    .filter(stateItem =>
                        (
                            stateItem.getGobiiExtractFilterType() === newItem.getGobiiExtractFilterType() &&
                            stateItem.compoundIdeEquals(newItem) &&
                            stateItem.getItemId() === newItem.getItemId() &&
                            (
                                (stateItem.getEntity() === null && newItem.getEntity() === null)
                                || (
                                    (stateItem.getEntity() !== null && newItem.getEntity() !== null)
                                    && (stateItem.getEntity().id === newItem.getEntity().id)
                                )
                            )
                        )
                    ).length === 0
            );

            let newFilterState = Object.assign({}, state.filters);
            newFilterState[filterId] = filterValue;


            returnVal = {
                gobiiExtractFilterType: state.gobiiExtractFilterType,
                uniqueIdsOfExtractFileItems: state.uniqueIdsOfExtractFileItems,
                allFileItems: [...state.allFileItems, ...newGobiiFileItems],
                filters: newFilterState
            };

            break;
        } // LOAD_FILE_ITEM_LIST_WITH_FILTER

        case gobiiFileItemAction.LOAD_FILTER: {
            const filterId = action.payload.filterId.toString();
            const filterValue = action.payload.filter;

            let newFilterState = Object.assign({}, state.filters);
            newFilterState[filterId] = filterValue;


            returnVal = {
                gobiiExtractFilterType: state.gobiiExtractFilterType,
                uniqueIdsOfExtractFileItems: state.uniqueIdsOfExtractFileItems,
                allFileItems: state.allFileItems,
                filters: newFilterState
            };

            break;
        } // LOAD_FILTER

        case gobiiFileItemAction.ADD_TO_EXTRACT: {

            const gobiiFileItemPayload: GobiiFileItem = action.payload;

            returnVal = addToExtractItems(state, gobiiFileItemPayload);

            break;
        } // ADD_TO_EXTRACT

        case gobiiFileItemAction.ADD_TO_EXTRACT_BY_ITEM_ID: {

            const fileItemUniqueIdPayload: string = action.payload;
            let gobiiFileItem: GobiiFileItem = state
                .allFileItems
                .find(fi => fi.getFileItemUniqueId() === fileItemUniqueIdPayload);

            returnVal = addToExtractItems(state, gobiiFileItem);

            break;

        } //

        case gobiiFileItemAction.REPLACE_IN_EXTRACT_BY_ITEM_ID : {

            let itemCurrentlyInExtract: GobiiFileItem = state
                .allFileItems
                .find(fi => fi.getFileItemUniqueId() === action.payload.itemIdCurrentlyInExtract);

            let itemToReplaceItWith: GobiiFileItem = state
                .allFileItems
                .find(fi => fi.getFileItemUniqueId() === action.payload.itemIdToReplaceItWith);

            let stateAfterRemove: State = removeFromExtractItems(state, itemCurrentlyInExtract);
            returnVal = addToExtractItems(stateAfterRemove, itemToReplaceItWith);

            break;
        }

        case gobiiFileItemAction.REPLACE_ITEM_OF_SAME_COMPOUND_ID: {

            let newItemToAdd: GobiiFileItem = action.payload.gobiiFileitemToReplaceWith;

            let items: GobiiFileItem[] = state.allFileItems
                .filter(fi =>
                    fi.compoundIdeEquals(newItemToAdd)
                );


            let fileItemToReplace: GobiiFileItem = state.allFileItems
                .find(fi => fi.getGobiiExtractFilterType() === newItemToAdd.getGobiiExtractFilterType()
                    && fi.compoundIdeEquals(newItemToAdd));

            let stateWithNewFileItem: State = {
                gobiiExtractFilterType: state.gobiiExtractFilterType,
                allFileItems: state.allFileItems,
                uniqueIdsOfExtractFileItems: state.uniqueIdsOfExtractFileItems,
                filters: state.filters
            };


            // remove existing item if applicable
            if (fileItemToReplace) {
                stateWithNewFileItem.allFileItems =
                    stateWithNewFileItem.allFileItems.filter(fi =>
                        fi.getFileItemUniqueId() !== fileItemToReplace.getFileItemUniqueId());
            }

            // add new item
            stateWithNewFileItem.allFileItems.push(newItemToAdd);

            // now add new item to selection if applicable
            let stateWithItemSelection: State;
            if (action.payload.selectForExtract) {
                if (fileItemToReplace) {
                    let oldItemUnselectedState: State = removeFromExtractItems(stateWithNewFileItem, fileItemToReplace);
                    stateWithItemSelection = addToExtractItems(oldItemUnselectedState, newItemToAdd);
                } else {
                    stateWithItemSelection = addToExtractItems(stateWithNewFileItem, newItemToAdd);
                }
            }

            returnVal = stateWithItemSelection;

            break;
        }

        case gobiiFileItemAction.REMOVE_FROM_EXTRACT: {

            let gobiiFileItemPayload: GobiiFileItem = action.payload;
            returnVal = removeFromExtractItems(state, gobiiFileItemPayload);

            break;
        }


        case gobiiFileItemAction.REMOVE_FROM_EXTRACT_BY_ITEM_ID: {

            const fileItemUniqueIdPayload: string = action.payload;

            let gobiiFileItemPayload: GobiiFileItem = state
                .allFileItems
                .find(fi => fi.getFileItemUniqueId() === fileItemUniqueIdPayload);

            returnVal = removeFromExtractItems(state, gobiiFileItemPayload);

            break;

        }

        case gobiiFileItemAction.REMOVE_ALL_FROM_EXTRACT: {

            // only those not of the same extract filter type should remain selected

            let newFIleItemState = state.allFileItems.slice();

            let itemsToDeselect: GobiiFileItem[] =
                newFIleItemState
                    .filter(fi => fi.getGobiiExtractFilterType() === action.payload);

            itemsToDeselect.forEach(fi => fi.setSelected(false));

            let newSelectedItems: string[] = state.uniqueIdsOfExtractFileItems
                .filter(id => !itemsToDeselect
                    .find(fi => fi.getFileItemUniqueId() === id));

            returnVal = {
                gobiiExtractFilterType: state.gobiiExtractFilterType,
                allFileItems: newFIleItemState,
                uniqueIdsOfExtractFileItems: newSelectedItems,
                filters: state.filters
            };

            break;
        }

        case gobiiFileItemAction.SET_EXTRACT_TYPE: {

            const gobiiExtractFilterType = action.payload.gobiiExtractFilterType;

            returnVal = {
                gobiiExtractFilterType: gobiiExtractFilterType,
                allFileItems: state.allFileItems,
                uniqueIdsOfExtractFileItems: state.uniqueIdsOfExtractFileItems,
                filters: state.filters
            };

            break;

        } //
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
 *
 * WARNING: as of 4.0.2, ngrx/platform  issue # 208: https://github.com/ngrx/platform/issues/208
 * has _not_ been resulved. So selectors _must_ return non-primtive objects. See my comment
 * in export-format-component.ts
 */

export const getGobiiExtractFilterType = (state: State) => state.gobiiExtractFilterType;

export const getFileItems = (state: State) => state.allFileItems.filter(fi =>
    fi.getGobiiExtractFilterType() === state.gobiiExtractFilterType
);

export const getUniqueIds = (state: State) => state.allFileItems.map(fileItem => fileItem.getFileItemUniqueId());

export const getSelectedUniqueIds = (state: State) => state.uniqueIdsOfExtractFileItems;

export const getFilters = (state: State) => state.filters;

export const getSelected = createSelector(getFileItems, getSelectedUniqueIds, (fileItems, selectedUniqueIds) => {

    let returnVal: GobiiFileItem[] = fileItems
        .filter(fileItem =>
            selectedUniqueIds
                .find(uniqueId => fileItem.getFileItemUniqueId() === uniqueId)
        );

    return returnVal;
});

export const getAll = createSelector(getFileItems, getUniqueIds, (entities, ids) => {
    return entities;
});

// ideally, the redux way of selecting data would be for the selection to be based
// on which entity is "selected" The problem is that we have controlls in which the
// entity type is parameterized -- it is not global state


/// ****************** SYNCHRONOUS METHODS


/// **************** GET SELECTED PER ENTITY TYPE


export const getSelectedFileFormat = createSelector(getFileItems, getSelectedUniqueIds, (fileItems, selectedUniqueIds) => {

    // default
    let returnVal = GobiiFileItem
        .build(this.gobiiExtractFilterType, ProcessType.UPDATE)
        .setExtractorItemType(ExtractorItemType.EXPORT_FORMAT)
        .setItemId(GobiiExtractFormat[GobiiExtractFormat.HAPMAP])
        .setItemName(GobiiExtractFormat[GobiiExtractFormat[GobiiExtractFormat.HAPMAP]]);

    let formatItem: GobiiFileItem = fileItems
        .find(fi => fi.getExtractorItemType() === ExtractorItemType.EXPORT_FORMAT
            && undefined !== selectedUniqueIds.find(id => id === fi.getFileItemUniqueId()));

    if (formatItem) {
        returnVal = formatItem;
    }

    return returnVal;

});

export const getSelectedSampleType = createSelector(getFileItems, getSelectedUniqueIds, (fileItems, selectedUniqueIds) => {

    // default
    let returnVal = GobiiFileItem.build(this.gobiiExtractFilterType, ProcessType.CREATE)
        .setExtractorItemType(ExtractorItemType.SAMPLE_LIST_TYPE)
        .setItemName(GobiiSampleListType[GobiiSampleListType.GERMPLASM_NAME])
        .setItemId(GobiiSampleListType[GobiiSampleListType.GERMPLASM_NAME]);

    let formatItem: GobiiFileItem = fileItems
        .find(fi => fi.getExtractorItemType() === ExtractorItemType.SAMPLE_LIST_TYPE
            && undefined !== selectedUniqueIds.find(id => id === fi.getFileItemUniqueId()));

    if (formatItem) {
        returnVal = formatItem;
    }

    return returnVal;

});

export const getJobId = createSelector(getFileItems, getSelectedUniqueIds, (fileItems, selectedUniqueIds) => {

    // default
    let returnVal = GobiiFileItem
        .build(this.gobiiExtractFilterType, ProcessType.UPDATE)
        .setExtractorItemType(ExtractorItemType.JOB_ID)
        .setItemId("UNSET")
        .setItemName("UNSET");

    let jobIdItem: GobiiFileItem = fileItems
        .find(fi => (fi.getExtractorItemType() === ExtractorItemType.JOB_ID)
            && (selectedUniqueIds.indexOf(fi.getFileItemUniqueId()) > -1 ));

    if (jobIdItem) {
        returnVal = jobIdItem;
    }

    return returnVal;

});


export const getUploadFiles = createSelector(getFileItems, getSelectedUniqueIds, (fileItems, selectedUniqueIds) => {

    // default
    let returnVal: GobiiFileItem[] = fileItems
        .filter(fi => fi.getExtractorItemType() === ExtractorItemType.MARKER_FILE
            || fi.getExtractorItemType() === ExtractorItemType.SAMPLE_FILE);


    return returnVal;

});


export const getPiContacts = createSelector(getFileItems, getUniqueIds, (fileItems, ids) => {

    let returnVal: GobiiFileItem[] = fileItems.filter(e =>
        (  e.getExtractorItemType() === ExtractorItemType.ENTITY
            || e.getExtractorItemType() === ExtractorItemType.LABEL )
        && e.getEntityType() === EntityType.CONTACT
        && e.getEntitySubType() === EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR)
        .map(fi => fi);

    return returnVal;
});


export const getProjects = createSelector(getFileItems, getUniqueIds, (fileItems, ids) => {

    return fileItems.filter(e =>
        ( e.getExtractorItemType() === ExtractorItemType.ENTITY
            || e.getExtractorItemType() === ExtractorItemType.LABEL )
        && e.getEntityType() === EntityType.PROJECT)
        .map(fi => fi);
});


export const getExperiments = createSelector(getFileItems, getUniqueIds, (fileItems, ids) => {

    return fileItems.filter(e =>
        ( e.getExtractorItemType() === ExtractorItemType.ENTITY
            || e.getExtractorItemType() === ExtractorItemType.LABEL )
        && e.getEntityType() === EntityType.EXPERIMENT)
        .map(fi => fi);
});


export const getDatasets = createSelector(getFileItems, getUniqueIds, (fileItems, ids) => {

    return fileItems.filter(e =>
        ( e.getExtractorItemType() === ExtractorItemType.ENTITY
            || e.getExtractorItemType() === ExtractorItemType.LABEL )
        && e.getEntityType() === EntityType.DATASET)
        .map(fi => fi);
});


export const getCvTermsDataType = createSelector(getFileItems, getUniqueIds, (fileItems, ids) => {

    let returnVal: GobiiFileItem[] = fileItems.filter(e =>
        ( e.getExtractorItemType() === ExtractorItemType.ENTITY
            || e.getExtractorItemType() === ExtractorItemType.LABEL )
        && e.getEntityType() === EntityType.CV
        && e.getCvFilterType() === CvFilterType.DATASET_TYPE)
        .map(fi => fi);

    return returnVal;
});


export const getCvTermsJobStatus = createSelector(getFileItems, getUniqueIds, (fileItems, ids) => {

    let returnVal: GobiiFileItem[] = fileItems.filter(e =>
        ( e.getExtractorItemType() === ExtractorItemType.ENTITY
            || e.getExtractorItemType() === ExtractorItemType.LABEL )
        && e.getEntityType() === EntityType.CV
        && e.getCvFilterType() === CvFilterType.JOB_STATUS)
        .map(fi => fi);

    return returnVal;
});


export const getMapsets = createSelector(getFileItems, getUniqueIds, (fileItems, ids) => {

    return fileItems.filter(e =>
        ( e.getExtractorItemType() === ExtractorItemType.ENTITY
            || e.getExtractorItemType() === ExtractorItemType.LABEL )
        && e.getEntityType() === EntityType.MAPSET)
        .map(fi => fi);
});


export const getPlatforms = createSelector(getFileItems, getUniqueIds, (fileItems, ids) => {

    return fileItems.filter(e =>
        ( e.getExtractorItemType() === ExtractorItemType.ENTITY
            || e.getExtractorItemType() === ExtractorItemType.LABEL )
        && e.getEntityType() === EntityType.PLATFORM)
        .map(fi => fi);
});


export const getMarkerGroups = createSelector(getFileItems, getUniqueIds, (fileItems, ids) => {

    return fileItems.filter(e =>
        ( e.getExtractorItemType() === ExtractorItemType.ENTITY
            || e.getExtractorItemType() === ExtractorItemType.LABEL )
        && e.getEntityType() === EntityType.MARKER_GROUP)
        .map(fi => fi);
});


// **************** GET SELECTED PER ENTITY TYPE
export const getSelectedPiContacts = createSelector(getFileItems, getUniqueIds, (fileItems, ids) => {

    return fileItems.filter(e =>
        ids.find(id => id === e.getFileItemUniqueId())
        && e.getExtractorItemType() === ExtractorItemType.ENTITY
        && e.getEntityType() === EntityType.CONTACT
        && e.getEntitySubType() === EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR)
        .map(fi => fi);
});


export const getProjectsForSelectedPi = createSelector(getFileItems, getFilters, (fileItems, filters) => {

    let returnVal: GobiiFileItem[] = [];

    if (filters[FilterParamNames.PROJECTS_BY_CONTACT]) {

        let contactId: string = filters[FilterParamNames.PROJECTS_BY_CONTACT].filterValue;
        returnVal = fileItems.filter(e =>
            ( e.getExtractorItemType() === ExtractorItemType.ENTITY )
            && ( e.getEntityType() === EntityType.PROJECT)
            && (e.getParentItemId() === contactId )
            && e.getProcessType() !== ProcessType.DUMMY)
            .map(fi => fi);

        if (returnVal.length <= 0) {
            returnVal = fileItems.filter(e =>
                ( e.getExtractorItemType() === ExtractorItemType.ENTITY )
                && ( e.getEntityType() === EntityType.PROJECT)
                //                && (e.getParentItemId() === contactId )
                && e.getProcessType() === ProcessType.DUMMY)
                .map(fi => fi);
        }
    }

    return returnVal;
});


export const getExperimentsForSelectedProject = createSelector(getFileItems, getFilters, (fileItems, filters) => {

    let returnVal: GobiiFileItem[] = [];

    if (filters[FilterParamNames.EXPERIMENTS_BY_PROJECT]) {

        let projectId: string = filters[FilterParamNames.EXPERIMENTS_BY_PROJECT].filterValue;
        returnVal = fileItems.filter(e =>
            ( e.getExtractorItemType() === ExtractorItemType.ENTITY )
            && ( e.getEntityType() === EntityType.EXPERIMENT)
            && (e.getParentItemId() === projectId )
            && e.getProcessType() !== ProcessType.DUMMY)
            .map(fi => fi);

        if (returnVal.length <= 0) {
            returnVal = fileItems.filter(e =>
                ( e.getExtractorItemType() === ExtractorItemType.ENTITY )
                && ( e.getEntityType() === EntityType.EXPERIMENT)
                //                && (e.getParentItemId() === projectId )
                && e.getProcessType() === ProcessType.DUMMY)
                .map(fi => fi);
        }
    }

    return returnVal;
});


export const getDatasetsForSelectedExperiment = createSelector(getFileItems, getFilters, (fileItems, filters) => {

    let returnVal: GobiiFileItem[] = [];

    if (filters[FilterParamNames.DATASETS_BY_EXPERIMENT]) {

        let experimentId: string = filters[FilterParamNames.DATASETS_BY_EXPERIMENT].filterValue;
        returnVal = fileItems.filter(e =>
            ( e.getExtractorItemType() === ExtractorItemType.ENTITY
                && e.getEntityType() === EntityType.DATASET
                && e.getParentItemId() === experimentId
                && e.getProcessType() !== ProcessType.DUMMY))
            .map(fi => fi);

        if (returnVal.length <= 0) {
            returnVal = fileItems.filter(e =>
                ( e.getExtractorItemType() === ExtractorItemType.ENTITY
                    && e.getEntityType() === EntityType.DATASET
                    //                    && e.getParentItemId() === experimentId
                    && e.getProcessType() === ProcessType.DUMMY))
                .map(fi => fi);
        }

    }

    return returnVal;
});

export const getDatasetEntities = createSelector(getFileItems, getFilters, (fileItems, filters) => {

    let returnVal: GobiiFileItem[];


    let jobStatusFilterParams = filters[FilterParamNames.CV_JOB_STATUS];
    if (
        jobStatusFilterParams
        && jobStatusFilterParams.filterValue != null) {

        let filterValue = filters[FilterParamNames.CV_JOB_STATUS].filterValue;
        returnVal = fileItems
            .filter(fi =>
                (fi.getEntityType() === EntityType.DATASET )
                && fi.hasEntity()
                && fi.getRelatedEntityFilterValue(jobStatusFilterParams.gobiiCompoundUniqueId) === filterValue
            );
//            .map(gfi => gfi.getEntity());

    } else {
        returnVal = fileItems
            .filter(fi => fi.getEntityType() === EntityType.DATASET
                && fi.hasEntity());
//            .map(gfi => gfi.getEntity());
    }


    return returnVal;
});