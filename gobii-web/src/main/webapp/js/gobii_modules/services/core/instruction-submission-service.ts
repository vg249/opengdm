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

@Injectable()
export class InstructionSubmissionService {

    constructor(private store: Store<fromRoot.State>,) {
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


    // public submitReady(gobiiFileItems:GobiiFileItem[], gobiiExtractFilterType:GobiiExtractFilterType) {
    //
    //     let returnVal:boolean = false;
    //
    //     if (gobiiExtractFilterType === GobiiExtractFilterType.WHOLE_DATASET) {
    //
    //         let returnVal  =
    //             gobiiFileItems
    //                 .filter(fi =>
    //                     fi.getGobiiExtractFilterType() === this.gobiiExtractFilterType
    //                     && fi.getExtractorItemType() === ExtractorItemType.ENTITY
    //                     && fi.getEntityType() === EntityType.DataSets
    //                 )
    //                 .length > 0;
    //     }
    //
    //     return returnVal;
    // }
}
