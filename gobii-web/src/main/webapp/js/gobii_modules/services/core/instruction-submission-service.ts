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
import {Observable} from "rxjs/Observable";
import {GobiiSampleListType} from "../../model/type-extractor-sample-list";
import {GobiiDataSetExtract} from "../../model/extractor-instructions/data-set-extract";
import {GobiiExtractorInstruction} from "../../model/extractor-instructions/gobii-extractor-instruction";
import {ExtractorInstructionFilesDTO} from "../../model/extractor-instructions/dto-extractor-instruction-files";
import {DtoRequestItemExtractorSubmission} from "../app/dto-request-item-extractor-submission";
import {FileName} from "../../model/file_name";
import {GobiiFileType} from "../../model/type-gobii-file";
import {DtoRequestService} from "./dto-request.service";
import {StatusLevel} from "../../model/type-status-level";

@Injectable()
export class InstructionSubmissionService {

    constructor(private store: Store<fromRoot.State>,
                private dtoRequestServiceExtractorFile: DtoRequestService<ExtractorInstructionFilesDTO>,) {
    }


    public submitReady(scope$): Observable<boolean> {


        return Observable.create(observer => {
                this.store.select(fromRoot.getSelectedFileItems)
                    .subscribe(all => {


                            let submistReady: boolean = false;

                            if (scope$.gobiiExtractFilterType === GobiiExtractFilterType.WHOLE_DATASET) {

                                submistReady =
                                    all
                                        .filter(fi =>
                                            fi.getGobiiExtractFilterType() === scope$.gobiiExtractFilterType
                                            && fi.getExtractorItemType() === ExtractorItemType.ENTITY
                                            && fi.getEntityType() === EntityType.DataSets
                                        )
                                        .length > 0;


                            } // if-else on extract type

                        let temp = "foo";
                            observer.next(submistReady);

                        }
                    ) // inner subscribe
            } //observer lambda
        ); // Observable.crate

    } // function()


    // In theory this method should be unnecessary because there should not be any duplicates;
    // however, in testing, it was discovered that there can be duplicate datasets and
    // duplicate platforms. I suspect that the root cause of this issue is the checkbox component:
    // because it keeps a history of selected items, it may be reposting existing items in a way that
    // is not detected by the file item service. In particular, it strikes me that if an item is added
    // in one extract type (e.g., by data set), and then selected again in another (by samples), there
    // could be duplicate items in the tree service, because it is specific to extract filter type.
    // TreeService::getFileItems() should be filtering correctly, but perhaps it's not. In any case,
    // at this point in the release cycle, it is too late to to do the trouble shooting to figure this out,
    // because I am unable to reproduce the issue in my local testing. This method at leaset reports
    // warnings to the effect that the problem exists, but results in an extract that is free of duplicates.
    // Technically, sample and marker list item duplicates should be eliminated in the list item control,
    // but it is also too late for that.
    private eliminateDuplicateEntities(extractorItemType: ExtractorItemType,
                                       entityType: EntityType,
                                       fileItems: GobiiFileItem[]): GobiiFileItem[] {

        return fileItems;

        // let returnVal: GobiiFileItem[] = [];
        //
        // if (fileItems
        //         .filter(fi => {
        //             return fi.getExtractorItemType() === extractorItemType
        //                 && fi.getEntityType() === entityType
        //         })
        //         .length == fileItems.length) {
        //
        //     fileItems.forEach(ifi => {
        //
        //         if (returnVal.filter(rfi => {
        //                 return rfi.getItemId() === ifi.getItemId()
        //             }).length === 0) {
        //
        //             returnVal.push(ifi);
        //         } else {
        //             let message: string = "A duplicate ";
        //             message += ExtractorItemType[extractorItemType];
        //             message += " (" + EntityType[entityType] + ") ";
        //             message += "item was found; ";
        //             if (ifi.getItemName()) {
        //                 message += "name: " + ifi.getItemName() + "; "
        //             }
        //
        //             if (ifi.getItemId()) {
        //                 message += "id: " + ifi.getItemId();
        //             }
        //
        //             // this.handleHeaderStatusMessage(new HeaderStatusMessage(message,
        //             //     StatusLevel.WARNING,
        //             //     null));
        //         }
        //     });
        //
        // } else {
        //
        //     // this.handleHeaderStatusMessage(new HeaderStatusMessage(
        //     //     "The elimination array contains mixed entities",
        //     //     StatusLevel.WARNING,
        //     //     null));
        //
        // }
        //
        //
        // return returnVal;
    }
    
    public submit(scope$) {
        let gobiiExtractorInstructions: GobiiExtractorInstruction[] = [];
        let gobiiDataSetExtracts: GobiiDataSetExtract[] = [];
        let mapsetIds: number[] = [];
        let submitterContactid: number = null;
        let jobId: string = null;
        let markerFileName: string = null;
        let sampleFileName: string = null;
        let sampleListType: GobiiSampleListType;

        this.store.select(fromRoot.getSelectedFileItems)
            .subscribe(fileItems => {


                // ******** JOB ID
                let fileItemJobId: GobiiFileItem = fileItems.find(item => {
                    return item.getExtractorItemType() === ExtractorItemType.JOB_ID
                });

                if (fileItemJobId != null) {
                    jobId = fileItemJobId.getItemId();
                }

                // ******** MARKER FILE
                let fileItemMarkerFile: GobiiFileItem = fileItems.find(item => {
                    return item.getExtractorItemType() === ExtractorItemType.MARKER_FILE
                });

                if (fileItemMarkerFile != null) {
                    markerFileName = fileItemMarkerFile.getItemId();
                }

                // ******** SAMPLE FILE
                let fileItemSampleFile: GobiiFileItem = fileItems.find(item => {
                    return item.getExtractorItemType() === ExtractorItemType.SAMPLE_FILE
                });

                if (fileItemSampleFile != null) {
                    sampleFileName = fileItemSampleFile.getItemId();
                }

                // ******** SUBMITTER CONTACT
                let submitterFileItem: GobiiFileItem = fileItems.find(item => {
                    return (item.getEntityType() === EntityType.Contacts)
                        && (item.getEntitySubType() === EntitySubType.CONTACT_SUBMITED_BY)
                });

                submitterContactid = Number(submitterFileItem.getItemId());


                // ******** MAPSET IDs
                let mapsetFileItems: GobiiFileItem[] = fileItems
                    .filter(item => {
                        return item.getEntityType() === EntityType.Mapsets
                    });
                mapsetFileItems = this.eliminateDuplicateEntities(ExtractorItemType.ENTITY,
                    EntityType.Mapsets,
                    mapsetFileItems);
                mapsetIds = mapsetFileItems
                    .map(item => {
                        return Number(item.getItemId())
                    });

                // ******** EXPORT FORMAT
                let exportFileItem: GobiiFileItem = fileItems.find(item => {
                    return item.getExtractorItemType() === ExtractorItemType.EXPORT_FORMAT
                });

                // these probably should be just one enum
                let gobiiFileType: GobiiFileType = null;
                let extractFormat: GobiiExtractFormat = GobiiExtractFormat[exportFileItem.getItemId()];
                if (extractFormat === GobiiExtractFormat.FLAPJACK) {
                    gobiiFileType = GobiiFileType.FLAPJACK;
                } else if (extractFormat === GobiiExtractFormat.HAPMAP) {
                    gobiiFileType = GobiiFileType.HAPMAP;
                } else if (extractFormat === GobiiExtractFormat.META_DATA_ONLY) {
                    gobiiFileType = GobiiFileType.META_DATA;
                }


                // ******** DATA SET TYPE
                let dataTypeFileItem: GobiiFileItem = fileItems.find(item => {
                    return item.getEntityType() === EntityType.CvTerms
                        && item.getCvFilterType() === CvFilterType.DATASET_TYPE
                });
                let datasetType: NameId = dataTypeFileItem != null ? new NameId(dataTypeFileItem.getItemId(),
                    dataTypeFileItem.getItemName(), EntityType.CvTerms) : null;


                // ******** PRINCIPLE INVESTIGATOR CONCEPT
                let principleInvestigatorFileItem: GobiiFileItem = fileItems.find(item => {
                    return item.getEntityType() === EntityType.Contacts
                        && item.getEntitySubType() === EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR
                });
                let principleInvestigator: NameId = principleInvestigatorFileItem != null ? new NameId(principleInvestigatorFileItem.getItemId(),
                    principleInvestigatorFileItem.getItemName(), EntityType.Contacts) : null;


                // ******** PROJECT
                let projectFileItem: GobiiFileItem = fileItems.find(item => {
                    return item.getEntityType() === EntityType.Projects
                });
                let project: NameId = projectFileItem != null ? new NameId(projectFileItem.getItemId(),
                    projectFileItem.getItemName(), EntityType.Projects) : null;


                // ******** PLATFORMS
                let platformFileItems: GobiiFileItem[] = fileItems.filter(item => {
                    return item.getEntityType() === EntityType.Platforms
                });

                platformFileItems = this.eliminateDuplicateEntities(ExtractorItemType.ENTITY,
                    EntityType.Platforms,
                    platformFileItems);

                let platforms: NameId[] = platformFileItems.map(item => {
                    return new NameId(item.getItemId(), item.getItemName(), EntityType.Platforms)
                });

                let markerGroupItems: GobiiFileItem[] = fileItems.filter(item => {
                    return item.getEntityType() === EntityType.MarkerGroups
                });

                markerGroupItems = this.eliminateDuplicateEntities(ExtractorItemType.ENTITY,
                    EntityType.MarkerGroups,
                    markerGroupItems);

                let markerGroups: NameId[] = markerGroupItems.map(item => {
                    return new NameId(item.getItemId(), item.getItemName(), EntityType.MarkerGroups)
                });

                // ******** MARKERS
                let markerListItems: GobiiFileItem[] =
                    fileItems
                        .filter(fi => {
                            return fi.getExtractorItemType() === ExtractorItemType.MARKER_LIST_ITEM
                        });

                markerListItems = this.eliminateDuplicateEntities(ExtractorItemType.MARKER_LIST_ITEM,
                    EntityType.UNKNOWN,
                    markerListItems);
                let markerList: string[] = markerListItems
                    .map(mi => {
                        return mi.getItemId()
                    });


                // ******** SAMPLES
                let sampleListItems: GobiiFileItem[] =
                    fileItems
                        .filter(fi => {
                            return fi.getExtractorItemType() === ExtractorItemType.SAMPLE_LIST_ITEM
                        });

                sampleListItems = this.eliminateDuplicateEntities(ExtractorItemType.SAMPLE_LIST_ITEM,
                    EntityType.UNKNOWN,
                    sampleListItems);
                let sampleList: string[] = sampleListItems
                    .map(mi => {
                        return mi.getItemId()
                    });


                let sampleListTypeFileItem: GobiiFileItem = fileItems.find(item => {
                    return item.getExtractorItemType() === ExtractorItemType.SAMPLE_LIST_TYPE;
                });

                if (sampleListTypeFileItem != null) {
                    sampleListType = GobiiSampleListType[sampleListTypeFileItem.getItemId()];
                }

                if (scope$.gobiiExtractFilterType === GobiiExtractFilterType.WHOLE_DATASET) {

                    let dataSetItems: GobiiFileItem[] = fileItems
                        .filter(item => {
                            return item.getEntityType() === EntityType.DataSets
                        });

                    dataSetItems = this.eliminateDuplicateEntities(ExtractorItemType.ENTITY,
                        EntityType.DataSets,
                        dataSetItems);

                    dataSetItems.forEach(datsetFileItem => {

                        let dataSet: NameId = new NameId(datsetFileItem.getItemId(),
                            datsetFileItem.getItemName(), EntityType.CvTerms);


                        gobiiDataSetExtracts.push(new GobiiDataSetExtract(gobiiFileType,
                            false,
                            null,
                            scope$.gobiiExtractFilterType,
                            null,
                            null,
                            markerFileName,
                            null,
                            datasetType,
                            platforms,
                            null,
                            null,
                            dataSet,
                            null));
                    });
                } else if (scope$.gobiiExtractFilterType === GobiiExtractFilterType.BY_MARKER) {
                    gobiiDataSetExtracts.push(new GobiiDataSetExtract(gobiiFileType,
                        false,
                        null,
                        scope$.gobiiExtractFilterType,
                        markerList,
                        null,
                        markerFileName,
                        null,
                        datasetType,
                        platforms,
                        null,
                        null,
                        null,
                        markerGroups));
                } else if (scope$.gobiiExtractFilterType === GobiiExtractFilterType.BY_SAMPLE) {
                    gobiiDataSetExtracts.push(new GobiiDataSetExtract(gobiiFileType,
                        false,
                        null,
                        scope$.gobiiExtractFilterType,
                        null,
                        sampleList,
                        sampleFileName,
                        sampleListType,
                        datasetType,
                        platforms,
                        principleInvestigator,
                        project,
                        null,
                        null));
                } else {
                    scope$.handleAddMessage("Unhandled extract filter type: " + GobiiExtractFilterType[scope$.gobiiExtractFilterType]);
                }
            }
        );


        gobiiExtractorInstructions.push(
            new GobiiExtractorInstruction(
                gobiiDataSetExtracts,
                submitterContactid,
                null,
                mapsetIds)
        );


        let fileName: string = jobId;

        let extractorInstructionFilesDTORequest: ExtractorInstructionFilesDTO =
            new ExtractorInstructionFilesDTO(gobiiExtractorInstructions,
                fileName);

        let extractorInstructionFilesDTOResponse: ExtractorInstructionFilesDTO = null;

        this.dtoRequestServiceExtractorFile.post(new DtoRequestItemExtractorSubmission(extractorInstructionFilesDTORequest))
            .subscribe(extractorInstructionFilesDTO => {
                    extractorInstructionFilesDTOResponse = extractorInstructionFilesDTO;
                    scope$.handleAddMessage("Extractor instruction file created on server: "
                        + extractorInstructionFilesDTOResponse.getInstructionFileName());
                },
                headerResponse => {

                    scope$.handleResponseHeader(headerResponse);
                });
        
    }

}
