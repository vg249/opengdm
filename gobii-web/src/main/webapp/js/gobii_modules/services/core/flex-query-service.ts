import {Injectable} from "@angular/core";
import {GobiiExtractFilterType} from "../../model/type-extractor-filter";
import * as treeNodeActions from '../../store/actions/treenode-action'
import * as historyAction from '../../store/actions/history-action';
import * as fileItemActions from '../../store/actions/fileitem-action'
import * as fromRoot from '../../store/reducers';

import {Store} from "@ngrx/store";
import {DtoRequestService} from "./dto-request.service";
import {VertexFilterDTO} from "../../model/vertex-filter";
import {EntityFileItemService} from "./entity-file-item-service";
import {FilterParamNames} from "../../model/file-item-param-names";
import {Vertex} from "../../model/vertex";
import {DtoRequestItemVertexFilterDTO} from "../app/dto-request-item-vertex-filter";
import {GobiiFileItem} from "../../model/gobii-file-item";
import {ProcessType} from "../../model/type-process";
import {ExtractorItemType} from "../../model/type-extractor-item";
import {PayloadFilter} from "../../store/actions/action-payload-filter";
import {FilterParamsColl} from "./filter-params-coll";
import {FilterParams} from "../../model/filter-params";
import {GobiiFileItemCompoundId} from "../../model/gobii-file-item-compound-id";
import {EntitySubType, EntityType, entityTypefromString} from "../../model/type-entity";
import {NameIdLabelType} from "../../model/name-id-label-type";
import {CvGroup, getCvGroupName} from "../../model/cv-group";
import {FilterService} from "./filter-service";
import {TreeStructureService} from "./tree-structure-service";
import {GobiiTreeNode} from "../../model/gobii-tree-node";

@Injectable()
export class FlexQueryService {


    constructor(private store: Store<fromRoot.State>,
                private entityFileItemService: EntityFileItemService,
                private dtoRequestServiceVertexFilterDTO: DtoRequestService<VertexFilterDTO>,
                private filterParamsColl: FilterParamsColl,
                private filterService: FilterService,
                private treeStructureService: TreeStructureService) {


    }


    public loadVertices(filterParamNames: FilterParamNames) {

        this.entityFileItemService.loadEntityList(GobiiExtractFilterType.FLEX_QUERY, filterParamNames);

    } // loadVertices()

    public loadSelectedVertexFilter(eventedFilterParamsName: FilterParamNames,
                                    eventedVertexId: string,
                                    eventedEntityType: EntityType,
                                    eventedEntitySubType: EntitySubType,
                                    eventedCvGroup: CvGroup,
                                    eventedCvTerm: string) {


        let currentVertexId: string = eventedVertexId;
        let currentVertexFilterParams: FilterParams = this.filterParamsColl.getFilter(eventedFilterParamsName, GobiiExtractFilterType.FLEX_QUERY);

        // the filterParams passed in should exist
        if (!currentVertexFilterParams) {
            this.store.dispatch(new historyAction.AddStatusMessageAction("Error loading filter: there is no query params object for query "
                + eventedFilterParamsName
                + " with extract filter type "
                + GobiiExtractFilterType.FLEX_QUERY[GobiiExtractFilterType.FLEX_QUERY]));
        }


        let previousTargetCompoundId: GobiiFileItemCompoundId = GobiiFileItemCompoundId
            .fromGobiiFileItemCompoundId(currentVertexFilterParams.getTargetEntityUniqueId());

        let newVertexFilterTargetCompoundId: GobiiFileItemCompoundId = GobiiFileItemCompoundId
            .fromGobiiFileItemCompoundId(currentVertexFilterParams.getTargetEntityUniqueId())
            .setEntityType(eventedEntityType)
            .setEntitySubType(eventedEntitySubType)
            .setCvGroup(eventedCvGroup)
            .setCvTerm(eventedCvTerm);

        let nullTargetCompoundIdOfVertex: GobiiFileItemCompoundId = GobiiFileItemCompoundId
            .fromGobiiFileItemCompoundId(currentVertexFilterParams.getTargetEntityUniqueId())
            .setEntityType(EntityType.UNKNOWN)
            .setEntitySubType(EntitySubType.UNKNOWN)
            .setCvGroup(CvGroup.UNKNOWN)
            .setCvTerm(null);

        let nullTargetCompoundIdOfVertexValue: GobiiFileItemCompoundId = GobiiFileItemCompoundId
            .fromGobiiFileItemCompoundId(nullTargetCompoundIdOfVertex)
            .setExtractorItemType(ExtractorItemType.VERTEX_VALUE);

        while (currentVertexFilterParams) {

            // dispatch target entity ID values for newly selected vertex
            currentVertexFilterParams.setTargetEntityUniqueId(newVertexFilterTargetCompoundId);
            let loadFilterActionForVertex: fileItemActions.LoadFilterAction = new fileItemActions.LoadFilterAction(
                {
                    filterId: currentVertexFilterParams.getQueryName(),
                    filter: new PayloadFilter(
                        GobiiExtractFilterType.FLEX_QUERY,
                        newVertexFilterTargetCompoundId,
                        currentVertexFilterParams.getRelatedEntityUniqueId(),
                        null,
                        currentVertexId,
                        null,
                        null
                    )
                }
            );
            this.store.dispatch(loadFilterActionForVertex);

            // do the same for the filter's tree node
            this.treeStructureService.updateTreeNode(GobiiExtractFilterType.FLEX_QUERY,
                newVertexFilterTargetCompoundId,
                GobiiFileItemCompoundId
                    .fromGobiiFileItemCompoundId(newVertexFilterTargetCompoundId)
                    .setExtractorItemType(ExtractorItemType.VERTEX_VALUE));


            //reset the selected vertex's value list
            if (//!currentVertexId &&
            currentVertexFilterParams.getChildFileItemParams()
            && currentVertexFilterParams.getChildFileItemParams().length > 0) {

                let currentVertexValuesFilterParams: FilterParams = currentVertexFilterParams.getChildFileItemParams()[0];

                // clear any selected nodes from selected items collection and from tree
                this.deSelectVertexValueFilters(GobiiFileItemCompoundId
                    .fromGobiiFileItemCompoundId(previousTargetCompoundId)
                    .setExtractorItemType(ExtractorItemType.VERTEX_VALUE));

                let loadFilterActionForVertexValue: fileItemActions.LoadFilterAction = new fileItemActions.LoadFilterAction(
                    {
                        filterId: currentVertexValuesFilterParams.getQueryName(),
                        filter: new PayloadFilter(
                            GobiiExtractFilterType.FLEX_QUERY,
                            nullTargetCompoundIdOfVertexValue,
                            currentVertexValuesFilterParams.getRelatedEntityUniqueId(),
                            null,
                            null,
                            null,
                            null
                        )
                    }
                );

                this.store.dispatch(loadFilterActionForVertexValue);


            } // if there is a values filter (child filter)


//            if (!currentVertexId) {
            currentVertexFilterParams = currentVertexFilterParams.getNextSiblingFileItemParams();
            if (currentVertexFilterParams) {
                previousTargetCompoundId = currentVertexFilterParams.getTargetEntityUniqueId();
                newVertexFilterTargetCompoundId = GobiiFileItemCompoundId.fromGobiiFileItemCompoundId(nullTargetCompoundIdOfVertex)
                    .setSequenceNum(currentVertexFilterParams.getSequenceNum());
            } // no else necessary because null currentVertexFilterParams will end execution

            currentVertexId = null; // by definition, we always null out the children
            // } else {
            //     currentFilterParams = null;
            // }
        } // while we have another filter value

    } // end function

    public loadSelectedVertexValueFilters(filterParamsName: FilterParamNames,
                                          currentValuesGfis: GobiiFileItem[],
                                          previousValuesGfis: GobiiFileItem[]) {


        let vertexValues: string[] = currentValuesGfis.map(gfi => gfi.getItemId());
        let vertexValueIdsCsv: string = null;
        let filterParams: FilterParams = this.filterParamsColl.getFilter(filterParamsName, GobiiExtractFilterType.FLEX_QUERY);

        if (filterParams && vertexValues && vertexValues.length > 0) {

            vertexValueIdsCsv = "";
            vertexValues.forEach(
                vv => vertexValueIdsCsv += vv + ","
            );

        } // if we have new vertex values


        previousValuesGfis.forEach(gfi => {

            let loadAction: fileItemActions.RemoveFromExtractAction = new fileItemActions.RemoveFromExtractAction(gfi);
            this.store.dispatch(loadAction);

        });

        currentValuesGfis.forEach(gfi => {

            let loadAction: fileItemActions.LoadFileItemtAction = new fileItemActions.LoadFileItemtAction(
                {
                    gobiiFileItem: gfi,
                    selectForExtract: true
                }
            );
            this.store.dispatch(loadAction);

        });

        this.filterService.loadFilter(GobiiExtractFilterType.FLEX_QUERY,
            filterParamsName,
            vertexValueIdsCsv);


        // let gobiiTreeNodes: GobiiTreeNode[] = currentValuesGfis
        //     .map(gfi => this.treeStructureService.makeTreeNodeFromFileItem(gfi));
        //
        // gobiiTreeNodes.forEach(gtn => {
        //     gtn.setSequenceNum(filterParams.getSequenceNum());
        //     gtn.setItemType(ExtractorItemType.VERTEX_VALUE);
        //     // gtn.setItemType(ExtractorItemType.VERTEX); // the three node we're adding has to be of type VERTEX
        //     //                                            // in order to added to the VERTEX nodes
        //     //                                            // this is probably bad
        // });
        //
        // gobiiTreeNodes.forEach(tn => {
        //     this.store.dispatch(new treeNodeActions.PlaceTreeNodeAction(tn));
        // });

    }


    public loadVertexValues(jobId: string, vertexFileItem: GobiiFileItem, filterParamName: FilterParamNames) {

//        return Observable.create(observer => {
        let filterParams: FilterParams = this.filterParamsColl.getFilter(filterParamName, GobiiExtractFilterType.FLEX_QUERY);
        if (vertexFileItem.getNameIdLabelType() == NameIdLabelType.UNKNOWN) {

            let targetVertex: Vertex = vertexFileItem.getEntity();
            let vertexFilterDTO: VertexFilterDTO = new VertexFilterDTO(
                targetVertex,
                [],
                [],
                null,
                null
            );

            let vertexFilterDtoResponse: VertexFilterDTO = null;
            this.dtoRequestServiceVertexFilterDTO.post(new DtoRequestItemVertexFilterDTO(
                vertexFilterDTO,
                jobId,
                false
            )).subscribe(vertexFilterDto => {
                    vertexFilterDtoResponse = vertexFilterDto;

                    // note that we are setting the entity type, sub type, cvgroup, and cvterm
                    // based on our request -- on the target vertex. In theory, the server could
                    // be responding with NameId items that do not fit this. But this is the
                    // way we handle other types of requests, basing our entity types and so forth
                    // largely on the content of the request request.
                    let vertexFileItems: GobiiFileItem[] = [];
                    vertexFilterDto.vertexValues.forEach(item => {

                            let currentFileItem: GobiiFileItem =
                                GobiiFileItem.build(
                                    GobiiExtractFilterType.FLEX_QUERY,
                                    ProcessType.CREATE)
                                    .setExtractorItemType(ExtractorItemType.VERTEX_VALUE)
                                    .setEntityType(targetVertex.entityType)
                                    .setEntitySubType(targetVertex.entitySubType)
                                    .setCvGroup(targetVertex.cvGroup)
                                    .setCvTerm(targetVertex.cvTerm)
                                    .setItemId(item.id)
                                    .setItemName(item.name)
                                    .setRequired(false)
                                    .setSequenceNum(filterParams.getSequenceNum());
                            //.setParentItemId(filterValue)
                            //.setIsExtractCriterion(filterParamsToLoad.getIsExtractCriterion())
                            //.withRelatedEntity(entityRelation);
                            vertexFileItems.push(currentFileItem);
                        }
                    );


                    // for flex query the "filter value" is not an actual id but a new entity type
                    // our selectors "just know" to look for the filter's target entity type as the thing to filter on
                    let targetCompoundUniqueId: GobiiFileItemCompoundId = filterParams.getTargetEntityUniqueId();
                    targetCompoundUniqueId.setExtractorItemType(ExtractorItemType.VERTEX_VALUE);
                    targetCompoundUniqueId.setEntityType(targetVertex.entityType);
                    let loadAction: fileItemActions.LoadFileItemListWithFilterAction =
                        new fileItemActions.LoadFileItemListWithFilterAction(
                            {
                                gobiiFileItems: vertexFileItems,
                                filterId: filterParams.getQueryName(),
                                filter: new PayloadFilter(
                                    GobiiExtractFilterType.FLEX_QUERY,
                                    targetCompoundUniqueId,
                                    filterParams.getRelatedEntityUniqueId(),
                                    null,
                                    null,
                                    null,
                                    null
                                )
                            }
                        );

                    this.store.dispatch(loadAction);

                    //observer.next(vertexFileItems);
                    //observer.complete();
                },
                headerResponse => {
                    headerResponse.status.statusMessages.forEach(statusMessage => {
                        this.store.dispatch(new historyAction.AddStatusAction(statusMessage));
                    });
                    //observer.complete();
                });

        } else {
            this.store.dispatch(new fileItemActions.LoadFilterAction(
                {
                    filterId: filterParamName,
                    filter: new PayloadFilter(
                        GobiiExtractFilterType.FLEX_QUERY,
                        new GobiiFileItemCompoundId(ExtractorItemType.VERTEX_VALUE,
                            EntityType.UNKNOWN, // effectively "null out" the selected entity type
                            EntitySubType.UNKNOWN,
                            CvGroup.UNKNOWN,
                            getCvGroupName(CvGroup.UNKNOWN)),
                        null,
                        null,
                        null,
                        null,
                        null
                    )
                }
            ))
        } // if-else file item type was label

        //} );//return observer create
    }


    private deSelectVertexValueFilters(compoundUniquueId: GobiiFileItemCompoundId) {

        this.store.select(fromRoot.getSelectedFileItems)
            .subscribe(gfi => {
                let itemsToDeselect: GobiiFileItem[] = gfi.filter(sgfi => sgfi.compoundIdeEquals(compoundUniquueId));
                itemsToDeselect.forEach(itr => {
                    let loadAction: fileItemActions.RemoveFromExtractAction = new fileItemActions.RemoveFromExtractAction(itr);
                    this.store.dispatch(loadAction);
                });
            }).unsubscribe();

    }
}
