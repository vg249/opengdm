import {Injectable} from "@angular/core";
import {EntitySubType, EntityType} from "../../model/type-entity";
import {Labels} from "../../views/entity-labels";
import {ExtractorItemType} from "../../model/type-extractor-item";
import {GobiiExtractFilterType} from "../../model/type-extractor-filter";
import {CvFilters, CvFilterType} from "../../model/cv-filter-type";
import {GobiiFileItem} from "../../model/gobii-file-item";
import {HeaderStatusMessage} from "../../model/dto-header-status-message";
import {ProcessType} from "../../model/type-process";
import {NameIdService} from "./name-id-service";
import {FileItemParams} from "../../model/name-id-request-params";
import * as historyAction from '../../store/actions/history-action';
import * as fileItemActions from '../../store/actions/fileitem-action'
import * as fromRoot from '../../store/reducers';

import {Store} from "@ngrx/store";
import {NameIdLabelType} from "../../model/name-id-label-type";
import {EntityFilter} from "../../model/type-entity-filter";
import {NameIdFilterParamTypes} from "../../model/type-nameid-filter-params";
import {Observable} from "rxjs/Observable";
import "rxjs/add/operator/expand"
import {EntityStats} from "../../model/entity-stats";
import {DtoRequestService} from "./dto-request.service";
import {DtoRequestItemEntityStats, EntityRequestType} from "../app/dto-request-item-entity-stats";
import {FilterHistory} from "../../store/reducers/history-reducer";

@Injectable()
export class FileItemService {

    fileFilterParams: FileItemParams[] = [];
    private addFilter(filterParamsToAdd:FileItemParams) {
        
        let existingFilterParams = this.fileFilterParams
            .find(ffp =>
                ffp.getQueryName() === filterParamsToAdd.getQueryName()
                && ffp.getGobiiExtractFilterType() === filterParamsToAdd.getGobiiExtractFilterType()
            );
        
        if( !existingFilterParams) {
            this.fileFilterParams.push(filterParamsToAdd);
        } else {
            this.store.dispatch(new historyAction.AddStatusMessageAction("The query "
                + filterParamsToAdd.getQueryName()
                + " because there is already a filter by that name for this extract type "
                + GobiiExtractFilterType[filterParamsToAdd.getQueryName()]));
            
        }
    }

    private getFilter(nameIdFilterParamTypes:NameIdFilterParamTypes, gobiiExtractFilterType:GobiiExtractFilterType): FileItemParams {

        return this.fileFilterParams.find(ffp =>
            ffp.getQueryName() === nameIdFilterParamTypes &&
            ffp.getGobiiExtractFilterType() === gobiiExtractFilterType
        )
    }

    constructor(private nameIdService: NameIdService,
                private entityStatsService: DtoRequestService<EntityStats>,
                private store: Store<fromRoot.State>,) {

        // For non-hierarchically filtered request params, we just create them simply
        // as we add them to the flat map
        this.addFilter(
            FileItemParams
                .build(NameIdFilterParamTypes.CV_DATATYPE,
                    GobiiExtractFilterType.BY_SAMPLE,
                    EntityType.CV)
                .setIsDynamicFilterValue(false)
                .setCvFilterType(CvFilterType.DATASET_TYPE)
                .setEntityFilter(EntityFilter.BYTYPENAME)
                .setFkEntityFilterValue(CvFilters.get(CvFilterType.DATASET_TYPE))
                .setNameIdLabelType(NameIdLabelType.SELECT_A)
        );


        this.addFilter(
            FileItemParams
                .build(NameIdFilterParamTypes.CV_DATATYPE,
                    GobiiExtractFilterType.BY_MARKER,
                    EntityType.CV)
                .setIsDynamicFilterValue(false)
                .setCvFilterType(CvFilterType.DATASET_TYPE)
                .setEntityFilter(EntityFilter.BYTYPENAME)
                .setFkEntityFilterValue(CvFilters.get(CvFilterType.DATASET_TYPE))
                .setNameIdLabelType(NameIdLabelType.SELECT_A)
        );

        this.addFilter(
            FileItemParams
                .build(NameIdFilterParamTypes.MAPSETS,
                    GobiiExtractFilterType.WHOLE_DATASET,
                    EntityType.MAPSET)
                .setIsDynamicFilterValue(false)
                .setNameIdLabelType(NameIdLabelType.NO));

        this.addFilter(
            FileItemParams
                .build(NameIdFilterParamTypes.MAPSETS,
                    GobiiExtractFilterType.BY_SAMPLE,
                    EntityType.MAPSET)
                .setIsDynamicFilterValue(false)
                .setNameIdLabelType(NameIdLabelType.NO));

        this.addFilter(
            FileItemParams
                .build(NameIdFilterParamTypes.MAPSETS,
                    GobiiExtractFilterType.BY_MARKER,
                    EntityType.MAPSET)
                .setIsDynamicFilterValue(false)
                .setNameIdLabelType(NameIdLabelType.NO));

        this.addFilter(
            FileItemParams
                .build(NameIdFilterParamTypes.PLATFORMS,
                    GobiiExtractFilterType.BY_SAMPLE,
                    EntityType.PLATFORM)
                .setIsDynamicFilterValue(false)
        );

        this.addFilter(
            FileItemParams
                .build(NameIdFilterParamTypes.PLATFORMS,
                    GobiiExtractFilterType.BY_MARKER,
                    EntityType.PLATFORM)
                .setIsDynamicFilterValue(false)
        );

        this.addFilter(
            FileItemParams
                .build(NameIdFilterParamTypes.MARKER_GROUPS,
                    GobiiExtractFilterType.BY_MARKER,
                    EntityType.MARKER_GROUP)
                .setIsDynamicFilterValue(false)
        );

        this.addFilter(
            FileItemParams
                .build(NameIdFilterParamTypes.PROJECTS,
                    GobiiExtractFilterType.BY_SAMPLE,
                    EntityType.PROJECT)
                .setIsDynamicFilterValue(false)
                .setNameIdLabelType(NameIdLabelType.ALL));

        this.addFilter(
            FileItemParams
            .build(NameIdFilterParamTypes.CONTACT_PI,
                GobiiExtractFilterType.BY_SAMPLE,
                EntityType.CONTACT)
            .setIsDynamicFilterValue(false)
            .setEntitySubType(EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR));



        //for hierarchical items, we need to crate the nameid requests separately from the
        //flat map: they _will_ need to be in the flat map; however, they all need to be
        //useed to set up the filtering hierarchy
        let nameIdRequestParamsContactsPi: FileItemParams = FileItemParams
            .build(NameIdFilterParamTypes.CONTACT_PI,
                GobiiExtractFilterType.WHOLE_DATASET,
                EntityType.CONTACT)
            .setIsDynamicFilterValue(true)
            .setEntitySubType(EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR);


        let nameIdRequestParamsProjectByPiContact: FileItemParams = FileItemParams
            .build(NameIdFilterParamTypes.PROJECTS_BY_CONTACT,
                GobiiExtractFilterType.WHOLE_DATASET,
                EntityType.PROJECT)
            .setIsDynamicFilterValue(true)
            .setEntityFilter(EntityFilter.BYTYPEID);

        let nameIdRequestParamsExperiments: FileItemParams = FileItemParams
            .build(NameIdFilterParamTypes.EXPERIMENTS_BY_PROJECT,
                GobiiExtractFilterType.WHOLE_DATASET,
                EntityType.EXPERIMENT)
            .setIsDynamicFilterValue(true)
            .setEntityFilter(EntityFilter.BYTYPEID);

        let nameIdRequestParamsDatasets: FileItemParams = FileItemParams
            .build(NameIdFilterParamTypes.DATASETS_BY_EXPERIMENT,
                GobiiExtractFilterType.WHOLE_DATASET,
                EntityType.DATASET)
            .setIsDynamicFilterValue(true)
            .setEntityFilter(EntityFilter.BYTYPEID);

        //add the individual requests to the map
        this.addFilter(nameIdRequestParamsContactsPi);
        this.addFilter(nameIdRequestParamsProjectByPiContact);
        this.addFilter(nameIdRequestParamsExperiments);
        this.addFilter(nameIdRequestParamsDatasets);

        //build the parent-child request params graph
        nameIdRequestParamsContactsPi
            .setChildNameIdRequestParams(
                [nameIdRequestParamsProjectByPiContact
                    .setParentFileItemParams(nameIdRequestParamsContactsPi)
                    .setChildNameIdRequestParams([nameIdRequestParamsExperiments
                        .setParentFileItemParams(nameIdRequestParamsProjectByPiContact)
                        .setChildNameIdRequestParams([nameIdRequestParamsDatasets
                            .setParentFileItemParams(nameIdRequestParamsExperiments)
                        ])
                    ])
                ]);

    } // constructor

    public getForFilter(nameIdFilterParamTypes: NameIdFilterParamTypes): Observable<GobiiFileItem[]> {

        //Wrapping an Observable around the select functions just doesn't work. So at leaset this
        //function can encapsualte getting the correct selector for the filter
        let returnVal: Observable<GobiiFileItem[]>;

        switch (nameIdFilterParamTypes) {

            case NameIdFilterParamTypes.MARKER_GROUPS:
                returnVal = this.store.select(fromRoot.getMarkerGroups);
                break;

            case NameIdFilterParamTypes.PROJECTS:
                returnVal = this.store.select(fromRoot.getProjects);
                break;

            case NameIdFilterParamTypes.PROJECTS_BY_CONTACT:
                returnVal = this.store.select(fromRoot.getProjectsByPI);
                break;

            case NameIdFilterParamTypes.EXPERIMENTS_BY_PROJECT:
                returnVal = this.store.select(fromRoot.getExperimentsByProject);
                break;

            case NameIdFilterParamTypes.EXPERIMENTS:
                returnVal = this.store.select(fromRoot.getExperiments);
                break;

            case NameIdFilterParamTypes.DATASETS_BY_EXPERIMENT:
                returnVal = this.store.select(fromRoot.getDatasetsByExperiment);
                break;

            case NameIdFilterParamTypes.PLATFORMS:
                returnVal = this.store.select(fromRoot.getPlatforms);
                break;

            case NameIdFilterParamTypes.CV_DATATYPE:
                returnVal = this.store.select(fromRoot.getCvTermsDataType);
                break;

            case NameIdFilterParamTypes.MAPSETS:
                returnVal = this.store.select(fromRoot.getMapsets);
                break;

            case NameIdFilterParamTypes.CONTACT_PI:
                returnVal = this.store.select(fromRoot.getPiContacts);
                break;


            default:
                returnVal = this.store.select(fromRoot.getAllFileItems);
                break;

        }

        return returnVal;

        // THIS IS THE THING THAT DIDN'T WORK; IT'S WORTH KEEPING IT AROUND FOR REFERENCE.
        // return Observable.create(observer => {
        //     let filteredItems: GobiiFileItem[] = [];
        //     this.store
        //         .select(fromRoot.getAllFileItems)
        //         .subscribe(fileItems => {
        //                 let nameIdRequestParams: FileItemParams = this.nameIdRequestParams.get(nameIdFilterParamTypes);
        //                 if (nameIdRequestParams) {
        //                     if (!nameIdRequestParams.getIsDynamicFilterValue()) {
        //                         filteredItems = fileItems.filter(fi => fi.compoundIdeEquals(nameIdRequestParams))
        //                     } else {
        //                         this.store.select(fromRoot.getFileItemsFilters)
        //                             .subscribe(filters => {
        //                                 if (filters[nameIdRequestParams.getQueryName()]) {
        //                                     let filterValue: string = filters[nameIdRequestParams.getQueryName()].filterValue;
        //                                     filteredItems = fileItems.filter(
        //                                         fi =>
        //                                             fi.compoundIdeEquals(nameIdRequestParams)
        //                                             && fi.getParentItemId() === filterValue);
        //
        //                                     if (filteredItems.length <= 0) {
        //                                         filteredItems = fileItems.filter(e =>
        //                                             ( e.getExtractorItemType() === ExtractorItemType.ENTITY
        //                                                 && e.getEntityType() === EntityType.DATASET
        //                                                 //                    && e.getParentItemId() === experimentId
        //                                                 && e.getProcessType() === ProcessType.DUMMY))
        //                                             .map(fi => fi);
        //
        //                                     }
        //                                     observer.next(filteredItems)
        //                                 } // if filters have been populated
        //                             });
        //
        //                     }
        //                 }
        //             } //Store.subscribe
        //         );
        //
        //
        //
        // }) //Observable.create()

    }


    public setItemLabelType(gobiiExtractFilterType: GobiiExtractFilterType,
                            nameIdFilterParamTypes: NameIdFilterParamTypes,
                            nameIdLabelType: NameIdLabelType) {

        let nameIdRequestParamsFromType: FileItemParams = this.getFilter(nameIdFilterParamTypes,gobiiExtractFilterType);
        if( nameIdRequestParamsFromType ) {
            nameIdRequestParamsFromType.setNameIdLabelType(nameIdLabelType);
        } else {
            this.store.dispatch(new historyAction.AddStatusMessageAction("Error setting label type: there is no query params object for query "
                + nameIdFilterParamTypes
                + " with extract filter type "
                + GobiiExtractFilterType[gobiiExtractFilterType]));
        }

    }

    public loadWithFilterParams(gobiiExtractFilterType: GobiiExtractFilterType,
                                nameIdFilterParamTypes: NameIdFilterParamTypes,
                                filterValue: string) {

        let nameIdRequestParamsFromType: FileItemParams = this.getFilter(nameIdFilterParamTypes,gobiiExtractFilterType);

        if (nameIdRequestParamsFromType) {

            this.makeFileLoadActions(gobiiExtractFilterType, nameIdFilterParamTypes, filterValue)
                .subscribe(action => {
                    if (action) {
                        this.store.dispatch(action);
                    }
                });

        } else {
            this.store.dispatch(new historyAction.AddStatusMessageAction("Error loading with filter params: there is no query params object for query "
                + nameIdFilterParamTypes
                + " with extract filter type "
                + GobiiExtractFilterType[gobiiExtractFilterType]));
        }
    }


    public loadFileItem(gobiiFileItem: GobiiFileItem, selectForExtract: boolean) {


        let loadAction: fileItemActions.LoadFileItemtAction = new fileItemActions.LoadFileItemtAction(
            {
                gobiiFileItem: gobiiFileItem,
                selectForExtract: selectForExtract
            }
        );


        this.store.dispatch(loadAction);

    }

    /***
     * This is a hard-repalce: the item is not just removed from the extract, but nuked entirely from
     * the store
     * @param {GobiiFileItem} gobiiFileItem
     * @param {boolean} selectForExtract
     */
    public replaceFileItemByCompoundId(gobiiFileItem: GobiiFileItem,
                                       selectForExtract: boolean) {


        let loadAction: fileItemActions.ReplaceItemOfSameCompoundIdAction = new fileItemActions.ReplaceItemOfSameCompoundIdAction(
            {
                gobiiFileitemToReplaceWith: gobiiFileItem,
                selectForExtract: selectForExtract
            }
        );


        this.store.dispatch(loadAction);

    }

    public unloadFileItemFromExtract(gobiiFileItem: GobiiFileItem) {


        let loadAction: fileItemActions.RemoveFromExtractAction = new fileItemActions.RemoveFromExtractAction(gobiiFileItem);

        this.store.dispatch(loadAction);

    }

    public makeFileLoadActions(gobiiExtractFilterType: GobiiExtractFilterType,
                               nameIdFilterParamTypes: NameIdFilterParamTypes,
                               filterValue: string): Observable<fileItemActions.LoadFileItemListWithFilterAction> {

        let nameIdRequestParamsFromType: FileItemParams = this.getFilter(nameIdFilterParamTypes,gobiiExtractFilterType);

        return this.loadFileItems(gobiiExtractFilterType,
            nameIdRequestParamsFromType,
            filterValue,
            true);
    }

    // public loadFileItemsFromFilter(gobiiExtractFilterType: GobiiExtractFilterType,
    //                                nameIdFilterParamTypes: NameIdFilterParamTypes,
    //                                filterValue: string) {
    //
    //     let nameIdRequestParamsFromType: FileItemParams = this.nameIdRequestParams.get(nameIdFilterParamTypes);
    //
    //     return this.loadFileItems(gobiiExtractFilterType,
    //         nameIdRequestParamsFromType,
    //         filterValue,
    //         false)
    //         .subscribe(action => {
    //             if (action) {
    //                 let listLoadAction: fileItemActions.LoadFileItemListAction = new fileItemActions.LoadFileItemListAction(
    //                     {
    //                         gobiiFileItems: action.payload.gobiiFileItems
    //                     }
    //                 );
    //                 this.store.dispatch(listLoadAction);
    //             }
    //         });
    // }


    private loadFileItems(gobiiExtractFilterType: GobiiExtractFilterType,
                          filterParamsToLoad: FileItemParams,
                          filterValue: string,
                          recurse: boolean): Observable<fileItemActions.LoadFileItemListWithFilterAction> {


        return Observable.create(observer => {

            if (filterParamsToLoad.getIsDynamicFilterValue()) {
                filterParamsToLoad.setFkEntityFilterValue(filterValue);
            }


            /***
             * This approach optimizes switching from one extract type another, when a whole bunch of
             * server requests are made at one type. But the improvement is not as great as I would have liked.
             * In my bench-testing, the total time to switch, for example, from Data Set to Sample Extract is over 800ms
             * the first time, and then 400ms on subsequent requests. Apparently the overhead of each call to lastmodified
             * is enough to diminish the benefits. In the next refactoring of this issue, we probably want to have
             * a lastmodified call that gives the datetime stamps for _all_entities, because I'm sure the setup/teardown of the
             * http request is a lot more expensive, cumulatively, than the extra payload. The trick there is that we'd need to cache the
             * entire set of lastmodified dates in such a way that they would get refreshed at the start of a "transaction,"
             * where a transaction for this purpose is ill-defined. Does it mean when we switch from one extract type to another?
             * There is actually a way to do this when the export type tabs are controlled by the angular router, because for a
             * given navigational path transition we could reset the server last modified dates in the store. For that matter,
             * with that model, the update of the items in the store could be driven from when the collection of lastmodified
             * dates are modified in the store: that is, when you switch from one extract type to another (or some other
             * consequential UI event), you dispatch a fresh collection of lastmodified dates, one per each entity-filter type to
             * the store. There is then an effect from that dispatch action that iterates all the query filters: if the
             * lastmodified for a given filter's entity type has changed, the effect requests a refresh of the filter's items.
             */
            // this.store.select(fromRoot.getFileItemsFilters)
            //     .subscribe(filters => {
            //         let filterState = filters[filterParamsToLoad.getQueryName()];
            //         let lastModifiedDateFromState: Date = null;
            //         if (filterState) {
            //             lastModifiedDateFromState = filterState.entityLasteUpdated;
            //         }

            this.entityStatsService.get(new DtoRequestItemEntityStats(
                EntityRequestType.LasetUpdated,
                filterParamsToLoad.getEntityType(),
                null,
                null))
                .subscribe(entityStats => {

                    this.store.select(fromRoot.getFiltersRetrieved)
                        .subscribe(filterHistoryItems => {

                            let fileHistoryItem: FilterHistory =
                                filterHistoryItems.find(fhi =>
                                    fhi.gobiiExtractFilterType === filterParamsToLoad.getGobiiExtractFilterType()
                                    && fhi.filterId === filterParamsToLoad.getQueryName()
                                    && fhi.filterValue === filterParamsToLoad.getFkEntityFilterValue()
                                );

                            let disregardDateSensitiveQueryingForNow = false;
                            if (disregardDateSensitiveQueryingForNow ||
                                (
                                    (!fileHistoryItem ) ||
                                    ( entityStats.lastModified > fileHistoryItem.entityLasteUpdated)
                                )
                            ) {
                                // Either the data have never been retrieved at all for a given filter value,
                                // or the server-side entity has been updated. So we shall refresh the
                                // data and dispatch both the new filter value and the
                                //BEGIN: nameIdService.get()
                                this.nameIdService.get(filterParamsToLoad)
                                    .subscribe(nameIds => {

                                            let minEntityLastUpdated: Date;
                                            let fileItems: GobiiFileItem[] = [];
                                            if (nameIds && ( nameIds.length > 0 )) {

                                                nameIds.forEach(n => {
                                                    let currentFileItem: GobiiFileItem =
                                                        GobiiFileItem.build(
                                                            gobiiExtractFilterType,
                                                            ProcessType.CREATE)
                                                            .setExtractorItemType(ExtractorItemType.ENTITY)
                                                            .setEntityType(filterParamsToLoad.getEntityType())
                                                            .setEntitySubType(filterParamsToLoad.getEntitySubType())
                                                            .setCvFilterType(filterParamsToLoad.getCvFilterType())
                                                            .setItemId(n.id)
                                                            .setItemName(n.name)
                                                            .setSelected(false)
                                                            .setRequired(false)
                                                            .setParentItemId(filterParamsToLoad.getFkEntityFilterValue());


                                                    fileItems.push(currentFileItem);
                                                });

                                                minEntityLastUpdated = new Date(Math.min.apply(null, nameIds
                                                    .map(nameId => nameId.entityLasetModified)));

                                                let temp: string = "foo";

                                                temp = "bar";

                                                if (filterParamsToLoad.getMameIdLabelType() != NameIdLabelType.UNKNOWN) {

                                                    let entityName: string = "";
                                                    if (filterParamsToLoad.getCvFilterType() !== CvFilterType.UNKNOWN) {
                                                        entityName += Labels.instance().cvFilterNodeLabels[filterParamsToLoad.getCvFilterType()];
                                                    } else if (filterParamsToLoad.getEntitySubType() !== EntitySubType.UNKNOWN) {
                                                        entityName += Labels.instance().entitySubtypeNodeLabels[filterParamsToLoad.getEntitySubType()];
                                                    } else {
                                                        entityName += Labels.instance().entityNodeLabels[filterParamsToLoad.getEntityType()];
                                                    }

                                                    let label: string = "";
                                                    switch (filterParamsToLoad.getMameIdLabelType()) {

                                                        case NameIdLabelType.SELECT_A:
                                                            label = "Select a " + entityName;
                                                            break;

                                                        // we require that these entity labels all be in the singular
                                                        case NameIdLabelType.ALL:
                                                            label = "All " + entityName + "s";
                                                            break;

                                                        case NameIdLabelType.NO:
                                                            label = "No " + entityName;
                                                            break;

                                                        default:
                                                            this.store.dispatch(new historyAction.AddStatusAction(new HeaderStatusMessage("Unknown label type "
                                                                + NameIdLabelType[filterParamsToLoad.getMameIdLabelType()], null, null)));

                                                    }


                                                    let labelFileItem: GobiiFileItem = GobiiFileItem
                                                        .build(gobiiExtractFilterType, ProcessType.CREATE)
                                                        .setEntityType(filterParamsToLoad.getEntityType())
                                                        .setEntitySubType(filterParamsToLoad.getEntitySubType())
                                                        .setCvFilterType(filterParamsToLoad.getCvFilterType())
                                                        .setExtractorItemType(ExtractorItemType.LABEL)
                                                        .setItemName(label)
                                                        .setParentItemId(filterParamsToLoad.getFkEntityFilterValue())
                                                        .setItemId("0");


                                                    fileItems.unshift(labelFileItem);
                                                    //.selectedFileItemId = "0";

                                                }

                                            } else {

                                                let noneFileItem: GobiiFileItem = GobiiFileItem
                                                    .build(gobiiExtractFilterType, ProcessType.DUMMY)
                                                    .setExtractorItemType(ExtractorItemType.ENTITY)
                                                    .setEntityType(filterParamsToLoad.getEntityType())
                                                    .setItemId("0")
                                                    .setItemName("<none>")
                                                    .setParentItemId(filterParamsToLoad.getFkEntityFilterValue());

                                                fileItems.push(noneFileItem);

                                            }// if/else any nameids were retrieved


                                            let loadAction: fileItemActions.LoadFileItemListWithFilterAction = new fileItemActions.LoadFileItemListWithFilterAction(
                                                {
                                                    gobiiFileItems: fileItems,
                                                    filterId: filterParamsToLoad.getQueryName(),
                                                    filter: {
                                                        gobiiExtractFilterType: gobiiExtractFilterType,
                                                        filterValue: filterParamsToLoad.getFkEntityFilterValue(),
                                                        entityLasteUpdated: minEntityLastUpdated
                                                    }
                                                }
                                            );

                                            observer.next(loadAction);

                                            // if there are children, we will load their data as well
                                            if (recurse) {

                                                if (filterParamsToLoad
                                                        .getChildFileItemParams()
                                                        .filter(rqp => rqp.getEntityFilter() === EntityFilter.BYTYPEID)
                                                        .length > 0) {

                                                    let parentId: string = fileItems[0].getItemId();


                                                    for (let idx: number = 0;
                                                         idx < filterParamsToLoad.getChildFileItemParams().length;
                                                         idx++) {
                                                        let rqp: FileItemParams = filterParamsToLoad.getChildFileItemParams()[idx];
                                                        if (rqp.getEntityFilter() === EntityFilter.BYTYPEID) {
                                                            rqp.setFkEntityFilterValue(parentId);

                                                            this.loadFileItems(gobiiExtractFilterType,
                                                                rqp,
                                                                parentId,
                                                                true)
                                                                .subscribe(fileItems => observer.next(fileItems));
                                                        }
                                                    }
                                                    ;
                                                }

                                            } // if we are recursing

                                        }, // Observer=>next
                                        responseHeader => {
                                            this.store.dispatch(new historyAction.AddStatusAction(responseHeader));
                                        }); // subscribe
                            } else {
                                // The data for given filter value exist and do not not need to be
                                // updated. So here we shall dispatch only the new filter value.
                                // The empty gobiiFileItems will amount to a null op.
                                //BEGIN: nameIdService.get()


                                let loadAction: fileItemActions.LoadFilterAction = new fileItemActions.LoadFilterAction(
                                    {
                                        filterId: filterParamsToLoad.getQueryName(),
                                        filter: {
                                            gobiiExtractFilterType: gobiiExtractFilterType,
                                            filterValue: filterParamsToLoad.getFkEntityFilterValue(),
                                            entityLasteUpdated: fileHistoryItem.entityLasteUpdated
                                        }
                                    }
                                );
                                observer.next(loadAction);

                                if (recurse) {
                                    if (filterParamsToLoad
                                            .getChildFileItemParams()
                                            .filter(rqp => rqp.getEntityFilter() === EntityFilter.BYTYPEID)
                                            .length > 0) {


                                        // we need to set the current filter in state, but with respect to
                                        // gobiiFileItems, it should be a null op

                                        //Because we don't have the data freshly from the sever, we shall need
                                        //to get the "parentId" from the file items we have in the store
                                        this.store.select(fromRoot.getAllFileItems)
                                            .subscribe(allFileItems => {

                                                // Get the parent item id from the store;
                                                // however, this will only work if the parent items have been loaded
                                                // already.
                                                let candidateParentFileItems: GobiiFileItem[] =
                                                    allFileItems.filter(fi =>
                                                        filterParamsToLoad.compoundIdeEquals(fi)
                                                        && fi.getParentItemId() === filterParamsToLoad.getFkEntityFilterValue()
                                                    );

                                                let childItemsFilterValue: string = "0";
                                                if (candidateParentFileItems.length > 0) {
                                                    childItemsFilterValue = candidateParentFileItems[0].getItemId();
                                                }

                                                for (let idx: number = 0;
                                                     idx < filterParamsToLoad.getChildFileItemParams().length;
                                                     idx++) {
                                                    let rqp: FileItemParams = filterParamsToLoad.getChildFileItemParams()[idx];
                                                    if (rqp.getEntityFilter() === EntityFilter.BYTYPEID) {
                                                        rqp.setFkEntityFilterValue(childItemsFilterValue);

                                                        this.loadFileItems(gobiiExtractFilterType,
                                                            rqp,
                                                            childItemsFilterValue,
                                                            true)
                                                            .subscribe(fileItems => observer.next(fileItems));
                                                    }
                                                }

                                            }).unsubscribe();//select all file items

                                    } // if we have child filters


                                } // if we are recursing

                            }// if-else we need to refresh from server or rely on what's in the store already
                            //END: nameIdService.get()

                        })
                        .unsubscribe(); // get filter history items


                }); //subscribe get entity stats

//                }).unsubscribe(); // git filter state from file items


        });//return Observer.create

    } // make file items from query

}
