import {Injectable} from "@angular/core";
import {ContainerType, GobiiTreeNode} from "../../model/GobiiTreeNode";
import {EntitySubType, EntityType} from "../../model/type-entity";
import {Labels} from "../../views/entity-labels";
import {ExtractorItemType} from "../../model/file-model-node";
import {GobiiExtractFilterType} from "../../model/type-extractor-filter";
import {CvFilterType} from "../../model/cv-filter-type";
import {GobiiFileItem} from "../../model/gobii-file-item";
import {HeaderStatusMessage} from "../../model/dto-header-status-message";
import {GobiiExtractFormat} from "../../model/type-extract-format";
import {ProcessType} from "../../model/type-process";
import {NameIdService} from "./name-id-service";
import {NameIdRequestParams} from "../../model/name-id-request-params";
import * as fileItemActions from '../../store/actions/fileitem-action'
import * as fromRoot from '../../store/reducers';

import {Store} from "@ngrx/store";

@Injectable()
export class FileItemService {

    constructor(private nameIdService: NameIdService,
                private store: Store<fromRoot.State>,) {

    }

    public loadNameIdsToFileItems(gobiiExtractFilterType: GobiiExtractFilterType,
                                  nameIdRequestParams: NameIdRequestParams) {

        this.nameIdService.get(nameIdRequestParams)
            .subscribe(nameIds => {
                    if (nameIds && ( nameIds.length > 0 )) {

                        let fileItems: GobiiFileItem[] = [];

                        nameIds.forEach(n => {
                            let currentFileItem: GobiiFileItem =
                                GobiiFileItem.build(
                                    gobiiExtractFilterType,
                                    ProcessType.CREATE)
                                    .setExtractorItemType(ExtractorItemType.ENTITY)
                                    .setEntityType(nameIdRequestParams.getEntityType())
                                    .setCvFilterType(CvFilterType.UNKNOWN)
                                    .setItemId(n.id)
                                    .setItemName(n.name)
                                    .setChecked(false)
                                    .setRequired(false)
                                    .setParentEntityType(nameIdRequestParams.getRefTargetEntityType())
                                    .setParentItemId(nameIdRequestParams.getEntityFilterValue());


                            fileItems.push(currentFileItem);
                        });

                        let loadAction: fileItemActions.LoadFilteredItemsAction = new fileItemActions.LoadFilteredItemsAction(
                            {
                                gobiiFileItems: fileItems,
                                nameIdRequestParams: nameIdRequestParams,
                            }
                        );
                        this.store.dispatch(loadAction)

                    }
                },
                responseHeader => {
                    //this.handleHeaderStatus(responseHeader);
                    console.log(responseHeader);
                });
    }

}
