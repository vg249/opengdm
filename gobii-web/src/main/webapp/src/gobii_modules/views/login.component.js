System.register(["@angular/core", "@angular/router", "../services/core/authentication.service", "../services/core/dto-request.service", "../services/app/dto-request-item-serverconfigs", "@angular/common", "../services/core/view-id-generator-service", "../services/core/type-control"], function (exports_1, context_1) {
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
    var core_1, router_1, authentication_service_1, dto_request_service_1, dto_request_item_serverconfigs_1, common_1, view_id_generator_service_1, type_control_1, LoginComponent;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (router_1_1) {
                router_1 = router_1_1;
            },
            function (authentication_service_1_1) {
                authentication_service_1 = authentication_service_1_1;
            },
            function (dto_request_service_1_1) {
                dto_request_service_1 = dto_request_service_1_1;
            },
            function (dto_request_item_serverconfigs_1_1) {
                dto_request_item_serverconfigs_1 = dto_request_item_serverconfigs_1_1;
            },
            function (common_1_1) {
                common_1 = common_1_1;
            },
            function (view_id_generator_service_1_1) {
                view_id_generator_service_1 = view_id_generator_service_1_1;
            },
            function (type_control_1_1) {
                type_control_1 = type_control_1_1;
            }
        ],
        execute: function () {
            LoginComponent = /** @class */ (function () {
                function LoginComponent(route, router, locationStrategy, authenticationService, dtoRequestServiceServerConfigs, viewIdGeneratorService) {
                    this.route = route;
                    this.router = router;
                    this.locationStrategy = locationStrategy;
                    this.authenticationService = authenticationService;
                    this.dtoRequestServiceServerConfigs = dtoRequestServiceServerConfigs;
                    this.viewIdGeneratorService = viewIdGeneratorService;
                    this.model = {};
                    this.loading = false;
                    this.userAgreed = false;
                    this.inputDisabled = true;
                    this.typeControl = type_control_1.TypeControl;
                }
                LoginComponent.prototype.ngOnInit = function () {
                    // reset login status
                    //this.authenticationService.logout();
                    var _this = this;
                    // get return url from route parameters or default to '/'
                    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '';
                    this.dtoRequestServiceServerConfigs
                        .get(new dto_request_item_serverconfigs_1.DtoRequestItemServerConfigs(), false)
                        .subscribe(function (serverConfigs) {
                        var foo = "foo";
                        if (serverConfigs && (serverConfigs.length > 0)) {
                            var path_1 = _this.locationStrategy.path();
                            var cropServerConfig = serverConfigs
                                .find(function (c) {
                                return path_1.indexOf(c.contextRoot) > -1;
                            });
                            if (cropServerConfig) {
                                _this.confidentialityNotice = cropServerConfig.confidentialityNotice;
                                if (_this.confidentialityNotice) {
                                    _this.inputDisabled = true;
                                }
                                else {
                                    _this.inputDisabled = false;
                                }
                            }
                        }
                    });
                };
                LoginComponent.prototype.onTermAgreeCheck = function () {
                    this.inputDisabled = !this.userAgreed;
                };
                LoginComponent.prototype.login = function () {
                    var _this = this;
                    this.loading = true;
                    //        this.router.navigate(['project']);
                    this.loading = false;
                    this.authenticationService.authenticate(this.model.username.trim(), this.model.password.trim())
                        .subscribe(function (dtoHeaderAuth) {
                        if (dtoHeaderAuth.getToken() != null) {
                            //                        this.router.navigate([this.returnUrl]);
                            _this.router.navigate(['']);
                        }
                    }, function (error) {
                        _this.message = error;
                        _this.loading = false;
                    });
                };
                LoginComponent = __decorate([
                    core_1.Component({
                        //    moduleId: module.id,
                        template: "\n        <BR>\n        <BR>\n        <BR>\n        <BR>\n        <BR>\n        <div class=\"container\">\n            <div class=\"col-md-6 col-md-offset-3\">\n                <div *ngIf=\"confidentialityNotice\">\n                    <h3 class=\"text-warning\">{{confidentialityNotice}}</h3>\n                    <p-checkbox label=\"Agree To Terms\"\n                                [(ngModel)]=\"userAgreed\" \n                                binary=\"true\"\n                                (onChange)=\"onTermAgreeCheck()\"\n                                [id]=\"viewIdGeneratorService.makeStandardId(typeControl.LOGIN_AGREE_TO_TERMS_CHECKBOX)\"\n                    ></p-checkbox>\n                </div>\n                <h2>GDM Login</h2>\n                <form name=\"form\" (ngSubmit)=\"f.form.valid && login()\" #f=\"ngForm\" novalidate>\n                    <div class=\"form-group\" [ngClass]=\"{ 'has-error': f.submitted && !username.valid }\">\n                        <label for=\"username\">Username</label>\n                        <input [disabled]=\"inputDisabled\" \n                               type=\"text\" \n                               class=\"form-control\" \n                               name=\"username\" \n                               [(ngModel)]=\"model.username\"\n                               #username=\"ngModel\" required\n                               [id]=\"viewIdGeneratorService.makeStandardId(typeControl.LOGIN_USER_NAME_INPUT)\"/>\n                        <div *ngIf=\"f.submitted && !username.valid\" class=\"help-block\">Username is required</div>\n                    </div>\n                    <div class=\"form-group\" [ngClass]=\"{ 'has-error': f.submitted && !password.valid }\">\n                        <label for=\"password\">Password</label>\n                        <input [disabled]=\"inputDisabled\" \n                               type=\"password\" \n                               class=\"form-control\" \n                               name=\"password\" \n                               [(ngModel)]=\"model.password\"\n                               #password=\"ngModel\" \n                               required\n                               [id]=\"viewIdGeneratorService.makeStandardId(typeControl.LOGIN_PASSWORD_INPUT)\"/>\n                        <div *ngIf=\"f.submitted && !password.valid\" class=\"help-block\">Password is required</div>\n                    </div>\n                    <div class=\"form-group\">\n                        <button \n                                [disabled]=\"inputDisabled\" \n                                class=\"btn btn-primary\"\n                                [id]=\"viewIdGeneratorService.makeStandardId(typeControl.LOGIN_SUBMIT_BUTTON)\">Login</button>\n                        <img *ngIf=\"loading\"\n                             src=\"data:image/gif;base64,R0lGODlhEAAQAPIAAP///wAAAMLCwkJCQgAAAGJiYoKCgpKSkiH/C05FVFNDQVBFMi4wAwEAAAAh/hpDcmVhdGVkIHdpdGggYWpheGxvYWQuaW5mbwAh+QQJCgAAACwAAAAAEAAQAAADMwi63P4wyklrE2MIOggZnAdOmGYJRbExwroUmcG2LmDEwnHQLVsYOd2mBzkYDAdKa+dIAAAh+QQJCgAAACwAAAAAEAAQAAADNAi63P5OjCEgG4QMu7DmikRxQlFUYDEZIGBMRVsaqHwctXXf7WEYB4Ag1xjihkMZsiUkKhIAIfkECQoAAAAsAAAAABAAEAAAAzYIujIjK8pByJDMlFYvBoVjHA70GU7xSUJhmKtwHPAKzLO9HMaoKwJZ7Rf8AYPDDzKpZBqfvwQAIfkECQoAAAAsAAAAABAAEAAAAzMIumIlK8oyhpHsnFZfhYumCYUhDAQxRIdhHBGqRoKw0R8DYlJd8z0fMDgsGo/IpHI5TAAAIfkECQoAAAAsAAAAABAAEAAAAzIIunInK0rnZBTwGPNMgQwmdsNgXGJUlIWEuR5oWUIpz8pAEAMe6TwfwyYsGo/IpFKSAAAh+QQJCgAAACwAAAAAEAAQAAADMwi6IMKQORfjdOe82p4wGccc4CEuQradylesojEMBgsUc2G7sDX3lQGBMLAJibufbSlKAAAh+QQJCgAAACwAAAAAEAAQAAADMgi63P7wCRHZnFVdmgHu2nFwlWCI3WGc3TSWhUFGxTAUkGCbtgENBMJAEJsxgMLWzpEAACH5BAkKAAAALAAAAAAQABAAAAMyCLrc/jDKSatlQtScKdceCAjDII7HcQ4EMTCpyrCuUBjCYRgHVtqlAiB1YhiCnlsRkAAAOwAAAAAAAAAAAA==\"/>\n                    </div>\n                </form>\n                <span>{{message}}</span>\n            </div>\n        </div>"
                    })
                    // this component and the entire login mechanism (AuthGuard, etc.) are borrowed form
                    // http://jasonwatmore.com/post/2016/09/29/angular-2-user-registration-and-login-example-tutorial
                    ,
                    __metadata("design:paramtypes", [router_1.ActivatedRoute,
                        router_1.Router,
                        common_1.LocationStrategy,
                        authentication_service_1.AuthenticationService,
                        dto_request_service_1.DtoRequestService,
                        view_id_generator_service_1.ViewIdGeneratorService])
                ], LoginComponent);
                return LoginComponent;
            }());
            exports_1("LoginComponent", LoginComponent);
        }
    };
});
//# sourceMappingURL=login.component.js.map