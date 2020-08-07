System.register(["rxjs/add/operator/catch", "rxjs/add/operator/do", "rxjs/add/operator/exhaustMap", "rxjs/add/operator/map", "rxjs/add/operator/take", "@angular/core", "@angular/router", "@ngrx/effects", "../../services/core/tree-structure-service", "../actions/treenode-action", "rxjs/operators"], function (exports_1, context_1) {
    "use strict";
    var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
        var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
        if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
        else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
        return c > 3 && r && Object.defineProperty(target, key, r), r;
    };
    var __metadata = (this && this.__metadata) || function (k, v) {
        if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
    };
    var core_1, router_1, effects_1, tree_structure_service_1, treeNodeActions, operators_1, TreeEffects;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [
            function (_1) {
            },
            function (_2) {
            },
            function (_3) {
            },
            function (_4) {
            },
            function (_5) {
            },
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (router_1_1) {
                router_1 = router_1_1;
            },
            function (effects_1_1) {
                effects_1 = effects_1_1;
            },
            function (tree_structure_service_1_1) {
                tree_structure_service_1 = tree_structure_service_1_1;
            },
            function (treeNodeActions_1) {
                treeNodeActions = treeNodeActions_1;
            },
            function (operators_1_1) {
                operators_1 = operators_1_1;
            }
        ],
        execute: function () {
            TreeEffects = /** @class */ (function () {
                function TreeEffects(actions$, treeStructureService, router) {
                    var _this = this;
                    this.actions$ = actions$;
                    this.treeStructureService = treeStructureService;
                    this.router = router;
                    this.initTreeNodes$ = this.actions$.pipe(effects_1.ofType(treeNodeActions.INIT), operators_1.map(function (action) {
                        var initialTreeNodes = _this.treeStructureService.getInitialTree();
                        return new treeNodeActions.InitTree(initialTreeNodes);
                    }));
                    this.placeNodeInTree$ = this.actions$.pipe(effects_1.ofType(treeNodeActions.PLACE_TREE_NODE), operators_1.map(function (action) {
                        return new treeNodeActions.ActivateForExtractAction(action.payload.getId());
                    }));
                    this.clearAll$ = this.actions$.pipe(effects_1.ofType(treeNodeActions.CLEAR_ALL), operators_1.map(function (action) {
                        return new treeNodeActions.InitAction();
                    }));
                }
                __decorate([
                    effects_1.Effect(),
                    __metadata("design:type", Object)
                ], TreeEffects.prototype, "initTreeNodes$", void 0);
                __decorate([
                    effects_1.Effect(),
                    __metadata("design:type", Object)
                ], TreeEffects.prototype, "placeNodeInTree$", void 0);
                __decorate([
                    effects_1.Effect(),
                    __metadata("design:type", Object)
                ], TreeEffects.prototype, "clearAll$", void 0);
                TreeEffects = __decorate([
                    core_1.Injectable(),
                    __metadata("design:paramtypes", [effects_1.Actions,
                        tree_structure_service_1.TreeStructureService,
                        router_1.Router])
                ], TreeEffects);
                return TreeEffects;
            }());
            exports_1("TreeEffects", TreeEffects);
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
        }
    };
});
//# sourceMappingURL=tree-effects.js.map