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

    public loadSelectedVertexFilter(filterParamsName: FilterParamNames,
                                    vertexId: string,
                                    entityType:EntityType,
                                    entitySubType:EntitySubType,
                                    cvGroup:CvGroup,
                                    cvTerm:string) {

        let filterParams: FilterParams = this.filterParamsColl.getFilter(filterParamsName, GobiiExtractFilterType.FLEX_QUERY);

        // the filterParams passed in should exist
        if (!filterParams) {
            this.store.dispatch(new historyAction.AddStatusMessageAction("Error loading filter: there is no query params object for query "
                + filterParamsName
                + " with extract filter type "
                + GobiiExtractFilterType.FLEX_QUERY[GobiiExtractFilterType.FLEX_QUERY]));
        }

        while (filterParams) {

            filterParams.getTargetEntityUniqueId().setEntityType(entityType);
            filterParams.getTargetEntityUniqueId().setEntitySubType(entitySubType);
            filterParams.getTargetEntityUniqueId().setCvGroup(cvGroup);
            filterParams.getTargetEntityUniqueId().setCvTerm(cvTerm);

            let targetFilterloadAction: fileItemActions.LoadFilterAction = new fileItemActions.LoadFilterAction(
                {
                    filterId: filterParams.getQueryName(),
                    filter: new PayloadFilter(
                        GobiiExtractFilterType.FLEX_QUERY,
                        filterParams.getTargetEntityUniqueId(),
                        filterParams.getRelatedEntityUniqueId(),
                        null,
                        vertexId,
                        null,
                        null
                    )
                }
            );

            this.store.dispatch(targetFilterloadAction);
            this.treeStructureService.updateTreeNode(GobiiExtractFilterType.FLEX_QUERY,
                filterParams.getTargetEntityUniqueId());


            // propagate null filter to child
            if (!vertexId
                && filterParams.getChildFileItemParams()
                && filterParams.getChildFileItemParams().length > 0) {

                let childFilterParams: FilterParams = filterParams.getChildFileItemParams()[0];
                let childFilterLoadAction: fileItemActions.LoadFilterAction = new fileItemActions.LoadFilterAction(
                    {
                        filterId: childFilterParams.getQueryName(),
                        filter: new PayloadFilter(
                            GobiiExtractFilterType.FLEX_QUERY,
                            childFilterParams.getTargetEntityUniqueId().setEntityType(EntityType.UNKNOWN),
                            childFilterParams.getRelatedEntityUniqueId(),
                            null,
                            vertexId,
                            null,
                            null
                        )
                    }
                );

                this.store.dispatch(childFilterLoadAction);
            }


            // if the current filter is getting nulled, we need to null the siblings as well
            // but we dont' need to cascade filter values here
            // note that for now this is only really relevant to FlexQuery filters
            if (!vertexId) {
                filterParams = filterParams.getNextSiblingFileItemParams();
            } else {
                filterParams = null;
            }
        } // while we have another filter value

    }

    public loadSelectedVertexValueFilters(filterParamsName: FilterParamNames,
                                          vertexValuesGfis: GobiiFileItem[]) {


        let vertexValues: string[] = vertexValuesGfis.map(gfi => gfi.getItemId());
        let vertexValueIdsCsv: string = null;

        if (vertexValues && vertexValues.length > 0) {

            vertexValueIdsCsv = "";
            vertexValues.forEach(
                vv => vertexValueIdsCsv += vv + ","
            );
        }

        this.filterService.loadFilter(GobiiExtractFilterType.FLEX_QUERY,
            filterParamsName,
            vertexValueIdsCsv);


        let gobiiTreeNodes: GobiiTreeNode[] = vertexValuesGfis
            .map(gfi => this.treeStructureService.makeTreeNodeFromFileItem(gfi));

        let filterParams: FilterParams = this.filterParamsColl.getFilter(filterParamsName, GobiiExtractFilterType.FLEX_QUERY);
        gobiiTreeNodes.forEach(gtn => {
           gtn.setSequenceNum(filterParams.getSequenceNum());
           gtn.setItemType(ExtractorItemType.VERTEX); // the three node we're adding has to be of type VERTEX
                                                      // in order to added to the VERTEX nodes
                                                      // this is probably bad
        });

        gobiiTreeNodes.forEach(tn => {
            this.store.dispatch(new treeNodeActions.PlaceTreeNodeAction(tn));
        });

    }


    public loadVertexValues(jobId: string, vertexFileItem: GobiiFileItem, filterParamName: FilterParamNames) {

//        return Observable.create(observer => {

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
                    // this.store.dispatch(new  historyAction
                    //     .AddStatusMessageAction("Extractor instruction file created on server: "
                    //         + extractorInstructionFilesDTOResponse.getInstructionFileName()));
                    //

                    let vertexFileItems: GobiiFileItem[] = [];
                    vertexFilterDto.vertexValues.forEach(item => {

                            let currentFileItem: GobiiFileItem =
                                GobiiFileItem.build(
                                    GobiiExtractFilterType.FLEX_QUERY,
                                    ProcessType.CREATE)
                                    .setExtractorItemType(ExtractorItemType.VERTEX_VALUE)
                                    .setEntityType(targetVertex.entityType)
                                    // .setEntitySubType(filterParamsToLoad.getEntitySubType())
                                    // .setCvFilterType(filterParamsToLoad.getCvFilterType())
                                    .setItemId(item.id)
                                    .setItemName(item.name)
                                    //.setSelected(false)
                                    .setRequired(false);
                            //.setParentItemId(filterValue)
                            //.setIsExtractCriterion(filterParamsToLoad.getIsExtractCriterion())
                            //.withRelatedEntity(entityRelation);
                            vertexFileItems.push(currentFileItem);
                        }
                    );


                    // for flex query the "filter value" is not an actua id but a new entity type
                    // our selectors "just know" to look for the filter's target entity type as the thing to filter on
                    let filterParams: FilterParams = this.filterParamsColl.getFilter(filterParamName, GobiiExtractFilterType.FLEX_QUERY);
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

}
