export class DtoHeaderAuth {

    public constructor(public userName:string,
                       public password:string,
                       public token:string) {
    }

    public getToken():string {
        return this.token;
    }


    // public fromJSON(json:JSON) {
    //     this.userName = json['userName'];
    //     this.password = json['password'];
    //     this.token = json['token'];
    // }
    //
    // public toJSON(): JSON {
    //     return {
    //         userName : this.userName,
    //         password: this.password,
    //         token: this.token
    //     };
    // }
}
