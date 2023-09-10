export const environment = {
    backendurl: 'http://localhost:8080',
    SESSION_ATTRIBUTE_NAME:'sessionId',
    LOGGEDUSER:"userId",
    USERNAME:"userName",
    options : {
      headers: {
          "Content-Type": 'application/json',
          "Access-Control-Allow-Origin": '*',
          "Authorization": "",
          "userId":""
      },

      credentials: 'same-origin',
  }
  }; 