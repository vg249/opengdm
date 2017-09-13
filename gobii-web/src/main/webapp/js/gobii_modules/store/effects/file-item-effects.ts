import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {Effect, Actions, toPayload} from '@ngrx/effects';
import 'rxjs/add/operator/switchMap'
import 'rxjs/add/observable/of';

import * as fileItemActions from '../actions/fileitem-action'
import * as treeNodeActions from '../actions/treenode-action'
import {TreeStructureService} from "../../services/core/tree-structure-service";
import {GobiiTreeNode} from "../../model/GobiiTreeNode";
import * as fromRoot from '../reducers';

import {NameIdService} from "../../services/core/name-id-service";
import {FileItemParams} from "../../model/name-id-request-params";
import {GobiiExtractFilterType} from "../../model/type-extractor-filter";
import {CvFilterType} from "../../model/cv-filter-type";
import {ExtractorItemType} from "../../model/file-model-node";
import {GobiiFileItem} from "../../model/gobii-file-item";
import {ProcessType} from "../../model/type-process";
import {Observable} from "rxjs/Observable";
import {ADD_TO_EXTRACT_BY_ITEM_ID} from "../actions/fileitem-action";
import {Store} from "@ngrx/store";

@Injectable()
export class FileItemEffects {

    // this effect acts on the content of the tree nodes (e.g., their names and icons so forth)
    // and then dispatches them to the tree node reducer. The tree node reducer holds the nodes
    // in state. Thus, the hierarchical arrangement of nodes is managed by the reducer in
    // accordance with how the nodes are defined by the tree service.
    @Effect()
    selectForExtract$ = this.actions$
        .ofType(fileItemActions.ADD_TO_EXTRACT)
        .map((action: fileItemActions.AddToExtractAction) => {
                let treeNode: GobiiTreeNode = this.treeStructureService.makeTreeNodeFromFileItem(action.payload);
                return new treeNodeActions.PlaceTreeNodeAction(treeNode);
            }
        );

    @Effect()
    selectForExtractByFileItemId$ = this.actions$
        .ofType(fileItemActions.ADD_TO_EXTRACT_BY_ITEM_ID)
        .switchMap((action: fileItemActions.AddToExtractByItemIdAction) => {

                // this is scary. The store is the single source of truth. The only way to get the fileItem for
                // the fileitem id is to get it from the store, and for that to work here, we need to wrap the
                // select in an Observable.
                return Observable.create(observer => {

                    let fileItemUniqueId: String = action.payload;
                    this.store.select(fromRoot.getAllFileItems)
                        .subscribe(all => {
                            let fileItem: GobiiFileItem = all.find(fi => fi.getFileItemUniqueId() === fileItemUniqueId);
                            let treeNode: GobiiTreeNode = this.treeStructureService.makeTreeNodeFromFileItem(fileItem);
                            observer.next(treeNode);
                            observer.complete();
                        })

                }).map(gfi => {
                    return new treeNodeActions.PlaceTreeNodeAction(gfi)
                })

            } //switchMap()
        );


    @Effect()
    deSelectFromExtract$ = this.actions$
        .ofType(fileItemActions.REMOVE_FROM_EXTRACT)
        .map((action: fileItemActions.RemoveFromExtractAction) => {
                return new treeNodeActions.DeActivateFromExtractAction(action.payload.getFileItemUniqueId());
            }
        );

    @Effect()
    deSelectFromExtractById$ = this.actions$
        .ofType(fileItemActions.REMOVE_FROM_EXTRACT_BY_ITEM_ID)
        .map((action: fileItemActions.RemoveFromExractByItemIdAction) => {
                return new treeNodeActions.DeActivateFromExtractAction(action.payload);
            }
        );


    @Effect()
    deselectAll$ = this.actions$
        .ofType(fileItemActions.REMOVE_ALL_FROM_EXTRACT)
        .map((action: fileItemActions.RemoveAllFromExtractAction) => {
                return new treeNodeActions.ClearAll(action.payload);
            }
        );


    // @Effect()
    // setEntityFilter$ = this.actions$
    //     .ofType(fileItemActions.SET_FILTER_VALUE)
    //     .switchMap((action: fileItemActions.SetFilterValueAction)  => {
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
    //                                         .setSelected(false)
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
                private store: Store<fromRoot.State>,
                private router: Router) {
    }

}


/*


    @Effect()
    setEntityFilter$ = this.actions$
        .ofType(fileItemActions.SET_FILTER_VALUE)
        .map((action: fileItemActions.SetFilterValueAction) => {

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
                                        .setSelected(false)
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
