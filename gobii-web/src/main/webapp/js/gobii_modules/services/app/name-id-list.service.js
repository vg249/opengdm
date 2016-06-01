System.register(["angular2/core", "angular2/http", "rxjs/Observable", '../core/authentication.service', "rxjs/add/operator/map", "rxjs/add/operator/catch", 'rxjs/add/observable/throw', "../../model/header-names"], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
        var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
        if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
        else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
        return c > 3 && r && Object.defineProperty(target, key, r), r;
    };
    var __metadata = (this && this.__metadata) || function (k, v) {
        if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
    };
    var core_1, http_1, Observable_1, authentication_service_1, header_names_1;
    var NameIdListService;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (http_1_1) {
                http_1 = http_1_1;
            },
            function (Observable_1_1) {
                Observable_1 = Observable_1_1;
            },
            function (authentication_service_1_1) {
                authentication_service_1 = authentication_service_1_1;
            },
            function (_1) {},
            function (_2) {},
            function (_3) {},
            function (header_names_1_1) {
                header_names_1 = header_names_1_1;
            }],
        execute: function() {
            NameIdListService = (function () {
                function NameIdListService(_http, _authenticationService) {
                    this._http = _http;
                    this._authenticationService = _authenticationService;
                }
                NameIdListService.prototype.getNameIds = function () {
                    var requestBody = JSON.stringify({
                        "processType": "READ",
                        "dtoHeaderAuth": { "userName": null, "password": null, "token": null },
                        "dtoHeaderResponse": { "succeeded": true, "statusMessages": [] },
                        "entityType": "DBTABLE",
                        "entityName": "datasetnames",
                        "namesById": {},
                        "filter": null
                    });
                    var token = this._authenticationService.getToken();
                    if (!token) {
                        this._authenticationService
                            .authenticate(null, null)
                            .subscribe(function (h) {
                            token = h.getToken();
                        }, function (error) { return console.log(error.message); });
                    }
                    if (!token) {
                        Observable_1.Observable.throw(Error("no authentication token"));
                    }
                    var headers = new http_1.Headers();
                    headers.append('Content-Type', 'application/json');
                    headers.append('Accept', 'application/json');
                    headers.append(header_names_1.HeaderNames.headerToken, token);
                    return this
                        ._http
                        .post("load/nameidlist", requestBody, { headers: headers })
                        .map(function (response) {
                        var payload = response.json();
                        console.log(payload);
                        console.log(response.headers);
                        return [];
                    });
                };
                NameIdListService = __decorate([
                    core_1.Injectable(), 
                    __metadata('design:paramtypes', [http_1.Http, authentication_service_1.AuthenticationService])
                ], NameIdListService);
                return NameIdListService;
            }());
            exports_1("NameIdListService", NameIdListService);
        }
    }
});
//# sourceMappingURL=name-id-list.service.js.map