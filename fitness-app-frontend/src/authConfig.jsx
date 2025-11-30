export const authConfig = {
  clientId: "oathu2-pcke-client",
  authorizationEndpoint:
    "https://localhost:8181/realms/fitness-oauth2/protocol/openid-connect/auth",
  tokenEndpoint:
    "https://localhost:8181/realms/fitness-oauth2/protocol/openid-connect/token",
  redirectUri: "http://localhost:5173",
  scope: "openid profile email offline_access",
  onRefreshTokenExpire: (event) => event.logIn(),
};
