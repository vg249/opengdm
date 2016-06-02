System.register(["angular2/core", "./name-id-list.service"], function(exports_1, context_1) {
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
    var core_1, name_id_list_service_1;
    var PrincipleInvestigatorService;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (name_id_list_service_1_1) {
                name_id_list_service_1 = name_id_list_service_1_1;
            }],
        execute: function() {
            PrincipleInvestigatorService = (function () {
                function PrincipleInvestigatorService(_nameIdListService) {
                    this._nameIdListService = _nameIdListService;
                    this._nameIdListService = _nameIdListService;
                } // ctor
                PrincipleInvestigatorService.prototype.getNameIds = function () {
                    var _this = this;
                    var astring = this._nameIdListService.getAString();
                    this._nameIdListService.getNameIds()
                        .subscribe(function (nameIds) { return _this.nameIds = nameIds; });
                    //let astring:string =  _nameIdListService.get
                    // let observable:Observable<NameId[]> = this._nameIdListService.getNameIds();
                    // //observable.subscribe()
                    //
                    // let nameIdsReceived: NameId[] = [];
                    // this._nameIdListService.getNameIds()
                    //     .map(nameIds => nameIds)
                    //     .subscribe(nameIds => nameIdsReceived = nameIds);
                    // let nameIds: NameId[] = [
                    // 	{"id": 11, "name": "Mr. Nice"},
                    // 	{"id": 12, "name": "Narco"},
                    // 	{"id": 13, "name": "Bombasto"},
                    // 	{"id": 14, "name": "Celeritas"},
                    // 	{"id": 15, "name": "Magneta"},
                    // 	{"id": 16, "name": "RubberMan"},
                    // 	{"id": 17, "name": "Dynama"},
                    // 	{"id": 18, "name": "Dr IQ"},
                    // 	{"id": 19, "name": "Magma"},
                    // 	{"id": 20, "name": "Tornado"}
                    // ];		
                    return this.nameIds;
                };
                PrincipleInvestigatorService = __decorate([
                    core_1.Injectable(), 
                    __metadata('design:paramtypes', [name_id_list_service_1.NameIdListService])
                ], PrincipleInvestigatorService);
                return PrincipleInvestigatorService;
            }());
            exports_1("PrincipleInvestigatorService", PrincipleInvestigatorService);
        }
    }
});
//# sourceMappingURL=principle-investigator.service.js.map