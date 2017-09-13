import {createSelector} from 'reselect';
import {GobiiFileItem} from "../../model/gobii-file-item";
import * as gobiiFileItemAction from "../actions/fileitem-action";
import {ExtractorItemType} from "../../model/file-model-node";
import {EntitySubType, EntityType} from "../../model/type-entity";
import {FileItemParams} from "../../model/name-id-request-params";
import {NameIdFilterParamTypes} from "../../model/type-nameid-filter-params";
import {ProcessType} from "../../model/type-process";
import {getForSelectedFilter} from "./treenode-reducer";


/***
 * By definition, GobiiFileItems are read-only; we only every get them
 * from the server and display them and select them for an extract.
 * CRUD operations on the entities themselves (e.g., Projects, Experiments, etc.)
 * will be handled each in their own fileItemsReducer.
 */
export interface State {
    uniqueIdsOfExtractFileItems: string[];
    allFileItems: GobiiFileItem[] ;
    filters: { [id: string]: string };
};

export const initialState: State = {
    uniqueIdsOfExtractFileItems: [],
    allFileItems: [],
    filters: {}
};

function addToExtractItems(state: State, gobiiFileItem: GobiiFileItem): State {

    gobiiFileItem.setSelected(true);

    let newSelectedUniqueIdsState: string[] = state.uniqueIdsOfExtractFileItems.slice();

    if (!newSelectedUniqueIdsState.find(id => id === gobiiFileItem.getFileItemUniqueId())) {
        newSelectedUniqueIdsState.push(gobiiFileItem.getFileItemUniqueId());
    }


    let returnVal: State = {
        allFileItems: state.allFileItems,
        uniqueIdsOfExtractFileItems: newSelectedUniqueIdsState,
        filters: state.filters
    };

    return returnVal;
}

function removeFromExtractItems(state: State, gobiiFileItem: GobiiFileItem): State {

    gobiiFileItem.setSelected(false);
    let newSelectedUniqueIdsState: string[] = state.uniqueIdsOfExtractFileItems.slice();

    let idx: number = newSelectedUniqueIdsState.findIndex(id => id === gobiiFileItem.getFileItemUniqueId())
    if (idx) {
        newSelectedUniqueIdsState.splice(idx, 1);
    }

    let returnVal: State = {
        allFileItems: state.allFileItems,
        uniqueIdsOfExtractFileItems: newSelectedUniqueIdsState,
        filters: state.filters
    };

    return returnVal;

}

export function fileItemsReducer(state: State = initialState, action: gobiiFileItemAction.All): State {

    let returnVal: State = state;

    switch (action.type) {

        case gobiiFileItemAction.LOAD_FILE_ITEMS: {
            const gobiiFileItemsPayload = action.payload.gobiiFileItems;
            const filterId = action.payload.filterId.toString();
            const filterValue = action.payload.filterValue;

            const newGobiiFileItems = gobiiFileItemsPayload.filter(newItem =>
                state
                    .allFileItems
                    .filter(stateItem =>
                        (
                            stateItem.getExtractorItemType() === newItem.getExtractorItemType() &&
                            stateItem.getEntityType() === newItem.getEntityType() &&
                            stateItem.getEntitySubType() === newItem.getEntitySubType() &&
                            stateItem.getCvFilterType() === newItem.getCvFilterType() &&
                            stateItem.getItemId() === newItem.getItemId()
                        )
                    ).length === 0
            );

            let newFilterState = Object.assign({}, state.filters);
            newFilterState[filterId] = filterValue;


            returnVal = {
                uniqueIdsOfExtractFileItems: state.uniqueIdsOfExtractFileItems,
                allFileItems: [...state.allFileItems, ...newGobiiFileItems],
                filters: newFilterState
            };

            break;
        } // LOAD_FILE_ITEMS

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
                allFileItems: newFIleItemState,
                uniqueIdsOfExtractFileItems: newSelectedItems,
                filters: state.filters
            };

            break;
        }

        case gobiiFileItemAction.SET_FILTER_VALUE: {

            const filterId = action.payload.filterId.toString();
            const filterValue = action.payload.filterValue;

            let newFilterState = Object.assign({}, state.filters);
            newFilterState[filterId] = filterValue;


            returnVal = {
                allFileItems: state.allFileItems,
                uniqueIdsOfExtractFileItems: state.uniqueIdsOfExtractFileItems,
                filters: newFilterState
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
 */

export const getFileItems = (state: State) => state.allFileItems;

export const getUniqueIds = (state: State) => state.allFileItems.map(fileItem => fileItem.getFileItemUniqueId());

export const getSelectedUniqueIds = (state: State) => state.uniqueIdsOfExtractFileItems;

export const getFilters = (state: State) => state.filters;

export const getSelected = createSelector(getFileItems, getSelectedUniqueIds, (fileItems, selectedUniqueIds) => {

   let returnVal:GobiiFileItem[] = fileItems
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


export const getContacts = createSelector(getFileItems, getUniqueIds, (fileItems, ids) => {

    return fileItems.filter(e =>
        ( e.getExtractorItemType() === ExtractorItemType.ENTITY
            || e.getExtractorItemType() === ExtractorItemType.LABEL )
        && e.getEntityType() === EntityType.Contacts)
        .map(fi => fi);
});


export const getFirstContact = createSelector(getContacts, (contacts) => {
    return contacts[0];
});

export const getProjects = createSelector(getFileItems, getUniqueIds, (fileItems, ids) => {

    return fileItems.filter(e =>
        ( e.getExtractorItemType() === ExtractorItemType.ENTITY
            || e.getExtractorItemType() === ExtractorItemType.LABEL )
        && e.getEntityType() === EntityType.Projects)
        .map(fi => fi);
});

export const getFirstProject = createSelector(getProjects, (projects) => {
    return projects[0];
});


export const getExperiments = createSelector(getFileItems, getUniqueIds, (fileItems, ids) => {

    return fileItems.filter(e =>
        ( e.getExtractorItemType() === ExtractorItemType.ENTITY
            || e.getExtractorItemType() === ExtractorItemType.LABEL )
        && e.getEntityType() === EntityType.Experiments)
        .map(fi => fi);
});

export const getFirstExperiment = createSelector(getExperiments, (experiments) => {
    return experiments[0];
});


export const getDatasets = createSelector(getFileItems, getUniqueIds, (fileItems, ids) => {

    return fileItems.filter(e =>
        ( e.getExtractorItemType() === ExtractorItemType.ENTITY
            || e.getExtractorItemType() === ExtractorItemType.LABEL )
        && e.getEntityType() === EntityType.DataSets)
        .map(fi => fi);
});


export const getFirstDataset = createSelector(getDatasets, (datasets) => {
    return datasets[0];
});


export const getCvTerms = createSelector(getFileItems, getUniqueIds, (fileItems, ids) => {

    return fileItems.filter(e =>
        ( e.getExtractorItemType() === ExtractorItemType.ENTITY
            || e.getExtractorItemType() === ExtractorItemType.LABEL )
        && e.getEntityType() === EntityType.CvTerms)
        .map(fi => fi);
});

export const getFirstCvTerm = createSelector(getCvTerms, (cvterms) => {
    return cvterms[0];
});


export const getMapsets = createSelector(getFileItems, getUniqueIds, (fileItems, ids) => {

    return fileItems.filter(e =>
        ( e.getExtractorItemType() === ExtractorItemType.ENTITY
            || e.getExtractorItemType() === ExtractorItemType.LABEL )
        && e.getEntityType() === EntityType.Mapsets)
        .map(fi => fi);
});


export const getFirstmapset = createSelector(getMapsets, (mapsets) => {
    return mapsets[0];
});


export const getPlatforms = createSelector(getFileItems, getUniqueIds, (fileItems, ids) => {

    return fileItems.filter(e =>
        ( e.getExtractorItemType() === ExtractorItemType.ENTITY
            || e.getExtractorItemType() === ExtractorItemType.LABEL )
        && e.getEntityType() === EntityType.Platforms)
        .map(fi => fi);
});

export const getFirstPlatform = createSelector(getCvTerms, (platforms) => {
    return platforms[0];
});


export const getMarkerGroups = createSelector(getFileItems, getUniqueIds, (fileItems, ids) => {

    return fileItems.filter(e =>
        ( e.getExtractorItemType() === ExtractorItemType.ENTITY
            || e.getExtractorItemType() === ExtractorItemType.LABEL )
        && e.getEntityType() === EntityType.MarkerGroups)
        .map(fi => fi);
});

export const getFirstMarkerGroup = createSelector(getCvTerms, (markergroups) => {
    return markergroups[0];
});


/// ****************** SYNCHRONOUS METHODS


/// **************** GET SELECTED PER ENTITY TYPE
export const getSelectedPiContacts = createSelector(getFileItems, getUniqueIds, (fileItems, ids) => {

    return fileItems.filter(e =>
        ids.find(id => id === e.getFileItemUniqueId())
        && e.getExtractorItemType() === ExtractorItemType.ENTITY
        && e.getEntityType() === EntityType.Contacts
        && e.getEntitySubType() === EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR)
        .map(fi => fi);
});


export const getProjectsForSelectedPi = createSelector(getFileItems, getFilters, (fileItems, filters) => {

    let returnVal: GobiiFileItem[] = [];

    if (filters[NameIdFilterParamTypes.PROJECTS_BY_CONTACT]) {

        let contactId: string = filters[NameIdFilterParamTypes.PROJECTS_BY_CONTACT];
        returnVal = fileItems.filter(e =>
            ( e.getExtractorItemType() === ExtractorItemType.ENTITY )
            && ( e.getEntityType() === EntityType.Projects)
            && (e.getParentItemId() === contactId )
            && e.getProcessType() !== ProcessType.DUMMY)
            .map(fi => fi);

        if (returnVal.length <= 0) {
            returnVal = fileItems.filter(e =>
                ( e.getExtractorItemType() === ExtractorItemType.ENTITY )
                && ( e.getEntityType() === EntityType.Projects)
                //                && (e.getParentItemId() === contactId )
                && e.getProcessType() === ProcessType.DUMMY)
                .map(fi => fi);
        }
    }

    return returnVal;
});


export const getExperimentsForSelectedProject = createSelector(getFileItems, getFilters, (fileItems, filters) => {

    let returnVal: GobiiFileItem[] = [];

    if (filters[NameIdFilterParamTypes.EXPERIMENTS_BY_PROJECT]) {

        let projectId: string = filters[NameIdFilterParamTypes.EXPERIMENTS_BY_PROJECT];
        returnVal = fileItems.filter(e =>
            ( e.getExtractorItemType() === ExtractorItemType.ENTITY )
            && ( e.getEntityType() === EntityType.Experiments)
            && (e.getParentItemId() === projectId )
            && e.getProcessType() !== ProcessType.DUMMY)
            .map(fi => fi);

        if (returnVal.length <= 0) {
            returnVal = fileItems.filter(e =>
                ( e.getExtractorItemType() === ExtractorItemType.ENTITY )
                && ( e.getEntityType() === EntityType.Experiments)
                //                && (e.getParentItemId() === projectId )
                && e.getProcessType() === ProcessType.DUMMY)
                .map(fi => fi);
        }
    }

    return returnVal;
});


export const getDatasetsForSelectedExperiment = createSelector(getFileItems, getFilters, (fileItems, filters) => {

    let returnVal: GobiiFileItem[] = [];

    if (filters[NameIdFilterParamTypes.DATASETS_BY_EXPERIMENT]) {

        let experimentId: string = filters[NameIdFilterParamTypes.DATASETS_BY_EXPERIMENT];
        returnVal = fileItems.filter(e =>
            ( e.getExtractorItemType() === ExtractorItemType.ENTITY
                && e.getEntityType() === EntityType.DataSets
                && e.getParentItemId() === experimentId
                && e.getProcessType() !== ProcessType.DUMMY))
            .map(fi => fi);

        if (returnVal.length <= 0) {
            returnVal = fileItems.filter(e =>
                ( e.getExtractorItemType() === ExtractorItemType.ENTITY
                    && e.getEntityType() === EntityType.DataSets
                    //                    && e.getParentItemId() === experimentId
                    && e.getProcessType() === ProcessType.DUMMY))
                .map(fi => fi);
        }

    }

    return returnVal;
});





