System.register(["@angular/core", "../../model/name-id", "../../model/http-values", "@angular/http", '../core/authentication.service', "rxjs/Observable", "rxjs/add/operator/map", "rxjs/add/operator/catch", 'rxjs/add/observable/throw'], function(exports_1, context_1) {
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
    var core_1, name_id_1, http_values_1, http_1, authentication_service_1, Observable_1;
    var NameIdListService;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (name_id_1_1) {
                name_id_1 = name_id_1_1;
            },
            function (http_values_1_1) {
                http_values_1 = http_values_1_1;
            },
            function (http_1_1) {
                http_1 = http_1_1;
            },
            function (authentication_service_1_1) {
                authentication_service_1 = authentication_service_1_1;
            },
            function (Observable_1_1) {
                Observable_1 = Observable_1_1;
            },
            function (_1) {},
            function (_2) {},
            function (_3) {}],
        execute: function() {
            NameIdListService = (function () {
                function NameIdListService(_http, _authenticationService) {
                    this._http = _http;
                    this._authenticationService = _authenticationService;
                }
                NameIdListService.prototype.getAString = function () {
                    return 'a string';
                };
                NameIdListService.prototype.getNameIds = function () {
                    var _this = this;
                    var requestBody = JSON.stringify({
                        "processType": "READ",
                        "dtoHeaderAuth": { "userName": null, "password": null, "token": null },
                        "dtoHeaderResponse": { "succeeded": true, "statusMessages": [] },
                        "entityType": "DBTABLE",
                        "entityName": "datasetnames",
                        "namesById": {},
                        "filter": null
                    });
                    // **************************
                    // this works:
                    // return Observable.create(observer => {
                    //     observer.next(this.mapToNameIds(JSON.parse('{"foo":"bar"}')));
                    //     observer.complete();
                    // })
                    // ***************************
                    var scope$ = this;
                    var existingToken = this._authenticationService.getToken();
                    if (existingToken) {
                        var headers_1 = http_values_1.HttpValues.makeTokenHeaders(existingToken);
                        return Observable_1.Observable.create(function (observer) {
                            // observer.next(scope$.mapToNameIds(JSON.parse('{"foo" : "bar"}')));
                            // observer.complete();
                            _this._http
                                .post("load/nameidlist", requestBody, { headers: headers_1 })
                                .map(function (response) { return response.json(); })
                                .subscribe(function (json) {
                                scope$.nameIds = scope$.mapToNameIds(json);
                                observer.next(scope$.nameIds);
                                observer.complete();
                            });
                        }); // observer.create
                    }
                    else {
                        return Observable_1.Observable.create(function (observer) {
                            _this._authenticationService
                                .authenticate(null, null)
                                .map(function (h) { return h.getToken(); })
                                .subscribe(function (token) {
                                var newTokenHeaders = http_values_1.HttpValues.makeTokenHeaders(token);
                                scope$._http
                                    .post("load/nameidlist", requestBody, { headers: newTokenHeaders })
                                    .map(function (response) { return response.json(); })
                                    .subscribe(function (json) {
                                    scope$.nameIds = scope$.mapToNameIds(json);
                                    observer.next(scope$.nameIds);
                                    observer.complete();
                                });
                            }, function (error) { return console.log(error.message); });
                        }); // observer create
                    } // if-else we have a token
                }; // getPiNameIds()
                NameIdListService.prototype.mapToNameIds = function (json) {
                    // for now, log the json and create a fake list
                    console.log(json);
                    return [new name_id_1.NameId(1, 'foo'), new name_id_1.NameId(2, 'bar')];
                };
                NameIdListService.prototype.getFake = function () {
                    var scope$ = this;
                    return Observable_1.Observable.create(function (observer) {
                        console.log("got fake");
                        observer.next(scope$.mapToNameIds(JSON.parse('{"foo" : "bar"}')));
                        observer.complete();
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