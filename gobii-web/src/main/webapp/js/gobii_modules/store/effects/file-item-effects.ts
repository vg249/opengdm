import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/exhaustMap';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/take';
import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {Effect, Actions} from '@ngrx/effects';
import {of} from 'rxjs/observable/of';

import * as fileItemActions from '../actions/fileitem-action'
import * as treeNodeActions from '../actions/treenode-action'
import {TreeStructureService} from "../../services/core/tree-structure-service";
import {GobiiTreeNode} from "../../model/GobiiTreeNode";

@Injectable()
export class FileItemEffects {
    @Effect()
    loadFileItems$ = this.actions$
        .ofType(fileItemActions.SELECT_FOR_EXTRACT)
        .map((action: fileItemActions.SelectForExtractAction ) => {
                let treeNode: GobiiTreeNode = this.treeStructureService.makeTreeNodeFromFileItem(action.payload);
                return new treeNodeActions.PlaceTreeNodeAction(treeNode);
            }
        );

    constructor(private actions$: Actions,
                private treeStructureService: TreeStructureService,
                private router: Router) {
    }
}

/*
* export class TreeEffects {
    @Effect()
    initTreeNodes$ = this.actions$
        .ofType(treeNodeActions.INIT)
        .map((action: Auth.Login) => action.payload)
        .exhaustMap(auth =>
            this.authService
                .login(auth)
                .map(user => new Auth.LoginSuccess({user}))
                .catch(error => of(new Auth.LoginFailure(error)))
        );
    constructor(
        private actions$: Actions,
        private treeStructureService: TreeStructureService,
        private router: Router
    ) {}
}

* */