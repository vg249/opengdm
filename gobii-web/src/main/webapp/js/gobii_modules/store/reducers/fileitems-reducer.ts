import {createSelector} from 'reselect';
import {GobiiFileItem} from "../../model/gobii-file-item";
import * as gobiiFileItemAction from "../actions/fileitem-action";
import {ExtractorItemType} from "../../model/file-model-node";
import {EntitySubType, EntityType} from "../../model/type-entity";
import {NameIdRequestParams} from "../../model/name-id-request-params";
import {NameIdFilterParamTypes} from "../../model/type-nameid-filter-params";
import {ProcessType} from "../../model/type-process";


/***
 * By definition, GobiiFileItems are read-only; we only every get them
 * from the server and display them and select them for an extract.
 * CRUD operations on the entities themselves (e.g., Projects, Experiments, etc.)
 * will be handled each in their own fileItemsReducer.
 */
export interface State {
    fileItemUniqueIdsSelected: string[];
    fileItems: GobiiFileItem[] ;
    filters: { [id: string]: string };
};

export const initialState: State = {
    fileItemUniqueIdsSelected: [],
    fileItems: [],
    filters: {}
};

function selectForExtraction(state: State, gobiiFileItem: GobiiFileItem): State {

    gobiiFileItem.setChecked(true);

    const selectedUniqueItemIds = state
        .fileItems
        .filter(fileItem =>
            gobiiFileItem.getFileItemUniqueId() !== fileItem.getFileItemUniqueId())
        .map(selectedFileItem => selectedFileItem.getFileItemUniqueId());

    let returnVal: State = {
        fileItems: state.fileItems,
        fileItemUniqueIdsSelected: [...state.fileItemUniqueIdsSelected, ...selectedUniqueItemIds],
        filters: state.filters
    };

    return returnVal;
}

function deSelectForExtraction(state: State, gobiiFileItem: GobiiFileItem): State {

    gobiiFileItem.setChecked(false);
    const newSelectedUniqueItemIds = state
        .fileItemUniqueIdsSelected
        .filter(selectedId =>
            gobiiFileItem.getFileItemUniqueId() != selectedId
        );

    let returnVal: State = {
        fileItems: state.fileItems,
        fileItemUniqueIdsSelected: newSelectedUniqueItemIds,
        filters: state.filters
    };

    return returnVal;

}

export function fileItemsReducer(state: State = initialState, action: gobiiFileItemAction.All): State {

    let returnVal: State = state;

    switch (action.type) {

        case gobiiFileItemAction.LOAD: {
            const gobiiFileItemsPayload = action.payload;

            const newGobiiFileItems = gobiiFileItemsPayload.filter(newItem =>
                state
                    .fileItems
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

            returnVal = {
                fileItemUniqueIdsSelected: state.fileItemUniqueIdsSelected,
                fileItems: [...state.fileItems, ...newGobiiFileItems],
                filters: state.filters
            };

            break;
        } // LOAD

        case gobiiFileItemAction.LOAD_FILTERED_ITEMS: {
            const gobiiFileItemsPayload = action.payload.gobiiFileItems;
            const filterId = action.payload.filterId.toString();
            const filterValue = action.payload.filterValue;

            const newGobiiFileItems = gobiiFileItemsPayload.filter(newItem =>
                state
                    .fileItems
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
                fileItemUniqueIdsSelected: state.fileItemUniqueIdsSelected,
                fileItems: [...state.fileItems, ...newGobiiFileItems],
                filters: newFilterState
            };

            break;
        } // LOAD_FILTERED_ITEMS

        case gobiiFileItemAction.SELECT_FOR_EXTRACT: {

            const gobiiFileItemPayload: GobiiFileItem = action.payload;

            returnVal = selectForExtraction(state, gobiiFileItemPayload);
            // gobiiFileItemPayload.setChecked(true);
            //
            // const selectedUniqueItemIds = state
            //     .fileItems
            //     .filter(fileItem =>
            //         gobiiFileItemPayload.getFileItemUniqueId() !== fileItem.getFileItemUniqueId())
            //     .map(selectedFileItem => selectedFileItem.getFileItemUniqueId());
            //
            // returnVal = {
            //     fileItems: state.fileItems,
            //     fileItemUniqueIdsSelected: [...state.fileItemUniqueIdsSelected, ...selectedUniqueItemIds],
            //     filters: state.filters
            // };

            break;
        } // SELECT_FOR_EXTRACT

        case gobiiFileItemAction.DESELECT_FOR_EXTRACT: {


            let gobiiFileItemPayload: GobiiFileItem = action.payload;
            returnVal = deSelectForExtraction(state, gobiiFileItemPayload);


            // gobiiFileItemPayload.setChecked(false);
            // const newSelectedUniqueItemIds = state
            //     .fileItemUniqueIdsSelected
            //     .filter(selectedId =>
            //         gobiiFileItemPayload.getFileItemUniqueId() != selectedId
            //     );
            //
            //
            // returnVal = {
            //     fileItems: state.fileItems,
            //     fileItemUniqueIdsSelected: newSelectedUniqueItemIds,
            //     filters: state.filters
            // };

            break;
        }

        case gobiiFileItemAction.SET_ENTITY_FILTER: {

            const filterId = action.payload.filterId.toString();
            const filterValue = action.payload.filterValue;

            let newFilterState = Object.assign({}, state.filters);
            newFilterState[filterId] = filterValue;


            returnVal = {
                fileItems: state.fileItems,
                fileItemUniqueIdsSelected: state.fileItemUniqueIdsSelected,
                filters: newFilterState
            };

            break;

        } //

        case gobiiFileItemAction.SELECT_FOR_EXTRACT_BY_FILE_ITEM_ID: {

            const fileItemUniqueIdPayload: string = action.payload;
            let gobiiFileItem: GobiiFileItem = state
                .fileItems
                .find(fi => fi.getFileItemUniqueId() === fileItemUniqueIdPayload);

            returnVal = selectForExtraction(state, gobiiFileItem);


            // let newUniqueId: string[] = [];
            // if (!state
            //         .fileItemUniqueIdsSelected
            //         .find(i => i === fileItemUniqueIdPayload)) {
            //     if (state
            //             .fileItems
            //             .find(fi => fi.getFileItemUniqueId() === fileItemUniqueIdPayload)) {
            //         newUniqueId.push(fileItemUniqueIdPayload);
            //     }
            // }
            //
            //
            // returnVal = {
            //     fileItems: state.fileItems,
            //     fileItemUniqueIdsSelected: [...state.fileItemUniqueIdsSelected, ...newUniqueId],
            //     filters: state.filters
            // };

            break;

        } //

        case gobiiFileItemAction.DESELECT_FOR_EXTRACT_BY_FILE_ITEM_ID: {

            const fileItemUniqueIdPayload: string = action.payload;

            let gobiiFileItemPayload: GobiiFileItem = state
                .fileItems
                .find(fi => fi.getFileItemUniqueId() === fileItemUniqueIdPayload);

            returnVal = deSelectForExtraction(state, gobiiFileItemPayload);


            //
            //     gobiiFileItemPayload.setChecked(false);
            // const newSelectedUniqueItemIds = state
            //     .fileItemUniqueIdsSelected
            //     .filter(selectedId =>
            //         gobiiFileItemPayload.getFileItemUniqueId() != selectedId
            //     );
            //
            // returnVal = {
            //     fileItems: state.fileItems,
            //     fileItemUniqueIdsSelected: newSelectedUniqueItemIds,
            //     filters: state.filters
            // };

            break;
        }
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

export const getFilters = (state: State) => state.filters;

export const getSelected = createSelector(getFileItems, getSelectedUniqueIds, (fileItems, selectedUniqueIds) => {
    return fileItems.filter(fileItem => {
        selectedUniqueIds.filter(uniqueId => fileItem.getFileItemUniqueId() === uniqueId)
    });
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

        if(returnVal.length <= 0 ) {
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

        if( returnVal.length <= 0 ) {
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

        if(returnVal.length <= 0 ) {
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





