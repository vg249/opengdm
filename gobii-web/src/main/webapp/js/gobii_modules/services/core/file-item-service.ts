import {Injectable} from "@angular/core";
import {ContainerType, GobiiTreeNode} from "../../model/GobiiTreeNode";
import {EntitySubType, EntityType} from "../../model/type-entity";
import {Labels} from "../../views/entity-labels";
import {ExtractorItemType} from "../../model/type-extractor-item";
import {GobiiExtractFilterType} from "../../model/type-extractor-filter";
import {CvFilters, CvFilterType} from "../../model/cv-filter-type";
import {GobiiFileItem} from "../../model/gobii-file-item";
import {HeaderStatusMessage} from "../../model/dto-header-status-message";
import {GobiiExtractFormat} from "../../model/type-extract-format";
import {ProcessType} from "../../model/type-process";
import {NameIdService} from "./name-id-service";
import {FileItemParams} from "../../model/name-id-request-params";
import * as historyAction from '../../store/actions/history-action';
import * as fileItemActions from '../../store/actions/fileitem-action'
import * as fromRoot from '../../store/reducers';

import {Store} from "@ngrx/store";
import {NameIdLabelType} from "../../model/name-id-label-type";
import {NameId} from "../../model/name-id";
import {EntityFilter} from "../../model/type-entity-filter";
import {NameIdFilterParamTypes} from "../../model/type-nameid-filter-params";
import {LoadFileItemListWithFilterAction} from "../../store/actions/fileitem-action";
import {Observable} from "rxjs/Observable";
import "rxjs/add/operator/expand"
import {EntityStats} from "../../model/entity-stats";
import {DtoRequestService} from "./dto-request.service";
import {DtoRequestItemEntityStats, EntityRequestType} from "../app/dto-request-item-entity-stats";

@Injectable()
export class FileItemService {

    nameIdRequestParams: Map<NameIdFilterParamTypes, FileItemParams> =
        new Map<NameIdFilterParamTypes, FileItemParams>();

    constructor(private nameIdService: NameIdService,
                private entityStatsService: DtoRequestService<EntityStats>,
                private store: Store<fromRoot.State>,) {

        // For non-hierarchically filtered request params, we just create them simply
        // as we add them to the flat map
        this.nameIdRequestParams.set(NameIdFilterParamTypes.CV_DATATYPE,
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


        this.nameIdRequestParams.set(NameIdFilterParamTypes.MAPSETS,
            FileItemParams
                .build(NameIdFilterParamTypes.MAPSETS,
                    GobiiExtractFilterType.WHOLE_DATASET,
                    EntityType.MAPSET)
                .setIsDynamicFilterValue(false)
                .setNameIdLabelType(NameIdLabelType.NO));

        this.nameIdRequestParams.set(NameIdFilterParamTypes.PLATFORMS,
            FileItemParams
                .build(NameIdFilterParamTypes.PLATFORMS,
                    GobiiExtractFilterType.WHOLE_DATASET,
                    EntityType.PLATFORM)
                .setIsDynamicFilterValue(false)
        );

        this.nameIdRequestParams.set(NameIdFilterParamTypes.MARKER_GROUPS,
            FileItemParams
                .build(NameIdFilterParamTypes.MARKER_GROUPS,
                    GobiiExtractFilterType.BY_MARKER,
                    EntityType.MARKER_GROUP)
                .setIsDynamicFilterValue(false)
        );

        this.nameIdRequestParams.set(NameIdFilterParamTypes.PROJECTS,
            FileItemParams
                .build(NameIdFilterParamTypes.PROJECTS,
                    GobiiExtractFilterType.BY_SAMPLE,
                    EntityType.PROJECT)
                .setIsDynamicFilterValue(false)
                .setNameIdLabelType(NameIdLabelType.ALL));


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
        this.nameIdRequestParams.set(nameIdRequestParamsContactsPi.getQueryName(), nameIdRequestParamsContactsPi);
        this.nameIdRequestParams.set(nameIdRequestParamsProjectByPiContact.getQueryName(), nameIdRequestParamsProjectByPiContact);
        this.nameIdRequestParams.set(nameIdRequestParamsExperiments.getQueryName(), nameIdRequestParamsExperiments);
        this.nameIdRequestParams.set(nameIdRequestParamsDatasets.getQueryName(), nameIdRequestParamsDatasets);

        //build the parent-child request params graph
        nameIdRequestParamsContactsPi
            .setChildNameIdRequestParams(
                [nameIdRequestParamsProjectByPiContact
                    .setParentNameIdRequestParams(nameIdRequestParamsContactsPi)
                    .setChildNameIdRequestParams([nameIdRequestParamsExperiments
                        .setParentNameIdRequestParams(nameIdRequestParamsProjectByPiContact)
                        .setChildNameIdRequestParams([nameIdRequestParamsDatasets
                            .setParentNameIdRequestParams(nameIdRequestParamsExperiments)
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

        let nameIdRequestParamsFromType: FileItemParams = this.nameIdRequestParams.get(nameIdFilterParamTypes);
        nameIdRequestParamsFromType.setNameIdLabelType(nameIdLabelType);
    }

    public loadWithFilterParams(gobiiExtractFilterType: GobiiExtractFilterType,
                                nameIdFilterParamTypes: NameIdFilterParamTypes,
                                filterValue: string) {

        let nameIdRequestParamsFromType: FileItemParams = this.nameIdRequestParams.get(nameIdFilterParamTypes);

        if (nameIdRequestParamsFromType) {

            this.makeFileLoadActions(gobiiExtractFilterType, nameIdFilterParamTypes, filterValue)
                .subscribe(action => {
                    if (action) {
                        this.store.dispatch(action);
                    }
                });

        } else {
            this.store.dispatch(new historyAction.AddStatusMessageAction("No is no query params object for query "
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

        let nameIdRequestParamsFromType: FileItemParams = this.nameIdRequestParams.get(nameIdFilterParamTypes);

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
                          nameIdRequestParamsToLoad: FileItemParams,
                          filterValue: string,
                          recurse: boolean): Observable<fileItemActions.LoadFileItemListWithFilterAction> {


        return Observable.create(observer => {

            if (nameIdRequestParamsToLoad.getIsDynamicFilterValue()) {
                nameIdRequestParamsToLoad.setFkEntityFilterValue(filterValue);
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
            this.store.select(fromRoot.getFileItemsFilters)
                .subscribe(filters => {
                    let filterState = filters[nameIdRequestParamsToLoad.getQueryName()];
                    let lastModifiedState: Date = null;
                    if (filterState) {
                        lastModifiedState = filterState.entityLasteUpdated;
                    }

                    this.entityStatsService.get(new DtoRequestItemEntityStats(
                        EntityRequestType.LasetUpdated,
                        nameIdRequestParamsToLoad.getEntityType(),
                        null,
                        null))
                        .subscribe(entityStats => {

                            this.store.select(fromRoot.getAllFileItems)
                                .subscribe(fileItems => {

                                    let itemCountForExtractType: number = fileItems
                                        .filter(fi =>
                                            fi.compoundIdeEquals(nameIdRequestParamsToLoad)
                                        ).length;

                                    if ((lastModifiedState === null ) ||
                                        itemCountForExtractType === 0 ||
                                        ( entityStats.lastModified > lastModifiedState)) {
                                        //BEGIN: nameIdService.get()
                                        this.nameIdService.get(nameIdRequestParamsToLoad)
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
                                                                    .setEntityType(nameIdRequestParamsToLoad.getEntityType())
                                                                    .setEntitySubType(nameIdRequestParamsToLoad.getEntitySubType())
                                                                    .setCvFilterType(nameIdRequestParamsToLoad.getCvFilterType())
                                                                    .setItemId(n.id)
                                                                    .setItemName(n.name)
                                                                    .setSelected(false)
                                                                    .setRequired(false)
                                                                    .setParentItemId(nameIdRequestParamsToLoad.getFkEntityFilterValue());


                                                            fileItems.push(currentFileItem);
                                                        });

                                                        minEntityLastUpdated = new Date(Math.min.apply(null, nameIds
                                                            .map(nameId => nameId.entityLasetModified)));

                                                        let temp: string = "foo";

                                                        temp = "bar";

                                                        if (nameIdRequestParamsToLoad.getMameIdLabelType() != NameIdLabelType.UNKNOWN) {

                                                            let entityName: string = "";
                                                            if (nameIdRequestParamsToLoad.getCvFilterType() !== CvFilterType.UNKNOWN) {
                                                                entityName += Labels.instance().cvFilterNodeLabels[nameIdRequestParamsToLoad.getCvFilterType()];
                                                            } else if (nameIdRequestParamsToLoad.getEntitySubType() !== EntitySubType.UNKNOWN) {
                                                                entityName += Labels.instance().entitySubtypeNodeLabels[nameIdRequestParamsToLoad.getEntitySubType()];
                                                            } else {
                                                                entityName += Labels.instance().entityNodeLabels[nameIdRequestParamsToLoad.getEntityType()];
                                                            }

                                                            let label: string = "";
                                                            switch (nameIdRequestParamsToLoad.getMameIdLabelType()) {

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
                                                                        + NameIdLabelType[nameIdRequestParamsToLoad.getMameIdLabelType()], null, null)));

                                                            }


                                                            let labelFileItem: GobiiFileItem = GobiiFileItem
                                                                .build(gobiiExtractFilterType, ProcessType.CREATE)
                                                                .setEntityType(nameIdRequestParamsToLoad.getEntityType())
                                                                .setEntitySubType(nameIdRequestParamsToLoad.getEntitySubType())
                                                                .setCvFilterType(nameIdRequestParamsToLoad.getCvFilterType())
                                                                .setExtractorItemType(ExtractorItemType.LABEL)
                                                                .setItemName(label)
                                                                .setParentItemId(nameIdRequestParamsToLoad.getFkEntityFilterValue())
                                                                .setItemId("0");


                                                            fileItems.unshift(labelFileItem);
                                                            //.selectedFileItemId = "0";

                                                        }

                                                    } else {

                                                        let noneFileItem: GobiiFileItem = GobiiFileItem
                                                            .build(gobiiExtractFilterType, ProcessType.DUMMY)
                                                            .setExtractorItemType(ExtractorItemType.ENTITY)
                                                            .setEntityType(nameIdRequestParamsToLoad.getEntityType())
                                                            .setItemId("0")
                                                            .setItemName("<none>")
                                                            .setParentItemId(nameIdRequestParamsToLoad.getFkEntityFilterValue());

                                                        fileItems.push(noneFileItem);

                                                    }// if/else any nameids were retrieved


                                                    let loadAction: fileItemActions.LoadFileItemListWithFilterAction = new fileItemActions.LoadFileItemListWithFilterAction(
                                                        {
                                                            gobiiFileItems: fileItems,
                                                            filterId: nameIdRequestParamsToLoad.getQueryName(),
                                                            filter: {
                                                                gobiiExtractFilterType: gobiiExtractFilterType,
                                                                filterValue: nameIdRequestParamsToLoad.getFkEntityFilterValue(),
                                                                entityLasteUpdated: minEntityLastUpdated
                                                            }
                                                        }
                                                    );
                                                    observer.next(loadAction);

                                                    // if there are children, we will load their data as well
                                                    if (recurse) {
                                                        if (nameIdRequestParamsToLoad
                                                                .getChildNameIdRequestParams()
                                                                .filter(rqp => rqp.getEntityFilter() === EntityFilter.BYTYPEID)
                                                                .length > 0) {

                                                            let parentId: string = nameIdRequestParamsToLoad.getSelectedItemId();
                                                            if (!parentId) {
                                                                parentId = fileItems[0].getItemId();
                                                            }

                                                            nameIdRequestParamsToLoad
                                                                .getChildNameIdRequestParams()
                                                                .forEach(rqp => {
                                                                    if (rqp.getEntityFilter() === EntityFilter.BYTYPEID) {
                                                                        rqp.setFkEntityFilterValue(parentId);

                                                                        this.loadFileItems(gobiiExtractFilterType,
                                                                            rqp,
                                                                            parentId,
                                                                            true)
                                                                            .subscribe(fileItems => observer.next(fileItems));
                                                                    }
                                                                });
                                                        }

                                                        let bar = "bar";
                                                    } // if we are recursing

                                                }, // Observer=>next
                                                responseHeader => {
                                                    this.store.dispatch(new historyAction.AddStatusAction(responseHeader));
                                                }); // subscribe
                                    } // if we are doing an update of the data
                                    //END: nameIdService.get()


                                })
                                .unsubscribe();


                        }); //subscribe

                }).unsubscribe();


        });//return Observer.create

    } // make file items from query

}
