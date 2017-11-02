import {EntityType} from "./type-entity";
export class NameId {

    constructor(public id: string,
                public name: string,
                public entityType: EntityType,
                public entityLasetModified: Date) {
        this.id = id;
        this.name = name;
        this.entityType = entityType;
        this.entityLasetModified = entityLasetModified;
    }
}
