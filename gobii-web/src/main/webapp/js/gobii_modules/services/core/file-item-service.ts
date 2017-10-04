import {Injectable} from "@angular/core";
import {ContainerType, GobiiTreeNode} from "../../model/GobiiTreeNode";
import {EntitySubType, EntityType} from "../../model/type-entity";
import {Labels} from "../../views/entity-labels";
import {ExtractorItemType} from "../../model/file-model-node";
import {GobiiExtractFilterType} from "../../model/type-extractor-filter";
import {CvFilters, CvFilterType} from "../../model/cv-filter-type";
import {GobiiFileItem} from "../../model/gobii-file-item";
import {HeaderStatusMessage} from "../../model/dto-header-status-message";
import {GobiiExtractFormat} from "../../model/type-extract-format";
import {ProcessType} from "../../model/type-process";
import {NameIdService} from "./name-id-service";
import {FileItemParams} from "../../model/name-id-request-params";
import * as fileItemActions from '../../store/actions/fileitem-action'
import * as fromRoot from '../../store/reducers';

import {Store} from "@ngrx/store";
import {NameIdLabelType} from "../../model/name-id-label-type";
import {NameId} from "../../model/name-id";
import {EntityFilter} from "../../model/type-entity-filter";
import {NameIdFilterParamTypes} from "../../model/type-nameid-filter-params";

@Injectable()
export class FileItemService {

    nameIdRequestParams: Map<NameIdFilterParamTypes, FileItemParams> =
        new Map<NameIdFilterParamTypes, FileItemParams>();

    constructor(private nameIdService: NameIdService,
                private store: Store<fromRoot.State>,) {

        // For non-hierarchically filtered request params, we just create them simply
        // as we add them to the flat map
        this.nameIdRequestParams.set(NameIdFilterParamTypes.CV_DATATYPE,
            FileItemParams
                .build(NameIdFilterParamTypes.CV_DATATYPE,
                    GobiiExtractFilterType.WHOLE_DATASET,
                    EntityType.CvTerms)
                .setCvFilterType(CvFilterType.DATASET_TYPE)
                .setEntityFilter(EntityFilter.BYTYPENAME)
                .setFkEntityFilterValue(CvFilters.get(CvFilterType.DATASET_TYPE))
                .setNameIdLabelType(NameIdLabelType.SELECT_A)
        );


        this.nameIdRequestParams.set(NameIdFilterParamTypes.MAPSETS,
            FileItemParams
                .build(NameIdFilterParamTypes.MAPSETS,
                    GobiiExtractFilterType.WHOLE_DATASET,
                    EntityType.Mapsets)
                .setNameIdLabelType(NameIdLabelType.NO));

        this.nameIdRequestParams.set(NameIdFilterParamTypes.PLATFORMS,
            FileItemParams
                .build(NameIdFilterParamTypes.PLATFORMS,
                    GobiiExtractFilterType.WHOLE_DATASET,
                    EntityType.Platforms));

        //for hierarchical items, we need to crate the nameid requests separately from the
        //flat map: they _will_ need to be in the flat map; however, they all need to be
        //useed to set up the filtering hierarchy
        let nameIdRequestParamsContactsPi: FileItemParams = FileItemParams
            .build(NameIdFilterParamTypes.CONTACT_PI,
                GobiiExtractFilterType.WHOLE_DATASET,
                EntityType.Contacts)
            .setEntitySubType(EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR);

        let nameIdRequestParamsProject: FileItemParams = FileItemParams
            .build(NameIdFilterParamTypes.PROJECTS_BY_CONTACT,
                GobiiExtractFilterType.WHOLE_DATASET,
                EntityType.Projects)
            .setEntityFilter(EntityFilter.BYTYPEID);

        let nameIdRequestParamsExperiments: FileItemParams = FileItemParams
            .build(NameIdFilterParamTypes.EXPERIMENTS_BY_PROJECT,
                GobiiExtractFilterType.WHOLE_DATASET,
                EntityType.Experiments)
            .setEntityFilter(EntityFilter.BYTYPEID);

        let nameIdRequestParamsDatasets: FileItemParams = FileItemParams
            .build(NameIdFilterParamTypes.DATASETS_BY_EXPERIMENT,
                GobiiExtractFilterType.WHOLE_DATASET,
                EntityType.DataSets)
            .setEntityFilter(EntityFilter.BYTYPEID);

        //add the individual requests to the map
        this.nameIdRequestParams.set(nameIdRequestParamsContactsPi.getQueryName(),nameIdRequestParamsContactsPi);
        this.nameIdRequestParams.set(nameIdRequestParamsProject.getQueryName(),nameIdRequestParamsProject);
        this.nameIdRequestParams.set(nameIdRequestParamsExperiments.getQueryName(),nameIdRequestParamsExperiments);
        this.nameIdRequestParams.set(nameIdRequestParamsDatasets.getQueryName(),nameIdRequestParamsDatasets);

        //build the parent-child request params graph
        nameIdRequestParamsContactsPi
            .setChildNameIdRequestParams(
                [nameIdRequestParamsProject
                    .setParentNameIdRequestParams(nameIdRequestParamsContactsPi)
                    .setChildNameIdRequestParams([nameIdRequestParamsExperiments
                        .setParentNameIdRequestParams(nameIdRequestParamsProject)
                        .setChildNameIdRequestParams([nameIdRequestParamsDatasets
                            .setParentNameIdRequestParams(nameIdRequestParamsExperiments)
                        ])
                    ])
                ]);

    } // constructor


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

        this.loadNameIdsToFileItems(gobiiExtractFilterType,
            nameIdRequestParamsFromType,
            filterValue);
    }


    public locadFileItem(gobiiFileItem:GobiiFileItem, selectForExtract: boolean) {


        let loadAction: fileItemActions.LoadFileItemtAction = new fileItemActions.LoadFileItemtAction(
            {
                gobiiFileItem: gobiiFileItem,
                selectForExtract: selectForExtract
            }
        );


        this.store.dispatch(loadAction);

    }

    private loadNameIdsToFileItems(gobiiExtractFilterType: GobiiExtractFilterType,
                                   nameIdRequestParamsToLoad: FileItemParams,
                                   filterValue: string) {

        nameIdRequestParamsToLoad.setFkEntityFilterValue(filterValue);

        this.nameIdService.get(nameIdRequestParamsToLoad)
            .subscribe(nameIds => {

                    let fileItems: GobiiFileItem[] = [];
                    if (nameIds && ( nameIds.length > 0 )) {

                        nameIds.forEach(n => {
                            let currentFileItem: GobiiFileItem =
                                GobiiFileItem.build(
                                    gobiiExtractFilterType,
                                    ProcessType.CREATE)
                                    .setExtractorItemType(ExtractorItemType.ENTITY)
                                    .setEntityType(nameIdRequestParamsToLoad.getEntityType())
                                    .setCvFilterType(CvFilterType.UNKNOWN)
                                    .setItemId(n.id)
                                    .setItemName(n.name)
                                    .setSelected(false)
                                    .setRequired(false)
                                    .setParentItemId(nameIdRequestParamsToLoad.getFkEntityFilterValue());


                            fileItems.push(currentFileItem);
                        });


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
                                    console.log(new HeaderStatusMessage("Unknown label type "
                                        + NameIdLabelType[nameIdRequestParamsToLoad.getMameIdLabelType()], null, null))
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

                    let loadAction: fileItemActions.LoadFileItemListAction = new fileItemActions.LoadFileItemListAction(
                        {
                            gobiiFileItems: fileItems,
                            filterId: nameIdRequestParamsToLoad.getQueryName(),
                            filterValue: nameIdRequestParamsToLoad.getFkEntityFilterValue()
                        }
                    );


                    this.store.dispatch(loadAction);

                    // if there are children, we will load their data as well
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
                                    this.loadNameIdsToFileItems(gobiiExtractFilterType,
                                        rqp,
                                        parentId);
                                }
                            });
                    }

                    // let formatItem: GobiiFileItem = GobiiFileItem
                    //     .build(gobiiExtractFilterType, ProcessType.UPDATE)
                    //     .setExtractorItemType(ExtractorItemType.EXPORT_FORMAT)
                    //     .setItemId(GobiiExtractFormat[GobiiExtractFormat.HAPMAP])
                    //     .setItemName(GobiiExtractFormat[GobiiExtractFormat[GobiiExtractFormat.HAPMAP]]);
                    // this.locadFileItem(formatItem,true);


                }, // Observer=>next
                responseHeader => {
                    //this.handleHeaderStatus(responseHeader);
                    console.log(responseHeader);
                }); // subscribe
    }

}
