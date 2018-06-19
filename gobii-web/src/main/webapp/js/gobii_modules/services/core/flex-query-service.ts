import {Injectable} from "@angular/core";
import {GobiiExtractFilterType} from "../../model/type-extractor-filter";
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
import {EntitySubType, EntityType} from "../../model/type-entity";
import {NameIdLabelType} from "../../model/name-id-label-type";
import {CvGroup, getCvGroupName} from "../../model/cv-group";
import {FilterService} from "./filter-service";
import {TreeStructureService} from "./tree-structure-service";
import {count} from "rxjs/operator/count";
import {VertexNameType} from "../../model/type-vertex-name";

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

    /***
     * Recall that in the FlexQuery universe of discourse, there are two focii of interest: vertices, and vertex values.
     * Each of the four flex query controls consists of a list of vertices and a list of corresponding vertex values
     * that is populated  when the user selects a vertex value. The tree is affected in two ways by by the control:
     *  1) When a new value is selected, the node corresponding to the filter in the tree is updated to receive a name
     *     that indicates the type (e.g., Analysis, Dataset, etc.);
     *  2) When a user clicks a value from the value list, the selected value is added under the corresponding tree
     *     node (e.g., if the user had selected Analysis as the vertex for filter 2, such that the Filter 2 node in the
     *     the tree will now say "Filter 2: Analysis," when the user now selects "My fine analysis" from the value list,
     *     it (and any others that are selected) will also be added under the "Filter 2: Aanlysis" node.
     * The tree will be updated in this way as the user selects vertices and vertex values for each of the filters.
     * When the user selects a new value on a flilter to the left of any other filter (i.e., filters 1 through 3),
     * all the filter to right need to be "cleared" in the following way:
     *  1) The filter's tree node needs to be reset for the newly selected entity;
     *  2) The child filters of the filter's tree node need to be cleared;
     *  3) The filter control's vertex selector needs to be reset to "Select A";
     *  4) The filter control's vertex values need to be cleared.
     * A word should be said here about the way that the FilterParam objects underlying this mechanism are organized.
     * Each FlexQuery filter control is associated with two FilterParams object -- one for the vertices and another
     * for the vertex values. The vertex values are arranged as a list, such that there are previous and next sibling
     * methods: given a FilterParams instance, you can figure out whether you have a filter to the left or right.
     * (FlexQuery so far is the only feature that uses sibling filter relationships). Each vertex filter params's object
     * has a child filter, which is the filter for the vertex values.
     * With all that as background, it will now be intelligible to explain that this method is called by the FlexQuery
     * filter component when a vertex value change, and that it is responsible for ensuring that the filter control and
     * tree behaviors described are carried out.
     * This method conceives of two different vertices:
     *      * the "evented" vertex is the one that was selected by the user's actions, and the values of which are passed
     *        in as the method's parameter values;
     *       * the "sibling" vertices are the ones to the right of the the evented vertex, as identified by vertex filter's
     *         "next sibling"
     * What this method does is as follows:
     *   * For the evented vertex:
     *      * Set the vertex's filter to the id of the evented vertex so that the vertex value list controlled
     *        by that filter is reset (if the user selected "Select A", the vertexId will be null and so no vertex values
     *        will be displayed;
     *      * change the label of the filter node for the filter to the newly selected vertex value (or blank if null);
     *    * For the sibling vertices:
     *       * Set the vertex's filter to null;
     *       * Clear the filter node's label
     *   * For the evented and sibling vertices: remove the previously selected vertex values from the tree;
     *
     *   The algorithm is otherwise self-documenting. However, a few points should be kept in mind:
     *      * We are using the FilterParam objects to maintain the filter's selected entity values, whilst the filter
     *        value itself is kept in the store so that the selectors for the vertices and vertex value lists will work
     *        out correctly; technically, all of the filter state should be kept the store. The reason it is not is because
     *        consuming the store values in this context requir3es a method that generates a series of dispatch actions
     *        that would increase the complexity of the method; for now, it seems to work; in the future, it may need to
     *        be refactored;
     *      * This method relies very heavily on the compound IDs for the entities. Pre FlexQuery functionality does not
     *        have dynamic entity types and so did not need to operate in this way. It will be noded that the CompoundUnqiueID
     *        class now has a "from" method to copy an existing one: in this method, we almost always want to make copies
     *        in this way; otherwise, we are copying references to insteawnces and that does not do what we want.
     *
     * @param {FilterParamNames} eventedFilterParamsName
     * @param {string} eventedVertexId
     * @param {EntityType} eventedEntityType
     * @param {EntitySubType} eventedEntitySubType
     * @param {CvGroup} eventedCvGroup
     * @param {string} eventedCvTerm
     */
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

            currentVertexFilterParams = currentVertexFilterParams.getNextSiblingFileItemParams();
            if (currentVertexFilterParams) {
                previousTargetCompoundId = currentVertexFilterParams.getTargetEntityUniqueId();
                newVertexFilterTargetCompoundId = GobiiFileItemCompoundId.fromGobiiFileItemCompoundId(nullTargetCompoundIdOfVertex)
                    .setSequenceNum(currentVertexFilterParams.getSequenceNum());
            } // no else necessary because null currentVertexFilterParams will end execution

            currentVertexId = null; // by definition, we always null out the children

        } // while we have another filter value

    } // end function

    public loadSelectedVertexValueFilters(filterParamsName: FilterParamNames,
                                          allSelectedGfis: GobiiFileItem[],
                                          newlySelectedValuesGfis: GobiiFileItem[],
                                          previousValuesGfis: GobiiFileItem[],
                                          targetValueVertex: Vertex) {


        let currentVertexValues: string[] = allSelectedGfis.map(gfi => gfi.getItemId());
        let filterParams: FilterParams = this.filterParamsColl.getFilter(filterParamsName, GobiiExtractFilterType.FLEX_QUERY);

        let filterVertex: Vertex = null;
        if (filterParams && currentVertexValues && currentVertexValues.length > 0) {

            filterVertex = Vertex.fromVertex(targetValueVertex);
            currentVertexValues.forEach(vv => {
                    filterVertex.filterVals.push(Number(vv));
                }
            );

        } // if we have new vertex values


        previousValuesGfis.forEach(gfi => {

            let loadAction: fileItemActions.RemoveFromExtractAction = new fileItemActions.RemoveFromExtractAction(gfi);
            this.store.dispatch(loadAction);

        });

        newlySelectedValuesGfis.forEach(gfi => {

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
            filterVertex);


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


    public loadVertexValues(jobId: string, vertexFileItem: GobiiFileItem, vertexValuesFilterPararamName: FilterParamNames) {

//        return Observable.create(observer => {
        let targetChildFilterParams: FilterParams = this.filterParamsColl.getFilter(vertexValuesFilterPararamName, GobiiExtractFilterType.FLEX_QUERY);
        if (vertexFileItem.getNameIdLabelType() == NameIdLabelType.UNKNOWN) {

            let filtterChildFilterParams: FilterParams;
            if (targetChildFilterParams.getParentFileItemParams()
                && targetChildFilterParams.getParentFileItemParams().getPreviousSiblingFileItemParams()
                && targetChildFilterParams.getParentFileItemParams().getPreviousSiblingFileItemParams().getChildFileItemParams()
                && targetChildFilterParams.getParentFileItemParams().getPreviousSiblingFileItemParams().getChildFileItemParams().length > 0) {
                filtterChildFilterParams = targetChildFilterParams.getParentFileItemParams().getPreviousSiblingFileItemParams().getChildFileItemParams()[0];
            }


            this.store
                .select(fromRoot.getFileItemsFilters)
                .subscribe(filters => {

                    let filterVertices: Vertex[] = [];
                    let vertexValueFilterFromState: PayloadFilter = filtterChildFilterParams ? filters[filtterChildFilterParams.getQueryName()] : null;
                    if (vertexValueFilterFromState) {

                        let filterValuesFromState: Vertex = vertexValueFilterFromState.targetEntityFilterValue;
                        filterVertices.push(filterValuesFromState);

                        // let vertexType:VertexNameType;
                        // if (!isNaN(filterValuesFromState[0])) {
                        //     vertexType = VertexNameType[VertexNameType[filterValuesFromState[0]]];
                        // }
                        //
                        // let filterIds:number[];
                        // filterValuesFromState.slice(1,filterIds.length -1).forEach(id => {
                        //     filterIds.push(Number(id));
                        // })

                        // let filterVertex:Vertex = new Vertex(0,
                        //     vertexType,
                        //     );

                    } // if we found vertex value filter in state


                    let targetVertex: Vertex = vertexFileItem.getEntity();
                    let vertexFilterDTO: VertexFilterDTO = new VertexFilterDTO(
                        targetVertex,
                        filterVertices,
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
                                            .setSequenceNum(targetChildFilterParams.getSequenceNum());
                                    //.setParentItemId(filterValue)
                                    //.setIsExtractCriterion(filterParamsToLoad.getIsExtractCriterion())
                                    //.withRelatedEntity(entityRelation);
                                    vertexFileItems.push(currentFileItem);
                                }
                            );


                            // for flex query the "filter value" is not an actual id but a new entity type
                            // our selectors "just know" to look for the filter's target entity type as the thing to filter on
                            let targetCompoundUniqueId: GobiiFileItemCompoundId = targetChildFilterParams.getTargetEntityUniqueId();
                            targetCompoundUniqueId.setExtractorItemType(ExtractorItemType.VERTEX_VALUE);
                            targetCompoundUniqueId.setEntityType(targetVertex.entityType);
                            let loadAction: fileItemActions.LoadFileItemListWithFilterAction =
                                new fileItemActions.LoadFileItemListWithFilterAction(
                                    {
                                        gobiiFileItems: vertexFileItems,
                                        filterId: targetChildFilterParams.getQueryName(),
                                        filter: new PayloadFilter(
                                            GobiiExtractFilterType.FLEX_QUERY,
                                            targetCompoundUniqueId,
                                            targetChildFilterParams.getRelatedEntityUniqueId(),
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


                }).unsubscribe();


        } else // else label is not UNKNOWN{
            this.store.dispatch(new fileItemActions.LoadFilterAction(
                {
                    filterId: vertexValuesFilterPararamName,
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
