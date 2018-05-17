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
import {EntitySubType, EntityType, entityTypefromString} from "../../model/type-entity";
import {NameIdLabelType} from "../../model/name-id-label-type";
import {CvFilters, CvFilterType} from "../../model/cv-filter-type";

@Injectable()
export class FlexQueryService {


    constructor(private store: Store<fromRoot.State>,
                private entityFileItemService: EntityFileItemService,
                private dtoRequestServiceVertexFilterDTO: DtoRequestService<VertexFilterDTO>,
                private filterParamsColl: FilterParamsColl) {


    }


    public loadVertices(filterParamNames: FilterParamNames) {

        this.entityFileItemService.loadEntityList(GobiiExtractFilterType.FLEX_QUERY, filterParamNames);

    } // loadVertices()

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
                                    .setEntityType(entityTypefromString(targetVertex.gobiiEntityNameTypeName))
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
                    targetCompoundUniqueId.setEntityType(entityTypefromString(targetVertex.gobiiEntityNameTypeName));
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
                            CvFilterType.UNKNOWN,
                            CvFilters.get(CvFilterType.UNKNOWN)),
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
