import {GobiiCropType} from  "./type-crop"

export class ServerConfig {
    
    constructor(public crop:GobiiCropType,
                public domain:string, 
                public port:number) {
        
        this.crop = crop;
        this.domain = domain;
        this.port = port;
    }
}