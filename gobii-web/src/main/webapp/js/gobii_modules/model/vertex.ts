import {EntityType} from "./type-entity";

export class Vertex {

    constructor(public vertexId:number,
                public vertexName:string,
                public gobiiEntityNameTypeName:String,
                public cvGroupname:string,
                public filterVals:string[]
                ) {
    }
}