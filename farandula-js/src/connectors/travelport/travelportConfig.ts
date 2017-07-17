export class TravelportConfig {
  private static user:string = 'Universal API/uAPI7099771471-91c9d403'
  private static password:string = 'G2EEYCxCrY7GjtwCrwR2js7Mf'
  static readonly branch:string = 'P7036596'
  static readonly hostname:string = 'americas.universal-api.pp.travelport.com'
  static readonly path:string = '/B2BGateway/connect/uAPI/AirService'

  public static getBasicAuth():string {
    return btoa(`${this.user}:${this.password}`)
  }
}