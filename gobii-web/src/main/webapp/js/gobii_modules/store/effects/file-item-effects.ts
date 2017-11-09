import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {Actions, Effect} from '@ngrx/effects';
import 'rxjs/add/operator/switchMap'
import 'rxjs/add/observable/of';

import * as fileItemActions from '../actions/fileitem-action'
import * as treeNodeActions from '../actions/treenode-action'
import {TreeStructureService} from "../../services/core/tree-structure-service";
import {GobiiTreeNode} from "../../model/GobiiTreeNode";
import * as fromRoot from '../reducers';
import * as historyAction from '../../store/actions/history-action';
import {ExtractorItemType} from "../../model/type-extractor-item";
import {GobiiFileItem} from "../../model/gobii-file-item";
import {Observable} from "rxjs/Observable";
import {Store} from "@ngrx/store";
import {FileItemService} from "../../services/core/file-item-service";
import {NameIdFilterParamTypes} from "../../model/type-nameid-filter-params";
import {EntitySubType, EntityType} from "../../model/type-entity";
import "rxjs/add/operator/mergeMap"

@Injectable()
export class FileItemEffects {

    /**
     * The canonical use case for effects is when you want to add the results of an asynchronous
     * operation to the store. In Angular/Redux speak, it is said that doing so in a reducer
     * would be adding a side effect. I think of this as meaning that if you have an asynch operation,
     * you are not guaranteeing that when the reducer function returns, the state will be as you believe
     * it should be based on the action you submitted -- not until the subscribe's callback gets called
     * in any case. So, the idea is that the effects are like extensions to the reducer, such that actions
     * get seen here as well as in the reducer. Thus, if you have an action that is handled _only_ by an
     * effect, the effect can subscribe, for example, to an http request and once all the data have been
     * retrieved, then the effect will do an old fashioned dispatch where the action is handled directly
     * by the reducer.
     * The effects in this project look a little different. First of call, the FileItemSErvice class does what
     * you would normally do in an effect -- does an asynchronous call to web services and, upon fulfillment
     * of the subscription, adds the items to the store. The only article I can find that deals with this issue head on is
     * https://medium.com/@flashMasterJim/the-basics-of-ngrx-effects-effect-and-async-middleware-for-ngrx-store-in-angular-2-f25587493329
     * by Jim Lunch. He says "You might be thinking, “What if you have the smart component just communicate with
     * another service that calls for async data, and then when that call comes back the service dispatches an event to
     * the store with the returned data as a payload?”, and in a way you’d be right!" So, am I right, or not? Only
     * time will tell: so far,this seems to be working ok.
     * Now, secondly, it turns out that you actually can handle an action in both an effect _and_ a reducer.
     * Based on experimentation and googling, I found that the effect should get called _after_ the
     * reducer, and this is by the design. In other words, another type of side effect is where you want to
     * do something as a result of adding something to state, but would violate the reducer contract by doing
     * so in the reducer. I use this pattern all over the palce. For example, when I set a file item to be
     * in the extract, I want the tree state to change along with it. This may be a design flaw -- making
     * file items more complex by combining them with the TreeNode type would have the payoff of being able to
     * maintain one state. I suspect that in the future we will want the clean separation, and in any case, this
     * is how it is for now. I think in a sense that this pattern fits with the canonical use case because really,
     * we want to do something to the tree, but the thing we want to do to it depends on the result of an asynchronous
     * action -- that of adding stuff to the file item store. I asked Gerald sans about this second usage
     * at the Angular Summit and he didn't seem to get my question: he said unequivocally that the only
     * purpose for efffects is what I describe. But I suspect that if I showed him what I am trying to do
     * he would agree. Again, I am quite sure that what I am doing here is an intended use pattern (there
     * was even an ngrx/pltform issue to ensure that an action in an effect is called after the same action
     * in a reducer.
     *
     *
     * @type {Observable<PlaceTreeNodeAction>}
     */


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
    replaceSameByCompoundId$ = this.actions$
        .ofType(fileItemActions.REPLACE_ITEM_OF_SAME_COMPOUND_ID)
        .map((action: fileItemActions.ReplaceItemOfSameCompoundIdAction) => {
                let treeNode: GobiiTreeNode = this.treeStructureService.makeTreeNodeFromFileItem(action.payload.gobiiFileitemToReplaceWith);
                return new treeNodeActions.PlaceTreeNodeAction(treeNode);
            }
        );


    @Effect()
    replaceInExtract$ = this.actions$
        .ofType(fileItemActions.REPLACE_IN_EXTRACT_BY_ITEM_ID)
        .switchMap((action: fileItemActions.ReplaceInExtractByItemIdAction) => {

                //  This action is triggered by the ubiguitous NameIdListBoxComponent
                // as such, there are business behaviors that must be implemented here.
                // you cannot trigger an ASYNCH requrest such as loadWithFilterParams() from within
                // the subscribe of a reducer.select(): if you do, you end up with an infinite loop
                return Observable.create(observer => {

                    let fileItemToReplaceWithUniqueId: string = action.payload.itemIdToReplaceItWith;
                    let fileItemCurrentlyInExtractUniqueId: string = action.payload.itemIdCurrentlyInExtract;
                    this.store.select(fromRoot.getAllFileItems)
                        .subscribe(all => {
                                let fileItemToReplaceWith: GobiiFileItem = all.find(fi => fi.getFileItemUniqueId() === fileItemToReplaceWithUniqueId);

                                // RUN FILTERED QUERY TO GET CHILD ITEMS WHEN NECESSARY
                                let nameIdFilterParamType: NameIdFilterParamTypes = NameIdFilterParamTypes.UNKNOWN;
                                let filterValue: string = fileItemToReplaceWith.getItemId();

                                if (fileItemToReplaceWith.getEntityType() === EntityType.CONTACT
                                    && (fileItemToReplaceWith.getEntitySubType() === EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR )) {
                                    nameIdFilterParamType = NameIdFilterParamTypes.PROJECTS_BY_CONTACT;
                                } else if (fileItemToReplaceWith.getEntityType() === EntityType.PROJECT) {
                                    nameIdFilterParamType = NameIdFilterParamTypes.EXPERIMENTS_BY_PROJECT;
                                } else if (fileItemToReplaceWith.getEntityType() === EntityType.EXPERIMENT) {
                                    nameIdFilterParamType = NameIdFilterParamTypes.DATASETS_BY_EXPERIMENT;
                                }

                                if ((nameIdFilterParamType !== NameIdFilterParamTypes.UNKNOWN && filterValue != null)) {

                                    this.fileItemService.makeFileLoadActions(action.payload.gobiiExtractFilterType,
                                        nameIdFilterParamType,
                                        filterValue).subscribe(loadFileItemListAction => {

                                            observer.next(loadFileItemListAction);


                                        },
                                        error => {
                                            this.store.dispatch(new historyAction.AddStatusMessageAction(error))
                                        }
                                    );


                                    // LOAD THE CORRESPONDING TREE NODE FOR THE SELECTED ITEM

                                } // if we had a filter to dispatch

                                if (fileItemToReplaceWith.getExtractorItemType() != ExtractorItemType.LABEL) {
                                    let treeNode: GobiiTreeNode = this.treeStructureService.makeTreeNodeFromFileItem(fileItemToReplaceWith);
                                    observer.next(new treeNodeActions.PlaceTreeNodeAction(treeNode));

                                } else {
                                    observer.next(new fileItemActions.RemoveFromExractByItemIdAction(fileItemCurrentlyInExtractUniqueId));
                                }

                            },
                            error => {
                                this.store.dispatch(new historyAction.AddStatusMessageAction(error))
                            }
                        ).unsubscribe(); // fromRoot.getAllFileItems()

                }).mergeMap(actions => {

                    // I think that if the loadWithFilterParams() returned an observable, such that it was
                    // "finished" after everything was dispatched, this would work. Or something like it
                    // something like this might work: https://github.com/Reactive-Extensions/RxJS/blob/master/doc/api/core/operators/expand.md
                    // In other words, loadWithFilterParams has to really return an observable of an array of GobiiFileItems,
                    // such that the load action would actually be done _here_
                    // we need to police every other place where loadWithFilterParams is used (if anywhere):
                    // those will have to drive effects, as well.
                    // the call to

                    return Observable.of(actions);
                });

            } //switchMap()
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
                            },
                            error => {
                                this.store.dispatch(new historyAction.AddStatusMessageAction(error))
                            }).unsubscribe();

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

    @Effect()
    loadFileItem = this.actions$
        .ofType(fileItemActions.LOAD_FILE_ITEM)
        .map((action: fileItemActions.LoadFileItemtAction) => {
                if (action.payload.selectForExtract) {
                    return new fileItemActions.AddToExtractAction(action.payload.gobiiFileItem);
                }
            }
        );

    @Effect()
    setExtractType = this.actions$
        .ofType(fileItemActions.SET_EXTRACT_TYPE)
        .map((action: fileItemActions.SetExtractType) => {
                return new treeNodeActions.SelectExtractType(action.payload.gobiiExtractFilterType);
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
                private fileItemService: FileItemService,
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
