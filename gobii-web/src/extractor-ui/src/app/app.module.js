System.register(["@angular/platform-browser", "@angular/core", "./app-routing.module", "./app.component", "@ngrx/store", "@ngrx/effects", "@ngrx/router-store", "@ngrx/store-devtools", "../environments/environment", "@angular/platform-browser/animations"], function (exports_1, context_1) {
    "use strict";
    var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
        var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
        if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
        else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
        return c > 3 && r && Object.defineProperty(target, key, r), r;
    };
    var platform_browser_1, core_1, app_routing_module_1, app_component_1, store_1, effects_1, router_store_1, store_devtools_1, environment_1, animations_1, AppModule;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [
            function (platform_browser_1_1) {
                platform_browser_1 = platform_browser_1_1;
            },
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (app_routing_module_1_1) {
                app_routing_module_1 = app_routing_module_1_1;
            },
            function (app_component_1_1) {
                app_component_1 = app_component_1_1;
            },
            function (store_1_1) {
                store_1 = store_1_1;
            },
            function (effects_1_1) {
                effects_1 = effects_1_1;
            },
            function (router_store_1_1) {
                router_store_1 = router_store_1_1;
            },
            function (store_devtools_1_1) {
                store_devtools_1 = store_devtools_1_1;
            },
            function (environment_1_1) {
                environment_1 = environment_1_1;
            },
            function (animations_1_1) {
                animations_1 = animations_1_1;
            }
        ],
        execute: function () {
            AppModule = /** @class */ (function () {
                function AppModule() {
                }
                AppModule = __decorate([
                    core_1.NgModule({
                        declarations: [
                            app_component_1.AppComponent
                        ],
                        imports: [
                            platform_browser_1.BrowserModule,
                            app_routing_module_1.AppRoutingModule,
                            store_1.StoreModule.forRoot({}, {}),
                            effects_1.EffectsModule.forRoot([]),
                            router_store_1.StoreRouterConnectingModule.forRoot(),
                            store_devtools_1.StoreDevtoolsModule.instrument({ maxAge: 25, logOnly: environment_1.environment.production }),
                            animations_1.BrowserAnimationsModule
                        ],
                        providers: [],
                        bootstrap: [app_component_1.AppComponent]
                    })
                ], AppModule);
                return AppModule;
            }());
            exports_1("AppModule", AppModule);
        }
    };
});
//# sourceMappingURL=app.module.js.map