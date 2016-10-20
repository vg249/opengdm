import {Header} from "./header"
import {Payload} from "./payload"

export class PayloadEnvelope {

    public constructor(public header:Header,
                       public payload:Payload) {}

    public static fromJSON(json:any):PayloadEnvelope {

        let header:Header = Header.fromJSON(json.header);
        let payload:Payload = Payload.fromJSON(json.payload);
        
        return new PayloadEnvelope(header,payload);

    } // fromJson()
}
