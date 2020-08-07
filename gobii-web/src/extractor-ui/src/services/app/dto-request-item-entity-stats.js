System.register(["@angular/core", "../../model/entity-stats", "../../model/type-entity"], function (exports_1, context_1) {
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
    var core_1, entity_stats_1, type_entity_1, EntityRequestType, DtoRequestItemEntityStats;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (entity_stats_1_1) {
                entity_stats_1 = entity_stats_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            }
        ],
        execute: function () {
            (function (EntityRequestType) {
                EntityRequestType[EntityRequestType["Count"] = 0] = "Count";
                EntityRequestType[EntityRequestType["ChildCountOfParent"] = 1] = "ChildCountOfParent";
                EntityRequestType[EntityRequestType["LasetUpdated"] = 2] = "LasetUpdated";
            })(EntityRequestType || (EntityRequestType = {}));
            exports_1("EntityRequestType", EntityRequestType);
            DtoRequestItemEntityStats = /** @class */ (function () {
                function DtoRequestItemEntityStats(requestType, entityType, parentEntityType, parentId) {
                    this.requestType = requestType;
                    this.entityType = entityType;
                    this.parentEntityType = parentEntityType;
                    this.parentId = parentId;
                }
                DtoRequestItemEntityStats.prototype.getUrl = function () {
                    var returnVal;
                    var baseUrl = "gobii/v1/";
                    var entityTypeName = type_entity_1.EntityType[this.entityType];
                    if (this.requestType === EntityRequestType.Count) {
                        returnVal = baseUrl + "entities/" + entityTypeName + "/count";
                    }
                    else if (this.requestType === EntityRequestType.ChildCountOfParent) {
                        var parentEntityTypeName = type_entity_1.EntityType[this.parentEntityType];
                        var parentIdAsString = this.parentId.toLocaleString();
                        returnVal = baseUrl + "entities/" + parentEntityTypeName + "/" + parentIdAsString + "/" + entityTypeName + "/count";
                    }
                    else {
                        returnVal = baseUrl + "entities/" + entityTypeName + "/lastmodified";
                    }
                    return returnVal;
                }; // getUrl()
                // this should be a GET only
                DtoRequestItemEntityStats.prototype.getRequestBody = function () {
                    return null;
                };
                DtoRequestItemEntityStats.prototype.resultFromJson = function (json) {
                    var returnVal;
                    var entityStatsFromServer = json.payload.data[0];
                    if (entityStatsFromServer) {
                        returnVal = new entity_stats_1.EntityStats(entityStatsFromServer.count, new Date(entityStatsFromServer.lastModified));
                    }
                    return returnVal;
                };
                DtoRequestItemEntityStats = __decorate([
                    core_1.Injectable(),
                    __metadata("design:paramtypes", [Number, Number, Number, Number])
                ], DtoRequestItemEntityStats);
                return DtoRequestItemEntityStats;
            }());
            exports_1("DtoRequestItemEntityStats", DtoRequestItemEntityStats);
        }
    };
});
//# sourceMappingURL=dto-request-item-entity-stats.js.map