import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {Effect, Actions, toPayload} from '@ngrx/effects';
import 'rxjs/add/operator/switchMap'
import 'rxjs/add/observable/of';

import * as fileItemActions from '../actions/fileitem-action'
import * as treeNodeActions from '../actions/treenode-action'
import {TreeStructureService} from "../../services/core/tree-structure-service";
import {GobiiTreeNode} from "../../model/GobiiTreeNode";
import {NameIdService} from "../../services/core/name-id-service";
import {NameIdRequestParams} from "../../model/name-id-request-params";
import {GobiiExtractFilterType} from "../../model/type-extractor-filter";
import {CvFilterType} from "../../model/cv-filter-type";
import {ExtractorItemType} from "../../model/file-model-node";
import {GobiiFileItem} from "../../model/gobii-file-item";
import {ProcessType} from "../../model/type-process";
import {Observable} from "rxjs/Observable";
import {SELECT_FOR_EXTRACT_BY_FILE_ITEM_ID} from "../actions/fileitem-action";

@Injectable()
export class FileItemEffects {

    @Effect()
    selectForExtract$ = this.actions$
        .ofType(fileItemActions.SELECT_FOR_EXTRACT)
        .map((action: fileItemActions.SelectForExtractAction) => {
                let treeNode: GobiiTreeNode = this.treeStructureService.makeTreeNodeFromFileItem(action.payload);
                return new treeNodeActions.PlaceTreeNodeAction(treeNode);
            }
        );


    @Effect()
    deSelectFromExtract$ = this.actions$
        .ofType(fileItemActions.DESELECT_FOR_EXTRACT)
        .map((action: fileItemActions.SelectForExtractAction) => {
                return new treeNodeActions.DeActivateFromExtractAction(action.payload.getFileItemUniqueId());
            }
        );





    // @Effect()
    // setEntityFilter$ = this.actions$
    //     .ofType(fileItemActions.SET_ENTITY_FILTER)
    //     .switchMap((action: fileItemActions.SetEntityFilter)  => {
    //
    //         let payload = action.payload;
    //
    //         return Observable.create(observer => {
    //
    //
    //             this.nameIdService.get(payload.nameIdRequestParams)
    //                 .subscribe(nameIds => {
    //                         if (nameIds && ( nameIds.length > 0 )) {
    //
    //
    //                             nameIds.forEach(n => {
    //                                 let currentFileItem: GobiiFileItem =
    //                                     GobiiFileItem.build(
    //                                         payload.gobiiExtractFilterType,
    //                                         ProcessType.CREATE)
    //                                         .setExtractorItemType(ExtractorItemType.ENTITY)
    //                                         .setEntityType(payload.nameIdRequestParams.getEntityType())
    //                                         .setCvFilterType(CvFilterType.UNKNOWN)
    //                                         .setItemId(n.id)
    //                                         .setItemName(n.name)
    //                                         .setChecked(false)
    //                                         .setRequired(false)
    //                                         .setParentEntityType(payload.nameIdRequestParams.getRefTargetEntityType())
    //                                         .setParentItemId(payload.nameIdRequestParams.getFkEntityFilterValue());
    //
    //                                 //fileItems.push(currentFileItem);
    //                                 observer.next(currentFileItem);
    //
    //                             });
    //
    //                             //new fileItemActions.LoadAction(fileItems);
    //                         }
    //                     },
    //                     responseHeader => {
    //                         console.log(responseHeader);
    //                     });
    //
    //         }).map( gfi => {
    //             return new fileItemActions.LoadAction([gfi]);
    //         })
    //
    //
    //     }); //switch map


    constructor(private actions$: Actions,
                private treeStructureService: TreeStructureService,
                private router: Router) {
    }

}


/*


    @Effect()
    setEntityFilter$ = this.actions$
        .ofType(fileItemActions.SET_ENTITY_FILTER)
        .map((action: fileItemActions.SetEntityFilter) => {

            }
        );

*
*                 this.nameIdService.get(nameIdRequestParams)
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
                                        .setParentItemId(nameIdRequestParams.getFkEntityFilterValue());

                                fileItems.push(currentFileItem);

                            });

                            return new fileItemActions.LoadAction(fileItems);
                        }
                    },
                    responseHeader => {
                        console.log(responseHeader);
                    });
*/
