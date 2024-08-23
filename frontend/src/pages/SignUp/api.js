import { i18nInstance } from "../../locales";
import http from "../../lib/http";


export function signUp(body) {
  return http.post("/api/v1/users", body,{
    headers: {
      "Accept-Language":i18nInstance.language
    }
  });
}