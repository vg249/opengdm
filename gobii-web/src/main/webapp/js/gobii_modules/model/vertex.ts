import {EntityType} from "./type-entity";

export class Vertex {

    constructor(public vertexId:number,
                public vertexName:string,
                public entityType:EntityType,
                public cvGroupname:string,
                public filterVals:string[]
                ) {
    }
}