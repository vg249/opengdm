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
import {NameIdRequestParams} from "../../model/name-id-request-params";
import * as fileItemActions from '../../store/actions/fileitem-action'
import * as fromRoot from '../../store/reducers';

import {Store} from "@ngrx/store";
import {NameIdLabelType} from "../../model/name-id-label-type";
import {NameId} from "../../model/name-id";
import {EntityFilter} from "../../model/type-entity-filter";
import {NameIdFilterParamTypes} from "../../model/type-nameid-filter-params";

@Injectable()
export class InstructionSubmissionService {

    constructor(private store: Store<fromRoot.State>,) {
    }


    public isSubmissionReady(gobiiExtractFilterType: GobiiExtractFilterType): boolean {

        let returnVal: boolean = false;


        this.store.select(fromRoot.getSelectedFileItems)
            .subscribe(all => {

                if (gobiiExtractFilterType === GobiiExtractFilterType.WHOLE_DATASET) {

                    returnVal =
                        all
                            .filter(fi =>
                                fi.getGobiiExtractFilterType() === gobiiExtractFilterType
                                && fi.getExtractorItemType() === ExtractorItemType.ENTITY
                                && fi.getEntityType() === EntityType.DataSets
                            )
                            .length > 0;
                }

            }).unsubscribe();


        return returnVal;
    }

}
