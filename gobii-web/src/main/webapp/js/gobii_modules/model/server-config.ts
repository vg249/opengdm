import {GobiiCropType} from  "./type-crop"

export class ServerConfig {
    
    constructor(public crop:string,
                public domain:string, 
                public port:number) {
        
        this.crop = crop;
        this.domain = domain;
        this.port = port;
    }
}